package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class RequestParameters extends Properties {

    Map<String, String> parameters = new HashMap<>();

    public void addParameter(String parameterKey, String parameterValue) {
        parameters.put(parameterKey, parameterValue);
    }

    public String getEncodedParameters() {

        if (parameters.isEmpty()) return "";
        StringBuilder sbEncodedParameters = new StringBuilder();
        try {
            for (String parameter : parameters.keySet()) {
                sbEncodedParameters.append(URLEncoder.encode(parameter, "UTF-8"));
                sbEncodedParameters.append("=");
                sbEncodedParameters.append(URLEncoder.encode(parameters.get(parameter), "UTF-8"));
                sbEncodedParameters.append("&");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sbEncodedParameters.substring(0, sbEncodedParameters.length() - 1);

    }

    public Map<String, String> getParameters() {
        return parameters;
    }

}

