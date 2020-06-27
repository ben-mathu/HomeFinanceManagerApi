package com.miiguar.hfms.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author bernard
 */
public interface Dao<T> {
    int save(T item, Connection connection) throws SQLException;
    int update(T item, Connection connection) throws SQLException;
    int delete(T item, Connection connection);
    T get(String id, Connection connection) throws SQLException;
    List<T> getAll(Connection connection) throws SQLException;
    List<T> getAll(String id, Connection connection) throws SQLException;
}
