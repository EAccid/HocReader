package com.eaccid.hocreader.provider.translator;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoDictionaryRx;
import com.eaccid.hocreader.temp.underdevelopment.TranslatedWord;

import rx.Observable;

public class HocDictionaryProvider {

    public Observable<Boolean> addTranslatedWord(TranslatedWord word) {
        return new LingualeoDictionaryRx()
                .addWord(word.getWordFromContext(), word.getTranslation(), word.getContext());
    }

    public Observable<Boolean> authorize(String email, String password) {
        return new LingualeoDictionaryRx().authorize(email, password);
    }
}


