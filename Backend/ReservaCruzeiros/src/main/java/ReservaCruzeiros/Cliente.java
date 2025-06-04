package ReservaCruzeiros;

import ReservaCruzeiros.Menu.Menu;

// GERA .jar's:
// mvn clean package -Pcliente
// mvn package -PadminPagamento
// mvn package -PadminPromocao

// java -jar cliente.jar
// java -jar adminPagamento.jar
// java -jar adminPromocao.jar
public class Cliente {

    public static void main(String[] argv) throws Exception {
        Menu menu = new Menu();
        menu.start();
    }

}
