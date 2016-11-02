package com.eaccid.libtranslator.translator;

import com.eaccid.libtranslator.lingualeo.translator.LingualeoTranslator;

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
                throw new RuntimeException("smth goes wrong!");
        }

    }
}