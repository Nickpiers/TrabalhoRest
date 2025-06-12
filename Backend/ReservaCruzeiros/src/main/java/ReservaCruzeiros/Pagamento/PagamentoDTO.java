package ReservaCruzeiros.Pagamento;

import ReservaCruzeiros.Reserva.ReservaClientIdDTO;

public class PagamentoDTO {
    ReservaClientIdDTO reservaClientIdDTO;
    String aprovado;
    long idReserva;

    public PagamentoDTO() {}

    public PagamentoDTO(ReservaClientIdDTO reservaClientIdDTO, String aprovado, long idReserva) {
        this.reservaClientIdDTO = reservaClientIdDTO;
        this.aprovado = aprovado;
        this.idReserva = idReserva;
    }

    public ReservaClientIdDTO getReservaClientIdDTO() {
        return reservaClientIdDTO;
    }

    public String getAprovado() {
        return aprovado;
    }

    public long getIdReserva() {
        return idReserva;
    }
}
