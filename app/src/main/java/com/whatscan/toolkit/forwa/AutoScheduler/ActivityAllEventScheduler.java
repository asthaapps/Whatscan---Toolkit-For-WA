package com.whatscan.toolkit.forwa.AutoScheduler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.GetSet.EventModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ActivityAllEventScheduler extends AppCompatActivity {
    public static ArrayList<EventModel> eventlist = new ArrayList<>();
    public static String sendcontactname;
    public static String contactname;
    public static String sendtoken;
    public static TabLayout tabLayout;
    public static boolean upswapdt = true;
    public int fragment_id;
    public ImageView la_info;
    public TextView tv_toolbar;
    public ImageView la_back;
    public FragmentEventsWp whatsappEvents;
    public FragmentEventsWpB fragmentEventsWpB;
    public EventsAdapterWp event_adapter;
    public FloatingActionButton addevent;
    public Date date1, date2;
    public RelativeLayout homelayout, ic_include;
    public ViewPager2 viewPager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_event_scheduler);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityAllEventScheduler.this, ll_banner);

        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        homelayout = findViewById(R.id.all_event_relativelayout);
        ic_include = findViewById(R.id.ic_include);
        viewPager = findViewById(R.id.viewpager);
        addevent = findViewById(R.id.btn_fload_add);
        tabLayout = findViewById(R.id.tablayout);
        la_info = findViewById(R.id.la_info);

        tv_toolbar.setText(Html.fromHtml("<small>" + "Whatsapp Scheduler" + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            homelayout.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorWhite));
            tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent));
            tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorBlack), ContextCompat.getColor(this, R.color.colorAccent));
        }

        fragmentEventsWpB = new FragmentEventsWpB();
        whatsappEvents = new FragmentEventsWp();
        viewPager.setAdapter(createCardAdapter());

        new TabLayoutMediator(tabLayout, viewPager, (tab, i) -> {
            if (i == 0) {
                tab.setText("Whatsapp");
            } else if (i == 1) {
                tab.setText("Business");
            }
        }).attach();

        Intent intent = getIntent();
        if (intent != null) {
            int intExtra = intent.getIntExtra("fragmentcode", 0);
            fragment_id = intExtra;
            viewPager.setCurrentItem(intExtra);
        }

        if (FragmentEventsWp.eventlistforwp.size() == 0) {
            la_info.setVisibility(View.GONE);
        } else {
            la_info.setVisibility(View.VISIBLE);
        }

        if (FragmentEventsWpB.eventlistforbwp.size() == 0) {
            la_info.setVisibility(View.GONE);
        } else {
            la_info.setVisibility(View.VISIBLE);
        }

        addevent.setOnClickListener(view -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem == 0) {
                Advertisement.getInstance(this).showFullAds(() -> {
                    ActivityAllEventScheduler.sendcontactname = null;
                    Intent intent1 = new Intent(ActivityAllEventScheduler.this, ActivityEventSchedule.class);
                    intent1.putExtra("PackageName", "com.whatsapp");
                    startActivity(intent1);
                });
            } else if (currentItem == 1) {
                Advertisement.getInstance(this).showFullAds(() -> {
                    ActivityAllEventScheduler.sendcontactname = null;
                    Intent intent2 = new Intent(ActivityAllEventScheduler.this, ActivityEventSchedule.class);
                    intent2.putExtra("PackageName", "com.whatsapp.w4b");
                    startActivity(intent2);
                });
            }
        });

        la_info.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem == 0) {
                ActivityAllEventScheduler.eventlist = FragmentEventsWp.eventlistforwp;
                event_adapter = FragmentEventsWp.event_adapter;
            } else if (currentItem == 1) {
                ActivityAllEventScheduler.eventlist = FragmentEventsWpB.eventlistforbwp;
                event_adapter = FragmentEventsWpB.event_adapter;
            }
            if (ActivityAllEventScheduler.upswapdt) {
                sortdatabydatedown();
                ActivityAllEventScheduler.upswapdt = false;
                return;
            }
            sortdatabydate();
            ActivityAllEventScheduler.upswapdt = true;
        });

        la_back.setOnClickListener(v -> onBackPressed());
    }

    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(whatsappEvents, getString(R.string.whatsapp));
        viewPagerAdapter.addFragment(fragmentEventsWpB, getString(R.string.business));
        return viewPagerAdapter;
    }

    @SuppressLint({"NewApi", "SimpleDateFormat", "NotifyDataSetChanged"})
    public void sortdatabydate() {
        Collections.sort(eventlist, (event_model, event_model2) -> {
            String str = event_model2.getDate() + "-" + event_model2.getTime();
            try {
                date1 = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(event_model.getDate() + "-" + event_model.getTime());
                date2 = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date2.compareTo(date1);
        });
        event_adapter.notifyDataSetChanged();
    }

    @SuppressLint({"NewApi", "SimpleDateFormat", "NotifyDataSetChanged"})
    private void sortdatabydatedown() {
        Collections.sort(eventlist, (event_model, event_model2) -> {
            String str = event_model2.getDate() + "-" + event_model2.getTime();
            try {
                date1 = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(event_model.getDate() + "-" + event_model.getTime());
                date2 = new SimpleDateFormat("dd-MM-yyyy-hh:mm").parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date1.compareTo(date2);
        });
        event_adapter.notifyDataSetChanged();
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

    public void setStatusBarTheme() {
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darkBlack));
    }

    public static class ViewPagerAdapter extends FragmentStateAdapter {
        public List<Fragment> fragment = new ArrayList<>();
        public List<String> tabtitle = new ArrayList<>();

        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public void addFragment(Fragment fragment2, String str) {
            fragment.add(fragment2);
            tabtitle.add(str);
        }

        @NonNull
        @Override
        public Fragment createFragment(int i) {
            return this.fragment.get(i);
        }

        @Override
        public int getItemCount() {
            return fragment.size();
        }
    }
}