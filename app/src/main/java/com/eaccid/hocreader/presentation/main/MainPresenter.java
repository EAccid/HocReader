package com.eaccid.hocreader.presentation.main;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.main.ins.CustomDirectories;
import com.eaccid.hocreader.presentation.main.ins.IconsProvider;
import com.eaccid.hocreader.presentation.main.ins.PermissionRequest;
import com.eaccid.hocreader.presentation.main.serchadapter.ItemGroupImpl;
import com.eaccid.hocreader.presentation.main.serchadapter.ItemObjectChild;
import com.eaccid.hocreader.presentation.main.serchadapter.ItemChild;
import com.eaccid.hocreader.presentation.main.serchadapter.ItemGroup;
import com.eaccid.hocreader.provider.db.books.BookInteractor;
import com.eaccid.hocreader.provider.file.findner.FileExtensions;
import com.eaccid.hocreader.provider.file.findner.FileOnDeviceProvider;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.provider.file.findner.FileProvider;
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainPresenter implements BasePresenter<MainActivity> {

    private final String logTAG = "MainPresenter";
    private MainActivity mView;
    private CustomDirectories directories;

    @Inject
    BookInteractor bookInteractor;

    public MainPresenter() {
        directories = new CustomDirectories(100);
        App.getAppComponent().inject(this);
    }

    @Override
    public void attachView(MainActivity mainActivity) {
        mView = mainActivity;
        checkReadExtStoragePermission();
        PreferenceManager.setDefaultValues(mView.getApplicationContext(), R.xml.preferences, false);
        Log.i(logTAG, "MainActivity has been attached.");
    }

    @Override
    public void detachView() {
        Log.i(logTAG, "MainActivity has been detached.");
        mView = null;
    }

    /***
     * on 'menu item clicked' actions handlers
     */

    public void onFabClicked() {
        mView.navigateToTraining();
    }

    public void onSettingsMenuSelected() {
        mView.navigateToSettings();
    }

    public void onAllDirectoryMenuSelected() {
        fillExpandableListView();
    }

    /**
     * todo make asynchronous
     * mView.showProgressDialog();
     * mView.dismissProgressDialog();
     */

    private void fillExpandableListView() {
        loadFilesToExpandableView(
                new FileOnDeviceProvider().findFiles()
        );
    }

    private void fillExpandableListView(File file) {
        loadFilesToExpandableView(
                new FileOnDeviceProvider().findFiles(file)
        );
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

    /***
     * check permission
     */

    public void readExternalStorageGranted() {
        fillExpandableListView();
    }

    public void requestPermission(int permission) {
        ActivityCompat.requestPermissions(mView,
                new String[]{PermissionRequest.getManifestPermission(permission)},
                permission);
    }

    private void checkReadExtStoragePermission() {
        int checkedPermission = PermissionRequest.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
        if (checkPermission(checkedPermission) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(checkedPermission)) {
                mView.showPermissionExplanation(
                        "Storage permission is needed to show books",
                        checkedPermission
                );
                return;
            }
            requestPermission(checkedPermission);
        }
        fillExpandableListView();
    }

    private boolean shouldShowRequestPermissionRationale(int permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(
                mView, PermissionRequest.getManifestPermission(permission));
    }

    private int checkPermission(int permission) {
        return ContextCompat.checkSelfPermission(
                mView,
                PermissionRequest.getManifestPermission(permission)
        );
    }

    /***
     * custom directories menu
     */

    public void onDirectoryChosen() {
        new DirectoryChooser().startOnResultDirectoryChooser(mView);
    }

    public void OnDirectoryChooserResult(ArrayList<String> paths) {
        for (String path : paths) {
            Uri uri = Uri.parse(path);
            File file = Utils.getFileForUri(uri);
            int newId = directories.addDirectory(file);
            mView.addCustomMenuItem(newId, directories.getName(newId));
        }
    }

    public boolean onNavigationItemSelected(int id) {
        if (directories.hasId(id)) {
            File file = directories.getFile(id);
            fillExpandableListView(file);
            return true;
        }
        return false;
    }

}
