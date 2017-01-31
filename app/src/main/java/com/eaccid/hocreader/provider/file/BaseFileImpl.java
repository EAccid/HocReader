package com.eaccid.hocreader.provider.file;

import com.eaccid.hocreader.provider.file.pagesplitter.BaseFile;
import com.eaccid.hocreader.provider.file.pagesplitter.CharsetName;

public class BaseFileImpl implements BaseFile {

    private final String filePath;

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
