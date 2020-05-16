package localfileserver.kit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * @author Mr.Jiong
 */
public class TokenKit {
    private static DateTimeFormatter DETAIL_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static String encode(String uniqueKey) {
        return Base64.getEncoder().encodeToString(uniqueKey.getBytes());
    }

    /**
     * create token use timestamp and a app flag
     *
     * @return a new unique token
     */
    public static String newToken() {
        final String appFlag = "LFS";
        String formattedTime = LocalDateTime.now().format(DETAIL_TIME_FORMATTER);
        return appFlag + "_" + formattedTime;
    }

}
