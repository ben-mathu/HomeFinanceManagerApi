package com.miiguar.hfms.data.user;

import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.user.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class UserDao implements Dao<User> {
    public static User getUserDetails(Connection connection, String username) throws SQLException {
        PreparedStatement getUser = connection.prepareStatement(
                "SELECT * FROM " + USERS_TB_NAME +
                        " WHERE " + USERNAME + "=?"
        );
        getUser.setString(1, username);
        ResultSet resultSet = getUser.executeQuery();

        User user = new User();
        while (resultSet.next()) {
            user.setUserId(resultSet.getString(USER_ID));
            user.setUsername(resultSet.getString(USERNAME));
            user.setEmail(resultSet.getString(EMAIL));
            user.setPassword("");
            user.setAdmin(resultSet.getBoolean(IS_ADMIN));
            user.setOnline(resultSet.getBoolean(IS_ONLINE));
        }

        return user;
    }

//    public static void createUserTable(Connection connection) throws SQLException {
//        PreparedStatement users = connection.prepareStatement(
//                "CREATE TABLE " + USERS_TB_NAME + " ("+
//                        USER_ID + " varchar(12),"+
//                        USERNAME + " varchar(25) NOT NULL UNIQUE,"+
//                        EMAIL + " text NOT NULL UNIQUE,"+
//                        PASSWORD + " varchar(255) NOT NULL,"+
//                        IS_ADMIN + " BOOLEAN NOT NULL," +
//                        IS_ONLINE + " BOOLEAN NOT NULL," +
//                        "CONSTRAINT " + PRIV_KEY_USERS + " PRIMARY KEY (" + USER_ID + "))"
//        );
//        users.execute();

//        // Create table for code
//        PreparedStatement code = connection.prepareStatement(
//                "CREATE TABLE " + CODE_TB_NAME + " ("+
//                        CODE + " text," +
//                        USER_ID + " varchar(12) UNIQUE," +
//                        EMAIL_CONFIRMED + " BOOLEAN DEFAULT false," +
//                        "CONSTRAINT " + PRIV_KEY_CODE + " PRIMARY KEY (" + CODE + ")," +
//                        "CONSTRAINT " + FK_TB_CODE_USER_ID + " FOREIGN KEY (" + USER_ID + ") REFERENCES " + USERS_TB_NAME + "(" + USER_ID + "))"
//        );
//        code.execute();
//    }

    public static int insert(User user, String userId, Connection connection) throws SQLException {
        PreparedStatement insertSmt = connection.prepareStatement(
                "INSERT INTO " + USERS_TB_NAME + " (" +
                        USER_ID + "," + USERNAME + "," + EMAIL + "," + PASSWORD + "," + IS_ADMIN + "," + IS_ONLINE + ") " +
                        "VALUES ('" +
                        userId + "','" +
                        user.getUsername() + "','" +
                        user.getEmail() + "','" +
                        user.getPassword() + "'," +
                        user.isAdmin() + "," +
                        user.isOnline() + ")"
        );

        return insertSmt.executeUpdate();
    }

    @Override
    public int save(User item, Connection connection) {
        return 0;
    }

    @Override
    public int update(User item, Connection connection) throws SQLException {

        PreparedStatement changeEmail = connection.prepareStatement(
                "UPDATE " + USERS_TB_NAME +
                        "SET " + EMAIL + "=?" +
                        " WHERE " + USER_ID + "=?"
        );
        changeEmail.setString(1, item.getEmail());
        changeEmail.setString(2, item.getUserId());
        return changeEmail.executeUpdate();
    }

    @Override
    public int delete(User item, Connection connection) {
        return 0;
    }

    @Override
    public User get(String id, Connection connection) {
        return null;
    }

    @Override
    public List<User> getAll(Connection connection) throws SQLException {
        PreparedStatement getUser = connection.prepareStatement(
                "SELECT * FROM " + USERS_TB_NAME
        );
        ResultSet resultSet = getUser.executeQuery();

        ArrayList<User> list = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setUserId(resultSet.getString(USER_ID));
            user.setUsername(resultSet.getString(USERNAME));
            user.setEmail(resultSet.getString(EMAIL));
            user.setPassword("");
            user.setAdmin(resultSet.getBoolean(IS_ADMIN));
            user.setOnline(resultSet.getBoolean(IS_ONLINE));
            list.add(user);
        }

        return list;
    }

    @Override
    public List<User> getAll(String id, Connection connection) throws SQLException {
        return null;
    }
}
