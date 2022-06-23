package com.whatscan.toolkit.forwa.WBubble.chatheads.ui.container;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHead;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadArrangement;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadCloseButton;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadConfig;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadContainer;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadDefaultConfig;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadListener;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadManager;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadOverlayView;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadViewAdapter;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.MaximizedArrangement;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.MinimizedArrangement;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.SpringConfigsHolder;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.UpArrowLayout;
import com.facebook.rebound.SpringConfigRegistry;
import com.facebook.rebound.SpringSystem;
import com.google.android.material.badge.BadgeDrawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@TargetApi(11)
public class DefaultChatHeadManager<T extends Serializable> implements ChatHeadCloseButton.CloseButtonListener, ChatHeadManager<T> {
    private final Map<Class<? extends ChatHeadArrangement>, ChatHeadArrangement> arrangements = new HashMap(3);
    private final ChatHeadContainer chatHeadContainer;
    private final Context context;
    ChatHeadManager.ChatHeadMovedListener a;
    ChatHeadArrangement activeArrangement;
    Bundle activeArrangementBundle;
    UpArrowLayout arrowLayout;
    List<ChatHead<T>> chatHeads;
    ChatHeadCloseButton closeButton;
    ChatHeadConfig config;
    DisplayMetrics displayMetrics;
    ChatHeadManager.OnItemSelectedListener<T> itemSelectedListener;
    ChatHeadListener listener;
    int maxHeight;
    int maxWidth;
    ChatHeadOverlayView overlayView;
    boolean overlayVisible;
    ArrangementChangeRequest requestedArrangement;
    SpringSystem springSystem;
    ChatHeadViewAdapter<T> viewAdapter;

    public DefaultChatHeadManager(Context context2, ChatHeadContainer chatHeadContainer2) {
        context = context2;
        chatHeadContainer = chatHeadContainer2;
        displayMetrics = chatHeadContainer2.getDisplayMetrics();
        init(context2, new ChatHeadDefaultConfig(context2));
    }

    @Override
    public void onCloseButtonDisappear() {
    }

