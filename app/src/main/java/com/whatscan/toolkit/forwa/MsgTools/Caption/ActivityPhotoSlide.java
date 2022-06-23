package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.ArrayList;

public class ActivityPhotoSlide extends AppCompatActivity {
    private ArrayList<String> list;
    private ViewPager viewPager;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityPhotoSlide.this);
        setContentView(R.layout.activity_photoslide);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        RelativeLayout rl_photo_slide = findViewById(R.id.rl_photo_slide);
        View ic_include = findViewById(R.id.ic_include);

        la_info.setVisibility(View.GONE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.caption_status) + "</small>"));

        if (Preference.getBooleanTheme(false)){
            setStatusBarTheme();
            rl_photo_slide.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }


        la_back.setOnClickListener(v -> onBackPressed());

        viewpager();
    }

    private void viewpager() {
        new Thread(() -> {
            list = getIntent().getStringArrayListExtra("paths");
            new Handler(Looper.getMainLooper()).post(() -> {
                if (list != null) {
                    viewPager = (ViewPager) findViewById(R.id.viewpager);
                    viewPager.setAdapter(new adapter(getSupportFragmentManager(), 1, list));
                    viewPager.setPageTransformer(true, new vanim());
                    viewPager.setCurrentItem(getIntent().getIntExtra("position", 0));
                }
            });
        }).start();
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
        ActivityPhotoSlide.this.finish();
    }

    private static class adapter extends FragmentPagerAdapter {
        private final ArrayList<String> paths;

        public adapter(FragmentManager fragmentManager, int i, ArrayList<String> arrayList) {
            super(fragmentManager, i);
            this.paths = arrayList;
        }

        @Override
        public Fragment getItem(int i) {
            return SingleImageFragment.newinstance(this.paths.get(i));
        }

        @Override
        public int getCount() {
            return paths.size();
        }
    }

    private class vanim implements ViewPager.PageTransformer {
        private static final float MIN_ALPHA = 0.2f;
        private static final float MIN_SCALE = 0.6f;

        @Override
        public void transformPage(View view, float f) {
            if (f < -1.0f) {
                view.setAlpha(0.0f);
            } else if (f <= 1.0f) {
                view.setScaleX(Math.max((float) MIN_SCALE, 1.0f - Math.abs(f)));
                view.setScaleY(Math.max((float) MIN_SCALE, 1.0f - Math.abs(f)));
                view.setAlpha(Math.max((float) MIN_ALPHA, 1.0f - Math.abs(f)));
            } else {
                view.setAlpha(0.0f);
            }
        }
    }
}
