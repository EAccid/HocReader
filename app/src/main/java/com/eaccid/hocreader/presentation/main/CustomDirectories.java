package com.eaccid.hocreader.presentation.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomDirectories {

    private List<File> files;

    public CustomDirectories() {
        files = new ArrayList<>();
    }

    public void addDirectory(File file) {
        files.add(file);
    }


    public int getSize() {
        return files.size();
    }

    public File getFile(int id) {
        return files.get(id);
    }

}

