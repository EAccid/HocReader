package com.eaccid.hocreader.presentation.service;

import android.content.Intent;
import android.util.Log;

import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.activity.notcard.CardWordActivity;

public class MemorizingPresenter  implements BasePresenter<MemorizingService> {

    private final String logTAG = "MemorizingService";
    private MemorizingService mView;
    private AppDatabaseManager dataManager; //TODO inject

    public MemorizingPresenter() {
        dataManager = new AppDatabaseManager();
    }

    @Override
    public void attachView(MemorizingService memorizingService) {
        mView = memorizingService;
        dataManager.loadDatabaseManager(mView);
        Log.i(logTAG, "MemorizingService has been attached.");
    }

    @Override
    public void detachView() {
        dataManager.releaseDatabaseManager();
        Log.i(logTAG, "MemorizingService has been detached.");
        mView = null;
    }

    public void onHandleIntent(String action) {
        Log.i(logTAG, "It's running.");
        switch (action) {
            case "ACTION_CREATE":
                sendNotification();
                break;
        }
    }

    private void sendNotification() {

        //TODO refactor to work with WordProvider
        Word word = dataManager.getRandomWord();
        if (word == null) return;

        Intent intent = new Intent(mView, CardWordActivity.class);
        intent.putExtra("word", word.getName());
        intent.putExtra("translation", word.getTranslation());

        mView.createMemorizingNotification(intent, (int) word.getId(), word.getName());

        Log.i(logTAG, "Notification has been created.");
    }


}
