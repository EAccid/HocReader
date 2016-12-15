package com.eaccid.hocreader.provider.db;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.local.db.entity.Word;


public class WordProvider {

    private Word word;

    //TODO inject
    private AppDatabaseManager dataManager;

    public WordProvider() {

    }

    public boolean isWordFetched() {
        return word != null;
    }

}
