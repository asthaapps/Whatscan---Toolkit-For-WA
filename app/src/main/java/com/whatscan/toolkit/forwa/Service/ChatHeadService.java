package com.whatscan.toolkit.forwa.Service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.WBubble.Adapter.ChatLayoutAdapter;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ChatHeadUtils;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHead;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadArrangement;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadCloseButton;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadListener;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadManager;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadViewAdapter;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.CustomSlide;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.MaximizedArrangement;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.MessagePreview;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.MinimizedArrangement;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.container.DefaultChatHeadManager;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.container.WindowManagerContainer;
import com.whatscan.toolkit.forwa.WBubble.model.ChatMessage;
import com.whatscan.toolkit.forwa.WBubble.util.Constant;
import com.whatscan.toolkit.forwa.WBubble.util.CustomChatHeadConfig;
import com.whatscan.toolkit.forwa.WBubble.util.CustomListView;
import com.whatscan.toolkit.forwa.WBubble.util.MyBounceInterpolator;
import com.whatscan.toolkit.forwa.WBubble.util.RelativeCustom;
import com.whatscan.toolkit.forwa.WBubble.util.setDrawable;
import com.flipkart.circularImageView.CircularDrawable;
import com.flipkart.circularImageView.TextDrawer;
import com.flipkart.circularImageView.notification.RectangularNotificationDrawer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatHeadService extends Service {
    public static Map<String, ChatLayoutAdapter> arrayMap = new HashMap();
    private final IBinder mBinder = new LocalBinder();
    public EditText ChatEditText;
    public ImageView buttonSend;
    public int chatHeadIdentifier = 0;
    public ChatLayoutAdapter chatLayoutAdapter;
    public CustomListView customListView;
    public boolean lastScroll = true;
    public int n = 0;
    public DefaultChatHeadManager<String> stringDefaultChatHeadManager;
    public Map<String, View> viewCache = new HashMap();
    public WindowManagerContainer windowManagerContainer;
    boolean a = false;
    String b = "NULL";
    ImageView background_chat_bg;
    RelativeLayout bottomContainer;
    BroadcastReceiver broadcastReceiver;
    CustomSlide customSlide;
    boolean e = false;
    boolean f = false;
    TextView m;
    MessagePreview messagePreview;
    ScheduledExecutorService scheduledExecutorService;
    RelativeLayout topbar;
    ImageView userimg;

    static void identify(ChatHeadService chatHeadService) {
        int i = chatHeadService.chatHeadIdentifier;
        chatHeadService.chatHeadIdentifier = i + 1;
    }

    public static int randomInt() {
        return new Random().nextInt(5) + 1;
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        broadcastReceiver = new MyReceiver();
        registerReceiver(broadcastReceiver, intentFilter);
        windowManagerContainer = new WindowManagerContainer(this);
        System.out.println("Scheduling");
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::selectTopApp, 0, 800, TimeUnit.MILLISECONDS);
    }

    public void updateAlpha(boolean z) {
        int intValue = (int) ((((float) (100 - Constant.getInt(this, "settings", "bubblealpha", 0))) / 100.0f) * 255.0f);
        DefaultChatHeadManager<String> defaultChatHeadManager = stringDefaultChatHeadManager;
        if (defaultChatHeadManager != null) {
            for (ChatHead<String> chatHead : defaultChatHeadManager.getChatHeads()) {
                if (z || intValue == 255) {
                    chatHead.setImageAlpha(255);
                } else {
                    chatHead.setImageAlpha(0);
                    if (getHero().equals(chatHead.getKey())) {
                        chatHead.setImageAlpha(intValue);
                    }
                }
            }
        }
    }

    public void selectTopApp() {
        new Thread(() -> {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
                    usageStatsManager.queryUsageStats(0, System.currentTimeMillis() - 1000 * 1000, System.currentTimeMillis());
                    UsageEvents queryEvents = usageStatsManager.queryEvents(System.currentTimeMillis() - 1000 * 1000, System.currentTimeMillis());
                    String str = "";
                    while (queryEvents.hasNextEvent()) {
                        UsageEvents.Event event = new UsageEvents.Event();
                        queryEvents.getNextEvent(event);
                        if (event.getEventType() == 1) {
                            str = event.getPackageName();
                        }
                    }
                    if (!str.equals("")) {
                        b = str;
                        return;
                    }
                    Log.i("CheckB", b);
                    return;
                }
                @SuppressLint("WrongConstant") List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) getSystemService("activity")).getRunningAppProcesses();
                b = runningAppProcesses.get(0).processName;
                if (b.equals("android") && runningAppProcesses.size() >= 2) {
                    b = runningAppProcesses.get(1).processName;
                }
            } catch (Exception ignored) {
            }
        }).start();

        new Handler(Looper.getMainLooper()).post(() -> {
            if (Constant.getBoolean(ChatHeadService.this, "settings", "closebubbleopen", false) && stringDefaultChatHeadManager != null) {
                Iterator<ChatHead<String>> it = stringDefaultChatHeadManager.getChatHeads().iterator();
                while (it.hasNext()) {
                    ChatHead<String> next = it.next();
                    if (stringDefaultChatHeadManager.getChatHeads().size() > 0 && next.getKey().startsWith(b)) {
                        it.remove();
                        stringDefaultChatHeadManager.onChatHeadRemoved(next, true);
                        stringDefaultChatHeadManager.getCloseButton().disappear(true, true);
                    }
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    public View initializeLayout(final String str, ViewGroup viewGroup) {
        ApplicationInfo applicationInfo;
        final View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.chat_circle, viewGroup, false);
        TextView textView = inflate.findViewById(R.id.identifier);
        RelativeCustom relativeCustom = inflate.findViewById(R.id.chatbg);
        background_chat_bg = inflate.findViewById(R.id.background_chat_bg);
        topbar = inflate.findViewById(R.id.topbar);
        userimg = inflate.findViewById(R.id.userimg);
        RelativeLayout relativeLayout = inflate.findViewById(R.id.bottomBox);
        customListView = inflate.findViewById(R.id.listView1);
        bottomContainer = inflate.findViewById(R.id.bottomContainer);
        int intValue = Constant.getInt(this, "savetheme", "selectedtheme", 0);

        topbar.setBackgroundResource(Constant.chatheader[intValue]);
        background_chat_bg.setBackgroundResource(Constant.chatbg[intValue]);
        bottomContainer.setBackgroundResource(Constant.chatbottom[intValue]);

        inflate.findViewById(R.id.popup).setOnClickListener(view -> {
            try {
                if (!(NotificationWear.openConv == null || NotificationWear.openConv.get(str) == null)) {
                    Objects.requireNonNull(NotificationWear.openConv.get(str)).send();
                }
                stringDefaultChatHeadManager.setArrangement(MinimizedArrangement.class, null);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        });
        ChatEditText = inflate.findViewById(R.id.sendBox);
        ChatEditText.setSelectAllOnFocus(false);
        ChatEditText.setFocusableInTouchMode(true);
        ChatEditText.setOnClickListener(view -> {
            ChatEditText.setFocusableInTouchMode(true);
            ChatEditText.requestFocus();
            ((InputMethodManager) getSystemService("input_method")).showSoftInput(ChatEditText, 1);
        });
        buttonSend = inflate.findViewById(R.id.sendButton);
        String substring = str.substring(0, str.indexOf(35));
        inflate.findViewById(R.id.replyBorder).setVisibility(8);
        StringBuilder sb = new StringBuilder();
        sb.append("send: ");
        sb.append(substring);
        buttonSend.setOnClickListener(view -> {
            boolean z;
            if (!ChatEditText.getText().toString().trim().isEmpty()) {
                if (!str.equals("dc#" + getString(R.string.app_name))) {
                    ChatHeadService chatHeadService = ChatHeadService.this;
                    z = chatHeadService.replyLastNotification(chatHeadService.ChatEditText.getText().toString(), str, ChatHeadService.this);
                } else {
                    z = true;
                }
                if (z) {
                    UpdateChatText(str, new SpannableString(ChatEditText.getText().toString().trim()), System.currentTimeMillis(), true, null, false);
                    ChatEditText.setText("");
                }
            }
        });
        ChatEditText.setOnEditorActionListener((textView1, i, keyEvent) -> {
            if (i == 4 && buttonSend != null) {
                buttonSend.performClick();
                if (getApplicationContext().getResources().getConfiguration().orientation == 2) {
                    ((InputMethodManager) textView1.getContext().getSystemService("input_method")).hideSoftInputFromWindow(textView1.getWindowToken(), 0);
                }
            }
            return false;
        });
        String substring2 = str.substring(str.indexOf(35) + 1);
        if (substring2.length() > 23) {
            substring2 = substring2.substring(0, 22) + "..";
        }
        textView.setText(substring2);
        TextView textView2 = inflate.findViewById(R.id.replyNotice);
        if (substring2.equals(getResources().getString(R.string.app_name)) || NotificationWear.replyIntent.containsKey(str)) {
            relativeLayout.setVisibility(0);
            buttonSend.setVisibility(0);
            textView2.setVisibility(8);
        } else {
            relativeLayout.setVisibility(8);
            buttonSend.setVisibility(8);
            textView2.setVisibility(0);
            PackageManager packageManager = getPackageManager();
            try {
                applicationInfo = packageManager.getApplicationInfo(str.substring(0, str.indexOf(35)), 0);
            } catch (PackageManager.NameNotFoundException unused) {
                applicationInfo = null;
            }
            String str2 = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
            if (str2.length() > 21) {
                String str3 = str2.substring(0, 18) + "..";
            }
            textView2.setText("No Reply key was found for this message");
        }
        chatLayoutAdapter = arrayMap.get(str);
        if (chatLayoutAdapter == null) {
            chatLayoutAdapter = new ChatLayoutAdapter(this, str, str.substring(0, str.indexOf(35)));
        }
        customListView.setLayoutManager(new LinearLayoutManager(this));
        customListView.addOnLayoutChangeListener((view, i, i2, i3, i4, i5, i6, i7, i8) -> {
            if (i4 < i8) {
                customListView.post(() -> customListView.scrollToPosition(customListView.getAdapter().getItemCount() - 1));
            }
        });
        customListView.setAdapter(chatLayoutAdapter);
        CustomListView customListView2 = customListView;
        customListView2.scrollToPosition(Objects.requireNonNull(customListView2.getAdapter()).getItemCount() - 1);

        chatLayoutAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                customListView.clearFocus();
                customListView.post(() -> customListView.smoothScrollToPosition(chatLayoutAdapter.getCount() - 1));
            }
        });

        customListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                lastScroll = !recyclerView.canScrollVertically(2);
            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                lastScroll = false;
            }
        });

        customSlide = new CustomSlide.Builder(relativeCustom, str, R.id.chatbg).withListeners(new CustomSlide.Listener() {
            @Override
            public void onVisibilityChanged(int i) {
            }

            @Override
            public void onSlide(float f) {
                if (f > 60.0f && (stringDefaultChatHeadManager.getActiveArrangement() instanceof MaximizedArrangement)) {
                    stringDefaultChatHeadManager.setArrangement(MinimizedArrangement.class, null);
                }
            }
        }).withStartState(CustomSlide.State.HIDDEN).withStartGravity(48).withGesturesEnabled(true).build();

        stringDefaultChatHeadManager.setListener(new ChatHeadListener() {
            @Override
            public void onChatHeadAnimateEnd(ChatHead chatHead) {
            }

            @Override
            public void onChatHeadAnimateStart(ChatHead chatHead) {
            }

            @Override
            public void onChatHeadAdded(Object obj) {
                boolean z = stringDefaultChatHeadManager.getActiveArrangement() instanceof MaximizedArrangement;
            }

            @Override
            public void onChatHeadRemoved(Object obj, boolean z) {
                if (Constant.getBoolean(ChatHeadService.this, "settings", "clearhistoryonbubbleclose", false)) {
                    ChatHeadService.arrayMap.remove(obj);
                    viewCache.remove(obj);
                }
                if ((stringDefaultChatHeadManager.getActiveArrangement() instanceof MaximizedArrangement) && stringDefaultChatHeadManager.getChatHeads().size() > 0) {
                    ChatEditText.setFocusableInTouchMode(true);
                    ChatEditText.requestFocus();
                }
            }

            @Override
            public void onChatHeadArrangementChanged(ChatHeadArrangement chatHeadArrangement, ChatHeadArrangement chatHeadArrangement2) {
                if (!(stringDefaultChatHeadManager.getChatHeads() == null || stringDefaultChatHeadManager.getChatHeads().size() == 0 || getHero() == null)) {
                    customSlide.show(getHero());
                }
                if ((chatHeadArrangement instanceof MinimizedArrangement) && (chatHeadArrangement2 instanceof MaximizedArrangement)) {
                    if (messagePreview != null) {
                        messagePreview.dismiss();
                    }
                    if (100 - Constant.getInt(ChatHeadService.this, "settings", "bubblealpha", 0) != 0) {
                        updateAlpha(true);
                    }
                    Animation loadAnimation = AnimationUtils.loadAnimation(ChatHeadService.this, R.anim.bounce);
                    String hero = getHero();
                    RelativeLayout relativeLayout = viewCache.get(hero).findViewById(R.id.root_view1);
                    if (relativeLayout == null) {
                        relativeLayout = inflate.findViewById(R.id.root_view1);
                    }
                    MyBounceInterpolator myBounceInterpolator = new MyBounceInterpolator(0.15d, 6.0d);
                    loadAnimation.setInterpolator(myBounceInterpolator);
                    relativeLayout.startAnimation(loadAnimation);
                    AnimationUtils.loadAnimation(ChatHeadService.this, R.anim.bounceup).setInterpolator(myBounceInterpolator);
                    try {
                        if (hero.equals(str) && NotificationWear.readIntent.get(str) != null) {
                            Objects.requireNonNull(NotificationWear.readIntent.get(str)).send();
                        }
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                } else if (100 - Constant.getInt(ChatHeadService.this, "settings", "bubblealpha", 0) != 100) {
                    updateAlpha(false);
                }
            }
        });
        stringDefaultChatHeadManager.setOnItemSelectedListener(new ChatHeadManager.OnItemSelectedListener<String>() {
            public void onChatHeadRollOut(String str, ChatHead chatHead) {
            }

            public void onChatHeadRollOver(String str, ChatHead chatHead) {
            }

            public boolean onChatHeadSelected(String str, ChatHead chatHead) {
                return false;
            }
        });
        arrayMap.put(str, chatLayoutAdapter);
        viewCache.put(str, inflate);
        MinimizedArrangement.setCustomEventListener(z -> {
            if (e != z) {
                e = z;
                for (ChatHead<String> chatHead : stringDefaultChatHeadManager.getChatHeads()) {
                    new setDrawable(chatHead).execute("0", chatHead.getKey());
                }
            }
            e = z;
        });
        return inflate;
    }

    public void toggleReply(final String str, final boolean z) {
        new Handler(getMainLooper()).post(() -> {
            if (viewCache.get(str) != null) {
                TextView textView = Objects.requireNonNull(viewCache.get(str)).findViewById(R.id.replyNotice);
                RelativeLayout relativeLayout = Objects.requireNonNull(viewCache.get(str)).findViewById(R.id.bottomBox);
                ImageView imageButton = Objects.requireNonNull(viewCache.get(str)).findViewById(R.id.sendButton);
                if (z) {
                    textView.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    imageButton.setVisibility(View.VISIBLE);
                    return;
                }
                relativeLayout.setVisibility(View.GONE);
                imageButton.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint("WrongConstant")
    public boolean isReplyVisible(String str) {
        return Objects.requireNonNull(viewCache.get(str)).findViewById(R.id.bottomBox).getVisibility() != 8;
    }

    public void initAdapter() {
        stringDefaultChatHeadManager = new DefaultChatHeadManager<>(this, windowManagerContainer);
        @SuppressLint("WrongConstant") Display defaultDisplay = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i = point.x;
        int dpToPx = ChatHeadUtils.dpToPx(this, 120);
        CustomChatHeadConfig.diameter = (Constant.getInt(this, "settings", "bubblesize", 50) / 3) + 40;
        if (Constant.getBoolean(this, "settings", "rememberlastposition", false)) {
            stringDefaultChatHeadManager.setConfig(new CustomChatHeadConfig(getApplicationContext(), Constant.getInt(this, "rememberposition", "lastPosX", 0), Constant.getInt(this, "rememberposition", "lastPosY", 0)));
        } else {
            stringDefaultChatHeadManager.setConfig(new CustomChatHeadConfig(getApplicationContext(), i, dpToPx));
        }
        ChatHead.setVibration(Constant.getBoolean(this, "settings", "vibrateonbubbleclose", false));
        stringDefaultChatHeadManager.setViewAdapter(new ChatHeadViewAdapter<String>() {

            @SuppressLint("WrongConstant")
            public View attachView(String str, ChatHead chatHead, ViewGroup viewGroup) {
                View view = viewCache.get(str);
                if (view == null) {
                    view = initializeLayout(str, viewGroup);
                } else if (!stringDefaultChatHeadManager.findChatHeadByKey(str).getUnreadCount().equals("")) {
                    new setDrawable(stringDefaultChatHeadManager.findChatHeadByKey(str)).execute("", str);
                    stringDefaultChatHeadManager.findChatHeadByKey(str).setUnreadCount("");
                    ChatHeadService chatHeadService = ChatHeadService.this;
                    chatHeadService.ChatEditText = Objects.requireNonNull(chatHeadService.viewCache.get(str)).findViewById(R.id.sendBox);
                    ChatEditText.setFocusableInTouchMode(true);
                    ChatEditText.requestFocus();
                }
                ChatHeadService chatHeadService2 = ChatHeadService.this;
                chatHeadService2.topbar = Objects.requireNonNull(chatHeadService2.viewCache.get(str)).findViewById(R.id.topbar);
                ChatHeadService chatHeadService3 = ChatHeadService.this;
                chatHeadService3.background_chat_bg = Objects.requireNonNull(chatHeadService3.viewCache.get(str)).findViewById(R.id.background_chat_bg);
                ChatHeadService chatHeadService4 = ChatHeadService.this;
                chatHeadService4.bottomContainer = Objects.requireNonNull(chatHeadService4.viewCache.get(str)).findViewById(R.id.bottomContainer);
                int intValue = Constant.getInt(ChatHeadService.this, "savetheme", "selectedtheme", 0);

                topbar.setBackgroundResource(Constant.chatheader[intValue]);
                background_chat_bg.setBackgroundResource(Constant.chatbg[intValue]);
                bottomContainer.setBackgroundResource(Constant.chatbottom[intValue]);
                ChatHeadService chatHeadService5 = ChatHeadService.this;
                chatHeadService5.ChatEditText = Objects.requireNonNull(chatHeadService5.viewCache.get(str)).findViewById(R.id.sendBox);
                ChatEditText.setFocusableInTouchMode(true);
                ChatEditText.requestFocus();
                viewGroup.addView(view);
                viewGroup.setOnTouchListener((view1, motionEvent) -> true);
                if (NotificationWear.remoteMap.get(str) != null && !isReplyVisible(str)) {
                    toggleReply(str, true);
                }
                Bitmap readOrCreateBitmap = readOrCreateBitmap(str);
                ChatHeadService chatHeadService6 = ChatHeadService.this;
                chatHeadService6.userimg = Objects.requireNonNull(chatHeadService6.viewCache.get(str)).findViewById(R.id.userimg);
                if (readOrCreateBitmap == null) {
                    userimg.setImageResource(R.drawable.logo);
                } else {
                    userimg.setImageBitmap(readOrCreateBitmap);
                }
                return view;
            }

            public void detachView(String str, ChatHead chatHead, ViewGroup viewGroup) {
                View view = viewCache.get(str);
                if (view != null) {
                    viewGroup.removeView(view);
                }
            }

            @SuppressLint("WrongConstant")
            public void removeView(String str, ChatHead chatHead, ViewGroup viewGroup) {
                if (messagePreview != null && messagePreview.isShowing()) {
                    messagePreview.dismiss();
                }
                if (stringDefaultChatHeadManager.getChatHeads().size() == 0 && chatHeadIdentifier != 0) {
                    MessagePreview.q.clear();
                    MessagePreview.pending = false;
                    stopForeground(true);
                    viewGroup.removeAllViews();
                    viewCache.clear();
                    NotificationWear.replyIntent.clear();
                    if (Constant.getBoolean(ChatHeadService.this, "settings", "clearhistoryonbubbleclose", false)) {
                        ChatHeadService.arrayMap.clear();
                    }
                    NotificationWear.readIntent.clear();
                    NotificationWear.openConv.clear();
                    NotificationWear.remoteMap.clear();
                    NotificationWear.bundleMap.clear();
                    chatHeadIdentifier = 0;
                    new Handler().postDelayed(() -> {
                        windowManagerContainer.destroy();
                        logRootViews();
                    }, 50);
                }
                NotificationWear.notificationID.remove(str);
                if (stringDefaultChatHeadManager.getChatHeads().size() > 0) {
                    View view = viewCache.get(str);
                    if (Constant.getBoolean(ChatHeadService.this, "settings", "clearhistoryonbubbleclose", false)) {
                        ChatHeadService.arrayMap.remove(str);
                    }
                    NotificationWear.remoteMap.remove(str);
                    NotificationWear.replyIntent.remove(str);
                    NotificationWear.bundleMap.remove(str);
                    NotificationWear.openConv.remove(str);
                    viewCache.remove(str);
                    viewGroup.removeView(view);
                } else if (m != null) {
                    m.setVisibility(8);
                }
            }

            public Drawable getChatHeadDrawable(String str) {
                if (stringDefaultChatHeadManager == null || stringDefaultChatHeadManager.findChatHeadByKey(str) == null) {
                    return null;
                }
                return stringDefaultChatHeadManager.findChatHeadByKey(str).getDrawable();
            }
        });
        stringDefaultChatHeadManager.getCloseButton().setListener(new ChatHeadCloseButton.CloseButtonListener() {
            @Override
            public void onCloseButtonAppear() {
            }

            @Override
            public void onCloseButtonDisappear() {
            }
        });
        stringDefaultChatHeadManager.setArrangement(MinimizedArrangement.class, null);
    }

    public void updatebuble() {
        @SuppressLint("WrongConstant") Display defaultDisplay = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i = point.x;
        int dpToPx = ChatHeadUtils.dpToPx(this, 120);
        CustomChatHeadConfig.diameter = (Constant.getInt(this, "settings", "bubblesize", 50) / 3) + 40;
        ChatHead.setVibration(Constant.getBoolean(this, "settings", "vibrateonbubbleclose", false));
        if (Constant.getBoolean(this, "settings", "rememberlastposition", false)) {
            stringDefaultChatHeadManager.setConfig(new CustomChatHeadConfig(getApplicationContext(), Constant.getInt(this, "rememberposition", "lastPosX", 0), Constant.getInt(this, "rememberposition", "lastPosY", 0)));
            return;
        }
        stringDefaultChatHeadManager.setConfig(new CustomChatHeadConfig(getApplicationContext(), i, dpToPx));
    }

    public void logRootViews() {
        try {
            Class<?> cls = Class.forName("android.view.WindowManagerGlobal");
            Object invoke = cls.getMethod("getInstance", new Class[0]).invoke(null, (Object) null);
            Method method = cls.getMethod("getViewRootNames", new Class[0]);
            Method method2 = cls.getMethod("getRootView", String.class);
            String[] strArr = (String[]) method.invoke(invoke, (Object) null);
            for (String str : strArr) {
                View view = (View) method2.invoke(invoke, str);
                if (!str.startsWith(getPackageName())) {
                    stringDefaultChatHeadManager.getChatHeads().size();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void clearUnread(String str) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (stringDefaultChatHeadManager != null) {
                for (ChatHead<String> chatHead : stringDefaultChatHeadManager.getChatHeads()) {
                    String key = chatHead.getKey();
                    if (key.startsWith(key) && !stringDefaultChatHeadManager.findChatHeadByKey(key).getUnreadCount().equals("")) {
                        new setDrawable(stringDefaultChatHeadManager.findChatHeadByKey(key)).execute("", key);
                        stringDefaultChatHeadManager.findChatHeadByKey(key).setUnreadCount("");
                    }
                }
            }
        });
    }

    private Bitmap readOrCreateBitmap(String str) {
        if (str.equals("dc#" + getResources().getString(R.string.app_name))) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.logo).copy(Bitmap.Config.ARGB_8888, true);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        File dir = new ContextWrapper(getApplicationContext()).getDir("chatbubbles", 0);
        if (!new File(dir, str + ".png").exists()) {
            return null;
        }
        return BitmapFactory.decodeFile(dir.getAbsolutePath() + "/" + str + ".png", options);
    }

    public void addChatHead(String str, Spannable spannable, Long l, byte[] bArr, boolean z) {
        RelativeLayout relativeLayout;
        DefaultChatHeadManager<String> defaultChatHeadManager;
        if (!(Constant.getBoolean(this, "settings", "closebubbleopen", false) && b.equals(str.substring(0, str.indexOf(35))))) {
            identify(this);
            if (chatHeadIdentifier == 1 && ((defaultChatHeadManager = stringDefaultChatHeadManager) == null || defaultChatHeadManager.getChatHeads().size() == 0)) {
                initAdapter();
            } else {
                updatebuble();
            }
            if ((stringDefaultChatHeadManager.getActiveArrangement() instanceof MaximizedArrangement) && (relativeLayout = Objects.requireNonNull(viewCache.get(getHero())).findViewById(R.id.root_view1)) != null) {
                relativeLayout.clearAnimation();
            }
            if (!spannable.equals("")) {
                stringDefaultChatHeadManager.addChatHead(str, false, true);
                UpdateChatText(str, spannable, l, false, bArr, false);
                updateAlpha(false);
            }
            a = true;
        }
    }

    @SuppressLint("WrongConstant")
    public void UpdateChatText(String str, Spannable spannable, Long l, boolean z, byte[] bArr, boolean z2) {
        View view;
        chatLayoutAdapter = arrayMap.get(str);
        if (chatLayoutAdapter == null) {
            chatLayoutAdapter = new ChatLayoutAdapter(this, str, str.substring(0, str.indexOf(35)));
            arrayMap.put(str, chatLayoutAdapter);
        }
        if (!z && z2 && bArr == null) {
            Date date = new Date(chatLayoutAdapter.getPrevTime());
            Date date2 = new Date(l);
            if (date2.before(date) && !date2.equals(date)) {
                createChatHead(str, "0");
                return;
            } else if (date2.equals(date) && chatLayoutAdapter.getPrevMsg().toString().startsWith(spannable.toString())) {
                createChatHead(str, "0");
                return;
            }
        }
        SpannableString spannableString = new SpannableString("            ");
        SpannableString spannableString2 = new SpannableString(TextUtils.concat(spannable, spannableString));
        View view2 = viewCache.get(str);
        if (view2 == null) {
            initializeLayout(str, null);
            view = viewCache.get(str);
        } else {
            view = view2;
        }
        assert view != null;
        customListView = view.findViewById(R.id.listView1);
        if (z) {
            chatLayoutAdapter.add(new ChatMessage(false, spannableString2, view, l, bArr));
            chatLayoutAdapter.notifyDataSetChanged();
        } else if (bArr != null && chatLayoutAdapter.getPrevByteArray() != null && l.equals(chatLayoutAdapter.getPrevTime())) {
            return;
        } else {
            if (z2 || !chatLayoutAdapter.getPrevMsg().toString().equals(spannableString2.toString())) {
                if ((stringDefaultChatHeadManager.getActiveArrangement() instanceof MinimizedArrangement) || stringDefaultChatHeadManager.getActiveArrangement() == null) {
                    DefaultChatHeadManager<String> defaultChatHeadManager = stringDefaultChatHeadManager;
                    defaultChatHeadManager.bringToFront(defaultChatHeadManager.findChatHeadByKey(str));
                    stringDefaultChatHeadManager.moveToFirstIndex(str);
                    if (!((KeyguardManager) getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
                        displayPreview(spannableString2, spannableString, str, l, false);
                    }
                }
                createChatHead(str, "1");
                stringDefaultChatHeadManager.findChatHeadByKey(str).setDrawingCacheEnabled(true);
                chatLayoutAdapter.setByteArray(bArr);
                if (!spannableString2.toString().startsWith("📷 WhatsApp Pic")) {
                    chatLayoutAdapter.setCurrentMsg(spannableString2);
                }
                chatLayoutAdapter.add(new ChatMessage(true, spannableString2, view, l, bArr));
                chatLayoutAdapter.notifyDataSetChanged();
            } else {
                createChatHead(str, "0");
            }
        }
        chatLayoutAdapter.setCurrentTime(l);
        viewCache.put(str, view);
    }

    public Drawable setChatHeadDrawable(String str, String str2) {
        String str3;
        CircularDrawable circularDrawable = new CircularDrawable();
        int intValue = Constant.getInt(this, "settings", "bubblebordercolor", -1);
        Bitmap readOrCreateBitmap = readOrCreateBitmap(str2);
        int i = 0;
        if (readOrCreateBitmap == null) {
            circularDrawable = setDefaultPic(str2, circularDrawable, intValue);
        } else {
            circularDrawable.setBitmapOrTextOrIcon(readOrCreateBitmap);
        }
        if (str.equals("%")) {
            return circularDrawable;
        }
        int intValue2 = Constant.getInt(this, "settings", "bubbleborder", 0);
        if (intValue2 != 0) {
            circularDrawable.setBorder(intValue, (float) ChatHeadUtils.dpToPx(this, intValue2));
        }
        /*if (Constant.getBoolean(this, "settings", "displaybadge", true).booleanValue() && (decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.logo)) != null) {
            circularDrawable.setBadge(decodeResource);
        }*/
        if ((stringDefaultChatHeadManager.getActiveArrangement() == null || (stringDefaultChatHeadManager.getActiveArrangement() instanceof MinimizedArrangement) || ((stringDefaultChatHeadManager.getActiveArrangement() instanceof MaximizedArrangement) && stringDefaultChatHeadManager.getActiveArrangement().getHeroIndex() != stringDefaultChatHeadManager.getChatHeads().indexOf(stringDefaultChatHeadManager.findChatHeadByKey(str2)))) && !str.equals("")) {
            if (stringDefaultChatHeadManager.findChatHeadByKey(str2) != null && !stringDefaultChatHeadManager.findChatHeadByKey(str2).getUnreadCount().equals("")) {
                i = Integer.parseInt(stringDefaultChatHeadManager.findChatHeadByKey(str2).getUnreadCount());
            }
            if (str.equals("-1")) {
                str3 = "1";
            } else {
                str3 = "" + (i + Integer.parseInt(str));
            }
            int i2 = 135;
            if (e) {
                i2 = 45;
            }
            if (!str3.equals("0")) {
                circularDrawable.setNotificationDrawer(new RectangularNotificationDrawer().setNotificationText(str3).setNotificationAngle(i2).setNotificationColor(-1, -3791334));
                if (stringDefaultChatHeadManager.findChatHeadByKey(str2) != null) {
                    stringDefaultChatHeadManager.findChatHeadByKey(str2).setUnreadCount(str3);
                }
            }
        }
        circularDrawable.setDivider(4.0f, -1);
        return circularDrawable;
    }

    private CircularDrawable setDefaultPic(String str, CircularDrawable circularDrawable, int i) {
        String substring = str.substring(str.indexOf(35) + 1);
        if (substring.length() > 5) {
            substring = substring.substring(0, 5) + "..";
        }
        if (i == -1) {
            i = -3013560;
        }
        circularDrawable.setBitmapOrTextOrIcon(new TextDrawer().setText(substring).setBackgroundColor(i));
        return circularDrawable;
    }

    public void displayPreview(SpannableString spannableString, SpannableString spannableString2, final String str, Long l, boolean z) {
        if (Constant.getBoolean(this, "settings", "messagepreviewpopup", true)) {
            MessagePreview messagePreview2 = messagePreview;
            if (messagePreview2 != null && messagePreview2.isShowing()) {
                messagePreview.dismiss();
            }
            if (MessagePreview.pending) {
                MessagePreview.q.clear();
                MessagePreview.q.add(new MessagePreview(this, spannableString, spannableString2, str, l, f, this));
            } else {
                messagePreview = new MessagePreview(this, spannableString, spannableString2, str, l, f, this);
                if (z) {
                    messagePreview.show();
                    messagePreview.updateView();
                } else {
                    messagePreview.showPreview();
                }
            }
            stringDefaultChatHeadManager.setChatHeadMovedListener(() -> {
                if (messagePreview.isDisplaying() && stringDefaultChatHeadManager.findChatHeadByKey(str) != null) {
                    stringDefaultChatHeadManager.findChatHeadByKey(str);
                    if (ChatHead.isDragging) {
                        messagePreview.setHoldState(true);
                        messagePreview.dismiss();
                    }
                }
                if (messagePreview.isDisplaying()) {
                    messagePreview.updateView();
                }
            });
        }
    }

    public void createChatHead(String str, String str2) {
        new setDrawable(stringDefaultChatHeadManager.findChatHeadByKey(str)).execute(str2, str);
        stringDefaultChatHeadManager.findChatHeadByKey(str).setPadding(ChatHeadUtils.dpToPx(this, 2), ChatHeadUtils.dpToPx(this, 2), ChatHeadUtils.dpToPx(this, 2), ChatHeadUtils.dpToPx(this, 2));
        stringDefaultChatHeadManager.findChatHeadByKey(str).setBackground(ContextCompat.getDrawable(this, R.drawable.profile_decorator));
        stringDefaultChatHeadManager.findChatHeadByKey(str).setImageAlpha((int) ((((float) (100 - Constant.getInt(this, "settings", "bubblealpha", 0))) / 100.0f) * 255.0f));
    }

    @SuppressLint({"WrongConstant"})
    public boolean replyLastNotification(String str, String str2, Context context) {
        try {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            RemoteInput[] remoteInputArr = new RemoteInput[NotificationWear.remoteMap.get(str2).length];
            Intent intent = new Intent();
            intent.addFlags(268435456);
            Bundle bundle = NotificationWear.bundleMap.get(str2);
            int i = 0;
            for (RemoteInput remoteInput : NotificationWear.remoteMap.get(str2)) {
                remoteInputArr[i] = remoteInput;
                bundle.putCharSequence(remoteInputArr[i].getResultKey(), str);
                i++;
            }
            RemoteInput.addResultsToIntent(remoteInputArr, intent, bundle);
            try {
                NotificationWear.replyIntent.get(str2).send(context, 0, intent);
                if (defaultSharedPreferences.getInt("replySession", 0) <= 4 && !str2.startsWith("dc#")) {
                    SharedPreferences.Editor edit = defaultSharedPreferences.edit();
                    edit.putInt("replySession", defaultSharedPreferences.getInt("replySession", 0) + 1);
                    edit.apply();
                }
                return true;
            } catch (PendingIntent.CanceledException e2) {
                Toast.makeText(context, "Sorry, there was an error in sending text!", 1).show();
                e2.printStackTrace();
                return false;
            }
        } catch (Exception e3) {
            Toast.makeText(context, "Sorry, there was an error in sending text!", 1).show();
            e3.printStackTrace();
            return false;
        }
    }

    public void removeAllChatHeads() {
        chatHeadIdentifier = 0;
        stringDefaultChatHeadManager.removeAllChatHeads(true);
        stringDefaultChatHeadManager.hideOverlayView(true);
    }

    public String getHero() {
        if (stringDefaultChatHeadManager.getActiveArrangement() == null) {
            return stringDefaultChatHeadManager.getChatHeads().get(0).getKey();
        }
        return stringDefaultChatHeadManager.getChatHeads().get(stringDefaultChatHeadManager.getActiveArrangement().getHeroIndex()).getKey();
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            windowManagerContainer.destroy();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
        }
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public ChatHeadService Binderservice() {
            return ChatHeadService.this;
        }
    }
}