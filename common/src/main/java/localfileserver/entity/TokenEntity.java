package localfileserver.entity;

import com.google.common.base.Strings;
import localfileserver.protobuf.TokenInfo.Token.TokenType;
import lombok.Data;

import java.time.Instant;

/**
 * a dto for protobuf token
 *
 * @author Mr.Jiong
 */
@Data
public class TokenEntity {
    private String value;

    private TokenType type;

    private Instant expireDate;

    public boolean isValid() {
        switch (type) {
            case FOREVER:
                return true;
            case TEMP:
                return expireDate.isAfter(Instant.now());
            case UNRECOGNIZED:
            default:
                return false;
        }
    }

    public boolean isUsed() {
        return !Strings.isNullOrEmpty(value);
    }
}
