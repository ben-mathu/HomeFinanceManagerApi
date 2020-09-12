package com.benardmathu.hfms.data.jdbc;

import com.benardmathu.hfms.config.ConfigureDb;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.SQLException;
import java.util.Properties;

/**
 * @author bernard
 */
public class JdbcConnection implements PostgresConnection {
    private static final String TAG = JdbcConnection.class.getSimpleName();

    @Override
    public DataSource getDataSource(String property) {
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
        pool.setValidationQuery("SELECT 1");

        pool.setMaxActive(10);
        pool.setMaxIdle(10);
        pool.setMinIdle(5);
        pool.setMaxWait(30000);
        pool.setInitialSize(1);
        pool.setTimeBetweenEvictionRunsMillis(5000);
        pool.setMinEvictableIdleTimeMillis(60000);
        pool.setRemoveAbandonedTimeout(60);

        pool.setLogAbandoned(true);
        pool.setRemoveAbandoned(true);
        pool.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        DataSource dataSource = new DataSource();
        dataSource.setPoolProperties(pool);

        return dataSource;
    }
}
