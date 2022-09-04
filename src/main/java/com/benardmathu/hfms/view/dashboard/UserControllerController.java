package com.benardmathu.hfms.view.dashboard;

import com.benardmathu.hfms.data.income.IncomeDto;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.ADD_USER_INCOME;
import static com.benardmathu.hfms.data.utils.URL.GET_USER_DETAILS;
import static com.benardmathu.hfms.utils.Constants.TOKEN;

/**
 * @author bernard
 */
@Controller("/dashboard/user-controller")
public class UserControllerController extends BaseController {
    private static final long serialVersionUID = 1L;

    @GetMapping
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(USER_ID);
        String token = req.getParameter(TOKEN);

        String requestParam = "?" + USER_ID + "=" + userId;

        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(GET_USER_DETAILS + requestParam, token, "GET");

        String line;
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

    @PostMapping
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(USER_ID);
        String incomeType = req.getParameter(INCOME_TYPE);
        String amount = req.getParameter(AMOUNT);
        String date = req.getParameter("date");

        String token = req.getParameter(TOKEN);

        String requestParam = "?" + USER_ID + "=" + userId;
        IncomeDto incomeDto = new IncomeDto();
        Income income = new Income();
        income.setAmount(Double.parseDouble(amount));
        income.setUserId(userId);
        income.setAccountType(incomeType);
        income.setSchedule(date);
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