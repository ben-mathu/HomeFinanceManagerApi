package com.miiguar.hfms.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.*;

import com.miiguar.hfms.api.mpesa.GetAccessTokenTask;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.utils.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

/**
 * Application
 */
public class Application implements IntervalChangeListener {
    private static final String TAG = Application.class.getSimpleName();
    private static Logger logger;
    private Properties properties;

    private static final JdbcConnection jdbcConnection = new JdbcConnection();

    private ScheduledExecutorService service;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static File getFile(String fileName) {
        Application app = new Application();
        return app.getFileFromResources(fileName);
    }

    public static void main(String[] args) {
        logger = Logger.getRootLogger();
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%d{ABSOLUTE} [%t] %p %c %x - %m%n")));

        Application main = new Application();

        ConfigureDb configureDb = new ConfigureDb();
        main.setProperties(configureDb.getProperties());
        Properties properties = main.getProperties();

        File file = main.getFileFromResources("db.properties");
        printFile(file);

        main.fetchDarajaAccessToken();

        // create schema if it does not exist
        main.addDb(properties, main);
    }

    public Set<Class<?>> getAnnotatedClasses() {
        Reflections reflections = new Reflections("com.miiguar.hfms", new TypeAnnotationsScanner());
        return reflections.getTypesAnnotatedWith(Table.class, true);
    }

