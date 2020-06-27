package com.miiguar.hfms.data.jdbc;

import com.miiguar.hfms.config.ConfigureDb;
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

    private Connection connection = null;

    @Override
    public Connection getConnection(String property) throws SQLException {
        ConfigureDb configureDb = new ConfigureDb();
        Properties prop = configureDb.getProperties();
        String mainDb = prop.getProperty("default.db");
        String password = prop.getProperty("default.password");
        String username = prop.getProperty("default.username");
        if (property.equals(prop.getProperty("db.main_db"))) {
            mainDb = property;
            password = prop.getProperty("db.password");
            username = prop.getProperty("db.username");
        }

        PoolProperties pool = new PoolProperties();
        pool.setUrl(prop.getProperty("db.url") + "/" + mainDb);
        pool.setDriverClassName(prop.getProperty("db.driver"));
        pool.setUsername(username);
        pool.setPassword(password);
        pool.setJmxEnabled(true);
        pool.setTestWhileIdle(false);
        pool.setTestOnBorrow(true);
        pool.setTestOnReturn(false);
        pool.setTimeBetweenEvictionRunsMillis(1000);
        pool.setMaxActive(500);
        pool.setMinEvictableIdleTimeMillis(500);
        pool.setMaxIdle(50);
        pool.setLogAbandoned(true);
        pool.setRemoveAbandonedTimeout(1000);
        pool.setRemoveAbandoned(true);
        pool.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource dataSource = new DataSource();
        dataSource.setPoolProperties(pool);

        connection = dataSource.getConnection();
        return connection;
    }

    public void disconnect() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
    }
}
