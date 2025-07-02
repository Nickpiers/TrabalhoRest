package ReservaCruzeiros.Service;

import ReservaCruzeiros.Bilhete.BilheteReceiver;
import ReservaCruzeiros.Reserva.ReservaReceiver;
import com.rabbitmq.client.Channel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Service {

    @Autowired
    private ReservaReceiver reservaReceiver;

    @PostConstruct
    public void inicializaReceivers() throws Exception {
        reservaReceiver.inicializaAguardaPagamento();
        BilheteReceiver.inicializaAguardaPagamentoAprovado();
    }

    public static void pararReceiver(Channel canal, String tag) throws IOException {
        if (canal != null && tag != null) {
            canal.basicCancel(tag);
        }
    }

    public void limparConsole() {
        try {
            String sistema = System.getProperty("os.name");
            if (sistema.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Não foi possível limpar o console");
        }
    }
}
