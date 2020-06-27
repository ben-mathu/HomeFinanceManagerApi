package com.miiguar.hfms.view.dashboard;

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
import static com.miiguar.hfms.data.utils.URL.ADD_GROCERY;
import static com.miiguar.hfms.data.utils.URL.GET_GROCERY;
import static com.miiguar.hfms.utils.Constants.*;

/**
 * @author bernard
 */
@WebServlet("/dashboard/groceries-controller")
public class GroceryServletController extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);

        String requestParam = "";
        requestParam = "?" + USERNAME + "=" + req.getParameter(USERNAME);

        String token = req.getParameter(TOKEN);

        InitUrlConnection<User> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(GET_GROCERY + requestParam, token, "GET");

        String line = "";
        GroceriesDto groceries = null;
        Report report = null;
        while((line = streamReader.readLine()) != null) {
            groceries = gson.fromJson(line, GroceriesDto.class);
        }

        if (groceries != null) {
            String response = gson.toJson(groceries);

            resp.setStatus(HttpServletResponse.SC_OK);
            writer = resp.getWriter();
            writer.write(response);
        } else {
            report = new Report();
            report.setMessage("An error has occurred retrieving the grocery.");
            String response = gson.toJson(report);

            resp.setStatus(report.getStatus());
            writer = resp.getWriter();
            writer.write(response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);

        String token = req.getParameter(TOKEN);

        GroceryDto groceryDto = new GroceryDto();
        Grocery grocery = new Grocery();
        grocery.setName(req.getParameter(GROCERY_NAME));
        grocery.setGroceryId(req.getParameter(GROCERY_ID));
        grocery.setPrice(Double.parseDouble(req.getParameter(GROCERY_PRICE)));
        grocery.setDescription(req.getParameter(GROCERY_DESCRIPTION));
        grocery.setRequired(Integer.parseInt(req.getParameter(REQUIRED_QUANTITY)));
        grocery.setRemaining(Integer.parseInt(req.getParameter(REMAINING_QUANTITY)));
        groceryDto.setGrocery(grocery);
        User user = new User();
        user.setUsername(req.getParameter(USERNAME));
        user.setUserId(req.getParameter(USER_ID));
        groceryDto.setUser(user);

        InitUrlConnection<GroceryDto> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(groceryDto, ADD_GROCERY, token, "PUT");

        String line = "";
        GroceryDto response = null;
        while((line = streamReader.readLine()) != null) {
            response = gson.fromJson(line, GroceryDto.class);
        }

        if (response != null) {
            String respStr = gson.toJson(response.getGrocery());

            resp.setStatus(HttpServletResponse.SC_OK);
            writer = resp.getWriter();
            writer.write(respStr);
        }
    }
}