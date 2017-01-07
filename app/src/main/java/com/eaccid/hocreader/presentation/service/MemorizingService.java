package com.eaccid.hocreader.presentation.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.BaseView;

public class MemorizingService extends IntentService implements BaseView {

    private static MemorizingPresenter mPresenter;
    private static final String NOTIFICATION_TAG = "WORD_MEMORIZING";
    private NotificationManager notificationManager;

    public MemorizingService() {
        super("memorizing-service");
    }

    @Override
    public BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mPresenter == null) mPresenter = new MemorizingPresenter();
        mPresenter.attachView(this);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;
        mPresenter.onHandleIntent(intent.getAction());
    }

    public void createMemorizingNotification(Intent pendingIntent, int id, String text) {

        final int REQUEST_CODE = id+1;
        PendingIntent contentIntent =
        PendingIntent.getActivity(this, REQUEST_CODE, pendingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_pets_white_24px)
                        .setContentTitle(getString(R.string.memorizing_notification_title))
                        .setContentText(text)
                        .setContentIntent(contentIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_TAG, id, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
