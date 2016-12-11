package com.eaccid.bookreader.provider;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eaccid.hocreader.data.local.WordFilter;
import com.eaccid.hocreader.data.local.WordManager;
import com.eaccid.hocreader.data.local.db.entity.Word;

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
    protected static WordManager wordManager;

    public WordDatabaseDataProvider() {
        sessionWords = new ArrayList<>();
    }

    @Override
    public void fillDataList() {
        setDataList(getDataListByBook());
    }

    public void fillSessionDataList() {
        setDataList(getDataListByBookAndSessionWords());
    }

    public void updateSessionDataList() {
        setDataList(getDataListByBookAndSessionWords());
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

        if (sessionWords.contains(wordBaseName)) {
            return;
        }

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

    @Override
    public int undoLastRemoval() {
        Word word = (Word) getLastRemovedData().getObject();
        sessionWords.add(word.getName());
        //todo del from here

        wordManager.createOrUpdateWord(word.getName(), word.getTranslation(), word.getContext(), true);
        return super.undoLastRemoval();
    }

    @Override
    public void removeItem(int position) {

        ItemDataProvider item = getDataList().get(position);
        WordItemListUtils.removeItem(item);

        Word word = (Word) item.getObject();
        sessionWords.remove(word.getName());

        super.removeItem(position);
    }

    private static class WordItemListUtils implements Callable<List<ItemDataProvider>> {
        private WordFilter wordFilter;
        private List<String> words;
        private int fromCurrentPosition;

        private WordItemListUtils(WordFilter wordFilter, @Nullable List<String> words) {
            this.wordFilter = wordFilter;
            if (words == null)
                words = new ArrayList<>();
            this.words = words;
            switch (wordFilter) {
                case BY_BOOK_AND_EXCLUDED_WORD_COLLECTION:
                    this.fromCurrentPosition = words.size();
                    break;
                default:
                    this.fromCurrentPosition = 0;
                    break;
            }
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
            Word word = wordManager.getCurrentBooksWordByPage(wordBaseName);
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
            wordManager.setFilter(wordFilter);
            List<Word> wordsFromDB = wordManager.getAllWords(words, null);

            for (Word word : wordsFromDB) {
                ItemDataProvider itemDataProvider = new ItemDataProvider(fromCurrentPosition + dataList.size(), word);
                itemDataProvider.setLastAdded(wordFilter == WordFilter.BY_BOOK_AND_WORD_COLLECTION);
                dataList.add(itemDataProvider);
            }

            return dataList;
        }

        public static void removeItem(ItemDataProvider itemData) {
            Word word = (Word) itemData.getObject();
            wordManager.deleteWord(word);
        }
    }


    protected static void loadWordManager(Context context) {
        if (wordManager == null) {
            wordManager = new WordManager();
            wordManager.loadDatabaseManager(context);
        }
    }

    protected static void releaseWordManager() {
        if (wordManager != null) {
            wordManager.releaseDatabaseManager();
            wordManager = null;
        }
    }

}
