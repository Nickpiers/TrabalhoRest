package ReservaCruzeiros;

import ReservaCruzeiros.NovoMarketing.PromocaoTipo;
import ReservaCruzeiros.NovoMarketing.NovoMarketingPublisher;

import java.util.Scanner;

public class AdminPromocao {
    public static void main(String[] argv) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int codPromocao;
        do {
            System.out.println("---------------------------");
            System.out.println("Qual promoção deseja enviar?: ");
            System.out.println("1 - 2 por 1");
            System.out.println("2 - Upgrade varanda");
            System.out.println("3 - Desconto pra grupos");
            System.out.println("0 - Sair");
            System.out.println("---------------------------");
            codPromocao = scanner.nextInt();

            PromocaoTipo promocao = switch (codPromocao) {
                case 1 -> PromocaoTipo.PROMO_2_POR_1;
                case 2 -> PromocaoTipo.UPGRADE_VARANDA;
                case 3 -> PromocaoTipo.DESCONTO_GRUPOS;
                default -> null;
            };

            if (codPromocao > 0 && codPromocao < 4) NovoMarketingPublisher.publicaPromocao(codPromocao, promocao.getRoutingKey());
            else if (codPromocao == 0) {
                System.out.println("Saindo...");
            } else {
                System.out.println("Código promoção inválida!");
            }
        }while(codPromocao != 0);
    }
}
