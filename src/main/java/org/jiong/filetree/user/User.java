package org.jiong.filetree.user;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jiong.filetree.token.ExpireHandleTokenWrapper;
import org.jiong.filetree.token.HandleToken;
import org.jiong.filetree.token.HandleTokenWrapper;
import org.jiong.protobuf.TokenInfo;
import org.jiong.protobuf.UserInfo;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

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

        TokenInfo.Token tokenInstance = parseToken(token);
        Assert.notNull(tokenInstance, "Parse Token failed");

        UserInfo.User build = UserInfo.User.newBuilder().setToken(tokenInstance).setName(name).setId(id).build();

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
        JSONObject parse = (JSONObject) JSONObject.parse(tokenJson);

        TokenInfo.Token.Builder builder = TokenInfo.Token.newBuilder();
        builder.setValue(parse.getString("value"));

        int tokenType = parse.getIntValue("type");
        if (TokenInfo.Token.TokenType.FOREVER_VALUE == tokenType) {
            builder.setIsValid(true);
            builder.setType(TokenInfo.Token.TokenType.FOREVER);
            builder.setIsValid(true);
        } else {
            long endTime = parse.getLongValue("endTime");
            builder.setLastTime(endTime);
            builder.setType(TokenInfo.Token.TokenType.TEMP);

            Instant instant = Instant.ofEpochMilli(endTime);
            builder.setIsValid(instant.isAfter(Instant.now()));
        }

        return builder.build();
    }
}
