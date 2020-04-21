package org.jiong.filetree.controller;

import lombok.extern.slf4j.Slf4j;
import org.jiong.filetree.common.TokenManageConfig;
import org.jiong.filetree.common.constants.AppConst;
import org.jiong.filetree.common.util.HttpKit;
import org.jiong.filetree.model.Result;
import org.jiong.filetree.service.ServerService;
import org.jiong.filetree.token.HandleToken;
import org.jiong.filetree.token.manager.TokensManager;
import org.jiong.protobuf.TokenInfo;
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
    private final TokenManageConfig tokenManageConfig;

    private final ServerService serverService;

    public TokenController(TokenManageConfig tokenManageConfig, ServerService serverService) {
        this.tokenManageConfig = tokenManageConfig;
        this.serverService = serverService;
    }

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

        log.info("tokenType is : {}", tokenType);

        HandleToken token;
        if ("1".equals(tokenType)) {
            token = TokensManager.newToken();
        } else if ("2".equals(tokenType)) {
            token = TokensManager.newToken(true);
        } else {
            return Result.fail(AppConst.FAIL, "Unknown token type.", null);
        }

        return Result.ok().add("token", token.value());
    }

    /**
     * The server operator dispatch a new token for the request of a user.
     * To a certain request for token, manager could choose to dispatch a new token to the user or just ignore it.
     * <p>
     * the promise action result will stored in an array in memory(in file when offline).
     * when the client is getting token with a key, then search result in the array and return it to the client.
     * <p>
     *
     * @return result include token if promise action is success, or failed msg
     */
    @RequestMapping("/token/dispatch")
    @ResponseBody
    public Result clientGetToken(HttpServletRequest request) {
        String user = (String) getAttr("user");
        String key = (String) getAttr("key");
        String ipAddress = HttpKit.getIpAddress(request);

        log.info("User [{}] is to fetch token. key: {}", user, key);
        boolean isKeyValid = serverService.checkUserApply(user, key);
        if (!isKeyValid) {
            log.info("User key is invalid.");
            return Result.fail("user key is invalid");
        }

        log.info("staring to find token from queue.");
        Result result = serverService.findFromResultQueue(ipAddress, user, key);
        if (result.isFailed()) {
            log.warn("Fetching token failed. return result: {}", result);
        }

        log.debug("Found token successfully, then return it back to client");
        return result;
    }

    /**
     * a request from the client applying for a new token
     * a user could apply it for many times but one apply is illegal at a time
     * <p>
     * <p>
     * Server will handle it and give a result immediately which not presents
     * a new token is dispatched to the client, but just a key to get the token
     * Then Server operator will decide is or not to dispatch the token to the client manually.
     *
     * @return result with a key, which is necessary to fetch token for the client.
     */
    @RequestMapping("/tokenApply")
    @ResponseBody
    public Result applyToken() {
        // todo receive request for apply tokens and return handling results
        HttpServletRequest request = getRequest();
        String ipAddress = HttpKit.getIpAddress(request);
        String user = (String) getAttr("user");

        Result result = serverService.applyToken(ipAddress, user);
        if (result.isOk()) {
            log.info("Successfully applied token for user {}[{}]", user, ipAddress);
            result = serverService.addToResultQueue(ipAddress, user, (String) result.get("key"), (TokenInfo.Token) result.get("token"));

            log.info("Stored token result for user {}[{}], wait for fetching: {}", user, ipAddress, result);
            return result;
        } else {
            log.info("User apply failed: {}", result);
        }
        return Result.ok();
    }
}
