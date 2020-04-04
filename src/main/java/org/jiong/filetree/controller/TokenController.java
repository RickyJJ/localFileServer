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
 * this controller is used for token manager to manage token-making
 * and token-applying
 * The manager could create new token if he found that tokens is not enough.
 * And manager could handle requests for tokens applying.
 *
 * @author Mr.Jiong
 */
@Controller
@Slf4j
public class TokenController extends BaseController {
    @Autowired
    private TokenManageConfig tokenManageConfig;

    /**
     * Get into the manager page
     *
     * @param request request
     * @return page
     */
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
     * create new tokens are not enough
     * if tokens system has are enough then not going to create new tokens
     * else create new ones.
     *
     * @return if success, return token
     */
    @RequestMapping("/token/create")
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
     * Dispatch a new token for a user
     * To a certain request for token, manager could choose to dispatch a new token to the user or just ignore it.
     * <p>
     * if status of token changed, then update it to file
     *
     * @return result of validation,
     */
    @RequestMapping("/token/dispatch")
    @ResponseBody
    public Result dispatchToken() {
        String tokenStr = (String) getAttr("token");
        User user = getCurrentUser();

        log.info("Invite token is : {}", tokenStr);

        HandleToken handleToken = TokensManager.getToken(tokenStr);
        if (handleToken == null) {
            log.info("Token does not exist.");
            return Result.fail(AppConst.FAIL, "token not exist");
        } else if (!handleToken.isAvailable()) {
            log.warn("Token is already available now. {}", handleToken);
            return Result.fail(AppConst.FAIL, "Unknown token type.");
        }

        log.info("Update user's token success.");
        user.updateToken(handleToken);
        return Result.ok();
    }

    /**
     * a request from the user to apply for a new token
     * a user could apply it for many times but a apply is illegal at a time
     * <p>
     * Server record requests as queues in files to handle them
     *
     * @return result of request for apply, not the result of applying
     */
    @RequestMapping("/token/apply")
    @ResponseBody
    public Result applyToken() {
        return Result.ok();
    }
}
