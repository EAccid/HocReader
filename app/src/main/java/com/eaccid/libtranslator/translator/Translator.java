package com.eaccid.libtranslator.translator;

public interface Translator {

    boolean translate(String word);

    TextTranslation getTranslations();
}
