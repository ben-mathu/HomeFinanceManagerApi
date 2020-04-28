package com.miiguar.hfms.config;

import com.miiguar.hfms.data.jdbc.JdbcConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author bernard
 */
public class ConfigureDb {
    public Properties readProperties() {
        Properties properties = new Properties();
        Path path = Paths.get("/src/main/resources/db.properties");

        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void main(String[] args) {
        ConfigureDb configureDb = new ConfigureDb();
        Properties prop = configureDb.readProperties();

        String url = prop.getProperty("db.url");
        String username = prop.getProperty("db.username");
        String password = prop.getProperty("db.password");

        JdbcConnection jdbcConnection = new JdbcConnection();
        try(Connection con = jdbcConnection.getConnection("", username, password, url)) {

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
