package com.whatscan.toolkit.forwa.WBubble.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.whatscan.toolkit.forwa.R;

public class Constant {
    public static int[] chatbg = {R.drawable.msg_bg_1, R.drawable.msg_bg_2, R.drawable.msg_bg_3, R.drawable.msg_bg_4, R.drawable.msg_bg_5};
    public static int[] chatbottom = {R.drawable.bottom_1, R.drawable.bottom_1, R.drawable.bottom_2, R.drawable.bottom_2, R.drawable.bottom_3};
    public static int[] chatheader = {R.drawable.top_1, R.drawable.top_2, R.drawable.top_2, R.drawable.top_3, R.drawable.top_4};
    public static String prefname = "whatsapp";

    public static void saveInt(Context context, String str, String str2, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putInt(str2, i);
        edit.apply();
    }

    public static Integer getInt(Context context, String str, String str2, int i) {
        return context.getSharedPreferences(str, 0).getInt(str2, i);
    }

    public static void setBoolean(Context context, String str, String str2, Boolean bool) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putBoolean(str2, bool);
        edit.apply();
    }

    public static Boolean getBoolean(Context context, String str, String str2, Boolean bool) {
        return context.getSharedPreferences(str, 0).getBoolean(str2, bool);
    }
}
