package com.miiguar.hfms.utils;

import com.google.gson.Gson;
import com.miiguar.hfms.config.ConfigureApp;
import com.miiguar.hfms.data.daraja.AccessToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;
import java.util.Properties;

import static com.miiguar.hfms.data.utils.URL.GENERATE_TOKEN;
import static com.miiguar.hfms.utils.Constants.ACCESS_TOKEN;

/**
 * @author bernard
 */
public class GetAccessTokenTask implements Runnable {
    public static final String TAG = GetAccessTokenTask.class.getSimpleName();
    private static IntervalChangeListener listener;
    private static Long intervalWhenError;

    public static void setListener(IntervalChangeListener intervalChangeListener) {
        listener = intervalChangeListener;
    }

    @Override
    public void run() {
        AccessToken accessToken;
        try {
            accessToken = getMpesaApiAccessToken();
            ConfigureApp app = new ConfigureApp();
            Properties prop = app.getProperties();

            prop.setProperty(ACCESS_TOKEN, accessToken.getAccessToken());
            app.setProperties(prop);

            Log.d(TAG, "Access Token acquired. Token expires in: " + accessToken.getExpires());

            if (listener != null) {
                long interval = Long.parseLong(accessToken.getExpires());
                listener.setIntervals(interval);
            }
        } catch (IOException e) {
            // handle UnknownHostException: internet problems
            if (e instanceof UnknownHostException || e instanceof HttpConnectTimeoutException) {
                if (intervalWhenError > 0) intervalWhenError += 5;
                else intervalWhenError = 1L;

                if (listener != null) {
                    listener.setIntervals(intervalWhenError);
                }

                Log.d(TAG, "Error handled");
            } else {
                Log.e(TAG, "Error retrieving access token: ", e);
            }
        }
    }

    /**
     * Generate Daraja API Access token
     */
    public AccessToken getMpesaApiAccessToken() throws IOException {

        InitUrlConnection<Void> initUrlConnection = new InitUrlConnection<>();
        BufferedReader streamReader = initUrlConnection.getReader(GENERATE_TOKEN, "GET");

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = streamReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        Gson gson = new Gson();
        return gson.fromJson(stringBuilder.toString(), AccessToken.class);
    }
}
