package ReservaCruzeiros.Menu;

import ReservaCruzeiros.Service.Service;

import java.util.Scanner;

public class Menu {
    private static final Service service = new Service();

    public void start() throws Exception {
        Scanner scanner = new Scanner(System.in);
        int codMenu;

        do {
            System.out.println("---------------------------");
            System.out.println("O que deseja fazer?: ");
            System.out.println("1 - Reserva");
            System.out.println("2 - Inscrever");
            System.out.println("0 - Sair");
            System.out.println("---------------------------");
            codMenu = scanner.nextInt();
            service.limparConsole();

            switch (codMenu) {
                case 1:
                    mostrarReservas(scanner);
                    break;
                case 2:
                    chamaPromocao(scanner);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (codMenu != 0);

        scanner.close();
    }

    private void mostrarReservas(Scanner scanner) throws Exception {
        scanner.nextLine();

        System.out.println("Destino: ");
        String destino = scanner.nextLine();
        System.out.println("Data de embarque: ");
        String dataEmbarque = scanner.nextLine();
        System.out.println("Porto de embarque: ");
        String portoEmbarque = scanner.nextLine();
        System.out.println();

        int codEscolhido;
        do {
            codEscolhido = Itinerarios.exibir(scanner, destino, dataEmbarque, portoEmbarque);
            if (codEscolhido == 1 || codEscolhido == 2) {
                criarReserva(scanner);
            } else if (codEscolhido != 0) {
                System.out.println("Código invalido!");
            }

        } while (codEscolhido < 0 || codEscolhido > 2);

    }

    private void criarReserva(Scanner scanner) throws Exception {
        service.limparConsole();

        scanner.nextLine();
        System.out.print("Digite seu nome completo : ");
        String nomeCompleto = scanner.nextLine();
        System.out.print("Digite a data de embarque: ");
        String dataEmbarque = scanner.nextLine();
        System.out.print("Digite o numero de passageiros: ");
        int numeroPassageiros = scanner.nextInt();
        System.out.print("Digite o numero de cabines: ");
        int numeroCabines = scanner.nextInt();

        service.inicializaReceivers();
//        ReservaDto reserva = new ReservaDto(nomeCompleto, dataEmbarque, numeroPassageiros, numeroCabines);
//        ReservaPublisher.novaReserva(reserva);
        Thread.sleep(5000);
    }

    private void chamaPromocao(Scanner scanner) throws Exception {
        int codPromocao;
        do {
            service.limparConsole();

            System.out.println("---------------------------");
            System.out.println("Em qual promoção deseja se inscrever?: ");
            System.out.println("1 - 2 por 1");
            System.out.println("2 - Upgrade varanda");
            System.out.println("3 - Desconto grupos");
            System.out.println("0 - Voltar ao menu");
            System.out.println("---------------------------");
            codPromocao = scanner.nextInt();
        } while (codPromocao < 0 || codPromocao > 3);
    }
}
