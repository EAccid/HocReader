package com.eaccid.hocreader;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.eaccid.hocreader.injection.component.AppComponent;
import com.eaccid.hocreader.injection.component.DaggerAppComponent;
import com.eaccid.hocreader.injection.module.AppModule;

public class App extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponent();
    }

    public static AppComponent getAppComponent() {
        return component;
    }

    private void buildComponent() {
        if (component == null) {
            component = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static void setComponent(AppComponent appComponent) {
        component = appComponent;
    }

}
