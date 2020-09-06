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
import com.miiguar.hfms.data.tablerelationships.userhouse.UserHouseholdDao;
import com.miiguar.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.data.tablerelationships.jargroceryrel.MoneyJarGroceriesDao;
import com.miiguar.hfms.data.tablerelationships.jargroceryrel.MoneyJarGroceriesRel;
import com.miiguar.hfms.data.tablerelationships.jarexpenserel.MoneyJarExpenseDao;
import com.miiguar.hfms.data.tablerelationships.jarexpenserel.MoneyJarExpenseRel;
import com.miiguar.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRel;
import com.miiguar.hfms.data.tablerelationships.schedulejarrel.MoneyJarScheduleDao;
import com.miiguar.hfms.data.user.UserDao;
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
    private final UserDao userDao;
    private final MoneyJarGroceriesDao moneyJarListDao;
    private final MoneyJarExpenseDao moneyJarExpenseDao;
    private final MoneyJarScheduleDao moneyJarScheduleDao;

    private final GenerateRandomString randomString;

    private final String now;

    public MoneyJarApi() {
        accountStatusDao = new AccountStatusDao();
        householdDao = new HouseholdDao();
        userHouseholdDao = new UserHouseholdDao();
        groceryDao = new GroceryDao();
        expenseDao = new ExpenseDao();
        jarDao = new MoneyJarsDao();
        userDao = new UserDao();
        moneyJarListDao = new MoneyJarGroceriesDao();
        moneyJarExpenseDao = new MoneyJarExpenseDao();
        moneyJarScheduleDao = new MoneyJarScheduleDao();

        randomString = new GenerateRandomString(
                12,
                new SecureRandom(),
                GenerateRandomString.getAlphaNumeric()
        );

        Date date = new Date();
        now = new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jarId = req.getParameter(MONEY_JAR_ID);
        
        MoneyJar jar = new MoneyJar();
        jar.setMoneyJarId(jarId);
        int affected = jarDao.delete(jar);
        
        if (affected > 0) {
            Report report = new Report();
            report.setMessage("Expense item was successfully deleted.");
            report.setStatus(HttpServletResponse.SC_OK);
            
            resp.setStatus(report.getStatus());
            writer = resp.getWriter();
            writer.write(gson.toJson(report));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);

        MoneyJarDto dto = gson.fromJson(requestStr, MoneyJarDto.class);
        MoneyJar jar = dto.getJar();
        
        if (dto.getUser() == null) {
            String houseId = dto.getJar().getHouseholdId();
            String userId = "";
            try {
                userId = householdDao.getUserId(houseId).get(0);
            } catch (Exception e) {
                Log.e(TAG, "I have found you, you cannot hide forever.", e);
            }
            
            User user = userDao.get(userId);
            
            dto.setUser(user);
        }

        String uri = req.getRequestURI();
        if (uri.endsWith("add-jar"))
            saveJar(dto, req, resp);
        else if (uri.endsWith("update-jar"))
            updateJar(dto, req, resp);
        else if (uri.endsWith("add-money-jar"))
            save(dto, req, resp);
        else if (uri.endsWith("update-money-jar"))
            updateDatabase(dto, req, resp);
    }
    
    public void updateJar(MoneyJarDto jarDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        MoneyJar jar = jarDto.getJar();
        JarScheduleDateRel jarScheduleDateRel = moneyJarScheduleDao.getWithStatusFalse(jarDto.getId());
        
        jarScheduleDateRel.setJarStatus(true);
        
        int affected = moneyJarScheduleDao.update(jarScheduleDateRel);
        
        if (affected > 0) {
            Report report = new Report();
            report.setMessage("Successfully added!");
            report.setStatus(HttpServletResponse.SC_OK);

            response.setStatus(report.getStatus());
            writer = response.getWriter();
            writer.write(gson.toJson(report));
        } else {
            Report report = new Report();
            report.setMessage("The schedule was not updated.");
            report.setStatus(HttpServletResponse.SC_OK);

            response.setStatus(report.getStatus());
            writer = response.getWriter();
            writer.write(gson.toJson(report));
        }
    }
    
    public void saveJar(MoneyJarDto jarDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        MoneyJar jar = jarDto.getJar();
        
        JarScheduleDateRel jarScheduleDateRel = new JarScheduleDateRel();
        jarScheduleDateRel.setId(randomString.nextString());
        jarScheduleDateRel.setHouseholdId(jar.getHouseholdId());
        jarScheduleDateRel.setJarId(jar.getMoneyJarId());
        jarScheduleDateRel.setScheduleDate(jar.getScheduledFor());
        jarScheduleDateRel.setAmount(jar.getTotalAmount());
        
        int affected = moneyJarScheduleDao.save(jarScheduleDateRel);
        
        if (affected > 0) {
            Report report = new Report();
            report.setMessage("Successfully added!");
            report.setStatus(HttpServletResponse.SC_OK);

            response.setStatus(report.getStatus());
            writer = response.getWriter();
            writer.write(gson.toJson(report));
        } else {
            Report report = new Report();
            report.setMessage("Saving that schedule was not successful");
            report.setStatus(HttpServletResponse.SC_OK);

            response.setStatus(report.getStatus());
            writer = response.getWriter();
            writer.write(gson.toJson(report));
        }
    }

    /**
     * Update expenses when user edits it
     * @param dto stores expense details and data to be updated
     * @param req HTTP request from the client side.
     * @param resp HTTP response to be sent to the client once data has been processed
     * @throws IOException thrown whenever there is an error performing I/O operations.
     */
    public void updateDatabase(MoneyJarDto dto, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MoneyJar jar = dto.getJar();

        int envAffectedRows;
        int expAffectedRows = 0;
        int groAffectedRows = 0;

        // get household
        User user = dto.getUser();
        UserHouseholdRel householdRel = userHouseholdDao.get(user.getUserId());

//        jar.setHouseholdId(householdRel.getHouseId());
//        envAffectedRows = jarDao.update(jar);

        JarScheduleDateRel jarScheduleDateRel = moneyJarScheduleDao.get(dto.getId());
        jarScheduleDateRel.setAmount(jar.getTotalAmount());
        jar.setMoneyJarId(jarScheduleDateRel.getJarId());
        
        envAffectedRows = moneyJarScheduleDao.update(jarScheduleDateRel);
        jarDao.update(jar);
        
        if (envAffectedRows > 0) {
            jar.setJarStatus(true);
        }
        
        dto.setJar(jar);


        List<Grocery> groceries = dto.getGroceries();
        if (JarType.LIST_EXPENSE_TYPE.equals(dto.getJar().getCategory())) {

            for (Grocery grocery : groceries) {
                grocery.setJarId(jar.getMoneyJarId());
//                groAffectedRows += groceryDao.save(grocery, grocery.getGroceryId());

                MoneyJarGroceriesRel moneyJarListRel = new MoneyJarGroceriesRel();
                moneyJarListRel.setGroceryId(grocery.getGroceryId());
                moneyJarListRel.setJarId(jar.getMoneyJarId());
                
                groceryDao.save(grocery);
            }
            dto.setGroceries(groceries);
        } else {
            Expense expense = dto.getExpense();
            expense.setJarId(jar.getMoneyJarId());
//            expAffectedRows = expenseDao.update(expense);

            dto.setExpense(expense);
            
            MoneyJarExpenseRel moneyJarExpenseRel = new MoneyJarExpenseRel();
            moneyJarExpenseRel.setExpenseId(expense.getExpenseId());
            moneyJarExpenseRel.setJarId(jar.getMoneyJarId());
            
            expenseDao.update(expense);
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
        } else {
            String responseStr = gson.toJson(dto);

            writer = resp.getWriter();
            writer.write(responseStr);
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
        
        JarScheduleDateRel jarScheduleDateRel = new JarScheduleDateRel();
        jarScheduleDateRel.setId(randomString.nextString());
        jarScheduleDateRel.setHouseholdId(householdRel.getHouseId());
        jarScheduleDateRel.setJarId(jarId);
        jarScheduleDateRel.setScheduleDate(jar.getScheduledFor());
        jarScheduleDateRel.setAmount(jar.getTotalAmount());
        
        moneyJarScheduleDao.save(jarScheduleDateRel);
        
        dto.setJar(jar);
        dto.setId(jarScheduleDateRel.getId());

        List<Grocery> groceries = dto.getGroceries();
        if (JarType.LIST_EXPENSE_TYPE.equals(dto.getJar().getCategory())) {
            
            String groceryId;

            for (Grocery grocery : groceries) {
                groceryId = randomString.nextString();
                grocery.setGroceryId(groceryId);
                grocery.setJarId(jarId);
                groAffectedRows += groceryDao.save(grocery);
                
                
                // set and save the schedule
                MoneyJarGroceriesRel moneyJarListRel = new MoneyJarGroceriesRel();
                moneyJarListRel.setGroceryId(groceryId);
                moneyJarListRel.setJarId(jarId);
                
                moneyJarListDao.save(moneyJarListRel);
            }
            dto.setGroceries(groceries);
        } else {
            Expense expense = dto.getExpense();
            String expenseId = randomString.nextString();
            expense.setExpenseId(expenseId);
            expense.setJarId(jarId);
            expAffectedRows = expenseDao.save(expense);
            dto.setExpense(expense);
            
            MoneyJarExpenseRel moneyJarExpenseRel = new MoneyJarExpenseRel();
            moneyJarExpenseRel.setExpenseId(expenseId);
            moneyJarExpenseRel.setJarId(jarId);
            
            moneyJarExpenseDao.save(moneyJarExpenseRel);
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
        } else {
            String responseStr = gson.toJson(dto);

            writer = resp.getWriter();
            writer.write(responseStr);
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
            List<JarScheduleDateRel> jarScheduleDateRelList = moneyJarScheduleDao.getAll(userHouseholdRel.getHouseId());
            
            assert jarScheduleDateRelList != null;
            jarScheduleDateRelList.forEach(jarScheduleDateRel -> {
                
                // get jars
                MoneyJar jar = jarDao.get(jarScheduleDateRel.getJarId());
                jar.setScheduledFor(jarScheduleDateRel.getScheduleDate());
                jar.setJarStatus(jarScheduleDateRel.isJarStatus());
                jar.setPaymentStatus(jarScheduleDateRel.isPaymentStatus());
                jar.setTotalAmount(jarScheduleDateRel.getAmount());
                
                MoneyJarDto jarDto = new MoneyJarDto();
                jarDto.setJar(jar);
                jarDto.setId(jarScheduleDateRel.getId());

                String jarId = jar.getMoneyJarId();
                if (JarType.LIST_EXPENSE_TYPE.equals(jar.getCategory())) {

                    // get all grocery ids from moneyjarlistrel table to get all groceries
                    List<String> ids = moneyJarListDao.getIdByJarId(jarId);
                    List<Grocery> groceries = new ArrayList<>();
                    ids.forEach((id) -> {
                        Grocery grocery = groceryDao.get(id);
                        groceries.add(grocery);
                    });

                    jarDto.setGroceries(groceries);
                } else {
                    
                    String id = moneyJarExpenseDao.getIdByJarId(jarId);
                    Expense expenses = expenseDao.get(id);
                    jarDto.setExpense(expenses);
                }
                
                jarDtoList.add(jarDto);
            });

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