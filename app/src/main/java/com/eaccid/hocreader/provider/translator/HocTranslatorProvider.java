package com.eaccid.hocreader.provider.translator;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.translator.LingualeoTranslatorRx;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TranslatorRx;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;

import rx.Observable;

public class HocTranslatorProvider implements TranslatorRx {

    @Override
    public Observable<TextTranslation> translate(String word) {
        return new LingualeoTranslatorRx().translate(word);
    }
}
