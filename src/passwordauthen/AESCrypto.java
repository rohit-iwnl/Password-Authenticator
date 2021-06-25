package passwordauthen;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class AESCrypto {

    private static final String Algo ="AES";
    private byte[] keyValue;
    public static String userkey = null;


    //QueZxsweqqER39w1
    public AESCrypto() {

        keyValue = userkey.getBytes();
    }

    private Key generateKey() throws Exception{

        Key key = new SecretKeySpec(keyValue, Algo);

        return key;
    }

    public String encrypt(String Data) throws Exception{

        Key key = generateKey();
        Cipher c = Cipher.getInstance(Algo);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = Base64.getEncoder().encodeToString(encVal);

        return encryptedValue;

    }

    public String decrypt(String encryptedData) throws Exception{

        Key key = generateKey();
        Cipher c = Cipher.getInstance(Algo);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String (decValue);

        return decryptedValue;

    }

    public static String generaterandomkey(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }



}