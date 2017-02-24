package com.eaccid.hocreader.provider.translator;

import com.eaccid.hocreader.data.remote.TranslatorFactory;
import com.eaccid.hocreader.data.remote.Translators;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;

import rx.Observable;

public class HocTranslatorProvider implements HocTranslator {

    private Translators availableTranslator;

    @Override
    public Observable<TextTranslation> translate(String word) {
        Translators translator = getAvailableTranslator();
        return TranslatorFactory.newTranslator(translator).translate(word);
    }

    private Translators getAvailableTranslator() {
        //TODO refactor after adding to Livio Dictionary
        return Translators.LINGUALEO_ONLINE;
    }
}
