package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class LingualeoHttpConnection {

    HttpURLConnection connection;
    LingualeoResponse lingualeoResponse;
    LingualeoCookies cookies;

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

    LingualeoResponse getResponse() {
        return lingualeoResponse;
    }

    void loadCookies(String cookies) {
        this.cookies.setCookies(cookies);
    }

    String getCookies() {
        return cookies.getCookies();
    }

    private void storeCookies() {
        try {
            cookies.setCookies(connection.getHeaderFields().get("Set-Cookie"));
        } catch (Exception ignored) {
        }

    }

    private void openHttpURLConnection(URL url, RequestMethod requestMethod) throws Exception {
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod.getName());
        connection.setDoOutput(true);
    }

    private void setHttpHeadersAndCookies() throws IOException {
        connection.setRequestProperty("Accept-Language", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("cookie", cookies.getCookies());
    }

    private void setHttpRequestParameters(RequestParameters requestParameters) {

        try {
            String data = requestParameters.getEncodedParameters();
            //the number of bytes which will be written to the OutputStream.
            connection.setFixedLengthStreamingMode(data.length());
            //an OutputStreamWriter is a bridge from character streams to byte streams:
            // characters written to it are encoded into bytes using a specified charset
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (Exception ignored) {
        }
    }

    private void handleHttpRequest() throws Exception {
        lingualeoResponse.setData(connection.getInputStream());
    }

    private void closeHttpURLConnection() {
        try {
            connection.disconnect();
        } catch (Exception ignored) {
        }

    }

}
