package com.eaccid.hocreader.presentation.activity.main;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.file.findner.FileOnDeviceFinder;
import com.eaccid.bookreader.searchfiles.ItemObjectChild;
import com.eaccid.bookreader.searchfiles.ItemObjectGroup;
import com.eaccid.bookreader.underdev.settings.MainSettings;
import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.presentation.BasePresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements BasePresenter<MainActivity> {

    private final String logTAG = "MainPresenter";
    private MainActivity mView;
    private AppDatabaseManager dataManager; //TODO inject

    public MainPresenter() {
        dataManager = new AppDatabaseManager();
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

    public void fillExpandableListView() {
        FileOnDeviceFinder fileOnDeviceFinder = new FileOnDeviceFinder();
        List<File> foundFiles = fileOnDeviceFinder.findFiles();
        loadFilesToExpandableView(foundFiles);
    }

    public void loadFilesToExpandableView(List<File> files) {

        //TODO: drop on methods

        List<ItemObjectGroup> itemObjectGroupList = new ArrayList<>();
        List<String> readableFiles = new ArrayList<>();

        List<ItemObjectChild> childObjectItemTXT = new ArrayList<>();
        List<ItemObjectChild> childObjectItemPDF = new ArrayList<>();

        for (File file : files) {
            String ext1 = MimeTypeMap.getFileExtensionFromUrl(file.getName());
            int lastDot = file.getName().lastIndexOf('.');
            String ext2 = "";
            if (lastDot != -1)
                ext2 = file.getName().substring(lastDot + 1, file.getName().length());
            if (ext1.equalsIgnoreCase("txt") || ext2.equalsIgnoreCase("txt")) {
                childObjectItemTXT.add(new ItemObjectChild(R.mipmap.generic_icon, file.getName(), file));
                readableFiles.add(file.getPath());
            } else {
                childObjectItemPDF.add(new ItemObjectChild(R.mipmap.generic_icon, file.getName(), file));
            }
        }

        ItemObjectGroup itemObjectGroupTXT = new ItemObjectGroup("TXT", childObjectItemTXT);
        itemObjectGroupList.add(itemObjectGroupTXT);
        ItemObjectGroup itemObjectGroupPDF = new ItemObjectGroup("PDF", childObjectItemPDF);
        itemObjectGroupList.add(itemObjectGroupPDF);

        mView.setItemsToExpandableListView(itemObjectGroupList);
        dataManager.refreshBooks(readableFiles);
    }


    /**
     * TODO in main presenter
     * - settings into separate presenter
     * - create fab action
     */
    private MainSettings settings;

    public void loadSettings() {
        settings = new MainSettings(mView);
        settings.setDefaultSettings();
    }

    public void clearBookSearchHistory() {
        settings.clearBookSearchHistory();
    }

    public void onFabButtonClickListener() {
        //TEMP
        int words = dataManager.getAllWords(null, null).size();
        int books = dataManager.getAllBooks().size();
        String text = "books: " + books + ", words: " + words;
        mView.showTestFab(text);
    }

}
