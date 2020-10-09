package com.antzuhl.kibana.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author AntzUhl
 * @Date 15:37
 */
public class CookieUtil {

    private static final Integer MAX_AGE = Integer.MAX_VALUE;

    private static final String COOKIE_PATH = "/";

    public static void set(HttpServletResponse response, String key, String value,
                           boolean isRemember) {
        int age = isRemember ? MAX_AGE : -1;
        set(response, key, value, null, COOKIE_PATH, age, true);
    }

    public static void set(HttpServletResponse response, String key, String value,
                           String domain, String path, int maxAge, boolean isHttpOnly) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        cookie.setHttpOnly(isHttpOnly);
        cookie.setMaxAge(maxAge);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    public static Cookie get(HttpServletRequest request, String key) {
        Cookie[]cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie;
            }
        }
        return null;
    }

    public static String getValue(HttpServletRequest request, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public static void remove(HttpServletRequest request,
                              HttpServletResponse response, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            set(response, key, "", null, COOKIE_PATH, 0, true);
        }
    }

}
