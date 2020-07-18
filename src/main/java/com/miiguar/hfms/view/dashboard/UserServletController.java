package com.miiguar.hfms.view.dashboard;

import com.miiguar.hfms.data.income.IncomeDto;
import com.miiguar.hfms.data.income.model.Income;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.view.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.ADD_USER_INCOME;
import static com.miiguar.hfms.data.utils.URL.GET_USER_DETAILS;
import static com.miiguar.hfms.utils.Constants.TOKEN;

/**
 * @author bernard
 */
@WebServlet("/dashboard/user-controller/*")
public class UserServletController extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);

        String username = req.getParameter(USERNAME);
        String token = req.getParameter(TOKEN);

        String requestParam = "?" + USERNAME + "=" + username;

        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(GET_USER_DETAILS + requestParam, token, "GET");

        String line = "";
        String response = "";
        while((line = streamReader.readLine()) != null) {
            response = line;
        }

        if (!response.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            writer = resp.getWriter();
            writer.write(response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(USER_ID);
        String incomeDesc = req.getParameter(INCOME_DESC);
        String incomeType = req.getParameter(ACCOUNT_TYPE);
        String amount = req.getParameter(AMOUNT);

        String token = req.getParameter(TOKEN);

        String requestParam = "?" + USER_ID + "=" + userId;
        IncomeDto incomeDto = new IncomeDto();
        Income income = new Income();
        income.setAmount(Double.parseDouble(amount));
        income.setUserId(userId);
        income.setAccountType(incomeType);
        income.setIncomeDesc(incomeDesc);
        incomeDto.setIncome(income);

        InitUrlConnection<IncomeDto> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(incomeDto, ADD_USER_INCOME + requestParam, token, "POST");

        String line = "";
        String response = "";
        while((line = streamReader.readLine()) != null) {
            response = line;
        }

        if (!response.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            writer = resp.getWriter();
            writer.write(response);
        }
    }
}