package com.eaccid.hocreader.presentation.main.ins;

import android.content.SharedPreferences;
import android.util.ArraySet;

import com.eaccid.hocreader.injection.App;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DirectoriesPreferences {

    private SharedPreferences sp;
    private final String KEY = "DIRECTORIES";
    private final int CONSTANT_ID;
    private List<File> directories = new ArrayList<>();

    public DirectoriesPreferences(SharedPreferences sp, int fromId) {
        this.sp = sp;
        this.CONSTANT_ID = fromId;
        this.directories = restoreDirectoriesFromFilesSet();
        App.getAppComponent().inject(this);
    }

    public int addDirectory(File file) {
        int newId = directories.size();
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

    public int getCustomId(int id) {
        return id + CONSTANT_ID;
    }

    public int getCustomId(File file) {
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

    public Set<String> getFilesSet() {
        Set<String> list = new ArraySet<>();
        for (File file : directories) {
            list.add(file.getPath());
        }
        return list;
    }

    public List<File> getFileList() {
        return directories;
    }

    public List<File> restoreDirectoriesFromFilesSet() {
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

    private void storeDirectoriesOnDevice() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(KEY, getFilesSet());
        editor.apply();
    }

    private Set<String> loadDirectoriesFromDevice() {
        return sp.getStringSet(KEY, new ArraySet<>());
    }

    private String getShortName(File file) {
        String onlyPath = file.getPath().substring(0, file.getPath().lastIndexOf(File.separatorChar));
        return "..."
                + onlyPath.substring(onlyPath.lastIndexOf(File.separatorChar), onlyPath.length())
                + "/"
                + file.getName();
    }

}