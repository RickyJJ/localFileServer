package localfileserver.server.service.impl;

import localfileserver.common.AppConst;
import localfileserver.entity.TokenRequest;
import localfileserver.model.Result;
import localfileserver.server.request.TokenRequestManager;
import localfileserver.server.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * internal service to help server service
 * @author Mr.Jiong
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public Result addToWaitQueue(String userName, String key) {
        TokenRequest tokenRequest = new TokenRequest();

        tokenRequest.setUserName(userName);
        tokenRequest.setStatus("0");
        tokenRequest.setKey(key);

        boolean b = TokenRequestManager.addToWaitQueue(tokenRequest);
        if (b) {
            return Result.ok().add(AppConst.TOKEN_APPLY_KEY, key);
        } else {
            if (TokenRequestManager.isFull()) {
                return Result.fail("1106", "Waiting queue is full, try later.");
            }

            log.warn("Cant put token request into wait queue. request: {}", tokenRequest);
            return Result.fail("1105", "Failed to join the waiting queue");
        }
    }
}
