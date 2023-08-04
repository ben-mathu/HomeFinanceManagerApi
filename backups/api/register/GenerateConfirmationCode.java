package com.benardmathu.hfms.api.register;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.code.CodeService;
import com.benardmathu.hfms.data.code.CodeRepository;
import com.benardmathu.hfms.data.code.model.Code;
import com.benardmathu.hfms.data.user.Identification;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.user.model.User;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.GenerateRandomString;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Random;

import static com.benardmathu.hfms.data.utils.URL.GENERATE_CODE;

/**
 * @author bernard
 */
@RestController
@RequestMapping(GENERATE_CODE)
public class GenerateConfirmationCode extends BaseController {

    @Autowired
    private CodeRepository codeRepository;

    private CodeService service = new CodeService();

    @PostMapping
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestStr = BufferRequestReader.bufferRequest(req);
        Identification user = gson.fromJson(requestStr, Identification.class);

        String code = generateCode();

        if (Integer.parseInt(saveCode(code, user.getUser()).getCode()) > 0) {
            service.sendCodeToEmail(user.getUser(), code);

            Report report = new Report();
            report.setMessage("Success");
            report.setStatus(HttpServletResponse.SC_OK);
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
        } else {
            Report report = new Report();
            report.setMessage("Error occurred please send the code again.");
            report.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String responseStr = gson.toJson(report);
            writer = resp.getWriter();
            writer.write(responseStr);
            Log.d(TAG, "Error storing confirmation code.");
        }
    }

    public String generateCode() {
        GenerateRandomString rand = new GenerateRandomString(6, new Random(), "0123456789");
        return rand.nextString();
    }

    public Code saveCode(String code, User user) {
        Code item = new Code();
        item.setCode(code);
        item = service.saveCode(item, user.getId().toString());

        Log.d(TAG, "Affected Rows:" + item);
        return item;
    }
}