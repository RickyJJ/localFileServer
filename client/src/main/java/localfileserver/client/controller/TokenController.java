package localfileserver.client.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import localfileserver.api.ServerService;
import localfileserver.entity.TokenRequest;
import localfileserver.model.Result;
import localfileserver.protobuf.TokenInfo;
import localfileserver.token.HandleTokenWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * this controller is used for token manager to manage token-making
 * and token-applying
 * The manager could create new token if he found that tokens is not enough.
 * And manager could handle requests for tokens applying.
 * <p>
 * Need authorize for admin
 *
 * @author Mr.Jiong
 */
@Slf4j
@RestController
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
    public String managerToken(HttpServletRequest request) {
        return "manager";
    }

    @GetMapping("tokenRequests")
    public Result getTokenRequest() {
        List<TokenRequest> tokenRequest = serverService.getTokenRequests();

        if (tokenRequest == null) {
            return Result.fail("1107", "Failed to get token request");
        }
        return Result.ok().add("list", tokenRequest);
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
    @PostMapping("/token/dispatch")
    public Result adminDispatchToken(@RequestBody Map<String, Object> name1) {
        log.info("name: {}", name1);
        String user = (String) name1.get("userName");
        String key = (String) name1.get("key");
        String tokenType = (String) name1.get("tokenType");

        if ("2".equals(tokenType)) {
            // 拒绝
            return serverService.rejectTokenApply(user, key);
        }

        String name = TokenInfo.Token.TokenType.forNumber(Integer.parseInt(tokenType)).name();

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
            return result;
        }

        HandleTokenWrapper tokenWrapper = (HandleTokenWrapper) result.get("token");
        result = serverService.addToResultQueue(user, key, tokenWrapper.getToken());
        if (result.isFailed()) {
            log.warn("get token in result queue failed. {}", result);
            serverService.changeStatusOfWaitingRequest(user, key, "failed");
        } else {
            log.debug("get token in result queue succeed. {}", key);
            serverService.changeStatusOfWaitingRequest(user, key, "done");
        }

        log.debug("Dispatch token completely");
        return result;
    }


}
