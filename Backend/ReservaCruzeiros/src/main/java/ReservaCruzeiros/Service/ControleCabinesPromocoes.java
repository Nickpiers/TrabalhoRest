package ReservaCruzeiros.Service;

import ReservaCruzeiros.Reserva.ReservaInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControleCabinesPromocoes {

    private static final int QUANTIDADE_MAXIMA_CABINES = 10;
    private static final Map<String, Map<String, ReservaInfo>> reservasPorCruzeiro = new ConcurrentHashMap<>();

    private static String chaveCruzeiro(int cruzeiro) {
        return "cruzeiro_" + cruzeiro;
    }

    public static synchronized boolean reservaCriada(int cruzeiro, String nome, int quantidadeSolicitadaCabines) {
        String chave = chaveCruzeiro(cruzeiro);
        reservasPorCruzeiro.putIfAbsent(chave, new ConcurrentHashMap<>());

        Map<String, ReservaInfo> reservasCruzeiro = reservasPorCruzeiro.get(chave);
        int cabinesReservadas = reservasCruzeiro.values().stream()
                .mapToInt(ReservaInfo::getQuantidadeCabines)
                .sum();
        int disponiveis = QUANTIDADE_MAXIMA_CABINES - cabinesReservadas;

        if (quantidadeSolicitadaCabines <= 0) {
            return false;
        }

        return disponiveis >= quantidadeSolicitadaCabines;
    }

    public static synchronized boolean reservaCancelada(int cruzeiro, String nome , int quantidadeCanceladaCabines) {
        String chave = chaveCruzeiro(cruzeiro);
        Map<String, ReservaInfo> reservasCruzeiro = reservasPorCruzeiro.get(chave);

        if (reservasCruzeiro == null || !reservasCruzeiro.containsKey(nome)) {
            System.out.printf("❌ Nenhuma reserva encontrada para %s no cruzeiro %d\n", nome, cruzeiro);
            return false;
        }

        if (quantidadeCanceladaCabines <= 0) {
            System.out.println("❌ Quantidade inválida.");
            return false;
        }

        ReservaInfo reserva = reservasCruzeiro.get(nome);
        int novaQuantidade = reserva.getQuantidadeCabines() - quantidadeCanceladaCabines;

        if (novaQuantidade > 0) {
            reserva.setQuantidadeCabines(novaQuantidade);
        } else {
            reservasCruzeiro.remove(nome);
        }

        System.out.printf("✅ Reserva cancelada para %s no cruzeiro %d. Cabines disponíveis: %d\n",
                nome, cruzeiro, getCabinesDisponiveis(cruzeiro));
        return true;
    }

    public static synchronized void confirmaReserva(int cruzeiro, String nome, int quantidadeSolicitadaCabines, long idReserva) {
        String chave = chaveCruzeiro(cruzeiro);
        reservasPorCruzeiro.putIfAbsent(chave, new ConcurrentHashMap<>());
        Map<String, ReservaInfo> reservasCruzeiro = reservasPorCruzeiro.get(chave);

        reservasCruzeiro.merge(nome,
                new ReservaInfo(idReserva, quantidadeSolicitadaCabines),
                (existente, nova) -> {
                    existente.incrementarCabines(nova.getQuantidadeCabines());
                    return existente;
                });

        System.out.printf("✅ Reserva criada para %s no cruzeiro %d. Cabines restantes: %d\n",
                nome, cruzeiro, getCabinesDisponiveis(cruzeiro));
    }

    public static int getCabinesDisponiveis(int cruzeiro) {
        String chave = chaveCruzeiro(cruzeiro);
        reservasPorCruzeiro.putIfAbsent(chave, new ConcurrentHashMap<>());
        int usadas = reservasPorCruzeiro.get(chave).values().stream()
                .mapToInt(ReservaInfo::getQuantidadeCabines)
                .sum();
        return QUANTIDADE_MAXIMA_CABINES - usadas;
    }

    public static Map<String, ReservaInfo> getReservasDoCruzeiro(int cruzeiro) {
        String chave = chaveCruzeiro(cruzeiro);
        return reservasPorCruzeiro.getOrDefault(chave, new ConcurrentHashMap<>());
    }

    public static Map<String, Map<String, ReservaInfo>> getTodasReservas() {
        return reservasPorCruzeiro;
    }
}
