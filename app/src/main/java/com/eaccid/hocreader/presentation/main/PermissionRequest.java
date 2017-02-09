package com.eaccid.hocreader.presentation.main;

import android.Manifest;

public class PermissionRequest {

    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    public static String getManifestPermission(int permission) {
        switch (permission) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                return Manifest.permission.READ_EXTERNAL_STORAGE;
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                return Manifest.permission.WRITE_EXTERNAL_STORAGE;
            default:
                throw new RuntimeException("There is no such permission.");
        }
    }

}
