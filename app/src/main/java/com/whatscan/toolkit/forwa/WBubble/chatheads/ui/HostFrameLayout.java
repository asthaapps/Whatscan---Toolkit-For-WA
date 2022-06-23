package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class HostFrameLayout extends FrameLayout {
    private final ChatHeadContainer container;
    private final ChatHeadManager manager;

    public HostFrameLayout(Context context, ChatHeadContainer chatHeadContainer, ChatHeadManager chatHeadManager) {
        super(context);
        this.manager = chatHeadManager;
        this.container = chatHeadContainer;
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.manager.onMeasure(getMeasuredHeight(), getMeasuredWidth());
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.manager.onSizeChanged(i, i2, i3, i4);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        boolean dispatchKeyEvent = super.dispatchKeyEvent(keyEvent);
        if (dispatchKeyEvent || keyEvent.getAction() != 1 || keyEvent.getKeyCode() != 4) {
            return dispatchKeyEvent;
        }
        minimize();
        return true;
    }

    public void minimize() {
        if (!(this.manager.getActiveArrangement() instanceof MinimizedArrangement)) {
            this.manager.setArrangement(MinimizedArrangement.class, null, true);
        }
    }
}