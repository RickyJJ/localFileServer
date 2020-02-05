package org.jiong.filetree.controller;

import org.jiong.filetree.common.constants.AppConst;
import org.jiong.filetree.common.util.HttpKit;
import org.jiong.filetree.common.util.SessionKit;
import org.jiong.filetree.user.User;
import org.jiong.filetree.user.UserFactory;
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
abstract class BaseController {

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

    protected User getCurrentUser() {
        User currentUser = (User) getSessionAttr(AppConst.CURRENT_USER);

        if (currentUser == null) {

            String ipAddress = HttpKit.getIpAddress(getRequest());
            currentUser = UserFactory.load(ipAddress);
        }

        return currentUser;
    }
}
