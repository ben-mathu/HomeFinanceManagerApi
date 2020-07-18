package com.miiguar.hfms.api.income;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.income.IncomeDao;
import com.miiguar.hfms.data.income.IncomeDto;
import com.miiguar.hfms.data.income.model.Income;
import com.miiguar.hfms.data.status.AccountStatus;
import com.miiguar.hfms.data.status.AccountStatusDao;
import com.miiguar.hfms.data.status.Status;
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
import java.util.Date;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.API;
import static com.miiguar.hfms.data.utils.URL.INCOME_ENDPOINT;
import static com.miiguar.hfms.utils.Constants.COMPLETE;
import static com.miiguar.hfms.utils.Constants.DATE_FORMAT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 */
@WebServlet(API + INCOME_ENDPOINT)
public class IncomeApi extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private IncomeDao incomeDao = new IncomeDao();
    private AccountStatusDao accountStatusDao = new AccountStatusDao();

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String userId = httpServletRequest.getParameter(USER_ID);
        String requestStr = BufferRequestReader.bufferRequest(httpServletRequest);

        IncomeDto incomeDto = gson.fromJson(requestStr, IncomeDto.class);
        Income income = incomeDto.getIncome();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String today = sf.format(date);

        GenerateRandomString randomString = new GenerateRandomString(
                12, new SecureRandom()
        );

        String incomeId = randomString.nextString();

        income.setIncomeId(incomeId);
        income.setCreatedAt(today);

        incomeDao.save(income);

        incomeDto.setIncome(income);

        String response = gson.toJson(incomeDto);

        httpServletResponse.setContentType(APPLICATION_JSON);
        writer = httpServletResponse.getWriter();
        writer.write(response);

        updateIncomeStatus(userId);
    }

    private void updateIncomeStatus(String userId) {
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

        if (accountStatusDao.updateIncomeStatus(accountStatus)) {
            Log.d(TAG, "Update table " + ACCOUNT_STATUS_TB_NAME);
        }
    }
}
