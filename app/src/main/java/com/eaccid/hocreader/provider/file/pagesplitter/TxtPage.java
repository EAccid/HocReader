package com.eaccid.hocreader.provider.file.pagesplitter;

public class TxtPage implements Page<String> {
    private String pageData;
    private int pageNumber;

    @Override
    public String getDataFromPage() {
        return pageData;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    void setPageData(String pageData) {
        this.pageData = pageData;
    }

    void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

}
