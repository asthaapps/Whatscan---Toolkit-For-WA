package com.whatscan.toolkit.forwa.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.whatscan.toolkit.forwa.R;

import org.jetbrains.annotations.NotNull;

import kotlin.TypeCastException;

public abstract class MyBaseTaskService extends Service {
    public static final int FINISHED_NOTIFICATION_ID = 1;
    public static final int PROGRESS_NOTIFICATION_ID = 0;
    public static final String TAG = "MyBaseTaskService";
    public int numTasks;

    private synchronized void changeNumberOfTasks(int i) {
        int i2 = numTasks + i;
        numTasks = i2;
        if (i2 <= 0) {
            stopSelf();
        }
    }

    private void createDefaultChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            getManager().createNotificationChannel(new NotificationChannel("default", "WhatsTool Sharing File", 3));
        }
    }

    private NotificationManager getManager() {
        Object systemService = getSystemService("notification");
        if (systemService != null) {
            return (NotificationManager) systemService;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.app.NotificationManager");
    }

    public final void a() {
        getManager().cancel(0);
    }

    public final void b(@NotNull String str, @NotNull Intent intent, boolean z) {
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 134217728);
        int i = z ? R.drawable.ic_done_white_24dp : R.drawable.ic_baseline_error_24;
        createDefaultChannel();
        getManager().notify(1, new NotificationCompat.Builder(this, "default").setSmallIcon(i).setContentTitle(getString(R.string.app_name)).setContentText(str).setAutoCancel(true).setContentIntent(activity).build());
    }

    public final void c(@NotNull String str, long j, long j2) {
        int i = j2 > 0 ? (int) ((((long) 100) * j) / j2) : 0;
        createDefaultChannel();
        getManager().notify(0, new NotificationCompat.Builder(this, "default").setSmallIcon(R.drawable.ic_timelapse_24px_rounded).setContentTitle(getString(R.string.app_name)).setContentText(str).setProgress(100, i, false).setOngoing(true).setAutoCancel(false).build());
    }

    public final void taskCompleted() {
        changeNumberOfTasks(-1);
    }

    public final void taskStarted(int i) {
        changeNumberOfTasks(numTasks);
    }
}