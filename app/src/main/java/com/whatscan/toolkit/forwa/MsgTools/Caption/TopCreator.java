package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.Random;

public class TopCreator {
    Context context;

    public TopCreator(Context context2) {
        this.context = context2;
    }

    public Bitmap createImage(Typeface typeface, int i, int i2, float f, String str, boolean z, ArrayList<Integer> arrayList, int i3) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(1080, 900, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(f);
            textPaint.setTypeface(typeface);
            textPaint.setColor(i2);
            textPaint.setAntiAlias(true);
            if (i3 == 1) {
                textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                textPaint.setStrokeWidth(2.0f);
            } else if (i3 == 2) {
                textPaint.setStyle(Paint.Style.STROKE);
                textPaint.setStrokeWidth(3.0f);
            } else if (i3 == 3) {
                textPaint.setStyle(Paint.Style.FILL);
            }
            if (z) {
                Paint paint = new Paint();
                Random random = new Random();
                int intValue = arrayList.get(0);
                int intValue2 = arrayList.get(1);
                int nextInt = random.nextInt(3);
                if (nextInt == 0) {
                    paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, 1080.0f, intValue, intValue2, Shader.TileMode.CLAMP));
                } else if (nextInt == 1) {
                    paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, 1080.0f, intValue, intValue2, Shader.TileMode.MIRROR));
                } else if (nextInt == 2) {
                    paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, 1080.0f, intValue, intValue2, Shader.TileMode.REPEAT));
                }
                canvas.drawPaint(paint);
            } else {
                canvas.drawColor(i);
            }
            StaticLayout staticLayout = new StaticLayout(str, textPaint, canvas.getWidth() - 200, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            canvas.translate((float) ((canvas.getWidth() / 2) - (staticLayout.getWidth() / 2)), (float) ((canvas.getHeight() / 2) - (staticLayout.getHeight() / 2)));
            staticLayout.draw(canvas);
            canvas.save();
            canvas.restore();
        } catch (Exception ignored) {
        }
        return bitmap;
    }
}
