package ReservaCruzeiros.Reserva;

import ReservaCruzeiros.Criptografia.Criptografia;
import ReservaCruzeiros.Service.ControleCabinesPromocoes;
import ReservaCruzeiros.Service.Service;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ReservaReceiver {
    private static Channel canalPagamentoAprovado;
    private static String tagPagamentoAprovado;

    private static Channel canalPagamentoRecusado;
    private static String tagPagamentoRecusado;

    private static Channel canalBilheteGerado;
    private static String tagBilheteGerado;

    public static void inicializaAguardaPagamento() throws Exception {
        receiverPagamentoAprovado();
        receiverPagamentoRecusado();
        receiverBilheteGerado();
        receiverReservaCancelada();
    }

    private static void receiverPagamentoAprovado() throws Exception {
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
                String jsonStr = new String(delivery.getBody(), "UTF-8");
                JSONObject json = new JSONObject(jsonStr);

                String mensagemBase64 = json.getString("mensagem");
                byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);

                boolean verificada = Criptografia.verificaMensagem(json);

                if (verificada) {
                    String nomeCompleto = new String(mensagemBytes, "UTF-8");
                    ControleCabinesPromocoes.confirmaReserva(1, nomeCompleto, 1, 2);
                    System.out.println("✅ Assinatura verificada. Pagamento de '" + nomeCompleto + "' foi aprovado!");

                } else {
                    System.out.println("❌ Assinatura inválida! Pagamento possivelmente adulterado.");
                }

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + e.getMessage());
                e.printStackTrace();
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };


        tagPagamentoAprovado = channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
        canalPagamentoAprovado = channel;
    }

    private static void receiverReservaCancelada() throws Exception {
        final String exchangeName = "cancelamento-reserva";
        final String queueName = "cancela-reserva";
        final String routingKey = "cancelamento";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "direct");
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String jsonStr = new String(delivery.getBody(), "UTF-8");
                JSONObject json = new JSONObject(jsonStr);

                String mensagemBase64 = json.getString("mensagem");
                byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);

                boolean verificada = Criptografia.verificaMensagem(json);

                if (verificada) {
                    String nomeCompleto = new String(mensagemBytes, "UTF-8");

                    boolean sucesso = ControleCabinesPromocoes.reservaCancelada(1, nomeCompleto, 1);
                    if (sucesso) {
                        System.out.println("✅ Reserva cancelada para: " + nomeCompleto);
                    } else {
                        System.out.println("❌ Reserva não existe");
                    }

                } else {
                    System.out.println("❌ Assinatura inválida! Pagamento possivelmente adulterado.");
                }

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + e.getMessage());
                e.printStackTrace();
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };


        tagPagamentoAprovado = channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
        canalPagamentoAprovado = channel;
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

        tagPagamentoRecusado = channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
        canalPagamentoRecusado = channel;
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

        tagBilheteGerado = channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
        canalBilheteGerado = channel;
    }

    public static void pararReservaReceivers() throws IOException {
        Service.pararReceiver(canalPagamentoAprovado, tagPagamentoAprovado);
        Service.pararReceiver(canalPagamentoRecusado, tagPagamentoRecusado);
        Service.pararReceiver(canalBilheteGerado, tagBilheteGerado);
    }
}