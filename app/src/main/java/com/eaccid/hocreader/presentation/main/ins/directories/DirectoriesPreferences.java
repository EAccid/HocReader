package com.eaccid.hocreader.presentation.main.ins.directories;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.ArraySet;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DirectoriesPreferences {

    private final String LOG_TAG = "DirectoriesPreferences";
    private final String KEY_DIRECTORIES = "DIRECTORIES";
    private final String KEY_DEFAULT = "DEFAULT_DIRECTORY";
    private final int CONSTANT_ID;
    private List<File> directories = new ArrayList<>();
    private OnDirectoriesChangedListener listener;
    private SharedPreferences sp;

    public interface OnDirectoriesChangedListener {
        void onDirectoryDeleted(int id);
    }

    public void setOnDirectoriesChangedListener(OnDirectoriesChangedListener listener) {
        this.listener = listener;
    }

    public DirectoriesPreferences(SharedPreferences sp, int idFromIndex) {
        this.sp = sp;
        this.CONSTANT_ID = idFromIndex;
        this.directories = restoreDirectoriesFromFilesSet();
    }

    public int addDirectory(File file) {
        int newId = directories.indexOf(file);
        if (newId >= 0)
            return getCustomId(newId);
        newId = directories.size();
        directories.add(file);
        onDirectoryAdded();
        return getCustomId(newId);
    }

    public void deleteByPath(String path) {
        File file = new File(path);
        if (!file.exists())
            return;
        int id = directories.indexOf(file);
        if (id < 0)
            return;
        directories.remove(id);
        if (listener != null)
            listener.onDirectoryDeleted(getCustomId(id));
        onDirectoryDeleted();
    }

    public List<File> getFileList() {
        return directories;
    }

    public int getSize() {
        return directories.size();
    }

    public String getName(int id) {
        return getShortName(directories.get(getDirectoriesId(id)));
    }

    public int getId(File file) {
        return getCustomId(directories.indexOf(file));
    }

    public File getFile(int id) {
        return directories.get(getDirectoriesId(id));
    }

    public boolean hasId(int id) {
        return getDirectoriesId(id) < directories.size();
    }

    public CharSequence[] getFiles() {
        CharSequence[] list = new CharSequence[directories.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = directories.get(i).getPath();
        }
        return list;
    }

    public void setDefaultFile(int id) {
        storeDefaultOnDevice(id);
    }

    @Nullable
    public File getDefaultFile() {
        return loadDefaultFromDevice();
    }

    private void onDirectoryDeleted() {
        storeDirectoriesOnDevice();
    }

    private void onDirectoryAdded() {
        storeDirectoriesOnDevice();
    }

    private int getDirectoriesId(int customId) {
        return customId - CONSTANT_ID;
    }

    private int getCustomId(int id) {
        return id + CONSTANT_ID;
    }

    private Set<String> getFilesSet() {
        Set<String> list = new ArraySet<>();
        for (File file : directories) {
            list.add(file.getPath());
        }
        return list;
    }

    private List<File> restoreDirectoriesFromFilesSet() {
        List<File> files = new ArrayList<>();
        for (String path : loadDirectoriesFromDevice()) {
            File file = new File(path);
            if (file.exists()) {
                files.add(file);
            }
        }
        Log.i(LOG_TAG, "restoreDirectoriesFromFilesSet");
        return files;
    }

    private String getShortName(File file) {
        String result;
        try {
            String onlyPath = file.getPath().substring(0, file.getPath().lastIndexOf(File.separatorChar));
            result = "..."
                    + onlyPath.substring(onlyPath.lastIndexOf(File.separatorChar), onlyPath.length())
                    + "/"
                    + file.getName();
        } catch (Exception e) {
            result = file.getName();
        }
        return result;
    }

    private void storeDirectoriesOnDevice() {
        SharedPreferences.Editor editor = sp.edit();
        Set<String> set = getFilesSet();
        editor.putStringSet(KEY_DIRECTORIES, set);
        Log.i(LOG_TAG, "Storing directories on device: " + set);
        editor.apply();

    }

    private Set<String> loadDirectoriesFromDevice() {
        Set<String> set = sp.getStringSet(KEY_DIRECTORIES, new ArraySet<>());
        Log.i(LOG_TAG, "Loading directories from device:" + set);
        return set;
    }

    private void storeDefaultOnDevice(int id) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_DEFAULT, getFile(id).getPath());
        editor.apply();
    }

    private File loadDefaultFromDevice() {
        String path = sp.getString(KEY_DEFAULT, "");
        File file = new File(path);
        if (path.isEmpty() || !directories.contains(file))
            return null;
        return file;
    }

}