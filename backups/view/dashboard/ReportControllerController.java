package com.benardmathu.hfms.view.dashboard;

import com.benardmathu.hfms.data.report.ReportRequest;
import static com.benardmathu.hfms.data.utils.DbEnvironment.USER_ID;
import static com.benardmathu.hfms.data.utils.URL.GET_REPORTS;
import static com.benardmathu.hfms.utils.Constants.TOKEN;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bernard
 */
@Controller("/reports/reports-controller")
public class ReportControllerController extends BaseController {

    @GetMapping
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter(DbEnvironment.USER_ID);
        String token = req.getParameter(TOKEN);
        
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setFrom(from);
        reportRequest.setTo(to);
        reportRequest.setUserId(Long.parseLong(userId));
        
        InitUrlConnection<ReportRequest> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(reportRequest, URL.GET_REPORTS, token, "POST");
        
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
