package com.eaccid.hocreader.presentation.activity.main.serchadapter;

import java.io.File;

public class ItemObjectChild {
    private int icon;
    private String name;
    private File file;

    public ItemObjectChild(int icon, String name, File file) {
        this.icon = icon;
        this.name = name;
        this.file = file;
    }

    public int getIcon() {
        return icon;
    }

    public String getText() {
        return name;
    }

    public File getFile() {
        return file;
    }

}
