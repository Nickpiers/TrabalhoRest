package ReservaCruzeiros.Reserva;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/reserva")
public class ReservaSse {

    private final Map<String, SseEmitter> emissores = new ConcurrentHashMap<>();

    @GetMapping(value = "/stream/{nomeCompleto}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@PathVariable("nomeCompleto") String nomeCompleto) {
        System.out.println(nomeCompleto);
        SseEmitter emitter = new SseEmitter(30000L);
        emissores.put(nomeCompleto, emitter);

        emitter.onCompletion(() -> emissores.remove(nomeCompleto));
        emitter.onTimeout(() -> emissores.remove(nomeCompleto));
        return emitter;
    }

    public void enviarLink(String nomeCompleto, String link) {
        System.out.println("Link para pagamento: " + nomeCompleto);
        SseEmitter emitter = emissores.get(nomeCompleto);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(link));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }
}

