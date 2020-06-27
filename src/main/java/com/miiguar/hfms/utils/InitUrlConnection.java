package com.miiguar.hfms.utils;


import com.google.gson.Gson;
import com.miiguar.hfms.config.ConfigureApp;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 *
 * @param <T> Request made to the server/backend
 */
public class InitUrlConnection<T> {
    private BufferedReader streamReader;
    private OutputStreamWriter outputStreamWriter;

    public BufferedReader getReader(T item, String endPoint, String method) throws IOException {
        ConfigureApp conf = new ConfigureApp();
        Properties prop  = conf.getProperties();

        String baseUrl = prop.getProperty("hfms.baseUrl");

        Gson gson = new Gson();
        String request = gson.toJson(item);

        // Set up connections
        final URL url = new URL(baseUrl + endPoint);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
        conn.setDoOutput(true);

        // Set up the output line/request stream
        outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
        outputStreamWriter.write(request);
        outputStreamWriter.flush();

        // Set up the input line/ response stream
        streamReader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        return streamReader;
    }

    public BufferedReader getReader(T item, String endPoint, String token, String method) throws IOException {
        ConfigureApp conf = new ConfigureApp();
        Properties prop  = conf.getProperties();

        String baseUrl = prop.getProperty("hfms.baseUrl");

        Gson gson = new Gson();
        String request = gson.toJson(item);

        // Set up connections
        final URL url = new URL(baseUrl + endPoint);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty(AUTHORIZATION, token);
        conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
        conn.setRequestMethod(method);
        conn.setDoOutput(true);

        // Set up the output line/request stream
        outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
        outputStreamWriter.write(request);
        outputStreamWriter.flush();

        // Set up the input line/ response stream
        streamReader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        return streamReader;
    }

    /**
     * sends request to API and receives an output.
     *
     * @param endPoint API endpoint to be triggered
     * @param token API key used for authentication
     * @param method HTTP request method used
     * @return an outputstream reader
     * @see BufferedReader
     * @throws IOException Input/output exception
     */
    public BufferedReader getReader(String endPoint, String token, String method) throws IOException {
        ConfigureApp conf = new ConfigureApp();
        Properties prop  = conf.getProperties();

        String baseUrl = prop.getProperty("hfms.baseUrl");

        // Set up connections
        final URL url = new URL(baseUrl + endPoint);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty(AUTHORIZATION, token);
        conn.setRequestProperty(CONTENT_TYPE, APPLICATION_FORM_URLENCODED);
        conn.setRequestMethod(method);
        conn.setDoOutput(true);

        // Set up the output line/response stream
        streamReader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        return streamReader;
    }

    public void close() throws IOException {
        outputStreamWriter.close();
        streamReader.close();
    }
}
