package com.techsquad;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LoginCookies {
    static final String JWT_ACCESS_COOKIE_NAME = "access_token_cookie";
    static final String JWT_REFRESH_COOKIE_NAME = "refresh_token_cookie";
    static final String JWT_ACCESS_CSRF_COOKIE_NAME = "csrf_access_token";
    static final String JWT_REFRESH_CSRF_COOKIE_NAME = "csrf_refresh_token";

    private String access;
    private String refresh;
    private String csrf_access;
    private String csrf_refresh;

    LoginCookies(List<HttpCookie> cookieList) throws NullPointerException {
        boolean result = this.setCookies(cookieList);
        if (!result) throw new NullPointerException("Invalid cookie keys");
    }

    LoginCookies(Map<String, String> cookieList) {
        this.setCookies(cookieList);
    }

    private boolean setCookies(List<HttpCookie> cookieList) {
        Map<String, String> jwtCookies = new HashMap<>();
        for (HttpCookie cookie : cookieList) {
            jwtCookies.put(cookie.getName(), cookie.getValue());
        }
        if (jwtCookies.containsKey(LoginCookies.JWT_ACCESS_COOKIE_NAME) && jwtCookies.containsKey(LoginCookies.JWT_REFRESH_COOKIE_NAME)
                && jwtCookies.containsKey(LoginCookies.JWT_ACCESS_CSRF_COOKIE_NAME) && jwtCookies.containsKey(LoginCookies.JWT_REFRESH_CSRF_COOKIE_NAME)) {
            this.access = jwtCookies.get(LoginCookies.JWT_ACCESS_COOKIE_NAME);
            this.refresh = jwtCookies.get(LoginCookies.JWT_REFRESH_COOKIE_NAME);
            this.csrf_access = jwtCookies.get(LoginCookies.JWT_ACCESS_CSRF_COOKIE_NAME);
            this.csrf_refresh = jwtCookies.get(LoginCookies.JWT_REFRESH_CSRF_COOKIE_NAME);
            return true;
        } else {
            return false;
        }
    }

    private void setCookies(Map<String, String> jwtCookies) {
        if (jwtCookies.containsKey(LoginCookies.JWT_ACCESS_COOKIE_NAME) && jwtCookies.containsKey(LoginCookies.JWT_REFRESH_COOKIE_NAME)
                && jwtCookies.containsKey(LoginCookies.JWT_ACCESS_CSRF_COOKIE_NAME) && jwtCookies.containsKey(LoginCookies.JWT_REFRESH_CSRF_COOKIE_NAME)) {
            this.access = jwtCookies.get(LoginCookies.JWT_ACCESS_COOKIE_NAME);
            this.refresh = jwtCookies.get(LoginCookies.JWT_REFRESH_COOKIE_NAME);
            this.csrf_access = jwtCookies.get(LoginCookies.JWT_ACCESS_CSRF_COOKIE_NAME);
            this.csrf_refresh = jwtCookies.get(LoginCookies.JWT_REFRESH_CSRF_COOKIE_NAME);
        }
    }

    String getAccessCookie() {
        return this.access;
    }

    String getRefreshCookie() {
        return this.refresh;
    }

    String getCsrfAccessCookie() {
        return this.csrf_access;
    }

    String getCsrfRefreshCookie() {
        return this.csrf_refresh;
    }


}
