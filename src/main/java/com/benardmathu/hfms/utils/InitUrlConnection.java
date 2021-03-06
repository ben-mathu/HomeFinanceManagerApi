package com.benardmathu.hfms.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.benardmathu.hfms.config.ConfigureApp;
import com.benardmathu.hfms.data.status.Report;

import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Properties;

import static javax.ws.rs.core.HttpHeaders.*;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author bernard
 *
 * @param <T> Request made to the server/backend
 */
public class InitUrlConnection<T> {
    public static final String TAG = InitUrlConnection.class.getSimpleName();

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
        conn.setRequestProperty(AUTHORIZATION, "Bearer " + token);
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
        conn.setRequestProperty(AUTHORIZATION, "Bearer " + token);
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

    public BufferedReader getReaderForDarajaApi(String generateToken, String method) throws IOException {
        ConfigureApp conf = new ConfigureApp();
        Properties prop  = conf.getProperties();

        String baseUrl = prop.getProperty("daraja.baseUrl");
        String grantType = prop.getProperty("daraja.grantType");
        String host = prop.getProperty("daraja.host");
        String key = prop.getProperty("daraja.consumerKey");
        String secret = prop.getProperty("daraja.consumerSecret");

        String cipher = Base64EncoderDecoder.encode(key + ":" + secret);

        // Set up connections
        final URL url = new URL(baseUrl + generateToken + "?grant_type=" + grantType);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty(HOST, host);
        conn.setRequestProperty(AUTHORIZATION, "Basic " + cipher);
        conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
        conn.setRequestMethod(method);
        conn.setDoOutput(true);

        // Set up the output line/response stream
        streamReader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        return streamReader;
    }

    public BufferedReader getReaderForDarajaApi(T item, String endPoint, String generateToken, String method) throws IOException {
        ConfigureApp conf = new ConfigureApp();
        Properties prop  = conf.getProperties();

        String baseUrl = prop.getProperty("daraja.baseUrl");
        String host = prop.getProperty("daraja.host");

        // Set up connections
        final URL url = new URL(baseUrl + endPoint);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty(HOST, host);
        conn.setRequestProperty(AUTHORIZATION, "Bearer " + generateToken);
        conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
        conn.setRequestMethod(method);
        conn.setDoOutput(true);

        try (OutputStream writer = conn.getOutputStream()) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            String requestBody = gson.toJson(item);

            Log.d(TAG, "Request: " + requestBody);

            writer.write(requestBody.getBytes());
        } catch (UnknownHostException e) {
            Log.d(TAG, "Error, Connection problem.");
        }
        
        InputStreamReader inputStreamReader = null;
        if (conn.getResponseCode() >= 400) {
            inputStreamReader = new InputStreamReader(conn.getErrorStream());
        } else {
            inputStreamReader = new InputStreamReader(conn.getInputStream());
        }

        // Set up the output line/response stream
        streamReader = new BufferedReader(inputStreamReader);
        return streamReader;
    }
}
