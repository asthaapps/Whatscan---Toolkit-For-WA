package com.whatscan.toolkit.forwa.WBubble.chatheads.ui.container;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHead;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadArrangement;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadManager;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.FrameChatHeadContainer;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.HostFrameLayout;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.MaximizedArrangement;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.MinimizedArrangement;
import com.google.android.material.badge.BadgeDrawable;

public class WindowManagerContainer extends FrameChatHeadContainer {
    private ChatHeadArrangement currentArrangement;
    private View motionCaptureView;
    private boolean motionCaptureViewAdded;
    private WindowManager windowManager;

    public WindowManagerContainer(Context context) {
        super(context);
    }

    @Override
    public void onInitialized(ChatHeadManager chatHeadManager) {
        super.onInitialized(chatHeadManager);
        this.motionCaptureView = new MotionCaptureView(getContext());
        this.motionCaptureView.setOnTouchListener(new MotionCapturingTouchListener());
        registerReceiver(getContext());
    }

    public void registerReceiver(Context context) {
        context.registerReceiver(new BroadcastReceiver() {
            /* class com.chatapp.wabubbleforchat.chatheads.ui.container.WindowManagerContainer.AnonymousClass1 */

            public void onReceive(Context context, Intent intent) {
                HostFrameLayout frameLayout = WindowManagerContainer.this.getFrameLayout();
                if (frameLayout != null) {
                    frameLayout.minimize();
                }
            }
        }, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    public WindowManager getWindowManager() {
        if (this.windowManager == null) {
            this.windowManager = (WindowManager) getContext().getSystemService("window");
        }
        return this.windowManager;
    }

    public void setContainerHeight(View view, int i) {
        WindowManager.LayoutParams orCreateLayoutParamsForContainer = getOrCreateLayoutParamsForContainer(view);
        orCreateLayoutParamsForContainer.height = i;
        getWindowManager().updateViewLayout(view, orCreateLayoutParamsForContainer);
    }

    public void setContainerWidth(View view, int i) {
        WindowManager.LayoutParams orCreateLayoutParamsForContainer = getOrCreateLayoutParamsForContainer(view);
        orCreateLayoutParamsForContainer.width = i;
        getWindowManager().updateViewLayout(view, orCreateLayoutParamsForContainer);
    }

    public WindowManager.LayoutParams getOrCreateLayoutParamsForContainer(View view) {
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            return layoutParams;
        }
        WindowManager.LayoutParams createContainerLayoutParams = createContainerLayoutParams(false);
        view.setLayoutParams(createContainerLayoutParams);
        return createContainerLayoutParams;
    }

    public void setContainerX(View view, int i) {
        WindowManager.LayoutParams orCreateLayoutParamsForContainer = getOrCreateLayoutParamsForContainer(view);
        orCreateLayoutParamsForContainer.x = i;
        getWindowManager().updateViewLayout(view, orCreateLayoutParamsForContainer);
    }

    public int getContainerX(View view) {
        return getOrCreateLayoutParamsForContainer(view).x;
    }

    public void setContainerY(View view, int i) {
        WindowManager.LayoutParams orCreateLayoutParamsForContainer = getOrCreateLayoutParamsForContainer(view);
        orCreateLayoutParamsForContainer.y = i;
        getWindowManager().updateViewLayout(view, orCreateLayoutParamsForContainer);
    }

    public int getContainerY(View view) {
        return getOrCreateLayoutParamsForContainer(view).y;
    }

    public WindowManager.LayoutParams createContainerLayoutParams(boolean z) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -2, Build.VERSION.SDK_INT >= 26 ? 2038 : 2002, 262152, -3);
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.gravity = BadgeDrawable.TOP_START;
        return layoutParams;
    }

    @Override
    public void addContainer(View view, boolean z) {
        addContainer(view, createContainerLayoutParams(z));
    }

    public void addContainer(View view, WindowManager.LayoutParams layoutParams) {
        try {
            view.setLayoutParams(layoutParams);
            getWindowManager().addView(view, layoutParams);
        } catch (Exception unused) {
        }
    }

    @Override
    public void setViewX(View view, int i) {
        super.setViewX(view, i);
        if ((view instanceof ChatHead) && ((ChatHead) view).isHero() && (this.currentArrangement instanceof MinimizedArrangement)) {
            setContainerX(this.motionCaptureView, i);
            setContainerWidth(this.motionCaptureView, view.getMeasuredWidth());
        }
    }

    @Override
    public void setViewY(View view, int i) {
        super.setViewY(view, i);
        if ((view instanceof ChatHead) && (this.currentArrangement instanceof MinimizedArrangement) && ((ChatHead) view).isHero()) {
            setContainerY(this.motionCaptureView, i);
            setContainerHeight(this.motionCaptureView, view.getMeasuredHeight());
        }
    }

    @Override
    public void onArrangementChanged(ChatHeadArrangement chatHeadArrangement, ChatHeadArrangement chatHeadArrangement2) {
        this.currentArrangement = chatHeadArrangement2;
        if (!(chatHeadArrangement instanceof MinimizedArrangement) || !(chatHeadArrangement2 instanceof MaximizedArrangement)) {
            WindowManager.LayoutParams orCreateLayoutParamsForContainer = getOrCreateLayoutParamsForContainer(this.motionCaptureView);
            orCreateLayoutParamsForContainer.flags |= 8;
            orCreateLayoutParamsForContainer.flags &= -17;
            orCreateLayoutParamsForContainer.flags |= 32;
            View view = this.motionCaptureView;
            if (!(view == null || view.getWindowToken() == null)) {
                this.windowManager.updateViewLayout(this.motionCaptureView, orCreateLayoutParamsForContainer);
            }
            WindowManager.LayoutParams orCreateLayoutParamsForContainer2 = getOrCreateLayoutParamsForContainer(getFrameLayout());
            orCreateLayoutParamsForContainer2.flags |= 24;
            this.windowManager.updateViewLayout(getFrameLayout(), orCreateLayoutParamsForContainer2);
            return;
        }
        WindowManager.LayoutParams orCreateLayoutParamsForContainer3 = getOrCreateLayoutParamsForContainer(this.motionCaptureView);
        orCreateLayoutParamsForContainer3.flags |= 24;
        this.windowManager.updateViewLayout(this.motionCaptureView, orCreateLayoutParamsForContainer3);
        WindowManager.LayoutParams orCreateLayoutParamsForContainer4 = getOrCreateLayoutParamsForContainer(getFrameLayout());
        orCreateLayoutParamsForContainer4.flags &= -9;
        orCreateLayoutParamsForContainer4.flags &= -17;
        orCreateLayoutParamsForContainer4.flags |= 32;
        this.windowManager.updateViewLayout(getFrameLayout(), orCreateLayoutParamsForContainer4);
        setContainerX(this.motionCaptureView, 0);
        setContainerY(this.motionCaptureView, 0);
        setContainerWidth(this.motionCaptureView, getFrameLayout().getMeasuredWidth());
        setContainerHeight(this.motionCaptureView, getFrameLayout().getMeasuredHeight());
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, layoutParams);
        if (!this.motionCaptureViewAdded && getManager().getChatHeads().size() > 0) {
            addContainer(this.motionCaptureView, true);
            WindowManager.LayoutParams orCreateLayoutParamsForContainer = getOrCreateLayoutParamsForContainer(this.motionCaptureView);
            orCreateLayoutParamsForContainer.width = 0;
            orCreateLayoutParamsForContainer.height = 0;
            this.windowManager.updateViewLayout(this.motionCaptureView, orCreateLayoutParamsForContainer);
            this.motionCaptureViewAdded = true;
        }
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        if (getManager().getChatHeads().size() == 0) {
            this.windowManager.removeViewImmediate(this.motionCaptureView);
            this.motionCaptureViewAdded = false;
        }
    }

    private void removeContainer(View view) {
        this.windowManager.removeView(view);
    }

    public void destroy() {
        View view = this.motionCaptureView;
        if (view != null) {
            this.windowManager.removeViewImmediate(view);
        }
        this.windowManager.removeViewImmediate(getFrameLayout());
    }

    protected class MotionCapturingTouchListener implements View.OnTouchListener {
        protected MotionCapturingTouchListener() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            motionEvent.offsetLocation((float) WindowManagerContainer.this.getContainerX(view), (float) WindowManagerContainer.this.getContainerY(view));
            HostFrameLayout frameLayout = WindowManagerContainer.this.getFrameLayout();
            if (frameLayout != null) {
                return frameLayout.dispatchTouchEvent(motionEvent);
            }
            return false;
        }
    }

    private class MotionCaptureView extends View {
        public MotionCaptureView(Context context) {
            super(context);
        }
    }
}