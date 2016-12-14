package com.eaccid.hocreader.provider.db;

import android.content.Context;
import android.database.Cursor;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.local.WordFilter;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.presentation.fragment.carousel.WordCarouselRecyclerViewAdapter;
import com.eaccid.hocreader.presentation.fragment.carousel.OrmliteCursorRecyclerViewAdapter;
import com.j256.ormlite.android.apptools.OrmLiteCursorLoader;
import com.j256.ormlite.stmt.PreparedQuery;

public class WordCursorProvider {

    //TODO inject
    private Context context;
    private AppDatabaseManager dataManager;

    public WordCursorProvider(Context context, AppDatabaseManager appDatabaseManager) {
        this.context = context;
        this.dataManager = appDatabaseManager;
    }

    public OrmliteCursorRecyclerViewAdapter
    createAdapterWithCursor(WordCarouselRecyclerViewAdapter adapter, boolean filterByBook) {
        if (filterByBook)
            dataManager.setFilter(WordFilter.BY_BOOK);

        PreparedQuery<Word> pq = dataManager.getWordPreparedQuery(null, null);
        OrmLiteCursorLoader<Word> liteCursorLoader = new OrmLiteCursorLoader<>(
                context,
                dataManager.getWordDao(),
                pq);
        Cursor cursor = liteCursorLoader.loadInBackground();
        adapter.changeCursor(cursor, pq);
        return adapter;
    }
}
