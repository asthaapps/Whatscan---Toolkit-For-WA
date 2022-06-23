package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public interface ChatHeadContainer {
    void addView(View view, ViewGroup.LayoutParams layoutParams);

    void bringToFront(View view);

    ViewGroup.LayoutParams createLayoutParams(int i, int i2, int i3, int i4);

    DisplayMetrics getDisplayMetrics();

    int getViewX(View view);

    int getViewY(View view);

    void onArrangementChanged(ChatHeadArrangement chatHeadArrangement, ChatHeadArrangement chatHeadArrangement2);

    void onInitialized(ChatHeadManager chatHeadManager);

    void removeView(View view);

    void requestLayout();

    void setViewX(View view, int i);

    void setViewY(View view, int i);
}
