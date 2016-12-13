package com.eaccid.hocreader.provider.translator;

public class TranslatedWord {

    private String wordBaseForm;
    private String wordFromContext;
    private String translation;
    private String context;

    public String getWordBaseForm() {
        return wordBaseForm;
    }

    public void setWordBaseForm(String wordBaseForm) {
        this.wordBaseForm = wordBaseForm;
    }

    public String getWordFromContext() {
        return wordFromContext;
    }

    public void setWordFromContext(String wordFromContext) {
        this.wordFromContext = wordFromContext;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
