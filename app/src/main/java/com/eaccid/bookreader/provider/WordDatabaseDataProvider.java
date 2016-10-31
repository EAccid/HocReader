package com.eaccid.bookreader.provider;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.eaccid.bookreader.db.AppDatabaseManager;
import com.eaccid.bookreader.db.WordFilter;
import com.eaccid.bookreader.db.entity.Word;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class WordDatabaseDataProvider extends DataProvider {

    private final String TAG = "words list";
    private List<String> sessionWords;

    public WordDatabaseDataProvider() {
        sessionWords = new ArrayList<>();
//        fillDataList();
    }

    @Override
    public void fillDataList() {
        setDataList(getDataListByBook());
    }

    public void fillSessionDataList() {
        setDataList(getDataListByBookAndSessionWords());
    }

    public void updateSessionDataList() {
        getDataList().addAll(addDataListByBookAndSessionWords());
    }

    private List<ItemDataProvider> getDataListByBook() {
        return WordItemListUtils.getAll();
    }

    private List<ItemDataProvider> getDataListByBookAndSessionWords() {
        return WordItemListUtils.getAllFromDatabase(sessionWords);
    }

    private List<ItemDataProvider> addDataListByBookAndSessionWords() {
        return WordItemListUtils.addAllFromDatabase(sessionWords);
    }

    public void addWord(String wordBaseName) {

        ItemDataProvider wordItem = WordItemListUtils.createItemWord(wordBaseName, sessionWords.size());
        if (wordItem == null) {
            Log.e(TAG, "Word has not been added to database.");
            return;
        }

        sessionWords.add(wordBaseName);
        int insertedPosition = 0;
        wordItem.setLastAdded(true);
        getDataList().add(insertedPosition, wordItem);

    }

    private static class WordItemListUtils implements Callable<List<ItemDataProvider>> {
        private WordFilter wordFilter;
        private List<String> words;

        private WordItemListUtils(WordFilter wordFilter, @Nullable List<String> words) {
            this.wordFilter = wordFilter;
            if (words == null)
                words = new ArrayList<>();
            this.words = words;
        }

        static List<ItemDataProvider> addAllFromDatabase(List<String> excludeWords) {
            return getWordItemByCurrentBookList(WordFilter.BY_BOOK_AND_EXCLUDED_WORD_COLLECTION, excludeWords);
        }

        static List<ItemDataProvider> getAllFromDatabase(List<String> words) {
            return getWordItemByCurrentBookList(WordFilter.BY_BOOK_AND_WORD_COLLECTION, words);
        }

        static List<ItemDataProvider> getAll() {
            return getWordItemByCurrentBookList(WordFilter.BY_BOOK, null);
        }

        @Nullable
        static ItemDataProvider createItemWord(String wordBaseName, int currentId) {
            Word word = AppDatabaseManager.getCurrentBooksWordByPage(wordBaseName);
            if (word == null) return null;
            return new DataProvider.ItemDataProvider(currentId, word);
        }


        private static List<ItemDataProvider> getWordItemByCurrentBookList(WordFilter wordFilter, @Nullable List<String> words) {
            List<DataProvider.ItemDataProvider> newDataList = new LinkedList<>();
            try {
                ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
                WordItemListUtils wordFromDBList = new WordItemListUtils(wordFilter, words);
                Future<List<ItemDataProvider>> future = executor.submit(wordFromDBList);
                newDataList = future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return newDataList;
        }

        @Override
        public List<ItemDataProvider> call() throws Exception {
            Log.i("TAG", "Updating words from database.");

            List<ItemDataProvider> dataList = new ArrayList<>();
            AppDatabaseManager.setFilter(wordFilter);
            List<Word> wordsFromDB = AppDatabaseManager.getAllWords(words, null);

            int fromPosition = words.size();

            for (Word word : wordsFromDB) {

                ItemDataProvider itemDataProvider = new ItemDataProvider(fromPosition + dataList.size(), word);
                itemDataProvider.setLastAdded(wordFilter == WordFilter.BY_BOOK_AND_WORD_COLLECTION);
                dataList.add(itemDataProvider);

            }
            return dataList;
        }

    }
}
