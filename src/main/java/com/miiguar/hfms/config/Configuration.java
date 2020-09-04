package com.miiguar.hfms.config;

import java.io.*;
import java.util.Properties;

import com.miiguar.hfms.init.Application;
import com.miiguar.hfms.utils.Log;

public class Configuration {
    private static final String TAG = Configuration.class.getSimpleName();
    
    public Properties readProperties(String fileName) {
        Properties properties = new Properties();
        try(FileReader reader = new FileReader(Application.getFile(fileName));
            BufferedReader br = new BufferedReader(reader)) { 
            properties.load(br);
        } catch (IOException e) {
            Log.e(TAG, "Configuration file: ", e);
        }
        return properties;
    }

    public void saveProperties(Properties properties, String fileName) {
        try(FileWriter writer = new FileWriter(Application.getFile(fileName));
            BufferedWriter br = new BufferedWriter(writer)) {
            properties.store(br, "Saved token");
        } catch (IOException e) {
            Log.e(TAG, "Configuration file: ", e);
        }
    }
}