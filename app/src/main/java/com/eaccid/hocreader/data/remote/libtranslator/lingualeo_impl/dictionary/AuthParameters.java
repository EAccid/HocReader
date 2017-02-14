package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.LingualeoResponse;

public class AuthParameters {

    private LingualeoResponse lingualeoResponse;
    private String picUrl;
    private String fullName;
    private boolean isAuth;
    private String email;

    public AuthParameters(LingualeoResponse lingualeoResponse) {
        initLingualeoResponse(lingualeoResponse);
        loadData();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getFullName() {
        return fullName;
    }

    private void initLingualeoResponse(LingualeoResponse lingualeoResponse) {
        this.lingualeoResponse = lingualeoResponse == null ? new LingualeoResponse() : lingualeoResponse;
    }

    private void loadData() {
        fullName = lingualeoResponse.getString("fullName").replace("\"", "");
        picUrl = lingualeoResponse.getString("avatar_mini").replace("\"", "");
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
