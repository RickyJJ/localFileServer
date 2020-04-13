package org.jiong.filetree.service.impl;

import com.jfinal.kit.Kv;
import lombok.extern.slf4j.Slf4j;
import org.jiong.filetree.common.constants.AppConst;
import org.jiong.filetree.common.util.HttpKit;
import org.jiong.filetree.model.Result;
import org.jiong.filetree.service.ClientService;
import org.jiong.filetree.user.User;
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
    @Override
    public Result applyToken(User user) {
        try {
            Map<String, String> result = HttpKit.post(AppConst.SERVER_URL + "applyToken", Kv.by("user", user.getName()));
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
        Map<String, String> result = HttpKit.post(AppConst.SERVER_URL + "/fetchToken", Kv.by("user", user.getName()).set("key", key));

        if (result == null) {
            return Result.fail("0", "No Response");
        }

        String code = result.get("code");
        if (isSucc(code)) {
            log.debug("fetch token succeed.");
            return Result.ok().add("token", result.get("token"));
        } else {
            log.warn("fetch token failed: {}", result.get("msg"));
            return Result.fail("0", result.get("msg"));
        }
    }
}
