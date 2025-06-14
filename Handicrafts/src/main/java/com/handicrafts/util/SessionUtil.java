package com.handicrafts.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {
    private static SessionUtil session;

    public static SessionUtil getInstance() {
        if (session == null) {
            session = new SessionUtil();
        }
        return session;
    }

    public HttpSession getSession(HttpServletRequest req) {
        return req.getSession();
    }

    public void putValue(HttpServletRequest req, String key, Object value) {
        req.getSession().setAttribute(key, value);
    }

    public Object getValue(HttpServletRequest req, String key) {
        return req.getSession().getAttribute(key);
    }

    public void removeValue(HttpServletRequest req, String key) {
        req.getSession().removeAttribute(key);
    }

}