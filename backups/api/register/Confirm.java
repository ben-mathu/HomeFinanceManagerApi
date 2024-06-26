package com.benardmathu.hfms.api.register;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.code.CodeService;
import com.benardmathu.hfms.data.code.model.Code;
import com.benardmathu.hfms.data.user.Identification;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.utils.BufferRequestReader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.benardmathu.hfms.data.utils.URL.API;
import static com.benardmathu.hfms.data.utils.URL.CONFIRM;

/**
 * @author bernard
 */
@RestController
@RequestMapping(API + CONFIRM)
public class Confirm extends BaseController {
    public static final String TAG = Confirm.class.getSimpleName();

    @PostMapping
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);

        Identification id = gson.fromJson(requestStr, Identification.class);

        if (isCodeCorrect(id)) {

            Report report = new Report();
            report.setStatus(HttpServletResponse.SC_OK);
            report.setMessage("Success");
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
        } else {
            Report report = new Report();
            report.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            report.setMessage("</br>Sorry that code is invalid</br> Please try again or send another");
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
        }
    }

    private boolean isCodeCorrect(Identification id) {
        CodeService codeService = new CodeService();
        Code item = codeService.get(id.getUser().getId());

        if (item.getCode().equals(id.getCode())) {
            codeService.emailConfirmed(item.getCode());
            return true;
        }
        return false;
    }
}