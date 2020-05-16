package localfileserver.server.impl;

import localfileserver.api.ServerService;
import localfileserver.model.Result;
import localfileserver.protobuf.TokenInfo;
import localfileserver.server.kit.Encrypt;
import localfileserver.server.request.TokenRequest;
import localfileserver.server.request.TokenRequestManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * impl for IServerService
 *
 * @author Mr.Jiong
 */
@Slf4j
@Service
public class ServerServiceImpl implements ServerService {
    @Override
    public Result applyToken(String ipAddr, String userName) {
        return null;
    }

    @Override
    public Result addToResultQueue(String ipAddr, String userName, String key, TokenInfo.Token token) {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setIp(ipAddr);
        tokenRequest.setUserName(userName);
        tokenRequest.setKey(key);
        tokenRequest.setToken(token);

        boolean b = TokenRequestManager.addToQueue(tokenRequest);
        if (b) {
            return Result.ok();
        } else if (TokenRequestManager.isFull()) {
            log.warn("Request array is full, adding new request failed");
            return Result.fail("Token apply is full");

        } else {
            log.warn("unknown error happened. adding new request failed.");
            return Result.fail("Add token to waiting array failed");
        }
    }

    @Override
    public Result findFromResultQueue(String ipAddr, String userName, String key) {
        return null;
    }

    @Override
    public boolean checkUserApply(String user, String key) {
        String encode = new Encrypt().encode(user);
        return Objects.equals(encode, key);
    }
}
