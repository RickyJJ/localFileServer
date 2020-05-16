package localfileserver.server.request;

import localfileserver.protobuf.TokenInfo;
import lombok.Data;

/**
 * a object presents a request of token applying in queue
 */
@Data
public class TokenRequest {
    private String ip;
    private String userName;
    private String key;
    private TokenInfo.Token token;
}
