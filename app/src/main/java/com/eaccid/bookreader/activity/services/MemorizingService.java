package com.eaccid.bookreader.activity.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.db.AppDatabaseManager;
import com.eaccid.bookreader.db.entity.Word;

import java.util.Timer;

public class MemorizingService extends IntentService {

    private static final String NOTIFICATION_TAG = "WORD_MEMORIZING";

    private NotificationManager notificationManager;
    private Timer timer;

    public MemorizingService() {
        super("memorizing-service");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.i("MemorizingService", "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("MemorizingService", "memorizing-service running");
        if (intent == null) return;

        String action = intent.getAction();

        switch (action) {
            case "ACTION_CREATE":
                sendNotification();
                break;
            case "ACTION_UPDATE":

                Word word = (Word) intent.getSerializableExtra("word");
                updateNotification(word);
                break;

        }



    }

    private void updateNotification(Word word) {
        AppDatabaseManager.loadDatabaseManager(this);
        createOrUpdateMemorizingNotification(word, false);
    }

    private void sendNotification() {
        AppDatabaseManager.loadDatabaseManager(this);
        // TODO create notification ID and separate random word fetching / work with db
        Word word = AppDatabaseManager.getRandomWord();
        if (word == null) {
            return;
        }
        createOrUpdateMemorizingNotification(word, true);

    }

    private void createOrUpdateMemorizingNotification(Word word, boolean w) {

        final int REQUEST_CODE = 17;

        Intent intentUpdate = new Intent(this, MemorizingService.class);
        intentUpdate.putExtra("word", word);
        intentUpdate.setAction("ACTION_UPDATE");

        PendingIntent contentIntent =
                PendingIntent.getService(this, REQUEST_CODE,
                        intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_pets_leo_training_24px)
                        .setContentTitle("Do you remember?")
                        .setContentIntent(contentIntent)
                        .setAutoCancel(false)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentText(w ? word.getName() : word.getTranslation());
        notificationManager.notify(NOTIFICATION_TAG, (int) word.getId(), builder.build());

        Log.i("MN", "Memorizing notification has been created / updated");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MemorizingService", "onDestroy");
        AppDatabaseManager.releaseDatabaseManager();
    }
}
