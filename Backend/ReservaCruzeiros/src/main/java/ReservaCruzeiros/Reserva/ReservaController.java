package ReservaCruzeiros.Reserva;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @GetMapping("/itinerarios")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Itinerarios!");
    }

    @PostMapping("/criarReserva")
    public ResponseEntity<String> criarReserva(@RequestBody String reserva) {
        // Envia reserva para RabbitMQ
        // Exemplo: rabbitTemplate.convertAndSend(...);
        return ResponseEntity.ok("Reserva criada!");
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
    public ResponseEntity<String> testarConexao() {
        return ResponseEntity.ok("Conexão bem-sucedida com o backend!");
    }
}

