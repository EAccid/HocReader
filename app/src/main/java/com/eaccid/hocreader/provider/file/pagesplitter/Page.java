package com.eaccid.hocreader.provider.file.pagesplitter;

public interface Page<T> {

    T getDataFromPage();

    int getPageNumber();
}
