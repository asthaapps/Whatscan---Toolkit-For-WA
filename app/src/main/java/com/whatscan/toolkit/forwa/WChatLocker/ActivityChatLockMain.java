package com.whatscan.toolkit.forwa.WChatLocker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.whatscan.toolkit.forwa.Other.CommonWebView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.WhatsappAccessibility;
import com.whatscan.toolkit.forwa.User.ActivityLogin;
import com.whatscan.toolkit.forwa.Util.AccessibilityPermission;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.concurrent.atomic.AtomicInteger;

public class ActivityChatLockMain extends AppCompatActivity {

    public static boolean isFromActivity = false;
    public FrameLayout content_frame;
    public FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_chat_lock_main);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityChatLockMain.this);
        FindView(savedInstanceState);
    }

    @SuppressLint("WrongConstant")
    private void FindView(Bundle bundle) {
        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        RelativeLayout rlChatLockMain = findViewById(R.id.rlChatLockMain);
        View ic_include = findViewById(R.id.ic_include);

        la_info.setVisibility(View.GONE);
        tv_toolbar.setText(Html.fromHtml("<small>" + "WhatsApp Chat Locker" + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlChatLockMain.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }

        content_frame = findViewById(R.id.content_frame);
        floatingActionButton = findViewById(R.id.btn_done);

        if (bundle == null) {
            FragmentChatLock homeFragment = new FragmentChatLock();
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.add(R.id.content_frame, homeFragment);
            beginTransaction.detach(homeFragment);
            beginTransaction.attach(homeFragment);
            beginTransaction.commit();
        }

        floatingActionButton.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                if (!AccessibilityPermission.isAccessibilityOn(ActivityChatLockMain.this, WhatsappAccessibility.class)) {
                    showPermission();
                } else {
                    try {
                        Constant.WhatsAppChatLock = true;
                        isFromActivity = true;
                        Intent intent = new Intent();
                        intent.setClassName("com.whatsapp", "com.whatsapp.HomeActivity");
                        intent.addFlags(1342177280);
                        intent.addFlags(67108864);
                        startActivity(intent);
                        finish();
                    } catch (Exception unused) {
                        Toast.makeText(ActivityChatLockMain.this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                startActivity(new Intent(ActivityChatLockMain.this, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            }
        });

        la_back.setOnClickListener(v -> onBackPressed());
    }

    public void showPermission() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityChatLockMain.this);
        View inflate = LayoutInflater.from(ActivityChatLockMain.this).inflate(R.layout.permission_discloser1, null);
        bottomSheetDialog.setContentView(inflate);
        AtomicInteger check = new AtomicInteger();
        Button bt_agree = inflate.findViewById(R.id.bt_agree);
        TextView bt_cancel = inflate.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(view13 -> bottomSheetDialog.dismiss());
        CheckBox checkBox_agree = inflate.findViewById(R.id.checkBox_agree);
        SpannableString SpanString = new SpannableString(
                "By signing in, you agree to the Terms of Use and Privacy Policy");

        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(ActivityChatLockMain.this, CommonWebView.class);
                mIntent.putExtra("tc", true);
                startActivity(mIntent);
            }
        };

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(ActivityChatLockMain.this, CommonWebView.class);
                mIntent.putExtra("pp", true);
                startActivity(mIntent);
            }
        };


        SpanString.setSpan(teremsAndCondition, 32, 45, 0);
        SpanString.setSpan(privacy, 49, 63, 0);
        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 32, 45, 0);
        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 49, 63, 0);

        checkBox_agree.setMovementMethod(LinkMovementMethod.getInstance());
        checkBox_agree.setText(SpanString, TextView.BufferType.SPANNABLE);
        checkBox_agree.setSelected(true);

        checkBox_agree.setOnClickListener(view12 -> {
            boolean checked = ((CheckBox) view12).isChecked();
            if (checked) {
                check.set(1);
            } else {
                check.set(0);
            }

        });

        bt_agree.setOnClickListener(view1 -> {
            if (check.get() == 0) {
                Toast.makeText(ActivityChatLockMain.this, "please agree conditions.", Toast.LENGTH_SHORT).show();
            } else if (check.get() == 1) {
                startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setCancelable(false);
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
}