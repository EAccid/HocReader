package com.eaccid.hocreader.data.remote.libtranslator.translator;

public interface Translator {

    boolean translate(String word);

    TextTranslation getTranslations();
}
