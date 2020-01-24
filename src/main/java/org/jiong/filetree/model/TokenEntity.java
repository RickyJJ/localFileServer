package org.jiong.filetree.model;

import com.google.common.base.Strings;
import lombok.Data;
import org.jiong.protobuf.TokenInfo;

import java.time.Instant;

/**
 * a dto for protobuf token
 *
 * @author Mr.Jiong
 */
@Data
public class TokenEntity {
    private String value;

    private TokenInfo.Token.TokenType type;

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
