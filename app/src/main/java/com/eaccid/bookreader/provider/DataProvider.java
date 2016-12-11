package com.eaccid.bookreader.provider;

import android.util.Log;

import com.eaccid.hocreader.data.local.db.entity.Word;

import java.util.LinkedList;
import java.util.List;

public abstract class DataProvider {

    private List<ItemDataProvider> mData;
    private ItemDataProvider mLastRemovedData;
    private int mLastRemovedPosition = -1;

    public DataProvider() {
        mData = new LinkedList<>();
    }

    public abstract void fillDataList();

    public List<ItemDataProvider> getDataList() {
        return mData;
    }

    public void setDataList(List<ItemDataProvider> dataList) {
        mData = dataList;
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

    public static final class ItemDataProvider {

        private final String TAG = "updating ItemData";
        private final String text;
        private final long id;
        private boolean pinned;
        private boolean lastAdded;
        private Object object;

        ItemDataProvider(int id, Object object) {
            this.id = id;
            this.object = object;
            Log.i(TAG, "Adding new data item (id=" + getItemId() + ")");
            text = makeTestText(id, object);
        }

        private static String makeTestText(long id, Object object) {
            final StringBuilder sb = new StringBuilder();
            sb.append(id);
            sb.append(" - ");
            if (object instanceof Word) {
                sb.append(((Word) object).getName());
                sb.append(" - p.");
                sb.append(((Word) object).getPage());
                sb.append("\n book:  ");
                sb.append(((Word) object).getBook().getName());
            } else {
                sb.append(object.toString());
            }
            return sb.toString();
        }

        public String getTestText() {
            return text;
        }

        public long getItemId() {
            return id;
        }

        public String toString() {
            return text;
        }

        public boolean isPinned() {
            return pinned;
        }

        public void setPinned(boolean pinned) {
            this.pinned = pinned;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public boolean isLastAdded() {
            return lastAdded;
        }

        public void setLastAdded(boolean lastAdded) {
            this.lastAdded = lastAdded;
            if (lastAdded) {
                Log.i(TAG, "Last item (id=" + getItemId() + ") has been set.");
            }
        }

    }

    public Object getItemObject(int position) {
        return mData.get(position).getObject();
    }
}
