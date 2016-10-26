package com.eaccid.libtranslator.lingualeo.connection;

import java.net.URL;
import java.net.UnknownHostException;

public class RequestHandler {

    //TODO a lot of fields

    private LingualeoHttpConnection connection = new LingualeoHttpConnection();
    private LingualeoResponse response = new LingualeoResponse();
    private ServiceStatus serviceStatus = ServiceStatus.FAILED;

    private RequestParameters requestParameters;
    private String urlString;
    private String cookies;

    private RequestHandler(String urlString, String cookies, RequestParameters requestParameters) {
        this.urlString = urlString;
        this.requestParameters = requestParameters;
        this.cookies = cookies;
    }

    public static RequestHandler newUnauthorizedRequestWithParameters(String urlString, RequestParameters requestParameters) {
        return new RequestHandler(urlString, null, requestParameters);
    }

    public static RequestHandler newAuthorizedRequest(String urlString, String cookies) {
        return new RequestHandler(urlString, cookies, null);
    }

    public static RequestHandler newAuthorizedRequestWithParameters(String urlString, String cookies, RequestParameters requestParameters) {
        return new RequestHandler(urlString, cookies, requestParameters);
    }

    public void handleRequest() {

        if (cookies != null && cookies.isEmpty()) {
            serviceStatus = ServiceStatus.UNAUTHORIZED;
        } else {

            //TODO smth with exception handler

            try {

                URL url = new URL(urlString);
                connection.loadCookies(cookies);
                connection.sendLingualeoRequest(url, RequestMethod.POST, requestParameters);
                response = connection.getResponse();

                serviceStatus = ServiceStatus.SUCCEEDED;

            } catch (UnknownHostException e) {
                serviceStatus = ServiceStatus.CONNECTION_ERROR;
                System.out.println("UnknownHostException");
            } catch (Exception e) {
                serviceStatus = ServiceStatus.FAILED;

                e.printStackTrace();
            }
        }
    }

    public LingualeoResponse getResponse() {
        return response;
    }

    public boolean isHandleRequestSucceeded() {
        return getResponseServiceStatus().getBooleanStatus();
    }

    public String getResponseCookies() {
        return connection.getCookies();
    }

    private ServiceStatus getResponseServiceStatus() {
        return serviceStatus;
    }

}
