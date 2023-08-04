package com.benardmathu.hfms.view.dashboard;

import com.benardmathu.hfms.data.income.IncomeDto;
import com.benardmathu.hfms.data.utils.DbEnvironment;
import static com.benardmathu.hfms.data.utils.URL.UPDATE_USER_INCOME;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.Constants;
import com.benardmathu.hfms.utils.InitUrlConnection;
import com.benardmathu.hfms.view.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handle income request methods
 * @author bernard
 */
@Controller("/dashboard/income-controller")
public class IncomeControllerController extends BaseController {

    @PutMapping
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestStr = BufferRequestReader.bufferRequest(req);
        String token = req.getParameter(Constants.TOKEN);
        String userId = req.getParameter(DbEnvironment.USER_ID);
        
        IncomeDto dto = gson.fromJson(requestStr, IncomeDto.class);
        
        InitUrlConnection<IncomeDto> conn = new InitUrlConnection<>();
        BufferedReader streamReader = conn.getReader(dto,
                URL.UPDATE_USER_INCOME + "?" + DbEnvironment.USER_ID + "=" + userId,
                token, "PUT"
        );
        
        String line;
        StringBuilder builder = new StringBuilder();
        
        while ((line = streamReader.readLine()) != null) {            
            builder.append(line);
        }
        
        int status = gson.fromJson(builder.toString(), IncomeDto.class).getReport().getStatus();
        resp.setStatus(status);
        
        writer = resp.getWriter();
        writer.write(builder.toString());
    }
}
