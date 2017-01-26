package com.eaccid.hocreader.provider.translator;

import com.eaccid.hocreader.provider.fromtext.ins.TextManagerImpl;

public class TranslatedWordImpl implements TranslatedWord {

    private String word;
    private String translation;
    private String context;

    public TranslatedWordImpl(String word, String context) {
        this.context = context;
        this.word = word;
    }

    @Override
    public String getWordFromContext() {
        return word;
    }

    @Override
    public String getTranslation() {
        return translation;
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public void addTranslation(String translation) {
        this.translation = new TextManagerImpl().capitalizeFirsChar(translation);
    }
}
