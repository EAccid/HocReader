package com.eaccid.hocreader.data.local;

import android.support.annotation.Nullable;

import com.eaccid.hocreader.data.local.db.entity.Word;

import java.util.List;

public interface AppWordManager {

    void createOrUpdateWord(String wordname, String translation, String context, boolean enabledOnline);

    List<Word> getAllWords(@Nullable Iterable<String> words, @Nullable WordFilter currentFilter, @Nullable String bookIdFilter);

    Word getRandomWord();

    Word getWord(String word);

    @Nullable
    Word getCurrentBooksWordByPage(String word);

    void deleteWord(Word word);

    boolean deleteWords(WordFilter filter);

}
