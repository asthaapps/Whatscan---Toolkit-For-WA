package com.whatscan.toolkit.forwa.Service;

import android.app.PendingIntent;
import android.os.Bundle;

import androidx.core.app.RemoteInput;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NotificationWear {
    public static Map<String, Bundle> bundleMap = new HashMap();
    public static Map<String, String> notificationID = new HashMap();
    public static Map<String, PendingIntent> openConv = new HashMap();
    public static Map<String, PendingIntent> readIntent = new HashMap();
    public static Map<String, RemoteInput[]> remoteMap = new HashMap();
    public static Map<String, PendingIntent> replyIntent = new HashMap();
    public String id = UUID.randomUUID().toString();
    public String packageName = "";
}
