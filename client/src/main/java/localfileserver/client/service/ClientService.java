package localfileserver.client.service;


import localfileserver.client.entity.User;
import localfileserver.model.Result;

/***
 * Providing Service for clients to acquire token
 * @author Administrator
 * @date 2020/4/4 0004
 */
public interface ClientService {
    /**
     * apply a new token for user
     * @param user client
     * @return apply result. It contains a key to fetch token
     */
    Result applyToken(User user);

    /**
     * if client's apply for token is successful and
     * then can fetch token back.
     * @param user client
     * @param key given from the server if applying token succeed
     * @return if success, then the result contains the token, or does not.
     */
    Result fetchToken(User user, String key);
}
