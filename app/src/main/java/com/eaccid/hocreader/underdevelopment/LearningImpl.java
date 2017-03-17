package com.eaccid.hocreader.underdevelopment;

import com.eaccid.hocreader.provider.db.words.WordItem;

public class LearningImpl implements Learning {
    private boolean learn = true;

    @Override
    public boolean isLearnByHart(WordItem wordItem) {
        return this.learn;
    }

    @Override
    public void setToLearn(WordItem wordItem, boolean learn) {
        this.learn = learn;
    }

    @Override
    public void isAlreadyLearned(WordItem wordItem) {

    }
}
