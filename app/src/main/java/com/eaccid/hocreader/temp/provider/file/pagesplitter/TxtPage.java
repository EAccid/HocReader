package com.eaccid.hocreader.temp.provider.file.pagesplitter;

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

    public void setPageData(String pageData) {
        this.pageData = pageData;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
