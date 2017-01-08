package com.eaccid.hocreader.provider.db;

import android.util.Log;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.provider.db.dataprovider.DataListProvider;
import com.eaccid.hocreader.provider.db.dataprovider.ItemDataProvider;

import java.util.ArrayList;
import java.util.List;

public class WordListProvider extends DataListProvider {

    private static final String logTAG = "WordListProvider";
    private List<String> sessionWords;

    public WordListProvider() {
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
            Log.e(logTAG, "Word has not been added to database.");
            return;
        }

        sessionWords.add(wordBaseName);

        int insertedPosition = 0;
        wordItem.setLastAdded(true);
        getDataList().add(insertedPosition, wordItem);

    }

    @Override
    public int undoLastRemoval() {
        WordProvider word = (WordProvider) getLastRemovedData();
        sessionWords.add(word.getName());
        WordItemListUtils.createItemWord(word);
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

}
