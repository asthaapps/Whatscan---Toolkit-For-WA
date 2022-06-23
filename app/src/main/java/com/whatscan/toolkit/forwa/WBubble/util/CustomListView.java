package com.whatscan.toolkit.forwa.WBubble.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.Service.AllNotificationService;

public class CustomListView extends RecyclerView {
    SharedPreferences I;

    public CustomListView(Context context) {
        super(context);
        this.I = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public CustomListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.I = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public CustomListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.I = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!AllNotificationService.chatHeadService.lastScroll || !this.I.getBoolean("slideUp", false)) {
            return super.onTouchEvent(motionEvent);
        }
        if (motionEvent.getAction() == 1) {
            return super.onTouchEvent(motionEvent);
        }
        super.onTouchEvent(motionEvent);
        return false;
    }
}
