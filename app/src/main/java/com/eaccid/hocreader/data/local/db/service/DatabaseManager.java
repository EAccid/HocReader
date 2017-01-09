package com.eaccid.hocreader.data.local.db.service;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.eaccid.hocreader.injection.ApplicationContext;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import java.sql.SQLException;

public class DatabaseManager {

    private DatabaseHelper databaseHelper;
    private final String logTAG = "DatabaseManager";

    public DatabaseManager(@ApplicationContext Context context) {
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        Log.i(logTAG, "load db manager: " + context.getClass().getName());
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    private DatabaseManager(Context context, String databaseName) {
        if (databaseHelper == null) {
            OpenHelperManager.setHelper(new DatabaseHelper(context, databaseName));
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    private static DatabaseManager getTestInstance(Context context, String databaseName) {
        return new DatabaseManager(context, databaseName);
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public void releaseConnection() {
        OpenHelperManager.releaseHelper();
        Log.i(logTAG, "release db manager");
    }

    public BookDaoService getBookService() throws SQLException {
        return new BookDaoService(databaseHelper);
    }

    public WordDaoService getWordService() throws SQLException {
        return new WordDaoService(databaseHelper);
    }

}
