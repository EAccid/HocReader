package com.eaccid.bookreader.file;

import com.eaccid.bookreader.file.pagesplitter.BaseFile;
import com.eaccid.bookreader.file.pagesplitter.CharsetName;

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
