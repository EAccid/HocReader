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

public class WordCursorBinder {

    public OrmliteCursorRecyclerViewAdapter
    createAdapterWithCursor(Context context, WordCarouselRecyclerViewAdapter adapter,
                            AppDatabaseManager wordManager, boolean filterByBook) {
        if (filterByBook)
            wordManager.setFilter(WordFilter.BY_BOOK);

        PreparedQuery<Word> pq = wordManager.getWordPreparedQuery(null, null);
        OrmLiteCursorLoader<Word> liteCursorLoader = new OrmLiteCursorLoader<>(
                context,
                wordManager.getWordDao(),
                pq);
        Cursor cursor = liteCursorLoader.loadInBackground();
        adapter.changeCursor(cursor, pq);
        return adapter;
    }
}
