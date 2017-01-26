package com.eaccid.hocreader.temp.provider.file.pagesplitter;

public interface Page<T> {

    T getDataFromPage();

    int getPageNumber();
}
