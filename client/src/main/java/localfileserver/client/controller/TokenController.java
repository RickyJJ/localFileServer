package localfileserver.client.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import localfileserver.api.ServerService;
import localfileserver.model.Result;
import localfileserver.protobuf.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * Need authorize for admin
 *
 * @author Mr.Jiong
 */
@Slf4j
@Controller
@PreAuthorize("admin")
public class TokenController extends BaseController {

    @Reference
    private ServerService serverService;


    /**
     * Get into the manager page
     *
     * @param request request
     * @return page
     */
    @RequestMapping("/token/manager")
    @PreAuthorize("admin")
    public String managerToken(HttpServletRequest request) {
        return "tokensManager";
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
    public Result adminDispatchToken() {
        String user = (String) getAttr("user");
        String key = (String) getAttr("key");
        String tokenType = (String) getAttr("tokenType");

        String name = TokenInfo.Token.TokenType.valueOf(tokenType).name();

        log.info("Admin dispatch token [{}] to user [{}] with key[{}]", name, user, key);

        boolean isKeyValid = serverService.checkUserApply(user, key);
        if (!isKeyValid) {
            log.info("User key is invalid.");
            return Result.fail("user key is invalid");
        }

        log.debug("staring to create token from apply.");
        Result result = serverService.createToken(tokenType);

        if (result.isFailed()) {
            log.warn("Dispatch token failed. return result: {}", result);
        }

        result = serverService.addToResultQueue(user, key, (TokenInfo.Token) result.get("token"));
        if (result.isFailed()) {
            log.warn("get token in result queue failed. {}", result);
        }

        log.debug("Dispatch token completely");
        return result;
    }


}
