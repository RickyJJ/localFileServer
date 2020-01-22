package org.jiong.filetree.common.util;

import org.junit.Test;

/***
 * Created by Administrator on 2020/1/23 0023.
 */
public class TokenKitTest {

    @Test
    public void newToken() {
        String newToken = TokenKit.newToken();
        System.out.println(newToken);
    }
}
