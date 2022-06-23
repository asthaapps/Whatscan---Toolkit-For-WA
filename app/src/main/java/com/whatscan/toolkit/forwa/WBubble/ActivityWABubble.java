package com.whatscan.toolkit.forwa.WBubble;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.DataBaseHelper.ChatHistoryDatabaseHelper;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.AllNotificationService;
import com.whatscan.toolkit.forwa.Util.AccessibilityPermission;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WBubble.util.Constant;

public class ActivityWABubble extends AppCompatActivity {

    ChatHistoryDatabaseHelper delChatDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        com.whatscan.toolkit.forwa.Util.Constant.adjustFontScale(getResources().getConfiguration(), ActivityWABubble.this);
        setContentView(R.layout.activity_wabubble);

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityWABubble.this, ll_banner);

        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        ToggleButton whatsapponoff = findViewById(R.id.whatsapponoff);
        RelativeLayout rl_history = findViewById(R.id.rl_history);
        RelativeLayout rl_setting = findViewById(R.id.rl_setting);
        RelativeLayout rlBubble = findViewById(R.id.rlBubble);
        View ic_include = findViewById(R.id.ic_include);
        TextView txtOne = findViewById(R.id.txtOne);
        TextView txtTwo = findViewById(R.id.txtTwo);
        TextView txtThree = findViewById(R.id.txtThree);

        la_info.setVisibility(View.VISIBLE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.whats_bubble) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlBubble.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        delChatDatabaseHelper = new ChatHistoryDatabaseHelper(this);

        if (Preference.getPermission_overlay().equals("true") && Preference.getPermission_notification().equals("true")) {
            whatsapponoff.setChecked(true);
        } else if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else if (!AccessibilityPermission.CheckNoti(this)) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }

        whatsapponoff.setOnCheckedChangeListener((compoundButton, z) -> {
            if (z) {
                Constant.setBoolean(ActivityWABubble.this, Constant.prefname, "com.whatsapp", true);
                return;
            }
            if (!(AllNotificationService.chatHeadService == null || AllNotificationService.chatHeadService.stringDefaultChatHeadManager == null || AllNotificationService.chatHeadService.stringDefaultChatHeadManager.getChatHeads().size() <= 0)) {
                AllNotificationService.chatHeadService.removeAllChatHeads();
            }
            Constant.setBoolean(ActivityWABubble.this, Constant.prefname, "com.whatsapp", false);
        });

        rl_history.setOnClickListener(v -> startActivity(new Intent(ActivityWABubble.this, ChatHistoryActivity.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        rl_setting.setOnClickListener(v -> startActivity(new Intent(ActivityWABubble.this, ActivityBubbleSetting.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(v -> com.whatscan.toolkit.forwa.Util.Constant.BottomSheetDialogView(ActivityWABubble.this, getString(R.string.fake_profile_wp), getString(R.string.fake_profile_info_text)));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}