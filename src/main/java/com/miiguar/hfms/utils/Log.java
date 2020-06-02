package com.miiguar.hfms.utils;

import com.miiguar.hfms.init.AppContextListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * @author bernard
 */
public class Log {
    private Log() {
        throw new UnsupportedOperationException("This Initialization is unsupported!");
    }

    public static void handleLogging(HttpServletRequest req, String tag) {
        // Initialize the application's logger.
        Logger logger = AppContextListener.getLogger();

        Throwable throwable =  (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        logger.setLevel(Level.DEBUG);
        if (throwable != null) {
            logger.error(tag, throwable);
        } else {
            logger.log(Level.INFO, "URL: " + req.getRequestURL());
        }
    }

    public static void d(String tag, String message) {
        Logger logger = AppContextListener.getLogger();
        logger.debug(tag + ": " + message);
    }

    public static void e(String tag, String message, Exception e) {
        Logger logger = AppContextListener.getLogger();
        logger.error(tag + ": " + message + "\n", e);
    }

    public static void w(String tag, String message) {
        Logger logger = AppContextListener.getLogger();
        logger.warn(tag + ": " + message);
    }

    public static void i(String tag, String message) {
        Logger logger = AppContextListener.getLogger();
        logger.info(tag + ": " + message);
    }

    public static void t(String tag, String message) {
        Logger logger = AppContextListener.getLogger();
        logger.trace(tag + ": " + message);
    }
}
