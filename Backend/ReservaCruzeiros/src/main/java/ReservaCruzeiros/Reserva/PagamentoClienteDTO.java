package ReservaCruzeiros.Reserva;

public class PagamentoClienteDTO {
    private int valor;
    private String moeda;
    private long idReserva;

    public PagamentoClienteDTO() {}

    public PagamentoClienteDTO(int valor, String moeda, long idReserva) {
        this.valor = valor;
        this.moeda = moeda;
        this.idReserva = idReserva;
    }

    public int getValor() {
        return valor;
    }

    public String getMoeda() {
        return moeda;
    }

    public long getIdReserva() {
        return idReserva;
    }
}
