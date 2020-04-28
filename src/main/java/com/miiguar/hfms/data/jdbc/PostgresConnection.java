package com.miiguar.hfms.data.jdbc;

import java.sql.Connection;

/**
 * @author bernard
 */
public interface PostgresConnection {
    Connection getConnection(String databaseName, String username, String password, String url);
}
