package com.eaccid.hocreader.data.local;

import android.util.Log;

import com.eaccid.hocreader.data.local.db.AppDatabaseManager;
import com.eaccid.hocreader.data.local.db.entity.Book;
import com.eaccid.hocreader.data.local.db.service.BookDaoService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookManager extends AppDatabaseManager {

    private final String logTAG = "BookManager";

    public void refreshBooks(List<String> bookpaths) {
        try {
            BookDaoService bookDaoService = mDatabaseManager.getBookService();
            List<Book> booksInDB = bookDaoService.getAll();
            for (Book book : booksInDB
                    ) {
                if (!bookpaths.contains(book.getPath())) {
                    bookDaoService.delete(book);
                    Log.i(logTAG, "book '" + book.getName() + "' has been deleted.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        try {
            BookDaoService bs = mDatabaseManager.getBookService();
            return bs.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void createOrUpdateBook(String bookPath, String bookName) {
        try {
            BookDaoService bs = mDatabaseManager.getBookService();
            Book book = new Book(bookPath, bookName);
            bs.createOrUpdate(book);
            Log.i(logTAG, "book '" + book.getName() + "' has been created / updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
