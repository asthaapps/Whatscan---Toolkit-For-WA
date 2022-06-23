package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.appcompat.widget.AppCompatImageView;

import com.whatscan.toolkit.forwa.R;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

@TargetApi(11)
public class ChatHeadCloseButton extends AppCompatImageView {
    private static final float PERC_PARENT_HEIGHT = 0.05f;
    private static final float PERC_PARENT_WIDTH = 0.1f;
    private int centerX;
    private int centerY;
    private ChatHeadManager chatHeadManager;
    private boolean disappeared;
    private CloseButtonListener listener;
    private int mParentHeight;
    private int mParentWidth;
    private Spring scaleSpring;
    private Spring xSpring;
    private Spring ySpring;

    public ChatHeadCloseButton(Context context, ChatHeadManager chatHeadManager2, int i, int i2) {
        super(context);
        init(chatHeadManager2, i, i2);
    }

    public void setListener(CloseButtonListener closeButtonListener) {
        this.listener = closeButtonListener;
    }

    public boolean isDisappeared() {
        return this.disappeared;
    }

    private void init(final ChatHeadManager chatHeadManager2, int i, int i2) {
        this.chatHeadManager = chatHeadManager2;
        setImageResource(R.drawable.dismiss_big);
        SpringSystem create = SpringSystem.create();
        this.xSpring = create.createSpring();
        this.xSpring.addListener(new SimpleSpringListener() {
            /* class com.chatapp.wabubbleforchat.chatheads.ui.ChatHeadCloseButton.AnonymousClass1 */

            @Override
            // com.facebook.rebound.SpringListener, com.facebook.rebound.SimpleSpringListener
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                chatHeadManager2.getChatHeadContainer().setViewX(ChatHeadCloseButton.this, ChatHeadCloseButton.this.getXFromSpring(spring));
            }
        });
        this.ySpring = create.createSpring();
        this.ySpring.addListener(new SimpleSpringListener() {
            /* class com.chatapp.wabubbleforchat.chatheads.ui.ChatHeadCloseButton.AnonymousClass2 */

            @Override
            // com.facebook.rebound.SpringListener, com.facebook.rebound.SimpleSpringListener
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                chatHeadManager2.getChatHeadContainer().setViewY(ChatHeadCloseButton.this, ChatHeadCloseButton.this.getYFromSpring(spring));
            }
        });
        this.scaleSpring = create.createSpring();
        this.scaleSpring.addListener(new SimpleSpringListener() {
            /* class com.chatapp.wabubbleforchat.chatheads.ui.ChatHeadCloseButton.AnonymousClass3 */

            @Override
            // com.facebook.rebound.SpringListener, com.facebook.rebound.SimpleSpringListener
            public void onSpringUpdate(Spring spring) {
                float currentValue = (float) spring.getCurrentValue();
                ChatHeadCloseButton.this.setScaleX(currentValue);
                ChatHeadCloseButton.this.setScaleY(currentValue);
            }
        });
    }

    private int getYFromSpring(Spring spring) {
        return (this.centerY + ((int) spring.getCurrentValue())) - (getMeasuredHeight() / 2);
    }

    private int getXFromSpring(Spring spring) {
        return (this.centerX + ((int) spring.getCurrentValue())) - (getMeasuredWidth() / 2);
    }

    public void appear() {
        if (isEnabled()) {
            this.ySpring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
            this.xSpring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
            this.scaleSpring.setEndValue(0.800000011920929d);
            ViewParent parent = getParent();
            if (parent instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parent;
                if (viewGroup.indexOfChild(this) != viewGroup.getChildCount() - 1) {
                    bringToFront();
                }
            }
            this.disappeared = false;
        }
    }

    public void onCapture() {
        this.scaleSpring.setEndValue(1.0d);
    }

    public void onRelease() {
        this.scaleSpring.setEndValue(0.8d);
    }

    public void disappear(boolean z, boolean z2) {
        this.ySpring.setEndValue((double) ((this.mParentHeight - this.centerY) + this.chatHeadManager.getConfig().getCloseButtonHeight()));
        this.ySpring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
        this.xSpring.setEndValue(0.0d);
        this.ySpring.addListener(new SimpleSpringListener() {
            /* class com.chatapp.wabubbleforchat.chatheads.ui.ChatHeadCloseButton.AnonymousClass4 */

            @Override
            // com.facebook.rebound.SpringListener, com.facebook.rebound.SimpleSpringListener
            public void onSpringAtRest(Spring spring) {
                super.onSpringAtRest(spring);
                ChatHeadCloseButton.this.ySpring.removeListener(this);
            }
        });
        this.scaleSpring.setEndValue(0.10000000149011612d);
        if (!z2) {
            this.ySpring.setCurrentValue((double) this.mParentHeight, true);
            this.xSpring.setCurrentValue(0.0d, true);
        }
        this.disappeared = true;
        CloseButtonListener closeButtonListener = this.listener;
        if (closeButtonListener != null) {
            closeButtonListener.onCloseButtonDisappear();
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        disappear(true, false);
    }

    public void onParentHeightRefreshed() {
        this.mParentWidth = this.chatHeadManager.getMaxWidth();
        this.mParentHeight = this.chatHeadManager.getMaxHeight();
    }

    public void setCenter(int i, int i2) {
        if ((i == this.centerX && i2 == this.centerY) ? false : true) {
            this.centerX = i;
            this.centerY = i2;
            this.xSpring.setCurrentValue(0.0d, false);
            this.ySpring.setCurrentValue(0.0d, false);
        }
    }

    public void pointTo(float f, float f2) {
        if (isEnabled()) {
            double translationFromSpring = getTranslationFromSpring((double) f, PERC_PARENT_WIDTH, this.mParentWidth);
            double translationFromSpring2 = getTranslationFromSpring((double) f2, PERC_PARENT_HEIGHT, this.mParentHeight);
            if (!this.disappeared) {
                this.xSpring.setEndValue(translationFromSpring);
                this.ySpring.setEndValue(translationFromSpring2);
                CloseButtonListener closeButtonListener = this.listener;
                if (closeButtonListener != null) {
                    closeButtonListener.onCloseButtonAppear();
                }
            }
        }
    }

    private double getTranslationFromSpring(double d, float f, int i) {
        float f2 = f * ((float) i);
        return SpringUtil.mapValueFromRangeToRange(d, 0.0d, (double) i, (double) ((-f2) / 2.0f), (double) (f2 / 2.0f));
    }

    public boolean isAtRest() {
        return this.xSpring.isAtRest() && this.ySpring.isAtRest();
    }

    public int getEndValueX() {
        return getXFromSpring(this.xSpring);
    }

    public int getEndValueY() {
        return getYFromSpring(this.ySpring);
    }

    public interface CloseButtonListener {
        void onCloseButtonAppear();

        void onCloseButtonDisappear();
    }
}