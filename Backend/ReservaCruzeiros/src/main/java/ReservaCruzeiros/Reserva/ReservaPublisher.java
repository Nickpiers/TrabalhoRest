package ReservaCruzeiros.Reserva;

import ReservaCruzeiros.Service.RabbitMQMetodos;

public class ReservaPublisher {
    public static void novaReserva(ReservaDto reserva) throws Exception {
        RabbitMQMetodos.publisherExchange("reserva-criada", "pagamento", null, reserva);
        Thread.sleep(2000);
        System.out.println("Processando pagamento...");
    };
}
