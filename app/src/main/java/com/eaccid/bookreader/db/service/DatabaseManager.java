package com.eaccid.bookreader.db.service;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import java.sql.SQLException;

public class DatabaseManager {

    private DatabaseHelper databaseHelper;

    private DatabaseManager(Context context) {
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static DatabaseManager getInstance(Context context) {
        return new DatabaseManager(context);
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
    }

    public BookDaoService getBookService() throws SQLException {
        return new BookDaoService(databaseHelper);
    }

    public WordDaoService getWordService() throws SQLException {
        return new WordDaoService(databaseHelper);
    }

}
