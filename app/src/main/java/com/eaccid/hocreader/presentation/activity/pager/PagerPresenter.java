package com.eaccid.hocreader.presentation.activity.pager;

import android.util.Log;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.provider.db.WordListInteractor;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.provider.translator.HocDictionaryProvider;
import com.eaccid.hocreader.underdevelopment.TranslatedWord;
import com.eaccid.hocreader.presentation.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PagerPresenter implements BasePresenter<PagerActivity> {
    private final String LOG_TAG = "PagerPresenter";
    private PagerActivity mView;

    @Inject
    AppDatabaseManager dataManager;
    @Inject
    WordListInteractor wordListInteractor;

    @Override
    public void attachView(PagerActivity pagerActivity) {
        App.getWordListComponent().inject(this);
        mView = pagerActivity;
        Log.i(LOG_TAG, "PagerActivity has been attached.");
        createOrUpdateCurrentBook();
    }

    @Override
    public void detachView() {
        App.clearWordListComponent();
        Log.i(LOG_TAG, "PagerActivity has been detached.");
        mView = null;
    }

    public AppDatabaseManager getDataManager() {
        return dataManager;
    }

    private void createOrUpdateCurrentBook() {
        String filePath = mView.getIntent().getStringExtra("filePath");
        String fileName = mView.getIntent().getStringExtra("fileName");

        dataManager.createOrUpdateBook(filePath, fileName);
        dataManager.setCurrentBookForAddingWord(filePath);
    }

    public void addWord(String word) {
        wordListInteractor.addItem(word);
        mView.notifyDataSetChanged();
    }

    public void onWordTranslated(TranslatedWord translatedWord) {
        new HocDictionaryProvider()
                .addTranslatedWord(translatedWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(succeed -> {
                    Log.i(LOG_TAG, "Dictionary updated status: " + succeed);
                    dataManager.createOrUpdateWord(translatedWord.getWordFromContext(),
                            translatedWord.getTranslation(),
                            translatedWord.getContext(),
                            succeed);
                    addWord(translatedWord.getWordFromContext());
                }, Throwable::printStackTrace);
    }

    public void OnWordFromTextViewClicked(WordFromText wordFromText) {
        dataManager.setCurrentPageForAddingWord(wordFromText.getPageNumber());
    }

}
