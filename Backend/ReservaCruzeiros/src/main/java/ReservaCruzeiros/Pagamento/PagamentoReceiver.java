package ReservaCruzeiros.Pagamento;

import ReservaCruzeiros.Reserva.ReservaClientIdDTO;
import ReservaCruzeiros.Reserva.ReservaDto;
import ReservaCruzeiros.Service.ControleCabinesPromocoes;
import ReservaCruzeiros.Service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class PagamentoReceiver {

    @PostConstruct
    public void inicializaAguardaNovaReserva() throws Exception {
        final String exchangeName = "reserva-criada";
        final String queueName = "fila-pagamento-receiver";
        final String routingKey = "pagamento";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "direct");
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            ReservaClientIdDTO reservaComClientId = mapper.readValue(json, ReservaClientIdDTO.class);

            ReservaDto reserva = reservaComClientId.getReserva();
            String clientId = reservaComClientId.getClientId();

            boolean sucesso = ControleCabinesPromocoes.reservaCriada(
                    reserva.getIdCruzeiro(),
                    reserva.getNomeCompleto(),
                    reserva.getNumeroCabines()
            );
            if (sucesso) {
                try {
                    aguardaAprovacao(reserva.getNomeCompleto());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("❌ Reserva falhou. Sem cabines disponíveis.");
            }

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }

    private void aguardaAprovacao(String nomeCompleto) throws Exception {
        String queueName = "aprova-pagamento";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String body = new String(delivery.getBody(), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            PagamentoDTO pagamentoDTO = mapper.readValue(body, PagamentoDTO.class);

            ReservaClientIdDTO reserva = pagamentoDTO.getReservaClientIdDTO();
            ReservaDto reservaDto = reserva.getReserva();
            String aprovadoStr = pagamentoDTO.getAprovado();
            boolean aprovado = Boolean.parseBoolean(aprovadoStr);
            long idReserva = pagamentoDTO.getIdReserva();

            String aprovacao = aprovado ? "aprovado" : "recusado";

            try {
                PagamentoPublisher pagamentoPublisher = new PagamentoPublisher();
                pagamentoPublisher.processaPagamento(pagamentoDTO, aprovacao);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
    }
}
