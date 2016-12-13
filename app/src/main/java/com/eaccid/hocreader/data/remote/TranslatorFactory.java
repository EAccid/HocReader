package com.eaccid.hocreader.data.remote;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.translator.LingualeoTranslator;
import com.eaccid.hocreader.data.remote.libtranslator.translator.Translator;

public class TranslatorFactory {
    private TranslatorFactory() {
    }

    public static Translator newTranslator(Translators tr) {

        switch (tr) {
            case LINGUALEO:
                return new LingualeoTranslator();
//            case G_TRANSLATOR:
//                return new GTranslator();
            default:
                throw new RuntimeException("smth went wrong!");
        }

    }
}