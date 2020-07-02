package com.miiguar.hfms.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.utils.Log;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

/**
 * Application
 */
public class Application {
    private static final String TAG = Application.class.getSimpleName();
    private static Logger logger;
    private Properties properties;

    private static Connection connection;
    private static JdbcConnection jdbcConnection;

    public static Connection getConnection(String property) throws SQLException {
        if (connection == null) {
            jdbcConnection = new JdbcConnection();
            connection = jdbcConnection.getConnection(property);
        }
        return connection;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static Logger getLogger() {
        if (logger == null) {
            main(null);
        }

        return logger;
    }

    public static File getFile(String fileName) {
        Application app = new Application();
        return app.getFileFromResources(fileName);
    }

    public static void main(String[] args) {
        logger = Logger.getRootLogger();
        BasicConfigurator.configure();

        Application main = new Application();

        ConfigureDb configureDb = new ConfigureDb();
        main.setProperties(configureDb.getProperties());
        Properties properties = main.getProperties();

        try {
            File file = main.getFileFromResources("db.properties");
            printFile(file);

            // create schema if it does not exist
            main.addDb(properties, getConnection(""), main);
        } catch (IOException | SQLException e) {
            Log.e(TAG, "Error: ", e);
        } finally {
            try {
                if (!connection.isClosed())
                    connection.close();
                connection = null;
                jdbcConnection.disconnect();
                jdbcConnection = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public Set<Class<?>> getAnnotatedClasses() {
        Reflections reflections = new Reflections("com.miiguar.hfms", new TypeAnnotationsScanner());
        return reflections.getTypesAnnotatedWith(Table.class, true);
    }

    /**
     * Creates a DB for the user's household.
     * @param prop holds list of predefined strings that define my db
     * @param connection JDBC connection
     * @throws SQLException thrown when an sqlexception is triggered.
     */
    private void addDb(Properties prop, Connection connection, Application main) throws SQLException {
        try {
            // create user
            PreparedStatement createUser = connection.prepareStatement(
                    "CREATE USER " + prop.getProperty("db.username") + " SUPERUSER ENCRYPTED PASSWORD '" + prop.getProperty("db.password") + "'"
            );
            Log.d(TAG, "\n" + createUser.toString() + "\n");
            createUser.execute();
        } catch (SQLException e) {
            Log.e(TAG, "Username already exists", e);
        }

        try {
            // create db
            PreparedStatement createDbSmt = connection.prepareStatement(
                    "CREATE DATABASE " + prop.getProperty("db.main_db") + " OWNER " + prop.get("db.username")
            );
            Log.d(TAG, "\n" + createDbSmt.toString() + "\n");
            createDbSmt.execute();
        } catch (SQLException e) {
            Log.e(TAG, "Database already exists", e);
        }

        connection = jdbcConnection.getConnection(prop.getProperty("db.main_db"));

        // create db
        StringBuilder str = new StringBuilder();
        str = new StringBuilder();

        // create table based on the annotation provided
        ArrayList<Table> tablesWithConstraint = new ArrayList<>();
        // get list of annotated classes
        Set<Class<?>> classes = main.getAnnotatedClasses();
        for (Class<?> clazz : classes) {

            if (clazz.isAnnotationPresent(Table.class)) {
                Table table = (Table) clazz.getAnnotation(Table.class);

                str.append(" CREATE TABLE IF NOT EXISTS ").append(table.tableName()).append("(");
                // get list fields of this class
                Field[] fields = clazz.getDeclaredFields();

                for(int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.isAnnotationPresent(Column.class)) {
                        Column column = (Column) field.getAnnotation(Column.class);
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
            PreparedStatement createSchema = connection.prepareStatement(str.toString());
            Log.d(TAG, "\n" + createSchema.toString() + "\n");
            createSchema.execute();
            str = new StringBuilder();
        }

        for (Table table : tablesWithConstraint) {
            Constraint[] constraints = table.constraint();
            for (Constraint constraint : constraints) {
                String columnName = constraint.columnName();
                str = new StringBuilder();
                str.append(" ALTER TABLE ").append(table.tableName());
                str.append(" ADD CONSTRAINT ").append(constraint.name())
                        .append(" FOREIGN KEY (").append(columnName).append(") REFERENCES ")
                        .append(constraint.tableName()).append("(").append(columnName).append(")");
                PreparedStatement alter = connection.prepareStatement(str.toString());
                Log.d(TAG, "\n" + alter.toString() + "\n");
                alter.execute();
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

//    private void createAllTables(String dbName) throws SQLException {
//        UserDao.createUserTable(connection);
//
//        // Create table for group
//        PreparedStatement group = connection.prepareStatement(
//                "CREATE TABLE " + GROUP_TB_NAME + " ("+
//                        COL_GROUP_ID + " varchar(12)," +
//                        COL_GROUP_NAME + " varchar(40) UNIQUE," +
//                        "CONSTRAINT " + PRIV_KEY_GROUP + " PRIMARY KEY (" + COL_GROUP_ID + "))"
//        );
//        group.execute();
//
//        // Create table for group
//        PreparedStatement members = connection.prepareStatement(
//                "CREATE TABLE " + MEMBERS_TB_NAME + " ("+
//                        MEMBERS_ID + " varchar(12)," +
//                        COL_GROUP_ID + " varchar(12) NOT NULL UNIQUE," +
//                        USER_ID + " varchar(12) NOT NULL UNIQUE," +
//                        USERNAME + " varchar(25) NOT NULL UNIQUE," +
//                        "CONSTRAINT " + PRIV_KEY_MEMBERS + " PRIMARY KEY (" + MEMBERS_ID + ")," +
//                        "CONSTRAINT " + FK_TB_MEMBERS_GROUP_ID + " FOREIGN KEY (" + COL_GROUP_ID + ") REFERENCES " + GROUP_TB_NAME + "(" + COL_GROUP_ID + "))"
//        );
//        members.execute();
//
//        // create the groceries table
//        GroceryDao.createTable(connection);
//    }

    private static void printFile(File file) throws IOException {
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
}