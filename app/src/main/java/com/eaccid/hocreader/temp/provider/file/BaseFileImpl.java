package com.eaccid.hocreader.temp.provider.file;

import com.eaccid.hocreader.temp.provider.file.pagesplitter.BaseFile;
import com.eaccid.hocreader.temp.provider.file.pagesplitter.CharsetName;

public class BaseFileImpl implements BaseFile {

    private String filePath;

    public BaseFileImpl(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public String getCharsetName() {
        return CharsetName.getCharset();
    }
}
