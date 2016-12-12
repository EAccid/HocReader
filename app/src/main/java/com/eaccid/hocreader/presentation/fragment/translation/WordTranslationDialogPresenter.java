package com.eaccid.hocreader.presentation.fragment.translation;

import android.util.Log;

import com.eaccid.hocreader.presentation.BasePresenter;

public class WordTranslationDialogPresenter implements BasePresenter<WordTranslationDialogFragment>{
    private final String logTAG = "TranslationPresenter";
    private WordTranslationDialogFragment mView;

    public WordTranslationDialogPresenter() {

    }

    @Override
    public void attachView(WordTranslationDialogFragment wordTranslationDialogFragment) {
        mView = wordTranslationDialogFragment;
        Log.i(logTAG, "TranslationPresenter has been attached.");
    }

    @Override
    public void detachView() {
        Log.i(logTAG, "TranslationPresenter has been detached.");
        mView = null;
    }
}
