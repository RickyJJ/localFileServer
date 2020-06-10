package localfileserver.server.manager;

import com.google.common.base.Strings;
import localfileserver.entity.TokenEntity;
import localfileserver.protobuf.TokenInfo;
import localfileserver.server.ServerApplication;
import localfileserver.server.config.TokenManageConfig;
import localfileserver.token.ExpireHandleTokenWrapper;
import localfileserver.token.ExpiredHandleToken;
import localfileserver.token.HandleToken;
import localfileserver.token.HandleTokenWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * Manager tokens
 *
 * @author Mr.Jiong
 */
@Slf4j
@Component
public class TokensManager {
    /**
     * store normal tokens
     */
    private final static ConcurrentMap<String, TokenPageEntity> TOKENS = new ConcurrentHashMap<>();

    /**
     * store expired tokens
     */
    private final static ConcurrentMap<String, TokenPageEntity> EXPIRED_TOKENS = new ConcurrentHashMap<>();

    private final static BlockingDeque<TokenPageEntity> NORMAL_WAITING_PAGE_QUEUE = new LinkedBlockingDeque<>();
    private final static BlockingDeque<TokenPageEntity> EXPIRE_WAITING_PAGE_QUEUE = new LinkedBlockingDeque<>();

    private static int normalCount = 0;

    private static int temporalCount = 0;

    private static TokenManageConfig tokenManageConfig;

    public static int totalSize() {
        return normalTokenCount() + temporalTokenCount();
    }

    public static int temporalTokenCount() {
        return temporalCount;
    }

    public static int normalTokenCount() {
        return normalCount;
    }


    public static HandleToken newToken(long time, TemporalUnit timeUnit) {
        Instant plus = Instant.now().plus(time, timeUnit);
        return newToken(true, plus);
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
            token = createToken(TokenInfo.Token.TokenType.FOREVER, 0L);
            return token;
        }

        if (null == deadDate) {
            deadDate = Instant.now().plus(30, ChronoUnit.MINUTES);
        }

        token = createToken(TokenInfo.Token.TokenType.TEMP, deadDate.toEpochMilli());
        return token;
    }

    /**
     * 如果没有则新建tokenPage然后在分配token
     *
     * @param tokenType token type, normal or expire
     * @param deadTime  dead time for expire token
     * @return new token
     */
    private static HandleToken createToken(TokenInfo.Token.TokenType tokenType, long deadTime) {
        switch (tokenType) {
            case FOREVER:
                return takeTokenFromPage();
            case TEMP:
                return tokenExpireTokenFromPage(deadTime);
            case UNRECOGNIZED:
                log.warn("unknown token type: {}", tokenType.name());
                throw new IllegalArgumentException("unknown token type");
            default:
                log.warn("unmatched token type");
                throw new IllegalArgumentException("unmatched token type");
        }
    }

    /**
     * take token from expire tokenPage
     *
     * @param deadTime expire time
     * @return expire token
     */
    private static ExpiredHandleToken tokenExpireTokenFromPage(long deadTime) {
        Optional<TokenPageEntity> pageEntity = EXPIRED_TOKENS.values().stream()
                .filter(tokenPageEntity -> tokenPageEntity.getSize() > tokenPageEntity.getUsedSize())
                .findFirst();

        ExpiredHandleToken expiredHandleToken;
        if (pageEntity.isPresent()) {
            TokenPageEntity tokenPageEntity = pageEntity.get();
            TokenEntity availableToken = tokenPageEntity.tokens().stream().
                    filter(token -> Strings.isNullOrEmpty(token.getValue()))
                    .findFirst().get();
            // update date of page in queue
            tokenPageEntity.setLastCheckTime(Instant.now());
            expiredHandleToken = ExpireHandleTokenWrapper.newInstance(availableToken, deadTime);
        } else {
            TokenPageEntity newExpireTokenPage = createExpireTokenPage();
            EXPIRED_TOKENS.put(newExpireTokenPage.getPageName(), newExpireTokenPage);
            EXPIRE_WAITING_PAGE_QUEUE.add(newExpireTokenPage);
            expiredHandleToken = ExpireHandleTokenWrapper.newInstance(newExpireTokenPage.tokens().get(0), deadTime);
        }

        return expiredHandleToken;
    }

    /**
     * token token as normal token
     *
     * @return normal token
     */
    private static HandleToken takeTokenFromPage() {
        Optional<TokenPageEntity> availableTokenPage = TOKENS.values().stream()
                .filter(tokenPageEntity -> tokenPageEntity.getSize() > tokenPageEntity.getUsedSize())
                .findFirst();

        HandleToken handleToken;
        TokenEntity token;
        if (availableTokenPage.isPresent()) {
            TokenPageEntity tokenPageEntity = availableTokenPage.get();
            token = tokenPageEntity.tokens().stream().filter(tokenItem -> Strings.isNullOrEmpty(tokenItem.getValue()))
                    .findFirst().get();
            tokenPageEntity.setLastCheckTime(Instant.now());
        } else {
            TokenPageEntity normalTokenPage = createNormalTokenPage();

            TOKENS.put(normalTokenPage.getPageName(), normalTokenPage);
            NORMAL_WAITING_PAGE_QUEUE.add(normalTokenPage);

            token = normalTokenPage.tokens().get(0);
        }

        handleToken = HandleTokenWrapper.newInstance(token);
        return handleToken;
    }

    @PostConstruct
    public static void init() {
        log.info("init TokensManager...");

        tokenManageConfig = ServerApplication.getAppContext().getBean(TokenManageConfig.class);

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

        EXPIRED_TOKENS.values().forEach(tokenPageEntity -> temporalCount += tokenPageEntity.getValidCount());
        log.info("valid expireTokens count: {}", temporalCount);

        List<Thread> tasks = new ArrayList<>();
        tasks.add(new TokenQueryLoopTask(NORMAL_WAITING_PAGE_QUEUE, 60));
        tasks.add(new TokenQueryLoopTask(EXPIRE_WAITING_PAGE_QUEUE, 30));

        tasks.forEach(Thread::start);
    }

    /**
     * create tokenPage for normal token (permanent)
     *
     * @return token page
     */
    private static TokenPageEntity createNormalTokenPage() {
        File tokenFile = createTokenFile("normal");
        return new TokenPageEntity(tokenFile.getAbsolutePath(), TokenInfo.Token.TokenType.FOREVER, 50);
    }

    /**
     * create tokenPage for temp token
     *
     * @return token page
     */
    private static TokenPageEntity createExpireTokenPage() {
        File tokenFile = createTokenFile("temporal");
        return new TokenPageEntity(tokenFile.getAbsolutePath(), TokenInfo.Token.TokenType.TEMP, 20);
    }

    private static File createTokenFile(String subPackage) {
        String tokenPath = tokenManageConfig.getTokenPath();
        while (true) {
            String tokenFilePath = tokenPath + File.separatorChar + subPackage + File.separatorChar + createFileName();
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
        return "token_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".token";
    }

    private static List<TokenPageEntity> loadTokenPage(String subTokenPage, FileFilter fileFilter) {
        String tokenPath = tokenManageConfig.getTokenPath();
        File tokenPageDir = new File(tokenPath + File.separatorChar + subTokenPage);

        if (!tokenPageDir.exists() || !tokenPageDir.isDirectory()) {
            log.warn("{} does not exist, now create new one", subTokenPage);
            //noinspection ResultOfMethodCallIgnored
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
     *
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
