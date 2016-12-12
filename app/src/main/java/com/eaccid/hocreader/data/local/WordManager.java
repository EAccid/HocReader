package com.eaccid.hocreader.data.local;

import android.support.annotation.Nullable;
import android.util.Log;

import com.eaccid.hocreader.data.local.db.AppDatabaseManager;
import com.eaccid.hocreader.data.local.db.entity.Book;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.data.local.db.service.BookDaoService;
import com.eaccid.hocreader.data.local.db.service.WordDaoService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordManager extends AppDatabaseManager {

    private final String logTAG = "BookManager";
    private WordFilter currentFilter = WordFilter.NONE;

    //TODO: refactor - > del current book and page solution
    private static Book currentBook;
    private static int currentPage = 1;

    public void setCurrentBookForAddingWord(String filePath) {
        try {
            BookDaoService bs = mDatabaseManager.getBookService();
            currentBook = (Book) bs.getById(filePath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentPageForAddingWord(int pageNumber) {
        currentPage = pageNumber;
    }

    public void createOrUpdateWord(String wordname, String translation, String context,
                                   boolean enabledOnline) {
        Word word = new Word();
        word.setName(wordname);
        word.setTranslation(translation);
        word.setContext(context);
        word.setPage(currentPage);
        word.setEnabledOnline(enabledOnline);
        word.setBook(currentBook);
        try {
            WordDaoService ws = mDatabaseManager.getWordService();
            ws.createOrUpdate(word);
            Log.i(logTAG, "word '" + word.getName() + "' has been created / updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWord(Word word) {
        try {
            WordDaoService ws = mDatabaseManager.getWordService();
            ws.delete(word);
            Log.i(logTAG, "word '" + word.getName() + "' has been deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public WordFilter clearFilter() {
        return currentFilter = WordFilter.NONE;
    }

    public void setFilter(WordFilter filter) {
        currentFilter = filter;
    }


    public List<Word> getAllWords(@Nullable Iterable<String> wordsFilter, @Nullable String bookIdFilter) {

        //TODO create filter and refactor/temp solution

        List<Word> lw = new ArrayList<>();
        try {
            WordDaoService ws = mDatabaseManager.getWordService();
            switch (currentFilter) {
                case BY_BOOK:

                    if (bookIdFilter == null && currentBook == null)
                        throw new RuntimeException("Current book filter has not been set: \n" +
                                "'wordManager.setFilter( ? )' or argument 'bookIdFilter ?'");
                    lw = ws.getAllByBookId(bookIdFilter == null ? currentBook.getPath() : bookIdFilter);
                    break;
                case BY_PAGE:

                    lw = ws.getAllByBookIdAndPage(bookIdFilter == null ? currentBook.getPath() : bookIdFilter, currentPage);
                    break;

                case BY_BOOK_AND_WORD_COLLECTION:

                    if (wordsFilter == null)
                        return lw;
                    lw = ws.getAllByWordNameCollectionAndBookId(wordsFilter, false, currentBook.getPath());
                    break;

                case BY_BOOK_AND_EXCLUDED_WORD_COLLECTION:

                    if (wordsFilter == null)
                        return lw;
                    lw = ws.getAllByWordNameCollectionAndBookId(wordsFilter, true, currentBook.getPath());
                    break;

                case BY_WORD_COLLECTION:

                    if (wordsFilter == null)
                        return lw;
                    lw = ws.getAllByWordNameCollection(wordsFilter);
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

    public PreparedQuery<Word> getWordPreparedQuery(@Nullable Iterable<String> wordsFilter, @Nullable String bookIdFilter) {

        //TODO create filter and refactor/temp solution

        try {
            WordDaoService ws = mDatabaseManager.getWordService();

            switch (currentFilter) {
                case BY_BOOK:
                    if (bookIdFilter == null && currentBook == null)
                        throw new RuntimeException("Current book filter has not been set: \n" +
                                "'wordManager.setFilter( ? )' or argument 'bookIdFilter ?'");
                    return ws.getWordsByBookIdPreparedQuery(bookIdFilter == null ? currentBook.getPath() : bookIdFilter);
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
            WordDaoService ws = mDatabaseManager.getWordService();
            return ws.getWordDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public Word getCurrentBooksWordByPage(String word) {
        try {
            WordDaoService ws = mDatabaseManager.getWordService();
            return ws.getWordByBookIdAndPage(word, currentBook.getPath(), currentPage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCurrentBookName() {
        return currentBook.getName();
    }

    public String getCurrentBookPath() {
        return currentBook.getPath();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Nullable
    public Word getRandomWord() {
        try {
            WordDaoService ws = mDatabaseManager.getWordService();
            Word word = ws.getRandomWord();
            Log.i(logTAG, "random word '" + word + "' has been fetched.");
            return word;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
