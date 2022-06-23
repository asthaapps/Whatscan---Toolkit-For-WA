package com.whatscan.toolkit.forwa.AutoResponse;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.Pager;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.MainService;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.material.tabs.TabLayout;

public class ActivityAutoResponse extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityAutoResponse.this);
        setContentView(R.layout.activity_auto_response);

        Utils.CheckConnection(ActivityAutoResponse.this, findViewById(R.id.rlAutoResponse));
        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityAutoResponse.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        ImageView la_info = findViewById(R.id.la_info);
        TabLayout tab_delete = findViewById(R.id.tab_delete);
        ViewPager viewPager = findViewById(R.id.viewPager);
        RelativeLayout rlAutoResponse = findViewById(R.id.rlAutoResponse);
        RelativeLayout ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.auto_response) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlAutoResponse.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tab_delete.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tab_delete.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorWhite));
            tab_delete.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.colorWhite));
        }

        if (MainService.contactsList != null) {
            MainService.contactsList.contactArrayList.clear();
        }

        Preference.setBoolean(this, "settings", "reply_continuously_radio", true);
        Preference.setBoolean(this, "servicecheck", "reply_once_radio", false);

        setupViewPager(viewPager);
        tab_delete.setupWithViewPager(viewPager);

        la_info.setOnClickListener(v -> startActivity(new Intent(ActivityAutoResponse.this, ActivityAutoSetting.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));
        la_back.setOnClickListener(v -> onBackPressed());
    }

    private void setupViewPager(ViewPager viewPager) {
        Pager pager = new Pager(getSupportFragmentManager());
        pager.addFragment(new FragmentHome(), getString(R.string.menu_home));
        pager.addFragment(new FragmentContact(), getString(R.string.contacts));
        viewPager.setAdapter(pager);
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
        finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivityAutoResponse.this, findViewById(R.id.rlAutoResponse), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}