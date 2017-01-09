package com.eaccid.hocreader.provider.db;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.provider.db.listprovider.ItemDataProviderImpl;


public class WordProvider extends ItemDataProviderImpl {

    public WordProvider(int id, Word word) {
        super(id, word);
        App.getAppComponent().inject(this);
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
