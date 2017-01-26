package com.eaccid.hocreader.provider.translator;

import com.eaccid.hocreader.temp.ins.impl.TextManagerImpl;
import com.eaccid.hocreader.temp.underdevelopment.TranslatedWord;

public class TranslatedWordImpl implements TranslatedWord {

    private String wordBaseForm;
    private String wordFromContext;
    private String translation;
    private String context;

    @Override
    public String getWordBaseForm() {
        return wordBaseForm;
    }

    public void setWordBaseForm(String wordBaseForm) {
        this.wordBaseForm = wordBaseForm;
    }

    @Override
    public String getWordFromContext() {
        return wordFromContext;
    }

    public void setWordFromContext(String wordFromContext) {
        this.wordFromContext = wordFromContext;
    }

    @Override
    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = new TextManagerImpl().capitalizeFirsChar(translation);
    }

    @Override
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
