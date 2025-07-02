package ReservaCruzeiros.Reserva;

import ReservaCruzeiros.NovoMarketing.ControleIdsPromocao;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/reserva")
public class ReservaSse {

    private final Map<String, SseEmitter> emissores = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emissoresPromocao = new ConcurrentHashMap<>();

    @GetMapping(value = "/stream/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@PathVariable("clientId") String clientId) {
        SseEmitter emitter = new SseEmitter(null);
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
            } catch (IOException e) {
            }
        }
    }

    @GetMapping(value = "/streamPromocao/{idPromocao}/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamPromocao(
            @PathVariable("idPromocao") int idPromocao,
            @PathVariable("clientId") UUID clientId
    ) {
        boolean adicionado = ControleIdsPromocao.adicionarCliente(idPromocao, clientId);
        if (!adicionado) {
            System.out.printf("📌 Cliente %s já está inscrito na promoção %d\n", clientId, idPromocao);
        }

        String chave = gerarChavePromocao(idPromocao, clientId);
        SseEmitter emitter = new SseEmitter(null);
        emissoresPromocao.put(chave, emitter);

        emitter.onCompletion(() -> {
            System.out.printf("🛑 SSE completo para cliente %s na promoção %d\n", clientId, idPromocao);
            emissoresPromocao.remove(chave);
        });
        emitter.onTimeout(() -> {
            System.out.printf("⏰ SSE timeout para cliente %s na promoção %d\n", clientId, idPromocao);
            emissoresPromocao.remove(chave);
        });

        System.out.printf("📡 SSE iniciado para cliente %s na promoção %d\n", clientId, idPromocao);
        return emitter;
    }

    public void enviarPromocaoParaTodos(int idPromocao, String mensagem) {
        String prefixo = idPromocao + "-";

        emissoresPromocao.forEach((chave, emitter) -> {
            if (chave.startsWith(prefixo)) {
                try {
                    emitter.send(SseEmitter.event().data(mensagem));
                    System.out.printf("✅ Mensagem enviada para %s\n", chave);
                } catch (IOException e) {
                    System.out.printf("❌ Erro ao enviar mensagem para %s: %s\n", chave, e.getMessage());
                    emissoresPromocao.remove(chave);
                }
            }
        });
    }

    private String gerarChavePromocao(int idPromocao, UUID clientId) {
        return idPromocao + "-" + clientId.toString();
    }
}

