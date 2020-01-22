package org.jiong.filetree.common.util;

import java.util.Base64;

/**
 * @author Mr.Jiong
 */
public class TokenKit {
    public static String encode(String uniqueKey) {
        return Base64.getEncoder().encodeToString(uniqueKey.getBytes());
    }

    public static String newToken() {
        // todo impl token creator
        return null;
    }
}
