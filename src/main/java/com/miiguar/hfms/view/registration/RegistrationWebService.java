package com.miiguar.hfms.view.registration;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;

import com.miiguar.hfms.utils.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author bernard
 */
@WebListener
public class RegistrationWebService implements ServletContextListener {
    private static final String TAG = RegistrationWebService.class.getSimpleName();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Executor executor = Executors.newFixedThreadPool(10);
        servletContextEvent.getServletContext().setAttribute("backgroundService", executor);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Log.i(TAG, servletContextEvent.getServletContext().getServletContextName());
    }
}
