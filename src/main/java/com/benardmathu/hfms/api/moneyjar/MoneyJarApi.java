package com.benardmathu.hfms.api.moneyjar;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.jar.MoneyJarsService;
import com.benardmathu.hfms.data.jar.MoneyJarDto;
import com.benardmathu.hfms.data.jar.MoneyJarsDto;
import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.benardmathu.hfms.data.expense.ExpenseService;
import com.benardmathu.hfms.data.expense.model.Expense;
import com.benardmathu.hfms.data.grocery.model.Grocery;
import com.benardmathu.hfms.data.grocery.GroceryService;
import com.benardmathu.hfms.data.household.HouseholdService;
import com.benardmathu.hfms.data.income.IncomeService;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.status.*;
import com.benardmathu.hfms.data.status.Status;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdService;
import com.benardmathu.hfms.data.tablerelationships.userhouse.UserHouseholdRel;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.tablerelationships.jargroceryrel.MoneyJarGroceriesService;
import com.benardmathu.hfms.data.tablerelationships.jargroceryrel.MoneyJarGroceriesRel;
import com.benardmathu.hfms.data.tablerelationships.jarexpenserel.MoneyJarExpenseService;
import com.benardmathu.hfms.data.tablerelationships.jarexpenserel.MoneyJarExpenseRel;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRel;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.MoneyJarScheduleServices;
import com.benardmathu.hfms.data.user.UserService;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.GenerateRandomString;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
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

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author bernard
 */
@RestController
@RequestMapping(MONEY_JARS)
public class MoneyJarApi extends BaseController {
    
    private SimpleDateFormat sp = new SimpleDateFormat(DATE_FORMAT);

    @Autowired
    private AccountStatusService accountStatusService;

    @Autowired
    private HouseholdService householdService;

    @Autowired
    private UserHouseholdService userHouseholdService;

    @Autowired
    private GroceryService groceryService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private MoneyJarsService moneyJarsService;

    @Autowired
    private UserService userService;

    private final MoneyJarGroceriesService moneyJarListDao;
    private final MoneyJarExpenseService moneyJarExpenseService;
    private final MoneyJarScheduleServices moneyJarScheduleServices;
    private final IncomeService incomeDao;

    private final GenerateRandomString randomString;

    private final String now;

    public MoneyJarApi() {
        moneyJarListDao = new MoneyJarGroceriesService();
        moneyJarExpenseService = new MoneyJarExpenseService();
        moneyJarScheduleServices = new MoneyJarScheduleServices();
        incomeDao = new IncomeService();

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
        moneyJarsService.delete(jar);

        Report report = new Report();
        report.setMessage("Expense item was successfully deleted.");
        report.setStatus(HttpServletResponse.SC_OK);

        resp.setStatus(report.getStatus());
        writer = resp.getWriter();
        writer.write(gson.toJson(report));
    }

