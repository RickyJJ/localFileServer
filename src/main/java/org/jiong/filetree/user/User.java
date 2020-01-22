package org.jiong.filetree.user;

import lombok.Data;
import org.jiong.filetree.token.HandleToken;

/**
 * @author Mr.Jiong
 */
@Data
public class User {

    private String name;
    private String infoPath;
    private HandleToken token;

    public void updateToken(HandleToken token) {
        // todo
    }
}
