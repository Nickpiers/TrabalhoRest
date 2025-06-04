package ReservaCruzeiros.Pagamento;

import ReservaCruzeiros.Service.RabbitMQMetodos;
import com.rabbitmq.client.*;

import java.util.Scanner;

public class AprovaPagamentoAdmin {
    private static void aprovaPagamento(int codAprovacao) throws Exception {
        boolean aprovado = codAprovacao == 1;
        String aprovadoStr = Boolean.toString(aprovado);

        RabbitMQMetodos.publisherQueue("aprova-pagamento", aprovadoStr);
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
            try {
                aprovarPagamento();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
    }

    private static void aprovarPagamento() throws Exception {
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
        AprovaPagamentoAdmin.aprovaPagamento(codAprovacao);
    }
}
