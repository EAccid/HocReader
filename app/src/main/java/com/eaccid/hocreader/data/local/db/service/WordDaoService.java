package com.eaccid.hocreader.data.local.db.service;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

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
            return existedWord != null && dao.delete(existedWord) == 1;
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

    public boolean deleteAll(List<Word> words) {
        try {
            Integer sizeToDelete = words.size();
            Integer DeletedWords = dao.delete(words);
            return sizeToDelete.equals(DeletedWords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Word> getAllByWordNameCollection(Iterable<String> words) {
        try {
            QueryBuilder<Word, String> qb = dao.queryBuilder();
            qb.where().in("word", words);
            PreparedQuery<Word> preparedQuery = qb.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Word> getAllByWordNameCollectionAndBookId(Iterable<String> words, boolean excludeWords, String bookId) {
        try {
            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();
            where.eq("book_id", bookId);
            where.and();
            if (excludeWords) {
                where.notIn("word", words);
            } else {
                where.in("word", words);
            }
            PreparedQuery<Word> preparedQuery = qb.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Word> getAllByBookId(String bookId) {
        try {
            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();
            where.eq("book_id", bookId);
            PreparedQuery<Word> preparedQuery = qb.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Word> getAllByBookIdAndPage(String bookId, int pageNumber) {
        try {
            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();
            where.eq("book_id", bookId);
            where.and();
            where.eq("page", pageNumber);
            PreparedQuery<Word> preparedQuery = qb.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public PreparedQuery<Word> getAllWordsPreparedQuery() {
        try {
            QueryBuilder<Word, String> qb = dao.queryBuilder();
            qb.selectColumns("word", "translation");
            return qb.prepare();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedQuery<Word> getWordsByBookIdPreparedQuery(String bookId) {
        try {
            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();
            where.eq("book_id", bookId);
            return qb.prepare();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<Word, String> getWordDao() {
        return dao;
    }

    @Nullable
    public Word getWordByBookIdAndPage(String word, String bookId, int pageNumber) {
        try {
            QueryBuilder<Word, String> qb = dao.queryBuilder();
            Where<Word, String> where = qb.where();

            where.eq("book_id", bookId);
            where.and();
            where.eq("word", word);
            where.and();
            where.eq("page", pageNumber);

            PreparedQuery<Word> preparedQuery = qb.prepare();
            return dao.queryForFirst(preparedQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public Word getRandomWord() {
        try {
            QueryBuilder<Word, String> qb = dao.queryBuilder();
            qb.orderByRaw(" random() ");
            PreparedQuery<Word> preparedQuery = qb.prepare();
            return dao.queryForFirst(preparedQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private Word getWord(Word word) {
        return getWordByBookIdAndPage(word.getName(), word.getBook().getPath(), word.getPage());
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public List<Word> getAllByWordName(String word) {
        List<String> words = new ArrayList<>();
        words.add(word);
        return getAllByWordNameCollection(words);
    }

}
