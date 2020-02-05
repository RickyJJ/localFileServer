package org.jiong.filetree.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jiong.filetree.token.HandleToken;

/**
 * @author Mr.Jiong
 */
@Data
@Slf4j
public class User {

    private String name;
    private String infoPath;
    private HandleToken token;

    /**
     * Update user's status in session immediately,
     * when user session invalidated, write user info to file
     * @param token new token info
     */
    public void updateToken(HandleToken token) {
        log.info("Token to update: {}", token);
        this.token = token;
    }
}
