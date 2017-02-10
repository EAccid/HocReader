package com.eaccid.hocreader.presentation.main.ins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomDirectories {

    private final int CONSTANT_ID;
    private List<File> directories;

    public CustomDirectories(int fromId) {
        this.CONSTANT_ID = fromId;
        this.directories = new ArrayList<>();
    }

    public int addDirectory(File file) {
        int newId = directories.size();
        directories.add(file);
        return getCustomId(newId);
    }

    public int getSize() {
        return directories.size();
    }

    public String getName(int id) {
        return directories.get(getDirectoriesId(id)).getName();
    }

    public int getCustomId(int id) {
        return id + CONSTANT_ID;
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
}

