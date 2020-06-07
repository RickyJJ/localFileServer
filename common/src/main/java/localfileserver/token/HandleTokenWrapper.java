package localfileserver.token;

import com.google.common.base.MoreObjects;
import localfileserver.entity.TokenEntity;
import localfileserver.kit.TokenKit;
import localfileserver.protobuf.TokenInfo;

import java.time.Instant;

/**
 * implement for HandleToken
 * @author Mr.Jiong
 */
public class HandleTokenWrapper implements HandleToken {

    protected TokenEntity token;

    protected HandleTokenWrapper(TokenInfo.Token token) {
        this.token = new TokenEntity();
        this.token.setType(token.getType());
        this.token.setValue(token.getValue());

        long lastTime = token.getLastTime();
        this.token.setExpireDate(lastTime == 0 ? null : Instant.ofEpochMilli(lastTime));

    }

    public static HandleToken newInstance(TokenInfo.Token token) {
        HandleTokenWrapper tokenWrapper = new HandleTokenWrapper(token);
        tokenWrapper.token.setValue(TokenKit.newToken());
        return tokenWrapper;
    }

    @Override
    public String value() {
        return token.getValue();
    }

    @Override
    public boolean isAvailable() {
        return token != null && token.isValid();
    }

    public static HandleToken toHandleToken(TokenInfo.Token token) {
        return new HandleTokenWrapper(token);
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(this.getClass())
                .add("token", token.getValue())
                .add("tokenType", "normal")
                .add("isAvailable", "true");

        return toStringHelper.toString();
    }
}
