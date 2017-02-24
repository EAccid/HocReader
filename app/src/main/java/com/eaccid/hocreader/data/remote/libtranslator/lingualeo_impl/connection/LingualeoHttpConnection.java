package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class LingualeoHttpConnection {
    private HttpURLConnection connection;
    private final LingualeoResponse lingualeoResponse;
    private final LingualeoCookies cookies;

    public LingualeoHttpConnection() {
        lingualeoResponse = new LingualeoResponse();
        cookies = new LingualeoCookies();
    }

    void sendLingualeoRequest(URL url, RequestMethod requestMethod, RequestParameters requestParameters) throws Exception {
        openHttpURLConnection(url, requestMethod);
        setHttpHeadersAndCookies();
        setHttpRequestParameters(requestParameters);
        handleHttpRequest();
        storeCookies();
        closeHttpURLConnection();
    }

    @NonNull
    LingualeoResponse getResponse() {
        return lingualeoResponse;
    }

    void loadCookies(String cookies) {
        this.cookies.setCookies(cookies);
    }

    String getCookies() {
        return cookies.getCookies();
    }

    private void storeCookies() throws Exception {
        cookies.setCookies(connection.getHeaderFields().get("Set-Cookie"));
    }

    private void openHttpURLConnection(URL url, RequestMethod requestMethod) throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod.getName());
        connection.setDoOutput(true);
        connection.setDoInput(true);
    }

    private void setHttpHeadersAndCookies() throws IOException {
        connection.setRequestProperty("Accept-Language", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Cookie", cookies.getCookies());
    }

    private void setHttpRequestParameters(RequestParameters requestParameters) throws IOException {
        String data = requestParameters.getEncodedParameters();
        connection.setFixedLengthStreamingMode(data.length());
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
        outputStreamWriter.write(data);
        outputStreamWriter.close();
    }

    private void handleHttpRequest() throws IOException {
        lingualeoResponse.setData(connection.getInputStream());
    }

    private void closeHttpURLConnection() throws IOException {
        connection.disconnect();
    }

}
