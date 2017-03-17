package com.eaccid.hocreader.provider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkAvailablenessImpl implements NetworkAvailableness {
    private Context context;

    public NetworkAvailablenessImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean isNetworkAvailable() {
        return isOnline();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
