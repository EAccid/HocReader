package com.eaccid.hocreader.presentation.fragment.carousel;

import android.util.Log;
import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.activity.pager.PagerActivity;
import com.eaccid.hocreader.presentation.activity.pager.PagerPresenter;
import com.eaccid.hocreader.provider.db.WordCursorProvider;

public class WordCarouselPresenter implements BasePresenter<WordsCarouselFragment> {
    private final String logTAG = "WordCarouselPresenter";
    WordsCarouselFragment mView;
    private AppDatabaseManager dataManager;

    @Override
    public void attachView(WordsCarouselFragment wordsCarouselFragment) {
        mView = wordsCarouselFragment;
        setDataManager();
        Log.i(logTAG, "WordsCarouselFragment has been attached.");
    }

    @Override
    public void detachView() {
        Log.i(logTAG, "WordsCarouselFragment has been detached.");
        mView = null;
    }

    private void setDataManager() {
        PagerPresenter pp = ((PagerActivity) mView.getActivity()).getPresenter();
        dataManager = pp.getDataManager();
    }

    public WordCarouselRecyclerViewAdapter createWordCarouselRecyclerViewAdapter() {
        WordCarouselRecyclerViewAdapter adapter = new WordCarouselRecyclerViewAdapter(mView.getContext());
        WordCursorProvider wordCursorProvider = new WordCursorProvider(mView.getContext(), dataManager);
        return (WordCarouselRecyclerViewAdapter) wordCursorProvider.createAdapterWithCursor(adapter, true);

    }
}
