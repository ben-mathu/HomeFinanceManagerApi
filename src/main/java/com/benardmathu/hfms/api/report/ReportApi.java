package com.benardmathu.hfms.api.report;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.household.HouseholdService;
import com.benardmathu.hfms.data.household.HouseholdRepository;
import com.benardmathu.hfms.data.income.IncomeChangeRepository;
import com.benardmathu.hfms.data.income.IncomeBaseService;
import com.benardmathu.hfms.data.income.IncomeRepository;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.income.model.IncomeChangeService;
import com.benardmathu.hfms.data.income.model.OnInComeChange;
import com.benardmathu.hfms.data.jar.MoneyJarRepository;
import com.benardmathu.hfms.data.jar.MoneyJarsBaseService;
import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.benardmathu.hfms.data.report.ReportDto;
import com.benardmathu.hfms.data.report.ReportRequest;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRel;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRepository;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.MoneyJarScheduleDao;
import com.benardmathu.hfms.data.transactions.TransactionBaseService;
import com.benardmathu.hfms.data.transactions.TransactionRepository;

import static com.benardmathu.hfms.data.utils.URL.REPORTS;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.Constants;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@RestController
@RequestMapping(name = "ReportApi", value = REPORTS)
public class ReportApi extends BaseController {
    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private IncomeChangeRepository incomeChangeRepository;

    @Autowired
    private JarScheduleDateRepository jarScheduleDateRepository;

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private MoneyJarRepository moneyJarRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private IncomeBaseService incomeDao;
    private IncomeChangeService incomeChangeService;
    private MoneyJarScheduleDao moneyJarScheduleDao;
    private HouseholdService householdDao;
    private MoneyJarsBaseService moneyJarsDao;
    private TransactionBaseService transactionDao;

    public ReportApi() {
        incomeDao = new IncomeBaseService();
        incomeChangeService = new IncomeChangeService();
        moneyJarScheduleDao = new MoneyJarScheduleDao();
        householdDao = new HouseholdService();
        moneyJarsDao = new MoneyJarsBaseService();
    }

    @PostMapping
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reportRequestStr = BufferRequestReader.bufferRequest(req);
        
        ReportRequest reportRequest = gson.fromJson(reportRequestStr, ReportRequest.class);
        
        String householdId = householdDao.getHouseholdByUserId(reportRequest.getUserId());
        
        Income income = incomeDao.get(reportRequest.getUserId());
        
        SimpleDateFormat sp = new SimpleDateFormat(Constants.DATE_FORMAT);
        SimpleDateFormat spDateOnly = new SimpleDateFormat("yyyy-MM-dd");
        
        Calendar from = new GregorianCalendar();
        Calendar to = new GregorianCalendar();
        try {
            from.setTime(sp.parse(reportRequest.getFrom()));
            to.setTime(sp.parse(reportRequest.getTo()));
        } catch (ParseException ex) {
            Log.e(TAG, "Error parsing date from sting to Date type", ex);
        }
        
        int yearTo = to.get(Calendar.YEAR);
        int yearFrom = from.get(Calendar.YEAR);
        
        ReportDto reportDto = new ReportDto();
        reportDto.setDateEnding(spDateOnly.format(to.getTime()));
        
        if (yearFrom < yearTo) {
            reportDto.setYearRange(yearFrom + "/" + yearTo);
        } else if (yearFrom == yearTo) {
            reportDto.setYearRange("" + yearTo);
        }
        int monthsDiff = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        
//        String formatFrom = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(from.getTime());
//        String formatTo = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(to.getTime());
        
        List<OnInComeChange> incomeChangeList = incomeChangeService.getAll(
                income.getIncomeId().toString(),
                reportRequest.getFrom(),
                reportRequest.getTo()
        );
        
        double totalIncome = 0;
        for (int i = 0; i < incomeChangeList.size(); i++) {
            totalIncome += incomeChangeList.get(i).getAmount();
        }
        
//        List<Transaction> transactions = transactionDao.getAllByUserId(reportRequest.getUserId());
//        HashMap<String, Double> amountMap = new HashMap<>();
//        for (Transaction transaction : transactions) {
//            double previousAmount = amountMap.get(transaction.getTransactionDesc()) == null ? 0 : amountMap.get(transaction.getTransactionDesc());
//            amountMap.put(transaction.getTransactionDesc(), previousAmount + transaction.getAmount());
//        }
        
        List<JarScheduleDateRel> paid = moneyJarScheduleDao.getAllPaid(
                householdId,
                reportRequest.getFrom(),
                reportRequest.getTo()
        );
        
        List<MoneyJar> moneyJars = new ArrayList<>();
        
        HashMap<String, MoneyJar> map = new HashMap<>();
        double totalExpenseAmount = 0;
        for (JarScheduleDateRel item : paid) {
            MoneyJar jar = moneyJarsDao.get(item.getJarId());
            
            if (isExpenseTypeExists(jar.getName(), map)) {
                MoneyJar mappedJar = map.get(jar.getName());
                double amount = map.get(jar.getName()).getTotalAmount() + item.getAmount();
                mappedJar.setTotalAmount(amount);
                map.put(jar.getName(), mappedJar);
            } else {
                jar.setTotalAmount(item.getAmount());
                map.put(jar.getName(), jar);
            }
        }
        
        for (Map.Entry<String, MoneyJar> mapItemEntry : map.entrySet()) {
            MoneyJar value = mapItemEntry.getValue();
            
            totalExpenseAmount += value.getTotalAmount();
            moneyJars.add(value);
        }
        
        reportDto.setIncome(totalIncome);
        reportDto.setMoneyJars(moneyJars);
        reportDto.setTotalExpenseAmount(totalExpenseAmount);
        
//        ConfigureApp app = new ConfigureApp();
//        Properties properties = app.getProperties();
//        
//        float rate = 1;
//        double incomeRange = 16666.67;
//        double personalRelief = Double.parseDouble(properties.getProperty("gov.tax.relief"));
//        if (totalIncome <= 24000) {
//            rate = Float.parseFloat(properties.getProperty("gov.tax.first"));
//        } else if (totalIncome > 24000 && totalIncome <= 40666.67) {
//            rate = Float.parseFloat(properties.getProperty("gov.tax.second"));
//        } else if (totalIncome > 40666.67 && totalIncome <= 57333.34) {
//            rate = Float.parseFloat(properties.getProperty("gov.tax.third"));
//        } else if (totalIncome > 57333.34) {
//            rate = Float.parseFloat(properties.getProperty("gov.tax.forth"));
//        }
//        
//        double tax = rate * totalIncome / 100;
//        reportDto.setTax(tax);
//        reportDto.setPersonalRelief(personalRelief);
//        reportDto.setIncomeAfterTax(totalIncome - tax + personalRelief);
        
        double netIncome = totalIncome - totalExpenseAmount;
        reportDto.setNetIncome(netIncome);
        
        reportDto.setMonthsRange(monthsDiff);
        
        resp.setStatus(HttpServletResponse.SC_OK);
        writer = resp.getWriter();
        writer.write(gson.toJson(reportDto));
    }

    private boolean isExpenseTypeExists(String name, HashMap<String, MoneyJar> map) {
        if (map.containsKey(name)) {
            return true;
        } else {
            return false;
        }
    }
}
