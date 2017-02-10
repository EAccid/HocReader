package com.eaccid.hocreader.presentation.main.ins.directories;

import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;

public class Storage {

    private final String ANDROID_STORAGE = "ANDROID_STORAGE";

    public File getExternalStorage() {
        return Environment.getExternalStorageDirectory();
    }

    public File getMountedStorage() {
        return readStorage();
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isMountedStorageReadable() {
        return readStorage() != null;
    }

    //todo delete: temp solutions
    @Nullable
    private File readStorage() {
        File dir = new File(System.getenv(ANDROID_STORAGE));
        if (dir.listFiles() == null) return null;
        for (File file : dir.listFiles())
            if (file.isDirectory()
                    && file.list() != null
                    && file.list().length != 0) {
                return file;
            }
        return null;
    }
}
