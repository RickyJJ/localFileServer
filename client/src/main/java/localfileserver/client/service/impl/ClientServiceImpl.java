package localfileserver.client.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import localfileserver.api.ServerService;
import localfileserver.client.config.param.Dict;
import localfileserver.client.entity.User;
import localfileserver.client.kit.SessionKit;
import localfileserver.client.service.ClientService;
import localfileserver.entity.TokenEntity;
import localfileserver.kit.DateKit;
import localfileserver.kit.MapKit;
import localfileserver.model.Result;
import localfileserver.service.BaseService;
import localfileserver.token.ExpiredHandleToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/***
 * implement for ClientService
 * @author Administrator
 * @date 2020/4/4 0004
 */
@Slf4j
@Service
public class ClientServiceImpl extends BaseService implements ClientService {
    @Reference
    private ServerService serverService;

    @Override
    public Result applyToken(User user) {
        try {
            Result result = serverService.applyToken(user.getName());
            log.info("{} apply token result: {}", user.getName(), result);

            if (result == null) {
                result = Result.fail("1101", "Remote service not found");
            }
            return result;
        } catch (Exception e) {
            log.warn("apply token failed", e);
            return Result.fail("5000", "apply failed");
        }
    }

    /**
     * 检验用户是否申请token的key，如果有则尝试从远程服务获取token
     */
    @Override
    public Result userCheckAndFetchToken(User user, String tokenKey) {
        if (tokenKey == null) {
            return Result.fail("There is no key to fetch token.");
        } else {
            Result result = fetchToken(user, tokenKey);

            if (result.isOk()) {
                log.info("Got token.");
                TokenEntity token = (TokenEntity) result.get("token");

                user.updateToken(token);

                String tokenExpireDate = user.getToken().isTemporal() ? DateKit.timestamp(((ExpiredHandleToken) user.getToken()).getExpiredTime()) : "长期";

                result.add("tokenExpireDate", tokenExpireDate);
                result.add("tokenType", user.getToken().isTemporal() ? "2" : "1");

            } else if ("wait".equals(result.getFlag())) {
                log.info("token request is waiting, {}", result);
                return Result.fail("Token request is waiting.");
            } else {
                log.warn("Fetching token failed. code: {}, msg: {}", result.getFlag(), result.getMessage());
            }

            SessionKit.getSession().setAttribute(Dict.Token.TOKEN_KEY, null);

            return result;
        }

    }

    @Override
    public Result fetchToken(User user, String key) {
        Map<String, String> user1 = MapKit.map("user", user.getName());
        user1.put("key", key);

        Result result = serverService.findFromResultQueue(user.getName(), key);

        if (result.isFailed()) {
            result = serverService.findFromWaitQueue(user.getName(), key);

            if (result.isFailed()) {
                log.warn("Token apply failed. {}", result.getMessage());
                result = Result.fail("No token Apply");
            } else {
                result = Result.fail("Token Apply is waiting");
                log.info("Token request is waiting to handle.");
            }
            return result;
        } else {
            serverService.removeFromResultQueue(key);
            serverService.removeFromWaitQueue(key);
        }

        log.debug("fetch token succeed.");
        return result;
    }
}
