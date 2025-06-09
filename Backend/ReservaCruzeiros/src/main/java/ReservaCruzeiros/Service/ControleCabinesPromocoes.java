package ReservaCruzeiros.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControleCabinesPromocoes {

    private static final int QUANTIDADE_MAXIMA_CABINES = 10;
    private static final Map<String, Map<String, Integer>> reservasPorCruzeiro = new ConcurrentHashMap<>();

    private static String chaveCruzeiro(int cruzeiro) {
        return "cruzeiro_" + cruzeiro;
    }

    public static synchronized boolean reservaCriada(int cruzeiro, String nome, int quantidadeSolicitadaCabines) {
        String chave = chaveCruzeiro(cruzeiro);
        reservasPorCruzeiro.putIfAbsent(chave, new ConcurrentHashMap<>());

        Map<String, Integer> reservasCruzeiro = reservasPorCruzeiro.get(chave);
        int cabinesReservadas = reservasCruzeiro.values().stream().mapToInt(Integer::intValue).sum();
        int disponiveis = QUANTIDADE_MAXIMA_CABINES - cabinesReservadas;

        if (quantidadeSolicitadaCabines <= 0) {
            System.out.println("❌ Quantidade inválida.");
            return false;
        }

        if (disponiveis >= quantidadeSolicitadaCabines) {
            System.out.printf("✅ Reserva possível para %s no cruzeiro %d\n", nome, cruzeiro);
            return true;
        } else {
            System.out.printf("❌ Reserva falhou para %s no cruzeiro %d. Cabines disponíveis: %d\n", nome, cruzeiro, disponiveis);
            return false;
        }
    }

    public static synchronized boolean reservaCancelada(int cruzeiro, String nome, int quantidadeCanceladaCabines) {
        String chave = chaveCruzeiro(cruzeiro);
        Map<String, Integer> reservasCruzeiro = reservasPorCruzeiro.get(chave);

        if (reservasCruzeiro == null || !reservasCruzeiro.containsKey(nome)) {
            System.out.printf("❌ Nenhuma reserva encontrada para %s no cruzeiro %d\n", nome, cruzeiro);
            return false;
        }

        if (quantidadeCanceladaCabines <= 0) {
            System.out.println("❌ Quantidade inválida.");
            return false;
        }

        int atual = reservasCruzeiro.get(nome);
        int nova = atual - quantidadeCanceladaCabines;

        if (nova > 0) {
            reservasCruzeiro.put(nome, nova);
        } else {
            reservasCruzeiro.remove(nome);
        }

        System.out.printf("✅ Reserva cancelada para %s no cruzeiro %d. Cabines disponíveis: %d\n", nome, cruzeiro, getCabinesDisponiveis(cruzeiro));
        return true;
    }

    public static synchronized void confirmaReserva(int cruzeiro, String nome, int quantidadeSolicitadaCabines) {
        String chave = chaveCruzeiro(cruzeiro);
        reservasPorCruzeiro.putIfAbsent(chave, new ConcurrentHashMap<>());
        Map<String, Integer> reservasCruzeiro = reservasPorCruzeiro.get(chave);

        reservasCruzeiro.merge(nome, quantidadeSolicitadaCabines, Integer::sum);
        System.out.printf("✅ Reserva criada para %s no cruzeiro %d. Cabines restantes: %d\n", nome, cruzeiro, getCabinesDisponiveis(cruzeiro));
    }

    public static int getCabinesDisponiveis(int cruzeiro) {
        String chave = chaveCruzeiro(cruzeiro);
        reservasPorCruzeiro.putIfAbsent(chave, new ConcurrentHashMap<>());
        int usadas = reservasPorCruzeiro.get(chave).values().stream().mapToInt(Integer::intValue).sum();
        return QUANTIDADE_MAXIMA_CABINES - usadas;
    }

    public static Map<String, Integer> getReservasDoCruzeiro(int cruzeiro) {
        String chave = chaveCruzeiro(cruzeiro);
        return reservasPorCruzeiro.getOrDefault(chave, new ConcurrentHashMap<>());
    }

    public static Map<String, Map<String, Integer>> getTodasReservas() {
        return reservasPorCruzeiro;
    }
}
