package com.eaccid.bookreader.file.pagesplitter;

public interface Page<T> {

    T getDataFromPage();

    int getPageNumber();
}
