package com.eaccid.hocreader.data.local;

import com.eaccid.hocreader.data.local.db.entity.Book;

import java.util.List;

public interface BookReaderMode {

    Book getCurrentBook();

    int getCurrentPage();

    void setCurrentBookForAddingWord(String filePath);

    void setCurrentPageForAddingWord(int pageNumber);

    void updateBooks(List<String> bookpaths);

    List<Book> getAllBooks();

    void createOrUpdateBook(String bookPath, String bookName);
}
