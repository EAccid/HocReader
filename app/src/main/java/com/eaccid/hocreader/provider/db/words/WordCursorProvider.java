package com.eaccid.hocreader.provider.db.words;

import android.content.Context;
import android.database.Cursor;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.local.WordFilter;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.App;
import com.eaccid.hocreader.presentation.training.carouseladapter.WordCarouselRecyclerViewAdapter;
import com.eaccid.hocreader.presentation.training.carouseladapter.OrmLiteCursorRecyclerViewAdapter;
import com.j256.ormlite.android.apptools.OrmLiteCursorLoader;
import com.j256.ormlite.stmt.PreparedQuery;

import javax.inject.Inject;

public class WordCursorProvider {
    @Inject
    AppDatabaseManager dataManager;

    public OrmLiteCursorRecyclerViewAdapter
    createAdapterWithCursor(Context context, WordCarouselRecyclerViewAdapter adapter, boolean filterByBook) {
        setupComponent(context);
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

    public void setupComponent(Context context) {
        App.get(context).getAppComponent().inject(this);
    }

}
