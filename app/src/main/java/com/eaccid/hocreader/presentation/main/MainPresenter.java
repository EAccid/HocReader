package com.eaccid.hocreader.presentation.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.main.serchadapter.ItemGroupImpl;
import com.eaccid.hocreader.presentation.main.serchadapter.ItemObjectChild;
import com.eaccid.hocreader.presentation.main.serchadapter.ItemChild;
import com.eaccid.hocreader.presentation.main.serchadapter.ItemGroup;
import com.eaccid.hocreader.presentation.training.TrainingActivity;
import com.eaccid.hocreader.provider.db.books.BookInteractor;
import com.eaccid.hocreader.provider.file.findner.FileExtensions;
import com.eaccid.hocreader.provider.file.findner.FileOnDeviceProvider;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.provider.file.findner.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainPresenter implements BasePresenter<MainActivity> {

    private final String logTAG = "MainPresenter";
    private MainActivity mView;

    @Inject
    BookInteractor bookInteractor;

    public MainPresenter() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void attachView(MainActivity mainActivity) {
        mView = mainActivity;
        fillExpandableListView();
        PreferenceManager.setDefaultValues(mView.getApplicationContext(), R.xml.preferences, false);
        Log.i(logTAG, "MainActivity has been attached.");
    }

    @Override
    public void detachView() {
        Log.i(logTAG, "MainActivity has been detached.");
        mView = null;
    }

    public void onFabClicked() {
        Intent intent = new Intent(mView.getApplicationContext(), TrainingActivity.class);
        mView.startActivity(intent);
    }

    private void fillExpandableListView() {
        mView.showProgressDialog();
        FileProvider fileProvider = new FileOnDeviceProvider();
        List<File> foundFiles = fileProvider.findFiles();
        loadFilesToExpandableView(foundFiles);
        mView.dismissProgressDialog();
    }

    private void loadFilesToExpandableView(List<File> files) {
        List<ItemGroup> itemGroupList = new ArrayList<>();
        List<String> readableFiles = new ArrayList<>();
        List<ItemChild> childObjectItemTXT = new ArrayList<>();
        List<ItemChild> childObjectItemPDF = new ArrayList<>();
        for (File file : files) {
            FileExtensions extension = FileExtensions.getFileExtension(file);
            if (extension == FileExtensions.PDF) {
                childObjectItemPDF.add(
                        new ItemObjectChild(
                                new IconsProvider().getFileExtensionsIconResId(extension), file.getName(), file)
                );
                readableFiles.add(file.getPath());
            }
            if (extension == FileExtensions.TXT) {
                childObjectItemTXT.add(new ItemObjectChild(
                        new IconsProvider().getFileExtensionsIconResId(extension), file.getName(), file)
                );
                readableFiles.add(file.getPath());
            }
        }
        ItemGroup itemGroupTXT = new ItemGroupImpl(FileExtensions.TXT.name().toUpperCase(), childObjectItemTXT);
        itemGroupList.add(itemGroupTXT);
        ItemGroup itemGroupPDF = new ItemGroupImpl(FileExtensions.PDF.name().toUpperCase(), childObjectItemPDF);
        itemGroupList.add(itemGroupPDF);
        mView.setBooksData(itemGroupList);
        bookInteractor.loadBooks(readableFiles);
    }


}
