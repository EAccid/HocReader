package com.eaccid.bookreader.activity.main;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.db.AppDatabaseManager;
import com.eaccid.bookreader.db.entity.Word;

import java.util.Timer;
import java.util.TimerTask;

public class MemorizeTimeService extends Service { //IntentService

    private static final String NOTIFICATION_TAG = "WORD_MEMORIZING";
    private static final long NOTIFY_INTERVAL = 5 * 60 * 1000;
    private NotificationManager notificationManager;
    private Timer timer;


















    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("TestService", "onCreate: notification=" + notificationManager + ", timer=" + timer);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        timer = new Timer();

        timer.scheduleAtFixedRate(new NotificationTimerTask(), 0, NOTIFY_INTERVAL);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_pets_leo_training_24px)
                        .setContentTitle("Do you remember?")
                        .setContentText(textNotification);
        notificationManager.notify(NOTIFICATION_TAG, id, mBuilder.build());
    }

    private class NotificationTimerTask extends TimerTask {
        private Thread thread;

        NotificationTimerTask() {
            thread = new Thread(this, "Memorize notification");
            thread.start();
        }

        @Override
        public void run() {
            sendNewNotification();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppDatabaseManager.releaseDatabaseManager();
    }
}
