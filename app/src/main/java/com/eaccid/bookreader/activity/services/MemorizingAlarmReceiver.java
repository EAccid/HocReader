package com.eaccid.bookreader.activity.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MemorizingAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 13;

    public MemorizingAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentCreate = new Intent(context, MemorizingService.class);
        intentCreate.setAction("ACTION_CREATE");
        context.startService(intentCreate);
    }

}
