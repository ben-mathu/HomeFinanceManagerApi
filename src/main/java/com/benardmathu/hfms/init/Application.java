package com.benardmathu.hfms.init;

import java.io.File;
import java.net.URL;
import java.util.concurrent.*;

import com.benardmathu.hfms.api.transaction.GetAccessTokenTask;
import com.benardmathu.hfms.utils.IntervalChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application
 */
public class Application implements IntervalChangeListener {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private ScheduledExecutorService service;

    public static Logger getLogger() {
        return logger;
    }

    public static File getFile(String fileName) {
        Application app = new Application();
        return app.getFileFromResources(fileName);
    }

    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(fileName);

        if (url == null) {
            throw new IllegalArgumentException("File not found");
        } else {
            return new File(url.getFile());
        }
    }

    @Override
    public void setIntervals(long intervals) {
        GetAccessTokenTask.setListener(null);
        GetAccessTokenTask task = new GetAccessTokenTask();

        service.shutdown();

        service = Executors.newScheduledThreadPool(5);
        service.scheduleWithFixedDelay(task, intervals, intervals, TimeUnit.SECONDS);
    }
}