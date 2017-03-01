package com.eaccid.hocreader.provider.db.words;

import android.content.Context;
import android.content.SharedPreferences;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.App;

import javax.inject.Inject;

public class BookOnReadProvider {

    private final String CURRENT_PAGE = "current_page_";
    @Inject
    SharedPreferences sp;
    @Inject
    AppDatabaseManager databaseManager;

    public BookOnReadProvider(Context context) {
        App.get(context).getAppComponent().inject(this);
    }

    public void storeCurrentBooksPage(int page) {
        storeCurrentBooksPageOnDevice(page);
    }

    public int loadCurrentBooksPage() {
        return loadSavedBooksPageFromDevice();
    }

    private void storeCurrentBooksPageOnDevice(int page) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(CURRENT_PAGE + databaseManager.getCurrentBook().getPath(), page);
        editor.apply();
    }

    private int loadSavedBooksPageFromDevice() {
        return sp.getInt(CURRENT_PAGE + databaseManager.getCurrentBook().getPath(), 0);
    }

}
