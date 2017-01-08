package com.eaccid.hocreader.provider.translator;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.translator.LingualeoTranslatorRx;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;

import rx.Observable;

public class HocTranslatorProvider {

    public Observable<TextTranslation> translate(final WordFromText wordFromText) {
        return new LingualeoTranslatorRx().translate(wordFromText.getText());
    }

}
