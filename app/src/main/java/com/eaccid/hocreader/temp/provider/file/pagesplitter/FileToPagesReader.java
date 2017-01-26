package com.eaccid.hocreader.temp.provider.file.pagesplitter;

import rx.Observable;

public interface FileToPagesReader {

    Observable<Page<String>> getPageObservable(BaseFile bf);

}
