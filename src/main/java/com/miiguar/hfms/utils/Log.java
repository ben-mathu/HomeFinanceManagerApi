package com.miiguar.hfms.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * @author bernard
 */
public class Log {
    public static void handleLogging(HttpServletRequest req, Logger logger, String tag) {
        Throwable throwable =  (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        logger.setLevel(Level.DEBUG);
        if (throwable != null) {
            logger.error(tag, throwable);
        } else {
            logger.log(Level.INFO, "URL: " + req.getRequestURL());
        }
    }

    public static void d(String tag, String message, Logger logger) {
        logger.debug(tag + ": " + message);
    }

    public static void e(String tag, String message, Logger logger, Exception e) {
        logger.error(tag + ": " + message + "\n", e);
    }

    public static void w(String tag, String message, Logger logger) {
        logger.warn(tag + ": " + message);
    }

    public static void i(String tag, String message, Logger logger) {
        logger.info(tag + ": " + message);
    }

    public static void t(String tag, String message, Logger logger) {
        logger.trace(tag + ": " + message);
    }
}
