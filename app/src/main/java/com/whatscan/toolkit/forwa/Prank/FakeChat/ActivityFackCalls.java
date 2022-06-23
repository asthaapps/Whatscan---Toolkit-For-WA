package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.whatscan.toolkit.forwa.GetSet.StatusData;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityFackCalls extends AppCompatActivity {
    public Handler customHandler = new Handler(Looper.myLooper());
    public long startTime = 0;
    public Runnable updateTimerThread = new updateTimeThreadListner();
    LinearLayout msg;
    LinearLayout mute;
    LinearLayout speaker;
    ImageView endCall;
    TextView Name;
    ImageView profileImages;
    ImageView rlUserImages;
    String profilename;
    TextView times;
    long timeInMilliseconds = 0;
    long timeSwapBuff = 0;
    long updatedTime = 0;
    List<StatusData> calldatalist = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorChat));
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityFackCalls.this);
        setContentView(R.layout.activity_fack_calls);

        Name = findViewById(R.id.txtname);
        times = findViewById(R.id.txtTime);
        profileImages = findViewById(R.id.rlProfile);
        rlUserImages = findViewById(R.id.rlUser);
        endCall = findViewById(R.id.imEndCall);
        speaker = findViewById(R.id.speaker);
        msg = findViewById(R.id.msg);
        mute = findViewById(R.id.mute);
        speaker.setOnClickListener(new btnSpeakerListner());
        msg.setOnClickListener(new btnMsgListner());
        mute.setOnClickListener(new btnMuteListner());

        boolean typeOne = getIntent().getBooleanExtra("TYPEONE", false);
        boolean typeTwo = getIntent().getBooleanExtra("TYPETWO", false);

        if (typeOne) {
            profilename = getIntent().getExtras().getString("NAME");
            if (FragmentCalls.selectedImageUri != null) {
                Bitmap mBitmap = null;
                try {
                    mBitmap = Media.getBitmap(getContentResolver(), FragmentCalls.selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profileImages.setBackground(new BitmapDrawable(getResources(), mBitmap));
            } else {
                profileImages.setBackground(ContextCompat.getDrawable(ActivityFackCalls.this, R.drawable.user_photo));
            }
        } else if (typeTwo) {
            profilename = getIntent().getExtras().getString("NAMENEW");
            if (FragmentCalls.selectedImageUrinew != null && FragmentCalls.selectedImageUri != null) {
                Bitmap mBitmap = null;
                Bitmap mBitmap2 = null;
                try {
                    mBitmap = Media.getBitmap(getContentResolver(), FragmentCalls.selectedImageUrinew);
                    mBitmap2 = Media.getBitmap(getContentResolver(), FragmentCalls.selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rlUserImages.setBackground(new BitmapDrawable(getResources(), mBitmap));
                profileImages.setBackground(new BitmapDrawable(getResources(), mBitmap2));
            } else {
                rlUserImages.setBackground(ContextCompat.getDrawable(ActivityFackCalls.this, R.drawable.user_photo));
                profileImages.setBackground(ContextCompat.getDrawable(ActivityFackCalls.this, R.drawable.user_photo));
            }
        }

        Name.setText(profilename);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        endCall.setOnClickListener(new btnEndCallListner());

        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat month_date = new SimpleDateFormat("dd MMMM hh:mm a");
        String month_name = month_date.format(cal.getTime());

        List<StatusData> list = new Gson().fromJson(getApplicationContext().getSharedPreferences("data", 0).getString("mycall", ""), new TypeToken<List<StatusData>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            calldatalist = list;
        }

        if (typeOne) {
            calldatalist.add(new StatusData(profilename, month_name, "a", "", "", String.valueOf(FragmentCalls.selectedImageUri), "", 0));
            SharedPreferences.Editor edit = getApplicationContext().getSharedPreferences("data", 0).edit();
            edit.putString("mycall", new Gson().toJson(calldatalist));
            edit.apply();
        } else if (typeTwo) {
            calldatalist.add(new StatusData(profilename, month_name, "v", "", String.valueOf(FragmentCalls.selectedImageUrinew), String.valueOf(FragmentCalls.selectedImageUri), "", 0));
            SharedPreferences.Editor edit = getApplicationContext().getSharedPreferences("data", 0).edit();
            edit.putString("mycall", new Gson().toJson(calldatalist));
            edit.apply();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private static class btnSpeakerListner implements OnClickListener {
        public void onClick(View v) {
        }
    }

    private static class btnMsgListner implements OnClickListener {
        public void onClick(View v) {
        }
    }

    private static class btnMuteListner implements OnClickListener {
        public void onClick(View v) {
        }
    }

    private class btnEndCallListner implements OnClickListener {
        public void onClick(View view) {
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);
            finish();
        }
    }

    private class updateTimeThreadListner implements Runnable {
        @SuppressLint("DefaultLocale")
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs %= 60;
            times.setText("" + String.format("%02d", mins) + " : " + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }
    }
}