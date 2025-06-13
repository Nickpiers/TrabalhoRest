package ReservaCruzeiros.Pagamento;

import ReservaCruzeiros.Criptografia.Criptografia;
import ReservaCruzeiros.Service.RabbitMQMetodos;

public class PagamentoPublisher {

    public void processaPagamento(PagamentoDTO pagamentoDTO, String pagamento) throws Exception {
        if (pagamento.equals("aprovado")) {
            aprovaPagamento(pagamentoDTO);
        }
        else {
            recusaPagamento(pagamentoDTO);
        }
    };

    private static void aprovaPagamento(PagamentoDTO pagamentoDTO) throws Exception {
        RabbitMQMetodos.publisherExchange("pagamento-aprovado", "pagamento", null, null, pagamentoDTO);
    }

    private static void recusaPagamento(PagamentoDTO pagamentoDTO) throws Exception {
        RabbitMQMetodos.publisherQueue("pagamento-recusado", null, pagamentoDTO);
    }
}
