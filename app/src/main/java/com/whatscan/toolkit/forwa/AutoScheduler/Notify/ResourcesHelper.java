package com.whatscan.toolkit.forwa.AutoScheduler.Notify;

import android.content.Context;

public class ResourcesHelper {
    public static String getStringResourceByKey(Context context, String str) {
        return context.getResources().getString(context.getResources().getIdentifier(str, "string", context.getPackageName()));
    }
}
