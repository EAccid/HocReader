package com.eaccid.hocreader.provider.file.findner;

import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import java.io.File;

public enum FileExtensions {
    TXT, PDF;

    public String getExtension() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return name();
    }

    @Nullable
    public static FileExtensions getFileExtension(File file) {
        String ext1 = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        int lastDot = file.getName().lastIndexOf('.');
        String ext2 = "";
        if (lastDot != -1)
            ext2 = file.getName().substring(lastDot + 1, file.getName().length());
        if (isMatchedExtension(FileExtensions.TXT, ext1, ext2))
            return FileExtensions.TXT;
        if (ext1.equalsIgnoreCase(FileExtensions.PDF.name()) || ext2.equalsIgnoreCase(FileExtensions.PDF.name()))
            return FileExtensions.PDF;
        return null;
    }

    private static boolean isMatchedExtension(FileExtensions extension, String... exts) {
        boolean isMatchExtension = false;
        for (String ext : exts
                ) {
            if (ext.equalsIgnoreCase(extension.name())) {
                isMatchExtension = true;
                break;
            }
        }
        return isMatchExtension;
    }

}
