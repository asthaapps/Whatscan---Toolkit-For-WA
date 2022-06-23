package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.whatscan.toolkit.forwa.GetSet.StatusData;
import com.whatscan.toolkit.forwa.GetSet.otherStatusData;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoriesProgressActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {
    static String type = "";
    LinearLayout bottom_lay;
    TextView captiontxt;
    long limit = 500;
    View next;
    long pressTime = 0;
    View previuos;
    CircleImageView profile_images;
    LinearLayout replylay;
    ImageView selectstorie;
    SimpleDateFormat simpleDateFormat;
    int total = 0;
    List<StatusData> totallist = new ArrayList<>();
    TextView totalview;
    TextView tvNames;
    TextView tvTimes;
    LinearLayout viewlay;
    private StoriesProgressView storiesProgressView;
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                pressTime = System.currentTimeMillis();
                storiesProgressView.pause();
                return false;
            } else if (action != 1) {
                return false;
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                storiesProgressView.resume();
                return limit < currentTimeMillis - pressTime;
            }
        }
    };

    public void onPointerCaptureChanged(boolean z) {
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), StoriesProgressActivity.this);
        setContentView(R.layout.activity_stories_progress);

        tvNames = findViewById(R.id.tvNames);
        tvTimes = findViewById(R.id.tvTimes);
        totalview = findViewById(R.id.totalview);
        previuos = findViewById(R.id.previuos);
        next = findViewById(R.id.next);
        captiontxt = findViewById(R.id.captiontxt);
        replylay = findViewById(R.id.reply);
        viewlay = findViewById(R.id.view);
        profile_images = findViewById(R.id.profile_images);
        selectstorie = findViewById(R.id.selectstorie);
        bottom_lay = findViewById(R.id.bottom_lay);
        storiesProgressView = findViewById(R.id.stories);

        type = getIntent().getStringExtra("type");
        getdata(type);

        storiesProgressView.setStoriesCount(totallist.size());
        storiesProgressView.setStoryDuration(3000);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories();

        simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

        if (totallist.get(0).getImageurl().length() > 0) {
            Glide.with(this).load(totallist.get(0).getImageurl()).into(selectstorie);
        }
        tvNames.setText(totallist.get(0).getName());

        try {
            String[] split = totallist.get(0).getTimeanddate().split(" ");
            SimpleDateFormat simpleDateFormat2 = simpleDateFormat;
            Date parse = simpleDateFormat2.parse(split[1] + " " + split[0] + ":00");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append("");
            printDifference(parse, sb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            TextView textView = tvTimes;
            textView.setText("Today, " + totallist.get(0).getTimeanddate().split(" ")[0]);
        }

        if (totallist.get(0).getProfileurl().length() > 3) {
            Glide.with(this).load(totallist.get(0).getProfileurl()).into(profile_images);
        } else {
            profile_images.setImageResource(R.drawable.ic_profile);
        }

        totalview.setText(totallist.get(0).getView());
        totallist.get(0).setViewedcompleted(1);
        if (totallist.get(0).getText().length() > 0) {
            captiontxt.setVisibility(View.VISIBLE);
            bottom_lay.setBackgroundResource(R.color.colorBlack);
            captiontxt.setText(totallist.get(0).getText());
        } else {
            bottom_lay.setBackgroundResource(0);
            captiontxt.setVisibility(View.GONE);
        }

        previuos.setOnTouchListener(onTouchListener);
        next.setOnTouchListener(onTouchListener);

        previuos.setOnClickListener(view -> storiesProgressView.reverse());

        next.setOnClickListener(view -> storiesProgressView.skip());

        findViewById(R.id.backact).setOnClickListener(view -> {
            updateList();
            finish();
        });
    }

    @Override
    public void onNext() {
        int size = totallist.size();
        int i = total + 1;
        if (size == i) {
            updateList();
            finish();
            return;
        }
        total = i;
        if (totallist.get(i).getText().length() > 0) {
            captiontxt.setVisibility(View.VISIBLE);
            captiontxt.setText(totallist.get(total).getText());
        } else {
            captiontxt.setVisibility(View.GONE);
        }
        totallist.get(total).setViewedcompleted(1);
        Glide.with(this).load(totallist.get(total).getImageurl()).into(selectstorie);
        try {
            String[] split = totallist.get(total).getTimeanddate().split(" ");
            SimpleDateFormat simpleDateFormat2 = simpleDateFormat;
            Date parse = simpleDateFormat2.parse(split[1] + " " + split[0] + ":00");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]);
            sb.append("");
            printDifference(parse, sb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            TextView textView = tvTimes;
            textView.setText("Today, " + totallist.get(total).getTimeanddate().split(" ")[0]);
        }
        totalview.setText(totallist.get(total).getView());
    }

    @Override
    public void onPrev() {
        int i = total;
        if (i > 0) {
            int i2 = i - 1;
            total = i2;
            if (totallist.get(i2).getText().length() > 0) {
                captiontxt.setVisibility(View.VISIBLE);
                captiontxt.setText(totallist.get(total).getText());
            } else {
                captiontxt.setVisibility(View.GONE);
            }
            Glide.with(this).load(totallist.get(total).getImageurl()).into(selectstorie);
            try {
                String[] split = totallist.get(total).getTimeanddate().split(" ");
                SimpleDateFormat simpleDateFormat2 = simpleDateFormat;
                Date parse = simpleDateFormat2.parse(split[1] + " " + split[0] + ":00");
                StringBuilder sb = new StringBuilder();
                sb.append(split[0]);
                sb.append("");
                printDifference(parse, sb.toString());
            } catch (ParseException e) {
                e.printStackTrace();
                TextView textView = tvTimes;
                textView.setText("Today, " + totallist.get(total).getTimeanddate().split(" ")[0]);
            }
            totalview.setText(totallist.get(total).getView());
        }
    }

    @Override
    public void onComplete() {
        updateList();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        storiesProgressView.destroy();
    }

    public List<StatusData> getdata(String str) {
        if (str.equals("mystatus")) {
            replylay.setVisibility(View.GONE);
            viewlay.setVisibility(View.VISIBLE);
            findViewById(R.id.cusmenu).setVisibility(View.GONE);
            totallist = new Gson().fromJson(getApplicationContext().getSharedPreferences("data", 0).getString("mystatus", ""), new TypeToken<List<StatusData>>() {
            }.getType());
            if (totallist == null || totallist.size() <= 0) {
                Toast.makeText(this, "Data Not Found!!!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                return totallist;
            }
        } else {
            replylay.setVisibility(View.VISIBLE);
            viewlay.setVisibility(View.VISIBLE);
            List<otherStatusData> list2 = new Gson().fromJson(getApplicationContext().getSharedPreferences("data", 0).getString("other", ""), new TypeToken<List<otherStatusData>>() {
            }.getType());
            if (list2 == null || list2.size() <= 0) {
                Toast.makeText(this, "Data Not Found!!!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                for (int i = 0; i < list2.size(); i++) {
                    if (list2.get(i).getOthername().equals(str)) {
                        totallist = list2.get(i).getDataList();
                        return list2.get(i).getDataList();
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    public void updateList() {
        if (type.equals("mystatus")) {
            SharedPreferences.Editor edit = getApplicationContext().getSharedPreferences("data", 0).edit();
            edit.putString("mystatus", new Gson().toJson(totallist));
            edit.apply();
            return;
        }

        List<otherStatusData> list = new Gson().fromJson(getApplicationContext().getSharedPreferences("data", 0).getString("other", ""), new TypeToken<List<otherStatusData>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getOthername().equals(type)) {
                    list.remove(i);
                    list.add(new otherStatusData(totallist.get(0).getName(), totallist));
                    SharedPreferences.Editor edit2 = getApplicationContext().getSharedPreferences("data", 0).edit();
                    edit2.putString("other", new Gson().toJson(list));
                    edit2.apply();
                    return;
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void printDifference(Date date, String str) {
        Date date2;
        try {
            date2 = simpleDateFormat.parse(new SimpleDateFormat("dd/M/yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            TextView textView = tvTimes;
            textView.setText("Today, " + str);
            date2 = null;
        }
        long time = date2.getTime() - date.getTime();
        PrintStream printStream = System.out;
        printStream.println("startDate : " + date2);
        PrintStream printStream2 = System.out;
        printStream2.println("endDate : " + date);
        PrintStream printStream3 = System.out;
        printStream3.println("different : " + time);
        long j = time / 86400000;
        long j2 = time % 86400000;
        long j3 = j2 / 3600000;
        long j4 = j2 % 3600000;
        long j5 = j4 / 60000;
        long j6 = (j4 % 60000) / 1000;
        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", j, j3, j5, j6);
        if (j > 0) {
            if (j == 1) {
                tvTimes.setText("yesterday, " + str);
                return;
            }
            tvTimes.setText(j + " day ago");
        } else if (j3 > 0) {
            if (j3 > 6) {
                tvTimes.setText("Today, " + str);
                return;
            }
            tvTimes.setText(j3 + " hours ago");
        } else if (j5 > 0) {
            if (j5 > 59) {
                tvTimes.setText("1 hours ago");
                return;
            }
            tvTimes.setText(j5 + " minutes ago");
        } else if (j6 > 0 && j6 < 59) {
            tvTimes.setText("Just now");
        }
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        } else if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        } else {
            window.clearFlags(0);
        }
    }

    @Override
    public void onBackPressed() {
        updateList();
        finish();
    }
}