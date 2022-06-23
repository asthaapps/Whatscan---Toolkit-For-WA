package com.whatscan.toolkit.forwa.Service;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.AutoResponse.FragmentHome;
import com.whatscan.toolkit.forwa.GetSet.ContactsList;
import com.whatscan.toolkit.forwa.Util.Preference;

public class MainService extends Service {
    public static final String ACTION_RECORD = "com.whatscan.toolkit.forwa.RECORD";
    public static final String ACTION_SHUTDOWN = "com.whatscan.toolkit.forwa.SHUTDOWN";
    public static ContactsList contactsList;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            if (ACTION_RECORD.equals(intent.getAction())) {
                sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                startActivity(new Intent(this, ActivityMain.class).addFlags(268435456));
            } else if (ACTION_SHUTDOWN.equals(intent.getAction())) {
                try {
                    FragmentHome.service_on_off.setChecked(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Preference.setBoolean(getApplicationContext(), "servicecheck", "onoff", false);
                sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                stopForeground(true);
            }
        }
        return 1;
    }

    private PendingIntent buildPendingIntent(String str) {
        Intent intent = new Intent(this, getClass());
        intent.setAction(str);
        return PendingIntent.getService(this, 0, intent, 0);
    }

    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    public static class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
        }
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public MainService Binderservice() {
            return MainService.this;
        }
    }
}