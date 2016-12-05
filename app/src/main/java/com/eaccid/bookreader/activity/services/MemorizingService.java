package com.eaccid.bookreader.activity.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.db.AppDatabaseManager;
import com.eaccid.bookreader.db.entity.Word;

import java.util.Timer;
import java.util.TimerTask;

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
        sendNewNotification();

    }

    private void sendNewNotification() {
        AppDatabaseManager.loadDatabaseManager(this);
        // TODO create notification ID and separate random word fetching / work with db
        Word word = AppDatabaseManager.getRandomWord();
        if (word == null) {
            return;
        }
        createOrUpdateMemorizingNotification((int) word.getId(), word.getName());

        Log.i("MN", "Memorizing notification has been created / updated");
    }

    private void createOrUpdateMemorizingNotification(int id, String textNotification) {

        final int REQUEST_CODE = 17;

        PendingIntent contentIntent =
                PendingIntent.getActivity(this, REQUEST_CODE,
                        new Intent(this, MemorizingService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_pets_leo_training_24px)
                        .setContentTitle("Do you remember?")
                        .setContentText(textNotification);
        notificationManager.notify(NOTIFICATION_TAG, id, mBuilder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MemorizingService", "onDestroy");
        AppDatabaseManager.releaseDatabaseManager();
    }
}
