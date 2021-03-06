package com.eaccid.hocreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.eaccid.hocreader.data.local.db.entity.Book;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.data.local.db.service.BookDaoService;
import com.eaccid.hocreader.data.local.db.service.DatabaseHelper;
import com.eaccid.hocreader.data.local.db.service.DatabaseManager;
import com.eaccid.hocreader.data.local.db.service.WordDaoService;
import com.j256.ormlite.table.TableUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
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
        TableUtils.createTable(databaseHelper.getConnectionSource(), Word.class);
        TableUtils.createTable(databaseHelper.getConnectionSource(), Book.class);
    }

    @Test
    public void testCreateOrUpdateWord() throws Exception {

        Book book = new Book("/storage/sdcard/Download/book1 test.txt", "book1 test.txt");
        Book book3 = new Book("/storage/sdcard/Download/book3 test.txt", "book3 test.txt");

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

        Word word2Created =  ws.getAllByWordName("devastate").get(0);
        assertEquals("Created word2 should be: '" + word1.getName() + "'", word2.getName(), word2Created.getName());


        Word word3Created = ws.getAllByWordName("persist").get(0);
        assertEquals("Created word3 should be in book: '" + book3.getName() + "'", book3.getName(), word3Created.getBook().getName());

    }

    @Test
    public void testAmountWords() throws Exception {

        Book book1 = new Book("/storage/sdcard/Download/book1 test.txt", "book1 test.txt");
        Book book2 = new Book("/storage/sdcard/Download/book2 test.txt", "book2 test.txt");
        Book book3 = new Book("/storage/sdcard/Download/book3 test.txt", "book3 test.txt");

        BookDaoService bs = databaseManager.getBookService();
        bs.createOrUpdate(book1);
        bs.createOrUpdate(book2);
        bs.createOrUpdate(book3);

        Word word1 = new Word("mad", "сумасшедший", "mad in some context", 13, book1, false);
        Word word2 = new Word("devastate", "опустошать", "devastate in some context", 13, book1, false);
        Word word3 = new Word("persist", "упорствовать", "persist in some context", 103, book2, false);
        Word word4 = new Word("segregate", "изолировать", "segregate in some context", 103, book2, false);
        Word word5 = new Word("designator", "указатель или обозначение", "designator in some context", 203, book3, false);

        WordDaoService ws = databaseManager.getWordService();

        ws.createOrUpdate(word1);
        ws.createOrUpdate(word2);
        ws.createOrUpdate(word3);
        ws.createOrUpdate(word4);
        ws.createOrUpdate(word5);

        int amountWords = ws.getAll().size();
        assertEquals("Words amount should  be 5", 5, amountWords);

        assertFalse("Word should not be enabled online", ws.getAllByWordName("segregate").get(0).isEnabledOnline());

        word4.setEnabledOnline(true);
        ws.createOrUpdate(word4);

        amountWords = ws.getAll().size();
        assertEquals("Words amount should  be 5", 5, amountWords);

        assertTrue("Word should be enabled online", ws.getAllByWordName("segregate").get(0).isEnabledOnline());

    }

    @Test
    public void testDeleteWord() throws Exception {

        Book book1 = new Book("/storage/sdcard/Download/book1 test.txt", "book1 test.txt");
        Book book2 = new Book("/storage/sdcard/Download/book2 test.txt", "book2 test.txt");
        Book book3 = new Book("/storage/sdcard/Download/book3 test.txt", "book3 test.txt");

        BookDaoService bs = databaseManager.getBookService();
        bs.createOrUpdate(book1);
        bs.createOrUpdate(book2);
        bs.createOrUpdate(book3);

        Word word1 = new Word("mad", "сумасшедший", "mad in some context", 13, book1, false);
        Word word2 = new Word("devastate", "опустошать", "devastate in some context", 13, book1, false);
        Word word3 = new Word("persist", "упорствовать", "persist in some context", 103, book2, false);
        Word word4 = new Word("segregate", "изолировать", "segregate in some context", 103, book2, false);
        Word word5 = new Word("designator", "указатель или обозначение", "designator in some context", 203, book3, false);

        WordDaoService ws = databaseManager.getWordService();

        ws.createOrUpdate(word1);
        ws.createOrUpdate(word2);
        ws.createOrUpdate(word3);
        ws.createOrUpdate(word4);
        ws.createOrUpdate(word5);

        ws.delete(word3);
        ws.delete(word4);

        int amountWords = ws.getAll().size();

        assertEquals("Words amount should  be 3", 3, amountWords);


        Word word2Created =  ws.getAllByWordName("devastate").get(0);
        assertEquals("Created word2 should be 'devastate'" , word2.getName(), word2Created.getName());

        Word word5Created = ws.getAllByWordName("designator").get(0);
        assertEquals("Created word5 should be in book: '" + book3.getName() + "'", book3.getName(), word5Created.getBook().getName());


       int word3CreatedSize = ws.getAllByWordName("persist").size();
        assertEquals("list of persist words should be 0", 0, word3CreatedSize);

    }

    @Test
    public void testForeignCollection() throws Exception {

        Book book1 = new Book("/storage/sdcard/Download/book1 test.txt", "book1 test.txt");
        Book book2 = new Book("/storage/sdcard/Download/book2 test.txt", "book2 test.txt");
        BookDaoService bs = databaseManager.getBookService();
        bs.createOrUpdate(book1);
        bs.createOrUpdate(book2);


        Word word1 = new Word("mad", "сумасшедший", "mad in some context", 13, book1, false);
        WordDaoService ws = databaseManager.getWordService();
        ws.createOrUpdate(word1);

        bs.delete(book1);

        Word word1Created = ws.getAllByWordName("mad").get(0);

        assertNull("Book should be null.", word1Created.getBook());

        word1Created.setBook(book2);
        ws.createOrUpdate(word1Created);

        word1Created = ws.getAllByWordName("mad").get(0);
        assertEquals("Created word3 should be in book: '" + book2.getName() + "'", book2.getName(), word1Created.getBook().getName());

    }

    @After
    public void tearDown() throws Exception {
        TableUtils.dropTable(databaseManager.getDatabaseHelper().getConnectionSource(), Word.class, true);
        TableUtils.dropTable(databaseManager.getDatabaseHelper().getConnectionSource(), Book.class, true);
    }

}