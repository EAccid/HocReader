package com.eaccid.bookreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.eaccid.hocreader.data.local.db.entity.Book;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.data.local.db.service.BookDaoService;
import com.eaccid.hocreader.data.local.db.service.DatabaseManager;
import com.j256.ormlite.table.TableUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class BookDaoServiceTest {

    private DatabaseManager databaseManager;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        Method method = DatabaseManager.class.getDeclaredMethod("getTestInstance", Context.class, String.class);
        method.setAccessible(true);
        databaseManager = (DatabaseManager) method.invoke(method, appContext, "test_hr.db");

        TableUtils.createTable(databaseManager.getDatabaseHelper().getConnectionSource(), Word.class);
        TableUtils.createTable(databaseManager.getDatabaseHelper().getConnectionSource(), Book.class);
    }

    @Test
    public void testCreateOrUpdateBook() throws Exception {

        BookDaoService bs = databaseManager.getBookService();

        Book book = new Book("/storage/sdcard/Download/book test.txt","book test.txt");
        assertTrue("Book has bean created.", bs.createOrUpdate(book));

        Book bookCreated = (Book) bs.getById(book.getPath());
        assertEquals("Created book should be: '" + book.getName() + "'", book.getName(), bookCreated.getName());

        book.setName("same book");
        bs.createOrUpdate(book);

        Book bookCreated1 = (Book) bs.getById(book.getPath());
        assertEquals("Created book should be: '" + book.getName() + "'", book.getName(), bookCreated1.getName());
    }

    @Test
    public void testAmountBooks() throws Exception {

        BookDaoService bs = databaseManager.getBookService();

        Book book1 = new Book("/storage/sdcard/Download/book test 1.txt", "book test 1 .txt");
        Book book2 = new Book("/storage/sdcard/Download/book test 2.txt", "book test 2 .txt");
        Book book3 = new Book("/storage/sdcard/Download/book test 3.txt", "book test 3 .txt");
        Book book4 = new Book("/storage/sdcard/Download/book test 4.txt", "book test 4 .txt");
        Book book5 = new Book("/storage/sdcard/Download/book test 5.txt", "book test 5 .txt");

        bs.createOrUpdate(book1);
        bs.createOrUpdate(book2);
        bs.createOrUpdate(book3);
        bs.createOrUpdate(book4);
        bs.createOrUpdate(book5);

        int amountBooks = bs.getAll().size();

        assertEquals("Books amount should  be 5", 5, amountBooks);

    }

    @Test
    public void testDeleteBook() throws Exception {

        BookDaoService bs = databaseManager.getBookService();

        Book book1 = new Book("/storage/sdcard/Download/book test 1.txt", "book test 1 .txt");
        Book book2 = new Book("/storage/sdcard/Download/book test 2.txt", "book test 2 .txt");
        Book book3 = new Book("/storage/sdcard/Download/book test 3.txt", "book test 3 .txt");
        Book book4 = new Book("/storage/sdcard/Download/book test 4.txt", "book test 4 .txt");
        Book book5 = new Book("/storage/sdcard/Download/book test 5.txt", "book test 5 .txt");

        bs.createOrUpdate(book1);
        bs.createOrUpdate(book2);
        bs.createOrUpdate(book3);
        bs.createOrUpdate(book4);
        bs.createOrUpdate(book5);

        bs.delete(book3);
        bs.delete(book4);

        int amountBooks = bs.getAll().size();

        assertEquals("Books amount should  be 3", 3, amountBooks);

        Book bookCreated1 = (Book) bs.getById(book1.getPath());
        assertEquals("book1 book should be: '" + book1.getName() + "'", book1.getName(), bookCreated1.getName());

        Book bookCreated2 = (Book) bs.getById(book2.getPath());
        assertEquals("book2 book should be: '" + book2.getName() + "'", book2.getName(), bookCreated2.getName());

        Book bookCreated5 = (Book) bs.getById(book5.getPath());
        assertEquals("book3 book should be: '" + book5.getName() + "'", book5.getName(), bookCreated5.getName());

    }

    @After
    public void tearDown() throws Exception {

        TableUtils.dropTable(databaseManager.getDatabaseHelper().getConnectionSource(), Word.class, true);
        TableUtils.dropTable(databaseManager.getDatabaseHelper().getConnectionSource(), Book.class, true);
//        databaseManager.releaseConnection();

    }

}