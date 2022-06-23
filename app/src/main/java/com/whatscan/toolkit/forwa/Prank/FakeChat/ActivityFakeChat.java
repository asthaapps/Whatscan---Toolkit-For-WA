package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.whatscan.toolkit.forwa.Adapter.Pager;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.tabs.TabLayout;

public class ActivityFakeChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorChat));
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityFakeChat.this);
        setContentView(R.layout.activity_fake_chat);

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityFakeChat.this, ll_banner);

        TabLayout tl_chat = findViewById(R.id.tl_chat);
        ViewPager viewpager = findViewById(R.id.viewpager);

        setupViewPager(viewpager);
        tl_chat.setupWithViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewpager) {
        Pager pager = new Pager(getSupportFragmentManager());
        pager.addFragment(new FragmentChats(), "CHATS");
        pager.addFragment(new FragmentStatus(), "STATUS");
        pager.addFragment(new FragmentCalls(), "CALLS");
        viewpager.setAdapter(pager);
    }
}