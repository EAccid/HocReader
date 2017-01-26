package com.eaccid.hocreader.temp.provider.db.words.listprovider;

import java.util.LinkedList;
import java.util.List;

public abstract class DataListProvider {

    private List<ItemDataProvider> mData;
    private ItemDataProvider mLastRemovedData;
    private int mLastRemovedPosition = -1;

    public DataListProvider() {
        mData = new LinkedList<>();
    }

    public abstract void populateDataList();

    public List<ItemDataProvider> getDataList() {
        return mData;
    }

    public void setDataList(List<ItemDataProvider> dataList) {
        mData = dataList;
    }

    public void clearDataList() {
        mData = new LinkedList<>();
    }

    public int getCount() {
        return mData.size();
    }

    public ItemDataProvider getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }
        return mData.get(index);
    }

    public int undoLastRemoval() {
        if (mLastRemovedData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = mData.size();
            }
            mData.add(insertedPosition, mLastRemovedData);
            mLastRemovedData = null;
            mLastRemovedPosition = -1;
            return insertedPosition;
        } else {
            return -1;
        }
    }

    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final ItemDataProvider removedItem = mData.remove(position);
        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;
    }

    public ItemDataProvider getLastRemovedData() {
        return mLastRemovedData;
    }

}
