package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.whatscan.toolkit.forwa.WBubble.chatheads.ChatHeadUtils;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.facebook.rebound.SpringListener;

import java.io.Serializable;
import java.util.List;

public class MinimizedArrangement<T extends Serializable> extends ChatHeadArrangement {
    public static final String BUNDLE_HERO_INDEX_KEY = "hero_index";
    public static final String BUNDLE_HERO_RELATIVE_X_KEY = "hero_relative_x";
    public static final String BUNDLE_HERO_RELATIVE_Y_KEY = "hero_relative_y";
    public static float currentPosX = 0.0f;
    public static float currentPosY = 120.0f;
    static OnCustomEventListener a = null;
    private static int MAX_VELOCITY_FOR_IDLING = 0;
    private static int MIN_VELOCITY_TO_POSITION_BACK = 0;
    private float DELTA = 0.0f;
    private float currentDelta = 0.0f;
    private Bundle extras;
    private boolean hasActivated = false;
    private ChatHead hero;
    private SpringChain horizontalSpringChain;
    private int idleStateX = Integer.MIN_VALUE;
    private int idleStateY = Integer.MIN_VALUE;
    private boolean isTransitioning;
    private ChatHeadManager<T> manager;
    private int maxHeight;
    private int maxWidth;

    private SpringListener horizontalHeroListener = new SimpleSpringListener() {
        @Override
        public void onSpringUpdate(Spring spring) {
            MinimizedArrangement minimizedArrangement = MinimizedArrangement.this;
            minimizedArrangement.currentDelta = (float) ((((double) minimizedArrangement.DELTA) * (((double) (MinimizedArrangement.this.maxWidth / 2)) - spring.getCurrentValue())) / ((double) (MinimizedArrangement.this.maxWidth / 2)));
            if (MinimizedArrangement.this.horizontalSpringChain != null) {
                MinimizedArrangement.this.horizontalSpringChain.getControlSpring().setCurrentValue(spring.getCurrentValue());
            }
        }

        @Override
        public void onSpringAtRest(Spring spring) {
            super.onSpringAtRest(spring);
            if (MinimizedArrangement.this.isTransitioning) {
                MinimizedArrangement.this.isTransitioning = false;
            }
        }
    };
    private double relativeXPosition = -1.0d;
    private double relativeYPosition = -1.0d;
    private SpringChain verticalSpringChain;
    private SpringListener verticalHeroListener = new SimpleSpringListener() {
        @Override
        public void onSpringUpdate(Spring spring) {
            if (MinimizedArrangement.this.verticalSpringChain != null) {
                MinimizedArrangement.this.verticalSpringChain.getControlSpring().setCurrentValue(spring.getCurrentValue());
            }
        }

        @Override
        public void onSpringAtRest(Spring spring) {
            super.onSpringAtRest(spring);
            if (MinimizedArrangement.this.isTransitioning) {
                MinimizedArrangement.this.isTransitioning = false;
            }
        }
    };

    public MinimizedArrangement(ChatHeadManager chatHeadManager) {
        this.manager = chatHeadManager;
        this.DELTA = (float) ChatHeadUtils.dpToPx(this.manager.getContext(), 5);
    }

    public static void setCustomEventListener(OnCustomEventListener onCustomEventListener) {
        a = onCustomEventListener;
    }

    @Override
    public boolean canDrag(ChatHead chatHead) {
        return true;
    }

    @Override
    public void onConfigChanged(ChatHeadConfig chatHeadConfig) {
    }

    @Override
    public void onReloadFragment(ChatHead chatHead) {
    }

    @Override
    public void selectChatHead(ChatHead chatHead) {
    }

    @Override
    public boolean shouldShowCloseButton(ChatHead chatHead) {
        return true;
    }

    public void setIdleStateX(int i) {
        this.idleStateX = i;
    }

    public void setIdleStateY(int i) {
        this.idleStateY = i;
    }

    public Point getIdleStatePosition() {
        return new Point(this.idleStateX, this.idleStateY);
    }

