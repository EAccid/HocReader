package com.eaccid.hocreader.provider.db;

public interface WordProvider {
    String getWordFromText();

    String getTranslation();

    String getContext();

    String getTranscription();

    String getSoundUrl();

    String getPicUrl();

    String getBook();

    int getPage();

    boolean isSetToLearn();
}
