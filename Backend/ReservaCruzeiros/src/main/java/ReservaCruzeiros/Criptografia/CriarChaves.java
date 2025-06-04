package ReservaCruzeiros.Criptografia;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

public class CriarChaves {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---------------------------");
        System.out.println("Qual chave deseja modificar?: ");
        System.out.println("1 - Chave original");
        System.out.println("2 - Chave fake");
        System.out.println("---------------------------");
        int codMenu = scanner.nextInt();

        criarChaves(codMenu);
    }

    private static void criarChaves(int codEscolhido) throws Exception {
        String privateKeyFileName;
        String publicKeyFileName;
        if (codEscolhido == 1) {
            privateKeyFileName = "private.key";
            publicKeyFileName = "public.key";
        } else {
            privateKeyFileName = "privateFake.key";
            publicKeyFileName = "publicFake.key";
        }
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        PublicKey pub = pair.getPublic();

        Files.write(Paths.get(privateKeyFileName), priv.getEncoded());
        Files.write(Paths.get(publicKeyFileName), pub.getEncoded());
    }
}
