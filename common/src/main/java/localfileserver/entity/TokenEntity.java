package localfileserver.entity;

import com.google.common.base.Strings;
import localfileserver.protobuf.TokenInfo;
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

    public static TokenEntity toEntity(TokenInfo.Token token) {
        TokenEntity tokenEntity = new TokenEntity();

        tokenEntity.setValue(token.getValue());
        tokenEntity.setType(token.getType());
        tokenEntity.setExpireDate(token.getLastTime() == 0 ? null : Instant.ofEpochMilli(token.getLastTime()));
        return tokenEntity;
    }

    /**
     * 长度为空视作未被使用的新token，是有效的
     * 不为空则已被分配，在根据类型做进一步判断
     *
     * @return token是否有效
     */
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

    public TokenInfo.Token toProtobuf() {
        return TokenInfo.Token.newBuilder()
                .setValue(value)
                .setIsValid(isValid())
                .setType(type)
                .setLastTime(expireDate.toEpochMilli()).build();
    }
}
