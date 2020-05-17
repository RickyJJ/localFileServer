package localfileserver.server.service.impl;

import localfileserver.model.Result;
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
        return null;
    }
}