    @Override
    public void setContainer(ChatHeadManager chatHeadManager) {
        this.manager = chatHeadManager;
    }

    @Override
    public void onActivate(ChatHeadManager chatHeadManager, Bundle bundle, int i, int i2, boolean z) {
        int i3;
        this.isTransitioning = true;
        if (!(this.horizontalSpringChain == null && this.verticalSpringChain == null)) {
            onDeactivate(i, i2);
        }
        MIN_VELOCITY_TO_POSITION_BACK = ChatHeadUtils.dpToPx(chatHeadManager.getDisplayMetrics(), 600);
        MAX_VELOCITY_FOR_IDLING = ChatHeadUtils.dpToPx(chatHeadManager.getDisplayMetrics(), 1);
        this.extras = bundle;
        if (bundle != null) {
            i3 = bundle.getInt("hero_index", -1);
            this.relativeXPosition = bundle.getDouble(BUNDLE_HERO_RELATIVE_X_KEY, -1.0d);
            this.relativeYPosition = bundle.getDouble(BUNDLE_HERO_RELATIVE_Y_KEY, -1.0d);
        } else {
            i3 = 0;
        }
        List<ChatHead<T>> chatHeads = chatHeadManager.getChatHeads();
        if (i3 < 0 || i3 > chatHeads.size() - 1) {
            i3 = 0;
        }
        if (i3 < chatHeads.size()) {
            this.hero = chatHeads.get(i3);
            this.hero.setHero(true);
            this.horizontalSpringChain = SpringChain.create();
            this.verticalSpringChain = SpringChain.create();
            for (int i4 = 0; i4 < chatHeads.size(); i4++) {
                final ChatHead<T> chatHead = chatHeads.get(i4);
                if (chatHead != this.hero) {
                    chatHead.setHero(false);
                    this.horizontalSpringChain.addSpring(new SimpleSpringListener() {
                        /* class com.chatapp.wabubbleforchat.chatheads.ui.MinimizedArrangement.AnonymousClass3 */

                        @Override
                        // com.facebook.rebound.SpringListener, com.facebook.rebound.SimpleSpringListener
                        public void onSpringUpdate(Spring spring) {
                            chatHead.getHorizontalSpring().setCurrentValue(spring.getCurrentValue() + ((double) (((float) ((MinimizedArrangement.this.horizontalSpringChain.getAllSprings().indexOf(spring) - MinimizedArrangement.this.horizontalSpringChain.getAllSprings().size()) + 1)) * MinimizedArrangement.this.currentDelta)));
                        }
                    });
                    this.horizontalSpringChain.getAllSprings().get(this.horizontalSpringChain.getAllSprings().size() - 1).setCurrentValue(chatHead.getHorizontalSpring().getCurrentValue());
                    this.verticalSpringChain.addSpring(new SimpleSpringListener() {
                        /* class com.chatapp.wabubbleforchat.chatheads.ui.MinimizedArrangement.AnonymousClass4 */

                        @Override
                        // com.facebook.rebound.SpringListener, com.facebook.rebound.SimpleSpringListener
                        public void onSpringUpdate(Spring spring) {
                            chatHead.getVerticalSpring().setCurrentValue(spring.getCurrentValue());
                        }
                    });
                    this.verticalSpringChain.getAllSprings().get(this.verticalSpringChain.getAllSprings().size() - 1).setCurrentValue(chatHead.getVerticalSpring().getCurrentValue());
                    this.manager.getChatHeadContainer().bringToFront(chatHead);
                }
            }
            this.idleStateX = chatHeadManager.getConfig().getInitialPosition().x;
            this.idleStateY = chatHeadManager.getConfig().getInitialPosition().y;
            this.idleStateX = stickToEdgeX(this.idleStateX, i, this.hero);
            ChatHead chatHead2 = this.hero;
            if (!(chatHead2 == null || chatHead2.getHorizontalSpring() == null || this.hero.getVerticalSpring() == null)) {
                this.manager.getChatHeadContainer().bringToFront(this.hero);
                this.horizontalSpringChain.addSpring(new SimpleSpringListener() {
                });
                this.verticalSpringChain.addSpring(new SimpleSpringListener() {
                });
                this.horizontalSpringChain.setControlSpringIndex(chatHeads.size() - 1);
                this.verticalSpringChain.setControlSpringIndex(chatHeads.size() - 1);
                this.hero.getHorizontalSpring().addListener(this.horizontalHeroListener);
                this.hero.getVerticalSpring().addListener(this.verticalHeroListener);
                this.hero.getHorizontalSpring().setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                if (this.hero.getHorizontalSpring().getCurrentValue() == ((double) this.idleStateX)) {
                    this.hero.getHorizontalSpring().setCurrentValue((double) (this.idleStateX - 1), true);
                }
                if (z) {
                    this.hero.getHorizontalSpring().setEndValue((double) this.idleStateX);
                } else {
                    this.hero.getHorizontalSpring().setCurrentValue((double) this.idleStateX, true);
                }
                this.hero.getVerticalSpring().setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                if (this.hero.getVerticalSpring().getCurrentValue() == ((double) this.idleStateY)) {
                    this.hero.getVerticalSpring().setCurrentValue((double) (this.idleStateY - 1), true);
                }
                if (z) {
                    this.hero.getVerticalSpring().setEndValue((double) this.idleStateY);
                } else {
                    this.hero.getVerticalSpring().setCurrentValue((double) this.idleStateY, true);
                }
            }
            this.maxWidth = i;
            this.maxHeight = i2;
            chatHeadManager.getCloseButton().setEnabled(true);
        }
        this.hasActivated = true;
    }

