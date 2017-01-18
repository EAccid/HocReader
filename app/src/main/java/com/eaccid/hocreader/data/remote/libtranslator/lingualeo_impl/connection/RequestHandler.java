package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection;

import java.net.URL;
import java.net.UnknownHostException;

public class RequestHandler {

    private LingualeoHttpConnection connection;
    private ServiceStatus serviceStatus = ServiceStatus.FAILED;
    private RequestParameters requestParameters;
    private String urlString;
    private String cookies;
    private RequestExceptionHandler exceptionHandler;

    private RequestHandler(String urlString, String cookies, RequestParameters requestParameters) {
        this.urlString = urlString;
        this.requestParameters = requestParameters;
        this.cookies = cookies;
        this.connection = new LingualeoHttpConnection();
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

    public void setRequestExceptionHandler(RequestExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void handleRequest() {
        if (exceptionHandler == null)
            throw new RuntimeException("RequestExceptionHandler can't be null!");
        if (cookies != null && cookies.isEmpty()) {
            serviceStatus = ServiceStatus.UNAUTHORIZED;
            return;
        }
        try {
            URL url = new URL(urlString);
            connection.loadCookies(cookies);
            connection.sendLingualeoRequest(url, RequestMethod.POST, requestParameters);
            LeoServiceStatus leoServiceStatus = new LeoServiceStatus();
            serviceStatus = leoServiceStatus.getGeneralServiceStatus(connection.getResponse());
        } catch (UnknownHostException e) {
            serviceStatus = ServiceStatus.CONNECTION_ERROR;
            exceptionHandler.handleException(e);
        } catch (Exception e) {
            serviceStatus = ServiceStatus.FAILED;
            exceptionHandler.handleException(e);
        }
    }

    public LingualeoResponse getResponse() {
        return connection.getResponse();
    }

    public boolean isHandleRequestSucceeded() {
        return getServiceStatus().getBooleanStatus();
    }

    public String getCookies() {
        return connection.getCookies();
    }

    private ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

}
