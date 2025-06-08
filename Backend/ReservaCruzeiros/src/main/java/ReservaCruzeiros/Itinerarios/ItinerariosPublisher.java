package ReservaCruzeiros.Itinerarios;

import ReservaCruzeiros.Menu.Itinerarios;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itinerarios")
public class ItinerariosPublisher {

    @PostMapping("/consultar")
    public ResponseEntity<String> status(@RequestBody ConsultaDTO consulta) {
        Itinerarios itinerarios = new Itinerarios();

        System.out.println("consulta: " + consulta);
        String destino = consulta.getDestino();
        String dataEmbarque = consulta.getDataEmbarque();
        String portoEmbarque = consulta.getPortoEmbarque();

        itinerarios.mostrarItinerarios(destino, dataEmbarque, portoEmbarque);

        return ResponseEntity.ok("Consulta realizada com sucesso!");
    }
}
