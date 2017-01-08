package com.eaccid.hocreader.data.remote.libtranslator.translator;


public interface Translator {

    @Deprecated
    boolean translate(String word);

    @Deprecated
    TextTranslation getTranslations();
}
