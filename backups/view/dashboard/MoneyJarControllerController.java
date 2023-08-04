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
import com.benardmathu.hfms.view.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.*;

/**
 * @author bernard
 */
@Controller("/dashboard/jars-controller/*")
public class MoneyJarControllerController extends BaseController {
    private static final long serialVersionUID = 1L;

    @GetMapping
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestParam;
        requestParam = "?" + DbEnvironment.USER_ID + "=" + req.getParameter(DbEnvironment.USER_ID);

        String token = req.getParameter(TOKEN);

        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(URL.GET_MONEY_JAR + requestParam, token, "GET");

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

    @PutMapping
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        String token = req.getParameter(TOKEN);
        String category = req.getParameter(DbEnvironment.CATEGORY);
        String name = req.getParameter(DbEnvironment.MONEY_EXPENSE_TYPE);

        MoneyJarDto jarDto = new MoneyJarDto();
        jarDto.setId(Long.parseLong(req.getParameter(DbEnvironment.MONEY_JAR_ID)));

        MoneyJar jar = new MoneyJar();
        jar.setCategory(category);
        jar.setName(name);
        jar.setScheduledFor(req.getParameter(DbEnvironment.SCHEDULED_FOR));
        String amount = req.getParameter(DbEnvironment.TOTAL_AMOUNT);
        jar.setTotalAmount(Double.parseDouble(amount));
        jar.setScheduleType(req.getParameter(DbEnvironment.SCHEDULED_TYPE));

        jarDto.setJar(jar);

        User user = new User();
        user.setUsername(req.getParameter(DbEnvironment.USERNAME));
        user.setId(Long.parseLong(req.getParameter(DbEnvironment.USER_ID)));

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
                jarDto.getGroceries().forEach(grocery -> grocery.setId(jarDto.getJar().getMoneyJarId()));
            if (JarType.SINGLE_EXPENSE_TYPE.equals(jarDto.getJar().getCategory()))
                jarDto.getExpense().getJar().setMoneyJarId(jarDto.getJar().getMoneyJarId());
            streamReader = conn.getReader(jarDto, URL.ADD_MONEY_JAR, token, "PUT");
        } else {
            streamReader = conn.getReader(jarDto, URL.UPDATE_MONEY_JAR, token, "PUT");
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