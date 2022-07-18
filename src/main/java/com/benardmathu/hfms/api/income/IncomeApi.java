package com.benardmathu.hfms.api.income;

import com.benardmathu.hfms.api.base.BaseController;
import com.benardmathu.hfms.data.income.IncomeBaseService;
import com.benardmathu.hfms.data.income.IncomeDto;
import com.benardmathu.hfms.data.income.model.Income;
import com.benardmathu.hfms.data.income.model.IncomeChangeService;
import com.benardmathu.hfms.data.income.model.OnInComeChange;
import com.benardmathu.hfms.data.status.AccountStatus;
import com.benardmathu.hfms.data.status.AccountStatusBaseService;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.status.Status;
import com.benardmathu.hfms.utils.BufferRequestReader;
import com.benardmathu.hfms.utils.GenerateRandomString;
import com.benardmathu.hfms.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;
import static com.benardmathu.hfms.data.utils.URL.INCOME_ENDPOINT;
import static com.benardmathu.hfms.utils.Constants.COMPLETE;
import static com.benardmathu.hfms.utils.Constants.DATE_FORMAT;

/**
 * @author bernard
 */
@RestController
@RequestMapping(value = INCOME_ENDPOINT)
public class IncomeApi extends BaseController {

    @Autowired
    private IncomeBaseService incomeService;

    @Autowired
    private IncomeChangeService incomeChangeService;

    @Autowired
    private AccountStatusBaseService accountStatusService;

    @PostMapping
    protected ResponseEntity<IncomeDto> addIncome(@RequestParam(USER_ID) String userId, @RequestBody IncomeDto incomeDto,
                                       HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse
    ) throws ServletException, IOException {

//        String userId = httpServletRequest.getParameter(USER_ID);
//        String requestStr = BufferRequestReader.bufferRequest(httpServletRequest);
//
//        IncomeDto incomeDto = gson.fromJson(requestStr, IncomeDto.class);
        Income income = incomeDto.getIncome();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String today = sf.format(date);

        GenerateRandomString randomString = new GenerateRandomString(
                12, new SecureRandom()
        );

        Long incomeId = Long.parseLong(randomString.nextString());

        income.setIncomeId(incomeId);
        income.setCreatedAt(today);
        income.setSchedule(income.getSchedule());
        
        OnInComeChange onInComeChange = new OnInComeChange();
        if (incomeService.save(income) != null) {
            onInComeChange.setAmount(income.getAmount());
            onInComeChange.setCreatedAt(today);
            onInComeChange.setIncomeId(incomeId);
            onInComeChange.setOnChangeStatus(true);

            incomeChangeService.save(onInComeChange);
        }

        incomeDto.setIncome(income);
        incomeDto.setOnIncomeChange(onInComeChange);

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        Report report = updateIncomeStatus(userId);
        incomeDto.setReport(report);
        
        return new ResponseEntity<>(incomeDto, HttpStatus.OK);
    }

    private Report updateIncomeStatus(String userId) {
        AccountStatus accountStatus = new AccountStatus();

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String today = sf.format(date);

        Status status = new Status();
        status.status = COMPLETE;
        status.date = today;

        String statusStr = gson.toJson(status);
        accountStatus.setIncomeStatus(statusStr);
        accountStatus.setUserId(userId);

        Report report = new Report();
        if (accountStatusService.updateIncomeStatus(accountStatus)) {
            Log.d(TAG, "Update table " + ACCOUNT_STATUS_TB_NAME);
            report.setStatus(HttpServletResponse.SC_OK);
            report.setMessage("Successfully saved income");
        } else {
            report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            report.setMessage("Please try again");
        }
        
        return report;
    }

    @PutMapping
    protected ResponseEntity<IncomeDto> updateIncomeChange(@RequestBody IncomeDto incomeDto,
                                      HttpServletRequest req, HttpServletResponse resp
    ) throws ServletException, IOException {

//        String requestStr = BufferRequestReader.bufferRequest(req);
//
//        IncomeDto incomeDto = gson.fromJson(requestStr, IncomeDto.class);
        
        OnInComeChange onInComeChange = new OnInComeChange();
        onInComeChange.setAmount(incomeDto.getIncome().getAmount());
        onInComeChange.setCreatedAt(new SimpleDateFormat(DATE_FORMAT).format(new Date()));
        
        Income income = incomeService.get(req.getParameter(USER_ID));
        incomeDto.getIncome().setAmount(income.getAmount() + incomeDto.getIncome().getAmount());
        
        onInComeChange.setIncomeId(income.getIncomeId());
        
        OnInComeChange inComeChange = incomeChangeService.save(onInComeChange);
        incomeDto.setOnIncomeChange(inComeChange);
        
        Income in = incomeService.save(incomeDto.getIncome());

        Report report = new Report();
        report.setStatus(HttpServletResponse.SC_OK);
        report.setMessage("Successfully Updated");

        incomeDto.setReport(report);
        incomeDto.setIncome(in);

        resp.setStatus(report.getStatus());
        return new ResponseEntity<>(incomeDto, HttpStatus.OK);
    }
}
