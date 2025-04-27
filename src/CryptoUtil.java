import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Pattern;

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

    public static boolean check_email_format(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(email).matches();
    }
}
