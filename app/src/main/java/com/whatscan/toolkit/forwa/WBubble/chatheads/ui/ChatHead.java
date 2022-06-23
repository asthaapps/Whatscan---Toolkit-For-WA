package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.whatscan.toolkit.forwa.Service.ChatHeadService;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ChatHeadUtils;
import com.whatscan.toolkit.forwa.WBubble.util.Constant;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import java.io.Serializable;

@TargetApi(11)
public class ChatHead<T extends Serializable> extends AppCompatImageView implements SpringListener {
    public static boolean isDragging = false;
    static boolean isvibrate = false;
    final int CLOSE_ATTRACTION_THRESHOLD = ChatHeadUtils.dpToPx(getContext(), 110);
    private final int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private final ChatHeadManager manager;
    private final SpringSystem springSystem;
    private float downTranslationX;
    private float downTranslationY;
    private float downX = -1.0f;
    private float downY = -1.0f;
    private Bundle extras;
    private boolean isHero;
    private boolean isSticky;
    private T key;
    private Spring scaleSpring;
    private State state;
    private String unreadCount = "";
    private VelocityTracker velocityTracker;
    private SpringListener xPositionListener;
    private Spring xPositionSpring;
    private SpringListener yPositionListener;
    private Spring yPositionSpring;

    public ChatHead(Context context) {
        super(context);
        throw new IllegalArgumentException("This constructor cannot be used");
    }

    public ChatHead(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        throw new IllegalArgumentException("This constructor cannot be used");
    }

    public ChatHead(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        throw new IllegalArgumentException("This constructor cannot be used");
    }

    public ChatHead(ChatHeadManager chatHeadManager, SpringSystem springSystem2, Context context, boolean z) {
        super(context);
        manager = chatHeadManager;
        springSystem = springSystem2;
        isSticky = z;
        init();
    }

    public static void setVibration(boolean z) {
        isvibrate = z;
    }

    @Override
    public void onSpringEndStateChange(Spring spring) {
    }

    public boolean isHero() {
        return isHero;
    }

    public void setHero(boolean z) {
        isHero = z;
    }

    public Bundle getExtras() {
        return extras;
    }

    public void setExtras(Bundle bundle) {
        extras = bundle;
    }

    public Spring getHorizontalSpring() {
        return xPositionSpring;
    }

    public Spring getVerticalSpring() {
        return yPositionSpring;
    }

    public boolean isSticky() {
        return isSticky;
    }

