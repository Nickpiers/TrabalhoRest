package ReservaCruzeiros.Service;

import ReservaCruzeiros.Bilhete.BilheteReceiver;
import ReservaCruzeiros.Pagamento.PagamentoReceiver;
import ReservaCruzeiros.Reserva.ReservaReceiver;
import com.rabbitmq.client.Channel;

import java.io.IOException;

public class Service {
    public void inicializaReceivers() throws Exception {
        ReservaReceiver.inicializaAguardaPagamento();
        PagamentoReceiver.inicializaAguardaNovaReserva();
        BilheteReceiver.inicializaAguardaPagamentoAprovado();
    }

    public void paraTodosReceivers() throws Exception {
        ReservaReceiver.pararReservaReceivers();
        BilheteReceiver.pararPagamentoAprovado();
        PagamentoReceiver.pararNovaReserva();
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
