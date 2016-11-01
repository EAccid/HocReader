package com.eaccid.bookreader.fragment_2;

import android.content.Context;
import android.database.Cursor;

import com.eaccid.bookreader.db.AppDatabaseManager;
import com.eaccid.bookreader.db.WordFilter;
import com.eaccid.bookreader.db.entity.Word;
import com.j256.ormlite.android.apptools.OrmLiteCursorLoader;
import com.j256.ormlite.stmt.PreparedQuery;

public class WordCursorBinder {

    private Context context;
    private boolean filterByBook;

    public WordCursorBinder(Context context, boolean filterByBook) {
        this.context = context;
        this.filterByBook = filterByBook;
    }

    public OrmliteCursorRecyclerViewAdapter createAdapterWithCursor(DrawerRecyclerViewAdapter adapter) {

        if (filterByBook) {
            AppDatabaseManager.setFilter(WordFilter.BY_BOOK);
        }

        PreparedQuery<Word> pq = AppDatabaseManager.getWordPreparedQuery(null, null);
        OrmLiteCursorLoader<Word> liteCursorLoader = new OrmLiteCursorLoader<>(
                context,
                AppDatabaseManager.getWordDao(),
                pq);
        Cursor cursor = liteCursorLoader.loadInBackground();
        adapter.changeCursor(cursor, pq);
        return adapter;
    }

}
