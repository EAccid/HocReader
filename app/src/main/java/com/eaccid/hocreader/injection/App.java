package com.eaccid.hocreader.injection;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.eaccid.hocreader.injection.component.AppComponent;
import com.eaccid.hocreader.injection.component.DaggerAppComponent;
import com.eaccid.hocreader.injection.component.WordListComponent;
import com.eaccid.hocreader.injection.module.AppModule;
import com.eaccid.hocreader.injection.module.DatabaseModule;
import com.eaccid.hocreader.injection.module.WordListModule;

public class App extends Application {
    private static AppComponent component;
    private static WordListComponent wordListComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponent();
    }

    public static AppComponent getAppComponent() {
        return component;
    }

    public static WordListComponent getWordListComponent() {
        return wordListComponent;
    }

    private void buildComponent() {
        if (component == null) {
            component = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .databaseModule(new DatabaseModule())
                    .build();
        }
    }

    public static WordListComponent plusWordListComponent() {
        if (wordListComponent == null) {
            wordListComponent = component.plusWordListComponent(new WordListModule());
        }
        return wordListComponent;
    }

    public static void clearWordListComponent() {
        wordListComponent = null;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static void setComponent(AppComponent appComponent) {
        component = appComponent;
    }

}
