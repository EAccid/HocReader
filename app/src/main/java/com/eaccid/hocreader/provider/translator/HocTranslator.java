package com.eaccid.hocreader.provider.translator;

import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;

import rx.Observable;

public interface HocTranslator {
    Observable<TextTranslation> translate(String word);
}
