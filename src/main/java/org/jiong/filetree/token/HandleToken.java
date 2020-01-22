package org.jiong.filetree.token;

/**
 * a token to allow person to upload or download file.
 * @author Mr.Jiong
 */
public interface HandleToken {

    /**
     * unique key for a token
     * @return a unique string
     */
    String value();

    /**
     * one token is just for one person.
     * and when token is used for one person, it is return false.
     * @return true for this token can be used for one person.
     */
    boolean isAvailable();

    default boolean isTemporal() {
        return this instanceof ExpiredHandleToken;
    }
}
