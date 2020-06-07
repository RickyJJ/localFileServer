package localfileserver.api;


import localfileserver.entity.TokenRequest;
import localfileserver.model.Result;
import localfileserver.protobuf.TokenInfo;

import java.util.List;

/**
 * define method for server
 * 1. to handle apply for token
 * 2. create token
 * 3. store token handle result to return it back to client
 * @author Mr.Jiong
 */
public interface ServerService {
    /**
     * handle request from ip address for apply token with user name
     * @param userName user name
     * @return handle result. If success then return success with a key, or fail
     */
    Result applyToken(String userName);

    /**
     * check user's key is valid or not.
     * @param user a string presents user
     * @param key a key returned from server when applying succeed
     * @return boolean true for user's key is valid
     */
    boolean checkUserApply(String user, String key);

    /**
     * find token in result array and return it to client
     * token has a time limit in result array, if timeout then token will be removed from
     * result array
     * @param userName client user name
     * @param key client key
     * @return result with token if token exist
     */
    Result findFromResultQueue(String userName, String key);

    /**
     * Get a token by specified token type
     *
     * if tokens are not enough, then create a new token
     * @param tokenType token type, forever or temporal
     * @return result
     */
    Result createToken(String tokenType);

    /**
     * add apply result to a result queue, then client can fetch result of apply token
     * from it
     * @param userName client user name
     * @param key a key returned from server when applying succeed
     * @param token token dispatched for the client
     * @return handle result
     */
    Result addToResultQueue(String userName, String key, TokenInfo.Token token);

    /**
     * remove token request from result queue.
     * @param key user key
     * @return if token request exists and be removed successfully, return true
     * otherwise return false
     */
    boolean removeFromResultQueue(String key);

    /**
     * find token request in waiting queue,
     * if a token request was rejected by admin, then status of
     * the token request is failed, or waiting for further handle.
     * @param name user name
     * @param key user key
     * @return result of token request is waiting or handled
     */
    Result findFromWaitQueue(String name, String key);

    /**
     * find token request in waiting queue, and change status of it
     * @param user user
     * @param key user key
     * @param status token request status
     */
    void changeStatusOfWaitingRequest(String user, String key, String status);

    /**
     * remove token request from wait queue
     * @param key user key
     * @return true for removing token request succeed.
     */
    boolean removeFromWaitQueue(String key);

    String test(String user);

    /**
     * get token request queue from server
     * @return list of token request
     */
    List<TokenRequest> getTokenRequests();

    Result rejectTokenApply(String user, String key);
}

