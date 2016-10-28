package com.eaccid.bookreader.provider;

import com.eaccid.bookreader.db.entity.Word;

import java.util.LinkedList;
import java.util.List;

public class WordDataProvider {

    private List<WordData> mData;
    private WordData mLastRemovedData;
    private int mLastRemovedPosition = -1;

    public WordDataProvider() {
        fillDataList();
    }

    public void fillDataList() {
        mData = new LinkedList<>();
        List<Word> words = AppDatabaseManager.getAllWords(true);
        for (Word word : words
                ) {
            mData.add(new WordData(mData.size(), word));
        }
    }

    // work with VIEW LIST

    public int getCount() {
        return mData.size();
    }

    public WordData getItem(int index) {
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
        final WordData removedItem = mData.remove(position);

        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;
    }

    //WORD DATA ITEM

    public static final class WordData {

        private final String text;

        private final long id;
        private boolean pinned;
        private Word word;

        WordData(int id, Word word) {
            this.id = id;
            this.word = word;
            text = makeTempTestText(id, word);
        }

        private static String makeTempTestText(long id, Word word) {
            final StringBuilder sb = new StringBuilder();
            sb.append(id);
            sb.append(" - ");
            sb.append(word.getWord());
            sb.append(" - p.");
            sb.append(word.getPage());
            return sb.toString();
        }

        public String getTempTestText() {
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

    }

}
