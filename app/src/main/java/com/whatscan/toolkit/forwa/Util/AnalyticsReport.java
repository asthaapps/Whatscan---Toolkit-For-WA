package com.whatscan.toolkit.forwa.Util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsReport {
    public static void addEvent(Context context, String str, Bundle bundle) {
        FirebaseAnalytics.getInstance(context).logEvent(str, bundle);
    }

    public static void setScreen(Activity activity, String str, String str2) {
        FirebaseAnalytics.getInstance(activity).setCurrentScreen(activity, str, str2);
    }
}
