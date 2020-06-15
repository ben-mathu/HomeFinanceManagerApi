package com.miiguar.hfms.data.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author bernard
 */
public interface PostgresConnection {
    Connection getConnection(String databaseName) throws SQLException;
}
