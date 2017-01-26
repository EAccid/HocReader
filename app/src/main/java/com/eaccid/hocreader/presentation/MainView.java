package com.eaccid.hocreader.presentation;

import java.util.List;

public interface MainView<T> extends BaseView{
    void setBooksData(List<T> itemGroupList);

    void provideBooksSearching(String searchText);
}
