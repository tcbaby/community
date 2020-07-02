package com.tcbaby.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tcbaby
 * @date 20/05/05 21:23
 */
public class CookieUtil {

    @Value("${server.servlet.context-path}")
    private static String CONTEXT_PATH;

    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        addCookie(response, cookieName, cookieValue, -1);
    }

    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int expire) {
        if (StringUtils.isBlank(cookieName) || StringUtils.isBlank(cookieValue)) {
            throw new IllegalArgumentException("cookie不能为空！");
        }
        doAddCookie(response, cookieName, cookieValue, expire);
    }

    private final static void doAddCookie(HttpServletResponse response, String cookieName, String cookieValue, int expire) {
        if (response != null && StringUtils.isNotBlank(cookieName) && StringUtils.isNotBlank(cookieName)) {
            Cookie cookie = new Cookie(cookieName, cookieValue);
            cookie.setPath(CONTEXT_PATH);
            cookie.setMaxAge(expire);
            response.addCookie(cookie);
        }
    }

    public static String getCookie(HttpServletRequest request, String cookieName) {
        if (request != null && StringUtils.isNotBlank(cookieName)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(cookieName)) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return null;
    }

    public static void delCookie(HttpServletResponse response, String cookieName) {
        doAddCookie(response, cookieName, "", 0);
    }
}
