package com.miiguar.hfms.config;

import java.util.Properties;

/**
 * ConfigureApp
 */
public class ConfigureApp extends Configuration {

    public Properties getProperties() {
        return readProperties("hfms.properties");
    }
}