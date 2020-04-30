package com.miiguar.hfms.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author bernard
 */
public class ConfigureDb {
    public Properties readProperties() {
        Properties properties = new Properties();
        Path path = Paths.get("src/main/resources/db.properties");

        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
