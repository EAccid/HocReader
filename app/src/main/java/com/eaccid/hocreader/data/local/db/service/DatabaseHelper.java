package com.eaccid.hocreader.data.local.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.eaccid.hocreader.data.local.db.entity.Book;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 5;
    private static final String databaseName = "hr.db";

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    DatabaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try
        {
            TableUtils.createTable(connectionSource, Word.class);
            TableUtils.createTable(connectionSource, Book.class);
            Log.e(TAG, "Database '" + databaseName + "' v." + DATABASE_VERSION + " has been created/updated.");
        }
        catch (SQLException e){
            Log.e(TAG, "Error creating database '" + databaseName + "' v." + DATABASE_VERSION);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try{
            //TODO delete dropping
            TableUtils.dropTable(connectionSource, Word.class, true);
            TableUtils.dropTable(connectionSource, Book.class, true);
            onCreate(database, connectionSource);
        }
        catch (SQLException e){
            Log.e(TAG,"Error upgrading database '" + databaseName + "' v." + oldVersion);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
        DaoManager.clearDaoCache();
    }
}
