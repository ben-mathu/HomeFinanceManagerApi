package com.miiguar.hfms.api.income;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.assets.AssetsDao;
import com.miiguar.hfms.data.assets.AssetDto;
import com.miiguar.hfms.data.assets.model.Assets;
import com.miiguar.hfms.data.status.AccountStatus;
import com.miiguar.hfms.data.status.AccountStatusDao;
import com.miiguar.hfms.data.status.Status;
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

    private AssetsDao incomeDao = new AssetsDao();
    private AccountStatusDao accountStatusDao = new AccountStatusDao();

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String userId = httpServletRequest.getParameter(USERNAME);
        String incomeDesc = httpServletRequest.getParameter(ACCOUNT_TYPE);
        String amount = httpServletRequest.getParameter(AMOUNT);

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String today = sf.format(date);

        GenerateRandomString randomString = new GenerateRandomString(
                12, new SecureRandom()
        );

        String incomeId = randomString.nextString();

        Assets income = new Assets();
        income.setUserId(userId);
        income.setIncomeId(incomeId);
        income.setAccountType(incomeDesc);
        income.setCreatedAt(today);
        income.setAmount(Double.parseDouble(amount));

        incomeDao.save(income);

        AssetDto incomeDto = new AssetDto();
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
