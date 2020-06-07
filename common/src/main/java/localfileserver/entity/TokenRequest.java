package localfileserver.entity;

import localfileserver.protobuf.TokenInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * a object presents a request of token applying in queue
 * @author Administrator
 */
@Data
public class TokenRequest implements Serializable {

    private static final long serialVersionUID = -206336384548210948L;
    private String ip;
    private String userName;
    private String key;
    private TokenInfo.Token token;
    private String status;
}
