package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.LingualeoResponse;

public class AuthParameters {
    private LingualeoResponse lingualeoResponse;
    private String picUrl;
    private String fullName;
    private boolean isAuth;
    private String email;

    AuthParameters(LingualeoResponse lingualeoResponse) {
        initLingualeoResponse(lingualeoResponse);
        loadData();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public String getEmail() {
        return email;
    }

    void setAuth(boolean auth) {
        isAuth = auth;
    }

    void setEmail(String email) {
        this.email = email;
    }

    private void initLingualeoResponse(LingualeoResponse lingualeoResponse) {
        this.lingualeoResponse = lingualeoResponse == null ?
                new LingualeoResponse() : lingualeoResponse;
    }

    private void loadData() {
        fullName = lingualeoResponse.getString("fullname").replace("\"", "");
        picUrl = lingualeoResponse.getString("avatar_mini").replace("\"", "");
    }

}
