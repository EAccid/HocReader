package com.eaccid.hocreader.data.local.db;

import android.content.Context;
import android.util.Log;

import com.eaccid.hocreader.data.local.db.service.DatabaseManager;

public class AppDatabaseManager {

    private final String logTAG = "AppDatabaseManager";
    protected DatabaseManager mDatabaseManager;
    private static int count = 0; //test

    public void loadDatabaseManager(Context baseContext) {
        mDatabaseManager = new DatabaseManager(baseContext);
        Log.i(logTAG, "load db manager from: " + baseContext.getClass().getName() + ", " + ++count);
    }

    public void releaseDatabaseManager() {
        mDatabaseManager.releaseConnection();
        Log.i(logTAG, "release db manager, " + --count);
    }

}
