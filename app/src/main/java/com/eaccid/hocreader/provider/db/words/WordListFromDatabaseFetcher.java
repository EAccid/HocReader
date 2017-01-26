package com.eaccid.hocreader.provider.db.words;

import android.support.annotation.Nullable;
import android.util.Log;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.local.WordFilter;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.provider.db.words.listprovider.ItemDataProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class WordListFromDatabaseFetcher implements Callable<List<ItemDataProvider>> {
    private final static String LOG_TAG = "WordListFetcher";
    private WordFilter wordFilter;
    private List<String> words;
    private int fromIndex;

    private AppDatabaseManager dataManager;

    public WordListFromDatabaseFetcher(AppDatabaseManager dataManager) {
        this.dataManager = dataManager;
    }

    List<ItemDataProvider> addAllFromDatabase(List<String> excludeWords) {
        return getWordItemByCurrentBookList(WordFilter.BY_BOOK_AND_EXCLUDED_WORD_COLLECTION, excludeWords);
    }

    List<ItemDataProvider> getAllFromDatabase(List<String> words) {
        return getWordItemByCurrentBookList(WordFilter.BY_BOOK_AND_WORD_COLLECTION, words);
    }

    List<ItemDataProvider> getAll() {
        return getWordItemByCurrentBookList(WordFilter.BY_BOOK, null);
    }

    @Nullable
    ItemDataProvider createItemWord(String wordBaseName, int currentId, String translation, String context, Boolean succeed) {
        dataManager.createOrUpdateWord(wordBaseName,
                translation,
                context,
                succeed);
        Word word = dataManager.getCurrentBooksWordByPage(wordBaseName);
        if (word == null) return null;
        return new WordItemImpl(currentId, word);
    }

    private List<ItemDataProvider> getWordItemByCurrentBookList(WordFilter wordFilter, @Nullable List<String> words) {
        List<ItemDataProvider> newDataList = new LinkedList<>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        try {
            setTemp(wordFilter, words);
            Future<List<ItemDataProvider>> future = executor.submit(this);
            newDataList = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        return newDataList;
    }

    private void setTemp(WordFilter wordFilter, List<String> words) {
        this.wordFilter = wordFilter;
        if (words == null)
            words = new ArrayList<>();
        this.words = words;
        switch (wordFilter) {
            case BY_BOOK_AND_EXCLUDED_WORD_COLLECTION:
                this.fromIndex = words.size();
                break;
            default:
                this.fromIndex = 0;
                break;
        }
    }

    @Override
    public List<ItemDataProvider> call() throws Exception {
        Log.i(LOG_TAG, "Updating words from database... start");

        List<ItemDataProvider> dataList = new ArrayList<>();
        dataManager.setFilter(wordFilter);
        List<Word> wordsFromDB = dataManager.getAllWords(words, null);

        for (Word word : wordsFromDB) {
            ItemDataProvider itemDataProvider = new WordItemImpl(fromIndex + dataList.size(), word);
            itemDataProvider.setLastAdded(wordFilter == WordFilter.BY_BOOK_AND_WORD_COLLECTION);
            dataList.add(itemDataProvider);
        }
        Log.i(LOG_TAG, "Updating words from database... end");
        return dataList;
    }

    void removeItem(ItemDataProvider itemData) {
        Word word = (Word) itemData.getObject();
        dataManager.deleteWord(word);
    }

    void createItemWord(WordItemImpl word) {
        dataManager.createOrUpdateWord(word.getWordFromText(), word.getTranslation(), word.getContext(), true);
    }

    public boolean removeItems() {
        Log.i(LOG_TAG, "Deleting words from database...");
        return dataManager.deleteWords(WordFilter.BY_BOOK);
    }

}
