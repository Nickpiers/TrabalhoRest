package ReservaCruzeiros.Service;

import ReservaCruzeiros.Reserva.ReservaInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControleCabinesPromocoes {

    private static final int QUANTIDADE_MAXIMA_CABINES = 10;
    private static final Map<String, Map<String, ReservaInfo>> reservasPorCruzeiro = new ConcurrentHashMap<>();
    private static final Map<Long, ReservaInfo> reservasPorId = new ConcurrentHashMap<>();

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

    public static synchronized boolean reservaCanceladaPorId(long idReserva) {
        ReservaInfo reserva = reservasPorId.get(idReserva);

        if (reserva == null) {
            System.out.printf("❌ Nenhuma reserva encontrada com id %d\n", idReserva);
            return false;
        }

        for (Map.Entry<String, Map<String, ReservaInfo>> entryCruzeiro : reservasPorCruzeiro.entrySet()) {
            Map<String, ReservaInfo> reservasCruzeiro = entryCruzeiro.getValue();

            for (Map.Entry<String, ReservaInfo> entryReserva : reservasCruzeiro.entrySet()) {
                if (entryReserva.getValue().getIdReserva() == idReserva) {
                    String nome = entryReserva.getKey();
                    String chaveCruzeiro = entryCruzeiro.getKey();

                    reservasCruzeiro.remove(nome);
                    reservasPorId.remove(idReserva);

                    int cruzeiroNum = Integer.parseInt(chaveCruzeiro.replace("cruzeiro_", ""));
                    System.out.printf("✅ Reserva de %s no cruzeiro %d cancelada completamente. Cabines disponíveis: %d\n",
                            nome, cruzeiroNum, getCabinesDisponiveis(cruzeiroNum));
                    return true;
                }
            }
        }

        System.out.println("❌ Inconsistência: reserva encontrada no índice mas não na estrutura principal.");
        return false;
    }

    public static synchronized void confirmaReserva(int cruzeiro, String nome, int quantidadeSolicitadaCabines, long idReserva) {
        String chave = chaveCruzeiro(cruzeiro);
        reservasPorCruzeiro.putIfAbsent(chave, new ConcurrentHashMap<>());
        Map<String, ReservaInfo> reservasCruzeiro = reservasPorCruzeiro.get(chave);

        ReservaInfo novaReserva = new ReservaInfo(idReserva, quantidadeSolicitadaCabines);
        reservasCruzeiro.merge(nome, novaReserva, (existente, nova) -> {
            existente.incrementarCabines(nova.getQuantidadeCabines());
            return existente;
        });

        reservasPorId.put(idReserva, reservasCruzeiro.get(nome));

        System.out.printf("✅ Reserva criada para %s no cruzeiro %d. Cabines restantes: %d\n",
                nome, cruzeiro, getCabinesDisponiveis(cruzeiro));
    }

    public static ReservaInfo getReservaPorId(long idReserva) {
        return reservasPorId.get(idReserva);
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
