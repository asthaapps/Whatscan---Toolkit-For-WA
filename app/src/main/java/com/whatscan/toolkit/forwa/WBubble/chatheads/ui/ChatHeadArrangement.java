package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.os.Bundle;
import android.view.MotionEvent;

import com.facebook.rebound.Spring;

public abstract class ChatHeadArrangement {
    public abstract void bringToFront(ChatHead chatHead);

    public abstract boolean canDrag(ChatHead chatHead);

    public abstract Integer getHeroIndex();

    public abstract Bundle getRetainBundle();

    public boolean handleRawTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public abstract boolean handleTouchUp(ChatHead chatHead, int i, int i2, Spring spring, Spring spring2, boolean z);

    public abstract void onActivate(ChatHeadManager chatHeadManager, Bundle bundle, int i, int i2, boolean z);

    public abstract void onCapture(ChatHeadManager chatHeadManager, ChatHead chatHead);

    public abstract void onChatHeadAdded(ChatHead chatHead, boolean z);

    public abstract void onChatHeadRemoved(ChatHead chatHead);

    public abstract void onConfigChanged(ChatHeadConfig chatHeadConfig);

    public abstract void onDeactivate(int i, int i2);

    public abstract void onReloadFragment(ChatHead chatHead);

    public abstract void onSpringUpdate(ChatHead chatHead, boolean z, int i, int i2, Spring spring, Spring spring2, Spring spring3, int i3);

    public abstract void removeOldestChatHead();

    public abstract void selectChatHead(ChatHead chatHead);

    public abstract void setContainer(ChatHeadManager chatHeadManager);

    public abstract boolean shouldShowCloseButton(ChatHead chatHead);
}
