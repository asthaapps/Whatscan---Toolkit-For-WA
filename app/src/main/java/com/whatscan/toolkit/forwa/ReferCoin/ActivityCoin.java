package com.whatscan.toolkit.forwa.ReferCoin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.whatscan.toolkit.forwa.Adapter.Pager;
import com.whatscan.toolkit.forwa.Other.ActivityProfile;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.User.ActivityLogin;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.onLoginCoinEventListener;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCoin extends AppCompatActivity implements onLoginCoinEventListener {
    public TabLayout tab_icon;
    public ViewPager viewPager;
    public TextView tv_toolbar, txtTotalCoin, txtHistory;
    public ImageView imgBanner, imgHistory, la_back;
    public CircleImageView imgProfile;
    public RelativeLayout rlCoinMain, rlProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityCoin.this);
        setContentView(R.layout.activity_coin);
        findView();
    }

    @Override
    public void coinEvent(String s) {
        ((TextView) findViewById(R.id.tvTotalCoin)).setText(s);
    }

    private void findView() {
        rlCoinMain = findViewById(R.id.rlCoinMain);
        rlProfile = findViewById(R.id.rlProfile);
        imgBanner = findViewById(R.id.imgBanner);
        imgHistory = findViewById(R.id.imgHistory);
        tab_icon = findViewById(R.id.tab_coin);
        viewPager = findViewById(R.id.viewPager);
        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        txtTotalCoin = findViewById(R.id.tvTotalCoin);
        txtHistory = findViewById(R.id.txtHistory);
        imgProfile = findViewById(R.id.imgProfile);

        tv_toolbar.setText(Html.fromHtml("<small>" + "Collect Coins" + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlCoinMain.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            imgBanner.setImageResource(R.drawable.coin_banner_w);
            tab_icon.setBackground(ContextCompat.getDrawable(this, R.drawable.dialog_black));
            tab_icon.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorWhite));
            tab_icon.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.colorWhite));
            viewPager.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }

        if (Preference.getTotal_coin().isEmpty() || Preference.getTotal_coin().equals("0")) {
            txtTotalCoin.setText("0");
        } else {
            txtTotalCoin.setText(Preference.getTotal_coin());
        }

        imgHistory.setOnClickListener(v -> startActivity(new Intent(ActivityCoin.this, ActivityRedemptions.class)));

        if (Preference.getProfile().isEmpty()) {
            Glide.with(ActivityCoin.this).load(R.drawable.ic_profile).placeholder(R.drawable.ic_profile).into(imgProfile);
        } else {
            Glide.with(ActivityCoin.this).load(Preference.getProfile()).placeholder(R.drawable.ic_profile).into(imgProfile);
        }

        setupViewPager(viewPager);
        tab_icon.setupWithViewPager(viewPager);

        la_back.setOnClickListener(v -> onBackPressed());

        txtHistory.setOnClickListener(v -> {
            startActivity(new Intent(ActivityCoin.this, ActivityCoinHistory.class));
        });


        rlProfile.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                startActivity(new Intent(ActivityCoin.this, ActivityProfile.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            } else {
                startActivity(new Intent(ActivityCoin.this, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        Pager pager = new Pager(getSupportFragmentManager());
        pager.addFragment(new FragmentCoin(), "Coin");
        pager.addFragment(new FragmentRedeem(), "Redeem");
        viewPager.setAdapter(pager);
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent));
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