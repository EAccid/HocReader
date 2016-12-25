package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection;

import android.util.Log;

public class LeoServiceStatus {

    public ServiceStatus getGeneralServiceStatus(final LingualeoResponse lingualeoResponse) {
        String error_msg = lingualeoResponse.getString("error_msg");
        if (!error_msg.isEmpty()) {
            Log.i("LeoServiceStatus", "error_msg from lingualeo service: " + error_msg);
            return ServiceStatus.UNAUTHORIZED;
        }
        if (!lingualeoResponse.getString("is_authorized").isEmpty()
                && !lingualeoResponse.getBoolean("is_authorized")) {
           return ServiceStatus.FAILED;
        }
        return ServiceStatus.SUCCEEDED;
    }
}
