package com.whatscan.toolkit.forwa.WBubble.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ChatHeadUtils;
import com.whatscan.toolkit.forwa.WBubble.model.ChatMessage;
import com.whatscan.toolkit.forwa.WBubble.util.Constant;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressLint({"WrongConstant", "ClickableViewAccessibility"})
public class ChatLayoutAdapter extends RecyclerView.Adapter<ChatLayoutAdapter.ChatHolder> {
    public byte[] bmp = null;
    public TextView chatText;
    public Context context;
    public Spannable prevMsg = new SpannableString("");
    public boolean aBoolean = true;
    public Long aLong = 0L;
    public List<ChatMessage> chatMessageList = new ArrayList<>();
    public RecyclerView recyclerView;
    public String string;
    public String string1;

    public ChatLayoutAdapter(Context context2, String str, String str2) {
        context = context2;
        string = str;
        string1 = str2;
    }

    public void add(ChatMessage chatMessage) {
        chatMessageList.add(chatMessage);
    }

    public void setCurrentMsg(Spannable spannable) {
        prevMsg = spannable;
    }

    public void setCurrentTime(Long l) {
        aLong = l;
    }

    public void setByteArray(byte[] bArr) {
        bmp = bArr;
    }

    public Long getPrevTime() {
        return aLong;
    }

    public Spannable getPrevMsg() {
        return prevMsg;
    }

    public List<ChatMessage> getList() {
        return chatMessageList;
    }

    public byte[] getPrevByteArray() {
        return bmp;
    }

    public int getCount() {
        return chatMessageList.size();
    }

    public ChatMessage getItem(int i) {
        return chatMessageList.get(i);
    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView2) {
        super.onAttachedToRecyclerView(recyclerView2);
        recyclerView = recyclerView2;
    }