    /**
     * Creates a DB for the user's household.
     * @param prop holds list of predefined strings that define my db
     */
    private void addDb(Properties prop, Application main) {
        String createUsers = "CREATE USER " + prop.getProperty("db.username") + " SUPERUSER ENCRYPTED PASSWORD '" + prop.getProperty("db.password") + "'";
        Connection conn = null;
        PreparedStatement preparedStatement= null;
        try {
            conn = jdbcConnection.getDataSource("").getConnection();
            preparedStatement = conn.prepareStatement(createUsers);

            // create user
            Log.d(TAG, "\n" + preparedStatement.toString() + "\n");
            preparedStatement.execute();

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException e) {
            Log.d(TAG, "Username already exists: " + prop.getProperty("db.username"));
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException throwable) { /* Intentionally blank. */ }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (SQLException throwable) { /* Intentionally blank. */ }
            }
        }

        String createDb = "CREATE DATABASE " + prop.getProperty("db.main_db") + " OWNER " + prop.get("db.username") + " CONNECTION LIMIT=200";

        try {
            conn = jdbcConnection.getDataSource("").getConnection();
            preparedStatement = conn.prepareStatement(createDb);

            // create db
            Log.d(TAG, "\n" + preparedStatement.toString() + "\n");
            preparedStatement.execute();

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException e) {
            Log.d(TAG, "Database already exists. DbName:" + prop.getProperty("db.main_db"));
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException throwable) { /* Intentionally blank. */ }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (SQLException throwable) { /* Intentionally blank. */ }
            }
        }

        // create db
        StringBuilder str;
        str = new StringBuilder();

        // create table based on the annotation provided
        ArrayList<Table> tablesWithConstraint = new ArrayList<>();
        // get list of annotated classes
        Set<Class<?>> classes = main.getAnnotatedClasses();
        for (Class<?> clazz : classes) {

            if (clazz.isAnnotationPresent(Table.class)) {
                Table table = clazz.getAnnotation(Table.class);

                str.append("CREATE TABLE IF NOT EXISTS ").append(table.tableName()).append("(");
                // get list fields of this class
                Field[] fields = clazz.getDeclaredFields();

                for(int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        String type = field.getGenericType().getTypeName();
                        if (column.unique()) {
                            str.append(uniqueColumns(type, column));
                        } else {
                            str.append(columns(type, column));
                        }
                    } else if (field.isAnnotationPresent(PrimaryKey.class)) {
                        PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
                        str.append(primaryKey.columnName())
                                .append(" VARCHAR(").append(primaryKey.characterLength()).append(") PRIMARY KEY")
                                .append(primaryKey.notNull()? " NOT NULL" : "");
                    }

                    if (i != fields.length - 1) {
                        str.append(",");
                    } else {
                        str.append(")");
                    }
                }

                Constraint[] constraints = table.constraint();
                if (constraints.length > 0) {
                    tablesWithConstraint.add(table);
                }
            }

            try {
                conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
                preparedStatement = conn.prepareStatement(str.toString());

                Log.d(TAG, "\n" + preparedStatement.toString() + "\n");
                preparedStatement.execute();
                str = new StringBuilder();

                preparedStatement.close();
                preparedStatement = null;
                conn.close();
                conn = null;
            } catch (SQLException throwable) {
                Log.e(TAG, "Error create tables: ", throwable);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                        if (conn.isClosed()) Log.d(TAG, "Connection closed");
                        conn = null;
                    } catch (SQLException throwable) { /* Intentionally blank. */ }
                }

                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                        if (preparedStatement.isClosed()) Log.d(TAG, "PreparedStatement closed.");
                        preparedStatement = null;
                    } catch (SQLException throwable) { /* Intentionally blank. */ }
                }
            }
        }

        for (Table table : tablesWithConstraint) {
            Constraint[] constraints = table.constraint();
            for (Constraint constraint : constraints) {
                String columnName = constraint.columnName();
                str = new StringBuilder();
                str.append(" ALTER TABLE ").append(table.tableName());
                str.append(" ADD CONSTRAINT ").append(constraint.name())
                        .append(" FOREIGN KEY (").append(columnName).append(") REFERENCES ")
                        .append(constraint.tableName()).append("(").append(columnName).append(")")
                        .append(" ON DELETE CASCADE");
                try {
                    conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
                    preparedStatement = conn.prepareStatement(str.toString());

                    Log.d(TAG, "\n" + preparedStatement.toString() + "\n");
                    preparedStatement.execute();

                    preparedStatement.close();
                    preparedStatement = null;
                    conn.close();
                    conn = null;
                } catch (SQLException throwable) {
                    Log.d(TAG, "Error creating constraints: " + str.toString());
                } finally {
                    if (conn != null) {
                        try { conn.close(); conn = null; } catch (SQLException throwable) { /* Intentionally blank. */ }
                    }

                    if (preparedStatement != null) {
                        try { preparedStatement.close(); preparedStatement = null; } catch (SQLException throwable) { /* Intentionally blank. */ }
                    }
                }
            }
        }
    }

    private String uniqueColumns(String type, Column column) {
        StringBuilder str = new StringBuilder();
        if (type.contains("String")) {
            str.append(column.columnName())
                    .append(" VARCHAR(").append(column.characterLength()).append(") ")
                    .append("UNIQUE").append(column.notNull()? " NOT NULL" : "");
        }

        return str.toString();
    }

    private String columns(String type, Column column) {
        StringBuilder str = new StringBuilder();
        if (type.contains("String")) {
            str.append(column.columnName())
                    .append(" VARCHAR(").append(column.characterLength()).append(")").append(column.notNull()? " NOT NULL" : "");
        } else if (type.equalsIgnoreCase("int")) {
            str.append(column.columnName())
                    .append(" INTEGER").append(column.notNull()? " NOT NULL" : "");
        } else if (type.equalsIgnoreCase("double")) {
            str.append(column.columnName())
                    .append(" NUMERIC(8,2)").append(column.notNull()? " NOT NULL" : "");
        } else if (type.equalsIgnoreCase("boolean")) {
            str.append(column.columnName())
                    .append(" BOOLEAN");
        }

        return str.toString();
    }

    private static void printFile(File file) {
        if (file == null) return;

        try (FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                Log.d(TAG, "Configuration file: " + line);
            }
        } catch(IOException e) {
            Log.e(TAG, "Error: ", e);
        }
    }

    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(fileName);

        if (url == null) {
            throw new IllegalArgumentException("File not found");
        } else {
            return new File(url.getFile());
        }
    }

    private void fetchDarajaAccessToken() {
        GetAccessTokenTask.setListener(this);

        service = Executors.newScheduledThreadPool(5);
        service.scheduleWithFixedDelay(new GetAccessTokenTask(), 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void setIntervals(long intervals) {
        GetAccessTokenTask.setListener(null);
        GetAccessTokenTask task = new GetAccessTokenTask();

        service.shutdown();

        service = Executors.newScheduledThreadPool(5);
        service.scheduleWithFixedDelay(task, intervals, intervals, TimeUnit.SECONDS);
    }
}