/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benardmathu.hfms.api.groceries;

import com.benardmathu.hfms.api.base.BaseServlet;
import com.benardmathu.hfms.data.grocery.GroceryDao;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import static com.benardmathu.hfms.data.utils.URL.API;
import static com.benardmathu.hfms.data.utils.URL.GROCERY_URL;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@WebServlet(name = "GroceriesApi", urlPatterns = {API + GROCERY_URL})
public class GroceriesApi extends BaseServlet {
    private GroceryDao groceryDao;
    
    public GroceriesApi() {
        groceryDao = new GroceryDao();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int affected = groceryDao.deleteGrocery(req.getParameter(DbEnvironment.GROCERY_ID));
        
        if (affected > 0) {
            Report report = new Report();
            report.setMessage("Grocery item was successfully deleted.");
            report.setStatus(HttpServletResponse.SC_OK);
            
            resp.setStatus(report.getStatus());
            writer = resp.getWriter();
            writer.write(gson.toJson(report));
        }
    }
}
