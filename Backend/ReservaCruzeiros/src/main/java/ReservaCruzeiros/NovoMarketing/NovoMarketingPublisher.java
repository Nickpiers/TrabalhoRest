package ReservaCruzeiros.NovoMarketing;

import ReservaCruzeiros.Service.RabbitMQMetodos;

public class NovoMarketingPublisher {
    public static void publicaPromocao(int codPromocao, String routingKey) throws Exception {
        String codString = String.valueOf(codPromocao);
        RabbitMQMetodos.publisherExchange("promocoes-destino", routingKey, codString, null, null);
    }
}
