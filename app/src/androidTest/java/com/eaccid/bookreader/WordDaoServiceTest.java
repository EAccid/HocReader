package com.eaccid.bookreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.eaccid.bookreader.db.entity.Book;
import com.eaccid.bookreader.db.entity.Word;
import com.eaccid.bookreader.db.service.BookDaoService;
import com.eaccid.bookreader.db.service.DatabaseHelper;
import com.eaccid.bookreader.db.service.DatabaseManager;
import com.eaccid.bookreader.db.service.WordDaoService;
import com.j256.ormlite.table.TableUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class WordDaoServiceTest {

    private DatabaseManager databaseManager;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        Method method = DatabaseManager.class.getDeclaredMethod("getTestInstance", Context.class, String.class);
        method.setAccessible(true);
        databaseManager = (DatabaseManager) method.invoke(method, appContext, "test_hr.db");

        DatabaseHelper databaseHelper = databaseManager.getDatabaseHelper();
        TableUtils.dropTable(databaseHelper.getConnectionSource(), Word.class, true);
        TableUtils.dropTable(databaseHelper.getConnectionSource(), Book.class, true);
        TableUtils.createTable(databaseHelper.getConnectionSource(), Word.class);
        TableUtils.createTable(databaseHelper.getConnectionSource(), Book.class);
    }

    @Test
    public void testCreateOrUpdateBook() throws Exception {

        Book book = new Book("/storage/sdcard/Download/book1 test.txt", "book1 test.txt", 513);
        Book book3 = new Book("/storage/sdcard/Download/book3 test.txt", "book3 test.txt", 713);

        Word word1 = new Word("mad", "сумасшедший", "mad in some context", 13, book, false);
        Word word2 = new Word("devastate", "опустошать", "devastate in some context", 13, book, false);
        Word word3 = new Word("persist", "упорствовать", "persist in some context", 13, book3, false);

        BookDaoService bs = databaseManager.getBookService();
        bs.createOrUpdate(book);
        bs.createOrUpdate(book3);

        WordDaoService ws = databaseManager.getWordService();

        assertTrue("word1 has bean created.", ws.createOrUpdate(word1));
        ws.createOrUpdate(word2);
        ws.createOrUpdate(word3);

        Word word2Created = (Word) ws.getById("2");
        assertEquals("Created word2 should be: '" + word1.getWord() + "'", word2.getWord(), word2Created.getWord());


        Word word3Created = (Word) ws.getById("3");
        assertEquals("Created word3 should be in book: '" + book3.getName() + "'", book3.getName(), word3Created.getBook().getName());


//
//        book.setName("same book");
//        bs.createOrUpdate(book);
//
//        Book bookCreated1 = (Book) bs.getById(1);
//        assertEquals("Created book should be: '" + book.getName() + "'", book.getName(), bookCreated1.getName());
    }

    @Test
    public void testAmountBooks() throws Exception {

//        BookDaoService bs = databaseManager.getBookService();
//
//        Book book1 = new Book("book test 1 .txt", 1);
//        Book book2 = new Book("book test 2 .txt", 2);
//        Book book3 = new Book("book test 3 .txt", 3);
//        Book book4 = new Book("book test 4 .txt", 4);
//        Book book5 = new Book("book test 5 .txt", 5);
//
//        bs.createOrUpdate(book1);
//        bs.createOrUpdate(book2);
//        bs.createOrUpdate(book3);
//        bs.createOrUpdate(book4);
//        bs.createOrUpdate(book5);
//
//        int amountBooks = bs.getAll().size();
//
//        assertEquals("Books amount should  be 5", 5, amountBooks);

    }

    @Test
    public void testDeleteBook() throws Exception {

//        BookDaoService bs = databaseManager.getBookService();
//
//        Book book1 = new Book("book test 1 .txt", 1);
//        Book book2 = new Book("book test 2 .txt", 2);
//        Book book3 = new Book("book test 3 .txt", 3);
//        Book book4 = new Book("book test 4 .txt", 4);
//        Book book5 = new Book("book test 5 .txt", 5);
//
//        bs.createOrUpdate(book1);
//        bs.createOrUpdate(book2);
//        bs.createOrUpdate(book3);
//        bs.createOrUpdate(book4);
//        bs.createOrUpdate(book5);
//
//        bs.delete(book3);
//        bs.delete(book4);
//
//        int amountBooks = bs.getAll().size();
//
//        assertEquals("Books amount should  be 3", 3, amountBooks);
//
//        Book bookCreated1 = (Book) bs.getById(1);
//        assertEquals("book1 book should be: '" + book1.getName() + "'", book1.getName(), bookCreated1.getName());
//
//        Book bookCreated2 = (Book) bs.getById(2);
//        assertEquals("book2 book should be: '" + book2.getName() + "'", book2.getName(), bookCreated2.getName());
//
//        Book bookCreated5 = (Book) bs.getById(5);
//        assertEquals("book3 book should be: '" + book5.getName() + "'", book5.getName(), bookCreated5.getName());

    }

    @After
    public void tearDown() throws Exception {
        databaseManager.releaseConnection();
    }

}