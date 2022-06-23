package com.whatscan.toolkit.forwa.Notification;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.whatscan.toolkit.forwa.ActivityHome;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.AutoResponse.ActivityAutoResponse;
import com.whatscan.toolkit.forwa.BulkSender.BulkActivityStart;
import com.whatscan.toolkit.forwa.Cleaner.ActivityCleaner;
import com.whatscan.toolkit.forwa.MsgTools.Caption.ActivityCaptionStatus;
import com.whatscan.toolkit.forwa.MsgTools.Emoticon.ActivityEmoticon;
import com.whatscan.toolkit.forwa.MsgTools.MagicText.ActivityMagicText;
import com.whatscan.toolkit.forwa.Prank.FakeChat.ActivityFakeChat;
import com.whatscan.toolkit.forwa.Story.ActivityStorySaver;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WBubble.ActivityWABubble;
import com.whatscan.toolkit.forwa.WSticker.ActivitySticker;
import com.whatscan.toolkit.forwa.WalkChat.ActivityWalkChat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    String imageUrl, code, title, message, timestamp, I_status;
    Intent intent;
    NotificationUtils notificationUtils;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Preference.setFcm_Id(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> map = remoteMessage.getData();
        I_status = map.get("I_status");
        code = map.get("code");
        timestamp = map.get("timestamp");

        if (I_status.equals("0")) {
            title = map.get("title");
            message = map.get("body");
        } else if (I_status.equals("1")) {
            title = map.get("title");
            imageUrl = map.get("image");
            message = map.get("body");
        }

        intent = getIntentName(code);

        if (remoteMessage.getNotification() != null) {

            if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                if (!TextUtils.isEmpty(imageUrl) || imageUrl != null) {
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, intent, imageUrl, timestamp);
                } else {
                    showNotificationMessage(getApplicationContext(), title, message, intent, imageUrl, timestamp);
                }
            } else {
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                if (!TextUtils.isEmpty(imageUrl) || imageUrl != null) {
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, intent, imageUrl, timestamp);
                } else {
                    showNotificationMessage(getApplicationContext(), title, message, intent, imageUrl, timestamp);
                }
            }
        } else {
            if (map.size() > 0) {

                if (imageUrl != null || !TextUtils.isEmpty(null)) {
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, intent, imageUrl, timestamp);
                } else {
                    showNotificationMessage(getApplicationContext(), title, message, intent, imageUrl, timestamp);
                }
            }
        }
    }

    private Intent getIntentName(String code) {
        Intent intent1;
        switch (code) {
            case "0":
                intent1 = new Intent(AppController.getApp(), ActivityHome.class);
                break;
            case "1":
                intent1 = new Intent(AppController.getApp(), BulkActivityStart.class);
                break;
            case "2":
                intent1 = new Intent(AppController.getApp(), ActivityAutoResponse.class);
                break;
            case "3":
                intent1 = new Intent(AppController.getApp(), ActivityStorySaver.class);
                break;
            case "4":
                intent1 = new Intent(AppController.getApp(), ActivityWABubble.class);
                break;
            case "5":
                intent1 = new Intent(AppController.getApp(), ActivityMagicText.class);
                break;
            case "6":
                intent1 = new Intent(AppController.getApp(), ActivityFakeChat.class);
                break;
            case "7":
                intent1 = new Intent(AppController.getApp(), ActivityWalkChat.class);
                break;
            case "8":
                intent1 = new Intent(AppController.getApp(), ActivityCleaner.class);
                break;
            case "9":
                intent1 = new Intent(AppController.getApp(), ActivityEmoticon.class);
                break;
            case "10":
                intent1 = new Intent(AppController.getApp(), ActivityCaptionStatus.class);
                break;
            case "11":
                intent1 = new Intent(AppController.getApp(), ActivitySticker.class);
                break;
            default:
                intent1 = new Intent(AppController.getApp(), ActivityMain.class);
                break;
        }

        return intent1;
    }

    private void showNotificationMessage(Context context, String title, String message, Intent intent, String imageUrl, String timestamp) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, imageUrl, timestamp, intent);
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message, Intent intent, String imageUrl, String timestamp) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationUtils.showNotificationMessage(title, message, imageUrl, timestamp, intent);
    }
}