package com.eaccid.hocreader.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.App;
import com.eaccid.hocreader.injection.ApplicationContext;
import com.eaccid.hocreader.presentation.main.ins.directories.DirectoriesPreferences;
import com.eaccid.hocreader.presentation.settings.Preference;
import com.eaccid.hocreader.provider.db.books.BookInteractor;
import com.eaccid.hocreader.provider.db.books.BookInteractorImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final App appContext;

    public AppModule(App appContext) {
        this.appContext = appContext;
    }

    @ApplicationContext
    @Provides
    @Singleton
    public Context provideContext() {
        return appContext;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return appContext;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return appContext.getSharedPreferences(Preference.SHP_NAME_APP, Context.MODE_PRIVATE);
    }

    //TODO activity scope
    @Provides
    @Singleton
    BookInteractor provideBookInteractor(AppDatabaseManager databaseManager) {
        return new BookInteractorImpl(databaseManager);
    }

    //TODO delete after transferring directories into db
    @Provides
    @Singleton
    DirectoriesPreferences provideDirectoriesPreferences(SharedPreferences sharedPreferences) {
        return new DirectoriesPreferences(sharedPreferences, 100);
    }

}
