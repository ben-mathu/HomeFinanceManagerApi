package com.benardmathu.hfms.view.dashboard;

import com.benardmathu.hfms.data.expense.ExpenseDto;
import com.benardmathu.hfms.data.expense.model.Expense;
import com.benardmathu.hfms.data.jar.MoneyJarsDto;
import com.benardmathu.hfms.data.jar.MoneyJarDto;
import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.benardmathu.hfms.data.grocery.GroceriesDto;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.*;
import static com.benardmathu.hfms.utils.Constants.*;

/**
 * @author bernard
 */
@WebServlet("/dashboard/jars-controller/*")
public class MoneyJarServletController extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);

        String requestParam;
        requestParam = "?" + USER_ID + "=" + req.getParameter(USER_ID);

        String token = req.getParameter(TOKEN);

        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(GET_MONEY_JAR + requestParam, token, "GET");

        String line;
        MoneyJarsDto jarsDto = null;
        String response = "";
        while((line = streamReader.readLine()) != null) {
            response = line;
            jarsDto = gson.fromJson(line, MoneyJarsDto.class);
        }

        assert jarsDto != null;
        Report report = jarsDto.getReport();
        resp.setStatus(report.getStatus());
        writer = resp.getWriter();
        writer.write(response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
        
        String uri = req.getRequestURI();

        String token = req.getParameter(TOKEN);
        String category = req.getParameter(CATEGORY);
        String name = req.getParameter(MONEY_EXPENSE_TYPE);

        MoneyJarDto jarDto = new MoneyJarDto();
        jarDto.setId(req.getParameter(MONEY_JAR_ID));

        MoneyJar jar = new MoneyJar();
        jar.setCategory(category);
        jar.setName(name);
        jar.setScheduledFor(req.getParameter(SCHEDULED_FOR));
        String amount = req.getParameter(TOTAL_AMOUNT);
        jar.setTotalAmount(Double.parseDouble(amount));
        jar.setScheduleType(req.getParameter(SCHEDULED_TYPE));

        jarDto.setJar(jar);

        User user = new User();
        user.setUsername(req.getParameter(USERNAME));
        user.setUserId(req.getParameter(USER_ID));

        GroceriesDto groceriesDto;
        Expense expense = null;
        if (category.equals(JarType.LIST_EXPENSE_TYPE)) {
            String groceries = req.getParameter(LIABILITIES);
            groceriesDto = gson.fromJson(groceries, GroceriesDto.class);
            jarDto.setGroceries(groceriesDto.getGroceries());
        } else {
            String expenses = req.getParameter(LIABILITIES);
            ExpenseDto expenseDto = gson.fromJson(expenses, ExpenseDto.class);
            jarDto.setExpense(expenseDto.getExpense());
        }

        jarDto.setUser(user);

        InitUrlConnection<MoneyJarDto> conn = new InitUrlConnection<>();
        BufferedReader streamReader;
        if (uri.endsWith("add-money-jar")) {
            if (JarType.LIST_EXPENSE_TYPE.equals(jarDto.getJar().getCategory()))
                jarDto.getGroceries().forEach(grocery -> grocery.setJarId(jarDto.getJar().getMoneyJarId()));
            if (JarType.SINGLE_EXPENSE_TYPE.equals(jarDto.getJar().getCategory()))
                jarDto.getExpense().setJarId(jarDto.getJar().getMoneyJarId());
            streamReader = conn.getReader(jarDto, ADD_MONEY_JAR, token, "PUT");
        } else {
            streamReader = conn.getReader(jarDto, UPDATE_MONEY_JAR, token, "PUT");
        }

        String line;
        MoneyJarDto response = null;
        while((line = streamReader.readLine()) != null) {
            response = gson.fromJson(line, MoneyJarDto.class);
        }

        if (response != null) {
            String respStr = gson.toJson(response);

            resp.setStatus(HttpServletResponse.SC_OK);
            writer = resp.getWriter();
            writer.write(respStr);
        } else {
            Report report = new Report();
            report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            report.setMessage("Something happened while saving the item.");

            String respStr = gson.toJson(report);

            resp.setStatus(report.getStatus());
            writer = resp.getWriter();
            writer.write(respStr);
        }
    }
}