package com.whatscan.toolkit.forwa.Shake;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.WpBShakeService;
import com.whatscan.toolkit.forwa.Service.WpShakeService;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ActivityShake extends AppCompatActivity {
    private float sensLast, sensVal, shake;
    private boolean switchOnOff;
    private SwitchCompat shake_switch;
    private RelativeLayout rl_shake;

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float eve = event.values[0];
            float eve1 = event.values[1];
            float eve2 = event.values[2];

            sensLast = sensVal;
            sensVal = (float) Math.sqrt((eve * eve) + (eve1 * eve1) + (eve2 * eve2));
            float eve3 = sensVal - sensLast;
            shake = (shake * 0.9f) + eve3;

            if (shake_switch.isChecked()) {
                saveData();
                SharedPreferences.Editor edit = getSharedPreferences("sharedPrefs", 0).edit();
                edit.putBoolean("shake_switch", true);
                edit.apply();
                if (shake > 10.0f) {
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                    if (Constant.whatsappInstalledOrNot(ActivityShake.this)) {
                        Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                        launchIntentForPackage.setPackage("com.whatsapp");
                        startActivity(launchIntentForPackage);
                        Toast.makeText(getApplicationContext(), "Opening WhatsApp", Toast.LENGTH_SHORT).show();
                    } else if (Constant.whatsappBusinessInstalledOrNot(ActivityShake.this)) {
                        Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b");
                        launchIntentForPackage.setPackage("com.whatsapp.w4b");
                        startActivity(launchIntentForPackage);
                        Toast.makeText(getApplicationContext(), "Opening WhatsApp Business", Toast.LENGTH_SHORT).show();
                    }
                }
                return;
            }

            SharedPreferences.Editor edit2 = getSharedPreferences("sharedPrefs", 0).edit();
            edit2.putBoolean("shake_switch", false);
            edit2.apply();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityShake.this);
        setContentView(R.layout.activity_shake);

        FindView();
    }

    private void FindView() {
        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        TextView tv_shake = findViewById(R.id.tv_shake);
        TextView tv_shake_info = findViewById(R.id.tv_shake_info);
        View ic_include = findViewById(R.id.ic_include);
        shake_switch = findViewById(R.id.shake_switch);
        rl_shake = findViewById(R.id.rl_shake);
        la_info.setVisibility(View.VISIBLE);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_shake.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tv_shake.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_shake_info.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rl_shake.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_shake.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            tv_shake_info.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(1), 3);

        sensVal = 9.80665f;
        sensLast = 9.80665f;
        shake = 0.0f;
        loadData();
        updateViews();
        shake_switch.setChecked(false);
        shake_switch.setChecked(getSharedPreferences("sharedPrefs", 0).getBoolean("shake_switch", true));

        shake_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Constant.whatsappInstalledOrNot(ActivityShake.this) && Constant.whatsappBusinessInstalledOrNot(ActivityShake.this)) {
                    OpenSelectDialog();
                } else if (Constant.whatsappInstalledOrNot(ActivityShake.this)) {
                    WhatsAppService();
                } else if (Constant.whatsappBusinessInstalledOrNot(ActivityShake.this)) {
                    WhatsApp_Business_Service();
                }
            } else {
                stopWhatsAppService();
                stopWhatsApp_Business_Service();
            }
        });

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.whats_shake) + "</small>"));

        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(v -> com.whatscan.toolkit.forwa.Util.Constant.BottomSheetDialogView(ActivityShake.this, getString(R.string.shake_device), getString(R.string.shake_text)));
    }


    private void startServiceWhatsApp() {
        startService(new Intent(this, WpShakeService.class));
    }

    private void stopWhatsAppService() {
        stopService(new Intent(this, WpShakeService.class));
    }

    private void startServiceWhatsApp_Business() {
        startService(new Intent(this, WpBShakeService.class));
    }

    private void stopWhatsApp_Business_Service() {
        stopService(new Intent(this, WpBShakeService.class));
    }

    private void WhatsAppService() {
        getPackageManager();
        Intent intent2 = new Intent("android.intent.action.SEND");
        intent2.setType("text/plain");
        intent2.setPackage("com.whatsapp");
        startServiceWhatsApp();
        Toast.makeText(getApplicationContext(), "WhatsShake Activated!", Toast.LENGTH_SHORT).show();
    }

    private void WhatsApp_Business_Service() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setPackage("com.whatsapp.w4b");
        if (getPackageManager().resolveActivity(intent, 0) != null) {
            Toast.makeText(getApplicationContext(), "WhatsShake Activated!", Toast.LENGTH_SHORT).show();
            startServiceWhatsApp_Business();
        }
    }

    private void OpenSelectDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityShake.this);
        View inflate = LayoutInflater.from(ActivityShake.this).inflate(R.layout.select_dialog, null);
        bottomSheetDialog.setContentView(inflate);

        RelativeLayout rl_dialog = inflate.findViewById(R.id.rl_dialog);
        TextView tv_dialog_title = inflate.findViewById(R.id.tv_dialog_title);
        TextView tv_dialog_tip = inflate.findViewById(R.id.tv_dialog_tip);
        Button bt_Wp = inflate.findViewById(R.id.bt_Wp);
        Button bt_WpB = inflate.findViewById(R.id.bt_WpB);

        if (Preference.getBooleanTheme(false)) {
            rl_dialog.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tv_dialog_title.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_dialog_tip.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        tv_dialog_title.setText(getString(R.string.selcet_title));
        tv_dialog_tip.setText(getString(R.string.select_text));

        bt_Wp.setOnClickListener(v -> {
            WhatsAppService();
            bottomSheetDialog.dismiss();
        });

        bt_WpB.setOnClickListener(v -> {
            WhatsApp_Business_Service();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
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

    public void setStatusBarTheme() {
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darkBlack));
    }

    @SuppressLint("CommitPrefEdits")
    public void saveData() {
        getSharedPreferences("sharedPrefs", 0).edit().putBoolean("shake_switch", shake_switch.isChecked());
    }

    public void loadData() {
        if (shake_switch.isChecked()) {
            switchOnOff = getSharedPreferences("sharedPrefs", 0).getBoolean("shake_switch", true);
        }
    }

    public void updateViews() {
        shake_switch.setChecked(switchOnOff);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}