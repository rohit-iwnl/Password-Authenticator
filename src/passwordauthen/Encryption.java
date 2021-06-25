package passwordauthen;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class Encryption {

    public static String md5generate(String data)
    {
        String hashtext = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(data.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            hashtext = number.toString(16);
        } catch (Exception e) {
            System.out.println(e);
        }
        return hashtext;
    }

    public static String getEncoded(String data)
    {
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String getDecoded(String data)
    {
        return new String(Base64.getMimeDecoder().decode(data));
    }
}
