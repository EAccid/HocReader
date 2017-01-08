package com.eaccid.hocreader.injection.component;

import android.content.Context;
import android.content.SharedPreferences;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoDictionaryRx;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoServiceCookies;
import com.eaccid.hocreader.injection.ApplicationContext;
import com.eaccid.hocreader.injection.module.AppModule;
import com.eaccid.hocreader.provider.translator.LingualeoServiceCookiesImpl;

import javax.inject.Singleton;
import dagger.Component;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

    void inject(LingualeoServiceCookiesImpl leoCookies);

    void inject(LingualeoDictionaryRx dictionaryRx);

    @ApplicationContext
    Context context();

    SharedPreferences sharedPreferences();

    LingualeoServiceCookies lingualeoServiceCookies();

}
