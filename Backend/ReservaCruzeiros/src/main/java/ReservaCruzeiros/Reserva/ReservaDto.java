package ReservaCruzeiros.Reserva;

public class ReservaDto {

    String nomeCompleto;
    String dataEmbarque;
    int numeroPassageiros;
    int numeroCabines;
    int idCruzeiro;

    public ReservaDto() {}

    public ReservaDto( String nomeCompleto, String dataEmbarque, int numeroPassageiros, int numeroCabines, int idCruzeiro) {
        this.nomeCompleto = nomeCompleto;
        this.dataEmbarque = dataEmbarque;
        this.numeroPassageiros = numeroPassageiros;
        this.numeroCabines = numeroCabines;
        this.idCruzeiro = idCruzeiro;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getDataEmbarque() {
        return dataEmbarque;
    }

    public int getNumeroPassageiros() {
        return numeroPassageiros;
    }

    public int getNumeroCabines() {
        return numeroCabines;
    }

    public int getIdCruzeiro() {
        return idCruzeiro;
    }
}
