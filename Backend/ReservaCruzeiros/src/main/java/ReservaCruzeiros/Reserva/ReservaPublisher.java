package ReservaCruzeiros.Reserva;

import ReservaCruzeiros.Service.RabbitMQMetodos;

public class ReservaPublisher {
    public static void novaReserva(ReservaDto reserva) throws Exception {
        String nomeCompleto = reserva.getNomeCompleto();

        String nomeJunto = nomeCompleto.replaceAll("\\s+", "");
        System.out.println("---------------------------");
        System.out.println("Link para pagamento: https://ReservaCruzeiros.com/reserva/pagamento/" + nomeJunto);
        RabbitMQMetodos.publisherExchange("reserva-criada", "pagamento", nomeCompleto);
        Thread.sleep(2000);
        System.out.println("Processando pagamento...");
    };
}
