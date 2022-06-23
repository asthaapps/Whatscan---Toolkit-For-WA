package com.whatscan.toolkit.forwa.WBubble.chatheads;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class ChatHeadUtils {
    public static int dpToPx(DisplayMetrics displayMetrics, int i) {
        float applyDimension = TypedValue.applyDimension(1, (float) i, displayMetrics);
        if (applyDimension < 1.0f) {
            applyDimension = 1.0f;
        }
        return (int) applyDimension;
    }

    public static int dpToPx(Context context, int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return dpToPx(displayMetrics, i);
    }

    public static int pxToDp(Context context, int i) {
        return Math.round(((float) i) / (context.getResources().getDisplayMetrics().xdpi / 160.0f));
    }
}
