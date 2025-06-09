package ReservaCruzeiros.Marketing;

import ReservaCruzeiros.Service.RabbitMQMetodos;

public class MarketingPublisher {
    public static void publicaPromocao(String promocaoDescricao, String routingKey) throws Exception {
        RabbitMQMetodos.publisherExchange("promocoes-destino", routingKey, promocaoDescricao, null);
    }
}
