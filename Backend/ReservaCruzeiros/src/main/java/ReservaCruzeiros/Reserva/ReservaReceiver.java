package ReservaCruzeiros.Reserva;

import ReservaCruzeiros.Criptografia.Criptografia;
import ReservaCruzeiros.Pagamento.PagamentoDTO;
import ReservaCruzeiros.Service.ControleCabinesPromocoes;
import ReservaCruzeiros.Service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class ReservaReceiver {

    @Autowired
    private ReservaSse reservaSse;

    public void inicializaAguardaPagamento() throws Exception {
        receiverPagamentoAprovado();
        receiverPagamentoRecusado();
        receiverBilheteGerado();
    }

    private void receiverPagamentoAprovado() throws Exception {
        final String exchangeName = "pagamento-aprovado";
        final String queueName = "fila-reserva";
        final String routingKey = "pagamento";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "direct");
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String body = new String(delivery.getBody(), StandardCharsets.UTF_8);
                ObjectMapper mapper = new ObjectMapper();
                PagamentoDTO pagamentoDTO = mapper.readValue(body, PagamentoDTO.class);
                String nomeCompleto = pagamentoDTO.getReservaClientIdDTO().getReserva().getNomeCompleto();
                int cruzeiro = pagamentoDTO.getReservaClientIdDTO().getReserva().getIdCruzeiro();
                int quantidadeCabines = pagamentoDTO.getReservaClientIdDTO().getReserva().getNumeroCabines();
                long idReserva = pagamentoDTO.getIdReserva();

                ControleCabinesPromocoes.confirmaReserva(cruzeiro, nomeCompleto, quantidadeCabines, idReserva);
                System.out.println("✅ Assinatura verificada. Pagamento de '" + nomeCompleto + "' foi aprovado!");

                reservaSse.pagamentoAprovado(pagamentoDTO.getReservaClientIdDTO().getClientId(), nomeCompleto);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + e.getMessage());
                e.printStackTrace();
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

       channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }

    private static void receiverPagamentoRecusado() throws Exception {
        final String queueName = "pagamento-recusado";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String jsonStr = new String(delivery.getBody(), "UTF-8");
                JSONObject json = new JSONObject(jsonStr);

                String mensagemBase64 = json.getString("mensagem");
                byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);

                boolean verificada = Criptografia.verificaMensagem(json);

                if (verificada) {
                    String nomeCompleto = new String(mensagemBytes, "UTF-8");
                    System.out.println("❌ Pagamento de '" + nomeCompleto + "' recusado!");
                } else {
                    System.out.println("❌ Assinatura inválida! Pagamento possivelmente adulterado.");
                }

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + e.getMessage());
                e.printStackTrace();
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }

    private static void receiverBilheteGerado() throws Exception {
        final String queueName = "bilhete-gerado";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String nomeCompleto = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("✅ Bilhete de '" + nomeCompleto + "' gerado!");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }
}