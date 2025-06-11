package ReservaCruzeiros.NovoMarketing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.stereotype.Component;

@Component
public class NovoMarketingReceiver {

    private final MarketingSse marketingSse;

    public NovoMarketingReceiver(MarketingSse marketingSse) {
        this.marketingSse = marketingSse;
    }

    public void inscreveNaPromocao(String promocaoNome, String routingKey) throws Exception {
        final String exchangeName = "promocoes-destino";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = channel.queueDeclare().getQueue();

        channel.exchangeDeclare(exchangeName, "direct");
        channel.queueBind(queueName, exchangeName, routingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String codPromocao = new String(delivery.getBody(), "UTF-8");
            int idPromocao = Integer.parseInt(codPromocao);

            System.out.printf("📨 Promoção recebida: %d\n", idPromocao);
            marketingSse.enviarPromocaoParaTodos(idPromocao, "🎉 promoção recebida");

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }
}
