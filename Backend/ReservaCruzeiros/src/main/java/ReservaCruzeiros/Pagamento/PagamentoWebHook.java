package ReservaCruzeiros.Pagamento;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamento")
public class PagamentoWebHook{

    PagamentoPublisher pagamentoPublisher;

    @PostMapping("/gerarLinkExterno")
    public ResponseEntity<String> chamarOutroEndpoint(@RequestParam("idReserva") long idReserva) {
        return ResponseEntity.ok("Link para pagamento: https://ReservaCruzeiros.com/reserva/pagamento/" + idReserva);
    }

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