    private int stickToEdgeX(int i, int i2, ChatHead chatHead) {
        if (i2 - i < i) {
            OnCustomEventListener onCustomEventListener = a;
            if (onCustomEventListener != null) {
                onCustomEventListener.onEvent(false);
            }
            return i2 - chatHead.getMeasuredWidth();
        }
        OnCustomEventListener onCustomEventListener2 = a;
        if (onCustomEventListener2 != null) {
            onCustomEventListener2.onEvent(true);
        }
        return 0;
    }

    @Override
    public void onChatHeadAdded(ChatHead chatHead, boolean z) {
        ChatHead chatHead2 = this.hero;
        if (!(chatHead2 == null || chatHead2.getHorizontalSpring() == null || this.hero.getVerticalSpring() == null)) {
            chatHead.getHorizontalSpring().setCurrentValue(this.hero.getHorizontalSpring().getCurrentValue() - ((double) this.currentDelta));
            chatHead.getVerticalSpring().setCurrentValue(this.hero.getVerticalSpring().getCurrentValue());
        }
        onActivate(this.manager, getRetainBundle(), this.maxWidth, this.maxHeight, z);
    }

    @Override
    public void onChatHeadRemoved(ChatHead chatHead) {
        ChatHeadManager<T> chatHeadManager = this.manager;
        chatHeadManager.detachView(chatHead, chatHeadManager.getArrowLayout());
        ChatHeadManager<T> chatHeadManager2 = this.manager;
        chatHeadManager2.removeView(chatHead, chatHeadManager2.getArrowLayout());
        if (chatHead == this.hero) {
            this.hero = null;
        }
        onActivate(this.manager, null, this.maxWidth, this.maxHeight, true);
    }

    @Override
    public void onCapture(ChatHeadManager chatHeadManager, ChatHead chatHead) {
        chatHeadManager.removeAllChatHeads(true);
    }

    @Override
    public void onDeactivate(int i, int i2) {
        this.hasActivated = false;
        ChatHead chatHead = this.hero;
        if (chatHead != null) {
            chatHead.getHorizontalSpring().removeListener(this.horizontalHeroListener);
            this.hero.getVerticalSpring().removeListener(this.verticalHeroListener);
        }
        SpringChain springChain = this.horizontalSpringChain;
        if (springChain != null) {
            for (Spring spring : springChain.getAllSprings()) {
                spring.destroy();
            }
        }
        SpringChain springChain2 = this.verticalSpringChain;
        if (springChain2 != null) {
            for (Spring spring2 : springChain2.getAllSprings()) {
                spring2.destroy();
            }
        }
        this.horizontalSpringChain = null;
        this.verticalSpringChain = null;
    }

