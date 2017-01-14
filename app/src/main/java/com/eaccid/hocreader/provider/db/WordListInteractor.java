package com.eaccid.hocreader.provider.db;

import android.util.Log;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.provider.db.listprovider.DataListProvider;
import com.eaccid.hocreader.provider.db.listprovider.ItemDataProvider;
import java.util.ArrayList;
import java.util.List;

public class WordListInteractor extends DataListProvider {

    private static final String logTAG = "WordListInteractor";
    private List<String> sessionWords;
    private WordListFromDatabaseFetcher wordListFromDatabaseFetcher;

    public WordListInteractor(WordListFromDatabaseFetcher wordListFromDatabaseFetcher) {
        this.sessionWords = new ArrayList<>();
        this.wordListFromDatabaseFetcher = wordListFromDatabaseFetcher;
    }

    @Override
    public void populateDataList() {
        setDataList(getWordList());
    }

    @Override
    public int undoLastRemoval() {
        WordProviderImpl word = (WordProviderImpl) getLastRemovedData();
        sessionWords.add(word.getWordFromText());
        wordListFromDatabaseFetcher.createItemWord(word);
        return super.undoLastRemoval();
    }

    @Override
    public void removeItem(int position) {
        ItemDataProvider item = getDataList().get(position);
        wordListFromDatabaseFetcher.removeItem(item);
        Word word = (Word) item.getObject();
        sessionWords.remove(word.getName());
        super.removeItem(position);
    }

    public void addItem(String wordBaseName) {
        if (sessionWords.contains(wordBaseName)) {
            return;
        }
        WordProviderImpl item = (WordProviderImpl) wordListFromDatabaseFetcher.createItemWord(wordBaseName, sessionWords.size());
        if (item == null) {
            Log.i(logTAG, "Word '" + wordBaseName + "' has not been added to database.");
            return;
        }
        sessionWords.add(wordBaseName);
        int insertedPosition = 0;
        item.setLastAdded(true);
        getDataList().add(insertedPosition, item);
    }

    public void fillSessionDataList() {
        setDataList(getDataListByBookAndSessionWords());
    }

    public void updateSessionDataList() {
        setDataList(getDataListByBookAndSessionWords());
        getDataList().addAll(addDataListByBookAndSessionWords());
    }

    private List<ItemDataProvider> getWordList() {
        return wordListFromDatabaseFetcher.getAll();
    }

    private List<ItemDataProvider> getDataListByBookAndSessionWords() {
        return wordListFromDatabaseFetcher.getAllFromDatabase(sessionWords);
    }

    private List<ItemDataProvider> addDataListByBookAndSessionWords() {
        return wordListFromDatabaseFetcher.addAllFromDatabase(sessionWords);
    }

}
