package com.eaccid.bookreader.translator;

import com.eaccid.libtranslator.lingualeo.translator.LingualeoTranslator;
import com.eaccid.libtranslator.translator.Translator;

public class TranslatorFactory {
    private TranslatorFactory() {
    }

    public static Translator newTranslator() {

        //TODO online LINGUALEO/ offline GOOGLE
        return new LingualeoTranslator();
    }
}
