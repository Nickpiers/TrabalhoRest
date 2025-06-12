package ReservaCruzeiros.Service;

import ReservaCruzeiros.Pagamento.PagamentoDTO;
import ReservaCruzeiros.Reserva.ReservaClientIdDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class RabbitMQMetodos {
    public static void publisherQueue(String queueName, String mensagem, PagamentoDTO pagamentoDTO) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);

            byte[] payload;

            if (pagamentoDTO != null) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(pagamentoDTO);
                payload = json.getBytes("UTF-8");
            } else {
                payload = mensagem.getBytes("UTF-8");
            }

            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, payload);
        }
    }

    public static void publisherExchange(String exchangeName, String routingKey, String mensagem, ReservaClientIdDTO reserva, PagamentoDTO pagamentoDTO) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(exchangeName, "direct");

            byte[] payload;

            if (pagamentoDTO != null) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(pagamentoDTO);
                payload = json.getBytes("UTF-8");
            }
            else if (reserva != null) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(reserva);
                payload = json.getBytes("UTF-8");
            } else {
                payload = mensagem.getBytes("UTF-8");
            }

            channel.basicPublish(exchangeName, routingKey, null, payload);
        }
    }
}
