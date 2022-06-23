package com.whatscan.toolkit.forwa.WBubble.model;

import android.text.Spannable;
import android.view.View;

public class ChatMessage {
    public byte[] bmpArray;
    public boolean left;
    public Spannable message;
    public View row;
    public Long time;

    public ChatMessage(boolean z, Spannable spannable, View view, Long l, byte[] bArr) {
        this.left = z;
        this.message = spannable;
        this.row = view;
        this.time = l;
        this.bmpArray = bArr;
    }
}
