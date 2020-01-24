package org.jiong.filetree.token;

import org.jiong.filetree.common.util.TokenKit;
import org.jiong.filetree.model.TokenEntity;
import org.jiong.protobuf.TokenInfo;

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
        return token != null;
    }

    public static HandleToken toHandleToken(TokenInfo.Token token) {
        return new HandleTokenWrapper(token);
    }
}
