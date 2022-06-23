package com.whatscan.toolkit.forwa.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Shake.ActivityShake;

public class WpShakeService extends Service {

    public final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            SharedPreferences.Editor edit = getSharedPreferences("sharedPrefs", 0).edit();
            edit.putBoolean("shake_switch", true);
            edit.apply();

            if (shake > 10.0f) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                launchIntentForPackage.setPackage("com.whatsapp");
                startActivity(launchIntentForPackage);
                Toast.makeText(getApplicationContext(), "Opening WhatsApp", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    float sensLast, sensVal, shake;

    public WpShakeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int i, int i2) {
        PendingIntent activity = PendingIntent.getActivity(this, 0, new Intent(this, ActivityShake.class), 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        return 2;
    }

    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan.setLightColor(Color.BLUE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        }
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(chan);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(1), 3);
        sensVal = 9.80665f;
        sensLast = 9.80665f;
        shake = 0.0f;
        startForeground(2, notification);
    }

}