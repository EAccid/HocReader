package com.eaccid.hocreader.injection.module;

import android.content.Context;
import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.local.db.service.DatabaseManager;
import com.eaccid.hocreader.injection.ApplicationContext;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    DatabaseManager provideDatabaseManager(@ApplicationContext Context context) {
        return new DatabaseManager(context);
    }

    @Provides
    @Singleton
    AppDatabaseManager provideAppDatabaseManager(DatabaseManager databaseManager) {
        return new AppDatabaseManager(databaseManager);
    }

}
