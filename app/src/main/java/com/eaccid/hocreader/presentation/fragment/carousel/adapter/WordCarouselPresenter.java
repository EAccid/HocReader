package com.eaccid.hocreader.presentation.fragment.carousel.adapter;

import android.util.Log;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.fragment.carousel.WordsCarouselFragment;
import com.eaccid.hocreader.provider.db.WordCursorProvider;

import javax.inject.Inject;

public class WordCarouselPresenter implements BasePresenter<WordsCarouselFragment> {
    private final String logTAG = "WordCarouselPresenter";
    WordsCarouselFragment mView;

    @Inject
    AppDatabaseManager dataManager;

    @Override
    public void attachView(WordsCarouselFragment wordsCarouselFragment) {
        mView = wordsCarouselFragment;
        App.getWordListComponent().inject(this);
        Log.i(logTAG, "WordsCarouselFragment has been attached.");
    }

    @Override
    public void detachView() {
        Log.i(logTAG, "WordsCarouselFragment has been detached.");
        mView = null;
    }

    public WordCarouselRecyclerViewAdapter createWordCarouselRecyclerViewAdapter() {
        WordCarouselRecyclerViewAdapter adapter = new WordCarouselRecyclerViewAdapter(mView.getContext());
        WordCursorProvider wordCursorProvider = new WordCursorProvider();
        return (WordCarouselRecyclerViewAdapter) wordCursorProvider.createAdapterWithCursor(adapter, true);

    }
}
