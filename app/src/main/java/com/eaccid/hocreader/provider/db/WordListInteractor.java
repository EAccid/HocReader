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
    private WordListProvider wordListProvider;

    public WordListInteractor(WordListProvider wordListProvider) {
        this.sessionWords = new ArrayList<>();
        this.wordListProvider = wordListProvider;
    }

    @Override
    public void fillDataList() {
        setDataList(getWordList());
    }

    @Override
    public int undoLastRemoval() {
        WordItem word = (WordItem) getLastRemovedData();
        sessionWords.add(word.getName());
        wordListProvider.createItemWord(word);
        return super.undoLastRemoval();
    }

    @Override
    public void removeItem(int position) {
        ItemDataProvider item = getDataList().get(position);
        wordListProvider.removeItem(item);
        Word word = (Word) item.getObject();
        sessionWords.remove(word.getName());
        super.removeItem(position);
    }

    public void addItem(String wordBaseName) {
        if (sessionWords.contains(wordBaseName)) {
            return;
        }
        WordItem item = (WordItem) wordListProvider.createItemWord(wordBaseName, sessionWords.size());
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
        return wordListProvider.getAll();
    }

    private List<ItemDataProvider> getDataListByBookAndSessionWords() {
        return wordListProvider.getAllFromDatabase(sessionWords);
    }

    private List<ItemDataProvider> addDataListByBookAndSessionWords() {
        return wordListProvider.addAllFromDatabase(sessionWords);
    }

}
