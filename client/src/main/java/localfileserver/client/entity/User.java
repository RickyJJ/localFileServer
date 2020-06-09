package localfileserver.client.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import localfileserver.entity.TokenEntity;
import localfileserver.protobuf.TokenInfo;
import localfileserver.protobuf.UserInfo;
import localfileserver.token.ExpireHandleTokenWrapper;
import localfileserver.token.HandleToken;
import localfileserver.token.HandleTokenWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Jiong
 */
@Data
@Slf4j
public class User {

    private String name;
    private String id;
    private String infoPath;
    private HandleToken token;

    /**
     * Update user's status in session immediately,
     * when user session invalidated, write user info to file
     *
     * @param token new token
     */
    public void updateToken(String token) {
        log.info("Token to update: {}", token);

        TokenInfo.Token protobufToken = parseToken(token);
        TokenEntity tokenInstance = TokenEntity.toEntity(protobufToken);
        Assert.notNull(tokenInstance, "Parse Token failed");

        UserInfo.User build = UserInfo.User.newBuilder().setToken(protobufToken).setName(name).setId(id).build();

        try (OutputStream outputStream = new FileOutputStream(infoPath)) {

            build.writeTo(outputStream);

            this.token = tokenInstance.getType() == TokenInfo.Token.TokenType.FOREVER ?
                    HandleTokenWrapper.toHandleToken(tokenInstance) :
                    ExpireHandleTokenWrapper.toHandleToken(tokenInstance);
        } catch (IOException e) {
            log.warn("User info update failed", e);
        }
    }

    private TokenInfo.Token parseToken(String token) {
        byte[] bytes = Base64Utils.decodeFromString(token);
        String tokenJson = new String(bytes, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> parse;
        try {
            //noinspection unchecked
            parse = objectMapper.readValue(tokenJson, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("wrong token json");
        }

        TokenInfo.Token.Builder builder = TokenInfo.Token.newBuilder();
        builder.setValue((String) parse.get("value"));

        int tokenType = (int) parse.get("type");
        if (TokenInfo.Token.TokenType.FOREVER_VALUE == tokenType) {
            builder.setIsValid(true);
            builder.setType(TokenInfo.Token.TokenType.FOREVER);
            builder.setIsValid(true);
        } else {
            long endTime = (long) parse.get("endTime");
            builder.setLastTime(endTime);
            builder.setType(TokenInfo.Token.TokenType.TEMP);

            Instant instant = Instant.ofEpochMilli(endTime);
            builder.setIsValid(instant.isAfter(Instant.now()));
        }

        return builder.build();
    }
}
