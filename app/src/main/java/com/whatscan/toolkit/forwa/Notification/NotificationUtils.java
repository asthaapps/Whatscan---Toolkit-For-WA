package com.whatscan.toolkit.forwa.Notification;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.internal.view.SupportMenu;

import com.whatscan.toolkit.forwa.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

@SuppressLint({"WrongConstant", "ObsoleteSdkInt", "SimpleDateFormat"})
public class NotificationUtils {

    private final Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            assert am != null;
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            assert am != null;
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            assert componentInfo != null;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    private static long getTimeMilliSec(String timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timestamp);
            assert date != null;
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @SuppressLint("WrongConstant")
    public void showNotificationMessage(final String title, final String message, String imageUrl, String timestamp, Intent intent) {
        PendingIntent pendingIntent;
        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/click_sound");

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (!TextUtils.isEmpty(imageUrl)) {
            try {
                String Image = "https://asthatechnology.net/api/Whatstool/uploads/notification_image/" + imageUrl;
                Bitmap bitmap = getBitmapFromURL(Image);

                String channel_id = null;


                pendingIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();

                bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                bigPictureStyle.bigPicture(bitmap);

                if (Build.VERSION.SDK_INT >= 26) {
                    channel_id = "id_product";

                    try {
                        NotificationChannel notificationChannel = new NotificationChannel(channel_id, "Product", NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.setDescription(message);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
                        assert notificationManager != null;
                        notificationManager.createNotificationChannel(notificationChannel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Notification notification;
                assert channel_id != null;
                notification = new NotificationCompat.Builder(mContext, channel_id)
                        .setSmallIcon(R.drawable.logo)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(bitmap)
                        .setSound(alarmSound)
                        .setColor(mContext.getResources().getColor(R.color.colorTools))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_HIGH | NotificationCompat.PRIORITY_MAX)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap)
                                .bigLargeIcon(null))
                        .setPriority(Notification.PRIORITY_HIGH | Notification.PRIORITY_MAX)
                        .setWhen(getTimeMilliSec(timestamp))
                        .build();


                assert notificationManager != null;
                notificationManager.notify(1, notification);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showSmallNotification(title, message, alarmSound, timestamp, intent);
            playNotificationSound();
        }
    }

    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/click_sound");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            if (r != null) {
                r.play();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("RestrictedApi")
    private void showSmallNotification(String title, String message, Uri alarmSound, String timestamp, Intent intent) {
        String channel_id = null;

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, FLAG_ACTIVITY_NEW_TASK);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            channel_id = "id_product";
            try {
                NotificationChannel notificationChannel = new NotificationChannel(channel_id, "Product", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription(message);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Notification notification;
        assert channel_id != null;
        notification = new NotificationCompat.Builder(mContext, channel_id)
                .setSmallIcon(R.drawable.logo).setWhen(getTimeMilliSec(timestamp))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setColor(mContext.getResources().getColor(R.color.colorTools))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH | NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentText(message)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo))
                .setContentIntent(pendingIntent)
                .build();
        assert notificationManager != null;
        notificationManager.notify(101, notification);
    }
}