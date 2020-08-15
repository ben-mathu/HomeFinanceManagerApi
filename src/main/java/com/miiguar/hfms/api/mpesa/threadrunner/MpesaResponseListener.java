package com.miiguar.hfms.api.mpesa.threadrunner;

import com.google.gson.Gson;
import com.miiguar.hfms.data.daraja.TransactionDao;
import com.miiguar.hfms.data.daraja.callback.CallbackMetadata;
import com.miiguar.hfms.data.daraja.callback.CallbackResponse;
import com.miiguar.hfms.data.daraja.callback.StkCallback;
import com.miiguar.hfms.data.daraja.models.Transaction;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static com.miiguar.hfms.data.utils.URL.*;

/**
 * @author bernard
 */
public class MpesaResponseListener implements StopServerListener {
    private HttpServer server;
    public static final String TAG = MpesaResponseListener.class.getSimpleName();
    private final String randomString;

    public MpesaResponseListener(String randomString) {
        this.randomString = randomString;
        try {
            int port = 8000;

            server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext(API + LNMO_CALLBACK_URL + "/" + randomString, new ConfirmHandler(this));
            server.setExecutor(null);

            // start the server
            server.start();
            System.out.println("Server started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopListening() {
        server.stop(10);
    }

    public static class ConfirmHandler implements HttpHandler {

        private StopServerListener listener;
        private final TransactionDao transactionDao;

        public ConfirmHandler(StopServerListener listener) {
            this.listener = listener;
            this.transactionDao = new TransactionDao();
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Request received.");

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
                // TODO: Notify the user of cancelled transaction
                listener.stopListening();
                return;
            }

            // get transaction already set in the database
            Transaction transaction = transactionDao.get(stkCallback.getCheckoutRequestID());

            if (transaction != null) {
                transaction.setResultCode(stkCallback.getResultCode());
                transaction.setResultDesc(stkCallback.getResultDesc());

                // Serialize the callback metadata
                CallbackMetadata metadata = stkCallback.getMetadata();
                String metadataStr = gson.toJson(metadata);

                transaction.setCallbackMetadata(metadataStr);
                transaction.setTransactionComplete(true);

                // save the transaction to database
                transactionDao.save(transaction);

                // TODO: Notify the user that the transaction was successful
            }

            System.out.println("Resp: " + callbackResponse.toString());

            listener.stopListening();
        }
    }

    public String getRandomString() {
        return randomString;
    }

    public static void main(String[] args) {
        MpesaResponseListener listener = new MpesaResponseListener("");
    }
}
