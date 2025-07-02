package ReservaCruzeiros.Pagamento;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/pagamento")
public class PagamentoWebHook{

    PagamentoPublisher pagamentoPublisher;

    @PostMapping("/gerarLinkExterno")
    public ResponseEntity<String> chamarOutroEndpoint(@RequestParam("idReserva") long idReserva) {
        return ResponseEntity.ok("Link para pagamento: https://ReservaCruzeiros.com/reserva/pagamento/" + idReserva);
    }

    public String aprovarPagamento(NotificacaoPagamentoDTO notificacao) throws InterruptedException {
        Thread.sleep(4000);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NotificacaoPagamentoDTO> request = new HttpEntity<>(notificacao, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:8080/pagamento/notificacao",
                request,
                String.class
        );

        return response.getBody();
    }

}

