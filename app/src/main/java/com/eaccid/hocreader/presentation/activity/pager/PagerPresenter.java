package com.eaccid.hocreader.presentation.activity.pager;

import android.util.Log;

import com.eaccid.hocreader.provider.db.WordListProvider;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.provider.translator.HocDictionary;
import com.eaccid.hocreader.provider.translator.TranslatedWord;
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
        HocDictionary hocDictionary = new HocDictionary(mView.getApplicationContext());
        boolean succeed = hocDictionary.addTranslatedWord(translatedWord);
        Log.i(logTAG, "Word translated status: " + succeed);
        dataManager.createOrUpdateWord(translatedWord.getWordFromContext(),//getWordBaseForm()
                translatedWord.getTranslation(),
                translatedWord.getContext(),
                succeed);
        addWordToWordListProvider(translatedWord.getWordBaseForm());
    }

    public void OnWordFromTextViewClicked(WordFromText wordFromText) {
        dataManager.setCurrentPageForAddingWord(wordFromText.getPageNumber());
    }

}
