package com.miiguar.hfms.data.jdbc;

import com.miiguar.hfms.config.ConfigureDb;
import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author bernard
 */
public class JdbcConnection implements PostgresConnection {
    private static final String TAG = JdbcConnection.class.getSimpleName();

    @Override
    public Connection getConnection(String databaseName, String username, String password) {
        ConfigureDb configureDb = new ConfigureDb();
        Properties prop = configureDb.getProperties();
        PoolProperties pool = new PoolProperties();
        pool.setUrl(prop.getProperty("db.url") + (databaseName.isEmpty() ? "" : "/" + databaseName));
        pool.setDriverClassName(prop.getProperty("db.driver"));
        if (username.equals("ben_hfms")) {
            pool.setUsername(prop.getProperty("db.username"));
            pool.setPassword(prop.getProperty("db.password"));
        } else {
            pool.setUsername(username);
            pool.setPassword(password);
        }
        pool.setJmxEnabled(true);
        pool.setTestWhileIdle(false);
        pool.setTestOnBorrow(true);
        pool.setTestOnReturn(false);
        pool.setTimeBetweenEvictionRunsMillis(60000);
        pool.setMaxActive(500);
        pool.setMinEvictableIdleTimeMillis(30000);
        pool.setLogAbandoned(true);
        pool.setRemoveAbandoned(true);
        pool.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource dataSource = new DataSource();
        dataSource.setPoolProperties(pool);
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            Logger logger = Logger.getRootLogger();
            logger.error(TAG, e);
            return null;
        }
    }
}
