package com.whatscan.toolkit.forwa.WBubble.util;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.whatscan.toolkit.forwa.Service.AllNotificationService;

public class setDrawable extends AsyncTask<String, Void, Drawable> {
    ImageView imageView;

    public setDrawable(ImageView imageView2) {
        this.imageView = imageView2;
    }

    public Drawable doInBackground(String... strArr) {
        return AllNotificationService.chatHeadService.setChatHeadDrawable(strArr[0], strArr[1]);
    }

    public void onPostExecute(final Drawable drawable) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                if (setDrawable.this.imageView != null) {
                    setDrawable.this.imageView.setImageDrawable(drawable);
                }
            }
        });
    }
}