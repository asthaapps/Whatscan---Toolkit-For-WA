package com.whatscan.toolkit.forwa.DeletedMedia;

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
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class ActivityDeleteMedia extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityDeleteMedia.this);
        setContentView(R.layout.activity_delete_media);

        Utils.CheckConnection(ActivityDeleteMedia.this, findViewById(R.id.rl_delete));

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityDeleteMedia.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        TabLayout tab_delete = findViewById(R.id.tab_delete);
        ViewPager viewPager = findViewById(R.id.viewPager);
        RelativeLayout rl_delete = findViewById(R.id.rl_delete);
        View ic_include = findViewById(R.id.ic_include);

        la_info.setVisibility(View.VISIBLE);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_delete.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tab_delete.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tab_delete.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorWhite));
            tab_delete.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rl_delete.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tab_delete.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tab_delete.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
            tab_delete.setTabTextColors(ContextCompat.getColor(this, R.color.colorBlack), ContextCompat.getColor(this, R.color.colorAccent));
        }

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.deleted_message) + "</small>"));

        setupViewPager(viewPager);
        tab_delete.setupWithViewPager(viewPager);
        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(v -> com.whatscan.toolkit.forwa.Util.Constant.BottomSheetDialogView(ActivityDeleteMedia.this, getString(R.string.deleted_message_title), getString(R.string.deleted_message_text)));
        CheckCachedFile();
    }

    private void CheckCachedFile() {
        try {
            File[] listFiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_directory) + "/.cached Files").listFiles();
            for (File file : listFiles) {
                if (file.exists()) {
                    Calendar instance = Calendar.getInstance();
                    instance.add(6, -3);
                    Date date = new Date(file.lastModified());
                    if (date.before(instance.getTime())) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        Pager pager = new Pager(getSupportFragmentManager());
        pager.addFragment(new FragmentChat(), getString(R.string.chats));
        pager.addFragment(new FragmentMedia(), getString(R.string.media));
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
        Utils.showSnack(ActivityDeleteMedia.this, findViewById(R.id.rl_delete), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}