    @Override
    public ChatHeadContainer getChatHeadContainer() {
        return chatHeadContainer;
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    @Override
    public ChatHeadListener getListener() {
        return listener;
    }

    @Override
    public void setListener(ChatHeadListener chatHeadListener) {
        listener = chatHeadListener;
    }

    @Override
    public List<ChatHead<T>> getChatHeads() {
        return chatHeads;
    }

    @Override
    public ChatHeadViewAdapter getViewAdapter() {
        return viewAdapter;
    }

    @Override
    public void setViewAdapter(ChatHeadViewAdapter chatHeadViewAdapter) {
        viewAdapter = chatHeadViewAdapter;
    }

    @Override
    public ChatHeadCloseButton getCloseButton() {
        return closeButton;
    }

    @Override
    public int getMaxWidth() {
        return maxWidth;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Class<? extends ChatHeadArrangement> getArrangementType() {
        if (activeArrangement != null) {
            return activeArrangement.getClass();
        } else if (requestedArrangement != null) {
            return requestedArrangement.getArrangement();
        }
        return null;
    }


    @Override
    public ChatHeadArrangement getActiveArrangement() {
        if (activeArrangement != null) {
            return activeArrangement;
        }
        return null;
    }

    @Override
    public void selectChatHead(ChatHead chatHead) {
        if (activeArrangement != null) {
            activeArrangement.selectChatHead(chatHead);
        }
    }

    @Override
    public void selectChatHead(T t) {
        ChatHead<T> findChatHeadByKey = findChatHeadByKey(t);
        if (findChatHeadByKey != null) {
            selectChatHead(findChatHeadByKey);
        }
    }

    @Override
    public void onMeasure(int height, int width) {
        boolean needsLayout = false;
        if (height != maxHeight && width != maxWidth) {
            needsLayout = true;
        }
        maxHeight = height;
        maxWidth = width;

        int closeButtonCenterX = (int) ((float) width * 0.5f);
        int closeButtonCenterY = (int) ((float) height * 0.9f);

        closeButton.onParentHeightRefreshed();
        closeButton.setCenter(closeButtonCenterX, closeButtonCenterY);

        if (maxHeight > 0 && maxWidth > 0) {
            if (requestedArrangement != null) {
                setArrangementImpl(requestedArrangement);
                requestedArrangement = null;
            } else {
                if (needsLayout) {
                    setArrangementImpl(new ArrangementChangeRequest(activeArrangement.getClass(), null, false));
                }
            }
        }
    }

    @Override
    public ChatHead<T> addChatHead(T t, boolean z, boolean z2) {
        ChatHeadArrangement chatHeadArrangement;
        ChatHead<T> findChatHeadByKey = findChatHeadByKey(t);
        if (findChatHeadByKey == null) {
            findChatHeadByKey = new ChatHead<>(this, springSystem, getContext(), z);
            findChatHeadByKey.setKey(t);
            chatHeads.add(findChatHeadByKey);
            chatHeadContainer.addView(findChatHeadByKey, chatHeadContainer.createLayoutParams(getConfig().getHeadWidth(), getConfig().getHeadHeight(), BadgeDrawable.TOP_START, 0));
            if (chatHeads.size() > config.getMaxChatHeads(maxWidth, maxHeight) && (chatHeadArrangement = activeArrangement) != null) {
                chatHeadArrangement.removeOldestChatHead();
            }
            reloadDrawable(t);
            if (activeArrangement != null) {
                activeArrangement.onChatHeadAdded(findChatHeadByKey, z2);
            } else {
                findChatHeadByKey.getHorizontalSpring().setCurrentValue(-100.0d);
                findChatHeadByKey.getVerticalSpring().setCurrentValue(-100.0d);
            }
            if (listener != null) {
                listener.onChatHeadAdded(t);
            }
        }
        return findChatHeadByKey;
    }

    @Override
    public ChatHead<T> findChatHeadByKey(T t) {
        for (ChatHead<T> chatHead : chatHeads) {
            if (chatHead.getKey().equals(t)) {
                return chatHead;
            }
        }
        return null;
    }

    @Override
    public void reloadDrawable(T t) {
        if (viewAdapter.getChatHeadDrawable(t) != null) {
            findChatHeadByKey(t).setImageDrawable(viewAdapter.getChatHeadDrawable(t));
        }
    }

    @Override
    public void removeAllChatHeads(boolean userTriggered) {
        for (Iterator<ChatHead<T>> iterator = chatHeads.iterator(); iterator.hasNext(); ) {
            ChatHead<T> chatHead = iterator.next();
            iterator.remove();
            onChatHeadRemoved(chatHead, userTriggered);
        }
    }

    @Override
    public boolean removeChatHead(T key, boolean userTriggered) {
        ChatHead chatHead = findChatHeadByKey(key);
        if (chatHead != null) {
            chatHeads.remove(chatHead);
            onChatHeadRemoved(chatHead, userTriggered);
            return true;
        }
        return false;
    }

    public void onChatHeadRemoved(ChatHead chatHead, boolean z) {
        if (chatHead != null && chatHead.getParent() != null) {
            chatHead.onRemove();
            chatHeadContainer.removeView(chatHead);
            if (activeArrangement != null) {
                activeArrangement.onChatHeadRemoved(chatHead);
            }
            if (listener != null) {
                listener.onChatHeadRemoved(chatHead.getKey(), z);
            }
        }
    }

    public void moveToFirstIndex(T t) {
        ChatHead<T> findChatHeadByKey = findChatHeadByKey(t);
        if (findChatHeadByKey != null) {
            chatHeads.remove(findChatHeadByKey);
            chatHeads.add(findChatHeadByKey);
        }
    }

    @Override
    public ChatHeadOverlayView getOverlayView() {
        return overlayView;
    }

    @SuppressLint("WrongConstant")
    private void init(Context context2, ChatHeadConfig chatHeadConfig) {
        chatHeadContainer.onInitialized(this);
        DisplayMetrics displayMetrics2 = new DisplayMetrics();
        ((WindowManager) context2.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics2);
        displayMetrics = displayMetrics2;
        config = chatHeadConfig;
        chatHeads = new ArrayList(5);
        arrowLayout = new UpArrowLayout(context2);
        arrowLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        ChatHeadContainer chatHeadContainer2 = chatHeadContainer;
        UpArrowLayout upArrowLayout = arrowLayout;
        chatHeadContainer2.addView(upArrowLayout, upArrowLayout.getLayoutParams());
        arrowLayout.setVisibility(8);
        springSystem = SpringSystem.create();
        closeButton = new ChatHeadCloseButton(context2, this, maxHeight, maxWidth);
        ViewGroup.LayoutParams createLayoutParams = chatHeadContainer.createLayoutParams(chatHeadConfig.getCloseButtonHeight(), chatHeadConfig.getCloseButtonWidth(), BadgeDrawable.TOP_START, 0);
        closeButton.setListener(this);
        chatHeadContainer.addView(closeButton, createLayoutParams);
        arrangements.put(MinimizedArrangement.class, new MinimizedArrangement(this));
        arrangements.put(MaximizedArrangement.class, new MaximizedArrangement(this));
        setupOverlay(context2);
        setConfig(chatHeadConfig);
        SpringConfigRegistry.getInstance().addSpringConfig(SpringConfigsHolder.DRAGGING, "dragging mode");
        SpringConfigRegistry.getInstance().addSpringConfig(SpringConfigsHolder.NOT_DRAGGING, "not dragging mode");
    }

    private void setupOverlay(Context context2) {
        overlayView = new ChatHeadOverlayView(context2);
        overlayView.setBackgroundResource(R.drawable.overlay_transition);
        getChatHeadContainer().addView(overlayView, getChatHeadContainer().createLayoutParams(-1, -1, 0, 0));
    }

    @Override
    public double getDistanceCloseButtonFromHead(float f, float f2) {
        if (closeButton.isDisappeared()) {
            return Double.MAX_VALUE;
        }
        return Math.hypot((double) (((f - ((float) closeButton.getLeft())) - ((float) getChatHeadContainer().getViewX(closeButton))) - ((float) (closeButton.getMeasuredWidth() / 2))), (double) (((f2 - ((float) closeButton.getTop())) - ((float) getChatHeadContainer().getViewY(closeButton))) - ((float) (closeButton.getMeasuredHeight() / 2))));
    }

    @Override
    public UpArrowLayout getArrowLayout() {
        return arrowLayout;
    }

    @Override
    public void captureChatHeads(ChatHead chatHead) {
        activeArrangement.onCapture(this, chatHead);
    }

    @Override
    public ChatHeadArrangement getArrangement(Class<? extends ChatHeadArrangement> cls) {
        return arrangements.get(cls);
    }

    @Override
    public void setArrangement(Class<? extends ChatHeadArrangement> cls, Bundle bundle) {
        setArrangement(cls, bundle, true);
    }

    @Override
    public void setArrangement(Class<? extends ChatHeadArrangement> cls, Bundle bundle, boolean z) {
        requestedArrangement = new ArrangementChangeRequest(cls, bundle, z);
        chatHeadContainer.requestLayout();
    }

    private void setArrangementImpl(ArrangementChangeRequest requestedArrangementParam) {
        boolean hasChanged = false;
        ChatHeadArrangement requestedArrangement = arrangements.get(requestedArrangementParam.getArrangement());
        ChatHeadArrangement oldArrangement = null;
        ChatHeadArrangement newArrangement = requestedArrangement;
        Bundle extras = requestedArrangementParam.getExtras();
        if (activeArrangement != requestedArrangement) hasChanged = true;
        if (extras == null) extras = new Bundle();

        if (activeArrangement != null) {
            extras.putAll(activeArrangement.getRetainBundle());
            activeArrangement.onDeactivate(maxWidth, maxHeight);
            oldArrangement = activeArrangement;
        }
        activeArrangement = requestedArrangement;
        activeArrangementBundle = extras;
        requestedArrangement.onActivate(this, extras, maxWidth, maxHeight, requestedArrangementParam.isAnimated());
        if (hasChanged) {
            chatHeadContainer.onArrangementChanged(oldArrangement, newArrangement);
            if (listener != null)
                listener.onChatHeadArrangementChanged(oldArrangement, newArrangement);
        }

    }

    @Override
    public void hideOverlayView(boolean animated) {
        if (overlayVisible) {
            TransitionDrawable drawable = (TransitionDrawable) overlayView.getBackground();
            int duration = 200;
            if (!animated) duration = 0;
            drawable.reverseTransition(duration);
            overlayView.setClickable(false);
            overlayVisible = false;
        }
    }

    @Override
    public void showOverlayView(boolean animated) {
        if (!overlayVisible) {
            TransitionDrawable drawable = (TransitionDrawable) overlayView.getBackground();
            int duration = 200;
            if (!animated) duration = 0;
            drawable.startTransition(duration);
            overlayView.setClickable(true);
            overlayVisible = true;
        }
    }

    @Override
    public int[] getChatHeadCoordsForCloseButton(ChatHead chatHead) {
        return new int[]{((closeButton.getLeft() + closeButton.getEndValueX()) + (closeButton.getMeasuredWidth() / 2)) - (chatHead.getMeasuredWidth() / 2), ((closeButton.getTop() + closeButton.getEndValueY()) + (closeButton.getMeasuredHeight() / 2)) - (chatHead.getMeasuredHeight() / 2)};
    }

    @Override
    public void setOnItemSelectedListener(ChatHeadManager.OnItemSelectedListener<T> onItemSelectedListener) {
        itemSelectedListener = onItemSelectedListener;
    }

    @Override
    public boolean onItemSelected(ChatHead<T> chatHead) {
        return itemSelectedListener != null && itemSelectedListener.onChatHeadSelected(chatHead.getKey(), chatHead);
    }

    @Override
    public void onItemRollOver(ChatHead<T> chatHead) {
        if (itemSelectedListener != null) {
            itemSelectedListener.onChatHeadRollOver(chatHead.getKey(), chatHead);
        }
    }

    @Override
    public void onItemRollOut(ChatHead<T> chatHead) {
        if (itemSelectedListener != null) {
            itemSelectedListener.onChatHeadRollOut(chatHead.getKey(), chatHead);
        }
    }

    @Override
    public void bringToFront(ChatHead chatHead) {
        if (activeArrangement != null) {
            activeArrangement.bringToFront(chatHead);
        }
    }

    @Override
    public void onCloseButtonAppear() {
        getConfig().isCloseButtonHidden();
    }

    @Override
    public void recreateView(T t) {
        detachView(findChatHeadByKey(t), getArrowLayout());
        removeView(findChatHeadByKey(t), getArrowLayout());
        if (activeArrangement != null) {
            activeArrangement.onReloadFragment(findChatHeadByKey(t));
        }
    }

    @Override
    public SpringSystem getSpringSystem() {
        return springSystem;
    }

    @Override
    public View attachView(ChatHead<T> chatHead, ViewGroup viewGroup) {
        return viewAdapter.attachView(chatHead.getKey(), chatHead, viewGroup);
    }

    @Override
    public void removeView(ChatHead<T> chatHead, ViewGroup viewGroup) {
        if (chatHeads.size() > 0) {
            viewAdapter.removeView(chatHead.getKey(), chatHead, viewGroup);
        }
    }

    @Override
    public void detachView(ChatHead<T> chatHead, ViewGroup viewGroup) {
        if (chatHeads.size() > 0) {
            viewAdapter.detachView(chatHead.getKey(), chatHead, viewGroup);
        }
    }

    @Override
    public ChatHeadConfig getConfig() {
        return config;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void setConfig(ChatHeadConfig chatHeadConfig) {
        config = chatHeadConfig;
        if (closeButton != null) {
            if (chatHeadConfig.isCloseButtonHidden()) {
                closeButton.setVisibility(8);
            } else {
                closeButton.setVisibility(0);
            }
        }
        for (Map.Entry<Class<? extends ChatHeadArrangement>, ChatHeadArrangement> entry : arrangements.entrySet()) {
            entry.getValue().onConfigChanged(chatHeadConfig);
        }
    }

    @Override
    public void setChatHeadMovedListener(ChatHeadManager.ChatHeadMovedListener chatHeadMovedListener) {
        a = chatHeadMovedListener;
    }

    @Override
    public void onChatHeadMoved() {
        ChatHeadManager.ChatHeadMovedListener chatHeadMovedListener = a;
        if (chatHeadMovedListener != null) {
            chatHeadMovedListener.onChatHeadMoved();
        }
    }

    @Override
    public Parcelable onSaveInstanceState(Parcelable parcelable) {
        SavedState savedState = new SavedState(parcelable);
        if (activeArrangement != null) {
            savedState.setActiveArrangement(activeArrangement.getClass());
            savedState.setActiveArrangementBundle(activeArrangement.getRetainBundle());
        }
        LinkedHashMap<T, Boolean> chatHeadState = new LinkedHashMap<>();
        for (ChatHead<T> chatHead : chatHeads) {
            T key = chatHead.getKey();
            boolean sticky = chatHead.isSticky();
            chatHeadState.put(key, sticky);
        }
        savedState.setChatHeads(chatHeadState);
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            final Class activeArrangementClass = savedState.getActiveArrangement();
            final Bundle activeArrangementBundle = savedState.getActiveArrangementBundle();
            final Map<? extends Serializable, Boolean> chatHeads = savedState.getChatHeads();
            for (Map.Entry<? extends Serializable, Boolean> entry : chatHeads.entrySet()) {
                T key = (T) entry.getKey();
                Boolean sticky = entry.getValue();
                addChatHead(key, sticky, false);
            }
            if (activeArrangementClass != null) {
                setArrangement(activeArrangementClass, activeArrangementBundle, false);
            }
        } else {
        }
    }

    @Override
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (closeButton != null) {
            closeButton.onParentHeightRefreshed();
        }
    }


    public static class SavedState extends View.BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };

