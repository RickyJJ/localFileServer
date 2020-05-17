package localfileserver.client.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import localfileserver.api.ServerService;
import localfileserver.client.entity.User;
import localfileserver.client.service.ClientService;
import localfileserver.common.AppConst;
import localfileserver.kit.HttpKit;
import localfileserver.kit.MapKit;
import localfileserver.model.Result;
import localfileserver.service.BaseService;
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

            Map<String, String> result = HttpKit.post(AppConst.SERVER_URL + "applyToken", MapKit.map("user", user.getName()));
            log.info("{} apply token result: {}", user.getName(), result);
            if (result != null) {
                String code = result.get("code");
                if (code != null) {
                    if (isSucc(code)) {
                        return Result.ok();
                    } else {
                        return Result.fail(code, result.getOrDefault("msg", ""));
                    }
                } else {
                    return Result.fail("0", "No message");
                }
            } else {
                return Result.fail("0", "No Response");
            }
        } catch (Exception e) {
            log.warn("apply token failed", e);
            return Result.fail("0", "apply failed");
        }
    }

    @Override
    public Result fetchToken(User user, String key) {
        Map<String, String> user1 = MapKit.map("user", user.getName());
        user1.put("key", key);

        Result result = serverService.findFromResultQueue(user.getName(), key);

        if (result.isFailed()) {
            log.warn("Fetching token failed.");
            return result;
        }

        log.debug("fetch token succeed.");
        return Result.ok().add("token", result.get("token"));
    }
}
