package com.eaccid.hocreader.provider.translator;

public interface TranslatedWord {

    String getWordFromContext();

    String getTranslation();

    String getContext();

    void addTranslation(String translation);
}
