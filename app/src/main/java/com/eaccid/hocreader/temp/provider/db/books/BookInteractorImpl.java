package com.eaccid.hocreader.temp.provider.db.books;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import java.util.List;

public class BookInteractorImpl implements BookInteractor {

    private AppDatabaseManager dataManager;

    public BookInteractorImpl(AppDatabaseManager appDatabaseManager) {
        this.dataManager = appDatabaseManager;
    }

    @Override
    public void loadBooks(List<String> filePaths) {
        dataManager.refreshBooks(filePaths);
    }

}