    @NotNull
    @Override
    public ChatHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new ChatHolder(LayoutInflater.from(context).inflate(R.layout.singlemsg, viewGroup, false));
    }

    public void onBindViewHolder(ChatHolder chatHolder, int i) {
        int i2;
        int i3;
        ChatMessage item = getItem(i);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        chatHolder.singleMessage.setTextSize((float) (Constant.getInt(context, "settings", "chatfonttextsize", 3) + 12));
        if (item.left) {
            int i4 = defaultSharedPreferences.getInt("in: " + string1, -1);
            defaultSharedPreferences.getInt("textin: " + string1, -15329770);
            String str = "";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aaa");
            Date date = new Date(item.time);
            if (defaultSharedPreferences.getBoolean("timeFormat", false)) {
                simpleDateFormat = new SimpleDateFormat("HH:mm");
            }
            String format = simpleDateFormat.format(date);
            if (format.contains(" ")) {
                String substring = format.substring(format.indexOf(" ") + 1);
                for (int i5 = 0; i5 < substring.length(); i5++) {
                    if (substring.charAt(i5) == ' ') {
                        str = str.concat("   ");
                    } else {
                        str = str.concat(" ");
                    }
                }
                int length = substring.length();
                i3 = 2;
                if (length > 2) {
                    str = str.concat(" ");
                }
            } else {
                i3 = 2;
            }
            CharSequence[] charSequenceArr = new CharSequence[i3];
            charSequenceArr[0] = item.message;
            charSequenceArr[1] = str;
            SpannableString spannableString = new SpannableString(TextUtils.concat(charSequenceArr));
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd");
            Date date2 = new Date(System.currentTimeMillis());
            if (i > 0) {
                date2 = new Date(getItem(i - 1).time);
            }
            if (Integer.parseInt(simpleDateFormat2.format(date)) != Integer.parseInt(simpleDateFormat2.format(date2))) {
                chatHolder.dateStamp.setBackground(ContextCompat.getDrawable(context, R.drawable.shape2));
                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd MMM");
                String format2 = simpleDateFormat3.format(date);
                if (format2.equals(simpleDateFormat3.format(System.currentTimeMillis()))) {
                    format2 = "Today";
                }
                chatHolder.dateStamp.setText(format2);
                chatHolder.dateStamp.setVisibility(0);
            } else {
                chatHolder.dateStamp.setVisibility(8);
            }
            if (item.bmpArray == null) {
                chatHolder.bubbleBG.setVisibility(0);
                chatHolder.singleMessage.setText(spannableString);
                chatHolder.timeStamp.setText(format);
            } else {
                chatHolder.bubbleBG.setVisibility(8);
            }
            if (item.bmpArray != null) {
                chatHolder.bubbleImage.setVisibility(0);
                chatHolder.image.setImageBitmap(BitmapFactory.decodeByteArray(item.bmpArray, 0, item.bmpArray.length));
                if (item.message.toString().startsWith("📷") && string1.equals("com.whatsapp")) {
                    chatHolder.bubbleBG.setVisibility(8);
                }
            } else {
                chatHolder.bubbleBG.setVisibility(0);
                chatHolder.bubbleImage.setVisibility(8);
            }

            chatHolder.singleMessage.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() != 1 || !aBoolean) {
                    if (motionEvent.getAction() == 0) {
                        aBoolean = false;
                    }
                    return view.onTouchEvent(motionEvent);
                }
                aBoolean = false;
                return true;
            });
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.incomingshape);
            drawable.setColorFilter(new PorterDuffColorFilter(i4, PorterDuff.Mode.SRC_IN));
            chatHolder.bubbleBG.setBackground(drawable);
            chatHolder.timeStamp.setPadding(ChatHeadUtils.pxToDp(context, 10), ChatHeadUtils.pxToDp(context, 0), ChatHeadUtils.pxToDp(context, 20), ChatHeadUtils.pxToDp(context, 10));
            chatHolder.singleMessage.setPadding(ChatHeadUtils.pxToDp(context, 140), ChatHeadUtils.pxToDp(context, 35), ChatHeadUtils.pxToDp(context, 30), ChatHeadUtils.pxToDp(context, 35));
            chatHolder.singleMessageContainer.setGravity(item.left ? 3 : 5);
            chatHolder.singleMessageParent.setGravity(item.left ? 3 : 5);
            return;
        }
        defaultSharedPreferences.getInt("textout: " + string1, -1);
        String str2 = "";
        SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("hh:mm aaa");
        Date date3 = new Date(item.time);
        if (defaultSharedPreferences.getBoolean("timeFormat", false)) {
            simpleDateFormat4 = new SimpleDateFormat("HH:mm");
        }
        String format3 = simpleDateFormat4.format(date3);
        if (format3.contains(" ")) {
            String substring2 = format3.substring(format3.indexOf(" ") + 1);
            String str3 = str2;
            for (int i6 = 0; i6 < substring2.length(); i6++) {
                if (substring2.charAt(i6) == ' ') {
                    str3 = str3.concat("   ");
                } else {
                    str3 = str3.concat(" ");
                }
            }
            int length2 = substring2.length();
            i2 = 2;
            str2 = length2 > 2 ? str3.concat(" ") : str3;
        } else {
            i2 = 2;
        }
        CharSequence[] charSequenceArr2 = new CharSequence[i2];
        charSequenceArr2[0] = item.message;
        charSequenceArr2[1] = str2;
        SpannableString spannableString2 = new SpannableString(TextUtils.concat(charSequenceArr2));
        SimpleDateFormat simpleDateFormat5 = new SimpleDateFormat("dd");
        Date date4 = new Date(System.currentTimeMillis());
        if (i > 0) {
            date4 = new Date(getItem(i - 1).time);
        }
        if (Integer.parseInt(simpleDateFormat5.format(date3)) != Integer.parseInt(simpleDateFormat5.format(date4))) {
            chatHolder.dateStamp.setBackground(ContextCompat.getDrawable(context, R.drawable.shape2));
            SimpleDateFormat simpleDateFormat6 = new SimpleDateFormat("dd MMM");
            String format4 = simpleDateFormat6.format(date3);
            if (format4.equals(simpleDateFormat6.format(System.currentTimeMillis()))) {
                format4 = "Today";
            }
            chatHolder.dateStamp.setText(format4);
            chatHolder.dateStamp.setVisibility(0);
        } else {
            chatHolder.dateStamp.setVisibility(8);
        }
        if (item.bmpArray == null) {
            chatHolder.bubbleBG.setVisibility(0);
            chatHolder.singleMessage.setText(spannableString2);
            chatHolder.timeStamp.setText(format3);
        } else {
            chatHolder.bubbleBG.setVisibility(8);
        }
        if (item.bmpArray != null) {
            chatHolder.bubbleImage.setVisibility(0);
            chatHolder.image.setImageBitmap(BitmapFactory.decodeByteArray(item.bmpArray, 0, item.bmpArray.length));
            if (item.message.toString().startsWith("📷") && string1.equals("com.whatsapp")) {
                chatHolder.bubbleBG.setVisibility(8);
            }
        } else {
            chatHolder.bubbleBG.setVisibility(0);
            chatHolder.bubbleImage.setVisibility(8);
        }
        chatHolder.bubbleBG.setBackground(ContextCompat.getDrawable(context, R.drawable.outgoingshape));
        chatHolder.timeStamp.setPadding(ChatHeadUtils.pxToDp(context, 10), ChatHeadUtils.pxToDp(context, 0), ChatHeadUtils.pxToDp(context, 130), ChatHeadUtils.pxToDp(context, 10));
        chatHolder.singleMessage.setPadding(ChatHeadUtils.pxToDp(context, 40), ChatHeadUtils.pxToDp(context, 35), ChatHeadUtils.pxToDp(context, 130), ChatHeadUtils.pxToDp(context, 35));
        chatHolder.singleMessageContainer.setGravity(item.left ? 3 : 5);
        chatHolder.singleMessageParent.setGravity(5);
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public static class ChatHolder extends RecyclerView.ViewHolder {
        public RelativeLayout bubbleBG;
        public RelativeLayout bubbleImage;
        public ImageView image;
        public LinearLayout singleMessageContainer;
        public LinearLayout singleMessageParent;
        public TextView title;
        protected TextView dateStamp;
        protected TextView singleMessage;
        protected TextView timeStamp;

        public ChatHolder(View view) {
            super(view);
            singleMessageContainer = view.findViewById(R.id.singleMessageContainer);
            singleMessageParent = view.findViewById(R.id.singleMessageParent);
            singleMessage = view.findViewById(R.id.singleMessage);
            image = view.findViewById(R.id.singleImage);
            bubbleBG = view.findViewById(R.id.bubbleBG);
            bubbleImage = view.findViewById(R.id.bubbleImage);
            bubbleImage.setVisibility(8);
            bubbleBG.setVisibility(0);
            timeStamp = view.findViewById(R.id.timeStamp);
            dateStamp = view.findViewById(R.id.dateStamp);
            title = view.findViewById(R.id.title);
        }
    }
}
