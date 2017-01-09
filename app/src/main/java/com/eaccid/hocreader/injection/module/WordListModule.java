package com.eaccid.hocreader.injection.module;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.injection.WordListScope;
import com.eaccid.hocreader.provider.db.WordListProvider;
import com.eaccid.hocreader.provider.db.WordListInteractor;

import dagger.Module;
import dagger.Provides;

@Module
@WordListScope
public class WordListModule {

    @Provides
    @WordListScope
    WordListProvider provideWordListProvider(AppDatabaseManager databaseManager) {
        return new WordListProvider(databaseManager);
    }

    @Provides
    @WordListScope
    WordListInteractor provideWordListInteractor(WordListProvider wordListProvider) {
        return new WordListInteractor(wordListProvider);
    }

}
