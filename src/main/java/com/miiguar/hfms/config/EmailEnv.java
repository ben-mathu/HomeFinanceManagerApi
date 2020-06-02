package com.miiguar.hfms.config;

import java.util.Properties;

/**
 * @author bernard
 */
public class EmailEnv extends Configuration {
    public Properties getProperties() {
        return readProperties(System.getProperty("user.home") + "/email.properties");
    }
}
