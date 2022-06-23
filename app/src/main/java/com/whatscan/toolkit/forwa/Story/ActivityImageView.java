package com.whatscan.toolkit.forwa.Story;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;
import com.leinardi.android.speeddial.SpeedDialView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class ActivityImageView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityImageView.this);
        setContentView(R.layout.activity_status_view);

        FindView();
    }

    private void FindView() {
        ImageView la_back = findViewById(R.id.la_back);
        ImageView iv_thumbnail = findViewById(R.id.iv_thumbnail);
        SpeedDialView speedDial = findViewById(R.id.speedDial);
        RelativeLayout rlStatusView = findViewById(R.id.rlStatusView);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlStatusView.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        } else {
            setStatusBar();
            rlStatusView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        la_back.setOnClickListener(v -> onBackPressed());

        Glide.with(this).load(getIntent().getStringExtra("path")).into(iv_thumbnail);

        if (getIntent().getStringExtra("strCheck").equals("clean")) {
            speedDial.setOnActionSelectedListener(actionItem -> {
                int id = actionItem.getId();
                if (id == R.id.delete) {
                    deleteFiles(getIntent().getStringExtra("path"));
                    speedDial.close();
                    return true;
                } else if (id == R.id.re_post) {
                    shareImageFile(ActivityImageView.this, getIntent().getStringExtra("path"));
                    return true;
                } else if (id == R.id.setAsWallpaper) {
                    setWallPaper(getIntent().getStringExtra("path"));
                    speedDial.close();
                    return true;
                } else if (id != R.id.share) {
                    return false;
                } else {
                    shareImageFile2(ActivityImageView.this, getIntent().getStringExtra("path"));
                    speedDial.close();
                    return true;
                }
            });
            speedDial.inflate(R.menu.menu_image_clean);
        } else {
            speedDial.setOnActionSelectedListener(actionItem -> {
                int id = actionItem.getId();
                if (id == R.id.delete) {
                    deleteFiles(getIntent().getStringExtra("path"));
                    speedDial.close();
                    return true;
                } else if (id == R.id.re_post) {
                    shareImageFile(ActivityImageView.this, getIntent().getStringExtra("path"));
                    return true;
                } else if (id == R.id.share) {
                    shareImageFile2(ActivityImageView.this, getIntent().getStringExtra("path"));
                    return true;
                } else if (id == R.id.setAsWallpaper) {
                    setWallPaper(getIntent().getStringExtra("path"));
                    speedDial.close();
                    return true;
                } else if (id != R.id.save) {
                    return false;
                } else {
                    downloadFile(ActivityImageView.this, getIntent().getStringExtra("path"));
                    speedDial.close();
                    return true;
                }
            });
            speedDial.inflate(R.menu.menu_image);
        }
    }

    private void setWallPaper(String path) {
        File imgFile = new File(path);
        Bitmap bitmap = null;
        if (imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        try {
            WallpaperManager.getInstance(getApplicationContext()).setBitmap(bitmap);
            Toast.makeText(this, "Wallpaper Set Successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Wallpaper Set Unsuccessfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadFile(Context context, String path) {
        File file = new File(path);
        try {
            copyFile(file, new File(Environment.getExternalStorageDirectory().toString() + "/" + context.getString(R.string.app_directory) + "/Images/" + file.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Advertisement.getInstance((Activity) context).showFullAds(() -> {
            Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
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