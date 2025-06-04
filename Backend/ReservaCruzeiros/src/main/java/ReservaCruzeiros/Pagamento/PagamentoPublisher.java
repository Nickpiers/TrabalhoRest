package ReservaCruzeiros.Pagamento;

import ReservaCruzeiros.Criptografia.Criptografia;
import ReservaCruzeiros.Service.RabbitMQMetodos;

public class PagamentoPublisher {

    public void processaPagamento(String nomeCompleto, String pagamento) throws Exception {
        if (pagamento.equals("aprovado")) {
            aprovaPagamento(nomeCompleto);
        }
        else {
            recusaPagamento(nomeCompleto);
        }
    };

    private static void aprovaPagamento(String nomeCompleto) throws Exception {
        String mesangemCriptografada = Criptografia.criptografaMensagem(nomeCompleto);
        RabbitMQMetodos.publisherExchange("pagamento-aprovado", "pagamento", mesangemCriptografada);
    }

    private static void recusaPagamento(String nomeCompleto) throws Exception {
        String mensagemCriptografada = Criptografia.criptografaMensagem(nomeCompleto);
        RabbitMQMetodos.publisherQueue("pagamento-recusado", mensagemCriptografada);
    }
}
