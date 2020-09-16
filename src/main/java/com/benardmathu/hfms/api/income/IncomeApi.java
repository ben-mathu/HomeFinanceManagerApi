package com.benardmathu.hfms.api.income;

import com.benardmathu.hfms.api.base.BaseServlet;
import com.benardmathu.hfms.data.income.IncomeDao;
import com.benardmathu.hfms.data.income.IncomeDto;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.income.model.IncomeChangeDao;
import com.benardmathu.hfms.data.income.model.OnInComeChange;
import com.benardmathu.hfms.data.status.AccountStatus;
import com.benardmathu.hfms.data.status.AccountStatusDao;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.status.Status;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.GenerateRandomString;
import com.benardmathu.hfms.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.API;
import static com.benardmathu.hfms.data.utils.URL.INCOME_ENDPOINT;
import static com.benardmathu.hfms.utils.Constants.COMPLETE;
import static com.benardmathu.hfms.utils.Constants.DATE_FORMAT;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
@WebServlet(API + INCOME_ENDPOINT)
public class IncomeApi extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private IncomeDao incomeDao;
    private IncomeChangeDao incomeChangeDao;
    private AccountStatusDao accountStatusDao;
    
    public IncomeApi() {
        incomeDao = new IncomeDao();
        incomeChangeDao = new IncomeChangeDao();
        accountStatusDao = new AccountStatusDao();
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String userId = httpServletRequest.getParameter(USER_ID);
        String requestStr = BufferRequestReader.bufferRequest(httpServletRequest);

        IncomeDto incomeDto = gson.fromJson(requestStr, IncomeDto.class);
        Income income = incomeDto.getIncome();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String today = sf.format(date);

        GenerateRandomString randomString = new GenerateRandomString(
                12, new SecureRandom()
        );

        String incomeId = randomString.nextString();

        income.setIncomeId(incomeId);
        income.setCreatedAt(today);
        income.setSchedule(income.getSchedule());
        
        OnInComeChange onInComeChange = new OnInComeChange();
        if (incomeDao.save(income) > 0) {
            onInComeChange.setAmount(income.getAmount());
            onInComeChange.setCreatedAt(today);
            onInComeChange.setIncomeId(incomeId);
            onInComeChange.setOnChangeStatus(true);

            incomeChangeDao.save(onInComeChange);
        }

        incomeDto.setIncome(income);
        incomeDto.setOnIncomeChange(onInComeChange);

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        Report report = updateIncomeStatus(userId);
        incomeDto.setReport(report);
        
        String response = gson.toJson(incomeDto);
        
        writer = httpServletResponse.getWriter();
        writer.write(response);
    }

    private Report updateIncomeStatus(String userId) {
        AccountStatus accountStatus = new AccountStatus();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String today = sf.format(date);

        Status status = new Status();
        status.status = COMPLETE;
        status.date = today;

        String statusStr = gson.toJson(status);
        accountStatus.setIncomeStatus(statusStr);
        accountStatus.setUserId(userId);

        Report report = new Report();
        if (accountStatusDao.updateIncomeStatus(accountStatus)) {
            Log.d(TAG, "Update table " + ACCOUNT_STATUS_TB_NAME);
            report.setStatus(HttpServletResponse.SC_OK);
            report.setMessage("Successfully saved income");
        } else {
            report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            report.setMessage("Please try again");
        }
        
        return report;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestStr = BufferRequestReader.bufferRequest(req);
        
        IncomeDto incomeDto = gson.fromJson(requestStr, IncomeDto.class);
        
        OnInComeChange onInComeChange = new OnInComeChange();
        onInComeChange.setAmount(incomeDto.getIncome().getAmount());
        onInComeChange.setCreatedAt(new SimpleDateFormat(DATE_FORMAT).format(new Date()));
        
        Income income = incomeDao.get(req.getParameter(USER_ID));
        incomeDto.getIncome().setAmount(income.getAmount() + incomeDto.getIncome().getAmount());
        
        onInComeChange.setIncomeId(income.getIncomeId());
        
        int affectedChange = incomeChangeDao.save(onInComeChange);
        if (affectedChange > 0) {
            incomeDto.setOnIncomeChange(onInComeChange);
        }
        
        int affected = incomeDao.update(incomeDto.getIncome());
        
        if (affected > 0) {
            Report report = new Report();
            report.setStatus(HttpServletResponse.SC_OK);
            report.setMessage("Successfully Updated");
            
            incomeDto.setReport(report);
            
            resp.setStatus(report.getStatus());
            writer = resp.getWriter();
            writer.write(gson.toJson(incomeDto));
        }
    }
}
