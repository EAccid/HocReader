package com.eaccid.hocreader.data.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eaccid.hocreader.data.local.db.entity.Book;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.data.local.db.service.BookDaoService;
import com.eaccid.hocreader.data.local.db.service.DatabaseManager;
import com.eaccid.hocreader.data.local.db.service.WordDaoService;
import com.eaccid.hocreader.data.local.db.service.WordReaderDaoService;
import com.eaccid.hocreader.exceptions.NotImplementedException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO:
 * 1. extract separate classes for BookReaderMode, AppWordManager
 * 2. create filter
 */

public class AppDatabaseManager implements BookReaderMode, AppWordManager {

    private final String LOG_TAG = "AppDatabaseManager";
    private DatabaseManager mDatabaseManager;

    public AppDatabaseManager(DatabaseManager mDatabaseManager) {
        this.mDatabaseManager = mDatabaseManager;
    }

    /**
     * reader mode
     */

    private Book mCurrentBook;
    private int mCurrentPage = 1;

    public void setCurrentBookForAddingWord(String filePath) {
        try {
            BookDaoService bs = mDatabaseManager.getBookService();
            mCurrentBook = (Book) bs.getById(filePath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentPageForAddingWord(int pageNumber) {
        mCurrentPage = pageNumber;
    }

    @NonNull
    @Override
    public Book getCurrentBook() {
        if (mCurrentBook == null)
            mCurrentBook = new Book();
        return mCurrentBook;
    }

    @Override
    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Nullable
    public Word getCurrentBooksWordByPage(String word) {
        try {
            WordDaoService ws = mDatabaseManager.getWordService();
            return ws.getWordByBookIdAndPage(word, getCurrentBook().getPath(), getCurrentPage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateBooks(List<String> bookpaths) {
        try {
            BookDaoService bookDaoService = mDatabaseManager.getBookService();
            List<Book> booksInDB = bookDaoService.getAll();
            for (Book book : booksInDB
                    ) {
                if (!bookpaths.contains(book.getPath())) {
                    bookDaoService.delete(book);
                    Log.i(LOG_TAG, "book '" + book.getName() + "' has been deleted.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> getAllBooks() {
        try {
            BookDaoService bs = mDatabaseManager.getBookService();
            return bs.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void createOrUpdateBook(String bookPath, String bookName) {
        try {
            BookDaoService bs = mDatabaseManager.getBookService();
            Book book = new Book(bookPath, bookName);
            bs.createOrUpdate(book);
            Log.i(LOG_TAG, "book '" + book.getName() + "' has been created / updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * words table
     */
    @Override
    public void createOrUpdateWord(String wordname, String translation, String context,
                                   boolean enabledOnline) {
        Word word = new Word();
        word.setName(wordname);
        word.setTranslation(translation);
        word.setContext(context);
        word.setPage(getCurrentPage());
        word.setEnabledOnline(enabledOnline);
        word.setBook(getCurrentBook());
        try {
            WordDaoService ws = mDatabaseManager.getWordService();
            boolean succeed = ws.createOrUpdate(word);
            Log.i(LOG_TAG, "Word '" + word.getName() + "' has been created / updated: " + succeed);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWord(Word word) {
        try {
            WordDaoService ws = mDatabaseManager.getWordService();
            ws.delete(word);
            Log.i(LOG_TAG, "word '" + word.getName() + "' has been deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO refactor: make method more readable
    @Override
    public List<Word> getAllWords(@Nullable Iterable<String> words, @Nullable WordFilter currentFilter, @Nullable String bookIdFilter) {
        if (currentFilter == null)
            currentFilter = WordFilter.NONE;
        List<Word> lw = new ArrayList<>();
        try {
            WordDaoService ws = mDatabaseManager.getWordService();
            switch (currentFilter) {
                case BY_BOOK:
                    if (getCurrentBook().getName().isEmpty())
                        throw new RuntimeException("Current book has not been set: \n" +
                                "argument 'bookIdFilter ?'");
                    lw = ws.getAllByBookId(bookIdFilter == null ? getCurrentBook().getPath() : bookIdFilter);
                    break;
                case BY_PAGE:
                    lw = ws.getAllByBookIdAndPage(bookIdFilter == null ? getCurrentBook().getPath() : bookIdFilter, getCurrentPage());
                    break;
                case BY_BOOK_AND_WORD_COLLECTION:
                    if (words == null)
                        return lw;
                    lw = ws.getAllByWordNameCollectionAndBookId(words, false, getCurrentBook().getPath());
                    break;
                case BY_BOOK_AND_EXCLUDED_WORD_COLLECTION:
                    if (words == null)
                        return lw;
                    lw = ws.getAllByWordNameCollectionAndBookId(words, true, getCurrentBook().getPath());
                    break;
                case BY_WORD_COLLECTION:
                    if (words == null)
                        return lw;
                    lw = ws.getAllByWordNameCollection(words);
                    break;
                case NONE:
                    lw = ws.getAll();
                    break;
                default:
                    return lw;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lw;
    }

    @Override
    @Nullable
    public Word getRandomWord() {
        try {
            WordReaderDaoService ws = mDatabaseManager.getWordService();
            Word word = ws.getRandomWord();
            Log.i(LOG_TAG, "random word '" + word + "' has been fetched.");
            return word;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Nullable
    public Word getWord(String word) {
        try {
            WordReaderDaoService ws = mDatabaseManager.getWordService();
            Log.i(LOG_TAG, "random word '" + word + "' has been fetched.");
            return ws.getAllByWordName(word).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteWords(WordFilter filter) {
        if (filter == WordFilter.BY_BOOK) {
            try {
                WordReaderDaoService ws = mDatabaseManager.getWordService();
                List<Word> words = ws.getAllByBookId(getCurrentBook().getPath());
                boolean succeed = ws.deleteAll(words);
                Log.i(LOG_TAG, "All words by book '" + getCurrentBook().getName() + "' have been deleted from database: " + succeed);
                return succeed;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (filter == WordFilter.NONE) {
            try {
                WordReaderDaoService ws = mDatabaseManager.getWordService();
                List<Word> words = ws.getAllWords();
                boolean succeed = ws.deleteAll(words);
                Log.i(LOG_TAG, "All words have been deleted from database: " + succeed);
                return succeed;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (filter != WordFilter.BY_BOOK || filter != WordFilter.NONE) {
            throw new NotImplementedException("Selection by filter '" + filter + "' not implemented yet.");
        }
        Log.i(LOG_TAG, "Words have not been deleted successfully.");
        return false;
    }

    public PreparedQuery<Word> getWordPreparedQuery(@Nullable WordFilter currentFilter, @Nullable Iterable<String> words, @Nullable String bookIdFilter) {
        if (currentFilter == null)
            currentFilter = WordFilter.NONE;
        try {
            WordReaderDaoService ws = mDatabaseManager.getWordService();
            switch (currentFilter) {
                case BY_BOOK:
                    if (getCurrentBook().getPath().isEmpty())
                        throw new RuntimeException("Current book has not been set: \n" +
                                "argument 'bookIdFilter ?'");
                    return ws.getWordsByBookIdPreparedQuery(bookIdFilter == null ? getCurrentBook().getPath() : bookIdFilter);
                default:
                    return ws.getAllWordsPreparedQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<Word, String> getWordDao() {
        try {
            WordReaderDaoService ws = mDatabaseManager.getWordService();
            return ws.getWordDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
