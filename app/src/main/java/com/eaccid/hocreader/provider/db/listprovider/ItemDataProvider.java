package com.eaccid.hocreader.provider.db.listprovider;

public interface ItemDataProvider {
    long getItemId();

    Object getObject();

    boolean isPinned();

    void setPinned(boolean pinned);

    boolean isLastAdded();

    void setLastAdded(boolean lastAdded);

}
