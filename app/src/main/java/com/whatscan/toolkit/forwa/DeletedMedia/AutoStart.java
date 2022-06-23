package com.whatscan.toolkit.forwa.DeletedMedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.whatscan.toolkit.forwa.Service.AllNotificationService;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            context.startService(new Intent(context, AllNotificationService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}