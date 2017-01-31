package com.eaccid.hocreader.provider.db.words;

import android.content.Context;
import android.database.Cursor;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.local.WordFilter;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.injection.ApplicationContext;
import com.eaccid.hocreader.presentation.training.carouseladapter.WordCarouselRecyclerViewAdapter;
import com.eaccid.hocreader.presentation.training.carouseladapter.OrmLiteCursorRecyclerViewAdapter;
import com.j256.ormlite.android.apptools.OrmLiteCursorLoader;
import com.j256.ormlite.stmt.PreparedQuery;

import javax.inject.Inject;

public class WordCursorProvider {

    @Inject
    @ApplicationContext
    Context context;
    @Inject
    AppDatabaseManager dataManager;

    public WordCursorProvider() {
        App.getAppComponent().inject(this);
    }

    public OrmLiteCursorRecyclerViewAdapter
    createAdapterWithCursor(WordCarouselRecyclerViewAdapter adapter, boolean filterByBook) {

        WordFilter wordFilter = filterByBook ? WordFilter.BY_BOOK : WordFilter.NONE;
        PreparedQuery<Word> pq = dataManager.getWordPreparedQuery(wordFilter, null, null);
        OrmLiteCursorLoader<Word> liteCursorLoader = new OrmLiteCursorLoader<>(
                context,
                dataManager.getWordDao(),
                pq);
        Cursor cursor = liteCursorLoader.loadInBackground();
        adapter.changeCursor(cursor, pq);
        return adapter;
    }

}
