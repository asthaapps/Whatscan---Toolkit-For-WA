package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.DeleteStatusadapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.GetSet.otherStatusData;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DeleteStoryActivity extends AppCompatActivity {
    RecyclerView rvohterStatus;
    List<otherStatusData> totalotherlist = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), DeleteStoryActivity.this);
        setContentView(R.layout.activity_delete_story);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(DeleteStoryActivity.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.delete_status) + "</small>"));

        la_back.setOnClickListener(v -> onBackPressed());

        rvohterStatus = findViewById(R.id.rvdeleteStatus);
        rvohterStatus.setLayoutManager(new LinearLayoutManager(this));

        updateRecent();
    }

    public void updateRecent() {
        if (getOtherData().size() > 0) {
            rvohterStatus.setAdapter(new DeleteStatusadapter(this, totalotherlist, this));
        }
    }

    public List<otherStatusData> getOtherData() {
        totalotherlist = new Gson().fromJson(getApplicationContext().getSharedPreferences("data", 0).getString("other", ""), new TypeToken<List<otherStatusData>>() {
        }.getType());
        if (totalotherlist == null || totalotherlist.size() <= 0) {
            return new ArrayList<>();
        }
        return totalotherlist;
    }

    @Override
    public void onBackPressed() {
        finish();
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
}