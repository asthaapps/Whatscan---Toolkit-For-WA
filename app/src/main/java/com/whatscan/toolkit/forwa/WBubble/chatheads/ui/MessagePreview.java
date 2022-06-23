package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.ChatHeadService;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ChatHeadUtils;
import com.whatscan.toolkit.forwa.WBubble.util.Constant;
import com.google.android.material.badge.BadgeDrawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessagePreview extends Dialog {
    public static int count = 0;
    public static boolean pending = false;
    public static ArrayList<MessagePreview> q = new ArrayList<>();
    private boolean aBoolean;
    private boolean aBoolean1;
    private long aLong = 0;
    private long aLong1 = 3000;
    private Long aLong2;
    private RelativeLayout bubbleBG;
    private ChatHeadService chatHeadService;
    private Context context;
    private Handler handler;
    private TextView previewName;
    private Runnable runnable;
    private TextView singleMessage;
    private SpannableString spannableString;
    private SpannableString spannableString2;
    private String string;
    private View view;

    public MessagePreview(@NonNull Context context2, SpannableString spannableString3, SpannableString spannableString4, String str, Long l, boolean z, ChatHeadService chatHeadService2) {
        super(context2);
        this.context = context2;
        this.spannableString = spannableString3;
        this.spannableString2 = spannableString4;
        this.string = str;
        this.aLong2 = l;
        this.aBoolean1 = z;
        this.chatHeadService = chatHeadService2;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().clearFlags(2);
        if (Build.VERSION.SDK_INT >= 26) {
            getWindow().setType(2038);
        } else {
            getWindow().setType(2002);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setFlags(131080, 262144);
        getLocationParams(false);
        setContentView(getView());
    }

    public int getChatX() {
        int x = (int) this.chatHeadService.stringDefaultChatHeadManager.findChatHeadByKey(this.string).getX();
        this.chatHeadService.stringDefaultChatHeadManager.findChatHeadByKey(this.string).getY();
        if (this.aBoolean1) {
            x = this.chatHeadService.stringDefaultChatHeadManager.getConfig().getInitialPosition().x;
            int i = this.chatHeadService.stringDefaultChatHeadManager.getConfig().getInitialPosition().y;
        }
        return x + this.chatHeadService.stringDefaultChatHeadManager.getConfig().getHeadWidth() + ChatHeadUtils.dpToPx(this.context, 4);
    }

    public int getChatY() {
        int y = (int) this.chatHeadService.stringDefaultChatHeadManager.findChatHeadByKey(this.string).getY();
        if (this.aBoolean1) {
            y = this.chatHeadService.stringDefaultChatHeadManager.getConfig().getInitialPosition().y;
        }
        ChatHeadUtils.dpToPx(this.context, 12);
        return y;
    }

    public void showPreview() {
        try {
            onWindowFocusChanged(false);
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.runnable = new Runnable() {
            public void run() {
                if (MessagePreview.this.view.getParent() != null) {
                    MessagePreview.this.dismiss();
                }
            }
        };
        this.handler = new Handler();
        this.aLong = System.currentTimeMillis();
        this.handler.postDelayed(this.runnable, (long) ((Constant.getInt(getContext(), "settings", "popuppreviewtime", 2).intValue() + 1) * 1000));
    }

    public void dismiss() {
        count = 0;
        View view2 = this.view;
        if (!(view2 == null || view2.getParent() == null || !isDisplaying())) {
            try {
                super.dismiss();
                if (this.handler != null && !this.aBoolean) {
                    this.handler.removeCallbacks(this.runnable);
                    this.aLong = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!this.aBoolean) {
            pending = false;
        }
    }

    public boolean isDisplaying() {
        return isShowing();
    }

    public void toggleFlag() {
        getWindow().clearFlags(8);
        if ((getWindow().getAttributes().flags & 8) == 0) {
            getWindow().setFlags(8, 8);
        }
    }

    public void setHoldState(boolean z) {
        this.aBoolean = z;
        if (!z) {
            return;
        }
        if (this.aLong == 0) {
            this.aLong1 = 3000;
            return;
        }
        if (this.aLong1 == 3000) {
            this.aLong1 = System.currentTimeMillis() - this.aLong;
        }
        Handler handler2 = this.handler;
        if (handler2 != null) {
            handler2.removeCallbacks(this.runnable);
        }
    }

    public WindowManager.LayoutParams getLocationParams(boolean z) {
        toggleFlag();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        @SuppressLint("WrongConstant") Display defaultDisplay = ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i = point.x;
        int dpToPx = ChatHeadUtils.dpToPx(getContext(), 6);
        int chatY = getChatY();
        if (z) {
            chatY = this.chatHeadService.n;
        }
        if (getChatX() > i / 2) {
            attributes.gravity = BadgeDrawable.TOP_END;
            attributes.x = (i - getChatX()) + this.chatHeadService.stringDefaultChatHeadManager.getConfig().getHeadWidth() + dpToPx;
            attributes.y = chatY;
        } else {
            attributes.gravity = BadgeDrawable.TOP_START;
            attributes.x = getChatX();
            attributes.y = chatY;
        }
        return attributes;
    }

    public void updateView() {
        ChatHeadService chatHeadService2;
        if (isDisplaying() && getView() != null && (chatHeadService2 = this.chatHeadService) != null && chatHeadService2.stringDefaultChatHeadManager.findChatHeadByKey(this.string) != null) {
            getWindow().setAttributes(getLocationParams(false));
        }
    }

    public void changeLayout(int i) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.singleMessage.getLayoutParams();
        layoutParams.width = ChatHeadUtils.dpToPx(getContext(), i);
        this.singleMessage.setLayoutParams(layoutParams);
        this.singleMessage.invalidate();
    }

    public View getView() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (this.view == null && this.spannableString != null) {
            this.view = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.instant_reply, (ViewGroup) null, false);
            this.view.setFocusable(false);
            this.view.setClickable(false);
            if (Build.VERSION.SDK_INT >= 26) {
                this.view.setFocusedByDefault(false);
            }
            this.singleMessage = (TextView) this.view.findViewById(R.id.singleMessage);
            this.previewName = (TextView) this.view.findViewById(R.id.previewName);
            this.bubbleBG = (RelativeLayout) this.view.findViewById(R.id.bubbleBG);
            TextView textView = (TextView) this.view.findViewById(R.id.timeStamp);
            if (this.spannableString.toString().trim().length() < 26) {
                changeLayout(155);
            } else {
                changeLayout(215);
            }
            String str = this.string;
            this.previewName.setText(this.string.substring(str.substring(0, str.indexOf(35)).length() + 1));
            this.bubbleBG.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MessagePreview.this.dismiss();
                }
            });
            if (Build.VERSION.SDK_INT >= 24) {
                TextView textView2 = this.singleMessage;
                textView2.setText(((Object) this.spannableString) + "     " + ((Object) this.spannableString2));
            }
            int intValue = Constant.getInt(getContext(), "settings", "popuptextcolor", ViewCompat.MEASURED_STATE_MASK).intValue();
            int intValue2 = Constant.getInt(getContext(), "settings", "popupbackgroundcolor", -1).intValue();
            Drawable drawable = ContextCompat.getDrawable(this.context, R.drawable.shape2);
            drawable.setColorFilter(new PorterDuffColorFilter(intValue2, PorterDuff.Mode.SRC_IN));
            this.bubbleBG.setBackground(drawable);
            this.singleMessage.setTextColor(intValue);
            textView.setTextColor(intValue);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.singleMessage.getLayoutParams();
            layoutParams.width = -2;
            this.singleMessage.setLayoutParams(layoutParams);
            this.singleMessage.setPadding(5, 5, 5, 5);
            this.singleMessage.setText(this.spannableString);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aaa");
            Date date = new Date(this.aLong2.longValue());
            if (defaultSharedPreferences.getBoolean("timeFormat", false)) {
                simpleDateFormat = new SimpleDateFormat("HH:mm");
            }
            textView.setText(simpleDateFormat.format(date));
        }
        return this.view;
    }
}
