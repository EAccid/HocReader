package com.eaccid.hocreader.temp.underdevelopment;

import android.util.Log;

import java.net.UnknownHostException;

public class ReaderExceptionHandlerImpl implements ReaderExceptionHandler {
    private final String LOG_TAG = "ExceptionHandler";

    //todo handle error
    public void handleError(Throwable e) {
        if (e instanceof UnknownHostException) {
            Log.e(LOG_TAG, "There is no internet connection");
        } else
            e.printStackTrace();
    }



}
