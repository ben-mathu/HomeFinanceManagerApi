package com.miiguar.hfms.api.mpesa;

import com.miiguar.hfms.api.base.BaseServlet;
import com.miiguar.hfms.config.ConfigureApp;
import com.miiguar.hfms.data.daraja.models.LnmoRequest;
import com.miiguar.hfms.utils.BufferRequestReader;
import com.miiguar.hfms.utils.GenerateCipher;
import com.miiguar.hfms.utils.InitUrlConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.URL.*;
import static com.miiguar.hfms.utils.Constants.*;

/**
 * @author bernard
 */
@WebServlet(API + TRANSACTIONS)
public class SendTransaction extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String requestStr = BufferRequestReader.bufferRequest(httpServletRequest);

        LnmoRequest request = gson.fromJson(requestStr, LnmoRequest.class);

        SimpleDateFormat sf = new SimpleDateFormat(DARAJA_DATE_FORMAT);
        Date now = new Date();
        String timestamp = sf.format(now);

        ConfigureApp app = new ConfigureApp();
        Properties properties = app.getProperties();

        String passKey = properties.getProperty("daraja.lnmo.passkey");

        GenerateCipher generateCipher = new GenerateCipher();
        String password = generateCipher.encode(request.getBusinessShortCode() + passKey + timestamp);

        request.setPassword(password);
        request.setTimestamp(timestamp);
        request.setTransactionType(PAY_BILL);
        request.setCallbackUrl(LNMO_CALLBACK_URL);

        InitUrlConnection<LnmoRequest> connection = new InitUrlConnection<>();
        BufferedReader streamReader = connection.getReaderForDarajaApi(
                request, LNMO, properties.getProperty(ACCESS_TOKEN), "POST"
        );
    }
}
