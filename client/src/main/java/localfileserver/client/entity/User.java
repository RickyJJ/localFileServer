package localfileserver.client.entity;

import localfileserver.entity.TokenEntity;
import localfileserver.protobuf.TokenInfo;
import localfileserver.protobuf.UserInfo;
import localfileserver.token.ExpireHandleTokenWrapper;
import localfileserver.token.HandleToken;
import localfileserver.token.HandleTokenWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
     * @param tokenEntity new token
     */
    public void updateToken(TokenEntity tokenEntity) {
        log.info("Token to update: {}", tokenEntity);

        Assert.notNull(tokenEntity, "Token is null");

        if (!tokenEntity.isValid()) {
            throw new IllegalArgumentException("invalid token entity");
        }

        TokenInfo.Token token = tokenEntity.toProtobuf();
        UserInfo.User build = UserInfo.User.newBuilder().setToken(token).setName(name).setId(name).build();

        try (OutputStream outputStream = new FileOutputStream(infoPath)) {

            build.writeTo(outputStream);

            this.token = tokenEntity.getType() == TokenInfo.Token.TokenType.FOREVER ?
                    HandleTokenWrapper.toHandleToken(tokenEntity) :
                    ExpireHandleTokenWrapper.toHandleToken(tokenEntity);

            updateUserRole(tokenEntity);

        } catch (IOException e) {
            log.warn("User info update failed", e);
        }
    }

    public void updateUserRole(TokenEntity tokenEntity) {
        UsernamePasswordAuthenticationToken oldUserToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken userToken;
        if (tokenEntity != null && tokenEntity.isValid()) {

            userToken = new UsernamePasswordAuthenticationToken(oldUserToken.getName(), oldUserToken.getCredentials(), AuthorityUtils.createAuthorityList("download"));

        } else {
            userToken = new UsernamePasswordAuthenticationToken(oldUserToken.getName(), oldUserToken.getCredentials(), AuthorityUtils.createAuthorityList("guest"));

        }
        SecurityContextHolder.getContext().setAuthentication(userToken);
    }
}
