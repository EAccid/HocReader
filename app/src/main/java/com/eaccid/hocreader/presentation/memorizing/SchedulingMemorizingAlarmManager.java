package com.eaccid.hocreader.presentation.memorizing;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SchedulingMemorizingAlarmManager {
    private Context context;

    public SchedulingMemorizingAlarmManager(Context context) {
        this.context = context;
    }

    public void scheduleAlarm(long triggerAtMillis) {
        if (triggerAtMillis == -1) {
            cancelAlarm();
            return;
        }
        startAlarm(triggerAtMillis);
    }

    private void startAlarm(long triggerAtMillis) {
        Intent intent = new Intent(context, MemorizingAlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, MemorizingAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis,
                triggerAtMillis, pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarm == null) return;
        Intent intent = new Intent(context, MemorizingAlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, MemorizingAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarm.cancel(pendingIntent);
    }

}
