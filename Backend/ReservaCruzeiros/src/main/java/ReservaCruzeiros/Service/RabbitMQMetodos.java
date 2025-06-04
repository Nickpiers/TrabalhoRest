package ReservaCruzeiros.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class RabbitMQMetodos {
    public static void publisherQueue(String queueName, String mensagem) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);

            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, mensagem.getBytes("UTF-8"));
        }
    }

    public static void publisherExchange(String exchangeName, String routingKey, String mensagem) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(exchangeName, "direct");

            channel.basicPublish(exchangeName, routingKey, null, mensagem.getBytes("UTF-8"));
        }
    }
}
