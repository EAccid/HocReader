package com.eaccid.hocreader.presentation.main;

import com.eaccid.hocreader.presentation.BaseView;

import java.util.List;

public interface MainView<T> extends BaseView {
    void setBooksData(List<T> itemGroupList);

    void provideBooksSearching(String searchText);
}
