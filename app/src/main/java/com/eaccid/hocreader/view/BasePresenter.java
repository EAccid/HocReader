package com.eaccid.hocreader.view;

public interface BasePresenter<V extends BaseView> {

    void attachView(V v);

    void detachView();
}
