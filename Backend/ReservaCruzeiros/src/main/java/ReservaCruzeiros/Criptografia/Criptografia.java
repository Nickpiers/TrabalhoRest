package ReservaCruzeiros.Criptografia;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Criptografia {
    public static String criptografaMensagem(String mensagem) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchProviderException, SignatureException, InvalidKeyException {

        InputStream inputStream = Criptografia.class.getClassLoader().getResourceAsStream("private.key");
        if (inputStream == null) {
            throw new FileNotFoundException("Arquivo private.key não encontrado no classpath!");
        }

//        byte[] privateBytes = Files.readAllBytes(Paths.get("private.key"));
        byte[] privateBytes = inputStream.readAllBytes();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(privateKey);
        byte[] mensagemBytes = mensagem.getBytes("UTF-8");
        dsa.update(mensagemBytes);
        byte[] assinatura = dsa.sign();

        String mensagemBase64 = Base64.getEncoder().encodeToString(mensagemBytes);
        String assinaturaBase64 = Base64.getEncoder().encodeToString(assinatura);

        JSONObject json = new JSONObject();
        json.put("mensagem", mensagemBase64);
        json.put("assinatura", assinaturaBase64);

        return json.toString();
    }

    public static boolean verificaMensagem(JSONObject json) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, SignatureException {

        String mensagemBase64 = json.getString("mensagem");
        String assinaturaBase64 = json.getString("assinatura");

        byte[] mensagemBytes = Base64.getDecoder().decode(mensagemBase64);
        byte[] assinaturaBytes = Base64.getDecoder().decode(assinaturaBase64);

        InputStream inputStream = Criptografia.class.getClassLoader().getResourceAsStream("public.key");
        if (inputStream == null) {
            throw new FileNotFoundException("Arquivo public.key não encontrado no classpath!");
        }

//        byte[] keyBytes = Files.readAllBytes(Paths.get("public.key"));
        byte[] keyBytes = inputStream.readAllBytes();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Signature sig = Signature.getInstance("SHA1withDSA");
        sig.initVerify(publicKey);
        sig.update(mensagemBytes);

        return sig.verify(assinaturaBytes);
    }
}
