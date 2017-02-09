package com.eaccid.hocreader.presentation.main.ins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomDirectories {

    private String parentDir;
    private List<Directory> directories;

    public CustomDirectories() {
        directories = new ArrayList<>();
        parentDir = ".../";
    }

    public void addDirectory(File file) {
        directories.add(new Directory(parentDir + file.getName(), file));
    }

    public int getSize() {
        return directories.size();
    }

    public File getFile(int id) {
        return directories.get(id).getFile();
    }

    public void setParentDir(String parentDir) {
        this.parentDir = parentDir;
    }

    public String getName(int id) {
        return directories.get(id).getName();
    }

    private class Directory {
        String name;
        File file;

        public Directory(String name, File file) {
            this.name = name;
            this.file = file;
        }

        public File getFile() {
            return file;
        }

        public String getName() {
            return name;
        }
    }

}

