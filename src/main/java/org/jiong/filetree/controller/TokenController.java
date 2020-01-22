package org.jiong.filetree.controller;

import lombok.extern.slf4j.Slf4j;
import org.jiong.filetree.common.TokenManageConfig;
import org.jiong.filetree.common.constants.AppConst;
import org.jiong.filetree.common.util.HttpKit;
import org.jiong.filetree.user.User;
import org.jiong.filetree.token.HandleToken;
import org.jiong.filetree.token.manager.TokensManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Jiong
 */
@Controller
@Slf4j
public class TokenController extends BaseController {
    @Autowired
    private TokenManageConfig tokenManageConfig;

    @RequestMapping("/token/manager")
    public String managerToken(HttpServletRequest request) {
        String ip = HttpKit.getIpAddress(request);

        if (!tokenManageConfig.getAdminIp().equals(ip)) {
            setAttr(AppConst.ERR_MSG, "No Auth!");
            log.warn("access ip: {}", ip);
            return "error";
        }

        return "tokensManager";
    }

    @RequestMapping("/token/create")
    @ResponseBody
    public Map<String, String> createToken() {
        String tokenType = (String) getAttr("tokenType");
        User user = getCurrentUser();

        log.info("tokenType is : {}", tokenType);

        Map<String, String> result = new HashMap<>(8);
        HandleToken token;
        if ("1".equals(tokenType)) {
            token = TokensManager.newToken();
        } else if ("2".equals(tokenType)) {
            token = TokensManager.newToken(true);
        } else {
            result.put(AppConst.STATUS, "0");
            result.put(AppConst.MSG, "Unknown token type.");
            return result;
        }

        result.put(AppConst.STATUS, "1");
        result.put("newToken", token.value());
        return result;
    }
}
