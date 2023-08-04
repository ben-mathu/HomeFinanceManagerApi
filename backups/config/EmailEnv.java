package com.benardmathu.hfms.config;

import java.util.Properties;

/**
 * @author bernard
 */
public class EmailEnv extends Configuration {
    public Properties getProperties() {
        return readProperties("email.properties");
    }
}
