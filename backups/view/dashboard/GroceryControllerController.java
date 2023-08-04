package com.benardmathu.hfms.view.dashboard;

import com.benardmathu.hfms.data.grocery.model.Grocery;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import static com.benardmathu.hfms.data.utils.URL.DELETE_GROCERY;
import com.benardmathu.hfms.utils.Constants;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles HTTP Requests for groceries
 * @author bernard
 */
@Controller("/dashboard/grocery-controller")
public class GroceryControllerController extends BaseController {

    @DeleteMapping
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String queryStr = "?" + DbEnvironment.GROCERY_ID + "=" + req.getParameter(DbEnvironment.GROCERY_ID);
        
        String token = req.getParameter(Constants.TOKEN);
        
        InitUrlConnection<Grocery> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(URL.DELETE_GROCERY + queryStr, token, "DELETE");
        
        String line;
        StringBuilder builder = new StringBuilder();
        
        while ((line = streamReader.readLine()) != null) {
            builder.append(line);
        }
        
        if (!builder.toString().contains("200")) {
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            
            writer = resp.getWriter();
            writer.write(builder.toString());
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
            
            writer = resp.getWriter();
            writer.write(builder.toString());
        }
    }
}
