package com.eaccid.hocreader.data.local.db.service;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.util.List;

public interface WordReaderDaoService {

    boolean deleteAll(List<Word> words);

    List<Word> getAllWords();

    List<Word> getAllByWordNameCollection(Iterable<String> words);

    List<Word> getAllByWordNameCollectionAndBookId(Iterable<String> words, boolean excludeWords, String bookid);

    List<Word> getAllByBookId(String bookid);

    List<Word> getAllByBookIdAndPage(String bookid, int pageNumber);

    PreparedQuery<Word> getAllWordsPreparedQuery();

    PreparedQuery<Word> getWordsByBookIdPreparedQuery(String bookid);

    Dao<Word, String> getWordDao();

    @Nullable
    Word getWordByBookIdAndPage(String word, String bookid, int pageNumber);

    @Nullable
    Word getRandomWord();

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    List<Word> getAllByWordName(String word);

}
