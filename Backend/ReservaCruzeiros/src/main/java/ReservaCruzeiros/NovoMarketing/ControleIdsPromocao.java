package ReservaCruzeiros.NovoMarketing;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ControleIdsPromocao {

    private static final Map<Integer, Set<UUID>> idsPorPromocao = new ConcurrentHashMap<>();

    public static synchronized boolean adicionarCliente(int idPromocao, UUID clienteId) {
        idsPorPromocao.putIfAbsent(idPromocao, ConcurrentHashMap.newKeySet());
        Set<UUID> clientes = idsPorPromocao.get(idPromocao);

        if (clientes.contains(clienteId)) {
            System.out.printf("⚠️ Cliente %s já está na promoção %d\n", clienteId, idPromocao);
            return false;
        }

        clientes.add(clienteId);
        System.out.printf("✅ Cliente %s adicionado à promoção %d\n", clienteId, idPromocao);
        return true;
    }

    public static synchronized boolean removerCliente(int idPromocao, UUID clienteId) {
        Set<UUID> clientes = idsPorPromocao.get(idPromocao);
        if (clientes != null && clientes.remove(clienteId)) {
            System.out.printf("❌ Cliente %s removido da promoção %d\n", clienteId, idPromocao);
            return true;
        }
        System.out.printf("⚠️ Cliente %s não encontrado na promoção %d\n", clienteId, idPromocao);
        return false;
    }

    public static Set<UUID> getClientesDaPromocao(int idPromocao) {
        return idsPorPromocao.getOrDefault(idPromocao, Collections.emptySet());
    }

    public static Map<Integer, Set<UUID>> getTodasPromocoes() {
        return idsPorPromocao;
    }

    public static synchronized void limparPromocao(int idPromocao) {
        idsPorPromocao.remove(idPromocao);
        System.out.printf("🗑️ Todos os clientes removidos da promoção %d\n", idPromocao);
    }

    public static boolean clienteParticipaDaPromocao(int idPromocao, UUID clienteId) {
        Set<UUID> clientes = idsPorPromocao.get(idPromocao);
        return clientes != null && clientes.contains(clienteId);
    }
}
