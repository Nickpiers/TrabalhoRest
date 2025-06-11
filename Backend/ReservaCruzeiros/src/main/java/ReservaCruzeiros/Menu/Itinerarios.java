package ReservaCruzeiros.Menu;

import ReservaCruzeiros.Service.CriarCruzeiro;
import ReservaCruzeiros.Service.Service;

import java.util.List;
import java.util.Scanner;

public class Itinerarios {
    public static int exibir(Scanner scanner, String destino, String dataEmbarque, String portoEmbarque) {
        Service service = new Service();
        service.limparConsole();

        System.out.println("---------------------------");
        System.out.println("Itinerários disponíveis:");

        System.out.println("1 - Navio: Cruzeiro Azul");
        System.out.println("    Partida: " + dataEmbarque);
        System.out.println("    Porto embarque: " + portoEmbarque);
        System.out.println("    Porto desembarque: Santos");
        System.out.println("    Lugares visitados: " + destino + ", Ilhabela, Angra dos Reis");
        System.out.println("    Duração: 7 noites");
        System.out.println("    Valor por pessoa: R$ 3.500");
        System.out.println();

        System.out.println("2 - Navio: Estrela do Mar");
        System.out.println("    Partida: " + dataEmbarque);
        System.out.println("    Porto embarque: " + portoEmbarque);
        System.out.println("    Porto desembarque: Salvador");
        System.out.println("    Lugares visitados: Recife, " + destino + ", Maceió, Natal");
        System.out.println("    Duração: 10 noites");
        System.out.println("    Valor por pessoa: R$ 4.800");
        System.out.println();

        System.out.println("0 - Voltar ao menu");
        System.out.println("---------------------------");

        int codEscolhido = scanner.nextInt();

        return codEscolhido;
    }

    public List<CriarCruzeiro> mostrarItinerarios(String destino, String dataEmbarque, String portoEmbarque) {
        CriarCruzeiro criador = new CriarCruzeiro();
        List<CriarCruzeiro> cruzeiros = criador.criar();

        return cruzeiros.stream()
                .filter(c -> (destino == null || c.getLugaresVisitados().toLowerCase().contains(destino.toLowerCase())) &&
                        (dataEmbarque == null || c.getDatasDisponiveis().toLowerCase().contains(dataEmbarque.toLowerCase())) &&
                        (portoEmbarque == null || c.getPortoEmbarque().toLowerCase().contains(portoEmbarque.toLowerCase())))
                .toList();
    }



}
