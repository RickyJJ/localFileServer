package org.jiong.filetree.token;

import org.jiong.filetree.common.util.TokenKit;
import org.jiong.protobuf.TokenInfo;

import java.time.Instant;

/**
 * @author Mr.Jiong
 */
public class ExpireHandleTokenWrapper extends HandleTokenWrapper implements ExpiredHandleToken {

    private ExpireHandleTokenWrapper(TokenInfo.Token token, Instant instant) {
        super(token);
        this.token.setExpireDate(instant);
    }

    @Override
    public boolean isExpired() {
        if (token.getType() == TokenInfo.Token.TokenType.TEMP) {
            return token.getExpireDate().isAfter(Instant.now());
        }

        return false;
    }

    @Override
    public Instant getExpiredTime() {
        if (token.getExpireDate() == null) {
            return null;
        }
        return token.getExpireDate();
    }

    public static ExpiredHandleToken newInstance(TokenInfo.Token token, long deadTime) {
        Instant instant = Instant.ofEpochMilli(deadTime);
        ExpireHandleTokenWrapper expireHandleTokenWrapper = new ExpireHandleTokenWrapper(token, instant);
        expireHandleTokenWrapper.token.setValue(TokenKit.newToken());
        return expireHandleTokenWrapper;
    }
}
