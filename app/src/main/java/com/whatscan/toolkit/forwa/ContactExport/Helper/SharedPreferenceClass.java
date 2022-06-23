package com.whatscan.toolkit.forwa.ContactExport.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceClass {
    static SharedPreferences sharedPreferences;

    public static void setString(Context context, String str, String str2) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = defaultSharedPreferences;
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static void setInt(Context context, String str, int i) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = defaultSharedPreferences;
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static int getInt(Context context, String str, int i) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = defaultSharedPreferences;
        return defaultSharedPreferences.getInt(str, i);
    }

    public static String getString(Context context, String str, String str2) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = defaultSharedPreferences;
        return defaultSharedPreferences.getString(str, str2);
    }

    public static void setBoolean(Context context, String str, Boolean bool) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = defaultSharedPreferences;
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putBoolean(str, bool);
        edit.apply();
    }

    public static Boolean getBoolean(Context context, String str, Boolean bool) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences = defaultSharedPreferences;
        return defaultSharedPreferences.getBoolean(str, bool);
    }

    public static void saveIntegerPrefrence(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static int getIntegerPrefrence(Context context, String str, int i) {
        return context.getSharedPreferences(context.getPackageName(), 0).getInt(str, i);
    }

    public static void saveStringPrefrence(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(context.getPackageName(), 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String getStringPrefrence(Context context, String str, String str2) {
        return context.getSharedPreferences(context.getPackageName(), 0).getString(str, str2);
    }
}
