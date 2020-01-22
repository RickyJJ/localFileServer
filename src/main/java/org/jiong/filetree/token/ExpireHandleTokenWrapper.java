package org.jiong.filetree.token;

import org.jiong.protobuf.TokenInfo;

import java.time.Instant;

/**
 * @author Mr.Jiong
 */
public class ExpireHandleTokenWrapper extends HandleTokenWrapper implements ExpiredHandleToken {
    private Instant deadDate;

    public ExpireHandleTokenWrapper(Instant deadDate) {
        this.deadDate = deadDate;
    }

    @Override
    public boolean isExpired() {
        if (token.getType() == TokenInfo.Token.TokenType.TEMP) {
            Instant instant = Instant.ofEpochMilli(token.getLastTime());
            return instant.isAfter(Instant.now());
        }

        return false;
    }

    @Override
    public Instant getExpiredTime() {
        if (deadDate == null) {
            return null;
        }
        return Instant.ofEpochMilli(deadDate.toEpochMilli());
    }


    public static ExpiredHandleToken asExpiredHandleToken(TokenInfo.Token token) {

        ExpireHandleTokenWrapper expireHandleTokenWrapper = new ExpireHandleTokenWrapper(null);
        expireHandleTokenWrapper.setToken(token);

        return expireHandleTokenWrapper;
    }
}
