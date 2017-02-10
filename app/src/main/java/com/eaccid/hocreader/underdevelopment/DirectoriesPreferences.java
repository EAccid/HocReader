package com.eaccid.hocreader.underdevelopment;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.ArraySet;

import com.eaccid.hocreader.injection.App;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DirectoriesPreferences {

    private SharedPreferences sp;
    private final String KEY_DIRECTORIES = "DIRECTORIES";
    private final String KEY_DEFAULT = "DEFAULT_DIRECTORY";
    private final int CONSTANT_ID;
    private List<File> directories = new ArrayList<>();
    private OnDirectoriesChangedListener listener;

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
        App.getAppComponent().inject(this);
    }

    public int addDirectory(File file) {
        int newId = directories.indexOf(file);
        if (newId >= 0)
            return getCustomId(newId);
        newId = directories.size();
        directories.add(file);
        storeDirectoriesOnDevice();
        return getCustomId(newId);
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

    private int getDirectoriesId(int customId) {
        return customId - CONSTANT_ID;
    }

    public CharSequence[] getFiles() {
        CharSequence[] list = new CharSequence[directories.size()];
        for (int i = 0; i < list.length; i++) {
            list[i] = directories.get(i).getPath();
        }
        return list;
    }

    public List<File> getFileList() {
        return directories;
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
        storeDirectoriesOnDevice();
    }

    public void setDefault(int id) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_DEFAULT, getFile(id).getPath());
        editor.apply();
    }

    @Nullable
    public File getDefault() {
        String path = sp.getString(KEY_DEFAULT, "");
        File file = new File(path);
        if (path.isEmpty() || !directories.contains(file))
            return null;
        return file;
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
        storeDirectoriesOnDevice();
        return files;
    }

    private Set<String> loadDirectoriesFromDevice() {
        return sp.getStringSet(KEY_DIRECTORIES, new ArraySet<>());
    }

    private void storeDirectoriesOnDevice() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(KEY_DIRECTORIES, getFilesSet());
        editor.apply();
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

}