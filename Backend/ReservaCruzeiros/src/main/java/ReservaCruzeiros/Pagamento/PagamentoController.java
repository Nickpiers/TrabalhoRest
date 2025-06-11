package ReservaCruzeiros.Pagamento;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PagamentoController {

    @PostMapping("/pagamento/gerarLink")
    public ResponseEntity<String> chamarOutroEndpoint(@RequestParam("idReserva") long idReserva) {
        return ResponseEntity.ok("Link para pagamento: https://ReservaCruzeiros.com/reserva/pagamento/" + idReserva);
    }
}
