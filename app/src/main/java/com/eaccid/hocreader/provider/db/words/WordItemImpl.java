package com.eaccid.hocreader.provider.db.words;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;
import com.eaccid.hocreader.provider.db.words.listprovider.ItemDataProviderImpl;

public class WordItemImpl extends ItemDataProviderImpl implements WordItem {

    private TextTranslation translation;

    public WordItemImpl(int id, Word word) {
        super(id, word);
    }

    public WordItemImpl(Word word) {
        super((int) word.getId(), word);
    }

    @Override
    public void setTranslationToText(TextTranslation textTranslation) {
        this.translation = textTranslation;
    }

    @Override
    public String getWordFromText() {
        return getWordValue().getName();
    }

    @Override
    public String getTranslation() {
        return getWordValue().getTranslation();
    }

    @Override
    public String getContext() {
        return getWordValue().getContext();
    }

    @Override
    public String getBook() {
        return ((Word) getObject()).getBook().getPath();
    }

    @Override
    public int getPage() {
        return ((Word) getObject()).getPage();
    }

    @Override
    public String getTranscription() {
        if (isTranslationEmpty())
            return emptyValue();
        return translation.getTranscription();
    }

    @Override
    public String getSoundUrl() {
        if (isTranslationEmpty())
            return emptyValue();
        return translation.getSoundUrl();
    }

    @Override
    public String getPictureUrl() {
        if (isTranslationEmpty())
            return emptyValue();
        return translation.getPicUrl();
    }

    private boolean isTranslationEmpty() {
        return translation == null;
    }

    private String emptyValue() {
        return "";
    }

    private Word getWordValue() {
        return (Word) getObject();
    }

}
