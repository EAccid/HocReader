package com.eaccid.hocreader.provider.db;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;
import com.eaccid.hocreader.provider.db.listprovider.ItemDataProviderImpl;

public class WordProviderImpl extends ItemDataProviderImpl implements WordProvider {

    private TextTranslation translation;

    public WordProviderImpl(int id, Word word) {
        super(id, word);
    }

    public void setTranslationToText(TextTranslation textTranslation) {
        this.translation = textTranslation;
    }

    @Override
    public String getWordFromText() {
        return ((Word) getObject()).getName();
    }

    @Override
    public String getTranslation() {
        return ((Word) getObject()).getTranslation();
    }

    @Override
    public String getContext() {
        return ((Word) getObject()).getContext();
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
    public boolean isSetToLearn() {
        //test
        return getItemId() % 2 != 0;
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

}
