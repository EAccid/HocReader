package com.eaccid.bookreader.db;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eaccid.bookreader.db.entity.Book;
import com.eaccid.bookreader.db.entity.Word;
import com.eaccid.bookreader.db.service.BookDaoService;
import com.eaccid.bookreader.db.service.DatabaseManager;
import com.eaccid.bookreader.db.service.WordDaoService;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppDatabaseManager {

    private static DatabaseManager databaseManager;

    public static void loadDatabaseManager(Context baseContext) {
        databaseManager = DatabaseManager.getInstance(baseContext);
    }

    public static void releaseDatabaseManager() {
        databaseManager.releaseConnection();
        databaseManager = null;
    }

    // TODO DELETE BOOK: Main activity, Main Book List View


    public static void refreshBooks(List<String> filePaths) {

        try {
            BookDaoService bookDaoService = databaseManager.getBookService();
            List<Book> booksInDB = bookDaoService.getAll();
            for (Book book : booksInDB
                    ) {
                if (!filePaths.contains(book.getPath())) {
                    bookDaoService.delete(book);
                    Log.i("BookDaoService: ", "book '" + book.getName() + "' has been deleted.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static List<Book> getAllBooks() {
        List<Book> lb = new ArrayList<>();
        try {
            BookDaoService bs = databaseManager.getBookService();

            lb = bs.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lb;
    }

    public static void createOrUpdateBook(String filePath, String fileName, int amountPages) {

        try {
            BookDaoService bs = databaseManager.getBookService();
            Book book = new Book(filePath, fileName, amountPages);
            bs.createOrUpdate(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    // TODO DELETE WORD: DataProvider


    //TODO create filter
    private static Book currentBook;
    private static int currentPage = 1;
    private static WordFilter currentFilter = WordFilter.NONE;

    public static void setCurrentBookForAddingWord(String filePath) {

        try {
            BookDaoService bs = databaseManager.getBookService();
            currentBook = (Book) bs.getById(filePath);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setCurrentPageForAddingWord(int pageNumber) {
        currentPage = pageNumber;
    }

    public static void createOrUpdateWord(String wordname, String translation, String context,
                                          boolean enabledOnline) {
        Word word = new Word();
        word.setName(wordname);
        word.setTranslation(translation);
        word.setContext(context);
        word.setPage(currentPage);
        word.setEnabledOnline(enabledOnline);
        word.setBook(currentBook);

        try {
            WordDaoService ws = databaseManager.getWordService();
            ws.createOrUpdate(word);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void deleteWord(Word word) {
        try {
            WordDaoService ws = databaseManager.getWordService();
            ws.delete(word);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static WordFilter clearFilter() {
        return currentFilter = WordFilter.NONE;
    }

    public static void setFilter(WordFilter filter) {
        currentFilter = filter;
    }

    //TODO create filter /temp solution
    public static List<Word> getAllWords(@Nullable Iterable<String> wordsFilter, @Nullable String bookIdFilter) {
        List<Word> lw = new ArrayList<>();
        try {
            WordDaoService ws = databaseManager.getWordService();
            switch (currentFilter) {
                case BY_BOOK:

                    if (bookIdFilter == null && currentBook == null)
//                        return lw;
                        throw new RuntimeException("Current book filter has not been set: \n" +
                                "'AppDatabaseManager.setFilter( ? )' or argument 'bookIdFilter ?'");
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

    public static PreparedQuery<Word> getWordPreparedQuery(@Nullable Iterable<String> wordsFilter, @Nullable String bookIdFilter) {

        try {
            WordDaoService ws = databaseManager.getWordService();

            switch (currentFilter) {
                case BY_BOOK:
                    if (bookIdFilter == null && currentBook == null)
                        throw new RuntimeException("Current book filter has not been set: \n" +
                                "'AppDatabaseManager.setFilter( ? )' or argument 'bookIdFilter ?'");
                    return ws.getWordsByBookIdPreparedQuery(bookIdFilter == null ? currentBook.getPath() : bookIdFilter);

                default:
                    return ws.getAllWordsPreparedQuery();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Dao<Word, String> getWordDao() {

        try {
            WordDaoService ws = databaseManager.getWordService();
            return ws.getWordDao();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Word getCurrentBooksWordByPage(String wordBaseName) {
        try {
            WordDaoService ws = databaseManager.getWordService();
            return ws.getWordByBookIdAndPage(wordBaseName, currentBook.getPath(), currentPage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static String getCurrentBookName() {
        return currentBook.getName();
    }

}