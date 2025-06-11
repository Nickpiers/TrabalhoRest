package ReservaCruzeiros.Pagamento;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamento")
public class PagamentoWebHook{

    PagamentoPublisher pagamentoPublisher;

    @PostMapping("/notificacao")
    public ResponseEntity<Void> receberNotificacao(@RequestBody NotificacaoPagamentoDTO notificacao) throws Exception {
        System.out.println("📬 Notificação recebida: " + notificacao);

        if ("aprovada".equalsIgnoreCase(notificacao.getStatus())) {
//            pagamentoPublisher.processaPagamento(notificacao.getIdReserva(), "aprovado");
        } else if ("recusada".equalsIgnoreCase(notificacao.getStatus())) {
//            pagamentoPublisher.processaPagamento(notificacao.getIdReserva(), "recusado");
        }

        return ResponseEntity.ok().build();
    }
}

