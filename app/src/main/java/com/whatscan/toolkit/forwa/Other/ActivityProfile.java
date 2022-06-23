package com.whatscan.toolkit.forwa.Other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.Ads.ActivityPremium;
import com.whatscan.toolkit.forwa.Prank.ImagePickerActivity;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.ReferCoin.ActivityCoin;
import com.whatscan.toolkit.forwa.ReferCoin.ActivityCoinHistory;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.bumptech.glide.Glide;

public class ActivityProfile extends AppCompatActivity {
    public ActivityResultLauncher<Intent> resultLauncher;
    public ImageView la_back;
    public RelativeLayout rlProfile, rl_coin;
    public ImageView imgPremium, imgGallery, imgProfile, imgCopy, imgShare, toolIdCopy;
    public TextView txtUsername, txtUserEmail, txtReferNo, tv_toolId, tvTotalCoin, txtSignOut, txtPurHistory, txtWallet;
    public LinearLayout llPurchaseList, llReferNo, llWallet, llSignOut;

    public ActivityProfile() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Glide.with(this).load(data.getParcelableExtra("path").toString()).into(imgProfile);
                    Preference.setProfile(data.getParcelableExtra("path").toString());
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityProfile.this);
        setContentView(R.layout.activity_profile);



        rlProfile = findViewById(R.id.rlProfile);
        rl_coin = findViewById(R.id.rl_coin);
        la_back = findViewById(R.id.la_back);
        txtUsername = findViewById(R.id.txtUsername);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtReferNo = findViewById(R.id.txtReferNo);
        tv_toolId = findViewById(R.id.tv_toolId);
        imgPremium = findViewById(R.id.imgPremium);
        imgGallery = findViewById(R.id.imgGallery);
        imgProfile = findViewById(R.id.imgProfile);
        imgCopy = findViewById(R.id.imgCopy);
        imgShare = findViewById(R.id.imgShare);
        toolIdCopy = findViewById(R.id.toolIdCopy);
        llPurchaseList = findViewById(R.id.llPurchaseList);
        llReferNo = findViewById(R.id.llReferNo);
        llWallet = findViewById(R.id.llWallet);
        llSignOut = findViewById(R.id.llSignOut);
        tvTotalCoin = findViewById(R.id.tvTotalCoin);
        txtSignOut = findViewById(R.id.txtSignOut);
        txtPurHistory = findViewById(R.id.txtPurHistory);
        txtWallet = findViewById(R.id.txtWallet);

        if (Preference.getTotal_coin().isEmpty() || Preference.getTotal_coin().equals("0")) {
            tvTotalCoin.setText("0");
        } else {
            tvTotalCoin.setText(Preference.getTotal_coin());
        }
        tv_toolId.setText(Preference.getTool_id());
        toolIdCopy.setOnClickListener(v -> Utils.setClipboard(ActivityProfile.this, Preference.getTool_id()));

        if (Preference.getLogin_status().equals("Yes")) {
            llSignOut.setVisibility(View.VISIBLE);
        } else {
            llSignOut.setVisibility(View.GONE);
        }

        if (!Preference.getUserName().isEmpty()) {
            txtUsername.setText(Html.fromHtml(Preference.getUserName().substring(0, 1).toUpperCase() + Preference.getUserName().substring(1)));
        }

        if (Preference.getEmail().isEmpty()) {
            txtUserEmail.setVisibility(View.GONE);
            txtReferNo.setVisibility(View.VISIBLE);
        } else {
            txtUserEmail.setText(Preference.getEmail());
            txtReferNo.setText(Preference.getReferedCode());
        }

        if (Preference.getProfile().isEmpty()) {
            Glide.with(ActivityProfile.this).load(R.drawable.ic_profile).placeholder(R.drawable.ic_profile).into(imgProfile);
        } else {
            Glide.with(ActivityProfile.this).load(Preference.getProfile()).placeholder(R.drawable.ic_profile).into(imgProfile);
        }

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_coin.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white_coin_w));
            rlProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            llPurchaseList.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_black));
            llWallet.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_black));
            llReferNo.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_black));
            llSignOut.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_black));
            tvTotalCoin.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtUsername.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtUserEmail.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_toolId.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSignOut.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtPurHistory.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtWallet.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtReferNo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            rl_coin.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white_coin));
            setStatusBar();
            rlProfile.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_color));
            llPurchaseList.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_card));
            llWallet.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_card));
            llReferNo.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_card));
            llSignOut.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_card));
            tvTotalCoin.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtUsername.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtUserEmail.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            tv_toolId.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtSignOut.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtPurHistory.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtWallet.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtReferNo.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        la_back.setOnClickListener(v -> onBackPressed());

        llPurchaseList.setOnClickListener(v -> startActivity(new Intent(ActivityProfile.this, ActivityCoinHistory.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        llWallet.setOnClickListener(v -> startActivity(new Intent(ActivityProfile.this, ActivityCoin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        imgCopy.setOnClickListener(v -> Utils.setClipboard(ActivityProfile.this, Preference.getReferedCode()));

        imgGallery.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityProfile.this, ImagePickerActivity.class);
            intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1);
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
            resultLauncher.launch(intent);
        });

        imgShare.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String shareBody = Preference.getReferedCode();
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent, "Select file"));
        });

        imgPremium.setOnClickListener(v -> startActivity(new Intent(ActivityProfile.this, ActivityPremium.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        llSignOut.setOnClickListener(v -> {
            Preference.setLogin_status("");
            Preference.setProfile("");
            Preference.setEmail("");
            Preference.setUserName("");
            Preference.setGoogle_key("");
            Preference.setFacebook_key("");
            Preference.setActive_CkeyM("");
            Preference.setActive_CkeyY("");
            Preference.setActive_PkeyM("");
            Preference.setActive_PkeyY("");
            Preference.setActive_MkeyM("");
            Preference.setActive_MkeyY("");
            Preference.setActive_offer("");
            Preference.setActive_vip("");
            Preference.setTotal_coin("");
            Preference.setReferedCode("");
            Preference.setAds_one_day("");
            Preference.setAds_three_day("");
            Preference.setAds_seven_day("");
            Preference.setAds_thirty_day("");
            Preference.setBulk_one_day("");
            Preference.setBulk_seven_day("");
            Preference.setAuto_one_day("");
            Preference.setAuto_seven_day("");
            Preference.setImport_one_day("");
            Preference.setImport_seven_day("");
            startActivity(new Intent(ActivityProfile.this, ActivityMain.class).addFlags(268468224));
        });
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
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.bg_color));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.bg_color));
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
