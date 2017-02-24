package com.eaccid.hocreader.provider.file.pagesplitter;

import java.util.List;

import rx.Observable;

public interface FileToPagesReader<T> {
    Observable<List<Page<T>>> getPagesObservable(BaseFile bf);
}
