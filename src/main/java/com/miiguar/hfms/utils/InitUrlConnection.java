package com.miiguar.hfms.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miiguar.hfms.config.ConfigureApp;
import com.miiguar.hfms.data.models.ConfirmationResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * @author bernard
 *
 * @param <T> Request made to the server/backend
 * @param <E> Response received after the request has been processed.
 */
public class InitUrlConnection<T,E> {
    private BufferedReader streamReader;
    private OutputStreamWriter outputStreamWriter;

    public E getReader(T item, String endPoint) throws IOException {
        ConfigureApp conf = new ConfigureApp();
        Properties prop  = conf.getProperties();

        String baseUrl = prop.getProperty("hfms.baseUrl");

        Gson gson = new Gson();
        String request = gson.toJson(item);

        // Set up connections
        final URL url = new URL(baseUrl + endPoint);
        final URLConnection conn = url.openConnection();
        conn.setDoOutput(true);

        // Set up the output line/request stream
        outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
        outputStreamWriter.write(request);
        outputStreamWriter.flush();

        // Set up the input line/ response stream
        streamReader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        String line = "";
        E response = null;
        while((line = streamReader.readLine()) != null) {
            Type typeToken = new TypeToken<E>() {}.getType();
            response = gson.fromJson(line, typeToken);
        }
        return response;
    }

    public E getReader(T item, String endPoint, String token) throws IOException {
        ConfigureApp conf = new ConfigureApp();
        Properties prop  = conf.getProperties();

        String baseUrl = prop.getProperty("hfms.baseUrl");

        Gson gson = new Gson();
        String request = gson.toJson(item);

        // Set up connections
        final URL url = new URL(baseUrl + endPoint);
        final URLConnection conn = url.openConnection();
        conn.setRequestProperty("Authorization", token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Set up the output line/request stream
        outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
        outputStreamWriter.write(request);
        outputStreamWriter.flush();

        // Set up the input line/ response stream
        streamReader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        String line = "";
        E response = null;
        while((line = streamReader.readLine()) != null) {
            Type typeToken = new TypeToken<E>() {}.getType();
            response = gson.fromJson(line, typeToken);
        }
        return response;
    }

    public void close() throws IOException {
        outputStreamWriter.close();
        streamReader.close();
    }
}
