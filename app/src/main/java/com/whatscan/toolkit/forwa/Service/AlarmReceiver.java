package com.whatscan.toolkit.forwa.Service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.whatscan.toolkit.forwa.AutoScheduler.ActivityResultScheduler;
import com.whatscan.toolkit.forwa.DataBaseHelper.DBHelperScheduler;

import java.util.ArrayList;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    public static String code;
    public static String msg;
    public static String pkg;
    public static ArrayList<String> contacts = new ArrayList<>();
    public static ArrayList<String> message = new ArrayList<>();
    public static ArrayList<String> packagename = new ArrayList<>();
    public static ArrayList<String> requestcode = new ArrayList<>();

    @SuppressLint("WrongConstant")
    public void onReceive(Context context, Intent intent) {
        try {
            contacts = intent.getStringArrayListExtra("contactname");
            message = intent.getStringArrayListExtra(DBHelperScheduler.EVENTS_COLUMN_MESSAGE);
            packagename = intent.getStringArrayListExtra(DBHelperScheduler.EVENTS_COLUMN_PACKAGE);
            requestcode = intent.getStringArrayListExtra(DBHelperScheduler.EVENTS_COLUMN_REQUESTCODE);
            msg = message.get(0);
            pkg = packagename.get(0);
            code = requestcode.get(0);
            Intent intent2 = new Intent(context, ActivityResultScheduler.class);
            intent2.setType("text/plain");
            intent2.putExtra(DBHelperScheduler.EVENTS_COLUMN_MESSAGE, msg);
            intent2.putExtra("package", pkg);
            intent2.putExtra("code", code);
            intent2.addFlags(268435488);
            context.startActivity(intent2);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (RuntimeException e2) {
            e2.printStackTrace();
            Toast.makeText(context, "Whatsapp is not installed in your mobile", Toast.LENGTH_LONG).show();
        }
    }
}
