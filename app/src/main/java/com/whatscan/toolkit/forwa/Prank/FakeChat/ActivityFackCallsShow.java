package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;

import java.io.IOException;

public class ActivityFackCallsShow extends AppCompatActivity {
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorChat));
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityFackCallsShow.this);
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
            String image_path = getIntent().getStringExtra("PROFILEPIC");
            if (image_path != null) {
                Bitmap mBitmap = null;
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image_path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profileImages.setBackground(new BitmapDrawable(getResources(), mBitmap));
            } else {
                profileImages.setBackground(ContextCompat.getDrawable(ActivityFackCallsShow.this, R.drawable.user_photo));
            }
        } else if (typeTwo) {
            profilename = getIntent().getExtras().getString("NAMENEW");
            String image_path = getIntent().getStringExtra("PROFILEPIC");
            String image_pathnew = getIntent().getStringExtra("PROFILEPICNEW");
            if (image_path != null && image_pathnew != null) {
                Bitmap mBitmap = null;
                Bitmap mBitmap2 = null;
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image_path));
                    mBitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image_pathnew));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rlUserImages.setBackground(new BitmapDrawable(getResources(), mBitmap));
                profileImages.setBackground(new BitmapDrawable(getResources(), mBitmap2));
            } else {
                rlUserImages.setBackground(ContextCompat.getDrawable(ActivityFackCallsShow.this, R.drawable.user_photo));
                profileImages.setBackground(ContextCompat.getDrawable(ActivityFackCallsShow.this, R.drawable.user_photo));
            }
        }

        Name.setText(profilename);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        endCall.setOnClickListener(new btnEndCallListner());
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