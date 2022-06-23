package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

public interface ChatHeadViewAdapter<T> {
    View attachView(T t, ChatHead<? extends Serializable> chatHead, ViewGroup viewGroup);

    void detachView(T t, ChatHead<? extends Serializable> chatHead, ViewGroup viewGroup);

    Drawable getChatHeadDrawable(T t);

    void removeView(T t, ChatHead<? extends Serializable> chatHead, ViewGroup viewGroup);
}
