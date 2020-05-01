package com.miiguar.hfms.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bernard
 */
public class HandleLogging {
    public static void handleLogging(HttpServletRequest req, Logger logger, String tag) {
        Throwable throwable =  (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        logger.setLevel(Level.DEBUG);
        if (throwable != null) {
            logger.error(tag, throwable);
        } else {
            logger.log(Level.INFO, "URL: " + req.getRequestURL());
        }
    }
}
