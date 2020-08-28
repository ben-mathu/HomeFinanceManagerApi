package com.miiguar.hfms.api.register;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureApp;
import com.miiguar.hfms.data.household.HouseholdDao;
import com.miiguar.hfms.data.household.model.Household;
import com.miiguar.hfms.data.status.AccountStatus;
import com.miiguar.hfms.data.status.AccountStatusDao;
import com.miiguar.hfms.data.status.Status;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdDao;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdRel;
import com.miiguar.hfms.data.user.UserDao;
import com.miiguar.hfms.data.user.UserRequest;
import com.miiguar.hfms.data.user.UserResponse;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequestReader;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;
import com.miiguar.tokengeneration.JwtTokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.miiguar.hfms.data.utils.DbEnvironment.ACCOUNT_STATUS_TB_NAME;
import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.REGISTRATION;
import static com.miiguar.hfms.utils.Constants.*;
import static com.miiguar.hfms.utils.Constants.COMPLETE;

/**
 * @author bernard
 */
@WebServlet(API + REGISTRATION)
public class Register extends BaseServlet {
    private static final long serialVersionUID = 1L;

    // Dao
    private UserDao userDao = new UserDao();
    private HouseholdDao householdDao = new HouseholdDao();
    private UserHouseholdDao userHouseDao = new UserHouseholdDao();
    private AccountStatusDao accountStatusDao = new AccountStatusDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String jsonRequest = BufferRequestReader.bufferRequest(req);

        UserRequest userRequest = gson.fromJson(jsonRequest, UserRequest.class);
        User user = userRequest.getUser();
        Household household = userRequest.getHousehold();

        boolean isJoinHouseHold = Boolean.parseBoolean(req.getParameter("joinHousehold"));

        Report report = null;

        if (isJoinHouseHold && !isHouseholdExists(household)) {
            UserResponse response = new UserResponse();
            report = new Report();
            report.setMessage("Household does not exist");
            report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);

            response.setReport(report);

            String jsonResp = gson.toJson(response);
            writer = resp.getWriter();
            writer.write(jsonResp);
            return;
        }

        if (isUserExists(user.getUsername())) {
            report = new Report();
            report.setMessage("username already in use");
            report.setStatus(HttpServletResponse.SC_CONFLICT);

            UserResponse response = new UserResponse();
            response.setReport(report);
            String jsonResp = gson.toJson(response);

            writer = resp.getWriter();
            writer.write(jsonResp);
        } else {

            GenerateRandomString randomString = new GenerateRandomString(
                    12,
                    new SecureRandom(), GenerateRandomString.getAlphaNumeric()
            );
            String userId = randomString.nextString();
            addUser(user, household, userId);
            user.setUserId(userId);

            AccountStatus accountStatus = new AccountStatus();
            accountStatus.setUserId(user.getUserId());
            int affectedRows = accountStatusDao.save(accountStatus);

            Log.d(TAG, "Affected Rows: " + affectedRows);

            UserHouseholdRel userHouseholdRel = new UserHouseholdRel();
            if (isJoinHouseHold) {
                userHouseholdRel.setHouseId(household.getId());
                userHouseholdRel.setUserId(user.getUserId());

                addUserToHousehold(userHouseholdRel);

                updateHouseholdStatus(user.getUserId());
            } else if (!household.getName().isEmpty()) {
                String houseId = randomString.nextString();
                household.setId(houseId);
                householdDao.save(household);

                userHouseholdRel.setUserId(user.getUserId());
                userHouseholdRel.setHouseId(houseId);
                userHouseholdRel.setOwner(true);

                addUserToHousehold(userHouseholdRel);

                updateHouseholdStatus(user.getUserId());
            }

            // generate a token
            JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
            Calendar calendar = Calendar.getInstance();
            Date date = new Date(calendar.getTimeInMillis() + TimeUnit.DAYS.toMillis(2));

            ConfigureApp configureApp = new ConfigureApp();
            Properties properties = configureApp.getProperties();
            String subject = properties.getProperty(SUBJECT, "");
            String token = jwtTokenUtil.generateToken(ISSUER, subject, date);

            report = new Report();
            report.setMessage("Success");
            report.setStatus(HttpServletResponse.SC_OK);
            report.setToken(token);

            UserResponse response = new UserResponse();
            response.setReport(report);
            response.setUser(user);
            response.setHousehold(household);
            String jsonResp = gson.toJson(response);

            writer = resp.getWriter();
            writer.write(jsonResp);
        }
    }

    private void updateHouseholdStatus(String userId) {
        AccountStatus accountStatus = new AccountStatus();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String today = sf.format(date);

        Status status = new Status();
        status.status = COMPLETE;
        status.date = today;

        String statusStr = gson.toJson(status);
        accountStatus.setHouseholdStatus(statusStr);
        accountStatus.setUserId(userId);

        if (accountStatusDao.updateHouseholdStatus(accountStatus))
            Log.d(TAG, "Update table " + ACCOUNT_STATUS_TB_NAME);
    }

    private void addUserToHousehold(UserHouseholdRel rel) {
        int affectedRows = userHouseDao.save(rel);
        Log.d(TAG, "Affected Rows: " + affectedRows);
    }

    private boolean isHouseholdExists(Household household) {
        Household house = householdDao.getHousehold(household.getId());
        if (house != null) {
            return true;
        }
        return false;
    }

    private boolean isUserExists(String username) {
        List<User> list = userDao.getAll();
        for (User user : list) {
            if (user.getUsername().equals(username))
                return true;
        }
        return false;
    }

    private void addUser(User user, Household household, String userId) {
        int rowsAffected = userDao.insert(user, userId);
        Log.d(TAG, "Rows Affected:" + rowsAffected);
    }

    private User getUser(String username, Connection connection) throws SQLException {
        return userDao.getUserDetails(username);
    }
}