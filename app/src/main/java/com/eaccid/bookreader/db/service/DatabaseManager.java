package com.eaccid.bookreader.db.service;


import android.content.Context;

import java.sql.SQLClientInfoException;
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

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public void releaseConnection() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public BookService getBookService() throws SQLException{
       return new BookService(databaseHelper);
    }

    public WordService getWordService() throws  SQLException{
        return new WordService(databaseHelper);
    }

}
