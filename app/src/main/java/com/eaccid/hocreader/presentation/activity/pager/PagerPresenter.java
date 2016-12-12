package com.eaccid.hocreader.presentation.activity.pager;

import android.util.Log;

import com.eaccid.hocreader.provider.WordListProvider;
import com.eaccid.bookreader.wordgetter.WordFromText;
import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.remote.ReaderDictionary;
import com.eaccid.hocreader.data.remote.TranslatedWord;
import com.eaccid.hocreader.presentation.BasePresenter;

public class PagerPresenter implements BasePresenter<PagerActivity> {
    private final String logTAG = "PagerPresenter";
    private PagerActivity mView;
    private AppDatabaseManager dataManager;
    private WordListProvider wordListProvider;

    public PagerPresenter() {
        dataManager = new AppDatabaseManager();
        wordListProvider = new WordListProvider();
    }

    @Override
    public void attachView(PagerActivity pagerActivity) {
        mView = pagerActivity;
        Log.i(logTAG, mView.getLocalClassName() + "' has been attached.");

        dataManager.loadDatabaseManager(mView);
        WordListProvider.setDataManager(dataManager);
        createOrUpdateCurrentBook();

    }

    @Override
    public void detachView() {
        dataManager.releaseDatabaseManager();
        WordListProvider.setDataManager(null);

        Log.i(logTAG, mView.getLocalClassName() + "' has been detached.");
        mView = null;
    }

    public WordListProvider getDataProvider() {
        return wordListProvider;
    }

    public AppDatabaseManager getDataManager() {
        return dataManager;
    }

    public void onWordTranslated(TranslatedWord translatedWord) {
        ReaderDictionary readerDictionary = new ReaderDictionary(mView.getApplicationContext());
        boolean succeed = readerDictionary.addTranslatedWord(translatedWord);
        dataManager.createOrUpdateWord(translatedWord.getWordBaseForm(),
                translatedWord.getTranslation(),
                translatedWord.getContext(),
                succeed);
        wordListProvider.addWord(translatedWord.getWordBaseForm());
    }

    public void OnWordFromTextViewCliched(WordFromText wordFromText) {
        dataManager.setCurrentPageForAddingWord(wordFromText.getPageNumber());
    }

    private void createOrUpdateCurrentBook() {
        String filePath = mView.getIntent().getStringExtra("filePath");
        String fileName = mView.getIntent().getStringExtra("fileName");

        dataManager.createOrUpdateBook(filePath, fileName);
        dataManager.setCurrentBookForAddingWord(filePath);
    }

}
