package com.miiguar.hfms.view.registration;

import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author bernard
 */
@WebListener
public class RegistrationWebService implements ServletContextListener {
    private AsyncContext asyncContext;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Executor executor = Executors.newFixedThreadPool(10);
        servletContextEvent.getServletContext().setAttribute("backgroundService", executor);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