    @Override
    public boolean handleTouchUp(ChatHead chatHead, int i, int i2, Spring spring, Spring spring2, boolean z) {
        settleToClosest(chatHead, i, i2);
        if (z || this.manager.onItemSelected(chatHead)) {
            return true;
        }
        deactivate();
        return false;
    }

    private void settleToClosest(ChatHead chatHead, int i, int i2) {
        int currentValue;
        Spring horizontalSpring = chatHead.getHorizontalSpring();
        Spring verticalSpring = chatHead.getVerticalSpring();
        if (chatHead.getState() == ChatHead.State.FREE) {
            if (Math.abs(i) < ChatHeadUtils.dpToPx(this.manager.getDisplayMetrics(), 50)) {
                i = horizontalSpring.getCurrentValue() < ((double) this.maxWidth) - horizontalSpring.getCurrentValue() ? -1 : 1;
            }
            if (i < 0) {
                int i3 = (int) ((-horizontalSpring.getCurrentValue()) * SpringConfigsHolder.DRAGGING.friction);
                if (i > i3) {
                    i = i3;
                }
            } else if (i > 0 && (currentValue = (int) (((((double) this.maxWidth) - horizontalSpring.getCurrentValue()) - ((double) this.manager.getConfig().getHeadWidth())) * SpringConfigsHolder.DRAGGING.friction)) > i) {
                i = currentValue;
            }
        }
        if (Math.abs(i) <= 1) {
            i = i < 0 ? -1 : 1;
        }
        if (i2 == 0) {
            i2 = 1;
        }
        horizontalSpring.setVelocity((double) i);
        verticalSpring.setVelocity((double) i2);
    }

    private void deactivate() {
        this.manager.setArrangement(MaximizedArrangement.class, getBundleWithHero());
    }

    @NonNull
    private Bundle getBundleWithHero() {
        return getBundle(getHeroIndex().intValue());
    }

    private Bundle getBundle(int i) {
        ChatHead chatHead = this.hero;
        if (!(chatHead == null || chatHead.getHorizontalSpring() == null)) {
            this.relativeXPosition = (this.hero.getHorizontalSpring().getCurrentValue() * 1.0d) / ((double) this.maxWidth);
            this.relativeYPosition = (this.hero.getVerticalSpring().getCurrentValue() * 1.0d) / ((double) this.maxHeight);
        }
        Bundle bundle = this.extras;
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt("hero_index", i);
        bundle.putDouble(BUNDLE_HERO_RELATIVE_X_KEY, this.relativeXPosition);
        bundle.putDouble(BUNDLE_HERO_RELATIVE_Y_KEY, this.relativeYPosition);
        return bundle;
    }

    @Override
    public Integer getHeroIndex() {
        return getHeroIndex(this.hero);
    }

    private Integer getHeroIndex(ChatHead chatHead) {
        int i = 0;
        int i2 = 0;
        for (ChatHead<T> chatHead2 : this.manager.getChatHeads()) {
            if (chatHead == chatHead2) {
                i = i2;
            }
            i2++;
        }
        return Integer.valueOf(i);
    }

    @Override
    public Bundle getRetainBundle() {
        return getBundleWithHero();
    }

    @Override
    public void removeOldestChatHead() {
        for (ChatHead<T> chatHead : this.manager.getChatHeads()) {
            if (!chatHead.isSticky()) {
                this.manager.removeChatHead(chatHead.getKey(), false);
                return;
            }
        }
    }

