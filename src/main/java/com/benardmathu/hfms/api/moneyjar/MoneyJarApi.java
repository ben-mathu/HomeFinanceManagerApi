package com.benardmathu.hfms.api.moneyjar;

import com.benardmathu.hfms.api.base.BaseServlet;
import com.benardmathu.hfms.data.expense.ExpenseRepository;
import com.benardmathu.hfms.data.grocery.GroceryRepository;
import com.benardmathu.hfms.data.household.HouseholdRepository;
import com.benardmathu.hfms.data.income.IncomeRepository;
import com.benardmathu.hfms.data.jar.MoneyJarRepository;
import com.benardmathu.hfms.data.jar.MoneyJarsDao;
import com.benardmathu.hfms.data.jar.MoneyJarDto;
import com.benardmathu.hfms.data.jar.MoneyJarsDto;
import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.benardmathu.hfms.data.expense.ExpenseDao;
import com.benardmathu.hfms.data.expense.model.Expense;
import com.benardmathu.hfms.data.grocery.model.Grocery;
import com.benardmathu.hfms.data.grocery.GroceryDao;
import com.benardmathu.hfms.data.household.HouseholdDao;
import com.benardmathu.hfms.data.income.IncomeDao;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.status.*;
import com.benardmathu.hfms.data.status.Status;
import com.benardmathu.hfms.data.tablerelationships.jarexpenserel.MoneyJarExpenseRepository;
import com.benardmathu.hfms.data.tablerelationships.jargroceryrel.MoneyJarGroceriesRepository;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRepository;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdDao;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRepository;
import com.benardmathu.hfms.data.user.UserRepository;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.tablerelationships.jargroceryrel.MoneyJarGroceriesDao;
import com.benardmathu.hfms.data.tablerelationships.jargroceryrel.MoneyJarGroceriesRel;
import com.benardmathu.hfms.data.tablerelationships.jarexpenserel.MoneyJarExpenseDao;
import com.benardmathu.hfms.data.tablerelationships.jarexpenserel.MoneyJarExpenseRel;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRel;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.MoneyJarScheduleDao;
import com.benardmathu.hfms.data.user.UserDao;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.GenerateRandomString;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.*;
import static com.benardmathu.hfms.utils.Constants.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bernard
 */
@RestController
@RequestMapping(MONEY_JARS)
public class MoneyJarApi extends BaseServlet {
    private static final long serialVersionUID = 1L;
    
    private SimpleDateFormat sp = new SimpleDateFormat(DATE_FORMAT);

    @Autowired
    private AccountStatusRepository accountStatusRepository;
    @Autowired
    private HouseholdRepository householdRepository;
    @Autowired
    private UserHouseholdRepository userHouseholdRepository;
    @Autowired
    private GroceryRepository groceryRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private MoneyJarRepository moneyJarRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MoneyJarGroceriesRepository moneyJarGroceriesRepository;
    @Autowired
    private MoneyJarExpenseRepository moneyJarExpenseRepository;
    @Autowired
    private JarScheduleDateRepository jarScheduleDateRepository;
    @Autowired
    private IncomeRepository incomeRepository;

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
    private final IncomeDao incomeDao;

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
        incomeDao = new IncomeDao();

        randomString = new GenerateRandomString(
                12,
                new SecureRandom(),
                GenerateRandomString.getAlphaNumeric()
        );

        Date date = new Date();
        now = new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    @DeleteMapping
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jarId = req.getParameter(MONEY_JAR_ID);
        
        MoneyJar jar = new MoneyJar();
        jar.setMoneyJarId(Long.parseLong(jarId));
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

    @PutMapping
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
        jarScheduleDateRel.setId(Long.parseLong(randomString.nextString()));
        jarScheduleDateRel.setHouseholdId(jar.getHouseholdId());
        jarScheduleDateRel.setJarId(jar.getMoneyJarId().toString());
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
        UserHouseholdRel householdRel = userHouseholdDao.get(user.getUserId().toString());

//        jar.setHouseholdId(householdRel.getHouseId());
//        envAffectedRows = jarDao.update(jar);

