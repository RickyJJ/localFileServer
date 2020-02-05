package org.jiong.filetree.controller;

import lombok.extern.slf4j.Slf4j;
import org.jiong.filetree.common.TokenManageConfig;
import org.jiong.filetree.common.constants.AppConst;
import org.jiong.filetree.common.util.HttpKit;
import org.jiong.filetree.model.Result;
import org.jiong.filetree.token.HandleToken;
import org.jiong.filetree.token.manager.TokensManager;
import org.jiong.filetree.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * get or create new token
     * @return if success, return token
     *
     */
    @RequestMapping("/token/active")
    @ResponseBody
    public Result createToken() {
        String tokenType = (String) getAttr("tokenType");
        User user = getCurrentUser();

        log.info("tokenType is : {}", tokenType);

        HandleToken token;
        if ("1".equals(tokenType)) {
            token = TokensManager.newToken();
        } else if ("2".equals(tokenType)) {
            token = TokensManager.newToken(true);
        } else {
            return Result.fail(AppConst.FAIL, "Unknown token type.", null);
        }

        user.updateToken(token);
        return Result.ok().add("token", token.value());
    }

    /**
     * Activate new token for user
     * Find token in token manager, and validate status of token
     * if status of token is available, then update status of user's
     * @return result of validation,
     */
    @RequestMapping("/token/active")
    @ResponseBody
    public Result activateToken() {
        String tokenStr = (String) getAttr("token");
        User user = getCurrentUser();

        log.info("Invite token is : {}", tokenStr);

        HandleToken handleToken = TokensManager.getToken(tokenStr);
        if (handleToken == null) {
            log.info("Token does not exist.");
            return Result.fail(AppConst.FAIL, "token not exist");
        } else if (!handleToken.isAvailable()){
            log.warn("Token is already available now. {}", handleToken);
            return Result.fail(AppConst.FAIL, "Unknown token type.");
        }

        log.info("Update user's token success.");
        user.updateToken(handleToken);
        return Result.ok();
    }
}
