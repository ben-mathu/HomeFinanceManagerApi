/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.benardmathu.hfms.api.groceries;

import com.benardmathu.hfms.api.base.BaseServlet;
import com.benardmathu.hfms.data.grocery.GroceryDao;
import com.benardmathu.hfms.data.grocery.GroceryRepository;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping(name = "GroceriesApi", value = GROCERY_URL)
public class GroceriesApi extends BaseServlet {
//    private GroceryDao groceryDao;
    @Autowired
    private GroceryRepository groceryRepository;
//    public GroceriesApi() {
//        groceryDao = new GroceryDao();
//    }

    @DeleteMapping
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        groceryRepository.deleteById(Long.parseLong(req.getParameter(DbEnvironment.GROCERY_ID)));

        Report report = new Report();
        report.setMessage("Grocery item was successfully deleted.");
        report.setStatus(HttpServletResponse.SC_OK);

        resp.setStatus(report.getStatus());
        writer = resp.getWriter();
        writer.write(gson.toJson(report));
    }
}
