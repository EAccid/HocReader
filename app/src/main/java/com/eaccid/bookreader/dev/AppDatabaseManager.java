package com.eaccid.bookreader.dev;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.ListView;

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
    private static Book currentBook;
    private static int currentPage = 0;

    public static void loadDatabaseManagerForAllActivities(Context baseContext) {
        databaseManager = DatabaseManager.getInstance(baseContext);
    }

    public static void releaseDatabaseManager() {
        databaseManager.releaseConnection();
        databaseManager = null;
    }

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

    public static void setCurrentBookForAddingWord(String filePath) {

        try {
            BookDaoService bs = databaseManager.getBookService();
            currentBook = (Book) bs.getById(filePath);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setCurrentPageForAddingWord(int page) {
        currentPage = page;
    }

    public static List<Word> getAllWords() {
        List<Word> lw = new ArrayList<>();
        try {
            WordDaoService ws = databaseManager.getWordService();
            lw = ws.getAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lw;
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

    public static void createOrUpdateWord(String wordname, String translation, String context, boolean enabledOnline) {

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