package com.eaccid.bookreader.db.service;

import android.database.Cursor;
import android.support.annotation.VisibleForTesting;

import com.eaccid.bookreader.db.entity.Word;
import com.j256.ormlite.android.AndroidCompiledStatement;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.android.apptools.support.OrmLiteCursorLoader;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordDaoService implements Crud {

    private Dao<Word, String> dao;

    WordDaoService(DatabaseHelper dbHelper) throws SQLException {
        dao = DaoManager.createDao(dbHelper.getConnectionSource(), Word.class);
    }

    @Override
    public boolean createOrUpdate(Object word) {

        boolean created = false;

        try {

            Word wordToWright = (Word) word;

            Word existedWord = getWord((Word) word);

            if (existedWord != null) {
                wordToWright.setId(existedWord.getId());
            }

            Dao.CreateOrUpdateStatus status = dao.createOrUpdate(wordToWright);
            created = status.isCreated() || status.isUpdated();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return created;
    }

    @Override
    public boolean delete(Object word) {
        try {

            Word existedWord = getWord((Word) word);

            if (existedWord != null) {
                return dao.delete(existedWord) == 1;
            }

            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Object getById(String id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Word> getAll() {

        List<Word> words = new ArrayList<>();
        try {
            words = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }


    //edit words from WordDataProvider

    public List<Word> getByBookidAndPage(String bookid, int pageNumber) {
        try {

            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();

            where.eq("book_id", bookid);
            where.and();
            where.eq("page", pageNumber);

            PreparedQuery<Word> preparedQuery = qb.prepare();
            List<Word> wordList = dao.query(preparedQuery);

            return wordList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


//    public List<Word> getByBookidAndPage(Word word) {
//        try {
//
//            QueryBuilder<Word, String> qb = dao.queryBuilder();
//            Where<Word, String> where = qb.where();
//
//            where.eq("word", word);
//            where.and();
//            where.eq("page", pageNumber);
//
//            PreparedQuery<Word> preparedQuery = qb.prepare();
//            List<Word> wordList = dao.query(preparedQuery);
//
//            return wordList;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>();
//    }


    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public List<Word> getByWordName(String word) {
        try {

            QueryBuilder<Word, String> qb = dao.queryBuilder();
            qb.where().eq("word", word);

            PreparedQuery<Word> preparedQuery = qb.prepare();
            List<Word> wordList = dao.query(preparedQuery);

            return wordList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private Word getWord(Word word) {
        try {

            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();

            where.eq("word", word.getWord());
            where.and();
            where.eq("page", word.getPage());
            where.and();
            where.eq("book_id", word.getBook().getPath());

            PreparedQuery<Word> preparedQuery = qb.prepare();
            List<Word> wordList = dao.query(preparedQuery);

            if (wordList.size() > 0) {
                return wordList.get(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //delete

    public Cursor getWordCursor() {

        try {

            QueryBuilder<Word, String> qb = dao.queryBuilder();
            qb.selectColumns("word", "translation");

            CloseableIterator<Word> iterator = dao.iterator(qb.prepare());
            AndroidDatabaseResults results =
                    (AndroidDatabaseResults) iterator.getRawResults();

            iterator.closeQuietly();

            return results.getRawCursor();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedQuery getWordPreparedQuery() {

        try {

            QueryBuilder<Word, String> qb = dao.queryBuilder();
            qb.selectColumns("word", "translation");
            return qb.prepare();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<Word, String> getWordDao() {
        return dao;
    }

}
