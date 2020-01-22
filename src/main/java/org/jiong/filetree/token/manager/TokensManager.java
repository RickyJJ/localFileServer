package org.jiong.filetree.token.manager;

import lombok.extern.slf4j.Slf4j;
import org.jiong.filetree.common.TokenManageConfig;
import org.jiong.filetree.token.ExpireHandleTokenWrapper;
import org.jiong.filetree.token.HandleToken;
import org.jiong.filetree.token.HandleTokenWrapper;
import org.jiong.protobuf.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Manager tokens
 *
 * @author Mr.Jiong
 */
@Component
@Slf4j
public class TokensManager {
    /**
     * store normal tokens
     */
    private final static ConcurrentMap<String, TokenPageEntity> TOKENS = new ConcurrentHashMap<>();

    /**
     * store expired tokens
     */
    private final static ConcurrentMap<String, TokenPageEntity> EXPIRED_TOKENS = new ConcurrentHashMap<>();

    private static int normalCount = 0;

    private static int expireCount = 0;

    @Autowired
    private static TokenManageConfig tokenManageConfig;

    public static int totalSize() {
        return normalTokenCount() + expireTokenCount();
    }

    public static int expireTokenCount() {
        return expireCount;
    }

    public static int normalTokenCount() {
        return normalCount;
    }


    public static HandleToken newToken(Date deadDate) {
        return newToken(true, deadDate.toInstant());
    }

    public static HandleToken newToken(boolean isTemp) {
        return newToken(isTemp, null);
    }

    public static HandleToken newToken() {
        return newToken(false, null);
    }

    /**
     * 生成一个token
     *
     * @param isExpired token是否长期
     * @param deadDate  如果非长期token，则需要指定过期时间。默认三十分钟
     * @return 长期token，短期token
     */
    private static HandleToken newToken(boolean isExpired, Instant deadDate) {
        HandleToken token;
        if (!isExpired) {
            token = HandleTokenWrapper.asHandleToken(createToken(TokenInfo.Token.TokenType.FOREVER, 0L));
            return token;
        }

        if (null == deadDate) {
            deadDate = Instant.now().plus(30, ChronoUnit.MINUTES);
        }

        token = ExpireHandleTokenWrapper.asHandleToken(createToken(TokenInfo.Token.TokenType.TEMP, deadDate.toEpochMilli()));
        return token;
    }

    /**
     * todo 实现token的创建，从现有的tokenPage中查找可以分配的token，
     * 如果没有则新建tokenPage然后在分配token
     * @param tokenType
     * @param deadTime
     * @return
     */
    private static TokenInfo.Token createToken(TokenInfo.Token.TokenType tokenType, long deadTime) {

        return null;
    }

    @PostConstruct
    public static void init() {
        log.info("init TokensManager...");

        List<TokenPageEntity> normal = loadTokenPage("normal", (file -> file.getName().endsWith(".token")));
        List<TokenPageEntity> temporal = loadTokenPage("temporal", (file -> file.getName().endsWith(".token")));

        if (normal != null) {
            normal.forEach(tokenPageEntity -> TOKENS.put(tokenPageEntity.getPageName(), tokenPageEntity));
        }

        TOKENS.values().forEach(tokenPageEntity -> normalCount += tokenPageEntity.getValidCount());
        log.info("normal token count: {}", normalCount);

        if (temporal != null) {
            temporal.forEach(tokenPageEntity -> EXPIRED_TOKENS.put(tokenPageEntity.getPageName(), tokenPageEntity));
        }

        EXPIRED_TOKENS.values().forEach(tokenPageEntity -> expireCount += tokenPageEntity.getValidCount());
        log.info("valid expireTokens count: {}", expireCount);
    }

    /**
     * create tokenPage for normal token (permanent)
     *
     * @return token page
     */
    private static TokenPageEntity createNormalTokenPage() {
        File tokenFile = createTokenFile();
        return new TokenPageEntity(tokenFile.getAbsolutePath(), TokenInfo.Token.TokenType.FOREVER, 50);
    }

    /**
     * create tokenPage for temp token
     *
     * @return token page
     */
    private static TokenPageEntity createExpireTokenPage() {
        File tokenFile = createTokenFile();
        return new TokenPageEntity(tokenFile.getAbsolutePath(), TokenInfo.Token.TokenType.TEMP, 20);
    }

    private static File createTokenFile() {
        String tokenPath = tokenManageConfig.getTokenPath();
        while (true) {
            String tokenFilePath = tokenPath + File.pathSeparator + createFileName();
            File file = new File(tokenFilePath);
            if (!file.exists()) {
                try {
                    boolean newFile = file.createNewFile();
                    if (newFile) {
                        return file;
                    }
                } catch (IOException e) {
                    log.warn("create file failed.");
                }
            }
        }
    }

    private static String createFileName() {
        return "token_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssfff")) + ".token";
    }

    private static List<TokenPageEntity> loadTokenPage(String subTokenPage, FileFilter fileFilter) {
        String tokenPath = tokenManageConfig.getTokenPath();
        File tokenPageDir = new File(tokenPath + File.pathSeparator + subTokenPage);

        if (!tokenPageDir.exists() || !tokenPageDir.isDirectory()) {
            log.warn("{} does not exist, now create new one", subTokenPage);
            tokenPageDir.mkdirs();
        }

        File[] tokenPageFiles = tokenPageDir.listFiles(fileFilter);
        if (tokenPageFiles == null) {
            log.warn("{} has not tokenPage files", subTokenPage);
            return null;
        }
        return Arrays.stream(tokenPageFiles).map(TokenPageEntity::load).collect(Collectors.toList());
    }

    /**
     * remove tokenPage from map for saving memory
     * @param tokenPageName tokenPage file name
     */
    static void removeTokenPage(String tokenPageName) {
        if (TOKENS.containsKey(tokenPageName)) {
            log.debug("remove normal tokenPage {}", tokenPageName);
            TOKENS.remove(tokenPageName);
        } else if (EXPIRED_TOKENS.containsKey(tokenPageName)) {
            log.debug("remove expired tokenPage: {}", tokenPageName);
            EXPIRED_TOKENS.remove(tokenPageName);
        } else {
            log.warn("there is no tokenPage named : {}", tokenPageName);
        }
    }

}
