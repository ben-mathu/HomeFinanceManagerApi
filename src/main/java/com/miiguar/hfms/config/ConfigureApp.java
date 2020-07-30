package com.miiguar.hfms.config;

import java.util.Properties;

/**
 * ConfigureApp
 */
public class ConfigureApp extends Configuration {
    private Properties properties;

    public Properties getProperties() {
        properties = readProperties("hfms.properties");
        return properties;
    }

    public void setProperties(Properties prop) {
        saveProperties(prop, "hfms.properties");
    }
}