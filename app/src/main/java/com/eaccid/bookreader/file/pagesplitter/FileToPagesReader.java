package com.eaccid.bookreader.file.pagesplitter;

import rx.Observable;

public interface FileToPagesReader {

    Observable<Page<String>> getPageObservable(BaseFile bf);

}
