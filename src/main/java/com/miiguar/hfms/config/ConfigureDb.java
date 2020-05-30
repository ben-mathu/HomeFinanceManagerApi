package com.miiguar.hfms.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.miiguar.hfms.utils.Log;

import org.apache.log4j.Logger;

/**
 * @author bernard
 */
public class ConfigureDb {
    private static final String TAG = ConfigureDb.class.getSimpleName();

    Logger logger = Logger.getLogger(ConfigureDb.class);
    public Properties readProperties() {
        Properties properties = new Properties();
        Path path = Paths.get("db.properties");
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) { 
            properties.load(ConfigureDb.class.getResourceAsStream("db.properties"));
        } catch (IOException e) {
            Log.e(TAG, "Configuration file: ", logger, e);
        }
        return properties;
    }
}
