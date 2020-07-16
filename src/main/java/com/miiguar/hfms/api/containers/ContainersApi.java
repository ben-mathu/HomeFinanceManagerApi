package com.miiguar.hfms.api.containers;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.data.container.ContainersDao;
import com.miiguar.hfms.data.container.ContainerDto;
import com.miiguar.hfms.data.container.ContainersDto;
import com.miiguar.hfms.data.container.model.Container;
import com.miiguar.hfms.data.expense.ExpenseDao;
import com.miiguar.hfms.data.expense.model.Expense;
import com.miiguar.hfms.data.grocery.model.Grocery;
import com.miiguar.hfms.data.grocery.GroceryDao;
import com.miiguar.hfms.data.household.HouseholdDao;
import com.miiguar.hfms.data.status.AccountStatus;
import com.miiguar.hfms.data.status.AccountStatusDao;
import com.miiguar.hfms.data.status.Status;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdDao;
import com.miiguar.hfms.data.tablerelationships.UserHouseholdRel;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.URL.*;
import static com.miiguar.hfms.utils.Constants.*;
import static com.miiguar.hfms.utils.Constants.EnvelopeType.GROCERY_CATEGORY;

/**
 * @author bernard
 */
@WebServlet(API + ENVELOPES)
public class ContainersApi extends BaseServlet {
    private static final long serialVersionUID = 1L;

    AccountStatusDao accountStatusDao = new AccountStatusDao();
    HouseholdDao householdDao = new HouseholdDao();
    UserHouseholdDao userHouseholdDao = new UserHouseholdDao();
    GroceryDao groceryDao = new GroceryDao();
    ExpenseDao expenseDao = new ExpenseDao();
    ContainersDao envelopeDao = new ContainersDao();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);

        int envAffectedRows;
        int expAffectedRows = 0;
        int groAffectedRows = 0;

        GenerateRandomString randomString = new GenerateRandomString(
                12,
                new SecureRandom(),
                GenerateRandomString.getAlphaNumeric()
        );

        Date date = new Date();
        String now = new SimpleDateFormat(DATE_FORMAT).format(date);

        ContainerDto dto = gson.fromJson(requestStr, ContainerDto.class);
        Container envelope = dto.getEnvelope();

        String envelopeId = randomString.nextString();
        envelope.setEnvelopeId(envelopeId);
        envelope.setCreatedAt(now);
        envAffectedRows = envelopeDao.save(envelope);
        dto.setEnvelope(envelope);

        // get household
        User user = dto.getUser();
        UserHouseholdRel householdRel = userHouseholdDao.get(user.getUserId());

        List<Grocery> groceries = dto.getGroceries();
        Expense expense = dto.getExpense();
        if (GROCERY_CATEGORY.equals(dto.getEnvelope().getCategory())) {
            String groceryId = "";

            for (Grocery grocery : groceries) {
                groceryId = grocery.getGroceryId().isEmpty() ? randomString.nextString() : grocery.getGroceryId();
                grocery.setGroceryId(groceryId);
                grocery.setEnvelopeId(envelopeId);
                groAffectedRows += groceryDao.save(grocery);
            }
            dto.setGroceries(groceries);
        } else {

            String expenseId = expense.getExpenseId().isEmpty() ? randomString.nextString() : expense.getExpenseId();
            expense.setExpenseId(expenseId);
            expense.setEnvelopeId(envelopeId);
            expAffectedRows = expenseDao.save(expense);
            dto.setExpense(expense);
        }

        String uri = req.getRequestURI();
        if (uri.endsWith(ADD_ENVELOPE)) {
            if (envAffectedRows > 0 && (expAffectedRows > 0 || groAffectedRows > 0)) {
                String responseStr = gson.toJson(dto);

                writer = resp.getWriter();
                writer.write(responseStr);

                updateEnvStatus(user.getUserId());
            } else {
                Report report = new Report();
                report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                report.setMessage("The item was not added.");

                String responseStr = gson.toJson(report);
                writer = resp.getWriter();
                writer.write(responseStr);
            }
        }
    }

    private String getHouseId(String userId) {
        return householdDao.getHouseholdId(userId);
    }

    private void updateEnvStatus(String userId) {
        AccountStatus accountStatus = new AccountStatus();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String today = sf.format(date);

        Status status = new Status();
        status.status = COMPLETE;
        status.date = today;

        String statusStr = gson.toJson(status);
        accountStatus.setEnvelopeStatus(statusStr);
        accountStatus.setUserId(userId);

        if (accountStatusDao.updateEnvelopeStatus(accountStatus))
            Log.d(TAG, "Update table " + ACCOUNT_STATUS_TB_NAME);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(USER_ID);
        ContainersDto envelopesDto = new ContainersDto();
        ArrayList<ContainerDto> envelopeDtoList = new ArrayList<>();

        // get household
        UserHouseholdRel userHouseholdRel = userHouseholdDao.get(userId);
        String householdId = userHouseholdRel.getHouseId();

        // get envelopes
        List<Container> envelopes = envelopeDao.getAll(householdId);

        // loop through each envelope to get grocery or expense related
        for (Container envelope : envelopes) {
            ContainerDto envelopeDto = new ContainerDto();
            envelopeDto.setEnvelope(envelope);

            String envelopeId = envelope.getEnvelopeId();
            if (GROCERY_CATEGORY.equals(envelope.getCategory())) {
                List<Grocery> groceries = groceryDao.getAll(envelopeId);
                envelopeDto.setGroceries(groceries);
            } else {
                Expense expenses = expenseDao.get(envelopeId);
                envelopeDto.setExpense(expenses);
            }
            envelopeDtoList.add(envelopeDto);
        }

        envelopesDto.setEnvelopeDto(envelopeDtoList);

        String response = "";
        if (envelopesDto.getEnvelopeDto().isEmpty()) {
            Report report = new Report();
            report.setStatus(HttpServletResponse.SC_NOT_FOUND);
            report.setMessage("The items requested were not found");
            envelopesDto.setReport(report);

            response = gson.toJson(envelopesDto);

            writer = resp.getWriter();
            writer.write(response);
        } else {
            Report report = new Report();
            report.setStatus(HttpServletResponse.SC_OK);
            report.setMessage("Success");
            envelopesDto.setReport(report);

            response = gson.toJson(envelopesDto);
            writer = resp.getWriter();
            writer.write(response);
        }
    }
}