package com.eaccid.hocreader.data.remote;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.deprecated.LingualeoTranslator;
import com.eaccid.hocreader.data.remote.libtranslator.translator.Translator;

public class TranslatorFactory {
    private TranslatorFactory() {
    }

    public static Translator newTranslator(Translators tr) {

        switch (tr) {
            case LINGUALEO_ONLINE:
                return new LingualeoTranslator();
//            case LIVIO_TRANSLATOR:
            default:
                throw new RuntimeException("smth went wrong!");
        }

    }
}