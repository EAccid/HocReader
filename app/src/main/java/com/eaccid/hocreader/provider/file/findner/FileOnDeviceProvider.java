package com.eaccid.hocreader.provider.file.findner;

import com.eaccid.hocreader.presentation.main.ins.directories.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileOnDeviceProvider implements FileProvider {
    private List<File> fileList = new ArrayList<>();

    @Override
    public List<File> findFiles() {
        fillFileList(null);
        return fileList;
    }

    @Override
    public List<File> findFiles(File directory) {
        fillFileList(directory);
        return fileList;
    }

    private void fillFileList(File directory) {
        if (directory != null) {
            addFilesToList(directory, getFormatExtensions(FileExtensions.values()), "");
            return;
        }
        if (isExternalStorageReadable())
            addFilesToList(new Storage().getExternalStorage(), getFormatExtensions(FileExtensions.values()), "");
        if (isMountedStorageReadable())
            addFilesToList(new Storage().getMountedStorage(), getFormatExtensions(FileExtensions.values()), "");
    }

    private boolean isExternalStorageReadable() {
        return new Storage().isExternalStorageReadable();
    }

    private boolean isMountedStorageReadable() {
        return new Storage().isMountedStorageReadable();
    }

    private void addFilesToList(File dir, String fileExtensions, String filenameFilter) {
        if (dir == null || dir.listFiles() == null) return;
        for (File file : dir.listFiles())
            if (file.isDirectory()) {
                if (!file.getName().equalsIgnoreCase("Android"))
                    addFilesToList(file, fileExtensions, filenameFilter);
            } else {
                Pattern pattern = Pattern.compile(".*" + filenameFilter.toLowerCase() + fileExtensions + "");
                if (pattern.matcher(file.getName().toLowerCase()).matches())
                    fileList.add(file);
            }
    }

    /**
     * f.e. (txt|pdf)
     */
    private String getFormatExtensions(FileExtensions[] values) {
        StringBuilder sb = new StringBuilder();
        int size = values.length;
        sb.append("(");
        for (int i = 0; i < size; i++) {
            final String extension = values[i].getExtension();
            if (i != size - 1) {
                sb.append(extension).append("|");
            } else sb.append(extension);
        }
        sb.append(")");
        return sb.toString();
    }

}

