package com.eaccid.hocreader.presentation.memorizing;

import android.content.Intent;
import android.util.Log;

import com.eaccid.hocreader.App;
import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.underdevelopment.cardremember.CardWordActivity;

import javax.inject.Inject;

public class MemorizingPresenter  implements BasePresenter<MemorizingService> {

    private final String logTAG = "MemorizingService";
    private MemorizingService mView;

    @Inject
    AppDatabaseManager dataManager;

    @Override
    public void attachView(MemorizingService memorizingService) {
        mView = memorizingService;
        App.get(mView).getAppComponent().inject(this);
        Log.i(logTAG, "MemorizingService has been attached.");
    }

    @Override
    public void detachView() {
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

        //TODO refactor to work with WordItem
        Word word = dataManager.getRandomWord();
        if (word == null) return;

        Intent intent = new Intent(mView, CardWordActivity.class);
        intent.putExtra("word", word.getName());
        intent.putExtra("translation", word.getTranslation());

        mView.createMemorizingNotification(intent, (int) word.getId(), word.getName());

        Log.i(logTAG, "Notification has been created.");
    }

}
