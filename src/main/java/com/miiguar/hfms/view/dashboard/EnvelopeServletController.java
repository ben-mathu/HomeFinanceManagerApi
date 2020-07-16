package com.miiguar.hfms.view.dashboard;

import com.miiguar.hfms.api.envelopes.EnvelopesDto;
import com.miiguar.hfms.data.envelope.EnvelopeDto;
import com.miiguar.hfms.data.envelope.model.Envelope;
import com.miiguar.hfms.data.expense.ExpenseDto;
import com.miiguar.hfms.data.expense.model.Expense;
import com.miiguar.hfms.data.grocery.GroceriesDto;
import com.miiguar.hfms.data.grocery.model.Grocery;
import com.miiguar.hfms.data.grocery.GroceryDto;
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
import static com.miiguar.hfms.data.utils.URL.ADD_ENVELOPE;
import static com.miiguar.hfms.data.utils.URL.GET_ENVELOPE;
import static com.miiguar.hfms.utils.Constants.*;
import static com.miiguar.hfms.utils.Constants.EnvelopeType.GROCERY_CATEGORY;

/**
 * @author bernard
 */
@WebServlet("/dashboard/envelopes-controller/*")
public class EnvelopeServletController extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);

        String requestParam = "";
        requestParam = "?" + USER_ID + "=" + req.getParameter(USER_ID);

        String token = req.getParameter(TOKEN);

        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(GET_ENVELOPE + requestParam, token, "GET");

        String line = "";
        EnvelopesDto envelopesDto = null;
        String response = "";
        while((line = streamReader.readLine()) != null) {
            response = line;
            envelopesDto = gson.fromJson(line, EnvelopesDto.class);
        }

        Report report = envelopesDto.getReport();
        resp.setStatus(report.getStatus());
        writer = resp.getWriter();
        writer.write(response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);

        String token = req.getParameter(TOKEN);
        String category = req.getParameter(CATEGORY);

        EnvelopeDto envelopeDto = new EnvelopeDto();

        Envelope envelope = new Envelope();
        envelope.setCategory(category);
        envelope.setName(req.getParameter(ENVELOPE_NAME));
        envelope.setScheduledFor(req.getParameter(SCHEDULED_FOR));
        envelope.setTotalAmount(Double.parseDouble(req.getParameter(TOTAL_AMOUNT)));
        envelope.setScheduleType(req.getParameter(SCHEDULED_TYPE));

        envelopeDto.setEnvelope(envelope);

        User user = new User();
        user.setUsername(req.getParameter(USERNAME));
        user.setUserId(req.getParameter(USER_ID));

        GroceriesDto groceriesDto = null;
        Expense expense = null;
        if (category.equals(GROCERY_CATEGORY)) {
            String groceries = req.getParameter(LIABILITIES);
            groceriesDto = gson.fromJson(groceries, GroceriesDto.class);
            envelopeDto.setGroceries(groceriesDto.getGroceries());
        } else {
            String expenses = req.getParameter(LIABILITIES);
            expense = gson.fromJson(expenses, Expense.class);
            envelopeDto.setExpense(expense);
        }

        envelopeDto.setUser(user);

        InitUrlConnection<EnvelopeDto> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(envelopeDto, ADD_ENVELOPE, token, "PUT");

        String line = "";
        EnvelopeDto response = null;
        while((line = streamReader.readLine()) != null) {
            response = gson.fromJson(line, EnvelopeDto.class);
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

            writer = resp.getWriter();
            writer.write(respStr);
        }
    }
}