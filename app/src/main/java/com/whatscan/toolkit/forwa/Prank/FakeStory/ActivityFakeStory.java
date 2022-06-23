package com.whatscan.toolkit.forwa.Prank.FakeStory;

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

public class ActivityFakeStory extends AppCompatActivity {
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PROFILE = 101;
    public Uri profile_uri, image_uri;
    public ImageView iv_thumbnail;
    public CircleImageView cv_profile;
    public TextView tv_toolbar, tv_heading;
    public CardView cv_help;
    public View ic_include;
    public LottieAnimationView  la_info, la_seen_info;
    public ImageView la_back;
    public TextInputLayout mtf_name, mtf_last_seen, mtf_caption;
    public TextInputEditText et_name, et_last_seen, et_caption;
    public AppCompatButton bt_select_profile, bt_select_image, bt_watch_story;
    public RelativeLayout rlFackStory, rl_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityFakeStory.this);
        setContentView(R.layout.activity_fake_story);

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityFakeStory.this, ll_banner);

        rlFackStory = findViewById(R.id.rlFackStory);
        rl_dialog = findViewById(R.id.rl_dialog);
        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_heading = findViewById(R.id.tv_heading);
        et_name = findViewById(R.id.et_name);
        et_last_seen = findViewById(R.id.et_last_seen);
        et_caption = findViewById(R.id.et_caption);
        bt_select_profile = findViewById(R.id.bt_select_profile);
        bt_select_image = findViewById(R.id.bt_select_image);
        bt_watch_story = findViewById(R.id.bt_watch_story);
        cv_profile = findViewById(R.id.cv_profile);
        iv_thumbnail = findViewById(R.id.iv_thumbnail);
        la_info = findViewById(R.id.la_info);
        la_seen_info = findViewById(R.id.la_seen_info);
        la_info.setVisibility(View.VISIBLE);
        cv_help = findViewById(R.id.cv_help);
        ic_include = findViewById(R.id.ic_include);
        mtf_name = findViewById(R.id.mtf_name);
        mtf_last_seen = findViewById(R.id.mtf_last_seen);
        mtf_caption = findViewById(R.id.mtf_caption);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.fake_story) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlFackStory.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tv_heading.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_name.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_last_seen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_caption.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            setTextInputLayoutHintColor(mtf_name, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_last_seen, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_caption, this, R.color.colorWhite);
        } else {
            setStatusBar();
            rlFackStory.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_heading.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            et_name.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_last_seen.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_caption.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            setTextInputLayoutHintColor(mtf_name, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_last_seen, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_caption, this, R.color.colorBlack);
        }

        la_back.setOnClickListener(v -> onBackPressed());
        bt_select_image.setOnClickListener(v -> SelectImage());
        bt_select_profile.setOnClickListener(v -> SelectProfile());
        la_info.setOnClickListener(v -> com.whatscan.toolkit.forwa.Util.Constant.BottomSheetDialogView(ActivityFakeStory.this, getString(R.string.fake_story_wp), getString(R.string.fake_story_info_text)));
        la_seen_info.setOnClickListener(v -> SetMethod(cv_help));

        bt_watch_story.setOnClickListener(v -> {
            String strName = Objects.requireNonNull(et_name.getText()).toString();
            String strSeen = Objects.requireNonNull(et_last_seen.getText()).toString();
            String strCaption = Objects.requireNonNull(et_caption.getText()).toString();

            if (strName.isEmpty()) {
                et_name.setError("Enter Name");
            } else if (strSeen.isEmpty()) {
                et_last_seen.setError("Enter Last Seen");
            } else if (strCaption.isEmpty()) {
                et_caption.setError("Enter Caption");
            } else if (image_uri == null) {
                Toast.makeText(ActivityFakeStory.this, "Please! Select Image", Toast.LENGTH_SHORT).show();
            } else if (profile_uri == null) {
                Toast.makeText(ActivityFakeStory.this, "Please! Select Profile", Toast.LENGTH_SHORT).show();
            } else {
                Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                ShowDialog(animSlideUp, strName, strSeen, strCaption);
            }
        });
    }

    private void setTextInputLayoutHintColor(TextInputLayout text_input1, Context context, int colorBlack) {
        text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, colorBlack)));
    }


    private void SetMethod(CardView cv_help) {
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        new Handler().postDelayed(() -> {
            cv_help.setAnimation(animFadeOut);
            cv_help.setVisibility(View.GONE);
        }, 2000);
        if (cv_help.getVisibility() == View.GONE) {
            cv_help.setVisibility(View.VISIBLE);
            cv_help.setAnimation(animFadeIn);
        } else if (cv_help.getVisibility() == View.VISIBLE) {
            cv_help.setAnimation(animFadeOut);
            cv_help.setVisibility(View.GONE);
        }
    }

    private void ShowDialog(Animation animSlideUp, String strName, String strSeen, String strCaption) {
        rl_dialog.setVisibility(View.VISIBLE);
        rl_dialog.setAnimation(animSlideUp);
        new Handler().postDelayed(() -> {
            Advertisement.getInstance(ActivityFakeStory.this).showFullAds(() -> {
                Intent intent = new Intent(ActivityFakeStory.this, ViewFakeStory.class);
                intent.putExtra("strName", strName);
                intent.putExtra("strSeen", strSeen);
                intent.putExtra("strCaption", strCaption);
                intent.putExtra("image_uri", image_uri.toString());
                intent.putExtra("profile_uri", profile_uri.toString());
                startActivity(intent);
                rl_dialog.setVisibility(View.GONE);
            });
        }, 3000);
    }

    private void SelectImage() {
        CheckPermission(REQUEST_IMAGE);
    }

    private void SelectProfile() {
        CheckPermission(REQUEST_PROFILE);
    }

    private void CheckPermission(int REQUEST_CODE) {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            launchGalleryIntent(REQUEST_CODE);
                        } else {
                            showSettingsDialog(REQUEST_CODE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void launchGalleryIntent(int request_code) {
        Intent intent = new Intent(ActivityFakeStory.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, request_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                assert data != null;
                image_uri = data.getParcelableExtra("path");
                Glide.with(this).load(image_uri.toString())
                        .into(iv_thumbnail);
                iv_thumbnail.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
            } else if (requestCode == REQUEST_PROFILE) {
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

    private void showSettingsDialog(int REQUEST_CODE) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityFakeStory.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings(REQUEST_CODE);
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings(int REQUEST_CODE) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CODE);
    }
}