package com.benardmathu.hfms.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.benardmathu.hfms.utils.Log;

/**
 * @author bernard
 *
 * This class is called whenever the tomcat starts the web app.
 * @see "https://docs.oracle.com/javaee/6/api/javax/servlet/ServletContextListener.html"
 */
public class AppContextListener implements ServletContextListener {
    private static final String TAG = AppContextListener.class.getSimpleName();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Log.i(TAG, "The server started. Path: " + event.getServletContext().getContextPath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        Log.i(TAG, "The application closed");
    }
}
