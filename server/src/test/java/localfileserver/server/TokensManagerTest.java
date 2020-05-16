package localfileserver.server;

import localfileserver.server.manager.TokensManager;
import localfileserver.token.ExpiredHandleToken;
import localfileserver.token.HandleToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokensManagerTest extends BaseTest {

    @Before
    public void setUp() {
        TokensManager.init();
    }
    @Test
    public void totalSize() {
        Assert.assertEquals(TokensManager.totalSize(), TokensManager.normalTokenCount() + TokensManager.temporalTokenCount());

        Assert.assertEquals(TokensManager.totalSize(), 20);
    }

    @Test
    public void expireTokenCount() {
        Assert.assertTrue(TokensManager.temporalTokenCount() > 0);
    }

    @Test
    public void normalTokenCount() {
        Assert.assertTrue(TokensManager.normalTokenCount() > 0);
    }

    @Test
    public void newToken() {
        HandleToken handleToken = TokensManager.newToken();

        Assert.assertNotNull(handleToken);
        Assert.assertNotNull(handleToken.value());

    }

    @Test
    public void testNewToken() {
        HandleToken handleToken = TokensManager.newToken(true);

        Assert.assertTrue(handleToken instanceof ExpiredHandleToken);

        ExpiredHandleToken handleToken1 = (ExpiredHandleToken) handleToken;
        Assert.assertNotNull(handleToken1.getExpiredTime());
    }

    @Test
    public void init() {
        TokensManager.init();

        Assert.assertEquals(30, TokensManager.temporalTokenCount());
        Assert.assertEquals(30, TokensManager.normalTokenCount());
    }

}
