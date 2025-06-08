package ReservaCruzeiros.Reserva;

public class ReservaDto {

    String nomeCompleto;
    String dataEmbarque;
    int numeroPassageiros;
    int numeroCabines;

    public ReservaDto() {}

    public ReservaDto( String nomeCompleto, String dataEmbarque, int numeroPassageiros, int numeroCabines) {
        this.nomeCompleto = nomeCompleto;
        this.dataEmbarque = dataEmbarque;
        this.numeroPassageiros = numeroPassageiros;
        this.numeroCabines = numeroCabines;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }
}
