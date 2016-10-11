package com.eaccid.translator.translator;

public interface Translator {

    boolean translate(String word);

    TextTranslation getTranslations();
}
