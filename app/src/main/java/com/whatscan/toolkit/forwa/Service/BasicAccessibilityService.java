package com.whatscan.toolkit.forwa.Service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.WalkChat.ActivityWalkChat;
import com.whatscan.toolkit.forwa.WalkChat.CameraOverlay;

public class BasicAccessibilityService extends AccessibilityService {
    public Context context;
    public View view;
    private final AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
    ActivityManager objActivity;
    LayoutInflater layoutInflater;
    WindowManager.LayoutParams layoutParams;
    View view1;
    WindowManager windowManager;

    @SuppressLint({"ClickableViewAccessibility", "WrongConstant", "InflateParams"})
    public void onServiceConnected() {
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        accessibilityServiceInfo.eventTypes = -1;
        accessibilityServiceInfo.feedbackType = 16;
        accessibilityServiceInfo.notificationTimeout = 100;
        setServiceInfo(accessibilityServiceInfo);
        context = this;
        objActivity = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view1 = layoutInflater.inflate(R.layout.service_transnlat_text, null);
        layoutParams = new WindowManager.LayoutParams(50, 50, 2003, 40, -3);
        layoutParams.gravity = 51;
        layoutParams.x = 0;
        layoutParams.y = 0;
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = 32;
        accessibilityServiceInfo.feedbackType = 16;
        accessibilityServiceInfo.flags = 2;
        setServiceInfo(accessibilityServiceInfo);
    }

    private ActivityInfo m18198a(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @SuppressLint({"NewApi"})
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Object obj = null;
        try {
            if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && accessibilityEvent.getPackageName() != null && accessibilityEvent.getClassName() != null) {
                ComponentName componentName = new ComponentName(accessibilityEvent.getPackageName().toString(), accessibilityEvent.getClassName().toString());
                if (m18198a(componentName) != null) {
                    obj = 1;
                }
                if (obj == null) {
                    return;
                }
                if (componentName.getPackageName().equals("com.whatsapp")) {
                    if (ActivityWalkChat.isWalk) {
                        view = CameraOverlay.methOverlayCheck(this);
                        if (view != null) {
                            view.setAlpha(0.5f);
                        }
                    }
                } else if (componentName.getPackageName().equals("com.whatsapp.w4b")) {
                    if (ActivityWalkChat.isWalk) {
                        view = CameraOverlay.methOverlayCheck(this);
                        if (view != null) {
                            view.setAlpha(0.5f);
                        }
                    }
                } else if (view != null) {
                    CameraOverlay.methWinManager();
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void onInterrupt() {
    }
}