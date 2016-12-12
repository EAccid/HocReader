package com.eaccid.hocreader.provider.file.pagesplitter;

import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedReaderHandler {

    private BufferedReader bufferedReader;
    private boolean eof;
    private BaseFile baseFile;

    public BufferedReaderHandler(BaseFile baseFile) {
        this.baseFile = baseFile;
    }

    @Nullable
    public BufferedReader openBufferedReader() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(baseFile.getFilePath()), baseFile.getCharsetName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedReader;
    }

    public void closeBufferedReader() {

        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String readLineBufferedReader() {
        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setEof(line == null);
        return line == null ? "" : line;
    }

    public boolean isEof() {
        return eof;
    }

    public void setEof(boolean eof) {
        this.eof = eof;
    }
}
