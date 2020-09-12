package com.benardmathu.hfms.api.report;

import com.benardmathu.hfms.api.base.BaseServlet;
import com.benardmathu.hfms.config.ConfigureApp;
import com.benardmathu.hfms.data.household.HouseholdDao;
import com.benardmathu.hfms.data.income.IncomeDao;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.income.model.IncomeChangeDao;
import com.benardmathu.hfms.data.income.model.OnInComeChange;
import com.benardmathu.hfms.data.jar.MoneyJarsDao;
import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.benardmathu.hfms.data.report.ReportDto;
import com.benardmathu.hfms.data.report.ReportRequest;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRel;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.MoneyJarScheduleDao;
import static com.benardmathu.hfms.data.utils.URL.API;
import static com.benardmathu.hfms.data.utils.URL.REPORTS;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.Constants;
import com.benardmathu.hfms.utils.Log;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@WebServlet(name = "ReportApi", urlPatterns = {API + REPORTS})
public class ReportApi extends BaseServlet {
    private IncomeDao incomeDao;
    private IncomeChangeDao incomeChangeDao;
    private MoneyJarScheduleDao moneyJarScheduleDao;
    private HouseholdDao householdDao;
    private MoneyJarsDao moneyJarsDao;

    public ReportApi() {
        incomeDao = new IncomeDao();
        incomeChangeDao = new IncomeChangeDao();
        moneyJarScheduleDao = new MoneyJarScheduleDao();
        householdDao = new HouseholdDao();
        moneyJarsDao = new MoneyJarsDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reportRequestStr = BufferRequestReader.bufferRequest(req);
        
        ReportRequest reportRequest = gson.fromJson(reportRequestStr, ReportRequest.class);
        
        String householdId = householdDao.getHouseholdId(reportRequest.getUserId());
        
        Income income = incomeDao.get(reportRequest.getUserId());
        
        SimpleDateFormat sp = new SimpleDateFormat(Constants.DATE_FORMAT);
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
        
        if (yearFrom < yearTo) {
            reportDto.setYearRange(yearFrom + "/" + yearTo);
        } else if (yearFrom == yearTo) {
            reportDto.setYearRange("" + yearTo);
        }
        int monthsDiff = to.get(Calendar.MONTH) - from.get(Calendar.MONTH);
        
        List<OnInComeChange> incomeChangeList = incomeChangeDao.getAll(
                income.getIncomeId(),
                reportRequest.getFrom(),
                reportRequest.getTo()
        );
        
        double totalIncome = 0;
        for (int i = 0; i < incomeChangeList.size(); i++) {
            totalIncome += incomeChangeList.get(i).getAmount();
        }
        
        List<JarScheduleDateRel> paid = moneyJarScheduleDao.getAllPaid(
                householdId,
                reportRequest.getFrom(),
                reportRequest.getTo()
        );
        
        List<MoneyJar> moneyJars = new ArrayList<>();
        double totalExpenseAmount = 0;
        for (JarScheduleDateRel item : paid) {
            MoneyJar jar = moneyJarsDao.get(item.getJarId());
            jar.setTotalAmount(item.getAmount());
            totalExpenseAmount += item.getAmount();
            moneyJars.add(jar);
        }
        
        reportDto.setIncome(totalIncome);
        reportDto.setMoneyJars(moneyJars);
        reportDto.setTotalExpenseAmount(totalExpenseAmount);
//        this is total income over period specified without tax
        double netIncome = totalIncome - totalExpenseAmount;
        reportDto.setNetIncome(netIncome);
        
        ConfigureApp app = new ConfigureApp();
        Properties properties = app.getProperties();
        
        float rate = 1;
        double incomeRange = 16666.67;
        double personalRelief = Double.parseDouble(properties.getProperty("gov.tax.relief"));
        if (totalIncome <= 24000) {
            rate = Float.parseFloat(properties.getProperty("gov.tax.first"));
        } else if (totalIncome > 24000 && totalIncome <= 40666.67) {
            rate = Float.parseFloat(properties.getProperty("gov.tax.second"));
        } else if (totalIncome > 40666.67 && totalIncome <= 57333.34) {
            rate = Float.parseFloat(properties.getProperty("gov.tax.third"));
        } else if (totalIncome > 57333.34) {
            rate = Float.parseFloat(properties.getProperty("gov.tax.forth"));
        }
        
        double tax = rate * totalIncome / 100;
        reportDto.setTax(tax);
        reportDto.setNetIncomeAfterTax(netIncome - tax + personalRelief);
        reportDto.setMonthsRange(monthsDiff);
        
        resp.setStatus(HttpServletResponse.SC_OK);
        writer = resp.getWriter();
        writer.write(gson.toJson(reportDto));
    }
}
