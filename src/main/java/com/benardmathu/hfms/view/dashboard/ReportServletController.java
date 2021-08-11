package com.benardmathu.hfms.view.dashboard;

import com.benardmathu.hfms.data.report.ReportRequest;
import static com.benardmathu.hfms.data.utils.DbEnvironment.USER_ID;
import static com.benardmathu.hfms.data.utils.URL.GET_REPORTS;
import static com.benardmathu.hfms.utils.Constants.TOKEN;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseServlet;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@WebServlet(name = "ReportServletController", urlPatterns = {"/reports/reports-controller/*"})
public class ReportServletController extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        
        String userId = req.getParameter(USER_ID);
        String token = req.getParameter(TOKEN);
        
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setFrom(from);
        reportRequest.setTo(to);
        reportRequest.setUserId(userId);
        
        InitUrlConnection<ReportRequest> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(reportRequest, GET_REPORTS, token, "POST");
        
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = streamReader.readLine()) != null) {
            builder.append(line);
        }
        
        resp.setStatus(HttpServletResponse.SC_OK);
        writer = resp.getWriter();
        writer.write(builder.toString());
    }
    
}
