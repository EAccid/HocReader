package com.eaccid.hocreader.provider.db;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.provider.db.dataprovider.ItemDataProviderImpl;

public class WordProvider extends ItemDataProviderImpl {

    public WordProvider(int currentId, Word word) {
        super(currentId, word);
    }

    public String getName() {
        return ((Word) getObject()).getName();
    }

    public String getTranslation() {
        return ((Word) getObject()).getTranslation();
    }

    public String getContext() {
        return ((Word) getObject()).getContext();
    }
}
