package ReservaCruzeiros.Reserva;

import java.io.Serializable;

public class ReservaDto implements Serializable {

    String nomeCompleto;
    String dataEmbarque;
    int numeroPassageiros;
    int numeroCabines;

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
