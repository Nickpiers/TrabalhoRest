package ReservaCruzeiros.Pagamento;

import ReservaCruzeiros.Service.ControleCabinesPromocoes;
import ReservaCruzeiros.Service.Service;
import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class PagamentoReceiver {

    private static Channel canalNovaReserva;
    private static String tagNovaReserva;

    public static void inicializaAguardaNovaReserva() throws Exception {
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
            String nomeCompleto = new String(delivery.getBody(), StandardCharsets.UTF_8);

            boolean sucesso = ControleCabinesPromocoes.reservaCriada("caribe", nomeCompleto, 1);
            if (sucesso) {
                try {
                    aguardaAprovacao(nomeCompleto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("❌ Reserva falhou. Sem cabines disponíveis.");
            }

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        tagNovaReserva = channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
        canalNovaReserva = channel;
    }

    public static void pararNovaReserva() throws Exception {
        Service.pararReceiver(canalNovaReserva, tagNovaReserva);
    }

    private static void aguardaAprovacao(String nomeCompleto) throws Exception {
        String queueName = "aprova-pagamento";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String body = new String(delivery.getBody(), StandardCharsets.UTF_8);
            boolean aprovado = Boolean.parseBoolean(body);
            String aprovacao = aprovado ? "aprovado" : "recusado";

            try {
                PagamentoPublisher pagamentoPublisher = new PagamentoPublisher();
                pagamentoPublisher.processaPagamento(nomeCompleto, aprovacao);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });
    }
}
