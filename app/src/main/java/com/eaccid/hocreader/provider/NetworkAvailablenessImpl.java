package com.eaccid.hocreader.provider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.injection.ApplicationContext;

import javax.inject.Inject;

public class NetworkAvailablenessImpl implements NetworkAvailableness {

    @Inject
    @ApplicationContext
    Context context;

    public NetworkAvailablenessImpl() {
        App.getAppComponent().inject(this);
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
