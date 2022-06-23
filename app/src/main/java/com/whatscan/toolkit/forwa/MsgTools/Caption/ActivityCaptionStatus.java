package com.whatscan.toolkit.forwa.MsgTools.Caption;

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
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.Pager;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.material.tabs.TabLayout;

public class ActivityCaptionStatus extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityCaptionStatus.this);
        setContentView(R.layout.activity_caption_status);

        Utils.CheckConnection(ActivityCaptionStatus.this, findViewById(R.id.rl_caption_status));

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityCaptionStatus.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        TabLayout tab_story = findViewById(R.id.tab_story);
        ViewPager viewPager = findViewById(R.id.viewPager);
        RelativeLayout rl_caption_status = findViewById(R.id.rl_caption_status);
        View ic_include = findViewById(R.id.ic_include);

        la_info.setVisibility(View.VISIBLE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.caption_status) + "</small>"));

        if (Preference.getBooleanTheme(false)){
            setStatusBarTheme();
            rl_caption_status.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tab_story.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tab_story.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorWhite));
            tab_story.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.colorWhite));
        }

        setupViewPager(viewPager);
        tab_story.setupWithViewPager(viewPager);

        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(v -> Constant.BottomSheetDialogView(ActivityCaptionStatus.this, getString(R.string.caption_status), getString(R.string.text_emoji_msg)));
    }

    private void setupViewPager(ViewPager viewPager) {
        Pager pager = new Pager(getSupportFragmentManager());
        pager.addFragment(new CaptionFragment(), getString(R.string.caption));
        pager.addFragment(new FavouriteFragment(), getString(R.string.txt_favourite));
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
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivityCaptionStatus.this, findViewById(R.id.rl_caption_status), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}