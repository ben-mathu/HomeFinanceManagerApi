package com.benardmathu.hfms.api.report;

import com.benardmathu.hfms.api.base.BaseServlet;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        paid.forEach(item -> {
            MoneyJar jar = moneyJarsDao.get(item.getJarId());
            jar.setTotalAmount(item.getAmount());
            
            moneyJars.add(jar);
        });
        
        ReportDto reportDto = new ReportDto();
        reportDto.setIncome(totalIncome);
        reportDto.setMoneyJars(moneyJars);
        
        resp.setStatus(HttpServletResponse.SC_OK);
        writer = resp.getWriter();
        writer.write(gson.toJson(reportDto));
    }
}
