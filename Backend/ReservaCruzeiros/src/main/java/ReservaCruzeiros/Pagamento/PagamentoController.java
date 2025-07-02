package ReservaCruzeiros.Pagamento;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class PagamentoController {

    @PostMapping("/pagamento/gerarLink")
    public ResponseEntity<Map<String, String>> chamarOutroEndpoint(@RequestParam("idReserva") long idReserva) {
        RestTemplate restTemplate = new RestTemplate();

        String resposta = restTemplate.postForObject(
                "http://localhost:8080/pagamento/gerarLinkExterno?idReserva=" + idReserva,
                null,
                String.class
        );

        return ResponseEntity.ok(Map.of("mensagem", resposta));
    }

}
