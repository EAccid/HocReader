package com.eaccid.hocreader.provider.db.dataprovider;

import android.util.Log;

public class ItemDataProviderImpl implements ItemDataProvider {

    private final String logTAG = "ItemDataProviderImpl";
    private final long id;
    private final Object object;
    private boolean pinned;
    private boolean lastAdded;

    public ItemDataProviderImpl(int id, Object object) {
        this.id = id;
        this.object = object;
        Log.i(logTAG, "New data item (id=" + getItemId() + ") has been created");
    }

    @Override
    public long getItemId() {
        return id;
    }

    @Override
    public Object getObject() {
        return object;
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

}
