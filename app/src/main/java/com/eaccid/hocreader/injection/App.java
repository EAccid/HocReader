package com.eaccid.hocreader.injection;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.eaccid.hocreader.injection.component.AppComponent;
import com.eaccid.hocreader.injection.component.DaggerAppComponent;
import com.eaccid.hocreader.injection.component.WordListComponent;
import com.eaccid.hocreader.injection.module.AppModule;
import com.eaccid.hocreader.injection.module.DataProviderModule;
import com.eaccid.hocreader.injection.module.DatabaseModule;

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
        //TODO delete null check after adding Moxy:
        //it's a temp solution, on rotate screen getWordListComponent() returns null
        if (wordListComponent == null)
            plusWordListComponent();
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
            wordListComponent = component.plusWordListComponent(new DataProviderModule());
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
