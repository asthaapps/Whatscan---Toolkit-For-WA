package com.whatscan.toolkit.forwa.AutoScheduler;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.whatscan.toolkit.forwa.AutoScheduler.Notify.Notify;
import com.whatscan.toolkit.forwa.DataBaseHelper.DBHelperScheduler;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.AlarmReceiver;

public class ActivityResultScheduler extends AppCompatActivity {
    public String code;
    public Intent goHome;
    public DBHelperScheduler helper;
    public Intent intent;
    public String msg;
    public String pkg;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        setContentView(R.layout.activity_result_scheduler);

        helper = new DBHelperScheduler(this);

        Intent intent2 = getIntent();
        msg = intent2.getStringExtra(DBHelperScheduler.EVENTS_COLUMN_MESSAGE);
        pkg = intent2.getStringExtra("package");
        code = intent2.getStringExtra("code");
        intent = new Intent(getApplicationContext(), ActivityAllEventScheduler.class);
        goHome = new Intent();
        goHome.setFlags(268435456);
        goHome.setAction("android.intent.action.MAIN");
        goHome.addCategory("android.intent.category.HOME");

        registerReceiver(new PhoneUnlockedReceiver(), new IntentFilter("android.intent.action.USER_PRESENT"));

        if (isDeviceLocked(this)) {
            Notify.build(getApplicationContext()).setTitle("Auto WhatScheduler").setContent("You Should need to open screen lock for send message.").setSmallIcon(R.drawable.logo).setLargeIcon(R.drawable.logo).setColor(R.color.colorBlack).setAction(this.goHome).setAutoCancel(true).largeCircularIcon().setId(100).show();
            return;
        }

        ActivityAllEventScheduler.contactname = null;
        ActivityAllEventScheduler.sendtoken = " ";
        Intent intent4 = new Intent("android.intent.action.SEND");
        intent4.setType("text/plain");
        intent4.putExtra("android.intent.extra.TEXT", msg);
        intent4.setPackage(pkg);
        intent4.addFlags(8388608);
        startActivityForResult(intent4, 1);
        msg = null;
        pkg = null;
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent2) {
        super.onActivityResult(i, i2, intent2);
        if (i2 == -1 && i == 1) {
            this.helper.updateStatus(Integer.parseInt(this.code), "success");
            AlarmReceiver.contacts.clear();
            ActivityAllEventScheduler.sendtoken = null;
            Notify.build(getApplicationContext()).setTitle("Auto WhatScheduler").setContent("Your message sent succeessfully").setSmallIcon(R.drawable.logo).setLargeIcon(R.drawable.logo).setColor(R.color.colorBlack).setAction(this.intent).setAutoCancel(true).largeCircularIcon().show();
            finishAndRemoveTask();
            finishAffinity();
        }
        if (i2 == 0 && i == 1) {
            this.helper.updateStatus(Integer.parseInt(this.code), "failed");
            AlarmReceiver.contacts.clear();
            ActivityAllEventScheduler.sendtoken = null;
            Notify.build(getApplicationContext()).setTitle("Auto WhatScheduler").setContent("Your message sending failed").setSmallIcon(R.drawable.logo).setLargeIcon(R.drawable.logo).setAction(this.intent).setAutoCancel(true).setColor(R.color.colorBlack).largeCircularIcon().show();
            finishAndRemoveTask();
            finishAffinity();
        }
    }

    public class PhoneUnlockedReceiver extends BroadcastReceiver {
        public PhoneUnlockedReceiver() {
        }

        @SuppressLint("WrongConstant")
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.USER_PRESENT") && msg != null && pkg != null) {
                Notify.cancel(getApplicationContext(), 100);
                ActivityAllEventScheduler.contactname = null;
                ActivityAllEventScheduler.sendtoken = " ";
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("text/plain");
                intent2.putExtra("android.intent.extra.TEXT", msg);
                intent2.setPackage(pkg);
                intent2.addFlags(8388608);
                startActivityForResult(intent2, 1);
                msg = null;
                pkg = null;
            } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                Notify.build(getApplicationContext()).setTitle("Auto WhatScheduler").setContent("You Should need to open screen lock for send message.").setSmallIcon(R.drawable.logo).setLargeIcon(R.drawable.logo).setAutoCancel(true).setAction(goHome).setColor(R.color.colorBlack).largeCircularIcon().setId(100).show();
            }
        }
    }

    public static boolean isDeviceLocked(Context context) {
        boolean z;
        if (((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE)).isKeyguardLocked()) {
            return true;
        }
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        z = powerManager.isInteractive();
        return !z;
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhite));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        } else {
            window.clearFlags(0);
        }
    }
}
