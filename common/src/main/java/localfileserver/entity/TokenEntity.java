package localfileserver.entity;

import com.google.common.base.Strings;
import localfileserver.protobuf.TokenInfo.Token.TokenType;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * a dto for protobuf token
 *
 * @author Mr.Jiong
 */
@Data
public class TokenEntity implements Serializable {

    private static final long serialVersionUID = -4907753427488900612L;

    private String value;

    private TokenType type;

    private Instant expireDate;

    public boolean isValid() {
        if (value == null || value.trim().length() == 0) {
            return false;
        }

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
