package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection;

import android.util.Log;

public class LeoServiceStatus {

    private final String LOG_TAG = "LeoServiceStatus";

    public ServiceStatus getGeneralServiceStatus(final LingualeoResponse lingualeoResponse) {
        if (lingualeoResponse.isEmpty()) {
            Log.i(LOG_TAG, "Response from Linguleo is empty.");
            return ServiceStatus.FAILED;
        }
        String error_msg = lingualeoResponse.getString("error_msg");
        String is_auth_msg = lingualeoResponse.getString("is_authorized");
        if (!error_msg.isEmpty()) {
            if (!is_auth_msg.isEmpty()) {
                Log.i(LOG_TAG, "User has not been authorized: Error message:" + error_msg);
                return ServiceStatus.UNAUTHORIZED;
            }
            Log.i(LOG_TAG, "Failed response from Lingualeo. Error message: " + error_msg);
            return ServiceStatus.FAILED;
        }
        return ServiceStatus.SUCCEEDED;
    }
}
