package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection;

public class LeoServiceMessages {

    private final LingualeoResponse response;

    public LeoServiceMessages(final LingualeoResponse response) {
        this.response = response;
    }

    public String getErrorMessage() {
        return response.getString("error_msg");
    }
}
