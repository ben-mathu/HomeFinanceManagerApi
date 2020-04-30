package com.miiguar.hfms.init;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author bernard
 *
 * This class is called whenever the tomcat starts the web app.
 * @see "https://docs.oracle.com/javaee/6/api/javax/servlet/ServletContextListener.html"
 */
public class AppContextListener implements ServletContextListener {
    private static final String TAG = AppContextListener.class.getSimpleName();
    private Logger logger = Logger.getRootLogger();
    @Override
    public void contextInitialized(ServletContextEvent event) {
        BasicConfigurator.configure();

        logger.info("Web Application started.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
