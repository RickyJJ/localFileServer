package org.jiong.filetree.token;

import org.jiong.protobuf.TokenInfo;

/**
 * implement for HandleToken
 * @author Mr.Jiong
 */
public class HandleTokenWrapper implements HandleToken {

    protected TokenInfo.Token token;

    @Override
    public String value() {
        return token.getValue();
    }

    @Override
    public boolean isAvailable() {
        return token != null;
    }

    protected void setToken(TokenInfo.Token token) {
        this.token = token;
    }
    public static HandleToken asHandleToken(TokenInfo.Token token) {
        HandleTokenWrapper handleTokenWrapper = new HandleTokenWrapper();
        handleTokenWrapper.setToken(token);

        return handleTokenWrapper;
    }
}
