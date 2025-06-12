package ReservaCruzeiros.Reserva;

import ReservaCruzeiros.Itinerarios.ConsultaDTO;
import ReservaCruzeiros.NovoMarketing.MarketingService;
import ReservaCruzeiros.Service.CriarCruzeiro;
import ReservaCruzeiros.Service.RabbitMQMetodos;
import ReservaCruzeiros.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final MarketingService marketingService;

    @Autowired
    public ReservaController(MarketingService marketingService) {
        this.marketingService = marketingService;
    }

    @PostMapping("/itinerarios")
    public ResponseEntity<List<CriarCruzeiro>> chamarOutroEndpoint(@RequestBody ConsultaDTO consulta) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/itinerarios/consultar";

        ResponseEntity<CriarCruzeiro[]> response = restTemplate.postForEntity(url, consulta, CriarCruzeiro[].class);
        CriarCruzeiro[] arrayCruzeiros = response.getBody();

        if (arrayCruzeiros == null || arrayCruzeiros.length == 0) {
            return ResponseEntity.status(404).build();
        }

        List<CriarCruzeiro> listaCruzeiros = List.of(arrayCruzeiros);

        return ResponseEntity.ok(listaCruzeiros);
    }


    @PostMapping("/criarReserva")
    public ResponseEntity<?> criarReserva(@RequestBody ReservaClientIdDTO reserva) throws Exception {
        if (ReservaPublisher.novaReserva(reserva)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "É possível criar reserva");
            return ResponseEntity.ok(response);
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "Falha ao criar reserva");
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @PostMapping("/cancelarReserva")
    public ResponseEntity<String> cancelarReserva(@RequestBody String reserva) {
        return ResponseEntity.ok("Reserva cancelada!");
    }

    @PostMapping("/inscreverPromocao")
    public ResponseEntity<String> inscreverPromocao(@RequestBody int codPromocao) throws Exception {
        marketingService.inscreveNovoAssinante(codPromocao);
        return ResponseEntity.ok("Inscrito na promocao!");
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

