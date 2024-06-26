package com.benardmathu.hfms.api.transaction.threadrunner;

import com.google.gson.Gson;
import com.benardmathu.hfms.data.transactions.TransactionService;
import com.benardmathu.hfms.data.daraja.callback.CallbackMetadata;
import com.benardmathu.hfms.data.daraja.callback.CallbackResponse;
import com.benardmathu.hfms.data.daraja.callback.StkCallback;
import com.benardmathu.hfms.data.transactions.model.Transaction;
import com.benardmathu.hfms.data.jar.MoneyJarsService;
import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.JarScheduleDateRel;
import com.benardmathu.hfms.data.tablerelationships.schedulejarrel.MoneyJarScheduleServices;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static com.benardmathu.hfms.data.utils.URL.*;
import com.benardmathu.hfms.utils.Constants;
import static com.benardmathu.hfms.utils.Constants.DATE_FORMAT;
import com.benardmathu.hfms.utils.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

/**
 * @author bernard
 */
public class MpesaResponseListener implements StopServerListener {
    private HttpServer server;
    public static final String TAG = MpesaResponseListener.class.getSimpleName();
    private final MoneyJar jar;
    private final Long transactionId;

    public MpesaResponseListener(MoneyJar jar, Long transactionId) {
        this.jar = jar;
        this.transactionId = transactionId;
        
        // start the server
        try {
            int port = 8000;
            server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext(LNMO_CALLBACK_URL, new ConfirmHandler(this, jar, transactionId));
            
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            server.setExecutor(threadPoolExecutor);

            // start the server
            server.start();
            Log.d(TAG, "Server started");
        } catch (IOException e) {
            Log.d(TAG, "Error: " + e.getMessage());
        }
    }

    @Override
    public void stopListening() {
        server.stop(10);
    }

    public static class ConfirmHandler implements HttpHandler {

        private StopServerListener listener;
        private final TransactionService transactionService;
        private final MoneyJarsService moneyJarsDao;
        private final MoneyJarScheduleServices moneyJarScheduleServices;
        
        private final Long transactionId;
        private final MoneyJar jar;

        public ConfirmHandler(StopServerListener listener, MoneyJar jar, Long transactionId) {
            this.listener = listener;
            this.transactionService = new TransactionService();
            this.jar = jar;
            
            moneyJarsDao = new MoneyJarsService();
            moneyJarScheduleServices = new MoneyJarScheduleServices();
            this.transactionId = transactionId;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Log.d(TAG, "Request received.");

            BufferedReader br = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8));

            String line;
            StringBuilder buffer = new StringBuilder();
            while((line = br.readLine()) != null) {
                buffer.append(line);
            }

            Gson gson = new Gson();
            CallbackResponse callbackResponse = gson.fromJson(buffer.toString(), CallbackResponse.class);
            StkCallback stkCallback = callbackResponse.getBody().getCallback();

            if (stkCallback.getMetadata() == null
                    && !stkCallback.getResultDesc().isEmpty()) {
                
                final URL url = new URL(httpExchange.getHttpContext() + "/mpesa/lnmo-url");
                final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                
                try (OutputStream writer = conn.getOutputStream()) {
                    Report report = new Report();
                    report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    report.setMessage(stkCallback.getResultDesc());
                    String responseBody = gson.toJson(report);
                    
                    writer.write(responseBody.getBytes());
                }
                
                listener.stopListening();
                return;
            }

            // get transaction already set in the database
            Transaction transaction = transactionService.get(transactionId);

            if (transaction != null) {
                transaction.setPaymentStatus(true);
                transaction.setPaymentTimestamp(new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date().getTime()));

                // Serialize the callback metadata
                CallbackMetadata metadata = stkCallback.getMetadata();
                String metadataStr = gson.toJson(metadata);

                // save the transaction to database
                transactionService.save(transaction);
                
                jar.setPaymentStatus(true);
                moneyJarsDao.update(jar);
                
                List<JarScheduleDateRel> list = moneyJarScheduleServices.getAllByJarId(jar.getMoneyJarId());
                JarScheduleDateRel jarScheduleDateRel;
                list.forEach(jarSchedule -> {
                    try {
                        Date date = new SimpleDateFormat(DATE_FORMAT).parse(jarSchedule.getScheduleDate());
                        Date now = new Date();
                        
                        if (now.getTime() > date.getTime() && !jarSchedule.isPaymentStatus()) {
                            
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(MpesaResponseListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                final URL url = new URL(httpExchange.getHttpContext() + "/mpesa/lnmo-url");
                final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                
                try (OutputStream writer = conn.getOutputStream()) {
                    Report report = new Report();
                    report.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    report.setMessage(stkCallback.getResultDesc());
                    String responseBody = gson.toJson(report);
                    
                    writer.write(responseBody.getBytes());
                }
            }

            Log.d(TAG, "Resp: " + callbackResponse.toString());

            listener.stopListening();
        }
    }

    public static void main(String[] args) {
        new MpesaResponseListener(null, 0L);
    }
}
