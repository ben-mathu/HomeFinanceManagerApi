package com.miiguar.hfms.view.dashboard;

import com.miiguar.hfms.data.expense.ExpenseDto;
import com.miiguar.hfms.data.expense.model.Expense;
import com.miiguar.hfms.data.jar.MoneyJarsDto;
import com.miiguar.hfms.data.jar.MoneyJarDto;
import com.miiguar.hfms.data.jar.model.MoneyJar;
import com.miiguar.hfms.data.grocery.GroceriesDto;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.view.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.*;
import static com.miiguar.hfms.utils.Constants.*;
import static com.miiguar.hfms.utils.Constants.JarType.EXPENSE_CATEGORY;
import static com.miiguar.hfms.utils.Constants.JarType.GROCERY_CATEGORY;

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

        String token = req.getParameter(TOKEN);
        String category = req.getParameter(CATEGORY);

        MoneyJarDto jarDto = new MoneyJarDto();

        MoneyJar jar = new MoneyJar();
        jar.setMoneyJarId(req.getParameter(MONEY_JAR_ID));
        jar.setCategory(category);
        jar.setName(req.getParameter(MONEY_JAR_NAME));
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
        if (category.equals(GROCERY_CATEGORY)) {
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
        if (jar.getMoneyJarId().isEmpty()) {
            if (EXPENSE_CATEGORY.equals(jarDto.getJar().getCategory()))
                jarDto.getExpense().setJarId(jarDto.getJar().getMoneyJarId());
            if (GROCERY_CATEGORY.equals(jarDto.getJar().getCategory()))
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