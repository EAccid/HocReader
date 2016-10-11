package com.eaccid;

import com.eaccid.translator.lingualeo.translator.LingualeoTranslator;
import com.eaccid.translator.translator.Translator;

public class TranslatorFactory {
    private TranslatorFactory() {
    }

    public static Translator newTranslator() {

        //TODO online LINGUALEO/ offline GOOGLE
        return new LingualeoTranslator();
    }
}
