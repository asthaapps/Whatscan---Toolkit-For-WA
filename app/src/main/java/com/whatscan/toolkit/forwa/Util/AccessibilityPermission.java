package com.whatscan.toolkit.forwa.Util;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.NotificationManagerCompat;

public class AccessibilityPermission {

    public static boolean isAccessibilityOn(Activity activity, Class<? extends AccessibilityService> cls) {
        int i;
        String str = activity.getPackageName() + "/" + cls.getCanonicalName();
        try {
            i = Settings.Secure.getInt(activity.getApplicationContext().getContentResolver(), "accessibility_enabled");
        } catch (Settings.SettingNotFoundException unused) {
            i = 0;
        }
        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
        if (i == 1) {
            String string = Settings.Secure.getString(activity.getApplicationContext().getContentResolver(), "enabled_accessibility_services");
            if (string != null) {
                simpleStringSplitter.setString(string);
                while (simpleStringSplitter.hasNext()) {
                    if (simpleStringSplitter.next().equalsIgnoreCase(str)) {
                        Constant.addEvent(activity, Event.AccessibilityServiceOn.name(), null);
                        return true;
                    }
                }
            }
        }
        Constant.addEvent(activity, Event.AccessibilityServiceOff.name(), null);
        return false;
    }

    public static boolean isAccessibilityw4bOn(Activity activity, Class<? extends AccessibilityService> cls) {
        int i;
        String str = activity.getPackageName() + "/" + cls.getCanonicalName();
        try {
            i = Settings.Secure.getInt(activity.getApplicationContext().getContentResolver(), "accessibility_enabled");
        } catch (Settings.SettingNotFoundException unused) {
            i = 0;
        }
        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
        if (i == 1) {
            String string = Settings.Secure.getString(activity.getApplicationContext().getContentResolver(), "enabled_accessibility_services");
            if (string != null) {
                simpleStringSplitter.setString(string);
                while (simpleStringSplitter.hasNext()) {
                    if (simpleStringSplitter.next().equals(str)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean CheckNoti(Activity activity) {
        boolean z = false;
        for (String str : NotificationManagerCompat.getEnabledListenerPackages(activity)) {
            if (str.equals(activity.getPackageName())) {
                z = true;
            }
        }
        return z;
    }

    public static void NotiPermission(Activity activity) {
        activity.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }
}