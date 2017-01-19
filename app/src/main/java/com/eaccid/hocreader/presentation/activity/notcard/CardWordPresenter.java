package com.eaccid.hocreader.presentation.activity.notcard;

import android.util.Log;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.provider.db.WordItem;
import com.eaccid.hocreader.provider.db.WordItemImpl;

import javax.inject.Inject;

public class CardWordPresenter implements BasePresenter<CardWordActivity> {

    private final String logTAG = "MainPresenter";
    private CardWordActivity mView;

    //TODO inject provider instead of manager
    @Inject
    AppDatabaseManager databaseManager;

    public CardWordPresenter() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void attachView(CardWordActivity cardWordActivity) {
        mView = cardWordActivity;
        Log.i(logTAG, "CardWordActivity has been attached.");
    }

    @Override
    public void detachView() {
        Log.i(logTAG, "CardWordActivity has been detached.");
        mView = null;
    }

    //todo temp solution, del after refactor setListenersToViewFromItem in View
    public WordItem getWordItem() {
        Word word = databaseManager.getWord(mView.getWord());
        if (word == null)
            word = new Word();
        WordItemImpl wordItem = new WordItemImpl(word);
        return wordItem;
    }
}
