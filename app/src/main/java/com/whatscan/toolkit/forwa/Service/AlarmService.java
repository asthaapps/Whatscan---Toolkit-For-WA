package com.whatscan.toolkit.forwa.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.whatscan.toolkit.forwa.AutoScheduler.ActivityEventSchedule;
import com.whatscan.toolkit.forwa.DataBaseHelper.DBHelperScheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AlarmService extends Service {
    public static String TAG = "AlarmService";
    public ArrayList<String> contacts = new ArrayList<>();
    public ArrayList<HashMap<String, ArrayList<String>>> eventlist = new ArrayList<>();
    public String message;
    public String packagename;
    public int requestcode;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        sendBroadcast();
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.e(TAG, "onStartCommand");
        return 1;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
    }

    private void sendBroadcast() {
        Calendar instance = Calendar.getInstance();
        instance.set(2, ActivityEventSchedule.mnth);
        instance.set(1, Integer.parseInt(ActivityEventSchedule.yr));
        instance.set(5, Integer.parseInt(ActivityEventSchedule.date));
        instance.set(11, Integer.parseInt(ActivityEventSchedule.hr));
        instance.set(12, Integer.parseInt(ActivityEventSchedule.min));
        instance.set(13, 0);
        instance.set(14, 0);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(ActivityEventSchedule.msg);
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add(ActivityEventSchedule.packagename);
        ArrayList<String> arrayList3 = new ArrayList<>();
        arrayList3.add(String.valueOf(ActivityEventSchedule.requestcode));
        HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
        hashMap.put("contactname", ActivityEventSchedule.contactlist);
        hashMap.put(DBHelperScheduler.EVENTS_COLUMN_MESSAGE, arrayList);
        hashMap.put(DBHelperScheduler.EVENTS_COLUMN_PACKAGE, arrayList2);
        hashMap.put(DBHelperScheduler.EVENTS_COLUMN_REQUESTCODE, arrayList3);
        this.eventlist.add(hashMap);
        for (int i = 0; i < this.eventlist.size(); i++) {
            HashMap<String, ArrayList<String>> hashMap2 = this.eventlist.get(i);
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            intent.putStringArrayListExtra("contactname", hashMap2.get("contactname"));
            intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_MESSAGE, hashMap2.get(DBHelperScheduler.EVENTS_COLUMN_MESSAGE));
            intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_PACKAGE, hashMap2.get(DBHelperScheduler.EVENTS_COLUMN_PACKAGE));
            intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_REQUESTCODE, hashMap2.get(DBHelperScheduler.EVENTS_COLUMN_REQUESTCODE));
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).setExact(AlarmManager.RTC_WAKEUP, instance.getTimeInMillis(), PendingIntent.getBroadcast(getApplicationContext(), ActivityEventSchedule.requestcode, intent, 134217728));
            ActivityEventSchedule.contactlist.clear();
        }
    }
}
