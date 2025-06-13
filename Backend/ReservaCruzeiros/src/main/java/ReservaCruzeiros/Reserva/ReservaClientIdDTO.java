package ReservaCruzeiros.Reserva;

public class ReservaClientIdDTO {
    private ReservaDto reserva;
    private String clientId;
    private long idReserva;

    public ReservaClientIdDTO() {}

    public ReservaClientIdDTO(ReservaDto reserva, String clientId, long idReserva) {
        this.reserva = reserva;
        this.clientId = clientId;
        this.idReserva = idReserva;
    }

    public ReservaDto getReserva() {
        return reserva;
    }

    public String getClientId() {
        return clientId;
    }

    public long getIdReserva() {
        return idReserva;
    }
}

