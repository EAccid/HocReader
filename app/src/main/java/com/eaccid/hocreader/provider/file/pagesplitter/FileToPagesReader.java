package com.eaccid.hocreader.provider.file.pagesplitter;

import rx.Observable;

public interface FileToPagesReader {

    Observable<Page<String>> getPageObservable(BaseFile bf);

}
