package com.eaccid.hocreader.provider.db;

import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;

public interface WordItem {

    void setTranslationToText(TextTranslation textTranslation);

    String getWordFromText();

    String getTranslation();

    String getContext();

    String getTranscription();

    String getSoundUrl();

    String getPictureUrl();

    String getBook();

    int getPage();

    boolean isSetToLearn();
}
