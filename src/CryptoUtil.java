import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class CryptoUtil {
    public static String hash_passwd(char[] passwd) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = new String(passwd).getBytes(StandardCharsets.UTF_8);
        byte[] hash = md.digest(bytes);

        Arrays.fill(bytes, (byte) 0);

        StringBuilder sb = new StringBuilder();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
