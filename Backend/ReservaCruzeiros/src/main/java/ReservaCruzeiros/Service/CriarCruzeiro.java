package ReservaCruzeiros.Service;

import java.util.ArrayList;
import java.util.List;

public class CriarCruzeiro {

    String datasDisponiveis;
    String nomeNavio;
    String portoEmbarque;
    String lugaresVisitados;
    int numeroNoites;
    int valorPorPessoa;
    int idCruzeiro;

    public CriarCruzeiro(String datasDisponiveis, String nomeNavio, String portoEmbarque,
                         String lugaresVisitados, int numeroNoites, int valorPorPessoa, int idCruzeiro) {
        this.datasDisponiveis = datasDisponiveis;
        this.nomeNavio = nomeNavio;
        this.portoEmbarque = portoEmbarque;
        this.lugaresVisitados = lugaresVisitados;
        this.numeroNoites = numeroNoites;
        this.valorPorPessoa = valorPorPessoa;
        this.idCruzeiro = idCruzeiro;
    }

    public CriarCruzeiro() {}

    @Override
    public String toString() {
        return "Cruzeiro \"" + nomeNavio + "\" saindo de " + portoEmbarque +
                " em " + datasDisponiveis + " com duração de " + numeroNoites + " noites.\n" +
                "Visitando: " + lugaresVisitados + "\nValor por pessoa: R$" + valorPorPessoa + "\n";
    }

    public List<CriarCruzeiro> criar() {
        List<CriarCruzeiro> lista = new ArrayList<>();

        lista.add(new CriarCruzeiro(
                "10 de julho", "Navio Estelar", "Rio de Janeiro",
                "Búzios, Salvador, Recife", 5, 3500, 1
        ));

        lista.add(new CriarCruzeiro(
                "01 de Agosto", "Mar Azul", "Santos",
                "Ilhabela, Florianópolis, Punta del Este", 7, 4200, 2
        ));

        lista.add(new CriarCruzeiro(
                "20 de dezembro", "Oceano Real", "Fortaleza",
                "Fernando de Noronha, Natal, João Pessoa", 7, 4800, 3
        ));

        return lista;
    }

    public String getDatasDisponiveis() {
        return datasDisponiveis;
    }

    public String getNomeNavio() {
        return nomeNavio;
    }

    public String getPortoEmbarque() {
        return portoEmbarque;
    }

    public String getLugaresVisitados() {
        return lugaresVisitados;
    }

    public int getNumeroNoites() {
        return numeroNoites;
    }

    public int getValorPorPessoa() {
        return valorPorPessoa;
    }

    public int getIdCruzeiro() {
        return idCruzeiro;
    }

}
