package ReservaCruzeiros.Marketing;

public class MarketingService {
    public static void inscreveNovoAssinante(int codPromocao) throws Exception {
        PromocaoTipo promocao = switch (codPromocao) {
            case 1 -> PromocaoTipo.PROMO_2_POR_1;
            case 2 -> PromocaoTipo.UPGRADE_VARANDA;
            case 3 -> PromocaoTipo.DESCONTO_GRUPOS;
            default -> throw new IllegalArgumentException("Código de promoção inválido: " + codPromocao);
        };

        String nome = promocao.getNome();
        String routingKey = promocao.getRoutingKey();
        MarketingReceiver.inscreveNaPromocao(nome, routingKey);
    }
}
