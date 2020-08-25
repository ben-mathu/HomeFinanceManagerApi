package com.miiguar.hfms.api.moneyjar;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.jar.MoneyJarsDao;
import com.miiguar.hfms.data.jar.MoneyJarDto;
import com.miiguar.hfms.data.jar.MoneyJarsDto;
import com.miiguar.hfms.data.jar.model.MoneyJar;
import com.miiguar.hfms.data.expense.ExpenseDao;
import com.miiguar.hfms.data.expense.model.Expense;
import com.miiguar.hfms.data.grocery.model.Grocery;
import com.miiguar.hfms.data.grocery.GroceryDao;
import com.miiguar.hfms.data.household.HouseholdDao;
import com.miiguar.hfms.data.status.AccountStatus;
import com.miiguar.hfms.data.status.AccountStatusDao;
import com.miiguar.hfms.data.status.Status;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdDao;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdRel;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.BufferRequestReader;
import com.miiguar.hfms.utils.GenerateRandomString;
import com.miiguar.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.*;
import static com.miiguar.hfms.utils.Constants.*;
import static com.miiguar.hfms.utils.Constants.JarType.GROCERY_CATEGORY;

/**
 * @author bernard
 */
@WebServlet(API + MONEY_JARS)
public class MoneyJarApi extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private final AccountStatusDao accountStatusDao;
    private final HouseholdDao householdDao;
    private final UserHouseholdDao userHouseholdDao;
    private final GroceryDao groceryDao;
    private final ExpenseDao expenseDao;
    private final MoneyJarsDao jarDao;

    private final GenerateRandomString randomString;

    private final String now;

    public MoneyJarApi() {
        accountStatusDao = new AccountStatusDao();
        householdDao = new HouseholdDao();
        userHouseholdDao = new UserHouseholdDao();
        groceryDao = new GroceryDao();
        expenseDao = new ExpenseDao();
        jarDao = new MoneyJarsDao();

        randomString = new GenerateRandomString(
                12,
                new SecureRandom(),
                GenerateRandomString.getAlphaNumeric()
        );

        Date date = new Date();
        now = new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);

        MoneyJarDto dto = gson.fromJson(requestStr, MoneyJarDto.class);
        MoneyJar jar = dto.getJar();

        if (!jar.getMoneyJarId().isEmpty())
            updateDatabase(dto, req, resp);
        else
            save(dto, req, resp);
    }

    public void updateDatabase(MoneyJarDto dto, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MoneyJar jar = dto.getJar();

        int envAffectedRows;
        int expAffectedRows = 0;
        int groAffectedRows = 0;

        // get household
        User user = dto.getUser();
        UserHouseholdRel householdRel = userHouseholdDao.get(user.getUserId());

        jar.setHouseholdId(householdRel.getHouseId());
        envAffectedRows = jarDao.update(jar);
        dto.setJar(jar);


        List<Grocery> groceries = dto.getGroceries();
        if (GROCERY_CATEGORY.equals(dto.getJar().getCategory())) {

            for (Grocery grocery : groceries) {
                grocery.setJarId(jar.getMoneyJarId());
                groAffectedRows += groceryDao.save(grocery, grocery.getGroceryId());
            }
            dto.setGroceries(groceries);
        } else {
            Expense expense = dto.getExpense();
            expense.setJarId(jar.getMoneyJarId());
            expAffectedRows = expenseDao.update(expense);
            dto.setExpense(expense);
        }

        String uri = req.getRequestURI();
        if (uri.endsWith(ADD_MONEY_JAR)) {
            if (envAffectedRows > 0 && (expAffectedRows > 0 || groAffectedRows > 0)) {
                String responseStr = gson.toJson(dto);

                writer = resp.getWriter();
                writer.write(responseStr);

                updateEnvStatus(user.getUserId());
            } else {
                Report report = new Report();
                report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                report.setMessage("The item was not added.");

                String responseStr = gson.toJson(report);
                writer = resp.getWriter();
                writer.write(responseStr);
            }
        }
    }

    public void save(MoneyJarDto dto, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MoneyJar jar = dto.getJar();

        int envAffectedRows;
        int expAffectedRows = 0;
        int groAffectedRows = 0;

        // get household
        User user = dto.getUser();
        UserHouseholdRel householdRel = userHouseholdDao.get(user.getUserId());

        String jarId = randomString.nextString();
        jar.setMoneyJarId(jarId);
        jar.setCreatedAt(now);
        jar.setHouseholdId(householdRel.getHouseId());
        envAffectedRows = jarDao.save(jar);
        dto.setJar(jar);

        List<Grocery> groceries = dto.getGroceries();
        if (GROCERY_CATEGORY.equals(dto.getJar().getCategory())) {
            String groceryId;

            for (Grocery grocery : groceries) {
                groceryId = grocery.getGroceryId().isEmpty() ? randomString.nextString() : grocery.getGroceryId();
                grocery.setGroceryId(groceryId);
                grocery.setJarId(jarId);
                groAffectedRows += groceryDao.save(grocery);
            }
            dto.setGroceries(groceries);
        } else {
            Expense expense = dto.getExpense();
            String expenseId = randomString.nextString();
            expense.setExpenseId(expenseId);
            expense.setJarId(jarId);
            expAffectedRows = expenseDao.save(expense);
            dto.setExpense(expense);
        }

        String uri = req.getRequestURI();
        if (uri.endsWith(ADD_MONEY_JAR)) {
            if (envAffectedRows > 0 && (expAffectedRows > 0 || groAffectedRows > 0)) {
                String responseStr = gson.toJson(dto);

                writer = resp.getWriter();
                writer.write(responseStr);

                updateEnvStatus(user.getUserId());
            } else {
                Report report = new Report();
                report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                report.setMessage("The item was not added.");

                String responseStr = gson.toJson(report);
                writer = resp.getWriter();
                writer.write(responseStr);
            }
        }
    }

    private String getHouseId(String userId) {
        return householdDao.getHouseholdId(userId);
    }

    private void updateEnvStatus(String userId) {
        AccountStatus accountStatus = new AccountStatus();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String today = sf.format(date);

        Status status = new Status();
        status.status = COMPLETE;
        status.date = today;

        String statusStr = gson.toJson(status);
        accountStatus.setJarStatus(statusStr);
        accountStatus.setUserId(userId);

        if (accountStatusDao.updateJarStatus(accountStatus))
            Log.d(TAG, "Update table " + ACCOUNT_STATUS_TB_NAME);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(USER_ID);
        MoneyJarsDto jarsDto = new MoneyJarsDto();
        ArrayList<MoneyJarDto> jarDtoList = new ArrayList<>();

        // get household
        Report report;
        UserHouseholdRel userHouseholdRel = userHouseholdDao.get(userId);
        if (userHouseholdRel != null) {
            String householdId = userHouseholdRel.getHouseId();
            
            // get jars
            List<MoneyJar> jars = jarDao.getAll(householdId);

            // loop through each jar to get grocery or expense related
            for (MoneyJar jar : jars) {
                MoneyJarDto jarDto = new MoneyJarDto();
                jarDto.setJar(jar);

                String jarId = jar.getMoneyJarId();
                if (GROCERY_CATEGORY.equals(jar.getCategory())) {
                    List<Grocery> groceries = groceryDao.getAll(jarId);
                    jarDto.setGroceries(groceries);
                } else {
                    Expense expenses = expenseDao.get(jarId);
                    jarDto.setExpense(expenses);
                }
                jarDtoList.add(jarDto);
            }

            jarsDto.setJarDto(jarDtoList);

            String response;
            report = new Report();
            if (jarsDto.getJarDto().isEmpty()) {
                report.setStatus(HttpServletResponse.SC_NOT_FOUND);
                report.setMessage("The items requested were not found");

            } else {
                report.setStatus(HttpServletResponse.SC_OK);
                report.setMessage("Success");

            }
            jarsDto.setReport(report);
            response = gson.toJson(jarsDto);
            writer = resp.getWriter();
            writer.write(response);
        } else {
            report = new Report();
            report.setStatus(HttpServletResponse.SC_NO_CONTENT);
            report.setMessage("Error retrieving jars");
            resp.setStatus(report.getStatus());
            
            writer = resp.getWriter();
            writer.write(gson.toJson(report)); 
        }
    }
}