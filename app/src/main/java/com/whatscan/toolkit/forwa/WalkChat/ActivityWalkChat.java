package com.whatscan.toolkit.forwa.WalkChat;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

public class ActivityWalkChat extends AppCompatActivity {
    public static boolean isWalk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityWalkChat.this);
        setContentView(R.layout.activity_walk_chat);

        FindView();
    }

    private void FindView() {
        if (!(ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_NETWORK_STATE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.SET_WALLPAPER") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.INTERNET") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.SYSTEM_ALERT_WINDOW") == 0) && Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.SET_WALLPAPER", "android.permission.INTERNET", "android.permission.SYSTEM_ALERT_WINDOW"}, 0);
        }

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        TextView tv_walk = findViewById(R.id.tv_walk);
        TextView tv_walk_info = findViewById(R.id.tv_walk_info);
        SwitchCompat walk_switch = findViewById(R.id.walk_switch);
        Button bt_open = findViewById(R.id.bt_open);
        RelativeLayout rl_walkChat = findViewById(R.id.rl_walkChat);
        View ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.walk_chat) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_walkChat.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tv_walk.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_walk_info.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rl_walkChat.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_walk.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            tv_walk_info.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        walk_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isWalk) {
                isWalk = false;
                return;
            }
            isWalk = true;
        });

        bt_open.setOnClickListener(v -> {
            if (Constant.whatsappInstalledOrNot(ActivityWalkChat.this)) {
                startActivity(getPackageManager().getLaunchIntentForPackage("com.whatsapp"));
            } else if (Constant.whatsappBusinessInstalledOrNot(ActivityWalkChat.this)) {
                startActivity(getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b"));
            }
        });

        la_back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
}