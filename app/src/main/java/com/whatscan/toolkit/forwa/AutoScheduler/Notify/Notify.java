package com.whatscan.toolkit.forwa.AutoScheduler.Notify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.view.ViewCompat;

public class Notify {
    private Intent action = null;
    private boolean autoCancel = false;
    private NotificationCompat.Builder builder;
    private String channelDescription;
    private String channelId;
    private String channelName;
    private boolean circle = false;
    private int color = -1;
    private String content = "Notification test";
    private Context context;
    private int id;
    private int importance;
    private Object largeIcon;
    private int oreoImportance;
    private Object picture = null;
    private int smallIcon;
    private String title = "Notify";
    private boolean vibration = true;
    private long[] vibrationPattern = {0, 250, 250, 250};

    public interface ChannelData {
        public static final String DESCRIPTION = "notify_channel_description";
        public static final String ID = "notify_channel_id";
        public static final String NAME = "notify_channel_name";
    }

    public enum NotifyImportance {
        MIN,
        LOW,
        HIGH,
        MAX
    }

    public NotificationCompat.Builder getNotificationBuilder() {
        return this.builder;
    }

    private Notify(Context context2) {
        this.context = context2;
        ApplicationInfo applicationInfo = context2.getApplicationInfo();
        this.id = (int) System.currentTimeMillis();
        this.largeIcon = Integer.valueOf(applicationInfo.icon);
        this.smallIcon = applicationInfo.icon;
        setDefaultPriority();
        try {
            this.channelId = ResourcesHelper.getStringResourceByKey(this.context, ChannelData.ID);
        } catch (Resources.NotFoundException unused) {
            this.channelId = "NotifyAndroid";
        }
        try {
            this.channelName = ResourcesHelper.getStringResourceByKey(this.context, ChannelData.NAME);
        } catch (Resources.NotFoundException unused2) {
            this.channelName = "NotifyAndroidChannel";
        }
        try {
            this.channelDescription = ResourcesHelper.getStringResourceByKey(this.context, ChannelData.DESCRIPTION);
        } catch (Resources.NotFoundException unused3) {
            this.channelDescription = "Default notification android channel";
        }
        this.builder = new NotificationCompat.Builder(this.context, this.channelId);
    }

    public static Notify build(Context context2) {
        return new Notify(context2);
    }

