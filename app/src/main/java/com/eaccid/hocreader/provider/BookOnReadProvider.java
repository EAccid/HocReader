package com.eaccid.hocreader.provider;

import com.eaccid.hocreader.data.local.AppDatabaseManager;

public class BookOnReadProvider {

    public static AppDatabaseManager dataManager; //TODO inject

    public static void setDataManager(AppDatabaseManager dm) {
        dataManager = dm;
    }

}
