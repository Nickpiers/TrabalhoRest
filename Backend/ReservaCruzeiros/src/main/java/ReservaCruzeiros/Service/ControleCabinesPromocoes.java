package ReservaCruzeiros.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControleCabinesPromocoes {

    private static final int QUANTIDADE_MAXIMA_CABINES = 10;
    private static final Map<String, Map<String, Integer>> reservasPorCruzeiro = new ConcurrentHashMap<>();

    public static synchronized boolean reservaCriada(String cruzeiro, String nome, int quantidadeSolicitadaCabines) {
        reservasPorCruzeiro.putIfAbsent(cruzeiro, new ConcurrentHashMap<>());

        Map<String, Integer> reservasCruzeiro = reservasPorCruzeiro.get(cruzeiro);

        int cabinesReservadas = reservasCruzeiro.values().stream().mapToInt(Integer::intValue).sum();
        int disponiveis = QUANTIDADE_MAXIMA_CABINES - cabinesReservadas;

        if (quantidadeSolicitadaCabines <= 0) {
            System.out.println("Quantidade inválida.");
            return false;
        }

        if (disponiveis >= quantidadeSolicitadaCabines) {
            System.out.printf("Reserva é possivel");
            return true;
        } else {
            System.out.printf("Reserva falhou para %s no cruzeiro %s. Cabines disponíveis: %d\n", nome, cruzeiro, disponiveis);
            return false;
        }
    }

    public static synchronized boolean reservaCancelada(String cruzeiro, String nome, int quantidadeCanceladaCabines) {
        Map<String, Integer> reservasCruzeiro = reservasPorCruzeiro.get(cruzeiro);

        if (reservasCruzeiro == null || !reservasCruzeiro.containsKey(nome)) {
            System.out.printf("Nenhuma reserva encontrada para %s no cruzeiro %s\n", nome, cruzeiro);
            return false;
        }

        if (quantidadeCanceladaCabines <= 0) {
            System.out.println("Quantidade inválida.");
            return false;
        }

        int atual = reservasCruzeiro.get(nome);
        int nova = atual - quantidadeCanceladaCabines;

        if (nova > 0) {
            reservasCruzeiro.put(nome, nova);
        } else {
            reservasCruzeiro.remove(nome);
        }

        System.out.printf("Reserva cancelada para %s no cruzeiro %s. Cabines disponíveis: %d\n", nome, cruzeiro, getCabinesDisponiveis(cruzeiro));
        return true;
    }

    public static synchronized void confirmaReserva(String cruzeiro, String nome, int quantidadeSolicitadaCabines) {
        Map<String, Integer> reservasCruzeiro = reservasPorCruzeiro.get(cruzeiro);

        reservasCruzeiro.merge(nome, quantidadeSolicitadaCabines, Integer::sum);
        System.out.printf("Reserva criada para %s no cruzeiro %s. Cabines restantes: %d\n", nome, cruzeiro, getCabinesDisponiveis(cruzeiro));
    }

    public static int getCabinesDisponiveis(String cruzeiro) {
        reservasPorCruzeiro.putIfAbsent(cruzeiro, new ConcurrentHashMap<>());
        int usadas = reservasPorCruzeiro.get(cruzeiro).values().stream().mapToInt(Integer::intValue).sum();
        return QUANTIDADE_MAXIMA_CABINES - usadas;
    }

    public static Map<String, Integer> getReservasDoCruzeiro(String cruzeiro) {
        return reservasPorCruzeiro.getOrDefault(cruzeiro, new ConcurrentHashMap<>());
    }

    public static Map<String, Map<String, Integer>> getTodasReservas() {
        return reservasPorCruzeiro;
    }
}