    public void show() {
        NotificationManager notificationManager;
        Bitmap bitmap;
        Bitmap bitmap2;
        Context context2 = this.context;
        if (context2 != null && (notificationManager = (NotificationManager) context2.getSystemService("notification")) != null) {
            this.builder.setAutoCancel(this.autoCancel).setDefaults(1).setWhen(System.currentTimeMillis()).setSmallIcon(this.smallIcon).setContentTitle(this.title).setContentText(this.content).setStyle(new NotificationCompat.BigTextStyle().bigText(this.content));
            Object obj = this.largeIcon;
            if (obj instanceof String) {
                bitmap = BitmapHelper.getBitmapFromUrl(String.valueOf(obj));
            } else {
                bitmap = BitmapHelper.getBitmapFromRes(this.context, ((Integer) obj).intValue());
            }
            if (bitmap != null) {
                if (this.circle) {
                    bitmap = BitmapHelper.toCircleBitmap(bitmap);
                }
                this.builder.setLargeIcon(bitmap);
            }
            Object obj2 = this.picture;
            if (obj2 != null) {
                if (obj2 instanceof String) {
                    bitmap2 = BitmapHelper.getBitmapFromUrl(String.valueOf(obj2));
                } else {
                    bitmap2 = BitmapHelper.getBitmapFromRes(this.context, ((Integer) obj2).intValue());
                }
                if (bitmap2 != null) {
                    NotificationCompat.BigPictureStyle summaryText = new NotificationCompat.BigPictureStyle().bigPicture(bitmap2).setSummaryText(this.content);
                    summaryText.bigLargeIcon(bitmap);
                    this.builder.setStyle(summaryText);
                }
            }
            int i = Build.VERSION.SDK_INT;
            int i2 = ViewCompat.MEASURED_STATE_MASK;
            if (i >= 23) {
                if (this.color != -1) {
                    i2 = this.context.getResources().getColor(this.color, null);
                }
            } else if (this.color != -1) {
                i2 = this.context.getResources().getColor(this.color);
            }
            this.builder.setColor(i2);
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(this.channelId, this.channelName, this.oreoImportance);
                notificationChannel.setDescription(this.channelDescription);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(i2);
                notificationChannel.enableVibration(this.vibration);
                notificationChannel.setVibrationPattern(this.vibrationPattern);
                notificationManager.createNotificationChannel(notificationChannel);
            } else {
                this.builder.setPriority(this.importance);
            }
            if (this.vibration) {
                this.builder.setVibrate(this.vibrationPattern);
            } else {
                this.builder.setVibrate(new long[]{0});
            }
            Intent intent = this.action;
            if (intent != null) {
                this.builder.setContentIntent(PendingIntent.getActivity(this.context, this.id, intent, 268435456));
            }
            notificationManager.notify(this.id, this.builder.build());
        }
    }

    public Notify setTitle(String str) {
        if (!str.isEmpty()) {
            this.title = str;
        }
        return this;
    }

    public Notify setContent(String str) {
        if (!str.isEmpty()) {
            this.content = str;
        }
        return this;
    }

    public Notify setChannelId(String str) {
        if (!str.isEmpty()) {
            this.channelId = str;
            this.builder.setChannelId(str);
        }
        return this;
    }

    public Notify setChannelName(String str) {
        if (!str.isEmpty()) {
            this.channelName = str;
        }
        return this;
    }

    public Notify setChannelDescription(String str) {
        if (!str.isEmpty()) {
            this.channelDescription = str;
        }
        return this;
    }

    /* renamed from: com.application.isradeleon.notify.Notify$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$application$isradeleon$notify$Notify$NotifyImportance;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            int[] iArr = new int[NotifyImportance.values().length];
            $SwitchMap$com$application$isradeleon$notify$Notify$NotifyImportance = iArr;
            iArr[NotifyImportance.MIN.ordinal()] = 1;
            $SwitchMap$com$application$isradeleon$notify$Notify$NotifyImportance[NotifyImportance.LOW.ordinal()] = 2;
            $SwitchMap$com$application$isradeleon$notify$Notify$NotifyImportance[NotifyImportance.HIGH.ordinal()] = 3;
            try {
                $SwitchMap$com$application$isradeleon$notify$Notify$NotifyImportance[NotifyImportance.MAX.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public Notify setImportance(NotifyImportance notifyImportance) {
        if (Build.VERSION.SDK_INT >= 24) {
            int i = AnonymousClass1.$SwitchMap$com$application$isradeleon$notify$Notify$NotifyImportance[notifyImportance.ordinal()];
            if (i == 1) {
                this.importance = -1;
                this.oreoImportance = 1;
            } else if (i == 2) {
                this.importance = -1;
                this.oreoImportance = 2;
            } else if (i == 3) {
                this.importance = 1;
                this.oreoImportance = 4;
            } else if (i == 4) {
                this.importance = 2;
                this.oreoImportance = 5;
            }
        }
        return this;
    }

    private void setDefaultPriority() {
        this.importance = 0;
        if (Build.VERSION.SDK_INT >= 24) {
            this.oreoImportance = 3;
        }
    }

    public Notify enableVibration(boolean z) {
        this.vibration = z;
        return this;
    }

    public Notify setAutoCancel(boolean z) {
        this.autoCancel = z;
        return this;
    }

    public Notify largeCircularIcon() {
        this.circle = true;
        return this;
    }

    public Notify setVibrationPattern(long[] jArr) {
        this.vibrationPattern = jArr;
        return this;
    }

    public Notify setColor(int i) {
        this.color = i;
        return this;
    }

    public Notify setSmallIcon(int i) {
        this.smallIcon = i;
        return this;
    }

    public Notify setLargeIcon(int i) {
        this.largeIcon = Integer.valueOf(i);
        return this;
    }

    public Notify setLargeIcon(String str) {
        this.largeIcon = str;
        return this;
    }

    public Notify setPicture(int i) {
        this.picture = Integer.valueOf(i);
        return this;
    }

    public Notify setPicture(String str) {
        this.picture = str;
        return this;
    }

    public Notify setAction(Intent intent) {
        this.action = intent;
        return this;
    }

    public Notify setId(int i) {
        this.id = i;
        return this;
    }

    public int getId() {
        return this.id;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public String getChannelDescription() {
        return this.channelDescription;
    }

    public static void cancel(Context context2, int i) {
        NotificationManager notificationManager = (NotificationManager) context2.getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.cancel(i);
        }
    }

    public static void cancelAll(Context context2) {
        NotificationManager notificationManager = (NotificationManager) context2.getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }
}
