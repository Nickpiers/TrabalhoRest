package ReservaCruzeiros.Bilhete;

import ReservaCruzeiros.Pagamento.PagamentoDTO;
import ReservaCruzeiros.Service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class BilheteReceiver {
    private static final String EXCHANGE_NAME = "pagamento-aprovado";
    private static final String QUEUE_NAME = "fila-bilhete";
    private static final String ROUTING_KEY = "pagamento";

    private static Channel canalPagamento;
    private static String tagPagamento;

    public static void inicializaAguardaPagamentoAprovado() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String body = new String(delivery.getBody(), StandardCharsets.UTF_8);
                ObjectMapper mapper = new ObjectMapper();
                PagamentoDTO pagamentoDTO = mapper.readValue(body, PagamentoDTO.class);
                String nomeCompleto = pagamentoDTO.getReservaClientIdDTO().getReserva().getNomeCompleto();

                BilhetePublisher bilhetePublisher = new BilhetePublisher();
                bilhetePublisher.geraBilhete(nomeCompleto);

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + e.getMessage());
                e.printStackTrace();
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        tagPagamento = channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});
        canalPagamento = channel;
    }

    public static void pararPagamentoAprovado() throws Exception {
        Service.pararReceiver(canalPagamento, tagPagamento);
    }
}
