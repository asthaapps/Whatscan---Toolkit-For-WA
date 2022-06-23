package com.whatscan.toolkit.forwa.AutoScheduler.Notify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.StrictMode;

import androidx.core.view.ViewCompat;

import java.io.IOException;
import java.net.URL;

public class BitmapHelper {
    public static Bitmap getBitmapFromUrl(String str) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        try {
            return BitmapFactory.decodeStream(new URL(str).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromRes(Context context, int i) {
        return BitmapFactory.decodeResource(context.getResources(), i);
    }

    public static Bitmap toCircleBitmap(Bitmap bitmap) {
        Bitmap bitmap2;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            bitmap2 = Bitmap.createBitmap(bitmap, (bitmap.getWidth() - bitmap.getHeight()) / 2, 0, bitmap.getHeight(), bitmap.getHeight());
        } else {
            bitmap2 = Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() - bitmap.getWidth()) / 2, bitmap.getWidth(), bitmap.getWidth());
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, rect, rect, paint);
        return createBitmap;
    }
}
