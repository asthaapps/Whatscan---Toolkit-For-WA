package com.whatscan.toolkit.forwa.Prank.FakeProfile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.Prank.ImagePickerActivity;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityFakeProfile extends AppCompatActivity {
    public static final int REQUEST_PROFILE = 101;
    public Uri profile_uri;
    public TextInputLayout mtf_name, mtf_last_seen, mtf_status, mtf_status_date, mtf_mobile;
    public TextInputEditText et_name, et_last_seen, et_status, et_status_dat, et_mobile;
    public AppCompatButton bt_select_profile, bt_watch_profile;
    public CardView cv_help_seen, cv_help_date;
    public TextView tv_tip, tv_toolbar;
    public LottieAnimationView  la_info, la_seen_info, la_date_info;
    public ImageView la_back;
    public CircleImageView cv_profile;
    public View ic_include;
    public RelativeLayout rlFackProfile, rl_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityFakeProfile.this);
        setContentView(R.layout.activity_fake_profile);

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityFakeProfile.this, ll_banner);

        rlFackProfile = findViewById(R.id.rlFackProfile);
        ic_include = findViewById(R.id.ic_include);
        rl_dialog = findViewById(R.id.rl_dialog);
        tv_tip = findViewById(R.id.tv_tip);
        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        mtf_name = findViewById(R.id.mtf_name);
        mtf_last_seen = findViewById(R.id.mtf_last_seen);
        mtf_status = findViewById(R.id.mtf_status);
        mtf_status_date = findViewById(R.id.mtf_status_date);
        mtf_mobile = findViewById(R.id.mtf_mobile);
        et_name = findViewById(R.id.et_name);
        et_last_seen = findViewById(R.id.et_last_seen);
        et_status = findViewById(R.id.et_status);
        et_status_dat = findViewById(R.id.et_status_dat);
        et_mobile = findViewById(R.id.et_mobile);
        bt_select_profile = findViewById(R.id.bt_select_profile);
        bt_watch_profile = findViewById(R.id.bt_watch_profile);
        cv_profile = findViewById(R.id.cv_profile);
        la_seen_info = findViewById(R.id.la_seen_info);
        la_info = findViewById(R.id.la_info);
        la_date_info = findViewById(R.id.la_date_info);
        cv_help_seen = findViewById(R.id.cv_help_seen);
        cv_help_date = findViewById(R.id.cv_help_date);

        la_info.setVisibility(View.VISIBLE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.fake_profile) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlFackProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            et_name.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_last_seen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_status.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_status_dat.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_mobile.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            setTextInputLayoutHintColor(mtf_name, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_last_seen, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_status, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_status_date, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_mobile, this, R.color.colorWhite);
        } else {
            setStatusBar();
            rlFackProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_name.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_last_seen.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_status.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_status_dat.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_mobile.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            setTextInputLayoutHintColor(mtf_name, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_last_seen, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_status, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_status_date, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_mobile, this, R.color.colorBlack);
        }

        la_back.setOnClickListener(v -> onBackPressed());
        bt_select_profile.setOnClickListener(v -> SelectProfile());
        la_info.setOnClickListener(v -> Constant.BottomSheetDialogView(ActivityFakeProfile.this, getString(R.string.fake_profile_wp), getString(R.string.fake_profile_info_text)));
        la_seen_info.setOnClickListener(v -> SetMethod(cv_help_seen));
        la_date_info.setOnClickListener(v -> SetMethodDate(cv_help_date));

        bt_watch_profile.setOnClickListener(v -> {
            String strName = Objects.requireNonNull(et_name.getText()).toString();
            String strSeen = Objects.requireNonNull(et_last_seen.getText()).toString();
            String strStatus = Objects.requireNonNull(et_status.getText()).toString();
            String strStatusDate = Objects.requireNonNull(et_status_dat.getText()).toString();
            String strMobile = Objects.requireNonNull(et_mobile.getText()).toString();

            if (strName.isEmpty()) {
                et_name.setError("Enter Name");
            } else if (strSeen.isEmpty()) {
                et_last_seen.setError("Enter Last Seen");
            } else if (strStatus.isEmpty()) {
                et_status.setError("Enter Status");
            } else if (strStatusDate.isEmpty()) {
                et_status_dat.setError("Enter Date");
            } else if (strMobile.isEmpty()) {
                et_mobile.setError("Enter Mobile No");
            } else if (profile_uri == null) {
                Toast.makeText(ActivityFakeProfile.this, "Please! Select Profile", Toast.LENGTH_SHORT).show();
            } else {
                Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                ShowDialog(animSlideUp, strName, strSeen, strStatus, strStatusDate, strMobile);
            }
        });
    }

    private void setTextInputLayoutHintColor(TextInputLayout text_input1, Context context, int colorBlack) {
        text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, colorBlack)));
    }

    private void SetMethodDate(CardView cv_help_date) {
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        new Handler().postDelayed(() -> {
            cv_help_date.setAnimation(animFadeOut);
            cv_help_date.setVisibility(View.GONE);
        }, 2000);
        if (cv_help_date.getVisibility() == View.GONE) {
            cv_help_date.setVisibility(View.VISIBLE);
            cv_help_date.setAnimation(animFadeIn);
        } else if (cv_help_date.getVisibility() == View.VISIBLE) {
            cv_help_date.setAnimation(animFadeOut);
            cv_help_date.setVisibility(View.GONE);
        }
    }

    private void ShowDialog(Animation animSlideUp, String strName, String strSeen, String strStatus, String strStatusDate, String strMobile) {
        rl_dialog.setVisibility(View.VISIBLE);
        rl_dialog.setAnimation(animSlideUp);
        new Handler().postDelayed(() -> {
            Advertisement.getInstance(ActivityFakeProfile.this).showFullAds(() -> {
                Intent intent = new Intent(ActivityFakeProfile.this, ViewFakeProfile.class);
                intent.putExtra("strName", strName);
                intent.putExtra("strSeen", strSeen);
                intent.putExtra("strStatus", strStatus);
                intent.putExtra("strStatusDate", strStatusDate);
                intent.putExtra("strMobile", strMobile);
                intent.putExtra("profile_uri", profile_uri.toString());
                startActivity(intent);
                rl_dialog.setVisibility(View.GONE);
            });
        }, 3000);
    }

    private void SetMethod(CardView cv_help_seen) {
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        new Handler().postDelayed(() -> {
            cv_help_seen.setAnimation(animFadeOut);
            cv_help_seen.setVisibility(View.GONE);
        }, 2000);
        if (cv_help_seen.getVisibility() == View.GONE) {
            cv_help_seen.setVisibility(View.VISIBLE);
            cv_help_seen.setAnimation(animFadeIn);
        } else if (cv_help_seen.getVisibility() == View.VISIBLE) {
            cv_help_seen.setAnimation(animFadeOut);
            cv_help_seen.setVisibility(View.GONE);
        }
    }

    private void SelectProfile() {
        CheckPermission();
    }

    private void CheckPermission() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            launchGalleryIntent();
                        } else {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityFakeProfile.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_PROFILE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(ActivityFakeProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_PROFILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PROFILE) {
                assert data != null;
                profile_uri = data.getParcelableExtra("path");
                Glide.with(this).load(profile_uri.toString())
                        .into(cv_profile);
                cv_profile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
            }
        }
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