    @Override
    public void onSpringUpdate(ChatHead chatHead, boolean z, int i, int i2, Spring spring, Spring spring2, Spring spring3, int i3) {
        double d;
        double velocity = spring2.getVelocity();
        double velocity2 = spring3.getVelocity();
        if (z || Math.abs(i3) >= MIN_VELOCITY_TO_POSITION_BACK || chatHead != this.hero) {
            d = velocity2;
        } else {
            if (Math.abs(i3) < MAX_VELOCITY_FOR_IDLING && chatHead.getState() == ChatHead.State.FREE && this.hasActivated) {
                setIdleStateX((int) spring2.getCurrentValue());
                setIdleStateY((int) spring3.getCurrentValue());
            }
            if (spring == spring2) {
                double currentValue = spring2.getCurrentValue();
                if (((double) this.manager.getConfig().getHeadWidth()) + currentValue > ((double) i) && spring2.getVelocity() > 0.0d) {
                    int headWidth = i - this.manager.getConfig().getHeadWidth();
                    spring2.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                    spring2.setEndValue((double) headWidth);
                } else if (currentValue < 0.0d && spring2.getVelocity() < 0.0d) {
                    spring2.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                    spring2.setEndValue(0.0d);
                }
            } else if (spring == spring3) {
                double currentValue2 = spring3.getCurrentValue();
                d = velocity2;
                if (((double) this.manager.getConfig().getHeadWidth()) + currentValue2 > ((double) i2) && spring3.getVelocity() > 0.0d) {
                    spring3.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                    spring3.setEndValue((double) (i2 - this.manager.getConfig().getHeadHeight()));
                } else if (currentValue2 < 0.0d && spring3.getVelocity() < 0.0d) {
                    spring3.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                    spring3.setEndValue(0.0d);
                }
            }
            d = velocity2;
        }
        if (!z && chatHead == this.hero) {
            int[] chatHeadCoordsForCloseButton = this.manager.getChatHeadCoordsForCloseButton(chatHead);
            if (this.manager.getDistanceCloseButtonFromHead(((float) spring2.getCurrentValue()) + ((float) (this.manager.getConfig().getHeadWidth() / 2)), ((float) spring3.getCurrentValue()) + ((float) (this.manager.getConfig().getHeadHeight() / 2))) < ((double) chatHead.CLOSE_ATTRACTION_THRESHOLD) && spring2.getSpringConfig() == SpringConfigsHolder.DRAGGING && spring3.getSpringConfig() == SpringConfigsHolder.DRAGGING) {
                spring2.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                spring3.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                chatHead.setState(ChatHead.State.CAPTURED);
            }
            if (chatHead.getState() == ChatHead.State.CAPTURED && spring2.getSpringConfig() != SpringConfigsHolder.CAPTURING) {
                spring2.setAtRest();
                spring3.setAtRest();
                spring2.setSpringConfig(SpringConfigsHolder.CAPTURING);
                spring3.setSpringConfig(SpringConfigsHolder.CAPTURING);
                spring2.setEndValue((double) chatHeadCoordsForCloseButton[0]);
                spring3.setEndValue((double) chatHeadCoordsForCloseButton[1]);
            }
            if (chatHead.getState() == ChatHead.State.FREE && velocity == 0.0d && d == 0.0d) {
                currentPosY = this.hero.getY();
                currentPosX = this.hero.getX();
                Log.e("currentPosX", currentPosX + " :::");
                Log.e("currentPosY", currentPosY + " :::");
                if (currentPosX > 0.0f) {
                    this.manager.getMaxWidth();
                }
            }
            if (chatHead.getState() == ChatHead.State.CAPTURED && spring3.isAtRest()) {
                this.manager.getCloseButton().disappear(false, true);
                this.manager.captureChatHeads(chatHead);
            }
            if (spring3.isAtRest() || this.isTransitioning) {
                this.manager.getCloseButton().disappear(true, true);
            } else {
                this.manager.getCloseButton().appear();
            }
        }
    }

    @Override
    public void bringToFront(ChatHead chatHead) {
        Bundle bundle = getBundle(getHeroIndex(chatHead).intValue());
        ChatHeadManager<T> chatHeadManager = this.manager;
        onActivate(chatHeadManager, bundle, chatHeadManager.getMaxWidth(), this.manager.getMaxHeight(), true);
    }

    public interface OnCustomEventListener {
        void onEvent(boolean z);
    }
}