package com.eaccid.hocreader.provider.translator;

import rx.Observable;

public interface HocDictionary {
    Observable<Boolean> addTranslatedWord(TranslatedWord word);

    Observable<Boolean> authorize(String email, String password);
}
