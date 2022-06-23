package com.whatscan.toolkit.forwa.WBubble.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.whatscan.toolkit.forwa.Service.AllNotificationService;

public class RelativeCustom extends RelativeLayout {
    public RelativeCustom(Context context) {
        super(context);
    }

    @RequiresApi(api = 21)
    public RelativeCustom(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public RelativeCustom(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public RelativeCustom(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            AllNotificationService.chatHeadService.lastScroll = false;
        }
        return super.onTouchEvent(motionEvent);
    }
}
