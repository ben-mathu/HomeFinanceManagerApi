package com.benardmathu.hfms.data.jdbc;

import org.apache.tomcat.jdbc.pool.DataSource;

import java.sql.SQLException;

/**
 * @author bernard
 */
public interface PostgresConnection {
    DataSource getDataSource(String property) throws SQLException;
}
