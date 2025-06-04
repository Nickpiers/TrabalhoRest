package ReservaCruzeiros.Bilhete;

import ReservaCruzeiros.Service.RabbitMQMetodos;

public class BilhetePublisher {
    public void geraBilhete(String nomeCompleto) throws Exception {
        RabbitMQMetodos.publisherQueue("bilhete-gerado", nomeCompleto);
    };
}
