package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

public abstract class FrameChatHeadContainer implements ChatHeadContainer {
    private final Context context;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    private HostFrameLayout frameLayout;
    private ChatHeadManager manager;

    public FrameChatHeadContainer(Context context2) {
        this.context = context2;
    }

    public abstract void addContainer(View view, boolean z);

    public ChatHeadManager getManager() {
        return this.manager;
    }

    @Override
    public void onInitialized(ChatHeadManager chatHeadManager) {
        this.manager = chatHeadManager;
        HostFrameLayout hostFrameLayout = new HostFrameLayout(this.context, this, chatHeadManager);
        hostFrameLayout.setFocusable(true);
        hostFrameLayout.setFocusableInTouchMode(true);
        this.frameLayout = hostFrameLayout;
        addContainer(hostFrameLayout, false);
    }

    public Context getContext() {
        return this.context;
    }

    public HostFrameLayout getFrameLayout() {
        return this.frameLayout;
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        HostFrameLayout hostFrameLayout = this.frameLayout;
        if (hostFrameLayout != null) {
            hostFrameLayout.addView(view, layoutParams);
        }
    }

    @Override
    public void requestLayout() {
        HostFrameLayout hostFrameLayout = this.frameLayout;
        if (hostFrameLayout != null) {
            hostFrameLayout.requestLayout();
        }
    }

    @Override
    public void removeView(View view) {
        HostFrameLayout hostFrameLayout = this.frameLayout;
        if (hostFrameLayout != null) {
            hostFrameLayout.removeView(view);
        }
    }

    @Override
    public ViewGroup.LayoutParams createLayoutParams(int i, int i2, int i3, int i4) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(i2, i);
        layoutParams.gravity = i3;
        layoutParams.bottomMargin = i4;
        return layoutParams;
    }

    @Override
    public void setViewX(View view, int i) {
        view.setTranslationX((float) i);
    }

    @Override
    public void setViewY(View view, int i) {
        view.setTranslationY((float) i);
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay().getMetrics(this.displayMetrics);
        return this.displayMetrics;
    }

    @Override
    public int getViewX(View view) {
        return (int) view.getTranslationX();
    }

    @Override
    public int getViewY(View view) {
        return (int) view.getTranslationY();
    }

    @Override
    public void bringToFront(View view) {
        view.bringToFront();
    }
}
