package org.jiong.filetree.service;

import org.jiong.filetree.model.Result;
import org.jiong.protobuf.TokenInfo;

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
     * @param ipAddr ip address of client
     * @param userName user name
     * @return handle result. If success then return success with a key, or fail
     */
    Result applyToken(String ipAddr, String userName);

    /**
     * add apply result to a result queue, then client can fetch result of apply token
     * from it
     * @param ipAddr client ip address
     * @param userName client user name
     * @param key client key
     * @param token token dispatched for the client
     * @return handle result
     */
    Result addToResultQueue(String ipAddr, String userName, String key, TokenInfo.Token token);

    /**
     * find token result from the queue.
     * @param ipAddr client ip address
     * @param userName client user name
     * @param key client key
     * @return handle result
     */
    Result findFromResultQueue(String ipAddr, String userName, String key);
}
