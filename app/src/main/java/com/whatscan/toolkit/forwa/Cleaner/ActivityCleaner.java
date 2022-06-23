package com.whatscan.toolkit.forwa.Cleaner;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class ActivityCleaner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityCleaner.this);
        setContentView(R.layout.activity_cleaner);

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityCleaner.this, ll_banner);

        TabLayout tab_cleaner = findViewById(R.id.tab_cleaner);
        ViewPager clean_viewPager = findViewById(R.id.clean_viewPager);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        RelativeLayout rl_cleaner = findViewById(R.id.rl_cleaner);
        View ic_include = findViewById(R.id.ic_include);

        la_info.setVisibility(View.VISIBLE);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.whatsCleaner) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_cleaner.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tab_cleaner.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tab_cleaner.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorWhite));
            tab_cleaner.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rl_cleaner.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tab_cleaner.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tab_cleaner.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
            tab_cleaner.setTabTextColors(ContextCompat.getColor(this, R.color.colorBlack), ContextCompat.getColor(this, R.color.colorAccent));
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/WhatsApp");
        File file1 = new File(Environment.getExternalStorageDirectory() + "/WhatsApp Business");

        File path11 = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp");
        File path11_b = new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (path11.exists() && path11_b.exists()) {
                if (path11.exists()) {
                    setupViewPager(clean_viewPager, "Wp");
                } else if (path11_b.exists()) {
                    setupViewPager(clean_viewPager, "BWp");
                }
            } else {
                if (path11.exists()) {
                    setupViewPagerEmptyWp(clean_viewPager, "Wp");
                } else if (path11_b.exists()) {
                    setupViewPagerEmptyWp(clean_viewPager, "BWp");
                }
            }
        } else {
            if (file.exists() && file1.exists()) {
                if (file.exists()) {
                    setupViewPager(clean_viewPager, "Wp");
                } else if (file1.exists()) {
                    setupViewPager(clean_viewPager, "BWp");
                }
            } else {
                if (file.exists()) {
                    setupViewPagerEmptyWp(clean_viewPager, "Wp");
                } else if (file1.exists()) {
                    setupViewPagerEmptyWp(clean_viewPager, "BWp");
                }
            }
        }

        tab_cleaner.setupWithViewPager(clean_viewPager);
        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(v -> com.whatscan.toolkit.forwa.Util.Constant.BottomSheetDialogView(ActivityCleaner.this, getString(R.string.whatsapp_cleaner), getString(R.string.intro_cleaner_text)));
    }

    private void setupViewPagerEmptyWp(ViewPager viewPager, String str) {
        Pager pager = new Pager(getSupportFragmentManager());
        if (str.equals("Wp")) {
            pager.addFragment(new WhatsAppData(), getString(R.string.whatsapp));
        } else if (str.equals("BWp")) {
            pager.addFragment(new WhatsAppBData(), getString(R.string.business));
        }
        viewPager.setAdapter(pager);
    }

    private void setupViewPager(ViewPager viewPager, String str) {
        Pager pager = new Pager(getSupportFragmentManager());
        if (str.equals("Wp")) {
            pager.addFragment(new WhatsAppData(), getString(R.string.whatsapp));
            pager.addFragment(new WhatsAppBData(), getString(R.string.business));
        } else if (str.equals("BWp")) {
            pager.addFragment(new WhatsAppBData(), getString(R.string.business));
            pager.addFragment(new WhatsAppData(), getString(R.string.whatsapp));
        }
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
}