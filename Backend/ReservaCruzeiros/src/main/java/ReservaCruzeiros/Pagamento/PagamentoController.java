package ReservaCruzeiros.Pagamento;

import ReservaCruzeiros.Reserva.ReservaClientIdDTO;
import ReservaCruzeiros.Service.RabbitMQMetodos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class PagamentoController {

    @PostMapping("/pagamento/gerarLink")
    public ResponseEntity<Map<String, String>> chamarOutroEndpoint(@RequestBody ReservaClientIdDTO reservaDto) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        long idReserva = reservaDto.getIdReserva();

        String resposta = restTemplate.postForObject(
                "http://localhost:8080/pagamento/gerarLinkExterno?idReserva=" + idReserva,
                null,
                String.class
        );

        PagamentoDTO pagamentoDTO = new PagamentoDTO(reservaDto, "aprovado", idReserva);
        NotificacaoPagamentoDTO notificacaoPagamentoDTO = new NotificacaoPagamentoDTO(idReserva, "aprovada", pagamentoDTO);

        CompletableFuture.runAsync(() -> {
            try {
                PagamentoWebHook webHook = new PagamentoWebHook();
                webHook.aprovarPagamento(notificacaoPagamentoDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return ResponseEntity.ok(Map.of("mensagem", resposta));
    }

    @PostMapping("/pagamento/notificacao")
    public ResponseEntity<Void> receberNotificacao(@RequestBody NotificacaoPagamentoDTO notificacao) throws Exception {
        if ("aprovada".equalsIgnoreCase(notificacao.getStatus())) {
            RabbitMQMetodos.publisherExchange("pagamento-aprovado", "pagamento", null, null, notificacao.getPagamento());
        }
        return ResponseEntity.ok().build();
    }
}
