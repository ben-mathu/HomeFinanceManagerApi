package com.benardmathu.hfms.data.user;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.Dao;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import com.benardmathu.hfms.utils.Log;

import javax.naming.PartialResultException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class UserDao implements Dao<User> {
    public static final String TAG = UserDao.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public UserDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }

    public User getUserDetails(String username) {
        String query = "SELECT * FROM " + USERS_TB_NAME +
                " WHERE " + USERNAME + "=?";
        User user = new User();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user.setUserId(resultSet.getString(USER_ID));
                user.setUsername(resultSet.getString(USERNAME));
                user.setEmail(resultSet.getString(EMAIL));
                user.setPassword("");
                user.setAdmin(resultSet.getBoolean(IS_ADMIN));
                user.setOnline(resultSet.getBoolean(IS_ONLINE));
                user.setSalt(resultSet.getString(SALT));
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing user query", throwables);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                    conn = null;
                } catch (Exception e) { /* Intentionally blank */ }

            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (Exception e) { /* Intentionally blank */ }

            if (resultSet != null)
                try {
                    resultSet.close();
                    resultSet = null;
                } catch (Exception e) { /* Intentionally blank */ }
        }
        return user;
    }

    @Override
    public int save(User user) {
        String query = "INSERT INTO " + USERS_TB_NAME + " (" +
                USER_ID + "," + USERNAME + "," + EMAIL + "," + PASSWORD + "," + IS_ADMIN + "," + IS_ONLINE + "," + SALT + ") " +
                "VALUES (?,?,?,?,?,?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setBoolean(5, user.isAdmin());
            preparedStatement.setBoolean(6, user.isOnline());
            preparedStatement.setString(7, user.getSalt());

            affectedRows = preparedStatement.executeUpdate();
            
            Log.d(TAG, "Rows Affected:" + affectedRows);

            conn.close();
            conn = null;
            preparedStatement.close();
            preparedStatement = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing user update", throwables);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }

            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
        }

        return affectedRows;
    }

    @Override
    public int update(User item) {
        String query = "UPDATE " + USERS_TB_NAME +
                "SET " + EMAIL + "=?" +
                " WHERE " + USER_ID + "=?";

        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, item.getEmail());
            preparedStatement.setString(2, item.getUserId());
            affectedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing user update", throwables);
        } finally {
            if (conn != null)
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }

            if (preparedStatement != null)
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
        }

        return affectedRows;
    }

    @Override
    public int delete(User item) {
        return 0;
    }

    @Override
    public User get(String id) {
        String query = "SELECT * FROM " + USERS_TB_NAME +
                " WHERE " + USER_ID + "=?";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        User user = new User();
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, id);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user.setUserId(resultSet.getString(USER_ID));
                user.setUsername(resultSet.getString(USERNAME));
                user.setEmail(resultSet.getString(EMAIL));
                user.setPassword("");
                user.setAdmin(resultSet.getBoolean(IS_ADMIN));
                user.setOnline(resultSet.getBoolean(IS_ONLINE));
                user.setSalt(resultSet.getString(SALT));
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing users query", throwables);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
            }

            if (resultSet != null) {
                try {
                    resultSet.close();
                    resultSet = null;
                } catch (SQLException throwables) { /* Intentionally blank. */}
            }
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM " + USERS_TB_NAME;
        ArrayList<User> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getString(USER_ID));
                user.setUsername(resultSet.getString(USERNAME));
                user.setEmail(resultSet.getString(EMAIL));
                user.setPassword("");
                user.setAdmin(resultSet.getBoolean(IS_ADMIN));
                user.setOnline(resultSet.getBoolean(IS_ONLINE));
                user.setSalt(resultSet.getString(SALT));
                list.add(user);
            }

            resultSet.close();
            resultSet = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing users query", throwables);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
            }

            if (resultSet != null) {
                try {
                    resultSet.close();
                    resultSet = null;
                } catch (SQLException throwables) { /* Intentionally blank. */}
            }
        }
        return list;
    }

    @Override
    public List<User> getAll(String id) {
        return null;
    }

    @Override
    public int saveAll(ArrayList<User> items) {
        return 0;
    }

    public User validateCredentials(String username, String password) {
        String query = "SELECT * FROM " + USERS_TB_NAME + " WHERE " + DbEnvironment.USERNAME + "=?";
        User user = null;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, username);
            result = preparedStatement.executeQuery();
            while (result.next()) {
                user = new User();
                user.setMobNum(result.getString(MOB_NUMBER));
                user.setUsername(result.getString(DbEnvironment.USERNAME));
                user.setPassword(result.getString(DbEnvironment.PASSWORD));
                user.setEmail(result.getString(DbEnvironment.EMAIL));
                user.setAdmin(result.getBoolean(IS_ADMIN));
                user.setOnline(result.getBoolean(IS_ONLINE));
                user.setUserId(result.getString(DbEnvironment.USER_ID));
                user.setSalt(result.getString(SALT));
            }

            result.close();
            result = null;
            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error validating user", throwables);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                    preparedStatement = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
            }

            if (result != null) {
                try {
                    result.close();
                    result = null;
                } catch (SQLException throwables) { /* Intentionally blank. */ }
            }
        }
        return user;
    }

    public int updateEmail(User user) {
        return 0;
    }
}
