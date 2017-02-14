package com.eaccid.hocreader.provider.translator;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.AuthParameters;

import rx.Observable;

public interface HocDictionary {
    Observable<Boolean> addTranslatedWord(TranslatedWord word);

    Observable<Boolean> authorize(String email, String password);

    Observable<AuthParameters> authorizeAndReturn(String email, String password);
}