        private Class<? extends ChatHeadArrangement> activeArrangement;
        private Bundle activeArrangementBundle;
        private LinkedHashMap<? extends Serializable, Boolean> chatHeads;

        public SavedState(Parcel parcel) {
            super(parcel);
            activeArrangement = (Class) parcel.readSerializable();
            activeArrangementBundle = parcel.readBundle();
            chatHeads = (LinkedHashMap) parcel.readSerializable();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public Class<? extends ChatHeadArrangement> getActiveArrangement() {
            return activeArrangement;
        }

        public void setActiveArrangement(Class<? extends ChatHeadArrangement> cls) {
            activeArrangement = cls;
        }

        public Bundle getActiveArrangementBundle() {
            return activeArrangementBundle;
        }

        public void setActiveArrangementBundle(Bundle bundle) {
            activeArrangementBundle = bundle;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeSerializable(activeArrangement);
            parcel.writeBundle(activeArrangementBundle);
            parcel.writeSerializable(chatHeads);
        }

        public Map<? extends Serializable, Boolean> getChatHeads() {
            return chatHeads;
        }

        public void setChatHeads(LinkedHashMap<? extends Serializable, Boolean> linkedHashMap) {
            chatHeads = linkedHashMap;
        }
    }

    public class ArrangementChangeRequest {
        private final boolean animated;
        private final Class<? extends ChatHeadArrangement> arrangement;
        private final Bundle extras;

        public ArrangementChangeRequest(Class<? extends ChatHeadArrangement> cls, Bundle bundle, boolean z) {
            arrangement = cls;
            extras = bundle;
            animated = z;
        }

        public Bundle getExtras() {
            return extras;
        }

        public Class<? extends ChatHeadArrangement> getArrangement() {
            return arrangement;
        }

        public boolean isAnimated() {
            return animated;
        }
    }
}