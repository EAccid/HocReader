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
    private WordListFetcher wordListFetcher;

    public WordListInteractor(WordListFetcher wordListFetcher) {
        this.sessionWords = new ArrayList<>();
        this.wordListFetcher = wordListFetcher;
    }

    @Override
    public void fillDataList() {
        setDataList(getWordList());
    }

    @Override
    public int undoLastRemoval() {
        WordProvider word = (WordProvider) getLastRemovedData();
        sessionWords.add(word.getName());
        wordListFetcher.createItemWord(word);
        return super.undoLastRemoval();
    }

    @Override
    public void removeItem(int position) {
        ItemDataProvider item = getDataList().get(position);
        wordListFetcher.removeItem(item);
        Word word = (Word) item.getObject();
        sessionWords.remove(word.getName());
        super.removeItem(position);
    }

    public void addItem(String wordBaseName) {
        if (sessionWords.contains(wordBaseName)) {
            return;
        }
        WordProvider item = (WordProvider) wordListFetcher.createItemWord(wordBaseName, sessionWords.size());
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
        return wordListFetcher.getAll();
    }

    private List<ItemDataProvider> getDataListByBookAndSessionWords() {
        return wordListFetcher.getAllFromDatabase(sessionWords);
    }

    private List<ItemDataProvider> addDataListByBookAndSessionWords() {
        return wordListFetcher.addAllFromDatabase(sessionWords);
    }

}
