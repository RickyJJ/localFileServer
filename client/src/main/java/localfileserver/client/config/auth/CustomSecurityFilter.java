package localfileserver.client.config.auth;

import localfileserver.client.config.param.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *
 * @author Administrator
 * @date 2020/5/23
 */
@Slf4j
public class CustomSecurityFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationTrustResolver trustResolver;

    public CustomSecurityFilter() {
        super(AnyRequestMatcher.INSTANCE);
        this.setAuthenticationManager(getProviderManager(new CustomAuthProvider()));
        this.trustResolver = new AuthenticationTrustResolverImpl();
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
            if (log.isDebugEnabled()) {
                log.info("ignore");
            }
        });
        this.setContinueChainBeforeSuccessfulAuthentication(true);
    }

    /**
     * Performs actual authentication.
     * <p>
     * The implementation should do one of the following:
     * <ol>
     * <li>Return a populated authentication token for the authenticated user, indicating
     * successful authentication</li>
     * <li>Return null, indicating that the authentication process is still in progress.
     * Before returning, the implementation should perform any additional work required to
     * complete the process.</li>
     * <li>Throw an <tt>AuthenticationException</tt> if the authentication process fails</li>
     * </ol>
     *
     * @param request  from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     *                 redirect as part of a multi-stage authentication process (such as OpenID).
     * @return the authenticated user token, or null if authentication is incomplete.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        // 用户token信息更新后需要重新加载detail信息
        boolean userNeedUpdate = request.getSession().getAttribute(Dict.User.UPDATED_FLAG) != null;
        if (userNeedUpdate) {
            authentication = null;
            request.getSession().removeAttribute(Dict.User.UPDATED_FLAG);
        }

        if (authentication == null || trustResolver.isAnonymous(authentication)) {
            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(request.getRemoteAddr(), "2", AuthorityUtils.NO_AUTHORITIES);
            user.setDetails(null);
            emptyContext.setAuthentication(user);
            // 设置context，防止AnonymousAuthenticationFilter生效
            SecurityContextHolder.setContext(emptyContext);
            authentication = user;
        }

        return this.getAuthenticationManager().authenticate(authentication);

    }

    public ProviderManager getProviderManager(CustomAuthProvider customAuthProvider) {
        ProviderManager providerManager = new ProviderManager(customAuthProvider);

        this.setAuthenticationManager(providerManager);
        return providerManager;
    }

}
