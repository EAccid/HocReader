package com.eaccid.hocreader.provider.db;

import android.util.Log;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.provider.db.listprovider.ItemDataProvider;

public class WordItem implements ItemDataProvider {

    private final String logTAG = "WordItem";
    private final long id;
    private final Word word;
    private boolean pinned;
    private boolean lastAdded;

    public WordItem(int id, Word word) {
        this.id = id;
        this.word = word;
        Log.i(logTAG, "New data item (id=" + getItemId() + ") has been created");
    }

    @Override
    public long getItemId() {
        return id;
    }

    @Override
    public Object getObject() {
        return word;
    }

    @Override
    public boolean isPinned() {
        return pinned;
    }

    @Override
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    @Override
    public boolean isLastAdded() {
        return lastAdded;
    }

    @Override
    public void setLastAdded(boolean lastAdded) {
        this.lastAdded = lastAdded;
        if (lastAdded) {
            Log.i(logTAG, "Last item (id=" + getItemId() + ") has been set.");
        }
    }

    public String getName() {
        return word.getName();
    }

    public String getTranslation() {
        return word.getTranslation();
    }

    public String getContext() {
        return word.getContext();
    }

}
