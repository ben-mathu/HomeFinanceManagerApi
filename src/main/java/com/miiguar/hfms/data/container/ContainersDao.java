package com.miiguar.hfms.data.container;

import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.Dao;
import com.miiguar.hfms.data.container.model.Container;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.utils.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
public class ContainersDao implements Dao<Container> {
    public static final String TAG = ContainersDao.class.getSimpleName();

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public ContainersDao() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }
    @Override
    public int save(Container item) {
        String query = "INSERT INTO " + ENVELOPE_TB_NAME + "(" +
                ENVELOPE_ID + "," + ENVELOPE_NAME + "," + CATEGORY + "," +
                TOTAL_AMOUNT + "," + CREATED_AT + "," + SCHEDULED_FOR + "," +
                SCHEDULED_TYPE + ")" +
                " VALUES (?,?,?,?,?,?,?)";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, item.getEnvelopeId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setString(3, item.getCategory());
            preparedStatement.setDouble(4, item.getTotalAmount());
            preparedStatement.setString(5, item.getCreatedAt());
            preparedStatement.setString(6, item.getScheduledFor());
            preparedStatement.setString(7, item.getScheduleType());
            affectedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing envelope update", throwables);
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
        }
        return affectedRows;
    }

    @Override
    public int update(Container item) {
        return 0;
    }

    @Override
    public int delete(Container item) {
        return 0;
    }

    @Override
    public Container get(String id) {
        return null;
    }

    @Override
    public List<Container> getAll() {
        return null;
    }

    @Override
    public List<Container> getAll(String id) {
        String query = "SELECT * FROM " + ENVELOPE_TB_NAME +
                " WHERE " + HOUSEHOLD_ID + "=?";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        ArrayList<Container> envelopes = new ArrayList<>();
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Container envelope = new Container();
                envelope.setEnvelopeId(resultSet.getString(ENVELOPE_ID));
                envelope.setName(resultSet.getString(ENVELOPE_NAME));
                envelope.setTotalAmount(resultSet.getDouble(TOTAL_AMOUNT));
                envelope.setCategory(resultSet.getString(CATEGORY));
                envelope.setScheduledFor(resultSet.getString(SCHEDULED_FOR));
                envelope.setScheduleType(resultSet.getString(SCHEDULED_TYPE));
                envelope.setHouseholdId(resultSet.getString(HOUSEHOLD_ID));
                envelope.setCreatedAt(resultSet.getString(CREATED_AT));
                envelopes.add(envelope);
            }
        } catch (SQLException throwables) {
            Log.e(TAG, "Error processing envelope query", throwables);
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
        return envelopes;
    }

    @Override
    public int saveAll(ArrayList<Container> items) {
        return 0;
    }
}
