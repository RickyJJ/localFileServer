package localfileserver.server.service;

import localfileserver.model.Result;

/**
 * internal service for server module
 * @author Mr.Jiong
 */
public interface TokenService {




    /**
     * add a result of applying request  to a waiting queue, request result is waiting to be handled
     * by the admin.
     * If a request has a failed flag, then it won't to be sent to result queue.
     *
     * Request from the same user will remained the latest one.
     * @param userName client user name
     * @param key a key returned from server when applying succeed
     * @return handle result
     */
    Result addToWaitQueue(String userName, String key);
}
