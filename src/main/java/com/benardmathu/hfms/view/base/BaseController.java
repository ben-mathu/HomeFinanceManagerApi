package com.benardmathu.hfms.view.base;

import com.google.gson.Gson;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.HashMap;

import static com.benardmathu.hfms.data.utils.DbEnvironment.USER_ID;
import static com.benardmathu.hfms.utils.Constants.*;

/**
 * @author bernard
 */
public abstract class BaseController {

    public Gson gson = new Gson();
    public PrintWriter writer;

    protected static HashMap<String, String>  map = new HashMap<>();

    protected static HashMap<String, String> getMap() {
        if (map.isEmpty()) {
            map.put(MESSAGES, URL_MESSAGES);
            map.put(MEMBERS, URL_MEMBERS);
            map.put(SETTINGS, URL_SETTINGS);
            map.put(GUIDE, URL_GUIDE);
        }

        return map;
    }

    protected String getTokenFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }

    protected String getUserIdFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (USER_ID.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
}