package com.eaccid.hocreader.presentation.main;

import android.os.Environment;

import java.io.File;

public class Storage {

    private final String ANDROID_STORAGE = "ANDROID_STORAGE";

    public File getExternalStorage() {
        return Environment.getExternalStorageDirectory();
    }

    public File getMountedStorage() {
        return new File(System.getenv(ANDROID_STORAGE));
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isMountedStorageReadable() {
        File storage = new File(System.getenv(ANDROID_STORAGE));
        return storage.exists();
    }
}
