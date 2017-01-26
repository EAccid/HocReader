package com.eaccid.hocreader.underdevelopment;

import android.util.Log;

import java.net.UnknownHostException;

public class ReaderExceptionHandlerImpl implements ReaderExceptionHandler {
    private final String LOG_TAG = "ExceptionHandler";

    @Override
    public void handleError(Throwable e) {
        if (e instanceof UnknownHostException) {
            Log.e(LOG_TAG, "There is no internet connection");
        } else
            e.printStackTrace();
    }

}