    @PutMapping
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);

        MoneyJarDto dto = gson.fromJson(requestStr, MoneyJarDto.class);
        MoneyJar jar = dto.getJar();
        
        if (dto.getUser() == null) {
            Long houseId = dto.getJar().getHouseholdId();
            User user = householdService.getUserId(houseId).get(0);
            
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
        JarScheduleDateRel jarScheduleDateRel = moneyJarScheduleServices.getWithStatusFalse(jarDto.getId());
        
        jarScheduleDateRel.setJarStatus(true);
        
        jarScheduleDateRel = moneyJarScheduleServices.update(jarScheduleDateRel);
        
        if (jarScheduleDateRel != null) {
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
        jarScheduleDateRel.setJarId(jar.getMoneyJarId());
        jarScheduleDateRel.setScheduleDate(jar.getScheduledFor());
        jarScheduleDateRel.setAmount(jar.getTotalAmount());

        JarScheduleDateRel scheduleDateRel = moneyJarScheduleServices.save(jarScheduleDateRel);

        if (scheduleDateRel != null) {
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

        // get household
        User user = dto.getUser();
        UserHouseholdRel householdRel = userHouseholdService.get(user.getId());

//        jar.setHouseholdId(householdRel.getHouseId());
//        envAffectedRows = jarDao.update(jar);

        JarScheduleDateRel jarScheduleDateRel = moneyJarScheduleServices.get(dto.getId());
        jarScheduleDateRel.setAmount(jar.getTotalAmount());
        jar.setMoneyJarId(jarScheduleDateRel.getJarId());
        
        jarScheduleDateRel = moneyJarScheduleServices.update(jarScheduleDateRel);
        jar.setHouseholdId(householdRel.getHouseId());
        moneyJarsService.update(jar);
        
        if (jarScheduleDateRel != null) {
            jar.setJarStatus(true);
        }
        
        dto.setJar(jar);

        List<Grocery> groceries = dto.getGroceries();
        if (JarType.LIST_EXPENSE_TYPE.equals(dto.getJar().getCategory())) {

            for (Grocery grocery : groceries) {
                grocery.setJar(jar);
                MoneyJarGroceriesRel moneyJarListRel = new MoneyJarGroceriesRel();
                moneyJarListRel.setGroceryId(grocery.getGroceryId());
                moneyJarListRel.setJarId(jar.getMoneyJarId());
                
                String groceryId = "";
                if (grocery.getGroceryId().isEmpty()) {
                    groceryId = randomString.nextString();
                    grocery.setGroceryId(groceryId);
                    
                    moneyJarListRel.setGroceryId(groceryId);
                    groceryService.save(grocery);
                    
                    moneyJarListDao.save(moneyJarListRel);
                } else {
                    groceryService.save(grocery);
                }
            }
            dto.setGroceries(groceries);
        } else {
            Expense expense = dto.getExpense();
            expense.setJar(jar);

            dto.setExpense(expense);
            
            MoneyJarExpenseRel moneyJarExpenseRel = new MoneyJarExpenseRel();
            moneyJarExpenseRel.setExpenseId(expense.getExpenseId());
            moneyJarExpenseRel.setJarId(jar.getMoneyJarId());
            
            expenseService.update(expense);
        }

        String uri = req.getRequestURI();
        if (uri.endsWith(ADD_MONEY_JAR)) {
            if (jarScheduleDateRel != null) {
                String responseStr = gson.toJson(dto);

                writer = resp.getWriter();
                writer.write(responseStr);

                updateEnvStatus(user.getId().toString());
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

        boolean jarSaved = false;
        boolean expSaved = false;
        boolean groSaved = false;

        // get household
        User user = dto.getUser();
        UserHouseholdRel householdRel = userHouseholdService.get(user.getId());

        jar.setCreatedAt(now);
        jar.setHouseholdId(householdRel.getHouseId());
        jar = moneyJarsService.save(jar);
        if (jar != null) jarSaved = true;

        JarScheduleDateRel jarScheduleDateRel = new JarScheduleDateRel();
        jarScheduleDateRel.setId(Long.parseLong(randomString.nextString()));
        jarScheduleDateRel.setHouseholdId(householdRel.getHouseId());
        jarScheduleDateRel.setJarId(jar.getMoneyJarId());
        jarScheduleDateRel.setScheduleDate(jar.getScheduledFor());
        jarScheduleDateRel.setAmount(jar.getTotalAmount());
        
        moneyJarScheduleServices.save(jarScheduleDateRel);
        
        dto.setJar(jar);
        dto.setId(jarScheduleDateRel.getId());

        List<Grocery> groceries = dto.getGroceries();
        if (JarType.LIST_EXPENSE_TYPE.equals(dto.getJar().getCategory())) {

            for (Grocery grocery : groceries) {
                grocery.setJar(jar);
                Grocery grox = groceryService.save(grocery);
                if (grox != null) groSaved = true;

                // set and save the schedule
                MoneyJarGroceriesRel moneyJarListRel = new MoneyJarGroceriesRel();
                moneyJarListRel.setGroceryId(grox.getGroceryId());
                moneyJarListRel.setJarId(jar.getMoneyJarId());
                
                moneyJarListDao.save(moneyJarListRel);
            }
            dto.setGroceries(groceries);
        } else {
            Expense expense = dto.getExpense();
            expense.setJar(jar);
            expense = expenseService.save(expense);
            if (expense != null) expSaved = true;
            dto.setExpense(expense);
            
            MoneyJarExpenseRel moneyJarExpenseRel = new MoneyJarExpenseRel();
            moneyJarExpenseRel.setExpenseId(expense.getExpenseId());
            moneyJarExpenseRel.setJarId(jar.getMoneyJarId());
            
            moneyJarExpenseService.save(moneyJarExpenseRel);
        }

        String uri = req.getRequestURI();
        if (uri.endsWith(ADD_MONEY_JAR)) {
            if (jarSaved && (expSaved || groSaved)) {
                String responseStr = gson.toJson(dto);

                writer = resp.getWriter();
                writer.write(responseStr);

                updateEnvStatus(user.getId().toString());
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

        if (accountStatusService.updateJarStatus(accountStatus))
            Log.d(TAG, "Update table " + ACCOUNT_STATUS_TB_NAME);
    }

    @GetMapping
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(USER_ID);
        MoneyJarsDto jarsDto = new MoneyJarsDto();
        ArrayList<MoneyJarDto> jarDtoList = new ArrayList<>();

        // get household
        Report report;
        UserHouseholdRel userHouseholdRel = userHouseholdService.get(Long.parseLong(userId));
        Calendar now = new GregorianCalendar();
        now.setTime(new Date());
        
        Calendar scheduled = new GregorianCalendar();
        
        if (userHouseholdRel != null) {
            List<JarScheduleDateRel> jarScheduleDateRelList = moneyJarScheduleServices.getAll(userHouseholdRel.getHouseId());
            
            assert jarScheduleDateRelList != null;
            jarScheduleDateRelList.forEach(jarScheduleDateRel -> {
                
                try {
                    // get jars
                    MoneyJar jar = moneyJarsService.get(jarScheduleDateRel.getJarId());
                    
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
                            
                            moneyJarScheduleServices.update(jarScheduleDateRel);
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
                            
                            moneyJarScheduleServices.update(jarScheduleDateRel);
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
                            
                            moneyJarScheduleServices.update(jarScheduleDateRel);
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
                    jarDto.setId(jarScheduleDateRel.getId());
                    
                    Long jarId = jar.getMoneyJarId();
                    if (JarType.LIST_EXPENSE_TYPE.equals(jar.getCategory())) {
                        
                        // get all grocery ids from moneyjarlistrel table to get all groceries
                        List<String> ids = moneyJarListDao.getIdByJarId(jarId);
                        List<Grocery> groceries = new ArrayList<>();
                        ids.forEach((id) -> {
                            Grocery grocery = groceryService.get(Long.parseLong(id));
                            groceries.add(grocery);
                        });
                        
                        jarDto.setGroceries(groceries);
                    } else {

                        Long id = moneyJarExpenseService.getIdByJarId(jarId);
                        Expense expenses = expenseService.get(id);
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
            
            Income income = incomeDao.get(Long.parseLong(userId));
            
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