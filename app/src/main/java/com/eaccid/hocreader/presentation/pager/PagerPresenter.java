package com.eaccid.hocreader.presentation.pager;

import android.util.Log;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.provider.db.books.BookInteractor;
import com.eaccid.hocreader.provider.db.words.WordListInteractor;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.provider.translator.HocDictionaryProvider;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.provider.translator.TranslatedWord;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PagerPresenter implements BasePresenter<PagerActivity> {
    private final String LOG_TAG = "PagerPresenter";
    private PagerActivity mView;

    @Inject
    BookInteractor bookInteractor;
    @Inject
    WordListInteractor wordListInteractor;

    @Override
    public void attachView(PagerActivity pagerActivity) {
        App.getWordListComponent().inject(this);
        mView = pagerActivity;
        Log.i(LOG_TAG, "PagerActivity has been attached.");
    }

    @Override
    public void detachView() {
        App.clearWordListComponent();
        Log.i(LOG_TAG, "PagerActivity has been detached.");
        mView = null;
    }

    public void createOrUpdateCurrentBook(String fileName, String filePath) {
        bookInteractor.createOrUpdateBook(filePath, fileName);
        bookInteractor.setCurrentBookForAddingWord(filePath);
    }

    public void onWordTranslated(TranslatedWord translatedWord) {
        new HocDictionaryProvider()
                .addTranslatedWord(translatedWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(succeed -> {
                    Log.i(LOG_TAG, "Dictionary updated status: " + succeed);
                    wordListInteractor.addItem(translatedWord.getWordFromContext(),
                            translatedWord.getTranslation(),
                            translatedWord.getContext(),
                            succeed);
                    mView.notifyDataSetChanged();
                }, Throwable::printStackTrace);
    }

    public void OnWordFromTextViewClicked(WordFromText wordFromText) {
        bookInteractor.setCurrentPageForAddingWord(wordFromText.getPageNumber());
        mView.showTranslationDialog(wordFromText);
    }

}
