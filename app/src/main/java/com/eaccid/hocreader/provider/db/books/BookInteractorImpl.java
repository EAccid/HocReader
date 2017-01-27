package com.eaccid.hocreader.provider.db.books;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.local.BookReaderMode;

import java.util.List;

public class BookInteractorImpl implements BookInteractor {

    private BookReaderMode dataManager;

    public BookInteractorImpl(AppDatabaseManager appDatabaseManager) {
        this.dataManager = appDatabaseManager;
    }

    @Override
    public void loadBooks(List<String> filePaths) {
        dataManager.updateBooks(filePaths);
    }

    @Override
    public String getCurrentBookPath() {
        return dataManager.getCurrentBook().getPath();
    }

    @Override
    public int getCurrentPage() {
        return dataManager.getCurrentPage();
    }

    @Override
    public void setCurrentPageForAddingWord(int pageNumber) {
        dataManager.setCurrentPageForAddingWord(pageNumber);
    }

    @Override
    public void createOrUpdateBook(String filePath, String fileName) {
        dataManager.createOrUpdateBook(filePath, fileName);
    }

    public void setCurrentBookForAddingWord(String filePath) {
        dataManager.setCurrentBookForAddingWord(filePath);
    }
}
