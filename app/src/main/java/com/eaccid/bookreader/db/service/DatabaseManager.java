package com.eaccid.bookreader.db.service;


import android.content.Context;
import android.support.annotation.VisibleForTesting;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class DatabaseManager {

    private static DatabaseManager databaseManager;
    private DatabaseHelper databaseHelper;

    private DatabaseManager(Context context) {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
    }

    public static DatabaseManager getInstance(Context context) {
        if (null == databaseManager) {
            databaseManager = new DatabaseManager(context);
        }
        return databaseManager;
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
        if (null == databaseManager) {
            databaseManager = new DatabaseManager(context, databaseName);
        }
        return databaseManager;
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public void releaseConnection() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public BookDaoService getBookService() throws SQLException {
        return new BookDaoService(databaseHelper);
    }

    public WordDaoService getWordService() throws SQLException {
        return new WordDaoService(databaseHelper);
    }

}
