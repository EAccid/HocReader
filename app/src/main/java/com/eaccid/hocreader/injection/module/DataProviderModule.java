package com.eaccid.hocreader.injection.module;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.injection.WordListScope;
import com.eaccid.hocreader.provider.db.words.WordListManager;
import com.eaccid.hocreader.provider.db.words.WordListInteractor;

import dagger.Module;
import dagger.Provides;

@Module
@WordListScope
public class DataProviderModule {

    @Provides
    @WordListScope
    WordListManager provideWordListProvider(AppDatabaseManager databaseManager) {
        return new WordListManager(databaseManager);
    }

    @Provides
    @WordListScope
    WordListInteractor provideWordListInteractor(WordListManager wordListManager) {
        return new WordListInteractor(wordListManager);
    }

}
