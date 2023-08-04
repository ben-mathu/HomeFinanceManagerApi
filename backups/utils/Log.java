package com.benardmathu.hfms.utils;

import com.benardmathu.hfms.init.AppContextListener;
import com.benardmathu.hfms.init.Application;
import org.slf4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * @author bernard
 */
public class Log {
    private static Logger logger = Application.getLogger();
    private Log() {
        throw new UnsupportedOperationException("This Initialization is unsupported!");
    }

    public static void handleLogging(HttpServletRequest req, String tag) {
        // Initialize the application's logger.

        Throwable throwable =  (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (throwable != null) {
            logger.error(tag, throwable);
        } else {
            logger.info("URL: " + req.getRequestURL() + " " + tag);
        }
    }

    public static void d(String tag, String message) {
        logger.debug(tag + ": " + message);
    }

    public static void e(String tag, String message, Exception e) {
        logger.error(tag + ": " + message + "\n", e);
    }

    public static void w(String tag, String message) {
        logger.warn(tag + ": " + message);
    }

    public static void i(String tag, String message) {
        logger.info(tag + ": " + message);
    }

    public static void t(String tag, String message) {
        logger.trace(tag + ": " + message);
    }
}
