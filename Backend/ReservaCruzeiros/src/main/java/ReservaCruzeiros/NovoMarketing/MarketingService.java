package ReservaCruzeiros.NovoMarketing;

import org.springframework.stereotype.Service;

@Service
public class MarketingService {

    private final NovoMarketingReceiver novoMarketingReceiver;

    public MarketingService(NovoMarketingReceiver novoMarketingReceiver) {
        this.novoMarketingReceiver = novoMarketingReceiver;
    }

    public void inscreveNovoAssinante(int codPromocao) throws Exception {
        PromocaoTipo promocao = switch (codPromocao) {
            case 1 -> PromocaoTipo.PROMO_2_POR_1;
            case 2 -> PromocaoTipo.UPGRADE_VARANDA;
            case 3 -> PromocaoTipo.DESCONTO_GRUPOS;
            default -> throw new IllegalArgumentException("Código de promoção inválido: " + codPromocao);
        };

        String nome = promocao.getNome();
        String routingKey = promocao.getRoutingKey();
        novoMarketingReceiver.inscreveNaPromocao(nome, routingKey);
    }
}
