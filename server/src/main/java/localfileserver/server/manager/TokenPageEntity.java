package localfileserver.server.manager;

import localfileserver.entity.TokenEntity;
import localfileserver.protobuf.TokenInfo;
import localfileserver.protobuf.TokenPool;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * store tokens of tokenPage
 *
 * implements  for reading tokens from file and write tokens to file
 *
 * a token with non-empty value is available, when set a new token value,
 * then the token is used and is not available any more, but it still is valid
 * if it is normal or not out of expired time.
 *
 * @author Mr.Jiong
 */
@Slf4j
class TokenPageEntity {

    private List<TokenEntity> tokenEntities;

    private File tokenFile;

    /**
     * total token size
     */
    private int size;

    /** size of tokens have value */
    private int usedSize;

    /**
     * total valid token size in a tokenPage
     */
    private int validCount;

    private Instant lastCheckTime;

    private TokenPageEntity() {
    }

    TokenPageEntity(String filePath, TokenInfo.Token.TokenType tokenType, int validCount) {
        Objects.requireNonNull(filePath, "Token file is null");

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    log.warn("Creating file failed");
                }
            } catch (IOException e) {
                log.error("create file failed", e);
            }
        }

        this.tokenEntities = new ArrayList<>();

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            TokenPool.TokenPage.Builder tokenPageBuilder = TokenPool.TokenPage.newBuilder();

            for (int i = 0; i < validCount; i++) {
                TokenInfo.Token.Builder tokenBuilder = TokenInfo.Token.newBuilder();
                // ignore set value when init
                tokenBuilder.setValue("");
                tokenBuilder.setType(tokenType);
                tokenBuilder.setIsValid(true);

                TokenInfo.Token token = tokenBuilder.build();

                tokenPageBuilder.addToken(token);
                this.tokenEntities.add(TokenEntity.toEntity(token));
            }

            TokenPool.TokenPage tokenPage = tokenPageBuilder.build();
            tokenPage.writeTo(outputStream);

            tokenFile = file;
            size = this.validCount = validCount;
            usedSize = 0;
            Instant now = Instant.now();
            // 两分钟检测一次token page的被访问情况
            lastCheckTime = now.plusSeconds(120);
        } catch (IOException e) {
            log.error("create new tokenPage failed", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * load tokenPage from a file
     *
     * @param tokenFile file stored tokes
     * @return an object of tokenPage
     */
    static TokenPageEntity load(File tokenFile) {
        // init tokenPageManager
        Objects.requireNonNull(tokenFile);

        TokenPageEntity tokenPageEntity = new TokenPageEntity();

        if (!tokenFile.exists()) {
            throw new NullPointerException("Token file not found : " + tokenFile.getPath());
        }

        tokenPageEntity.tokenFile = tokenFile;
        tokenPageEntity.readTokenFromFile();

        return tokenPageEntity;
    }

    private void writeTokenToFile() {
        if (this.validCount == 0) {
            File parentFile = tokenFile.getParentFile();
            String outFilePath = parentFile.getPath() + File.pathSeparator + tokenFile.getName() + "_out";
            File dest = new File(outFilePath);
            boolean renameTo = tokenFile.renameTo(dest);

            if (!renameTo) {
                log.warn("renaming invalid tokenPage file failed: {}", tokenFile.getPath());
            } else {
                tokenFile = dest;
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(tokenFile)) {
            TokenPool.TokenPage.Builder builder = TokenPool.TokenPage.newBuilder();
            for (TokenEntity tokenEntity : tokenEntities) {
                TokenInfo.Token token = tokenEntity.toProtobuf();
                builder.addToken(token);
            }

            builder.build().writeTo(outputStream);
        } catch (IOException e) {
            log.error("write token to file failed", e);
        }
    }

    private void readTokenFromFile() {
        try (FileInputStream inputStream = new FileInputStream(tokenFile)) {
            TokenPool.TokenPage tokenPage = TokenPool.TokenPage.parseFrom(inputStream);

            this.tokenEntities = new ArrayList<>();
            tokenPage.getTokenList().forEach(token -> tokenEntities.add(TokenEntity.toEntity(token)));

            this.size = this.tokenEntities.size();
            this.validCount = (int) tokenEntities.stream().filter(TokenEntity::isValid).count();
            usedSize = (int) tokenEntities.stream().filter(TokenEntity::isUsed).count();
        } catch (IOException e) {
            log.error("io error", e);
            throw new RuntimeException("cant read tokenPage data from file");
        }
    }

    List<TokenEntity> tokens() {
        return this.tokenEntities;
    }

    boolean hasToken(TokenEntity targetToken) {
        if (targetToken == null) {
            return false;
        }
        return tokenEntities.stream().anyMatch(token -> token.getValue().equals(targetToken.getValue()));
    }

    boolean isTokenValid(TokenInfo.Token targetToken) {
        if (targetToken == null) {
            return false;
        }

        Optional<TokenEntity> firstToken = tokenEntities.stream().filter(token -> token.getValue().equals(targetToken.getValue())).findFirst();

        if (firstToken.isPresent()) {
            TokenEntity token = firstToken.get();
            if (token.getType() == TokenInfo.Token.TokenType.FOREVER) {
                return true;
            } else {
                return Instant.now().isBefore(token.getExpireDate());
            }
        } else {
            return false;
        }
    }

    void whenShutDown() {
        writeTokenToFile();
    }

    void listenOverTime() {
        writeTokenToFile();
    }

    public int getSize() {
        return size;
    }

    public int getUsedSize() {
        return usedSize;
    }

    public int getValidCount() {
        return validCount;
    }

    public Instant getLastCheckTime() {
        if (lastCheckTime == null) {
            return null;
        }
        return Instant.ofEpochMilli(lastCheckTime.toEpochMilli());
    }

    public void setLastCheckTime(Instant instant) {
        this.lastCheckTime = instant;
    }

    public String getPageName() {
        return tokenFile.getName();
    }
}
