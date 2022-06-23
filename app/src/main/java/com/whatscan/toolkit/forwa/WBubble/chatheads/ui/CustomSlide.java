package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.whatscan.toolkit.forwa.Service.AllNotificationService;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressLint({"WrongConstant"})
public class CustomSlide implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener, View.OnTouchListener {
    private static final String KEY_AUTO_SLIDE_DURATION = "SlideUp_auto_slide_duration";
    private static final String KEY_DEBUG = "SlideUp_debug";
    private static final String KEY_HIDE_SOFT_INPUT = "SlideUp_hide_soft_input";
    private static final String KEY_START_GRAVITY = "SlideUp_start_gravity";
    private static final String KEY_STATE = "SlideUp_state";
    private static final String KEY_TOUCHABLE_AREA = "SlideUp_touchable_area";
    private static final String TAG = "SlideUp";
    public static String name;
    public View sliderView;
    public int startGravity;
    public float viewHeight;
    public float viewWidth;
    private int autoSlideDuration;
    private boolean canSlide;
    private State currentState;
    private boolean debug;
    private float density;
    private boolean gesturesEnabled;
    private boolean hideKeyboard;
    private TimeInterpolator interpolator;
    private boolean isRTL;
    private int layout;
    private List<Listener> listeners;
    private float maxSlidePosition;
    private float slideAnimationTo;
    private float startPositionX;
    private float startPositionY;
    private State startState;
    private float touchableArea;
    private ValueAnimator valueAnimator;
    private float viewStartPositionX;
    private float viewStartPositionY;

    private CustomSlide(Builder builder) {
        this.canSlide = true;
        this.startGravity = builder.startGravity;
        this.listeners = builder.listeners;
        this.sliderView = builder.sliderView;
        this.layout = builder.layout;
        this.startState = builder.startState;
        this.density = builder.density;
        this.touchableArea = builder.touchableArea;
        this.autoSlideDuration = builder.autoSlideDuration;
        this.debug = builder.debug;
        this.isRTL = builder.isRTL;
        this.gesturesEnabled = builder.gesturesEnabled;
        this.hideKeyboard = builder.hideKeyboard;
        this.interpolator = builder.interpolator;
        init();
    }

    private void d(String str, String str2, String str3) {
    }

    public final void onAnimationCancel(Animator animator) {
    }

    public final void onAnimationRepeat(Animator animator) {
    }

    public void hideSoftInput() {
        ((InputMethodManager) this.sliderView.getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.sliderView.getWindowToken(), 2);
    }

    public void showSoftInput() {
        ((InputMethodManager) this.sliderView.getContext().getSystemService("input_method")).showSoftInput(this.sliderView, 0);
    }

