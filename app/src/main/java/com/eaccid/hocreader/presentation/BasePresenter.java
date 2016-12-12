package com.eaccid.hocreader.presentation;

public interface BasePresenter<V extends BaseView> {

    void attachView(V v);

    void detachView();
}
