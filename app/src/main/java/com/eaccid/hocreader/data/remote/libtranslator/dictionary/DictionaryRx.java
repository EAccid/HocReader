package com.eaccid.hocreader.data.remote.libtranslator.dictionary;

import rx.Observable;

public interface DictionaryRx {

    Observable<Boolean> authorize(String login, String password);

    Observable<Boolean> isAuth();

    Observable<Boolean> addWord(String word, String textTranslation, String context);

}
