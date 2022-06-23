package com.whatscan.toolkit.forwa.Service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.whatscan.toolkit.forwa.DataBaseHelper.ChatHistoryDatabaseHelper;
import com.whatscan.toolkit.forwa.DataBaseHelper.ContactDatabaseHelper;
import com.whatscan.toolkit.forwa.DataBaseHelper.CustomReplyDatabaseHelper;
import com.whatscan.toolkit.forwa.DataBaseHelper.DBHelper;
import com.whatscan.toolkit.forwa.DataBaseHelper.GroupDatabaseHelper;
import com.whatscan.toolkit.forwa.DataBaseHelper.WelcomeReplyDatabaseHelper;
import com.whatscan.toolkit.forwa.DeletedMedia.ActivityDeleteMedia;
import com.whatscan.toolkit.forwa.DeletedMedia.AutoStart;
import com.whatscan.toolkit.forwa.GetSet.ContactModelAuto;
import com.whatscan.toolkit.forwa.GetSet.GroupModel;
import com.whatscan.toolkit.forwa.GetSet.ReplyModel;
import com.whatscan.toolkit.forwa.GetSet.WelcomeReplyModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WBubble.util.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class AllNotificationService extends NotificationListenerService {
    public static FileObserver WpImageOb, WpVideoOb, WpAudioOb, WpDocOb, WpGifOb, WpStickerOb;
    public static FileObserver BWpImageOb, BWpVideoOb, BWpAudioOb, BWpDocOb, BWpGifOb, BWpStickerOb;
    public static ChatHeadService chatHeadService;
    public static MainService mainService;
    public final Map<String, Long> pkgLastNotificationWhen = new HashMap<>();
    public boolean bound;
    public final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            chatHeadService = ((ChatHeadService.LocalBinder) iBinder).Binderservice();
            bound = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };
    public final ServiceConnection mConnectionAuto = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mainService = ((MainService.LocalBinder) iBinder).Binderservice();
            bound = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };
    public BroadcastReceiver DeleteNotice = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("deleteKey");
            try {
                cancelNotification(NotificationWear.notificationID.get(stringExtra));
                NotificationWear.notificationID.remove(stringExtra);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    public String wpMessge;
    String replymsg, replymsg2;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ChatHistoryDatabaseHelper chatHistoryDatabaseHelper = new ChatHistoryDatabaseHelper(this);
    SharedPreferences sharedPreferences;
    public BroadcastReceiver MsgBroadcast = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String str;
            CharSequence charSequence;
            intent.getStringExtra("title");
            if (intent.getBooleanExtra("isGroup", false)) {
                str = intent.getStringExtra("senderName");
                charSequence = str + "\n" + intent.getCharSequenceExtra("text");
            } else {
                str = "";
                charSequence = intent.getCharSequenceExtra("text");
            }
            intent.getStringExtra("ticker");
            String stringExtra = intent.getStringExtra("nameTag");
            intent.getStringExtra("package");
            String.valueOf(intent.getIntExtra("unread", 1));
            byte[] byteArrayExtra = intent.getByteArrayExtra("background");
            boolean booleanExtra = intent.getBooleanExtra("extender", false);
            Long valueOf = intent.getLongExtra("time", System.currentTimeMillis());
            SpannableString spannableString = new SpannableString(charSequence);
            spannableString.setSpan(new StyleSpan(1), 0, str.length(), 33);
            Random random = new Random();
            spannableString.setSpan(new ForegroundColorSpan(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))), 0, str.length(), 33);

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (chatHeadService != null) {
                addChatHead("com.whatsapp#" + stringExtra, spannableString, valueOf, byteArrayExtra, booleanExtra);
            }
        }
    };
    ContactDatabaseHelper contactDatabaseHelper;
    ContactModelAuto contactModel;
    ArrayList<ContactModelAuto> contactModels;
    CustomReplyDatabaseHelper customReplyDatabaseHelper;
    GroupDatabaseHelper groupDatabaseHelper;
    GroupModel groupModel;
    ArrayList<GroupModel> groupModels;
    boolean replied = false;
    ReplyModel replyModel;
    ArrayList<ReplyModel> replyModels;
    boolean replyonce = true;
    WelcomeReplyDatabaseHelper welcomeReplyDatabaseHelper;
    WelcomeReplyModel welcomeReplyModel;
    ArrayList<WelcomeReplyModel> welcomeReplyModels;
    Context context;

    public static boolean hasPermissions(Context context2, String... strArr) {
        if (Build.VERSION.SDK_INT < 23 || context2 == null || strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (ContextCompat.checkSelfPermission(context2, str) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ImageObserver("Wp");
        ImageObserver("BWp");
        VideoObserver("Wp");
        VideoObserver("BWp");
        AudioObserver("Wp");
        AudioObserver("BWp");
        DocObserver("Wp");
        DocObserver("BWp");
        GifObserver("Wp");
        GifObserver("BWp");
        StickerObserver("Wp");
        StickerObserver("BWp");

        String[] strArr = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        if (!isServiceRunning() && hasPermissions(getApplicationContext(), strArr)) {
            startDELAlarm();
            startServiceAlarm();
        }

        LocalBroadcastManager.getInstance(context).registerReceiver(MsgBroadcast, new IntentFilter("Msg"));
        LocalBroadcastManager.getInstance(context).registerReceiver(DeleteNotice, new IntentFilter("delete"));
        startAppService(this);
        startAppServiceAuto(this);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        boolean z;
        boolean z2 = false;
        int i;
        String valueOf2;
        String packageName = statusBarNotification.getPackageName();
        try {
            if (packageName.equals("com.whatsapp")) {
                Bundle bundle = statusBarNotification.getNotification().extras;
                String charSequence = bundle.getCharSequence(NotificationCompat.EXTRA_TEXT).toString();
                //    String strimg = bundle.getCharSequence(NotificationCompat.EXTRA_PICTURE).toString();
                String string = bundle.getString(NotificationCompat.EXTRA_TITLE);
                if (!string.equals("WhatsApp") && !string.equals("You") && !string.equals("WhatsApp Web") && !string.equals("Finished backup") && !string.equals("Backup paused") && !string.equals("Backup in progress")) {
                    saveMsgs(string + " - Whatsapp", charSequence);
                }
            } else if (packageName.equals("com.whatsapp.w4b")) {
                Bundle bundle2 = statusBarNotification.getNotification().extras;
                String charSequence2 = bundle2.getCharSequence(NotificationCompat.EXTRA_TEXT).toString();
                String string2 = bundle2.getString(NotificationCompat.EXTRA_TITLE);
                if (!string2.equals("WhatsApp Business") && !string2.equals("You") && !string2.equals("WhatsApp Web") && !string2.equals("Finished backup") && !string2.equals("Backup paused") && !string2.equals("Backup in progress")) {
                    saveMsgs(string2 + " - Business", charSequence2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        }

        if (!Constant.getBoolean(getApplicationContext(), Constant.prefname, "com.whatsapp", true)) {
            return;
        }

        if (packageName.equals("com.whatsapp") || packageName.equals("com.wa") || packageName.equals("com.whatsapp.w4")) {
            saveBadge(packageName, packageName);
            String charSequence = statusBarNotification.getNotification().tickerText != null ? statusBarNotification.getNotification().tickerText.toString() : "empty";
            Bundle bundle = statusBarNotification.getNotification().extras;
            NotificationCompat.CarExtender carExtender = new NotificationCompat.CarExtender(statusBarNotification.getNotification());
            String str = (String) bundle.getCharSequence(NotificationCompat.EXTRA_TITLE);
            if (str != null && charSequence.equals("empty") && !str.equals("WhatsApp")) {
                Intent intent = new Intent("Msg");
                intent.putExtra("package", packageName);
                Long valueOf = carExtender.getUnreadConversation() != null ? getTimeStamp(carExtender, intent, statusBarNotification.getNotification()) : null;
                String substring = str.substring(str.lastIndexOf(58) + 2);
                if (str.contains("(")) {
                    intent.putExtra("isGroup", true);
                    intent.putExtra("nameTag", str.substring(0, str.lastIndexOf(40) - 1));
                    intent.putExtra("senderName", substring);
                    intent.putExtra("bubbleKey", packageName + "#" + str.substring(0, str.lastIndexOf(40) - 1));
                    str = str.substring(0, str.lastIndexOf(40) - 1);
                } else if (str.contains(":")) {
                    intent.putExtra("isGroup", true);
                    intent.putExtra("nameTag", str.substring(0, str.lastIndexOf(58)));
                    intent.putExtra("senderName", substring);
                    intent.putExtra("bubbleKey", packageName + "#" + str.substring(0, str.lastIndexOf(58)));
                    str = str.substring(0, str.lastIndexOf(58));
                } else if (str.contains("@")) {
                    intent.putExtra("isGroup", true);
                    String substring2 = str.substring(str.lastIndexOf(64) + 2);
                    intent.putExtra("nameTag", substring2);
                    intent.putExtra("senderName", str.substring(0, str.lastIndexOf(64) - 1));
                    intent.putExtra("bubbleKey", packageName + "#" + substring2);
                    str = substring2;
                } else {
                    intent.putExtra("isGroup", false);
                    intent.putExtra("groupName", (String) null);
                    intent.putExtra("nameTag", str);
                    intent.putExtra("bubbleKey", packageName + "#" + str);
                }
                intent.putExtra("time", valueOf);
                Bitmap largeIcon = carExtender.getLargeIcon();
                if (largeIcon == null) {
                    try {
                        largeIcon = statusBarNotification.getNotification().largeIcon;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (largeIcon == null) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        largeIcon = ((BitmapDrawable) statusBarNotification.getNotification().getLargeIcon().loadDrawable(this)).getBitmap();
                    } else {
                        largeIcon = statusBarNotification.getNotification().largeIcon;
                    }
                }
                intent.putExtra("extender", true);
                String str2 = packageName + "#" + str;
                extractWearNotification(statusBarNotification, str2);
                if (largeIcon != null) {
                    saveBitmap(str2, largeIcon);
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    largeIcon.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                }
                @SuppressLint("SimpleDateFormat") String format = new SimpleDateFormat("dd-MM-yy hh:mm a").format((Object) new Date(statusBarNotification.getNotification().when));
                valueOf2 = String.valueOf(bundle.getCharSequence(NotificationCompat.EXTRA_TEXT));
                if (!valueOf2.equalsIgnoreCase("this message was deleted") && !valueOf2.equalsIgnoreCase("checking for new messages")) {
                    chatHistoryDatabaseHelper.insertData("WhatsApp", byteArrayOutputStream, str, valueOf2, valueOf2, format, false);
                    intent.putExtra("text", valueOf2);
                    if (bundle.containsKey(NotificationCompat.EXTRA_PICTURE)) {
                        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                        ((Bitmap) bundle.get(NotificationCompat.EXTRA_PICTURE)).compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream2);
                        intent.putExtra("background", byteArrayOutputStream2.toByteArray());
                    }
                    Intent intent2 = new Intent("Msg1");
                    intent2.putExtra("package", "WhatsApp");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        }

        if (!Preference.getBoolean(this, "servicecheck", "onoff", false)) {
            return;
        }

        if ((packageName.contains("com.whatsapp") || packageName.contains("com.wa") || packageName.contains("com.whatsapp.w4")) && (statusBarNotification.getNotification().flags & 512) == 0) {
            Long l = this.pkgLastNotificationWhen.get(statusBarNotification.getPackageName());
            if (l == null || l < statusBarNotification.getNotification().when) {
                this.pkgLastNotificationWhen.put(statusBarNotification.getPackageName(), statusBarNotification.getNotification().when);
                String charSequence = statusBarNotification.getNotification().tickerText != null ? statusBarNotification.getNotification().tickerText.toString() : "empty";
                Bundle bundle = statusBarNotification.getNotification().extras;
                String str = (String) bundle.getCharSequence(NotificationCompat.EXTRA_TITLE);
                if (str != null && charSequence.equals("empty") && !str.equals("WhatsApp")) {
                    Intent intent = new Intent("Msg");
                    intent.putExtra("package", packageName);
                    String substring = str.substring(str.lastIndexOf(58) + 2);
                    if (str.contains("(")) {
                        intent.putExtra("isGroup", true);
                        intent.putExtra("nameTag", str.substring(0, str.lastIndexOf(40) - 1));
                        intent.putExtra("senderName", substring);
                        intent.putExtra("bubbleKey", packageName + "#" + str.substring(0, str.lastIndexOf(40) - 1));
                        str = str.substring(0, str.lastIndexOf(40) - 1);
                        z = true;
                    } else if (str.contains(":")) {
                        intent.putExtra("isGroup", true);
                        intent.putExtra("nameTag", str.substring(0, str.lastIndexOf(58)));
                        intent.putExtra("senderName", substring);
                        intent.putExtra("bubbleKey", packageName + "#" + str.substring(0, str.lastIndexOf(58)));
                        str = str.substring(0, str.lastIndexOf(58));
                        z = true;
                    } else if (str.contains("@")) {
                        intent.putExtra("isGroup", true);
                        String substring2 = str.substring(str.lastIndexOf(64) + 2);
                        intent.putExtra("nameTag", substring2);
                        intent.putExtra("senderName", str.substring(0, str.lastIndexOf(64) - 1));
                        intent.putExtra("bubbleKey", packageName + "#" + substring2);
                        str = substring2;
                        z = true;
                    } else {
                        intent.putExtra("isGroup", false);
                        intent.putExtra("groupName", (String) null);
                        intent.putExtra("nameTag", str);
                        intent.putExtra("bubbleKey", packageName + "#" + str);
                        z = false;
                    }
                    intent.putExtra("time", (Serializable) null);
                    intent.putExtra("extender", true);
                    String str2 = packageName + "#" + str;
                    extractWearNotification(statusBarNotification, str2);
                    String valueOf = String.valueOf(bundle.getCharSequence(NotificationCompat.EXTRA_TEXT));
                    if (!valueOf.toLowerCase().equals("this message was deleted") && !valueOf.toLowerCase().equals("checking for new messages")) {
                        intent.putExtra("text", valueOf);
                        if (bundle.containsKey(NotificationCompat.EXTRA_PICTURE)) {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ((Bitmap) bundle.get(NotificationCompat.EXTRA_PICTURE)).compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            intent.putExtra("background", byteArrayOutputStream.toByteArray());
                        }
                        this.replied = false;
                        this.replyonce = Preference.getBoolean(this, "servicecheck", "reply_once_radio", false);
                        if (Preference.getBoolean(this, "settings", "reply_once_radio", false)) {
                            i = 0;
                        } else if (Preference.getBoolean(this, "settings", "time_delay_radio", false)) {
                            String[] split = Preference.getstring(this, "timedelaydialog", "timedelay", "3 Seconds").split(" ");
                            i = split[1].equalsIgnoreCase("Seconds") ? Integer.parseInt(split[0]) * 1000 : Integer.parseInt(split[0]) * 60000;
                        } else {
                            i = 0;
                        }
                        try {
                            Thread.sleep(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!this.replyonce) {
                            if (Preference.getBoolean(this, "settings", "reply_once_radio", false)) {
                                Preference.setBoolean(this, "servicecheck", "reply_once_radio", true);
                            }
                            if (z) {
                                if (Preference.getstring(this, "groups_radio", "groups_radio_group", "everyone_group").equals("exceptgroup_radio") || Preference.getstring(this, "groups_radio", "groups_radio_group", "everyone_group").equals("mygroup_radio")) {
                                    this.groupModels = new ArrayList<>();
                                    this.groupModels = groupviewAll();
                                    if (this.groupModels.size() > 0) {
                                        boolean z3 = false;
                                        boolean z4 = false;
                                        for (int i2 = 0; i2 < this.groupModels.size(); i2++) {
                                            if (this.groupModels.get(i2).getName().equalsIgnoreCase(str)) {
                                                if (Preference.getstring(this, "groups_radio", "groups_radio_group", "everyone_group").equals("exceptgroup_radio")) {
                                                    if (this.groupModels.get(i2).getName().equalsIgnoreCase(str)) {
                                                        z3 = true;
                                                        z4 = false;
                                                    } else {
                                                        z3 = true;
                                                        z4 = true;
                                                    }
                                                } else if (this.groupModels.get(i2).getName().equalsIgnoreCase(str)) {
                                                    z3 = true;
                                                    z4 = true;
                                                } else {
                                                    z3 = true;
                                                    z4 = false;
                                                }
                                            }
                                        }
                                        if (z3 || !Preference.getstring(this, "groups_radio", "groups_radio_group", "everyone_group").equals("exceptgroup_radio")) {
                                            z2 = z4;
                                        } else {
                                            replymsg(valueOf, str, str2);
                                        }
                                        if (z2) {
                                            replymsg(valueOf, str, str2);
                                        }
                                    } else if (Preference.getstring(this, "groups_radio", "groups_radio_group", "everyone_group").equals("exceptgroup_radio")) {
                                        replymsg(valueOf, str, str2);
                                    }
                                } else if (Preference.getstring(this, "groups_radio", "groups_radio_group", "everyone_group").equals("everyone_group")) {
                                    replymsg(valueOf, str, str2);
                                }
                            } else if (Preference.getstring(this, "contacts_group", "contacts_radio_group", "everyone_radio").equals("exceptcontact_radio") || Preference.getstring(this, "contacts_group", "contacts_radio_group", "everyone_radio").equals("mycontact_radio")) {
                                this.contactModels = new ArrayList<>();
                                this.contactModels = contactviewAll();
                                if (this.contactModels.size() > 0) {
                                    boolean z5 = false;
                                    boolean z6 = false;
                                    for (int i3 = 0; i3 < this.contactModels.size(); i3++) {
                                        if (this.contactModels.get(i3).getName().equals(str)) {
                                            if (Preference.getstring(this, "contacts_group", "contacts_radio_group", "everyone_radio").equals("exceptcontact_radio")) {
                                                if (this.contactModels.get(i3).getName().equals(str)) {
                                                    z5 = true;
                                                    z6 = false;
                                                } else {
                                                    z5 = true;
                                                    z6 = true;
                                                }
                                            } else if (this.contactModels.get(i3).getName().equals(str)) {
                                                z5 = true;
                                                z6 = true;
                                            } else {
                                                z5 = true;
                                                z6 = false;
                                            }
                                        }
                                    }
                                    if (z5 || !Preference.getstring(this, "contacts_group", "contacts_radio_group", "everyone_radio").equals("exceptcontact_radio")) {
                                        z2 = z6;
                                    } else {
                                        replymsg(valueOf, str, str2);
                                    }
                                    if (z2) {
                                        replymsg(valueOf, str, str2);
                                    }
                                } else if (Preference.getstring(this, "contacts_group", "contacts_radio_group", "everyone_radio").equals("exceptcontact_radio")) {
                                    replymsg(valueOf, str, str2);
                                }
                            } else if (Preference.getstring(this, "contacts_group", "contacts_radio_group", "everyone_radio").equals("everyone_radio")) {
                                replymsg(valueOf, str, str2);
                            }
                        }
                    }
                }
            }
        }
    }

    private void saveMsgs(String str, String str2) {
        if (str2.equals("This message was deleted")) {
            sendNotification(str);
        }
        if (str.contains("messages")) {
            String substring = str.substring(0, str.indexOf("("));
            if (!str2.contains("new messages") && !str.contains("@") && !str.equals("WhatsApp") && !str.equals("WhatsApp Business - Business") && !str.equals("Deleting messages...") && !str2.equals("This message was deleted")) {
                String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd", Locale.US);
            }
        } else if (!str2.contains("new messages") && !str.contains("@") && !str.equals("WhatsApp") && !str.equals("WhatsApp Business - Business") && !str.equals("Deleting messages...") && !str2.equals("This message was deleted")) {
            wpMessge = str2;
            String format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMMM dd", Locale.US);
        }
    }

    private void sendNotification(String str) {
        Intent intent = new Intent(context, ActivityDeleteMedia.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, context.getString(R.string.app_name));
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle("New Message Deleted")
                .setContentText(str + " deleted a message. Check it")
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI).setContentIntent(activity);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd", Locale.US);
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        new DBHelper(this.context).saveUsers(str, wpMessge, simpleDateFormat.format(Calendar.getInstance().getTime()) + " " + format.substring(11, 13) + ":" + format.substring(14, 16), format);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("10001", "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mBuilder.setChannelId("10001");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        alarmIt();
    }

    @SuppressLint("WrongConstant")
    private void alarmIt() {
        Intent intent = new Intent(this.context, AutoStart.class);
        intent.putExtra("RestartService", true);
        intent.addFlags(335577088);
        ((AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)).set(1, System.currentTimeMillis() + 200, PendingIntent.getActivity(this, 0, intent, 1073741824));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        tryReconnectService();

        startAppService(getApplicationContext());
        startAppServiceAuto(getApplicationContext());
        return START_STICKY;
    }

    public void tryReconnectService() {
        toggleNotificationListenerService();
        if (Build.VERSION.SDK_INT >= 24) {
            requestRebind(new ComponentName(getApplicationContext(), AllNotificationService.class));
        }
    }

    private void toggleNotificationListenerService() {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, AllNotificationService.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, AllNotificationService.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @SuppressLint({"WrongConstant"})
    public void startServiceAlarm() {
        Intent intent = new Intent(this, AutoStart.class);
        intent.setAction("RestartService");
        ((AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)).setInexactRepeating(0, Calendar.getInstance().getTimeInMillis(), 300000, PendingIntent.getBroadcast(this, 0, intent, 0));
    }

    private boolean isServiceRunning() {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (AllNotificationService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint({"WrongConstant"})
    public void startDELAlarm() {
        ((AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM)).setInexactRepeating(0, Calendar.getInstance().getTimeInMillis(), 1800000, PendingIntent.getBroadcast(this, 0, new Intent(this, AutoStart.class), 0));
    }

    private void ImageObserver(String str) {
        if (str.equals("Wp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.imagesReceivedPath11);
                if (WpImageOb != null) {
                    WpImageOb.stopWatching();
                }
                WpImageOb = new Observer(file.getPath(), "Image", "Wp");
                WpImageOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.imagesReceivedPath);
                if (WpImageOb != null) {
                    WpImageOb.stopWatching();
                }
                WpImageOb = new Observer(file.getPath(), "Image", "Wp");
                WpImageOb.startWatching();
            }

        } else if (str.equals("BWp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.imagesReceivedPathB);
                if (BWpImageOb != null) {
                    BWpImageOb.stopWatching();
                }
                BWpImageOb = new Observer(file.getPath(), "Image", "BWp");
                BWpImageOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.imagesReceivedPathNB);
                if (BWpImageOb != null) {
                    BWpImageOb.stopWatching();
                }
                BWpImageOb = new Observer(file.getPath(), "Image", "BWp");
                BWpImageOb.startWatching();
            }
        }
    }

    private void VideoObserver(String str) {
        if (str.equals("Wp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.videosReceivedPath11);
                if (WpVideoOb != null) {
                    WpVideoOb.stopWatching();
                }
                WpVideoOb = new Observer(file.getPath(), "Video", "Wp");
                WpVideoOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.videosReceivedPath);
                if (WpVideoOb != null) {
                    WpVideoOb.stopWatching();
                }
                WpVideoOb = new Observer(file.getPath(), "Video", "Wp");
                WpVideoOb.startWatching();
            }
        } else if (str.equals("BWp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.videosReceivedPathB);
                if (BWpVideoOb != null) {
                    BWpVideoOb.stopWatching();
                }
                BWpVideoOb = new Observer(file.getPath(), "Video", "BWp");
                BWpVideoOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.videosReceivedPathNB);
                if (BWpVideoOb != null) {
                    BWpVideoOb.stopWatching();
                }
                BWpVideoOb = new Observer(file.getPath(), "Video", "BWp");
                BWpVideoOb.startWatching();
            }
        }
    }

    private void AudioObserver(String str) {
        if (str.equals("Wp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.audiosReceivedPath11);
                if (WpAudioOb != null) {
                    WpAudioOb.stopWatching();
                }
                WpAudioOb = new Observer(file.getPath(), "Audio", "Wp");
                WpAudioOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.audiosReceivedPath);
                if (WpAudioOb != null) {
                    WpAudioOb.stopWatching();
                }
                WpAudioOb = new Observer(file.getPath(), "Audio", "Wp");
                WpAudioOb.startWatching();
            }
        } else if (str.equals("BWp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.audiosReceivedPathB);
                if (BWpAudioOb != null) {
                    BWpAudioOb.stopWatching();
                }
                BWpAudioOb = new Observer(file.getPath(), "Audio", "BWp");
                BWpAudioOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.audiosReceivedPathNB);
                if (BWpAudioOb != null) {
                    BWpAudioOb.stopWatching();
                }
                BWpAudioOb = new Observer(file.getPath(), "Audio", "BWp");
                BWpAudioOb.startWatching();
            }
        }
    }

    private void DocObserver(String str) {
        if (str.equals("Wp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.documentsReceivedPath11);
                if (WpDocOb != null) {
                    WpDocOb.stopWatching();
                }
                WpDocOb = new Observer(file.getPath(), "Document", "Wp");
                WpDocOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.documentsReceivedPath);
                if (WpDocOb != null) {
                    WpDocOb.stopWatching();
                }
                WpDocOb = new Observer(file.getPath(), "Document", "Wp");
                WpDocOb.startWatching();
            }

        } else if (str.equals("BWp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.documentsReceivedPathB);
                if (BWpDocOb != null) {
                    BWpDocOb.stopWatching();
                }
                BWpDocOb = new Observer(file.getPath(), "Document", "BWp");
                BWpDocOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.documentsReceivedPathNB);
                if (BWpDocOb != null) {
                    BWpDocOb.stopWatching();
                }
                BWpDocOb = new Observer(file.getPath(), "Document", "BWp");
                BWpDocOb.startWatching();
            }
        }
    }

    private void GifObserver(String str) {
        if (str.equals("Wp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.gifReceivedPath11);
                if (WpGifOb != null) {
                    WpGifOb.stopWatching();
                }
                WpGifOb = new Observer(file.getPath(), "Gif", "Wp");
                WpGifOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.gifReceivedPath);
                if (WpGifOb != null) {
                    WpGifOb.stopWatching();
                }
                WpGifOb = new Observer(file.getPath(), "Gif", "Wp");
                WpGifOb.startWatching();
            }
        } else if (str.equals("BWp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.gifReceivedPath11);
                if (BWpGifOb != null) {
                    BWpGifOb.stopWatching();
                }
                BWpGifOb = new Observer(file.getPath(), "Gif", "BWp");
                BWpGifOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.gifReceivedPathNB);
                if (BWpGifOb != null) {
                    BWpGifOb.stopWatching();
                }
                BWpGifOb = new Observer(file.getPath(), "Gif", "BWp");
                BWpGifOb.startWatching();
            }
        }
    }

    private void StickerObserver(String str) {
        if (str.equals("Wp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.stickerReceivedPath11);
                if (WpStickerOb != null) {
                    WpStickerOb.stopWatching();
                }
                WpStickerOb = new Observer(file.getPath(), "Sticker", "Wp");
                WpStickerOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.stickerReceivedPath);
                if (WpStickerOb != null) {
                    WpStickerOb.stopWatching();
                }
                WpStickerOb = new Observer(file.getPath(), "Sticker", "Wp");
                WpStickerOb.startWatching();
            }
        } else if (str.equals("BWp")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                File file = new File(Environment.getExternalStorageDirectory(), com.whatscan.toolkit.forwa.Util.Constant.stickerReceivedPathB);
                if (BWpStickerOb != null) {
                    BWpStickerOb.stopWatching();
                }
                BWpStickerOb = new Observer(file.getPath(), "Sticker", "BWp");
                BWpStickerOb.startWatching();
            } else {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), com.whatscan.toolkit.forwa.Util.Constant.stickerReceivedPathNB);
                if (BWpStickerOb != null) {
                    BWpStickerOb.stopWatching();
                }
                BWpStickerOb = new Observer(file.getPath(), "Sticker", "BWp");
                BWpStickerOb.startWatching();
            }
        }
    }

    public void copyFile(File file, File file2) throws IOException {
        if (!file2.getParentFile().exists()) {
            file2.getParentFile().mkdirs();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }
        FileChannel channel = new FileInputStream(file).getChannel();
        FileChannel channel2 = new FileOutputStream(file2).getChannel();
        channel2.transferFrom(channel, 0, channel.size());
        channel.close();
        channel2.close();
    }

    public void moveFile(File file, File file2) throws IOException {
        if (!file2.getParentFile().exists()) {
            file2.getParentFile().mkdirs();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }
        FileChannel channel = new FileInputStream(file).getChannel();
        FileChannel channel2 = new FileOutputStream(file2).getChannel();
        channel2.transferFrom(channel, 0, channel.size());
        channel.close();
        channel2.close();
    }

    @SuppressLint("WrongConstant")
    public void showNotification(String path) {
        PendingIntent pendingIntent;
        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/click_sound");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channel_id = null;
        Intent myIntent;

        myIntent = new Intent(context, ActivityDeleteMedia.class);

        pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();

        bigPictureStyle.setSummaryText(Html.fromHtml("New Data Arrived.").toString());
        bigPictureStyle.bigPicture(BitmapFactory.decodeFile(path));
        if (Build.VERSION.SDK_INT >= 26) {
            channel_id = "id_product";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            try {
                NotificationChannel notificationChannel = new NotificationChannel(channel_id, "Product", importance);
                notificationChannel.setDescription("New Data Arrived.");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Notification notification;
        assert channel_id != null;
        notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("New Data Arrived.")
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeFile(path))
                .setSound(alarmSound)
                .setColor(context.getResources().getColor(R.color.colorTools))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_HIGH | Notification.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeFile(path))
                        .bigLargeIcon(null))
                .setPriority(Notification.PRIORITY_HIGH | Notification.PRIORITY_MAX)
                .build();


        assert notificationManager != null;
        notificationManager.notify(1, notification);
    }

    @SuppressLint("WrongConstant")
    public void startAppService(Context context2) {
        if (!bound) {
            Intent intent = new Intent(context2, ChatHeadService.class);
            startService(intent);
            getApplicationContext().bindService(intent, mConnection, 1);
        }
    }

    @SuppressLint("WrongConstant")
    public void startAppServiceAuto(Context context) {
        if (!bound) {
            customReplyDatabaseHelper = new CustomReplyDatabaseHelper(getApplicationContext());
            Intent intent = new Intent(context, MainService.class);
            startService(intent);
            getApplicationContext().bindService(intent, mConnectionAuto, 1);
        }
    }

    public void addChatHead(String str, Spannable spannable, Long l, byte[] bArr, boolean z) {
        if (NotificationWear.replyIntent != null && NotificationWear.replyIntent.containsKey(str) && !str.substring(str.indexOf("#") + 1).equals("WhatsApp")) {
            chatHeadService.addChatHead(str, spannable, l, bArr, z);
        }
    }

    private void extractWearNotification(StatusBarNotification statusBarNotification, String str) {
        NotificationWear.notificationID.put(str, statusBarNotification.getKey());
        new NotificationWear().packageName = statusBarNotification.getPackageName();
        List<NotificationCompat.Action> actions = new NotificationCompat.WearableExtender(statusBarNotification.getNotification()).getActions();
        NotificationCompat.CarExtender carExtender = new NotificationCompat.CarExtender(statusBarNotification.getNotification());
        if (carExtender.getUnreadConversation() != null) {
            NotificationWear.readIntent.put(str, carExtender.getUnreadConversation().getReadPendingIntent());
        }
        NotificationWear.openConv.put(str, statusBarNotification.getNotification().contentIntent);
        for (NotificationCompat.Action action : actions) {
            if (!(action == null || action.getRemoteInputs() == null)) {
                NotificationWear.remoteMap.put(str, action.getRemoteInputs());
                NotificationWear.openConv.put(str, statusBarNotification.getNotification().contentIntent);
                NotificationWear.replyIntent.put(str, action.getActionIntent());
                NotificationWear.bundleMap.put(str, statusBarNotification.getNotification().extras);
                ChatHeadService chatHeadService2 = chatHeadService;
                if (!(chatHeadService2 == null || chatHeadService2.viewCache.get(str) == null || chatHeadService.isReplyVisible(str))) {
                    chatHeadService.toggleReply(str, true);
                }
                return;
            }
        }
        NotificationCompat.CarExtender carExtender2 = new NotificationCompat.CarExtender(statusBarNotification.getNotification());
        if (carExtender2.getUnreadConversation() != null) {
            NotificationWear.replyIntent.put(str, carExtender2.getUnreadConversation().getReplyPendingIntent());
            NotificationWear.remoteMap.put(str, new RemoteInput[]{carExtender2.getUnreadConversation().getRemoteInput()});
            NotificationWear.bundleMap.put(str, statusBarNotification.getNotification().extras);
            NotificationWear.openConv.put(str, statusBarNotification.getNotification().contentIntent);
            ChatHeadService chatHeadService3 = chatHeadService;
            if (!(chatHeadService3 == null || chatHeadService3.viewCache.get(str) == null || chatHeadService.isReplyVisible(str))) {
                chatHeadService.toggleReply(str, true);
            }
            return;
        }
        for (int i = 0; i < NotificationCompat.getActionCount(statusBarNotification.getNotification()); i++) {
            NotificationCompat.Action action2 = NotificationCompat.getAction(statusBarNotification.getNotification(), i);
            if (action2 != null && action2.getRemoteInputs() != null) {
                NotificationWear.remoteMap.put(str, action2.getRemoteInputs());
                NotificationWear.openConv.put(str, statusBarNotification.getNotification().contentIntent);
                NotificationWear.replyIntent.put(str, action2.getActionIntent());
                NotificationWear.bundleMap.put(str, statusBarNotification.getNotification().extras);
                return;
            }
        }
        for (NotificationCompat.Action action3 : actions) {
            if (!(action3 == null || action3.getRemoteInputs() == null)) {
                NotificationWear.remoteMap.put(str, action3.getRemoteInputs());
                NotificationWear.openConv.put(str, statusBarNotification.getNotification().contentIntent);
                NotificationWear.replyIntent.put(str, action3.getActionIntent());
                NotificationWear.bundleMap.put(str, statusBarNotification.getNotification().extras);
                ChatHeadService chatHeadService4 = chatHeadService;
                if (!(chatHeadService4 == null || chatHeadService4.viewCache.get(str) == null || chatHeadService.isReplyVisible(str))) {
                    chatHeadService.toggleReply(str, true);
                }
                return;
            }
        }
        ChatHeadService chatHeadService5 = chatHeadService;
        if (chatHeadService5 != null) {
            if (!(chatHeadService5.viewCache.get(str) == null || chatHeadService.isReplyVisible(str) || NotificationWear.replyIntent.get(str) == null)) {
                chatHeadService.toggleReply(str, true);
            }
            return;
        }
        NotificationWear.bundleMap.put(str, statusBarNotification.getNotification().extras);
    }

    @SuppressLint("SimpleDateFormat")
    private long getTimeStamp(NotificationCompat.CarExtender carExtender, Intent intent, Notification notification) {
        long l;
        new SimpleDateFormat("hh:mm aaa");
        new Date();
        Long.valueOf(System.currentTimeMillis());
        intent.putExtra("extender", false);
        if (carExtender.getUnreadConversation() != null) {
            l = carExtender.getUnreadConversation().getLatestTimestamp();
        } else {
            l = notification.when;
        }
        intent.putExtra("extender", true);
        intent.putExtra("time", l);
        return l;
    }

    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("clearunread", false)) {
            chatHeadService.clearUnread(statusBarNotification.getPackageName());
        } else if (Preference.getBoolean(this, "servicecheck", "onoff", false) && !statusBarNotification.getPackageName().contains("com.whatsapp") && !statusBarNotification.getPackageName().contains("com.wa")) {
            statusBarNotification.getPackageName().contains("com.whatsapp.w4");
        }
    }

    private void saveBitmap(String str, Bitmap bitmap) {
        File dir = new ContextWrapper(getApplicationContext()).getDir("chatbubbles", 0);
        String absolutePath = dir.getAbsolutePath();
        File file = new File(absolutePath, str + ".png");
        if (!file.exists() || (file.exists() && bitmap != null)) {
            dir.mkdirs();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    private void saveBadge(String str, String str2) {
        Bitmap bitmap;
        try {
            Drawable applicationIcon = getPackageManager().getApplicationIcon(str2);
            if (applicationIcon instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) applicationIcon).getBitmap();
            } else {
                Bitmap createBitmap = Bitmap.createBitmap(applicationIcon.getIntrinsicWidth(), applicationIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                applicationIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                applicationIcon.draw(canvas);
                bitmap = createBitmap;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            bitmap = null;
        }
        if (bitmap != null) {
            File dir = new ContextWrapper(getApplicationContext()).getDir("badges", 0);
            File file = new File(dir, str + ".png");
            if (!file.exists()) {
                dir.mkdirs();
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    try {
                        fileOutputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    public long printDifference(Date date, Date date2) {
        long time = date2.getTime() - date.getTime();
        long j = time / 86400000;
        long j2 = time % 86400000;
        long j3 = j2 / 3600000;
        long j4 = j2 % 3600000;
        long j5 = j4 / 60000;
        long j6 = (j4 % 60000) / 1000;
        return j;
    }

    public void replymsg(String str, String str2, final String str3) {
        if (Preference.getBoolean(this, "settings", "welcome_radio_button", false)) {
            this.welcomeReplyDatabaseHelper = new WelcomeReplyDatabaseHelper(this);
            if (this.welcomeReplyDatabaseHelper.insertData(str2, str, getDate(System.currentTimeMillis(), "dd/M/yyyy hh:mm:ss"))) {
                replyLastNotification(Preference.getstring(this, "settings", "welcome_text", "Hi welcome, thank you for messaging me, i will reply you soon"), str3, getApplicationContext());
                Preference.setBoolean(this, "servicecheck", "reply_once_radio", false);
                return;
            }
            this.welcomeReplyModels = new ArrayList<>();
            this.welcomeReplyModels = WelcomeviewAll();
            for (int i = 0; i < this.welcomeReplyModels.size(); i++) {
                if (this.welcomeReplyModels.get(i).getName().equals(str2)) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
                    try {
                        if (printDifference(Objects.requireNonNull(simpleDateFormat.parse(this.welcomeReplyModels.get(i).getDate())), Objects.requireNonNull(simpleDateFormat.parse(getDate(System.currentTimeMillis(), "dd/M/yyyy hh:mm:ss")))) >= 3) {
                            replyLastNotification(Preference.getstring(this, "settings", "welcome_text", "Hi welcome, thank you for messaging me, i will reply you soon"), str3, getApplicationContext());
                            Preference.setBoolean(this, "servicecheck", "reply_once_radio", false);
                            this.welcomeReplyDatabaseHelper.updateData(this.welcomeReplyModels.get(i).getId(), this.welcomeReplyModels.get(i).getName(), str, getDate(System.currentTimeMillis(), "dd/M/yyyy hh:mm:ss"));
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.welcomeReplyDatabaseHelper.close();
        }
        this.replyModels = new ArrayList<>();
        this.replyModels = viewAll();
        if (this.replyModels.size() > 0) {
            for (int i2 = 0; i2 < this.replyModels.size(); i2++) {
                if (!this.replied) {
                    if (this.replyModels.get(i2).getSelectedoption().equals("exact") && this.replyModels.get(i2).getIncomingmsg().equals(str)) {
                        this.replied = true;
                        replymsg = this.replyModels.get(i2).getReplymsg();
                        String[] split = str2.split(" ");
                        if (this.replyModels.get(i2).getReplymsg().contains("[first name]")) {
                            if (split.length > 2) {
                                replymsg = replymsg.replace("[first name]", split[0]);
                            } else if (split.length > 1) {
                                replymsg = replymsg.replace("[first name]", split[0]);
                            } else if (split.length == 1) {
                                replymsg = replymsg.replace("[first name]", split[0]);
                            } else {
                                replymsg = replymsg.replace("[first name]", str2);
                            }
                        }
                        if (this.replyModels.get(i2).getReplymsg().contains("[name]")) {
                            if (split.length > 2) {
                                replymsg = replymsg.replace("[name]", split[1]);
                            } else if (split.length > 1) {
                                replymsg = replymsg.replace("[name]", split[1]);
                            } else if (split.length == 1) {
                                replymsg = replymsg.replace("[name]", split[0]);
                            } else {
                                replymsg = replymsg.replace("[name]", str2);
                            }
                        }
                        if (this.replyModels.get(i2).getReplymsg().contains("[last name]")) {
                            if (split.length > 2) {
                                replymsg = replymsg.replace("[last name]", split[2]);
                            } else if (split.length > 1) {
                                replymsg = replymsg.replace("[last name]", split[1]);
                            } else if (split.length == 1) {
                                replymsg = replymsg.replace("[last name]", split[0]);
                            } else {
                                replymsg = replymsg.replace("[last name]", str2);
                            }
                        }
                        replyLastNotification(replymsg, str3, getApplicationContext());
                    }
                    if (this.replyModels.get(i2).getSelectedoption().equals("contains")) {
                        if (str.matches("(?i).*" + this.replyModels.get(i2).getIncomingmsg() + ".*")) {
                            this.replied = true;
                            replymsg2 = this.replyModels.get(i2).getReplymsg();
                            String[] split2 = str2.split(" ");
                            if (this.replyModels.get(i2).getReplymsg().contains("[first name]")) {
                                if (split2.length > 2) {
                                    replymsg2 = replymsg2.replace("[first name]", split2[0]);
                                } else if (split2.length > 1) {
                                    replymsg2 = replymsg2.replace("[first name]", split2[0]);
                                } else if (split2.length == 1) {
                                    replymsg2 = replymsg2.replace("[first name]", split2[0]);
                                } else {
                                    replymsg2 = replymsg2.replace("[first name]", str2);
                                }
                            }
                            if (this.replyModels.get(i2).getReplymsg().contains("[name]")) {
                                if (split2.length > 2) {
                                    replymsg2 = replymsg2.replace("[name]", split2[1]);
                                } else if (split2.length > 1) {
                                    replymsg2 = replymsg2.replace("[name]", split2[1]);
                                } else if (split2.length == 1) {
                                    replymsg2 = replymsg2.replace("[name]", split2[0]);
                                } else {
                                    replymsg2 = replymsg2.replace("[name]", str2);
                                }
                            }
                            if (this.replyModels.get(i2).getReplymsg().contains("[last name]")) {
                                if (split2.length > 2) {
                                    replymsg2 = replymsg2.replace("[last name]", split2[2]);
                                } else if (split2.length > 1) {
                                    replymsg2 = replymsg2.replace("[last name]", split2[1]);
                                } else if (split2.length == 1) {
                                    replymsg2 = replymsg2.replace("[last name]", split2[0]);
                                } else {
                                    replymsg2 = replymsg2.replace("[last name]", str2);
                                }
                            }
                            replyLastNotification(replymsg2, str3, getApplicationContext());
                        }
                    }
                } else {
                    return;
                }
            }
        }
        if (this.replied) {
            return;
        }
        replyLastNotification(Preference.getstring(this, "servicecheck", "listmsgs", "Can't talk now , text you later"), str3, getApplicationContext());
    }

    public ArrayList<ReplyModel> viewAll() {
        Cursor allData = customReplyDatabaseHelper.getAllData();
        replyModels = new ArrayList<>();
        while (allData.moveToNext()) {
            replyModel = new ReplyModel();
            replyModel.setIncomingmsg(allData.getString(1));
            replyModel.setReplymsg(allData.getString(2));
            replyModel.setSelectedoption(allData.getString(3));
            replyModels.add(replyModel);
        }
        return replyModels;
    }

    public ArrayList<WelcomeReplyModel> WelcomeviewAll() {
        Cursor allData = welcomeReplyDatabaseHelper.getAllData();
        welcomeReplyModels = new ArrayList<>();
        while (allData.moveToNext()) {
            welcomeReplyModel = new WelcomeReplyModel();
            welcomeReplyModel.setId(allData.getString(0));
            welcomeReplyModel.setName(allData.getString(1));
            welcomeReplyModel.setIncomingmsg(allData.getString(2));
            welcomeReplyModel.setDate(allData.getString(3));
            welcomeReplyModels.add(welcomeReplyModel);
        }
        return welcomeReplyModels;
    }

    public ArrayList<ContactModelAuto> contactviewAll() {
        contactDatabaseHelper = new ContactDatabaseHelper(this);
        Cursor allData = contactDatabaseHelper.getAllData();
        contactModels = new ArrayList<>();
        while (allData.moveToNext()) {
            contactModel = new ContactModelAuto();
            contactModel.setId(allData.getString(0));
            contactModel.setName(allData.getString(1));
            contactModels.add(contactModel);
        }
        return contactModels;
    }

    public ArrayList<GroupModel> groupviewAll() {
        groupDatabaseHelper = new GroupDatabaseHelper(this);
        Cursor allData = groupDatabaseHelper.getAllData();
        groupModels = new ArrayList<>();
        while (allData.moveToNext()) {
            groupModel = new GroupModel();
            groupModel.setId(allData.getString(0));
            groupModel.setName(allData.getString(1));
            groupModels.add(groupModel);
        }
        return groupModels;
    }

    @SuppressLint("WrongConstant")
    public void replyLastNotification(String str, String str2, Context context) {
        try {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            RemoteInput[] remoteInputArr = new RemoteInput[Objects.requireNonNull(NotificationWear.remoteMap.get(str2)).length];
            Intent intent = new Intent();
            intent.addFlags(268435456);
            Bundle bundle = NotificationWear.bundleMap.get(str2);
            int i = 0;
            for (RemoteInput remoteInput : Objects.requireNonNull(NotificationWear.remoteMap.get(str2))) {
                remoteInputArr[i] = remoteInput;
                bundle.putCharSequence(remoteInputArr[i].getResultKey(), str);
                i++;
            }
            RemoteInput.addResultsToIntent(remoteInputArr, intent, bundle);
            try {
                Objects.requireNonNull(NotificationWear.replyIntent.get(str2)).send(context, 0, intent);
                if (defaultSharedPreferences.getInt("replySession", 0) <= 4 && !str2.startsWith("dc#")) {
                    SharedPreferences.Editor edit = defaultSharedPreferences.edit();
                    edit.putInt("replySession", defaultSharedPreferences.getInt("replySession", 0) + 1);
                    edit.apply();
                }
            } catch (PendingIntent.CanceledException e) {
                Toast.makeText(context, "Sorry, there was an error in sending text!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } catch (Exception e2) {
            Toast.makeText(context, "Sorry, there was an error in sending text!", Toast.LENGTH_LONG).show();
            e2.printStackTrace();
        }
    }

    public String getDate(long j, String str) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        return simpleDateFormat.format(instance.getTime());
    }

    private class Observer extends FileObserver {
        private final String folder;
        private final String app;
        private String folder_name;
        private File file;


        @SuppressLint("WrongConstant")
        public Observer(String path, String folder, String app) {
            super(path, DELETE);
            this.folder = folder;
            this.app = app;
        }

        @Override
        public void onEvent(int event, @Nullable String path) {
            if (event == 256 || event == 128) {
                if (app.equals("Wp")) {
                    switch (folder) {
                        case "Audio":
                            folder_name = "WhatsApp Audio";
                            break;
                        case "Image":
                            folder_name = "WhatsApp Images";
                            break;
                        case "Video":
                            folder_name = "WhatsApp Video";
                            break;
                        case "Document":
                            folder_name = "WhatsApp Documents";
                            break;
                        case "Gif":
                            folder_name = "WhatsApp Animated Gifs";
                            break;
                        case "Sticker":
                            folder_name = "WhatsApp Stickers";
                            break;
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        file = new File(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp/WhatsApp/Media/" + folder_name + "/" + path);
                    } else {
                        file = new File(Environment.getExternalStorageDirectory() + "/WhatsApp/Media/" + folder_name + "/" + path);
                    }
                } else if (app.equals("BWp")) {
                    switch (folder) {
                        case "Audio":
                            folder_name = "WhatsApp Business Audio";
                            break;
                        case "Image":
                            folder_name = "WhatsApp Business Images";
                            break;
                        case "Video":
                            folder_name = "WhatsApp Business Video";
                            break;
                        case "Document":
                            folder_name = "WhatsApp Business Documents";
                            break;
                        case "Gif":
                            folder_name = "WhatsApp Business Animated Gifs";
                            break;
                        case "Sticker":
                            folder_name = "WhatsApp Business Stickers";
                            break;
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        file = new File(Environment.getExternalStorageDirectory() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/" + folder_name + "/" + path);
                    } else {
                        file = new File(Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/" + folder_name + "/" + path);
                    }
                }
                File file3 = new File(new File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_directory)), ".cached Files/" + file.getName() + ".temp");
                if (!file3.exists()){
                    file3.mkdirs();
                }

                try {
                    copyFile(file, file3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if ((event & 512) != 0 || (event & 1024) != 0) {
                File file4 = new File(Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_directory) + "/.cached Files/" + path + ".temp");
                if (!file4.exists()){
                    file4.mkdirs();
                }
                File file5 = new File(Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_directory) + "/Deleted Media/" + path);
                if (new File(String.valueOf(file4)).exists()) {
                    try {
                        moveFile(file4, file5);
                        showNotification(file5.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}