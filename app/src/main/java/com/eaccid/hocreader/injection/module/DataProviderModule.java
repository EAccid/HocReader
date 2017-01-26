package com.eaccid.hocreader.injection.module;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.injection.WordListScope;
import com.eaccid.hocreader.provider.db.words.WordListFromDatabaseFetcher;
import com.eaccid.hocreader.provider.db.words.WordListInteractor;

import dagger.Module;
import dagger.Provides;

@Module
@WordListScope
public class DataProviderModule {

    @Provides
    @WordListScope
    WordListFromDatabaseFetcher provideWordListProvider(AppDatabaseManager databaseManager) {
        return new WordListFromDatabaseFetcher(databaseManager);
    }

    @Provides
    @WordListScope
    WordListInteractor provideWordListInteractor(WordListFromDatabaseFetcher wordListFromDatabaseFetcher) {
        return new WordListInteractor(wordListFromDatabaseFetcher);
    }

}
