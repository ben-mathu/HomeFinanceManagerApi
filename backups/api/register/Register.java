package com.benardmathu.hfms.api.register;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.config.ConfigureApp;
import com.benardmathu.hfms.data.household.HouseholdService;
import com.benardmathu.hfms.data.household.HouseholdRepository;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.status.*;
import com.benardmathu.hfms.data.status.Status;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdService;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRepository;
import com.benardmathu.hfms.data.user.UserService;
import com.benardmathu.hfms.data.user.UserRepository;
import com.benardmathu.hfms.data.user.UserRequest;
import com.benardmathu.hfms.data.user.UserResponse;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.GenerateRandomString;
import com.benardmathu.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.benardmathu.hfms.data.utils.DbEnvironment.ACCOUNT_STATUS_TB_NAME;
import static com.benardmathu.hfms.data.utils.URL.REGISTRATION;
import static com.benardmathu.hfms.utils.Constants.COMPLETE;
import com.benardmathu.hfms.utils.PasswordUtil;
import com.benardmathu.tokengeneration.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bernard
 */
@RestController
@RequestMapping(REGISTRATION)
public class Register extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private UserHouseholdRepository userHouseholdRepository;

    @Autowired
    private AccountStatusRepository accountStatusRepository;

    // Dao
    private UserService userService = new UserService();
    private HouseholdService householdService = new HouseholdService();
    private UserHouseholdService userHouseholdService = new UserHouseholdService();
    private AccountStatusService statusService = new AccountStatusService();

    @PostMapping
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
            
            // encrypt password
            GenerateRandomString generateSalt = new GenerateRandomString(30,
                    new SecureRandom(),
                    GenerateRandomString.getAlphaNumeric()
            );
            
            String salt = generateSalt.nextString();
            String securePassword = PasswordUtil.securePassword(user.getPassword(), salt);
            
            user.setPassword(securePassword);
            user.setSalt(salt);
            user.setId(Long.parseLong(userId));
            
            User userDto = userService.save(user);

            AccountStatus accountStatus = new AccountStatus();
            accountStatus.setUserId(user.getId().toString());
            AccountStatus status = statusService.save(accountStatus);

            UserHouseholdRel userHouseholdRel = new UserHouseholdRel();
            if (isJoinHouseHold) {
                userHouseholdRel.setHouseId(household.getId());
                userHouseholdRel.setUserId(user.getId());

                addUserToHousehold(userHouseholdRel);

                updateHouseholdStatus(user.getId().toString());
            } else if (!household.getName().isEmpty()) {
                String houseId = randomString.nextString();
                household.setId(Long.parseLong(houseId));
                householdService.save(household);

                userHouseholdRel.setUserId(user.getId());
                userHouseholdRel.setHouseId(Long.parseLong(houseId));
                userHouseholdRel.setOwner(true);

                addUserToHousehold(userHouseholdRel);

                updateHouseholdStatus(user.getId().toString());
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

        if (statusService.updateHouseholdStatus(accountStatus))
            Log.d(TAG, "Update table " + ACCOUNT_STATUS_TB_NAME);
    }

    private void addUserToHousehold(UserHouseholdRel rel) {
        UserHouseholdRel relDto = userHouseholdService.save(rel);
        Log.d(TAG, "Affected Rows: " + relDto.toString());
    }

    private boolean isHouseholdExists(Household household) {
        Household house = householdService.getHousehold(household.getId());
        if (house != null) {
            return true;
        }
        return false;
    }

    private boolean isUserExists(String username) {
        List<User> list = userService.getAll();
        for (User user : list) {
            if (user.getUsername().equals(username))
                return true;
        }
        return false;
    }
}