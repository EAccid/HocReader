package com.eaccid.hocreader.underdevelopment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BookFileViaIntent {

    private String fileName;
    private String filePath;

    public boolean readFile(Activity context) {
        Intent intent = context.getIntent();
        Uri uri = intent.getData();
        if (uri == null) {
            return readFromExtras(intent);
        }
        return readFromData(context);
    }

    private boolean readFromExtras(Intent intent) {
        try {
            setFilePath(intent.getStringExtra("filePath"));
            setFileName(intent.getStringExtra("fileName"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean readFromData(Activity context) {
        Uri uri = context.getIntent().getData();
        File outputFile;
        try {
            outputFile = File.createTempFile("temp_book_", ".txt", context.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            outputStream = new FileOutputStream(outputFile);
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setFilePath(outputFile.getPath());
        setFileName(outputFile.getName());
        return true;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
