package com.miiguar.hfms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import com.miiguar.hfms.utils.Log;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Application
 */
public class Application {
    private static final String TAG = Application.class.getSimpleName();
    private static Logger logger;

    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getRootLogger();
        }

        return logger;
    }    

    public static void main(String[] args) {
        logger = Logger.getRootLogger();
        BasicConfigurator.configure();

        Application main = new Application();
        try {
            File file = main.getFileFromResources("db.properties");
            printFile(file);
        } catch (IOException e) {
            Log.e(TAG, "Error: ", logger, e);
        }
    }

    private static void printFile(File file) throws IOException {
        if (file == null) return;

        try (FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader)) {
                String line;
                while ((line = br.readLine()) != null) {
                    Log.d(TAG, "Configuration file: " + line, logger);
                }
        } catch(IOException e) {
            Log.e(TAG, "Error: ", logger, e);
        }
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
}