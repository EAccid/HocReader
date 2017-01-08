package com.eaccid.hocreader.data.remote.libtranslator.translator;
import rx.Observable;

public interface TranslatorRx {
    Observable<TextTranslation> translate(String word);
}
