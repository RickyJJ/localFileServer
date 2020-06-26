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
     * 检验用户是否申请token的key，如果有则尝试从远程服务获取token
     * @param user current user
     * @param tokenKey token key for applying token
     * @return handle result
     */
    Result userCheckAndFetchToken(User user, String tokenKey);

    /**
     * if client's apply for token is successful and
     * then can fetch token back.
     * @param user client
     * @param key given from the server if applying token succeed
     * @return if success, then the result contains the token, or does not.
     */
    Result fetchToken(User user, String key);
}
