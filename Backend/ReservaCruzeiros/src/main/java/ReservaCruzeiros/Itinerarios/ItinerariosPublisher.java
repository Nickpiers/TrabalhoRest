package ReservaCruzeiros.Itinerarios;

import ReservaCruzeiros.Menu.Itinerarios;
import ReservaCruzeiros.Service.CriarCruzeiro;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itinerarios")
public class ItinerariosPublisher {

    @PostMapping("/consultar")
    public ResponseEntity<?> status(@RequestBody ConsultaDTO consulta) {
        Itinerarios itinerarios = new Itinerarios();

        String destino = consulta.getDestino();
        String dataEmbarque = consulta.getDataEmbarque();
        String portoEmbarque = consulta.getPortoEmbarque();

        List<CriarCruzeiro> resultados = itinerarios.mostrarItinerarios(destino, dataEmbarque, portoEmbarque);

        if (resultados.isEmpty()) {
            return ResponseEntity.status(404).body("❌ Nenhum cruzeiro encontrado com os critérios informados.");
        }

        return ResponseEntity.ok(resultados);
    }

}
