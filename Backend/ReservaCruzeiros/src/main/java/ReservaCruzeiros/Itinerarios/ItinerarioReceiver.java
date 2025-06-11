package ReservaCruzeiros.Itinerarios;

import ReservaCruzeiros.Reserva.ReservaClientIdDTO;
import ReservaCruzeiros.Reserva.ReservaDto;
import ReservaCruzeiros.Reserva.ReservaSse;
import ReservaCruzeiros.Service.ControleCabinesPromocoes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class ItinerarioReceiver {

    @Autowired
    private ReservaSse reservaSse;

    @PostConstruct
    public void inicializaAguardaNovaReserva() throws Exception {
        final String exchangeName = "reserva-criada";
        final String queueName = "fila-pagamento-receiver";
        final String routingKey = "pagamento";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "direct");
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            ReservaClientIdDTO reservaComClientId = mapper.readValue(json, ReservaClientIdDTO.class);

            ReservaDto reserva = reservaComClientId.getReserva();
            String clientId = reservaComClientId.getClientId();

            boolean sucesso = ControleCabinesPromocoes.reservaCriada(
                    reserva.getIdCruzeiro(),
                    reserva.getNomeCompleto(),
                    reserva.getNumeroCabines()
            );

            if (sucesso) {
                try {
                    UUID uuid = UUID.randomUUID();
                    long idReserva = uuid.getMostSignificantBits() & Long.MAX_VALUE;
                    String link = gerarLinkPagamento(idReserva);
                    reservaSse.enviarLink(clientId, link);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("❌ Reserva falhou. Sem cabines disponíveis.");
            }

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }

    private String gerarLinkPagamento(long idReserva) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://localhost:8080/pagamento/gerarLink?idReserva=" + idReserva, null, String.class);
    }
}