        JarScheduleDateRel jarScheduleDateRel = moneyJarScheduleDao.get(dto.getId());
        jarScheduleDateRel.setAmount(jar.getTotalAmount());
        jar.setMoneyJarId(Long.parseLong(jarScheduleDateRel.getJarId()));
        
        envAffectedRows = moneyJarScheduleDao.update(jarScheduleDateRel);
        jar.setHouseholdId(householdRel.getHouseId());
        jarDao.update(jar);
        
        if (envAffectedRows > 0) {
            jar.setJarStatus(true);
        }
        
        dto.setJar(jar);


        List<Grocery> groceries = dto.getGroceries();
        if (JarType.LIST_EXPENSE_TYPE.equals(dto.getJar().getCategory())) {

            for (Grocery grocery : groceries) {
                grocery.setJarId(jar.getMoneyJarId().toString());
//                groAffectedRows += groceryDao.save(grocery, grocery.getGroceryId());

                MoneyJarGroceriesRel moneyJarListRel = new MoneyJarGroceriesRel();
                moneyJarListRel.setGroceryId(grocery.getGroceryId());
                moneyJarListRel.setJarId(jar.getMoneyJarId().toString());
                
                String groceryId = "";
                if (grocery.getGroceryId().isEmpty()) {
                    groceryId = randomString.nextString();
                    grocery.setGroceryId(groceryId);
                    
                    moneyJarListRel.setGroceryId(groceryId);
                    groceryDao.save(grocery);
                    
                    moneyJarListDao.save(moneyJarListRel);
                } else {
                    groceryDao.save(grocery);
                }
            }
            dto.setGroceries(groceries);
        } else {
            Expense expense = dto.getExpense();
            expense.setJarId(jar.getMoneyJarId().toString());
//            expAffectedRows = expenseDao.update(expense);

            dto.setExpense(expense);
            
            MoneyJarExpenseRel moneyJarExpenseRel = new MoneyJarExpenseRel();
            moneyJarExpenseRel.setExpenseId(expense.getExpenseId().toString());
            moneyJarExpenseRel.setJarId(jar.getMoneyJarId().toString());
            
            expenseDao.update(expense);
        }

