package ReservaCruzeiros.Reserva;

import ReservaCruzeiros.Itinerarios.ConsultaDTO;
import ReservaCruzeiros.Service.RabbitMQMetodos;
import ReservaCruzeiros.Service.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @PostMapping("/itinerarios")
    public ResponseEntity<String> chamarOutroEndpoint(@RequestBody ConsultaDTO consulta) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/itinerarios/consultar";
        String resposta = restTemplate.postForObject(url, consulta, String.class);

        return ResponseEntity.ok("Resposta interna: " + resposta);
    }

    @PostMapping("/criarReserva")
    public ResponseEntity<?> criarReserva(@RequestBody ReservaClientIdDTO reserva) throws Exception {
        if (ReservaPublisher.novaReserva(reserva)) {
            return ResponseEntity.ok().body("É possível criar reserva");
        };
        return ResponseEntity.badRequest().body("Falha ao criar reserva");
    }

    @PostMapping("/cancelarReserva")
    public ResponseEntity<String> cancelarReserva(@RequestBody String reserva) {
        return ResponseEntity.ok("Reserva cancelada!");
    }

    @PostMapping("/inscreverPromocao")
    public ResponseEntity<String> inscreverPromocao(@RequestBody String promocao) {
        return ResponseEntity.ok("Promocao acionada!");
    }

    @PostMapping("/cancelarPromocao")
    public ResponseEntity<String> cancelarPromocao(@RequestBody String promocao) {
        return ResponseEntity.ok("Promocao cancelada!");
    }

    @GetMapping("/testeConexao")
    public ResponseEntity<String> testarConexao() throws Exception {
        final Service service = new Service();
        service.inicializaReceivers();
        return ResponseEntity.ok("Conexão bem-sucedida com o backend!");
    }

    @PostMapping("/pagamento")
    public ResponseEntity<String> pagamentoReserva(@RequestBody PagamentoClienteDTO pagamento) throws Exception {
        RabbitMQMetodos.publisherExchange("pagamento-para-aprovar", "pagamento", null, null);
        return ResponseEntity.ok("Promocao cancelada!");
    }
}

