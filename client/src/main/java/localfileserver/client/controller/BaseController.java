package localfileserver.client.controller;

import localfileserver.client.entity.User;
import localfileserver.client.entity.UserFactory;
import localfileserver.client.kit.SessionKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/***
 *
 * @author DEV049104
 * @date 2019/12/25
 */
@Slf4j
abstract class BaseController {

    private static final String USER_OBJ = "USER_OBJECT";

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    protected BaseController setAttr(String attrName, Object attrVal) {
        RequestContextHolder.currentRequestAttributes().setAttribute(attrName, attrVal, RequestAttributes.SCOPE_REQUEST);
        return this;
    }

    protected Object getAttr(String attrName) {
        return RequestContextHolder.currentRequestAttributes().getAttribute(attrName, RequestAttributes.SCOPE_REQUEST);
    }

    protected Object getSessionAttr(String attrName) {
        return SessionKit.getSession().getAttribute(attrName);
    }

    protected Object setSessionAttr(String attrName, Object attrVal) {
        SessionKit.getSession().setAttribute(attrName, attrVal);
        return this;
    }

    /**
     * 返回重定向的方法
     *
     * @param url 重定向的地址
     * @return url to redirect
     */
    protected String redirectTo(String url) {
        Objects.requireNonNull(url, "Url for redirect cant be null");
        return "redirect:" + url;
    }

    /**
     * 返回转发的url
     *
     * @param url 转发的目标url
     * @return url to forward
     */
    protected String forward(String url) {
        Objects.requireNonNull(url, "Url for forward cant be null");

        List<String> oldUrl = (List<String>) getAttr("old_url");
        if (oldUrl == null) {
            oldUrl = new LinkedList<>();
        }

        oldUrl.add(url);
        return "forward:" + url;
    }

    /**
     * Get user info from session or load user info if null in session
     * @return user info
     */
    protected User getCurrentUser() {
        User user = (User) SessionKit.getSession().getAttribute(USER_OBJ);

        if (log.isDebugEnabled()) {
            log.debug("User exist: {}", user != null);
        }

        if (user == null) {
            user = UserFactory.load(getRequest().getRemoteAddr());
            SessionKit.getSession().setAttribute(USER_OBJ, user);
        }

        return user;
    }
}
