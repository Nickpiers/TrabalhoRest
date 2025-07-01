package ReservaCruzeiros.NovoMarketing;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/marketing")
public class MarketingSse {

    private final Map<String, SseEmitter> emissores = new ConcurrentHashMap<>();

    @GetMapping(value = "/stream/{idPromocao}/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(
            @PathVariable("idPromocao") int idPromocao,
            @PathVariable("clientId") UUID clientId
    ) {
        boolean adicionado = ControleIdsPromocao.adicionarCliente(idPromocao, clientId);
        if (!adicionado) {
            System.out.printf("📌 Cliente %s já está inscrito na promoção %d\n", clientId, idPromocao);
        }

        String chave = gerarChave(idPromocao, clientId);
        SseEmitter emitter = new SseEmitter(null);
        emissores.put(chave, emitter);

        emitter.onCompletion(() -> {
            System.out.printf("🛑 SSE completo para cliente %s na promoção %d\n", clientId, idPromocao);
            emissores.remove(chave);
        });
        emitter.onTimeout(() -> {
            System.out.printf("⏰ SSE timeout para cliente %s na promoção %d\n", clientId, idPromocao);
            emissores.remove(chave);
        });

        System.out.printf("📡 SSE iniciado para cliente %s na promoção %d\n", clientId, idPromocao);
        return emitter;
    }

    private String gerarChave(int idPromocao, UUID clientId) {
        return idPromocao + "-" + clientId.toString();
    }

    public void enviarPromocaoParaTodos(int idPromocao, String mensagem) {
        String prefixo = idPromocao + "-";

        emissores.forEach((chave, emitter) -> {
            if (chave.startsWith(prefixo)) {
                try {
                    emitter.send(SseEmitter.event().data(mensagem));
                    System.out.printf("✅ Mensagem enviada para %s\n", chave);
                } catch (IOException e) {
                    System.out.printf("❌ Erro ao enviar mensagem para %s: %s\n", chave, e.getMessage());
                    emitter.completeWithError(e);
                    emissores.remove(chave);
                }
            }
        });
    }
}
