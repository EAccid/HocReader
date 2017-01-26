package com.eaccid.hocreader.injection.component;

import android.content.Context;
import android.content.SharedPreferences;
import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoDictionaryRx;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoServiceCookies;
import com.eaccid.hocreader.injection.ApplicationContext;
import com.eaccid.hocreader.injection.module.AppModule;
import com.eaccid.hocreader.injection.module.DatabaseModule;
import com.eaccid.hocreader.injection.module.DataProviderModule;
import com.eaccid.hocreader.presentation.main.MainReaderPresenter;
import com.eaccid.hocreader.temp.presentation.activity.notcard.CardWordPresenter;
import com.eaccid.hocreader.temp.presentation.fragment.translation.semantic.ImageViewManager;
import com.eaccid.hocreader.temp.presentation.service.MemorizingPresenter;
import com.eaccid.hocreader.temp.presentation.service.SchedulingMemorizingAlarmManager;
import com.eaccid.hocreader.temp.provider.db.words.BookOnReadProvider;
import com.eaccid.hocreader.temp.provider.db.words.WordCursorProvider;
import com.eaccid.hocreader.temp.provider.translator.LingualeoServiceCookiesImpl;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, DatabaseModule.class})
@Singleton
public interface AppComponent {

    WordListComponent plusWordListComponent(DataProviderModule dataProviderModule);

    @ApplicationContext
    Context context();

    SharedPreferences sharedPreferences();

    LingualeoServiceCookies lingualeoServiceCookies();

    AppDatabaseManager appDatabaseManager();

    void inject(LingualeoServiceCookiesImpl leoCookies);

    void inject(LingualeoDictionaryRx dictionaryRx);

    void inject(ImageViewManager imageViewManager);

    void inject(MainReaderPresenter mainReaderPresenter);

    void inject(MemorizingPresenter memorizingPresenter);

    void inject(SchedulingMemorizingAlarmManager schedulingMemorizingAlarmManager);

    void inject(WordCursorProvider wordCursorProvider);

    void inject(CardWordPresenter cardWordPresenter);

    void inject(BookOnReadProvider bookOnReadProvider);
}
