package com.eaccid.hocreader.presentation.activity.pager;

import android.util.Log;

import com.eaccid.hocreader.provider.db.WordListProvider;
import com.eaccid.hocreader.provider.wordgetter.WordFromText;
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
        Log.i(logTAG, "PagerActivity has been attached.");

        dataManager.loadDatabaseManager(mView);
        WordListProvider.setDataManager(dataManager);
        createOrUpdateCurrentBook();

    }

    @Override
    public void detachView() {
        dataManager.releaseDatabaseManager();
        WordListProvider.setDataManager(null);

        Log.i(logTAG, "PagerActivity has been detached.");
        mView = null;
    }

    public WordListProvider getDataProvider() {
        return wordListProvider;
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

    public void addWordToWordListProvider(String translatedWord) {
        wordListProvider.addWord(translatedWord);
        mView.notifyItemChanged();
    }

    public void onWordTranslated(TranslatedWord translatedWord) {
        ReaderDictionary readerDictionary = new ReaderDictionary(mView.getApplicationContext());
        boolean succeed = readerDictionary.addTranslatedWord(translatedWord);
        dataManager.createOrUpdateWord(translatedWord.getWordBaseForm(),
                translatedWord.getTranslation(),
                translatedWord.getContext(),
                succeed);
        addWordToWordListProvider(translatedWord.getWordBaseForm());
    }

    public void OnWordFromTextViewClicked(WordFromText wordFromText) {
        dataManager.setCurrentPageForAddingWord(wordFromText.getPageNumber());
    }

}
