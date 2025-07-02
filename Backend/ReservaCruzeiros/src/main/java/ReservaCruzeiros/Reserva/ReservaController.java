package ReservaCruzeiros.Reserva;

import ReservaCruzeiros.Itinerarios.ConsultaDTO;
import ReservaCruzeiros.NovoMarketing.ControleIdsPromocao;
import ReservaCruzeiros.NovoMarketing.MarketingService;
import ReservaCruzeiros.Service.ControleCabinesPromocoes;
import ReservaCruzeiros.Service.CriarCruzeiro;
import ReservaCruzeiros.Service.RabbitMQMetodos;
import ReservaCruzeiros.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final MarketingService marketingService;

    @Autowired
    private ReservaSse reservaSse;

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
    public ResponseEntity<Boolean> cancelarReserva(@RequestBody long idReserva) {
        boolean sucesso = ControleCabinesPromocoes.reservaCanceladaPorId(idReserva);
        if (sucesso) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/consultarReserva")
    public ResponseEntity<?> consultarReserva(@RequestBody long idReserva) {
        ReservaInfo reserva = ControleCabinesPromocoes.getReservaPorId(idReserva);

        if (reserva != null) {
            return ResponseEntity.ok(reserva);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Reserva com ID " + idReserva + " não encontrada.");
        }
    }

    @PostMapping("/inscreverPromocao")
    public ResponseEntity<Map<String, String>> inscreverPromocao(@RequestBody int codPromocao) throws Exception {
        marketingService.inscreveNovoAssinante(codPromocao);
        return ResponseEntity.ok(Map.of("mensagem", "Inscrito na promocao " + codPromocao + ", com sucesso!"));
    }

    @PostMapping("/cancelarPromocao")
    public ResponseEntity<Map<String, String>> cancelarPromocao(@RequestBody CancelarInscricaoDto dto) {
        UUID clienteId = UUID.fromString(dto.getClientId());
        int idPromocao = dto.getIdPromocao();

        boolean removido = ControleIdsPromocao.removerCliente(idPromocao, clienteId);

        if (removido) {
            reservaSse.cancelarSSEPromocao(idPromocao, clienteId);

            return ResponseEntity.ok(Map.of(
                    "mensagem", "Inscrição na promoção " + idPromocao + ", cancelada com sucesso!"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensagem", "Cliente não inscrito nesta promoção."));
        }
    }

    @GetMapping("/testeConexao")
    public ResponseEntity<String> testarConexao() throws Exception {
        final Service service = new Service();
        service.inicializaReceivers();
        return ResponseEntity.ok("Conexão bem-sucedida com o backend!");
    }

    @PostMapping("/pagamento")
    public ResponseEntity<String> pagamentoReserva(@RequestBody PagamentoClienteDTO pagamento) throws Exception {
        RabbitMQMetodos.publisherExchange("pagamento-para-aprovar", "pagamento", null, null, null);
        return ResponseEntity.ok("Promocao cancelada!");
    }
}

