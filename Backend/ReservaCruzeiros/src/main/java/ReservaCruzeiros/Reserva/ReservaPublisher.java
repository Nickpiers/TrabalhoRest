package ReservaCruzeiros.Reserva;

import ReservaCruzeiros.Service.ControleCabinesPromocoes;
import ReservaCruzeiros.Service.RabbitMQMetodos;

public class ReservaPublisher {
    public static boolean novaReserva(ReservaClientIdDTO reserva) throws Exception {
        RabbitMQMetodos.publisherExchange("reserva-criada", "pagamento", null, reserva, null);
        ReservaDto reservaDto = reserva.getReserva();
        return ControleCabinesPromocoes.reservaCriada(
                reservaDto.getIdCruzeiro(),
                reservaDto.getNomeCompleto(),
                reservaDto.getNumeroCabines()
        );
    };
}
