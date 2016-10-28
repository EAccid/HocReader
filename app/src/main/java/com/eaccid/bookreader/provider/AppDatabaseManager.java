package com.eaccid.bookreader.provider;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.eaccid.bookreader.db.entity.Book;
import com.eaccid.bookreader.db.entity.Word;
import com.eaccid.bookreader.db.service.BookDaoService;
import com.eaccid.bookreader.db.service.DatabaseManager;
import com.eaccid.bookreader.db.service.WordDaoService;
import com.j256.ormlite.dao.Dao;
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

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static void setDatabaseManager(DatabaseManager databaseManager) {
        AppDatabaseManager.databaseManager = databaseManager;
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


    // TODO DELETE WORD

    //TODO create filter filling
    private static Book currentBook;
    private static int currentPage;

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
        word.setWord(wordname);
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

    //TODO create filter filling temp - boolean
    public static List<Word> getAllWords(boolean filter) {

        List<Word> lw = new ArrayList<>();

        try {
            WordDaoService ws = databaseManager.getWordService();

            if (filter) {
                lw = ws.getByBookidAndPage(currentBook.getPath(), currentPage == 0 ? 1 : currentPage);
            } else {
                lw = ws.getAll();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lw;
    }

    //delete

    public static Cursor getWordCursor() {

        try {
            WordDaoService ws = databaseManager.getWordService();
            return ws.getWordCursor();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedQuery getWordPreparedQuery() {
        try {
            WordDaoService ws = databaseManager.getWordService();
            return ws.getWordPreparedQuery();

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
}