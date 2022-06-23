package com.whatscan.toolkit.forwa.Cleaner;

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
import com.whatscan.toolkit.forwa.Adapter.TabsAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.tabs.TabLayout;

public class TabLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), TabLayoutActivity.this);
        setContentView(R.layout.activity_tab_layout_cleaner);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(TabLayoutActivity.this, ll_banner);

        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        ImageView la_back = findViewById(R.id.la_back);
        TabLayout tabLayout = findViewById(R.id.tab_cleaner);
        ViewPager viewPager = findViewById(R.id.clean_viewPager);
        RelativeLayout rl_cleaner = findViewById(R.id.rl_cleaner);
        View ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.whatsCleaner) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_cleaner.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorWhite));
            tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rl_cleaner.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
            tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorBlack), ContextCompat.getColor(this, R.color.colorAccent));
        }

        String category = getIntent().getStringExtra("category");
        String pathname = getIntent().getStringExtra("pathname");
        TabsAdapter tabsAdapter;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            switch (category) {
                case Constant.IMAGE:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.IMAGE, Constant.imagesReceivedPath11, Constant.imagesSentPath11);
                    break;
                case Constant.DOCUMENT:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.DOCUMENT, Constant.documentsReceivedPath11, Constant.documentsSentPath11);
                    break;
                case Constant.VIDEO:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.VIDEO, Constant.videosReceivedPath11, Constant.videosSentPath11);
                    break;
                case Constant.AUDIO:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.AUDIO, Constant.audiosReceivedPath11, Constant.audiosSentPath11);
                    break;
                case Constant.GIF:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.GIF, Constant.gifReceivedPath11, Constant.gifSentPath11);
                    break;
                case Constant.WALLPAPER:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.WALLPAPER, Constant.wallReceivedPath11, Constant.wallgifSentPath11);
                    break;
                case Constant.VOICE:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.VOICE, Constant.voiceReceivedPath11, Constant.voicegifSentPath11);
                    break;
                case Constant.STATUS:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.STATUS, Constant.statuscache11, Constant.statusdownload11);
                    break;
                default:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.NONDEFAULT, pathname, pathname);
                    break;
            }
        } else {
            switch (category) {
                case Constant.IMAGE:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.IMAGE, Constant.imagesReceivedPath, Constant.imagesSentPath);
                    break;
                case Constant.DOCUMENT:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.DOCUMENT, Constant.documentsReceivedPath, Constant.documentsSentPath);
                    break;
                case Constant.VIDEO:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.VIDEO, Constant.videosReceivedPath, Constant.videosSentPath);
                    break;
                case Constant.AUDIO:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.AUDIO, Constant.audiosReceivedPath, Constant.audiosSentPath);
                    break;
                case Constant.GIF:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.GIF, Constant.gifReceivedPath, Constant.gifSentPath);
                    break;
                case Constant.WALLPAPER:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.WALLPAPER, Constant.wallReceivedPath, Constant.wallgifSentPath);
                    break;
                case Constant.VOICE:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.VOICE, Constant.voiceReceivedPath, Constant.voicegifSentPath);
                    break;
                case Constant.STATUS:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.STATUS, Constant.statuscache, Constant.statusdownload);
                    break;
                default:
                    tabsAdapter = new TabsAdapter(getSupportFragmentManager(), Constant.NONDEFAULT, pathname, pathname);
                    break;
            }
        }

        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        la_back.setOnClickListener(v -> onBackPressed());
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