package com.whatscan.toolkit.forwa.Story;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.leinardi.android.speeddial.SpeedDialView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class ActivityVideoView extends AppCompatActivity {
    public MediaController mediaControls;
    public File file;
    public VideoView videoView;
    public int stopPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlack));
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityVideoView.this);
        setContentView(R.layout.activity_video_view);

        FindView();
    }

    private void FindView() {
        ImageView la_back = findViewById(R.id.la_back);

        LottieAnimationView la_play = findViewById(R.id.la_play);
        SpeedDialView speedDial = findViewById(R.id.speedDial);
        videoView = findViewById(R.id.videoView);

        la_back.setOnClickListener(v -> onBackPressed());

        la_play.setOnClickListener(v -> {
            videoView.start();
            la_play.setVisibility(View.GONE);
        });

        file = new File(getIntent().getStringExtra("path"));
        if (file.exists()) {
            videoView = findViewById(R.id.videoView);
            if (mediaControls == null) {
                mediaControls = new MediaController(ActivityVideoView.this);
                mediaControls.setAnchorView(videoView);
            }
            videoView.setMediaController(mediaControls);
            videoView.setVideoPath(file.getPath());
            videoView.requestFocus();
            videoView.start();
            videoView.setOnPreparedListener(mp -> la_play.setVisibility(View.GONE));
            videoView.setOnCompletionListener(mp -> la_play.setVisibility(View.VISIBLE));
        }

        if (getIntent().getStringExtra("strCheck").equals("clean")) {
            speedDial.setOnActionSelectedListener(actionItem -> {
                int id = actionItem.getId();
                if (id == R.id.delete) {
                    deleteFiles(getIntent().getStringExtra("path"));
                    speedDial.close();
                    return true;
                } else if (id == R.id.re_post) {
                    shareImageFile(ActivityVideoView.this, getIntent().getStringExtra("path"));
                    return true;
                } else if (id != R.id.share) {
                    return false;
                } else {
                    shareImageFile2(ActivityVideoView.this, getIntent().getStringExtra("path"));
                    return true;
                }
            });
            speedDial.inflate(R.menu.menu_context_clean);
        } else {
            speedDial.setOnActionSelectedListener(actionItem -> {
                int id = actionItem.getId();
                if (id == R.id.delete) {
                    deleteFiles(getIntent().getStringExtra("path"));
                    speedDial.close();
                    return true;
                } else if (id == R.id.re_post) {
                    shareImageFile(ActivityVideoView.this, getIntent().getStringExtra("path"));
                    return true;
                } else if (id == R.id.share) {
                    shareImageFile2(ActivityVideoView.this, getIntent().getStringExtra("path"));
                    return true;
                } else if (id != R.id.save) {
                    return false;
                } else {
                    downloadFile(ActivityVideoView.this, getIntent().getStringExtra("path"));
                    speedDial.close();
                    return true;
                }
            });
            speedDial.inflate(R.menu.menu_context);
        }
    }

    private void downloadFile(Context context, String path) {
        File file = new File(path);
        try {
            copyFile(file, new File(Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_directory) + "/Videos/" + file.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Advertisement.getInstance((Activity) context).showFullAds(() -> {
            Toast.makeText(context, "Video Saved!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(file));
            context.sendBroadcast(intent);
        });
    }

    private void copyFile(File file, File file1) throws IOException {
        if (!Objects.requireNonNull(file1.getParentFile()).exists()) {
            file1.getParentFile().mkdirs();
        }
        if (!file1.exists()) {
            file1.createNewFile();
        }
        FileChannel channel = new FileInputStream(file).getChannel();
        FileChannel channel2 = new FileOutputStream(file1).getChannel();
        channel2.transferFrom(channel, 0, channel.size());
        channel.close();
        channel2.close();
    }

    private void shareImageFile2(Context context, String path) {
        if (new File(path).exists()) {
            Uri parse = Uri.parse(path);
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", parse);
            context.startActivity(Intent.createChooser(intent, "Share Photo Via"));
        }
    }

    private void shareImageFile(Context context, String path) {
        if (new File(path).exists()) {
            Uri parse = Uri.parse(path);
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.setPackage("com.whatsapp");
            intent.putExtra("android.intent.extra.STREAM", parse);
            context.startActivity(Intent.createChooser(intent, "Share Photo Via"));
        }
    }

    private void deleteFiles(String path) {
        if (deleteFile(path)) {
            finish();
        } else {
            Toast.makeText(this, "Failed to delete file", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean deleteFile(String str) {
        File file = new File(str);
        return file.exists() && file.delete();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            stopPosition = videoView.getCurrentPosition();
            videoView.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (stopPosition == videoView.getDuration()) {
                stopPosition = 0;
            }
            videoView.seekTo(stopPosition);
            videoView.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}