package com.whatscan.toolkit.forwa.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import com.whatscan.toolkit.forwa.Shake.ActivityShake;

public class WpBShakeService extends Service {
    public final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            SharedPreferences.Editor edit = getSharedPreferences("sharedPrefs", 0).edit();
            edit.putBoolean("shake_switch", true);
            edit.apply();

            if (shake > 12.0f) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b");
                launchIntentForPackage.setPackage("com.whatsapp.w4b");
                startActivity(launchIntentForPackage);
                Toast.makeText(getApplicationContext(), "Opening WhatsApp Business", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    float sensLast, sensVal, shake;

    public WpBShakeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        PendingIntent activity = PendingIntent.getActivity(this, 0, new Intent(this, ActivityShake.class), 0);
        // startForeground(1, new NotificationCompat.Builder(this, Constant.CHANNEL_ID).setContentTitle("WhatsShake Running!").setContentText("Open WhatsApp Business any time by shake").setSmallIcon(R.drawable.logo).setContentIntent(activity).build());

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(1), 3);
        sensVal = 9.80665f;
        sensLast = 9.80665f;
        shake = 0.0f;
        return super.onStartCommand(intent, flags, startId);
    }
}