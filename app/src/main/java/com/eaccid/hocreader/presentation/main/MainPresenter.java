package com.eaccid.hocreader.presentation.main;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Size;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.eaccid.hocreader.App;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.main.ins.directories.DirectoriesPreferences;
import com.eaccid.hocreader.presentation.main.ins.directories.DirectoryChooser;
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
import com.nononsenseapps.filepicker.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainPresenter implements BasePresenter<MainActivity> {

    private final String LOG_TAG = "MainPresenter";
    private MainActivity mView;

    @Inject
    DirectoriesPreferences directories;
    @Inject
    BookInteractor bookInteractor;

    public MainPresenter() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void attachView(MainActivity mainActivity) {
        mView = mainActivity;
        Log.i(LOG_TAG, "MainActivity has been attached.");
        PreferenceManager.setDefaultValues(mView.getApplicationContext(), R.xml.preferences, false);
        directories.setOnDirectoriesChangedListener(id -> {
            loadCustomMenu();
        });
        checkReadExtStoragePermission();
    }

    @Override
    public void detachView() {
        Log.i(LOG_TAG, "MainActivity has been detached.");
        mView = null;
    }

    /***
     * on 'menu item clicked' actions handlers
     */

    public void onFab1Clicked() {
        mView.navigateToTraining();
    }

    public void onFab2Clicked() {
        mView.navigateToEditing();
    }

    public void onSettingsMenuSelected() {
        mView.navigateToSettings();
    }

    public void onAllDirectoryMenuSelected() {
        loadCustomMenu();
        fillBooks(true);
    }

    public void onAboutSelected() {
        mView.navigateToAbout();
    }

    public void onCloseSearchView() {
        fillBooks(false);
    }

    /***
     * check permission
     */

    public void readExternalStorageGranted() {
        fillBooks(true);
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
        onPermissionChecked();
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

    private void onPermissionChecked() {
        loadCustomMenu();
        fillBooks(false);
    }

    private void fillBooks(boolean showAll) {
        new LoadingList().execute(showAll);
        loadCustomMenu();
        if (!showAll)
            setCheckedDirectory();

    }

    private void setCheckedDirectory() {
        File file = directories.getDefaultFile();
        if (file != null) {
            mView.setCheckedMenuItem(directories.getId(file));
        }
    }

    /***
     * custom directories menu
     */

    public void onDirectoryChosen() {
        if (checkPermission(PermissionRequest.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_DENIED)
            new DirectoryChooser().startOnResultDirectoryChooser(mView);
    }

    public void OnDirectoryChooserResult(List<String> paths) {
        for (String path : paths) {
            Uri uri = Uri.parse(path);
            File file = Utils.getFileForUri(uri);
            int newId = directories.addDirectory(file);
            mView.addCustomMenuItem(newId, directories.getName(newId));
        }
    }

    public boolean onNavigationItemSelected(int id) {
        if (directories.hasId(id)) {
            directories.setDefaultFile(id);
            fillBooks(false);
            return true;
        }
        return false;
    }

    private void loadCustomMenu() {
        mView.resetCustomMenu();
        for (File dir :
                directories.getFileList()) {
            int id = directories.getId(dir);
            mView.addCustomMenuItem(id, directories.getName(id));
        }
    }

    private class LoadingList extends AsyncTask<Boolean, Void, List<ItemGroup>> {

        @Override
        protected void onPreExecute() {
            mView.showProgressDialog();
        }

        @Override
        protected List<ItemGroup> doInBackground(@Size(min = 1) Boolean... fillByAll) {
            File file = directories.getDefaultFile();
            if (fillByAll[0] || file == null)
                return fillExpandableListView();
            return fillExpandableListView(file);
        }

        @Override
        protected void onPostExecute(List<ItemGroup> result) {
            mView.dismissProgressDialog();
            mView.setBooksData(result);
        }

        private List<ItemGroup> fillExpandableListView() {
            return loadFilesToExpandableView(
                    new FileOnDeviceProvider().findFiles()
            );
        }

        private List<ItemGroup> fillExpandableListView(File file) {
            return loadFilesToExpandableView(
                    new FileOnDeviceProvider().findFiles(file)
            );
        }

        private List<ItemGroup> loadFilesToExpandableView(List<File> files) {
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
            bookInteractor.loadBooks(readableFiles);
            return itemGroupList;
        }

    }

}