    private void init() {
        this.sliderView.setOnTouchListener(this);
        createAnimation();
        this.sliderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                CustomSlide customSlide = CustomSlide.this;
                customSlide.viewHeight = (float) customSlide.sliderView.getHeight();
                CustomSlide customSlide2 = CustomSlide.this;
                customSlide2.viewWidth = (float) customSlide2.sliderView.getWidth();
                int i = CustomSlide.this.startGravity;
                if (i == 48) {
                    CustomSlide.this.sliderView.setPivotY(CustomSlide.this.viewHeight);
                } else if (i == 80) {
                    CustomSlide.this.sliderView.setPivotY(0.0f);
                } else if (i == 8388611) {
                    CustomSlide.this.sliderView.setPivotX(0.0f);
                } else if (i == 8388613) {
                    CustomSlide.this.sliderView.setPivotX(CustomSlide.this.viewWidth);
                }
                CustomSlide.this.updateToCurrentState();
                ViewTreeObserver viewTreeObserver = CustomSlide.this.sliderView.getViewTreeObserver();
                if (Build.VERSION.SDK_INT <= 16) {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                } else {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                }
            }
        });
        updateToCurrentState();
    }

    public void updateToCurrentState() {
        switch (this.startState) {
            case HIDDEN:
                hideImmediately();
                return;
            case SHOWED:
                showImmediately();
                return;
            default:
                return;
        }
    }

    public boolean isVisible() {
        return this.sliderView.getVisibility() == 0;
    }

    public void addSlideListener(@NonNull Listener listener) {
        this.listeners.add(listener);
    }

    public void removeSlideListener(@NonNull Listener listener) {
        this.listeners.remove(listener);
    }

    public <T extends View> T getSliderView() {
        return (T) this.sliderView;
    }

    public float getAutoSlideDuration() {
        return (float) this.autoSlideDuration;
    }

    public void setAutoSlideDuration(int i) {
        this.autoSlideDuration = i;
    }

    public float getTouchableArea() {
        return this.touchableArea / this.density;
    }

    public void setTouchableArea(float f) {
        this.touchableArea = f * this.density;
    }

    public boolean isAnimationRunning() {
        ValueAnimator valueAnimator2 = this.valueAnimator;
        return valueAnimator2 != null && valueAnimator2.isRunning();
    }

    public void show(String str) {
        this.sliderView = AllNotificationService.chatHeadService.viewCache.get(str).findViewById(this.layout);
        show(false);
    }

    public void setKey(String str) {
        name = str;
    }

    public void show() {
        this.sliderView = AllNotificationService.chatHeadService.viewCache.get(name).findViewById(this.layout);
        show(false);
    }

    public void hide() {
        show(false);
    }

    public void hideImmediately() {
        show(true);
    }

    public void showImmediately() {
        show(true);
    }

    public boolean isLoggingEnabled() {
        return this.debug;
    }

    public void setLoggingEnabled(boolean z) {
        this.debug = z;
    }

    public boolean isGesturesEnabled() {
        return this.gesturesEnabled;
    }

    public void setGesturesEnabled(boolean z) {
        this.gesturesEnabled = z;
    }

    public TimeInterpolator getInterpolator() {
        return this.interpolator;
    }

    public void setInterpolator(TimeInterpolator timeInterpolator) {
        ValueAnimator valueAnimator2 = this.valueAnimator;
        this.interpolator = timeInterpolator;
        valueAnimator2.setInterpolator(timeInterpolator);
    }

    public int getStartGravity() {
        return this.startGravity;
    }

    public boolean isHideKeyboardWhenDisplayed() {
        return this.hideKeyboard;
    }

    public void setHideKeyboardWhenDisplayed(boolean z) {
        this.hideKeyboard = z;
    }

    public void toggle() {
        if (isVisible()) {
            hide();
        } else {
            show();
        }
    }

    public void toggleImmediately() {
        if (isVisible()) {
            hideImmediately();
        } else {
            showImmediately();
        }
    }

    public Bundle onSaveInstanceState(@Nullable Bundle bundle) {
        if (bundle == null) {
            bundle = Bundle.EMPTY;
        }
        bundle.putInt(KEY_START_GRAVITY, this.startGravity);
        bundle.putBoolean(KEY_DEBUG, this.debug);
        bundle.putFloat(KEY_TOUCHABLE_AREA, this.touchableArea / this.density);
        bundle.putParcelable(KEY_STATE, this.currentState);
        bundle.putInt(KEY_AUTO_SLIDE_DURATION, this.autoSlideDuration);
        bundle.putBoolean(KEY_HIDE_SOFT_INPUT, this.hideKeyboard);
        return bundle;
    }

    private void endAnimation() {
        if (this.valueAnimator.getValues() != null) {
            this.valueAnimator.end();
        }
    }

    private void hide(boolean z) {
        endAnimation();
        int i = this.startGravity;
        if (i != 48) {
            if (i != 80) {
                if (i != 8388611) {
                    if (i != 8388613) {
                        return;
                    }
                    if (!z) {
                        this.slideAnimationTo = (float) this.sliderView.getWidth();
                        this.valueAnimator.setFloatValues(this.sliderView.getTranslationX(), this.slideAnimationTo);
                        this.valueAnimator.start();
                    } else if (this.sliderView.getWidth() > 0) {
                        notifyVisibilityChanged(0);
                    } else {
                        this.startState = State.HIDDEN;
                    }
                } else if (!z) {
                    this.slideAnimationTo = (float) this.sliderView.getWidth();
                    this.valueAnimator.setFloatValues(this.sliderView.getTranslationX(), this.slideAnimationTo);
                    this.valueAnimator.start();
                } else if (this.sliderView.getWidth() > 0) {
                    notifyVisibilityChanged(0);
                } else {
                    this.startState = State.HIDDEN;
                }
            } else if (!z) {
                this.slideAnimationTo = (float) this.sliderView.getHeight();
                this.valueAnimator.setFloatValues(this.sliderView.getTranslationY(), this.slideAnimationTo);
                this.valueAnimator.start();
            } else if (this.sliderView.getHeight() > 0) {
                notifyVisibilityChanged(0);
            } else {
                this.startState = State.HIDDEN;
            }
        } else if (z && this.sliderView.getHeight() <= 0) {
            this.startState = State.HIDDEN;
        }
    }

    private void show(boolean z) {
        endAnimation();
        int i = this.startGravity;
        if (i != 48) {
            if (i != 80) {
                if (i != 8388611) {
                    if (i != 8388613) {
                        return;
                    }
                } else if (!z) {
                    this.slideAnimationTo = 0.0f;
                    this.valueAnimator.setFloatValues(this.sliderView.getTranslationX(), this.slideAnimationTo);
                    this.valueAnimator.start();
                } else if (this.sliderView.getWidth() > 0) {
                    this.sliderView.setTranslationX(0.0f);
                    this.sliderView.setVisibility(0);
                    notifyVisibilityChanged(0);
                } else {
                    this.startState = State.SHOWED;
                }
                if (!z) {
                    this.slideAnimationTo = 0.0f;
                    this.valueAnimator.setFloatValues(this.sliderView.getTranslationX() * 2.0f, this.slideAnimationTo);
                    this.valueAnimator.start();
                    return;
                } else if (this.sliderView.getWidth() > 0) {
                    this.sliderView.setTranslationX(0.0f);
                    this.sliderView.setVisibility(0);
                    notifyVisibilityChanged(0);
                    return;
                } else {
                    this.startState = State.SHOWED;
                    return;
                }
            }
        } else if (!z) {
            this.slideAnimationTo = 0.0f;
            this.valueAnimator.setFloatValues(this.sliderView.getTranslationY(), this.slideAnimationTo);
            this.valueAnimator.start();
        } else if (this.sliderView.getHeight() > 0) {
            this.sliderView.setTranslationY(0.0f);
            this.sliderView.setVisibility(0);
            notifyVisibilityChanged(0);
        } else {
            this.startState = State.SHOWED;
        }
        if (!z) {
            this.slideAnimationTo = 0.0f;
            this.valueAnimator.setFloatValues(this.sliderView.getTranslationY(), this.slideAnimationTo);
            this.valueAnimator.start();
        } else if (this.sliderView.getHeight() > 0) {
            this.sliderView.setTranslationY(0.0f);
            this.sliderView.setVisibility(0);
            notifyVisibilityChanged(0);
        } else {
            this.startState = State.SHOWED;
        }
    }

    private void createAnimation() {
        this.valueAnimator = ValueAnimator.ofFloat(new float[0]);
        this.valueAnimator.setDuration((long) this.autoSlideDuration);
        this.valueAnimator.setInterpolator(this.interpolator);
        this.valueAnimator.addUpdateListener(this);
        this.valueAnimator.addListener(this);
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        this.sliderView = AllNotificationService.chatHeadService.viewCache.get(AllNotificationService.chatHeadService.stringDefaultChatHeadManager.getChatHeads().get(AllNotificationService.chatHeadService.stringDefaultChatHeadManager.getActiveArrangement().getHeroIndex().intValue()).getKey()).findViewById(this.layout);
        if (!this.gesturesEnabled || isAnimationRunning()) {
            return false;
        }
        int i = this.startGravity;
        if (i == 48) {
            return onTouchUpToDown(motionEvent);
        }
        if (i == 80) {
            return onTouchDownToUp(motionEvent);
        }
        if (i == 8388611) {
            return onTouchStartToEnd(motionEvent);
        }
        if (i == 8388613) {
            return onTouchEndToStart(motionEvent);
        }
        e("onTouchListener", "(onTouch)", "You are using not supportable gravity");
        return false;
    }

    private boolean onTouchEndToStart(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - ((float) getEnd());
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.viewWidth = (float) this.sliderView.getWidth();
                this.startPositionX = motionEvent.getRawX();
                this.viewStartPositionX = this.sliderView.getTranslationX();
                if (this.touchableArea < rawX) {
                    this.canSlide = false;
                    break;
                }
                break;
            case 1:
                float translationX = this.sliderView.getTranslationX();
                if (translationX != this.viewStartPositionX) {
                    boolean z = this.maxSlidePosition > motionEvent.getRawX();
                    if (this.sliderView.getTranslationX() <= ((float) (this.sliderView.getWidth() / 5)) || z) {
                        this.slideAnimationTo = 0.0f;
                    } else {
                        this.slideAnimationTo = (float) this.sliderView.getWidth();
                    }
                    this.valueAnimator.setFloatValues(translationX, this.slideAnimationTo);
                    this.valueAnimator.start();
                    this.canSlide = true;
                    this.maxSlidePosition = 0.0f;
                    break;
                } else {
                    return false;
                }
            case 2:
                float rawX2 = this.viewStartPositionX + (motionEvent.getRawX() - this.startPositionX);
                float width = (100.0f * rawX2) / ((float) this.sliderView.getWidth());
                if (rawX2 > 0.0f && this.canSlide) {
                    notifyPercentChanged(width);
                    this.sliderView.setTranslationX(rawX2);
                }
                if (motionEvent.getRawX() > this.maxSlidePosition) {
                    this.maxSlidePosition = motionEvent.getRawX();
                    break;
                }
                break;
        }
        return true;
    }

    private boolean onTouchStartToEnd(MotionEvent motionEvent) {
        float end = ((float) getEnd()) - motionEvent.getRawX();
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.maxSlidePosition = this.viewWidth;
                this.viewWidth = (float) this.sliderView.getWidth();
                this.startPositionX = motionEvent.getRawX();
                this.viewStartPositionX = this.sliderView.getTranslationX();
                if (this.touchableArea < end) {
                    this.canSlide = false;
                    break;
                }
                break;
            case 1:
                float f = -this.sliderView.getTranslationX();
                if (f != this.viewStartPositionX) {
                    boolean z = this.maxSlidePosition < motionEvent.getRawX();
                    if (this.sliderView.getTranslationX() >= ((float) ((-this.sliderView.getHeight()) / 5)) || z) {
                        this.slideAnimationTo = 0.0f;
                    } else {
                        this.slideAnimationTo = (float) this.sliderView.getWidth();
                    }
                    this.valueAnimator.setFloatValues(f, this.slideAnimationTo);
                    this.valueAnimator.start();
                    this.canSlide = true;
                    this.maxSlidePosition = 0.0f;
                    break;
                } else {
                    return false;
                }
            case 2:
                float rawX = this.viewStartPositionX + (motionEvent.getRawX() - this.startPositionX);
                float f2 = (100.0f * rawX) / ((float) (-this.sliderView.getWidth()));
                if (rawX < 0.0f && this.canSlide) {
                    notifyPercentChanged(f2);
                    this.sliderView.setTranslationX(rawX);
                }
                if (motionEvent.getRawX() < this.maxSlidePosition) {
                    this.maxSlidePosition = motionEvent.getRawX();
                    break;
                }
                break;
        }
        return true;
    }

    private boolean onTouchDownToUp(MotionEvent motionEvent) {
        float rawY = motionEvent.getRawY() - ((float) this.sliderView.getTop());
        boolean z = false;
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.viewHeight = (float) this.sliderView.getHeight();
                this.startPositionY = motionEvent.getRawY();
                this.viewStartPositionY = this.sliderView.getTranslationY();
                if (this.touchableArea >= rawY) {
                    z = true;
                }
                this.canSlide = z;
                break;
            case 1:
                float translationY = this.sliderView.getTranslationY();
                if (translationY != this.viewStartPositionY) {
                    boolean z2 = this.maxSlidePosition > motionEvent.getRawY();
                    if (this.sliderView.getTranslationY() <= ((float) (this.sliderView.getHeight() / 5)) || z2) {
                        this.slideAnimationTo = 0.0f;
                    } else {
                        this.slideAnimationTo = (float) this.sliderView.getHeight();
                    }
                    this.valueAnimator.setFloatValues(translationY, this.slideAnimationTo);
                    this.valueAnimator.start();
                    this.canSlide = true;
                    this.maxSlidePosition = 0.0f;
                    break;
                } else {
                    return false;
                }
            case 2:
                float rawY2 = this.viewStartPositionY + (motionEvent.getRawY() - this.startPositionY);
                float height = (100.0f * rawY2) / ((float) this.sliderView.getHeight());
                if (rawY2 > 0.0f && this.canSlide) {
                    notifyPercentChanged(height);
                    this.sliderView.setTranslationY(rawY2);
                }
                if (motionEvent.getRawY() > this.maxSlidePosition) {
                    this.maxSlidePosition = motionEvent.getRawY();
                    break;
                }
                break;
        }
        return true;
    }

    private boolean onTouchUpToDown(MotionEvent motionEvent) {
        float rawY = motionEvent.getRawY() - ((float) this.sliderView.getBottom());
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.viewHeight = (float) this.sliderView.getHeight();
                this.startPositionY = motionEvent.getRawY();
                this.viewStartPositionY = this.sliderView.getTranslationY();
                this.maxSlidePosition = this.viewHeight;
                if (this.touchableArea < rawY) {
                    this.canSlide = false;
                    break;
                }
                break;
            case 1:
                float f = -this.sliderView.getTranslationY();
                if (f != this.viewStartPositionY) {
                    boolean z = this.maxSlidePosition < motionEvent.getRawY();
                    if (this.sliderView.getTranslationY() >= ((float) ((-this.sliderView.getHeight()) / 3)) || z) {
                        this.slideAnimationTo = 0.0f;
                    } else {
                        this.slideAnimationTo = (float) (this.sliderView.getHeight() + this.sliderView.getTop());
                    }
                    this.valueAnimator.setFloatValues(f, this.slideAnimationTo);
                    this.valueAnimator.start();
                    this.canSlide = true;
                    this.maxSlidePosition = 0.0f;
                    break;
                } else {
                    return false;
                }
            case 2:
                float rawY2 = this.viewStartPositionY + (motionEvent.getRawY() - this.startPositionY) + 125.0f;
                float f2 = (100.0f * rawY2) / ((float) (-this.sliderView.getHeight()));
                if (rawY2 < 0.0f && this.canSlide) {
                    notifyPercentChanged(f2);
                    this.sliderView.setTranslationY(rawY2);
                }
                if (motionEvent.getRawY() < this.maxSlidePosition) {
                    this.maxSlidePosition = motionEvent.getRawY();
                    break;
                }
                break;
        }
        return true;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
        float floatValue = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
        int i = this.startGravity;
        if (i == 48) {
            onAnimationUpdateUpToDown(floatValue);
        } else if (i == 80) {
            onAnimationUpdateDownToUp(floatValue);
        } else if (i == 8388611) {
            onAnimationUpdateStartToEnd(floatValue);
        } else if (i == 8388613) {
            onAnimationUpdateEndToStart(floatValue);
        }
    }

    private void onAnimationUpdateUpToDown(float f) {
        this.sliderView.setTranslationY(-f);
        float top = ((((float) this.sliderView.getTop()) - this.sliderView.getY()) * 100.0f) / this.viewHeight;
        if (top >= 60.0f) {
            this.sliderView.setTranslationX(0.0f);
        }
        notifyPercentChanged(top);
    }

    private void onAnimationUpdateDownToUp(float f) {
        this.sliderView.setTranslationY(f);
        float y = ((this.sliderView.getY() - ((float) this.sliderView.getTop())) * 100.0f) / this.viewHeight;
        if (y >= 60.0f) {
            this.sliderView.setTranslationX(0.0f);
        }
        notifyPercentChanged(y);
    }

    private void onAnimationUpdateStartToEnd(float f) {
        this.sliderView.setTranslationX(-f);
        float x = ((this.sliderView.getX() - ((float) getStart())) * 100.0f) / (-this.viewWidth);
        if (x >= 60.0f) {
            this.sliderView.setTranslationX(0.0f);
        }
        notifyPercentChanged(x);
    }

    private void onAnimationUpdateEndToStart(float f) {
        this.sliderView.setTranslationX(f);
        notifyPercentChanged(((this.sliderView.getX() - ((float) getStart())) * 100.0f) / this.viewWidth);
    }

    private int getStart() {
        if (this.isRTL) {
            return this.sliderView.getRight();
        }
        return this.sliderView.getLeft();
    }

    private int getEnd() {
        if (this.isRTL) {
            return this.sliderView.getLeft();
        }
        return this.sliderView.getRight();
    }

    private void notifyPercentChanged(float f) {
        if (f > 100.0f) {
            f = 100.0f;
        }
        if (f < 0.0f) {
            f = 0.0f;
        }
        if (this.slideAnimationTo == 0.0f && this.hideKeyboard) {
            hideSoftInput();
        }
        List<Listener> list = this.listeners;
        if (!(list == null || list.isEmpty())) {
            for (int i = 0; i < this.listeners.size(); i++) {
                Listener listener = this.listeners.get(i);
                if (listener != null) {
                    listener.onSlide(f);
                    d("Listener(" + i + ")", "(onSlide)", "value = " + f);
                } else {
                    e("Listener(" + i + ")", "(onSlide)", "Listener is null, skip notification...");
                }
            }
        }
    }

    private void notifyVisibilityChanged(int i) {
        List<Listener> list = this.listeners;
        if (list != null && !list.isEmpty()) {
            for (int i2 = 0; i2 < this.listeners.size(); i2++) {
                Listener listener = this.listeners.get(i2);
                if (listener != null) {
                    listener.onVisibilityChanged(i);
                    String str = "Listener(" + i2 + ")";
                    StringBuilder sb = new StringBuilder();
                    sb.append("value = ");
                    sb.append((i == 0 || i == 0) ? "VISIBLE" : Integer.valueOf(i));
                    d(str, "(onVisibilityChanged)", sb.toString());
                } else {
                    e("Listener(" + i2 + ")", "(onVisibilityChanged)", "Listener is null, skip  notify for him...");
                }
            }
        }
        if (i == 0) {
            this.currentState = State.SHOWED;
        }
    }

    public final void onAnimationStart(Animator animator) {
        if (this.sliderView.getVisibility() != 0) {
            this.sliderView.setVisibility(0);
            notifyVisibilityChanged(0);
        }
    }

    public final void onAnimationEnd(Animator animator) {
        if (this.slideAnimationTo != 0.0f && this.sliderView.getVisibility() != 0) {
            notifyVisibilityChanged(0);
        }
    }

    private void e(String str, String str2, String str3) {
        if (this.debug) {
            Log.e(TAG, String.format("%1$-15s %2$-23s %3$s", str, str2, str3));
        }
    }

    public enum State implements Parcelable, Serializable {
        HIDDEN,
        SHOWED;

        public static Creator<State> CREATOR;

        static {
            CREATOR = null;
            CREATOR = new Creator<State>() {
                /* class com.chatapp.wabubbleforchat.chatheads.ui.CustomSlide.State.AnonymousClass1 */

                @Override // android.os.Parcelable.Creator
                public State createFromParcel(Parcel parcel) {
                    return State.values()[parcel.readInt()];
                }

                @Override // android.os.Parcelable.Creator
                public State[] newArray(int i) {
                    return new State[i];
                }
            };
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(ordinal());
        }
    }

    public interface Listener {
        void onSlide(float f);

        void onVisibilityChanged(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface StartVector {
    }

    public static class ListenerAdapter implements Listener {
        @Override
        public void onSlide(float f) {
        }

        @Override
        public void onVisibilityChanged(int i) {
        }
    }

    public static final class Builder {
        public int autoSlideDuration = 300;
        public boolean debug = false;
        public float density;
        public boolean gesturesEnabled = true;
        public boolean hideKeyboard = false;
        public TimeInterpolator interpolator = new DecelerateInterpolator();
        public boolean isRTL;
        public int layout;
        public List<Listener> listeners = new ArrayList();
        public View sliderView;
        public int startGravity = 80;
        public State startState = State.HIDDEN;
        public float touchableArea;

        public Builder(@NonNull View view, String str, int i) {
            this.sliderView = view;
            this.layout = i;
            CustomSlide.name = str;
            this.density = view.getResources().getDisplayMetrics().density;
            this.touchableArea = this.density * 300.0f;
        }

        public Builder withStartState(@NonNull State state) {
            this.startState = state;
            return this;
        }

        public Builder withStartGravity(int i) {
            this.startGravity = i;
            return this;
        }

        public Builder withListeners(@NonNull List<Listener> list) {
            this.listeners = list;
            return this;
        }

        public Builder withListeners(@NonNull Listener... listenerArr) {
            ArrayList arrayList = new ArrayList();
            Collections.addAll(arrayList, listenerArr);
            return withListeners(arrayList);
        }

        public Builder withLoggingEnabled(boolean z) {
            this.debug = z;
            return this;
        }

        public Builder withAutoSlideDuration(int i) {
            this.autoSlideDuration = i;
            return this;
        }

        public Builder withTouchableArea(float f) {
            this.touchableArea = f * this.density;
            return this;
        }

        public Builder withGesturesEnabled(boolean z) {
            this.gesturesEnabled = z;
            return this;
        }

        public Builder withHideSoftInputWhenDisplayed(boolean z) {
            this.hideKeyboard = z;
            return this;
        }

        public Builder withInterpolator(TimeInterpolator timeInterpolator) {
            this.interpolator = timeInterpolator;
            return this;
        }

        public Builder withSavedState(@Nullable Bundle bundle) {
            restoreParams(bundle);
            return this;
        }

        public CustomSlide build() {
            return new CustomSlide(this);
        }

        private void restoreParams(@Nullable Bundle bundle) {
            if (bundle != null) {
                if (bundle.getParcelable(CustomSlide.KEY_STATE) != null) {
                    this.startState = (State) bundle.getParcelable(CustomSlide.KEY_STATE);
                }
                this.startGravity = bundle.getInt(CustomSlide.KEY_START_GRAVITY, this.startGravity);
                this.debug = bundle.getBoolean(CustomSlide.KEY_DEBUG, this.debug);
                this.touchableArea = bundle.getFloat(CustomSlide.KEY_TOUCHABLE_AREA, this.touchableArea) * this.density;
                this.autoSlideDuration = bundle.getInt(CustomSlide.KEY_AUTO_SLIDE_DURATION, this.autoSlideDuration);
                this.hideKeyboard = bundle.getBoolean(CustomSlide.KEY_HIDE_SOFT_INPUT, this.hideKeyboard);
            }
        }
    }
}
