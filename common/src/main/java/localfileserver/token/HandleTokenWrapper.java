package localfileserver.token;

import com.google.common.base.MoreObjects;
import localfileserver.entity.TokenEntity;
import localfileserver.kit.TokenKit;

/**
 * implement for HandleToken
 * @author Mr.Jiong
 */
public class HandleTokenWrapper implements HandleToken {

    protected TokenEntity token;

    protected HandleTokenWrapper() { }
    protected HandleTokenWrapper(TokenEntity token) {
        this.token = token;
    }

    public static HandleToken newInstance(TokenEntity token) {
        HandleTokenWrapper tokenWrapper = new HandleTokenWrapper(token);
        tokenWrapper.token.setValue(TokenKit.newToken());
        return tokenWrapper;
    }

    @Override
    public String value() {
        return token.getValue();
    }

    /**
     * get object of {@link TokenEntity}
     *
     * @return return token entity
     */
    @Override
    public TokenEntity getToken() {
        return token;
    }

    @Override
    public boolean isAvailable() {
        return token != null && token.isValid();
    }

    public static HandleToken toHandleToken(TokenEntity token) {
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
