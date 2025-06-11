package ReservaCruzeiros.Reserva;

public class ReservaClientIdDTO {
    private ReservaDto reserva;
    private String clientId;

    public ReservaClientIdDTO() {}

    public ReservaClientIdDTO(ReservaDto reserva, String clientId) {
        this.reserva = reserva;
        this.clientId = clientId;
    }

    public ReservaDto getReserva() {
        return reserva;
    }

    public String getClientId() {
        return clientId;
    }
}

