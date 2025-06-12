package ReservaCruzeiros.Pagamento;

import ReservaCruzeiros.Reserva.ReservaClientIdDTO;
import ReservaCruzeiros.Reserva.ReservaDto;
import ReservaCruzeiros.Service.RabbitMQMetodos;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AprovaPagamentoAdmin {
    private static void aprovaPagamento(ReservaClientIdDTO reserva, long idReserva, int codAprovacao) throws Exception {
        boolean aprovado = codAprovacao == 1;
        String aprovadoStr = Boolean.toString(aprovado);
        PagamentoDTO pagamentoDTO = new PagamentoDTO(reserva, aprovadoStr, idReserva);

        RabbitMQMetodos.publisherQueue("aprova-pagamento", null, pagamentoDTO);
    };

    public static void recebeReservaCriada() throws Exception {
        final String exchangeName = "reserva-criada";
        final String queueName = "fila-admin-pagamento";
        final String routingKey = "pagamento";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, "direct");
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            ReservaClientIdDTO reservaComClientId = mapper.readValue(json, ReservaClientIdDTO.class);

            try {
                aprovarPagamento(reservaComClientId, reservaComClientId.getIdReserva());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }

    private static void aprovarPagamento(ReservaClientIdDTO reserva, long idReserva) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int codAprovacao;
        do {
            System.out.println("---------------------------");
            System.out.println("1 - Aprovar pagamento");
            System.out.println("2 - Recusar pagamento");
            System.out.println("---------------------------");
            codAprovacao = scanner.nextInt();
        }while(codAprovacao != 1 && codAprovacao != 2);

        System.out.println("APROVADO/RECUSADO ENVIADO!");
        AprovaPagamentoAdmin.aprovaPagamento(reserva, idReserva, codAprovacao);
    }
}
