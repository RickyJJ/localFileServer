package localfileserver.server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import localfileserver.api.ServerService;
import localfileserver.kit.StringKit;
import localfileserver.model.Result;
import localfileserver.protobuf.TokenInfo;
import localfileserver.server.kit.Encrypt;
import localfileserver.server.manager.TokensManager;
import localfileserver.server.request.TokenRequest;
import localfileserver.server.request.TokenRequestManager;
import localfileserver.server.service.TokenService;
import localfileserver.token.HandleToken;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static localfileserver.protobuf.TokenInfo.Token.TokenType;
import static localfileserver.protobuf.TokenInfo.Token.TokenType.forNumber;

/**
 * impl for IServerService
 *
 * @author Mr.Jiong
 */
@Slf4j
@Service(interfaceClass = ServerService.class)
public class ServerServiceImpl implements ServerService {
    private final TokenService tokenService;

    public ServerServiceImpl(TokenService tokenService) {
        this.tokenService = tokenService;
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
    @Override
    public Result applyToken(String user) {
        Result result = Result.ok();
        // could add some extern validation
        String key = new Encrypt().encode(user);
        if (result.isOk()) {
            result = tokenService.addToWaitQueue(user, key);

            log.debug("Now get in waiting queue for dispatch token for user {}  by admin", user);
            return result;
        } else {
            log.info("User apply failed: {}", result);
        }
        return Result.ok();
    }

    @Override
    public Result createToken(String tokenType) {
        log.debug("token type is {}", tokenType);
        if (StringKit.isEmpty(tokenType)) {
            log.warn("toke type is null");
            return Result.fail("0001", "Token type is null");
        }

        int tokenTypeInt;
        try {
            tokenTypeInt = Integer.parseInt(tokenType);
        } catch (NumberFormatException e) {
            log.warn("token type can not be parsed to int: {}", tokenType);
            tokenTypeInt = -1;
        }

        TokenType type = forNumber(tokenTypeInt);
        if (type == null) {
            return Result.fail("1001", "Token type is wrong.");
        }

        HandleToken token;
        switch (type) {
            case FOREVER:
                token = TokensManager.newToken();
                break;
            case TEMP:
                token = TokensManager.newToken(true);
                break;
            default:
                token = null;
        }

        if (token == null) {
            return Result.fail("1002", "Create token failed");
        }
        log.debug("Get token: {}", token);
        return Result.ok().add("token", token);
    }

    @Override
    public boolean checkUserApply(String user, String key) {
        String encode = new Encrypt().encode(user);
        return Objects.equals(encode, key);
    }

    @Override
    public Result findFromResultQueue(String userName, String key) {
        TokenRequest tokenRequest = TokenRequestManager.findInQueue(userName, key);

        return tokenRequest == null ? Result.fail("2001", "No token found in Result queue")
                : Result.ok().add("token", tokenRequest.getToken()).add("key", key);
    }

    @Override
    public Result addToResultQueue(String userName, String key, TokenInfo.Token token) {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setUserName(userName);
        tokenRequest.setKey(key);
        tokenRequest.setToken(token);

        boolean b = TokenRequestManager.addToQueue(tokenRequest);
        if (b) {
            return Result.ok();
        } else if (TokenRequestManager.isFull()) {
            log.warn("Request array is full, adding new request failed");
            return Result.fail("Token apply is full");

        } else {
            log.warn("unknown error happened. adding new request failed.");
            return Result.fail("Add token to waiting array failed");
        }
    }

    @Override
    public boolean removeFromResultQueue(String key) {
        TokenRequest tokenRequest = TokenRequestManager.removeFromQueue(key);

        if (tokenRequest == null) {
            log.warn("token request for key[{}] not found in queue", key);
            return false;
        }

        log.debug("removing token request from queue completely.");
        return true;
    }

    /**
     * find token request in waiting queue,
     * if a token request was rejected by admin, then status of
     * the token request is failed, or waiting for further handle.
     *
     * @param name user name
     * @param key  user key
     * @return result of token request is waiting or handled
     */
    @Override
    public Result findFromWaitQueue(String name, String key) {
        TokenRequest tokenRequest = TokenRequestManager.findInWaitQueue(key);

        if (tokenRequest == null) {
            return Result.fail("2002", "Token request not found");
        }

        if ("wait".equals(tokenRequest.getStatus()) || "done".equals(tokenRequest.getStatus())) {
            return Result.ok();
        } else if ("canceled".equals(tokenRequest.getStatus())) {
            return Result.fail("2003", "Token request is canceled.");
        }
        return Result.fail("2009", "unknown token request status.");
    }

    /**
     * remove token request from wait queue
     *
     * @param key user key
     * @return token request to remove
     */
    @Override
    public boolean removeFromWaitQueue(String key) {
        TokenRequest tokenRequest = TokenRequestManager.removeFromWaitQueue(key);

        if (tokenRequest == null) {
            log.warn("token request not found in wait queue. key: {}", key);
            return false;
        }

        return true;
    }

    @Override
    public String test(String user) {
        return "Hello " + user +", RPC is here it.";
    }

    /**
     * find token request in waiting queue, and change status of it
     *
     * @param user   user
     * @param key    user key
     * @param status token request status
     */
    @Override
    public void changeStatusOfWaitingRequest(String user, String key, String status) {
        TokenRequest inWaitQueue = TokenRequestManager.findInWaitQueue(key);

        if (inWaitQueue == null) {
            log.warn("token request in waiting queue not found. key: {}", key);
            return;
        }
        inWaitQueue.setStatus(status);
    }
}
