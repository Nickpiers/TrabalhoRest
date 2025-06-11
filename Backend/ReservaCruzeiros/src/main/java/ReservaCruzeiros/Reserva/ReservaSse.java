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

    @GetMapping(value = "/stream/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@PathVariable("clientId") String clientId) {
        SseEmitter emitter = new SseEmitter(30000L);
        emissores.put(clientId, emitter);

        emitter.onCompletion(() -> emissores.remove(clientId));
        emitter.onTimeout(() -> emissores.remove(clientId));
        return emitter;
    }

    public void enviarLink(String clientId, String link) {
        SseEmitter emitter = emissores.get(clientId);
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

