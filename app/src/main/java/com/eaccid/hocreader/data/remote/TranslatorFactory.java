package com.eaccid.hocreader.data.remote;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.translator.LingualeoTranslatorRx;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TranslatorRx;

public class TranslatorFactory {
    private TranslatorFactory() {
    }

    public static TranslatorRx newTranslator(Translators tr) {

        switch (tr) {
            case LINGUALEO_ONLINE:
                return new LingualeoTranslatorRx();
//            case LIVIO_TRANSLATOR:
            default:
                throw new RuntimeException("Something went wrong!");
        }

    }
}