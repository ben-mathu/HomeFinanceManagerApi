package com.miiguar.hfms.config;

import java.util.Properties;

/**
 * @author bernard
 */
public class ConfigureDb extends Configuration {
    public Properties getProperties() {
        return readProperties("db.properties");
    }
}
