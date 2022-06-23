package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.rebound.SpringSystem;

import java.io.Serializable;
import java.util.List;

public interface ChatHeadManager<T extends Serializable> {

    ChatHead<T> addChatHead(T t, boolean z, boolean z2);

    View attachView(ChatHead<T> chatHead, ViewGroup viewGroup);

    void bringToFront(ChatHead chatHead);

    void captureChatHeads(ChatHead chatHead);

    void detachView(ChatHead<T> chatHead, ViewGroup viewGroup);

    ChatHead<T> findChatHeadByKey(T t);

    ChatHeadArrangement getActiveArrangement();

    ChatHeadArrangement getArrangement(Class<? extends ChatHeadArrangement> cls);

    Class<? extends ChatHeadArrangement> getArrangementType();

    UpArrowLayout getArrowLayout();

    ChatHeadContainer getChatHeadContainer();

    int[] getChatHeadCoordsForCloseButton(ChatHead chatHead);

    List<ChatHead<T>> getChatHeads();

    ChatHeadCloseButton getCloseButton();

    ChatHeadConfig getConfig();

    void setConfig(ChatHeadConfig chatHeadConfig);

    Context getContext();

    DisplayMetrics getDisplayMetrics();

    double getDistanceCloseButtonFromHead(float f, float f2);

    ChatHeadListener getListener();

    void setListener(ChatHeadListener chatHeadListener);

    int getMaxHeight();

    int getMaxWidth();

    ChatHeadOverlayView getOverlayView();

    SpringSystem getSpringSystem();

    ChatHeadViewAdapter getViewAdapter();

    void setViewAdapter(ChatHeadViewAdapter chatHeadViewAdapter);

    void hideOverlayView(boolean z);

    void onChatHeadMoved();

    void onCloseButtonAppear();

    void onCloseButtonDisappear();

    void onItemRollOut(ChatHead<T> chatHead);

    void onItemRollOver(ChatHead<T> chatHead);

    boolean onItemSelected(ChatHead<T> chatHead);

    void onMeasure(int i, int i2);

    void onRestoreInstanceState(Parcelable parcelable);

    Parcelable onSaveInstanceState(Parcelable parcelable);

    void onSizeChanged(int i, int i2, int i3, int i4);

    void recreateView(T t);

    void reloadDrawable(T t);

    void removeAllChatHeads(boolean z);

    boolean removeChatHead(T t, boolean z);

    void removeView(ChatHead<T> chatHead, ViewGroup viewGroup);

    void selectChatHead(ChatHead chatHead);

    void selectChatHead(T t);

    void setArrangement(Class<? extends ChatHeadArrangement> cls, Bundle bundle);

    void setArrangement(Class<? extends ChatHeadArrangement> cls, Bundle bundle, boolean z);

    void setChatHeadMovedListener(ChatHeadMovedListener chatHeadMovedListener);

    void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener);

    void showOverlayView(boolean z);

    public interface ChatHeadMovedListener {
        void onChatHeadMoved();
    }

    public interface OnItemSelectedListener<T> {
        void onChatHeadRollOut(T t, ChatHead chatHead);

        void onChatHeadRollOver(T t, ChatHead chatHead);

        boolean onChatHeadSelected(T t, ChatHead chatHead);
    }
}
