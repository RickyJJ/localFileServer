package org.jiong.filetree.request;

import lombok.Data;
import org.jiong.protobuf.TokenInfo;

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
