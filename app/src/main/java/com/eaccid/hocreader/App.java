package com.eaccid.hocreader;

import android.app.Application;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoServiceCookies;
import com.eaccid.hocreader.injection.component.AppComponent;
import com.eaccid.hocreader.injection.component.DaggerAppComponent;
import com.eaccid.hocreader.injection.component.WordListComponent;
import com.eaccid.hocreader.injection.module.AppModule;
import com.eaccid.hocreader.injection.module.DataProviderModule;
import com.eaccid.hocreader.injection.module.DatabaseModule;
import com.eaccid.hocreader.provider.translator.LingualeoServiceCookiesImpl;

public class App extends Application {

    private AppComponent component;
    private WordListComponent wordListComponent;
    //TODO take into component
    private static LingualeoServiceCookies cookies;

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponent();
        cookies = new LingualeoServiceCookiesImpl(this);
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public static LingualeoServiceCookies getLeoCookies() {
        return cookies;
    }

    public AppComponent getAppComponent() {
        return component;
    }

    public WordListComponent getWordListComponent() {
        return wordListComponent;
    }

    public WordListComponent plusWordListComponent() {
        wordListComponent = component.plusWordListComponent(new DataProviderModule());
        return wordListComponent;
    }

    public void clearWordListComponent() {
        wordListComponent = null;
    }

    private void buildComponent() {
        if (component == null) {
            component = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .databaseModule(new DatabaseModule())
                    .build();
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void setComponent(AppComponent appComponent) {
        component = appComponent;
    }

}
