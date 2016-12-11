package com.eaccid.bookreader.activity.main;

import android.util.Log;

import com.eaccid.bookreader.file.findner.FileOnDeviceFinder;
import com.eaccid.bookreader.underdev.settings.MainSettings;
import com.eaccid.hocreader.data.local.BookManager;
import com.eaccid.hocreader.data.local.WordManager;
import com.eaccid.hocreader.view.BasePresenter;

import java.io.File;
import java.util.List;

public class MainPresenter implements BasePresenter<MainActivity> {

    private MainActivity mView;
    private MainSettings settings;
    private final String logTAG = "MainPresenter";
    private BookManager dataManager;

    public MainPresenter() {
        dataManager = new BookManager();
    }

    @Override
    public void attachView(MainActivity mainActivity) {
        mView = mainActivity;
        dataManager.loadDatabaseManager(mView);
        Log.i(logTAG, mView.getLocalClassName() + "' has been attached.");
    }

    @Override
    public void detachView() {
        dataManager.releaseDatabaseManager();
        Log.i(logTAG, mView.getLocalClassName() + "' has been detached.");
        mView = null;
    }

    public void onFabButtonClickListener() {
        //TEMP
        WordManager wordManager = new WordManager();
        wordManager.loadDatabaseManager(mView);
        int words = wordManager.getAllWords(null, null).size();
        wordManager.releaseDatabaseManager();
        int books = dataManager.getAllBooks().size();
        String text = "books: " + books + ", words: " + words;
        mView.showTempDataFromDB(text);
    }

    public void loadFiles() {
        FileOnDeviceFinder fileOnDeviceFinder = new FileOnDeviceFinder();
        List<File> foundFiles = fileOnDeviceFinder.findFiles();
        List<String> fileNames = mView.fillExpandableListView(foundFiles);
        dataManager.refreshBooks(fileNames);
    }

    public void loadSettings() {
        settings = new MainSettings(mView);
        settings.setDefaultSettings();
    }

    public void clearBookSearchHistory() {
        settings.clearBookSearchHistory();
    }

}
