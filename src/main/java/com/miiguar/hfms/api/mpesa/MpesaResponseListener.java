package com.miiguar.hfms.api.mpesa;

import com.miiguar.hfms.utils.Log;
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
public class MpesaResponseListener {
    private HttpServer server;
    public static final String TAG = MpesaResponseListener.class.getSimpleName();

    public MpesaResponseListener() {
        try {
            int port = 8000;

            server = HttpServer.create(new InetSocketAddress(port), 0);

            server.createContext(API + LNMO_CALLBACK_URL, new ConfirmHandler());
            server.setExecutor(null);

            // start the server
            server.start();
            System.out.println("Server started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ConfirmHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Request received.");

            BufferedReader br = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8));

            String line;
            StringBuilder buffer = new StringBuilder();
            while((line = br.readLine()) != null) {
                buffer.append(line);
            }

            System.out.println("Resp: " + buffer.toString());
        }
    }

    public static void main(String[] args) {
        MpesaResponseListener listener = new MpesaResponseListener();
    }
}
