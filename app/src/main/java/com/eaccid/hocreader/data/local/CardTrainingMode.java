package com.eaccid.hocreader.data.local;

import android.support.annotation.Nullable;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

public interface CardTrainingMode {
    @Nullable
    PreparedQuery<Word> getWordPreparedQuery(@Nullable WordFilter currentFilter, @Nullable Iterable<String> words, @Nullable String bookIdFilter);

    @Nullable
    Dao<Word, String> getWordDao();
}
