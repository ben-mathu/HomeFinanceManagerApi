package com.benardmathu.hfms.api.report;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.household.HouseholdService;
import com.benardmathu.hfms.data.household.HouseholdRepository;
import com.benardmathu.hfms.data.household.model.Household;
import com.benardmathu.hfms.data.income.IncomeChangeRepository;
import com.benardmathu.hfms.data.income.IncomeService;
import com.benardmathu.hfms.data.income.IncomeRepository;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.income.model.IncomeChangeService;
import com.benardmathu.hfms.data.income.model.OnInComeChange;
import com.benardmathu.hfms.data.jar.MoneyJarRepository;
import com.benardmathu.hfms.data.jar.MoneyJarsService;
import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.benardmathu.hfms.data.report.ReportDto;
import com.benardmathu.hfms.data.report.ReportRequest;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRel;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRepository;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.MoneyJarScheduleServices;
import com.benardmathu.hfms.data.transactions.TransactionService;
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

    private IncomeService incomeDao;
    private IncomeChangeService incomeChangeService;
    private MoneyJarScheduleServices moneyJarScheduleServices;
    private HouseholdService householdService;
    private MoneyJarsService moneyJarsService;
    private TransactionService transactionDao;

    public ReportApi() {
        incomeDao = new IncomeService();
        incomeChangeService = new IncomeChangeService();
        moneyJarScheduleServices = new MoneyJarScheduleServices();
        householdService = new HouseholdService();
        moneyJarsService = new MoneyJarsService();
    }

    @PostMapping
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, ParseException {

        String reportRequestStr = BufferRequestReader.bufferRequest(req);
        
        ReportRequest reportRequest = gson.fromJson(reportRequestStr, ReportRequest.class);
        
        Household household = householdService.getHouseholdByUserId(reportRequest.getUserId());
        
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
        
        SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        
        List<OnInComeChange> incomeChangeList = incomeChangeService.getAll(
                income.getIncomeId(),
                sp.parse(reportRequest.getFrom()),
                sp.parse(reportRequest.getTo())
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
        
        List<JarScheduleDateRel> paid = moneyJarScheduleServices.getAllPaid(
                household.getId(),
                reportRequest.getFrom(),
                reportRequest.getTo()
        );
        
        List<MoneyJar> moneyJars = new ArrayList<>();
        
        HashMap<String, MoneyJar> map = new HashMap<>();
        double totalExpenseAmount = 0;
        for (JarScheduleDateRel item : paid) {
            MoneyJar jar = moneyJarsService.get(item.getJarId());
            
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
