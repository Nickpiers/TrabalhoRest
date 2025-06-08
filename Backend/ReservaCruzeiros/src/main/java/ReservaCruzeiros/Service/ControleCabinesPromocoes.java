package ReservaCruzeiros.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControleCabinesPromocoes {

    private static final int QUANTIDADE_MAXIMA_CABINES = 10;
    private static final Map<String, Integer> reservas = new ConcurrentHashMap<>();

    public static synchronized boolean reservaCriada(String nome, int quantidadeSolicitadaCabines) {
        int cabinesReservadas = reservas.values().stream().mapToInt(Integer::intValue).sum();
        int disponiveis = QUANTIDADE_MAXIMA_CABINES - cabinesReservadas;

        if (quantidadeSolicitadaCabines <= 0) {
            System.out.println("Quantidade inválida.");
            return false;
        }

        if (disponiveis >= quantidadeSolicitadaCabines) {
            reservas.merge(nome, quantidadeSolicitadaCabines, Integer::sum);
            System.out.println("Reserva criada para " + nome + ". Cabines restantes: " + getCabinesDisponiveis());
            return true;
        } else {
            System.out.println("Reserva falhou para " + nome + ". Cabines disponíveis: " + disponiveis);
            return false;
        }
    }

    public static synchronized boolean reservaCancelada(String nome, int quantidadeCanceladaCabines) {
        if (!reservas.containsKey(nome)) {
            System.out.println("Nenhuma reserva encontrada para " + nome);
            return false;
        }

        if (quantidadeCanceladaCabines <= 0) {
            System.out.println("Quantidade inválida.");
            return false;
        }

        int atual = reservas.get(nome);
        int nova = atual - quantidadeCanceladaCabines;

        if (nova > 0) {
            reservas.put(nome, nova);
        } else {
            reservas.remove(nome);
        }

        System.out.println("Reserva cancelada para " + nome + ". Cabines disponíveis: " + getCabinesDisponiveis());
        return true;
    }

    public static int getCabinesDisponiveis() {
        int usadas = reservas.values().stream().mapToInt(Integer::intValue).sum();
        return QUANTIDADE_MAXIMA_CABINES - usadas;
    }

    public static Map<String, Integer> getTodasReservas() {
        return reservas;
    }
}
