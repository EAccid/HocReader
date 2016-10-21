package com.eaccid.bookreader.file;


import android.os.Environment;

import com.eaccid.bookreader.file.FileExtensions;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FileOnDeviceFinder {

    private ArrayList<File> fileList = new ArrayList<>();

    public ArrayList<File> findFiles() {
        fillFileList();
        return fileList;
    }

//    public FileExtensions[] getFileExtensions() {
//        return FileExtensions.values();
//    }

    private void fillFileList() {
        if (isExternalStorageReadable())
            addFilesToList(Environment.getExternalStorageDirectory(), getFormatExtensions(FileExtensions.values()), "");
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void addFilesToList(File dir, String fileExtensions, String filenameFilter) {

        if (dir == null || dir.listFiles() == null) return;

        System.out.println(dir);
        for (File file : dir.listFiles())
            if (file.isDirectory()) {

                //TODO set user directory names or none. temp condition
                String dirName = file.getName();
                if (dirName.equalsIgnoreCase("downloads") ||
                        dirName.equalsIgnoreCase("books") ||
                        dirName.equalsIgnoreCase("download") ||
                        dirName.equalsIgnoreCase("book")) {
                    addFilesToList(file, fileExtensions, filenameFilter);
                }

            } else {
                Pattern pattern = Pattern.compile(".*" + filenameFilter.toLowerCase() + fileExtensions + "");
                if (pattern.matcher(file.getName().toLowerCase()).matches())
                    fileList.add(file);
            }

    }

    /**
     * f.e. (txt|pdf)
     */
    private static String getFormatExtensions(FileExtensions[] values) {
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

