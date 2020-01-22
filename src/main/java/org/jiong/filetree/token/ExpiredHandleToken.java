package org.jiong.filetree.token;

import java.time.Instant;

/**
 * @author Mr.Jiong
 */
public interface ExpiredHandleToken extends HandleToken {

    /**
     * check the token is expired.
     * @return true for the token is available
     */
    boolean isExpired();

    /**
     * expire time for this token
     * @return time for long
     */
    Instant getExpiredTime();
}