        String uri = req.getRequestURI();
        if (uri.endsWith(ADD_MONEY_JAR)) {
            if (envAffectedRows > 0 && (expAffectedRows > 0 || groAffectedRows > 0)) {
                String responseStr = gson.toJson(dto);

                writer = resp.getWriter();
                writer.write(responseStr);

                updateEnvStatus(user.getUserId().toString());
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
        UserHouseholdRel householdRel = userHouseholdDao.get(user.getUserId().toString());

        String jarId = randomString.nextString();
        jar.setMoneyJarId(Long.parseLong(jarId));
        jar.setCreatedAt(now);
        jar.setHouseholdId(householdRel.getHouseId());
        envAffectedRows = jarDao.save(jar);
        
        JarScheduleDateRel jarScheduleDateRel = new JarScheduleDateRel();
        jarScheduleDateRel.setId(Long.parseLong(randomString.nextString()));
        jarScheduleDateRel.setHouseholdId(householdRel.getHouseId());
        jarScheduleDateRel.setJarId(jarId);
        jarScheduleDateRel.setScheduleDate(jar.getScheduledFor());
        jarScheduleDateRel.setAmount(jar.getTotalAmount());
        
        moneyJarScheduleDao.save(jarScheduleDateRel);
        
        dto.setJar(jar);
        dto.setId(jarScheduleDateRel.getId().toString());

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
            expense.setExpenseId(Long.parseLong(expenseId));
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

                updateEnvStatus(user.getUserId().toString());
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

    @GetMapping
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(USER_ID);
        MoneyJarsDto jarsDto = new MoneyJarsDto();
        ArrayList<MoneyJarDto> jarDtoList = new ArrayList<>();

        // get household
        Report report;
        UserHouseholdRel userHouseholdRel = userHouseholdDao.get(userId);
        Calendar now = new GregorianCalendar();
        now.setTime(new Date());
        
        Calendar scheduled = new GregorianCalendar();
        
        if (userHouseholdRel != null) {
            List<JarScheduleDateRel> jarScheduleDateRelList = moneyJarScheduleDao.getAll(userHouseholdRel.getHouseId());
            
            assert jarScheduleDateRelList != null;
            jarScheduleDateRelList.forEach(jarScheduleDateRel -> {
                
                try {
                    // get jars
                    MoneyJar jar = jarDao.get(jarScheduleDateRel.getJarId());
                    
                    MoneyJarDto jarDto = new MoneyJarDto();
                    if (jar.getScheduleType().equals(ScheduleType.DAILY)) {
                        scheduled.setTime(sp.parse(jarScheduleDateRel.getScheduleDate()));
                        
                        int daysDiff = now.get(Calendar.DAY_OF_YEAR) - scheduled.get(Calendar.DAY_OF_YEAR);
                        jarDto.setDiff(daysDiff);
                        if (daysDiff > 1) {
                            scheduled.add(Calendar.DAY_OF_YEAR, daysDiff-1);
                            jar.setScheduledFor(sp.format(scheduled.getTime()));
                            
                            jarScheduleDateRel.setJarStatus(true);
                            jarScheduleDateRel.setScheduleDate(sp.format(scheduled.getTime()));
                            
                            moneyJarScheduleDao.update(jarScheduleDateRel);
                        } else {
                            jar.setScheduledFor(jarScheduleDateRel.getScheduleDate());
                        }
                    } else if (jar.getScheduleType().equals(ScheduleType.MONTHLY)) {
                        scheduled.setTime(sp.parse(jarScheduleDateRel.getScheduleDate()));
                        
                        int monthsDiff = now.get(Calendar.MONTH) - scheduled.get(Calendar.MONTH);
                        jarDto.setDiff(monthsDiff);
                        if (monthsDiff > 1) {
                            scheduled.add(Calendar.MONTH, monthsDiff-1);
                            jar.setScheduledFor(sp.format(scheduled.getTime()));
                            
                            jarScheduleDateRel.setJarStatus(true);
                            jarScheduleDateRel.setScheduleDate(sp.format(scheduled.getTime()));
                            
                            moneyJarScheduleDao.update(jarScheduleDateRel);
                        } else {
                            jar.setScheduledFor(jarScheduleDateRel.getScheduleDate());
                        }
                    } else if (jar.getScheduleType().equals(ScheduleType.WEEKLY)) {
                        scheduled.setTime(sp.parse(jarScheduleDateRel.getScheduleDate()));
                        
                        int weeksDiff = now.get(Calendar.WEEK_OF_YEAR) - scheduled.get(Calendar.WEEK_OF_YEAR);
                        jarDto.setDiff(weeksDiff);
                        if (weeksDiff > 1) {
                            scheduled.add(Calendar.WEEK_OF_YEAR, weeksDiff-1);
                            jar.setScheduledFor(sp.format(scheduled.getTime()));
                            
                            jarScheduleDateRel.setJarStatus(true);
                            jarScheduleDateRel.setScheduleDate(sp.format(scheduled.getTime()));
                            
                            moneyJarScheduleDao.update(jarScheduleDateRel);
                        } else {
                            jar.setScheduledFor(jarScheduleDateRel.getScheduleDate());
                        }
                    } else if (jar.getScheduleType().equals(ScheduleType.SCHEDULED)) {
                        jar.setScheduledFor(jarScheduleDateRel.getScheduleDate());
                    }
                    
                    jar.setJarStatus(jarScheduleDateRel.isJarStatus());
                    jar.setPaymentStatus(jarScheduleDateRel.isPaymentStatus());
                    jar.setTotalAmount(jarScheduleDateRel.getAmount());
                    
                    jarDto.setJar(jar);
                    jarDto.setId(jarScheduleDateRel.getId().toString());
                    
                    String jarId = jar.getMoneyJarId().toString();
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
                } catch (ParseException ex) {
                    Log.e(TAG, "Error: ", ex);
                }
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
            
            Income income = incomeDao.get(userId);
            
            jarsDto.setReport(report);
            jarsDto.setIncome(income);
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