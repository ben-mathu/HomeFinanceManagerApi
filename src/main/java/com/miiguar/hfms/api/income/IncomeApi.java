package com.miiguar.hfms.api.income;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.income.IncomeDao;
import com.miiguar.hfms.data.income.IncomeDto;
import com.miiguar.hfms.data.income.model.Income;
import com.miiguar.hfms.data.jdbc.JdbcConnection;
import com.miiguar.hfms.data.status.AccountStatus;
import com.miiguar.hfms.data.status.AccountStatusDao;
import com.miiguar.hfms.data.status.Status;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.INCOME_ENDPOINT;
import static com.miiguar.hfms.utils.Constants.COMPLETE;
import static com.miiguar.hfms.utils.Constants.DATE_FORMAT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
@WebServlet(API + INCOME_ENDPOINT)
public class IncomeApi extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private IncomeDao incomeDao = new IncomeDao();
    private AccountStatusDao accountStatusDao = new AccountStatusDao();
    private Connection connection;
    private Properties prop;
    private JdbcConnection jdbcConnection;

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        ConfigureDb db = new ConfigureDb();
        this.prop = db.getProperties();
        this.jdbcConnection = new JdbcConnection();


        String userId = httpServletRequest.getParameter(USERNAME);
        String incomeDesc = httpServletRequest.getParameter(ACCOUNT_TYPE);
        String amount = httpServletRequest.getParameter(AMOUNT);

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String today = sf.format(date);

        GenerateRandomString randomString = new GenerateRandomString(
                12, new SecureRandom()
        );

        String incomeId = randomString.nextString();

        Income income = new Income();
        income.setUserId(userId);
        income.setIncomeId(incomeId);
        income.setAccountType(incomeDesc);
        income.setCreatedAt(today);
        income.setAmount(Double.parseDouble(amount));

        try {
            connection = jdbcConnection.getConnection(prop.getProperty("db.main_db"));
            incomeDao.save(income, connection);

            IncomeDto incomeDto = new IncomeDto();
            incomeDto.setIncome(income);

            String response = gson.toJson(incomeDto);

            httpServletResponse.setContentType(APPLICATION_JSON);
            writer = httpServletResponse.getWriter();
            writer.write(response);

            updateIncomeStatus(userId);
        } catch (SQLException throwables) {
            Log.e(TAG, "Error adding income: ", throwables);
        }
    }

    private void updateIncomeStatus(String userId) throws SQLException {
        AccountStatus accountStatus = new AccountStatus();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String today = sf.format(date);

        Status status = new Status();
        status.status = COMPLETE;
        status.date = today;

        String statusStr = gson.toJson(status);
        accountStatus.setIncomeStatus(statusStr);
        accountStatus.setUserId(userId);

        if (accountStatusDao.updateIncomeStatus(accountStatus, connection)) {
            Log.d(TAG, "Update table " + ACCOUNT_STATUS_TB_NAME);
        }
    }

    @Override
    public void closeConnection() throws SQLException {
        if (!connection.isClosed())
            connection.close();
        connection = null;
        jdbcConnection.disconnect();
        jdbcConnection = null;
    }
}
