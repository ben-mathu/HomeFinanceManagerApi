/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benardmathu.hfms.data;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Base class for all data access classes
 * @author bernard
 * @param <T> A class whose variables are changed or modified
 */
public abstract class BaseDao<T> {
    public final String TAG = this.getClass().getSimpleName();

    public final JdbcConnection jdbcConnection;
    public final ConfigureDb db;
    public final Properties prop;

    public BaseDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }
    
    public T save(T item) { return null; }
    public int update(T item) { return 0; }
    public int delete(T item) { return 0; }
    public T get(String id) { return null; }
    public List<T> getAll() { return null; }
    public List<T> getAll(String id) { return null; }
    public int saveAll(ArrayList<T> items) { return 0; }
}
