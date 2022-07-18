package com.benardmathu.hfms.data.income;

import com.benardmathu.hfms.config.ConfigureDb;
import com.benardmathu.hfms.data.BaseService;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.jdbc.JdbcConnection;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * Member methods to create and update the income table
 * @author bernard
 */
public class IncomeBaseService implements BaseService<Income> {
    public static final String TAG = IncomeBaseService.class.getSimpleName();

    @Autowired
    private IncomeRepository incomeRepository;

    private JdbcConnection jdbcConnection;
    private ConfigureDb db;
    private Properties prop;

    public IncomeBaseService() {
        jdbcConnection = new JdbcConnection();
        db = new ConfigureDb();
        prop = db.getProperties();
    }

    @Override
    public Income save(Income item) {
        return incomeRepository.save(item);
    }

    @Override
    public int update(Income item) {
        String query = "UPDATE " + INCOME_TB_NAME + ""
                + " SET " + AMOUNT + "=?,"
                + INCOME_TYPE + "=?,"
                + USER_ID + "=?,"
                + SCHEDULED_FOR + "=?,"
                + CREATED_AT + "=?" +
                " WHERE " + INCOME_ID + "=?";
        int affectedRows = 0;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = jdbcConnection.getDataSource(prop.getProperty("db.main_db")).getConnection();
            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setDouble(1, item.getAmount());
            preparedStatement.setString(2, item.getAccountType());
            preparedStatement.setString(3, item.getUserId());
            preparedStatement.setString(4, item.getSchedule());
            preparedStatement.setString(5, item.getCreatedAt());
            preparedStatement.setString(6, item.getIncomeId());
            affectedRows = preparedStatement.executeUpdate();

            preparedStatement.close();
            preparedStatement = null;
            conn.close();
            conn = null;
        } catch (SQLException throwables) {
            Log.e(TAG, "Error adding income: ", throwables);
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
    public int delete(Income item) {
        return 0;
    }

    @Override
    public Income get(String id) {
        Optional<Income> optional =  incomeRepository.findByUserId(id);
        return optional.orElse(null);
    }

    @Override
    public List<Income> getAll() {
        return null;
    }

    @Override
    public List<Income> getAll(String id) {
        return null;
    }

    @Override
    public int saveAll(ArrayList<Income> items) {
        return 0;
    }
}
