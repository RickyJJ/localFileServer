package localfileserver.token;

import com.google.common.base.MoreObjects;
import localfileserver.entity.TokenEntity;
import localfileserver.kit.DateKit;
import localfileserver.kit.TokenKit;
import localfileserver.protobuf.TokenInfo;

import java.time.Instant;

/**
 * @author Mr.Jiong
 */
public class ExpireHandleTokenWrapper extends HandleTokenWrapper implements ExpiredHandleToken {

    private ExpireHandleTokenWrapper() {
        super();
    }

    @Override
    public boolean isExpired() {
        if (token.getExpireDate() == null) {
            return false;
        }

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

    public static ExpireHandleTokenWrapper toHandleToken(TokenEntity tokenEntity) {
        ExpireHandleTokenWrapper expireHandleTokenWrapper = new ExpireHandleTokenWrapper();

        expireHandleTokenWrapper.token = tokenEntity;
        return expireHandleTokenWrapper;
    }

    public static ExpiredHandleToken newInstance(TokenEntity token, long deadTime) {
        ExpireHandleTokenWrapper expireHandleTokenWrapper = new ExpireHandleTokenWrapper();

        token.setExpireDate(Instant.ofEpochMilli(deadTime));
        token.setType(TokenInfo.Token.TokenType.TEMP);
        token.setValue(TokenKit.newToken());

        expireHandleTokenWrapper.token = token;

        return expireHandleTokenWrapper;
    }

    @Override
    public String toString() {
        Instant expireDate = getExpiredTime();
        MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(this.getClass())
                .add("token", token.getValue())
                .add("tokenType", token.getType().name())
                .add("isAvailable", isExpired())
                .add("expireDate", expireDate == null ? "" : DateKit.timestamp(expireDate));
        return toStringHelper.toString();
    }
}
