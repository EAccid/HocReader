package com.eaccid.hocreader.underdevelopment;

import com.eaccid.hocreader.provider.db.words.WordItem;

public interface Learning {
    boolean isLearnByHart(WordItem wordItem);

    void setToLearn(WordItem wordItem, boolean learn);

    void isAlreadyLearned(WordItem wordItem);
}
