package com.benardmathu.hfms.config;

import java.util.Properties;

/**
 * @author bernard
 */
@Deprecated
public class ConfigureDb extends Configuration {
    public Properties getProperties() {
        return readProperties("db.properties");
    }

    public void setProperties(Properties properties) {
        saveProperties(properties, "db.properties");
    }
}
