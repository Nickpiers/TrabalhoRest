package ReservaCruzeiros.Bilhete;

import ReservaCruzeiros.Criptografia.Criptografia;
import ReservaCruzeiros.Service.Service;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONObject;

import java.util.Base64;

public class BilheteReceiver {
    private static final String EXCHANGE_NAME = "pagamento-aprovado";
    private static final String QUEUE_NAME = "fila-bilhete";
    private static final String ROUTING_KEY = "pagamento";

    private static Channel canalPagamento;
    private static String tagPagamento;

    public static void inicializaAguardaPagamentoAprovado() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String jsonStr = new String(delivery.getBody(), "UTF-8");
                JSONObject json = new JSONObject(jsonStr);

                String mensagemBase64 = json.getString("mensagem");
                byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);

                boolean verificada = Criptografia.verificaMensagem(json);

                if (verificada) {
                    String nomeCompleto = new String(mensagemBytes, "UTF-8");
                    BilhetePublisher bilhetePublisher = new BilhetePublisher();
                    bilhetePublisher.geraBilhete(nomeCompleto);
                }

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (Exception e) {
                System.err.println("Erro ao processar mensagem: " + e.getMessage());
                e.printStackTrace();
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        tagPagamento = channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});
        canalPagamento = channel;
    }

    public static void pararPagamentoAprovado() throws Exception {
        Service.pararReceiver(canalPagamento, tagPagamento);
    }
}
