package ReservaCruzeiros.Reserva;

public class ReservaInfo {
    private long idReserva;
    private int quantidadeCabines;

    public ReservaInfo(long idReserva, int quantidadeCabines) {
        this.idReserva = idReserva;
        this.quantidadeCabines = quantidadeCabines;
    }

    public long getIdReserva() {
        return idReserva;
    }

    public int getQuantidadeCabines() {
        return quantidadeCabines;
    }

    public void setQuantidadeCabines(int quantidadeCabines) {
        this.quantidadeCabines = quantidadeCabines;
    }

    public void incrementarCabines(int quantidade) {
        this.quantidadeCabines += quantidade;
    }
}

