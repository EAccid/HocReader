package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl;

import android.util.Log;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.RequestExceptionHandler;

import java.net.UnknownHostException;

public class RequestExceptionHandlerImpl implements RequestExceptionHandler {

    private final String LOG_TAG = "RequestExceptionHandler";

    @Override
    public void handleException(Exception e) {

        if (e instanceof UnknownHostException) {
            Log.e(LOG_TAG, "There is no internet connection");
        } else
            e.printStackTrace();

    }
}
