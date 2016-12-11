package com.eaccid.bookreader.activity.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.eaccid.bookreader.R;
import com.eaccid.hocreader.data.local.WordManager;
import com.eaccid.hocreader.data.local.db.entity.Word;

public class MemorizingService extends IntentService {

    private static final String NOTIFICATION_TAG = "WORD_MEMORIZING";
    private WordManager wordManager;
    private NotificationManager notificationManager;

    public MemorizingService() {
        super("memorizing-service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MemorizingService", "on create");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        wordManager = new WordManager();
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
        }
    }

    private void sendNotification() {

        // TODO create notification ID and separate random word fetching / work with db
        wordManager.loadDatabaseManager(this);
        Word word = wordManager.getRandomWord();
        if (word == null) return;

        Intent intent = new Intent(this, MemorizingActivity.class);
        intent.putExtra("word", word.getName());
        intent.putExtra("translation", word.getTranslation());

        createMemorizingNotification(intent, (int) word.getId(), word.getName());

    }

    private void createMemorizingNotification(Intent pendingIntent, int id, String textNotification) {

        final int REQUEST_CODE = 17;

        PendingIntent contentIntent =
        PendingIntent.getActivity(this, REQUEST_CODE,
                pendingIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_pets_memorizing_24px)
                        .setContentTitle(textNotification)
                        .setContentText("Do you remember?")
                        .setContentIntent(contentIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_TAG, id, builder.build());

        Log.i("MemorizingService", "notification has been created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MemorizingService", "on destroy");
        wordManager.releaseDatabaseManager();
    }
}
