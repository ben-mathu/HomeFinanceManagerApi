package com.miiguar.hfms.data.code;

import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.code.model.Code;
import com.miiguar.hfms.data.user.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class CodeDao implements Dao<Code> {
    public int saveCode(Code item, String userId, Connection connection) throws SQLException {
        PreparedStatement insertCode = connection.prepareStatement(
                "INSERT INTO " + CODE_TB_NAME + "(" +
                        CODE + "," + USER_ID + ")" +
                        "VALUES (?,?)" +
                        " ON CONFLICT (" + USER_ID + ")" +
                        " DO UPDATE" +
                        " SET " + CODE + "=?"+
                        " WHERE " + CODE_TB_NAME + "." + USER_ID + "=?"
        );
        insertCode.setString(1, item.getCode());
        insertCode.setString(2, userId);
        insertCode.setString(3, item.getCode());
        insertCode.setString(4, userId);
        return insertCode.executeUpdate();
    }
    @Override
    public int save(Code item, Connection connection) throws SQLException {
        return 0;
    }

    @Override
    public int update(Code item, Connection connection) throws SQLException {
        return 0;
    }

    @Override
    public int delete(Code item, Connection connection) {
        return 0;
    }

    @Override
    public Code get(String id, Connection connection) throws SQLException {

        PreparedStatement codeConfirm = connection.prepareStatement(
                "SELECT * FROM " + CODE_TB_NAME + " WHERE " + USER_ID + "=?"
        );
        codeConfirm.setString(1, id);

        ResultSet resultSet = codeConfirm.executeQuery();
        String code = "";
        Code item = new Code();
        while (resultSet.next()) {
            item.setCode(resultSet.getString(CODE));
            item.setEmailConfirmed(resultSet.getBoolean(EMAIL_CONFIRMED));
            item.setUserId(id);
        }

        return item;
    }

    @Override
    public List<Code> getAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public List<Code> getAll(String id, Connection connection) throws SQLException {
        return null;
    }

    public int emailConfirmed(String code, Connection connection) throws SQLException {
        PreparedStatement update = connection.prepareStatement(
                "UPDATE " + CODE_TB_NAME + " SET " + EMAIL_CONFIRMED + "=? " + "WHERE " + CODE + "=?"
        );
        update.setBoolean(1, true);
        update.setString(2, code);
        return update.executeUpdate();
    }
}