    private void init() {
        xPositionListener = new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                manager.getChatHeadContainer().setViewX(ChatHead.this, (int) spring.getCurrentValue());
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                super.onSpringAtRest(spring);
            }
        };

        xPositionSpring = springSystem.createSpring();
        xPositionSpring.addListener(xPositionListener);
        xPositionSpring.addListener(this);
        yPositionListener = new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                if (manager.getChatHeads().size() != 0) {
                    manager.onChatHeadMoved();
                    manager.getChatHeadContainer().setViewY(ChatHead.this, (int) spring.getCurrentValue());
                }
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                super.onSpringAtRest(spring);
            }
        };

        yPositionSpring = springSystem.createSpring();
        yPositionSpring.addListener(yPositionListener);
        yPositionSpring.addListener(this);
        scaleSpring = springSystem.createSpring();
        scaleSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                float currentValue = (float) spring.getCurrentValue();
                setScaleX(currentValue);
                setScaleY(currentValue);
            }
        });
        scaleSpring.setCurrentValue(1.0d).setAtRest();
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String str) {
        if (!str.equals(unreadCount)) {
            manager.reloadDrawable(key);
        }
        unreadCount = str;
    }

    public State getState() {
        return state;
    }

    public void setState(State state2) {
        state = state2;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T t) {
        key = t;
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        Spring spring2;
        Spring spring3 = xPositionSpring;
        if (spring3 != null && (spring2 = yPositionSpring) != null) {
            if (spring == spring3 || spring == spring2) {
                int hypot = (int) Math.hypot(spring3.getVelocity(), spring2.getVelocity());
                if (manager.getActiveArrangement() != null) {
                    manager.getActiveArrangement().onSpringUpdate(this, isDragging, manager.getMaxWidth(), manager.getMaxHeight(), spring, spring3, spring2, hypot);
                }
            }
        }
    }

    @Override
    public void onSpringAtRest(Spring spring) {
        if (manager.getListener() != null) {
            manager.getListener().onChatHeadAnimateEnd(this);
        }
    }

    @Override
    public void onSpringActivate(Spring spring) {
        if (manager.getListener() != null) {
            manager.getListener().onChatHeadAnimateStart(this);
        }
    }

    public SpringListener getHorizontalPositionListener() {
        return xPositionListener;
    }

    public SpringListener getVerticalPositionListener() {
        return yPositionListener;
    }

    @SuppressLint("WrongConstant")
    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        Spring spring;
        Spring spring2 = xPositionSpring;
        if (spring2 == null || (spring = yPositionSpring) == null) {
            return false;
        }
        int action = motionEvent.getAction();
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        float f = rawX - downX;
        float f2 = rawY - downY;
        boolean shouldShowCloseButton = manager.getActiveArrangement().shouldShowCloseButton(this);
        motionEvent.offsetLocation((float) manager.getChatHeadContainer().getViewX(this), (float) manager.getChatHeadContainer().getViewY(this));
        if (action == 0) {
            VelocityTracker velocityTracker2 = velocityTracker;
            if (velocityTracker2 == null) {
                velocityTracker = VelocityTracker.obtain();
            } else {
                velocityTracker2.clear();
            }
            spring2.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
            spring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
            setState(State.FREE);
            downX = rawX;
            downY = rawY;
            downTranslationX = (float) spring2.getCurrentValue();
            downTranslationY = (float) spring.getCurrentValue();
            scaleSpring.setEndValue(0.8999999761581421d);
            spring2.setAtRest();
            spring.setAtRest();
            velocityTracker.addMovement(motionEvent);
        } else if (action == 2) {
            if (Math.hypot(f, f2) > ((double) touchSlop)) {
                isDragging = true;
                if (shouldShowCloseButton) {
                    manager.getCloseButton().appear();
                }
            }
            velocityTracker.addMovement(motionEvent);
            if (isDragging) {
                manager.getCloseButton().pointTo(rawX, rawY);
                if (manager.getActiveArrangement().canDrag(this)) {
                    if (manager.getDistanceCloseButtonFromHead(rawX, rawY) >= ((double) CLOSE_ATTRACTION_THRESHOLD) || !shouldShowCloseButton) {
                        setState(State.FREE);
                        spring2.setSpringConfig(SpringConfigsHolder.DRAGGING);
                        spring.setSpringConfig(SpringConfigsHolder.DRAGGING);
                        spring2.setCurrentValue(downTranslationX + f);
                        spring.setCurrentValue(downTranslationY + f2);
                        manager.getCloseButton().onRelease();
                    } else {
                        if (isvibrate && getState() != State.CAPTURED) {
                            @SuppressLint("WrongConstant") Vibrator vibrator = (Vibrator) getContext().getSystemService("vibrator");
                            @SuppressLint("WrongConstant") AudioAttributes build = new AudioAttributes.Builder().setContentType(4).setUsage(4).build();
                            if (Build.VERSION.SDK_INT >= 26) {
                                vibrator.vibrate(VibrationEffect.createOneShot(70, -1), build);
                            } else {
                                ((Vibrator) getContext().getApplicationContext().getSystemService("vibrator")).vibrate(70);
                            }
                        }
                        if (Constant.getBoolean(getContext(), "settings", "rememberlastposition", false)) {
                            Constant.saveInt(getContext(), "rememberposition", "lastPosX", (int) MinimizedArrangement.currentPosX);
                            Constant.saveInt(getContext(), "rememberposition", "lastPosY", (int) MinimizedArrangement.currentPosY);
                        }
                        if (Constant.getBoolean(getContext(), "settings", "clearhistoryonbubbleclose", false)) {
                            Log.e("getKey", getKey() + " ::::");
                            ChatHeadService.arrayMap.remove(getKey());
                        }
                        setState(State.CAPTURED);
                        spring2.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                        spring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                        int[] chatHeadCoordsForCloseButton = manager.getChatHeadCoordsForCloseButton(this);
                        spring2.setEndValue(chatHeadCoordsForCloseButton[0]);
                        spring.setEndValue(chatHeadCoordsForCloseButton[1]);
                        manager.getCloseButton().onCapture();
                    }
                    velocityTracker.computeCurrentVelocity(1000);
                }
            }
        } else if (action == 1 || action == 3) {
            boolean z = isDragging;
            spring2.setSpringConfig(SpringConfigsHolder.DRAGGING);
            spring2.setSpringConfig(SpringConfigsHolder.DRAGGING);
            isDragging = false;
            scaleSpring.setEndValue(1.0d);
            int xVelocity = (int) velocityTracker.getXVelocity();
            int yVelocity = (int) velocityTracker.getYVelocity();
            velocityTracker.recycle();
            velocityTracker = null;
            if (!(xPositionSpring == null || yPositionSpring == null)) {
                if ((manager.getActiveArrangement() instanceof MaximizedArrangement) && manager.getChatHeads().size() < 2) {
                    manager.setArrangement(MinimizedArrangement.class, null);
                }
                manager.getActiveArrangement().handleTouchUp(this, xVelocity, yVelocity, spring2, spring, z);
            }
        }
        return true;
    }

    public void onRemove() {
        xPositionSpring.setAtRest();
        xPositionSpring.removeAllListeners();
        xPositionSpring.destroy();
        xPositionSpring = null;
        yPositionSpring.setAtRest();
        yPositionSpring.removeAllListeners();
        yPositionSpring.destroy();
        yPositionSpring = null;
        scaleSpring.setAtRest();
        scaleSpring.removeAllListeners();
        scaleSpring.destroy();
        scaleSpring = null;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    public enum State {
        FREE,
        CAPTURED
    }
}