package com.whatscan.toolkit.forwa.Other;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.BasicAccessibilityService;
import com.whatscan.toolkit.forwa.Service.WhatsappAccessibility;
import com.whatscan.toolkit.forwa.Service.WhatsappAccessibilitySchedulerService;
import com.whatscan.toolkit.forwa.Util.AccessibilityPermission;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ActivityPermission extends AppCompatActivity {
    Button allowbtn, allowmanage, allowaccessbility, btnNext, allownoti, allowbtn_manage;
    RelativeLayout rlNext, rl_skip;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityPermission.this);
        setContentView(R.layout.activity_permission);

        FindView();
    }

    private void FindView() {
        allowbtn = findViewById(R.id.allowbtn);

        allowmanage = findViewById(R.id.allowmanage);
        allowaccessbility = findViewById(R.id.allowaccessbility);
        btnNext = findViewById(R.id.btnNext);
        rlNext = findViewById(R.id.rlNext);
        allownoti = findViewById(R.id.allownoti);
        rl_skip = findViewById(R.id.rl_skip);

        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText(Html.fromHtml("<small>" + "Permission" + "</small>"));

        new Handler().postDelayed(() -> rl_skip.setVisibility(View.VISIBLE), 3000);

        rl_skip.setOnClickListener(this::CheckLogIn);

        allowbtn.setOnClickListener(v -> CheckPermission());

        allowmanage.setOnClickListener(v -> {
            if (SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1111);
            } else if (SDK_INT >= 23 && Settings.canDrawOverlays(ActivityPermission.this)) {
                allowmanage.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
                allowmanage.setText(getResources().getString(R.string.permission_granted));
                allowmanage.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
                allowmanage.setPadding(20, 10, 20, 10);
                allowmanage.setMinHeight(0);
                Preference.setPermission_overlay("true");
            }
        });

        allownoti.setOnClickListener(v -> {
            if (!AccessibilityPermission.CheckNoti(this)) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivityForResult(intent, 3333);
            } else if (AccessibilityPermission.CheckNoti(this)) {
                allownoti.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
                allownoti.setText(getResources().getString(R.string.permission_granted));
                allownoti.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
                allownoti.setPadding(20, 10, 20, 10);
                allownoti.setMinHeight(0);
                Preference.setPermission_notification("true");
            }
        });

        allowaccessbility.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(ActivityPermission.this);
            View inflate = LayoutInflater.from(ActivityPermission.this).inflate(R.layout.permission_discloser1, null);

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
                    Intent mIntent = new Intent(ActivityPermission.this, CommonWebView.class);
                    mIntent.putExtra("tc", true);
                    startActivity(mIntent);
                }
            };

            ClickableSpan privacy = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Intent mIntent = new Intent(ActivityPermission.this, CommonWebView.class);
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
                    Toast.makeText(ActivityPermission.this, "please agree conditions.", Toast.LENGTH_SHORT).show();
                } else if (check.get() == 1) {
                    if (!AccessibilityPermission.isAccessibilityOn(ActivityPermission.this, WhatsappAccessibility.class)) {
                        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                        startActivityForResult(intent, 2222);
                    } else if (!AccessibilityPermission.isAccessibilityOn(ActivityPermission.this, BasicAccessibilityService.class)) {
                        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                        startActivityForResult(intent, 2222);
                    } else if (!AccessibilityPermission.isAccessibilityOn(ActivityPermission.this, WhatsappAccessibilitySchedulerService.class)) {
                        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                        startActivityForResult(intent, 2222);
                    } else if (AccessibilityPermission.isAccessibilityOn(ActivityPermission.this, WhatsappAccessibility.class) && AccessibilityPermission.isAccessibilityOn(ActivityPermission.this, BasicAccessibilityService.class) && AccessibilityPermission.isAccessibilityOn(ActivityPermission.this, WhatsappAccessibilitySchedulerService.class)) {
                        allowaccessbility.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
                        allowaccessbility.setText(getResources().getString(R.string.permission_granted));
                        allowaccessbility.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
                        allowaccessbility.setPadding(20, 10, 20, 10);
                        allowaccessbility.setMinHeight(0);
                        Preference.setPermission_accessbility("true");
                    }
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.show();
        });
    }


    private void PermissionFlag() {
        if (Preference.getPermission_storage().equals("true")) {
            allowbtn.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
            allowbtn.setText(getResources().getString(R.string.permission_granted));
            allowbtn.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
            allowbtn.setPadding(20, 10, 20, 10);
            allowbtn.setMinHeight(0);
        }
        if (Preference.getPermission_overlay().equals("true")) {
            allowmanage.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
            allowmanage.setText(getResources().getString(R.string.permission_granted));
            allowmanage.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
            allowmanage.setPadding(20, 10, 20, 10);
            allowmanage.setMinHeight(0);
        }
        if (Preference.getPermission_accessbility().equals("true")) {
            allowaccessbility.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
            allowaccessbility.setText(getResources().getString(R.string.permission_granted));
            allowaccessbility.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
            allowaccessbility.setPadding(20, 10, 20, 10);
            allowaccessbility.setMinHeight(0);
        }
        if (Preference.getPermission_notification().equals("true")) {
            allownoti.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
            allownoti.setText(getResources().getString(R.string.permission_granted));
            allownoti.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
            allownoti.setPadding(20, 10, 20, 10);
            allownoti.setMinHeight(0);
        }
    }

    private void CheckPermission() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            allowbtn.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
                            allowbtn.setText(getResources().getString(R.string.permission_granted));
                            allowbtn.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
                            allowbtn.setPadding(20, 10, 20, 10);
                            allowbtn.setMinHeight(0);
                            Preference.setPermission_media("true");
                            CheckPermissionAllow();
                        } else {
                            CheckPermission();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111) {
            if (SDK_INT >= 23 && Settings.canDrawOverlays(this)) {
                allowmanage.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
                allowmanage.setText(getResources().getString(R.string.permission_granted));
                allowmanage.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
                allowmanage.setPadding(20, 10, 20, 10);
                allowmanage.setMinHeight(0);
                Preference.setPermission_overlay("true");
                CheckPermissionAllow();
            }
        } else if (requestCode == 2222) {
            if (AccessibilityPermission.isAccessibilityOn(ActivityPermission.this, WhatsappAccessibility.class) && AccessibilityPermission.isAccessibilityOn(ActivityPermission.this, BasicAccessibilityService.class) && AccessibilityPermission.isAccessibilityOn(ActivityPermission.this, WhatsappAccessibilitySchedulerService.class)) {
                allowaccessbility.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
                allowaccessbility.setText(getResources().getString(R.string.permission_granted));
                allowaccessbility.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
                allowaccessbility.setPadding(20, 10, 20, 10);
                allowaccessbility.setMinHeight(0);
                Preference.setPermission_accessbility("true");
                CheckPermissionAllow();
            }
        } else if (requestCode == 3333) {
            if (AccessibilityPermission.CheckNoti(this)) {
                allownoti.setBackground(ContextCompat.getDrawable(ActivityPermission.this, R.drawable.gnt_rounded_corners_shape1));
                allownoti.setText(getResources().getString(R.string.permission_granted));
                allownoti.setTextColor(ContextCompat.getColor(ActivityPermission.this, R.color.colorBlack));
                allownoti.setPadding(20, 10, 20, 10);
                allownoti.setMinHeight(0);
                Preference.setPermission_notification("true");
                CheckPermissionAllow();
            }
        }
    }


    public void CheckPermissionAllow() {
        if (Preference.getPermission_storage().equals("true") && Preference.getPermission_media().equals("true") && Preference.getPermission_notification().equals("true")
                && Preference.getPermission_overlay().equals("true") && Preference.getPermission_accessbility().equals("true")) {
            rlNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(this::CheckLogIn);
        } else if (!Preference.getPermission_storage().equals("true") && !Preference.getPermission_media().equals("true") && !Preference.getPermission_notification().equals("true")
                && !Preference.getPermission_overlay().equals("true") && !Preference.getPermission_accessbility().equals("true")) {
            rlNext.setVisibility(View.GONE);
        }
    }

    public void CheckLogIn(View v) {
        startActivity(new Intent(ActivityPermission.this, ActivityMain.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PermissionFlag();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckPermissionAllow();
        PermissionFlag();
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhite));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else if (SDK_INT == 21 || SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        } else {
            window.clearFlags(0);
        }
    }
}