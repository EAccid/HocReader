package com.eaccid.hocreader.temp.provider.db.books;

import java.util.List;

public interface BookInteractor {
    void loadBooks(List<String> filePaths);

    String getCurrentBookPath();

    int getCurrentPage();

    void setCurrentPageForAddingWord(int pageNumber);

    void createOrUpdateBook(String filePath, String fileName);

    void setCurrentBookForAddingWord(String filePath);
}
