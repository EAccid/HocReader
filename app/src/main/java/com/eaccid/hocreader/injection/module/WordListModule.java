package com.eaccid.hocreader.injection.module;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.injection.WordListScope;
import com.eaccid.hocreader.provider.db.WordListFetcher;
import com.eaccid.hocreader.provider.db.WordListInteractor;

import dagger.Module;
import dagger.Provides;

@Module
@WordListScope
public class WordListModule {

    @Provides
    @WordListScope
    WordListFetcher provideWordListProvider(AppDatabaseManager databaseManager) {
        return new WordListFetcher(databaseManager);
    }

    @Provides
    @WordListScope
    WordListInteractor provideWordListInteractor(WordListFetcher wordListFetcher) {
        return new WordListInteractor(wordListFetcher);
    }

}
