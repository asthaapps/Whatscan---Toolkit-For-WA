package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import com.whatscan.toolkit.forwa.WBubble.chatheads.ChatHeadUtils;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@TargetApi(11)
public class MaximizedArrangement<T extends Serializable> extends ChatHeadArrangement {
    public static final String BUNDLE_HERO_INDEX_KEY = "hero_index";
    private static double MAX_DISTANCE_FROM_ORIGINAL;
    private static int MIN_VELOCITY_TO_POSITION_BACK;
    private final Map<ChatHead, Point> positions = new ArrayMap();
    private UpArrowLayout arrowLayout;
    private ChatHead currentChatHead = null;
    private Bundle extras;
    private boolean isActive = false;
    private boolean isTransitioning = false;
    private ChatHeadManager<T> manager;
    private int maxDistanceFromOriginal;
    private int maxHeight;
    private int maxWidth;
    private int topPadding;

    public MaximizedArrangement(ChatHeadManager<T> chatHeadManager) {
        this.manager = chatHeadManager;
    }

    public static void sendViewToBack(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null && viewGroup.indexOfChild(view) != 0) {
            viewGroup.removeView(view);
            viewGroup.addView(view, 0);
        }
    }

    @Override
    public void onConfigChanged(ChatHeadConfig chatHeadConfig) {
    }

    @Override
    public void setContainer(ChatHeadManager chatHeadManager) {
        this.manager = chatHeadManager;
    }

    @Override
    public void onActivate(ChatHeadManager chatHeadManager, Bundle bundle, int i, int i2, boolean z) {
        this.isTransitioning = true;
        this.manager = chatHeadManager;
        this.maxWidth = i;
        this.maxHeight = i2;
        MIN_VELOCITY_TO_POSITION_BACK = ChatHeadUtils.dpToPx(chatHeadManager.getDisplayMetrics(), 50);
        MAX_DISTANCE_FROM_ORIGINAL = (double) ChatHeadUtils.dpToPx(chatHeadManager.getContext(), 10);
        this.isActive = true;
        List<ChatHead<T>> chatHeads = chatHeadManager.getChatHeads();
        this.extras = bundle;
        int i3 = bundle != null ? bundle.getInt("hero_index", -1) : 0;
        if (i3 < 0 && this.currentChatHead != null) {
            i3 = getHeroIndex().intValue();
        }
        if (i3 < 0 || i3 > chatHeads.size() - 1) {
            i3 = 0;
        }
        if (chatHeads.size() > 0 && i3 < chatHeads.size()) {
            this.currentChatHead = chatHeads.get(i3);
            this.maxDistanceFromOriginal = (int) MAX_DISTANCE_FROM_ORIGINAL;
            int headHorizontalSpacing = chatHeadManager.getConfig().getHeadHorizontalSpacing(i, i2);
            int headWidth = chatHeadManager.getConfig().getHeadWidth();
            this.topPadding = ChatHeadUtils.dpToPx(chatHeadManager.getContext(), 5);
            int i4 = headWidth + headHorizontalSpacing;
            int size = i - (chatHeads.size() * i4);
            for (int i5 = 0; i5 < chatHeads.size(); i5++) {
                ChatHead<T> chatHead = chatHeads.get(i5);
                Spring horizontalSpring = chatHead.getHorizontalSpring();
                int i6 = (i5 * i4) + size;
                this.positions.put(chatHead, new Point(i6, this.topPadding));
                horizontalSpring.setAtRest();
                horizontalSpring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                double d = (double) i6;
                horizontalSpring.setEndValue(d);
                if (!z) {
                    horizontalSpring.setCurrentValue(d);
                }
                Spring verticalSpring = chatHead.getVerticalSpring();
                verticalSpring.setAtRest();
                verticalSpring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                verticalSpring.setEndValue((double) this.topPadding);
                if (!z) {
                    verticalSpring.setCurrentValue((double) this.topPadding);
                }
            }
            chatHeadManager.getCloseButton().setEnabled(true);
            chatHeadManager.getOverlayView().setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MaximizedArrangement.this.deactivate();
                }
            });
            chatHeadManager.showOverlayView(z);
            selectChatHead(this.currentChatHead);
            this.currentChatHead.getVerticalSpring().addListener(new SimpleSpringListener() {
                /* class com.chatapp.wabubbleforchat.chatheads.ui.MaximizedArrangement.AnonymousClass2 */

                @Override
                // com.facebook.rebound.SpringListener, com.facebook.rebound.SimpleSpringListener
                public void onSpringAtRest(Spring spring) {
                    super.onSpringAtRest(spring);
                    if (MaximizedArrangement.this.isTransitioning) {
                        MaximizedArrangement.this.isTransitioning = false;
                    }
                    if (MaximizedArrangement.this.currentChatHead.getVerticalSpring() != null) {
                        MaximizedArrangement.this.currentChatHead.getVerticalSpring().removeListener(this);
                    }
                }
            });
            this.currentChatHead.getHorizontalSpring().addListener(new SimpleSpringListener() {
                /* class com.chatapp.wabubbleforchat.chatheads.ui.MaximizedArrangement.AnonymousClass3 */

                @Override
                // com.facebook.rebound.SpringListener, com.facebook.rebound.SimpleSpringListener
                public void onSpringAtRest(Spring spring) {
                    super.onSpringAtRest(spring);
                    if (MaximizedArrangement.this.isTransitioning) {
                        MaximizedArrangement.this.isTransitioning = false;
                    }
                    if (MaximizedArrangement.this.currentChatHead.getHorizontalSpring() != null) {
                        MaximizedArrangement.this.currentChatHead.getHorizontalSpring().removeListener(this);
                    }
                }
            });
        }
    }

    @Override
    public void onDeactivate(int i, int i2) {
        ChatHead<T> chatHead = this.currentChatHead;
        if (chatHead != null) {
            this.manager.detachView(chatHead, getArrowLayout());
        }
        hideView();
        this.manager.hideOverlayView(true);
        this.positions.clear();
        this.isActive = false;
    }

    @Override
    public boolean handleTouchUp(ChatHead chatHead, int i, int i2, Spring spring, Spring spring2, boolean z) {
        if (i == 0 && i2 == 0) {
            i = 1;
            i2 = 1;
        }
        spring.setVelocity((double) i);
        spring2.setVelocity((double) i2);
        if (z) {
            return true;
        }
        if (chatHead == this.currentChatHead || this.manager.onItemSelected(chatHead)) {
            boolean onItemSelected = this.manager.onItemSelected(chatHead);
            if (!onItemSelected) {
                deactivate();
            }
            return onItemSelected;
        }
        selectTab(chatHead);
        return true;
    }

    private void selectTab(ChatHead<T> chatHead) {
        ChatHead<T> chatHead2 = this.currentChatHead;
        if (chatHead2 != chatHead) {
            detach(chatHead2);
            this.currentChatHead = chatHead;
        }
        pointTo(chatHead);
        showOrHideView(chatHead);
    }

    private void detach(ChatHead chatHead) {
        this.manager.detachView(chatHead, getArrowLayout());
    }

    private void positionToOriginal(ChatHead chatHead, Spring spring, Spring spring2) {
        Point point;
        Point point2;
        if (chatHead.isSticky() && (point2 = this.positions.get(chatHead)) != null && Math.hypot(((double) point2.x) - spring.getCurrentValue(), ((double) point2.y) - spring2.getCurrentValue()) > MAX_DISTANCE_FROM_ORIGINAL) {
            deactivate();
        } else if (chatHead.getState() == ChatHead.State.FREE && (point = this.positions.get(chatHead)) != null) {
            spring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
            spring.setVelocity(0.0d);
            spring.setEndValue((double) point.x);
            spring2.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
            spring2.setVelocity(0.0d);
            spring2.setEndValue((double) point.y);
        }
    }

    @Override
    public void onSpringUpdate(ChatHead chatHead, boolean z, int i, int i2, Spring spring, Spring spring2, Spring spring3, int i3) {
        if (spring == spring2 && !z) {
            double currentValue = spring2.getCurrentValue();
            if (((double) this.manager.getConfig().getHeadWidth()) + currentValue > ((double) i) && spring2.getSpringConfig() != SpringConfigsHolder.NOT_DRAGGING && !spring2.isOvershooting()) {
                positionToOriginal(chatHead, spring2, spring3);
            }
            if (currentValue < 0.0d && spring2.getSpringConfig() != SpringConfigsHolder.NOT_DRAGGING && !spring2.isOvershooting()) {
                positionToOriginal(chatHead, spring2, spring3);
            }
        } else if (spring == spring3 && !z) {
            double currentValue2 = spring3.getCurrentValue();
            if (((double) this.manager.getConfig().getHeadHeight()) + currentValue2 > ((double) i2) && spring2.getSpringConfig() != SpringConfigsHolder.NOT_DRAGGING && !spring2.isOvershooting()) {
                positionToOriginal(chatHead, spring2, spring3);
            }
            if (currentValue2 < 0.0d && spring2.getSpringConfig() != SpringConfigsHolder.NOT_DRAGGING && !spring2.isOvershooting()) {
                positionToOriginal(chatHead, spring2, spring3);
            }
        }
        if (!z && i3 < MIN_VELOCITY_TO_POSITION_BACK && spring2.getSpringConfig() == SpringConfigsHolder.DRAGGING) {
            positionToOriginal(chatHead, spring2, spring3);
        }
        if (chatHead == this.currentChatHead) {
            showOrHideView(chatHead);
        }
        if (!z) {
            int[] chatHeadCoordsForCloseButton = this.manager.getChatHeadCoordsForCloseButton(chatHead);
            if (this.manager.getDistanceCloseButtonFromHead(((float) spring2.getCurrentValue()) + ((float) (this.manager.getConfig().getHeadWidth() / 2)), ((float) spring3.getCurrentValue()) + ((float) (this.manager.getConfig().getHeadHeight() / 2))) < ((double) chatHead.CLOSE_ATTRACTION_THRESHOLD) && spring2.getSpringConfig() == SpringConfigsHolder.DRAGGING && spring3.getSpringConfig() == SpringConfigsHolder.DRAGGING && !chatHead.isSticky()) {
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

    private void showOrHideView(ChatHead chatHead) {
        Point point = this.positions.get(chatHead);
        if (point != null) {
            double currentValue = chatHead.getHorizontalSpring().getCurrentValue() - ((double) point.x);
            double currentValue2 = chatHead.getVerticalSpring().getCurrentValue() - ((double) point.y);
            double hypot = Math.hypot(currentValue, currentValue2);
            if (hypot < ((double) this.maxDistanceFromOriginal)) {
                showView(chatHead, currentValue, currentValue2, hypot);
            } else {
                hideView();
            }
        }
    }

    private UpArrowLayout getArrowLayout() {
        if (this.arrowLayout == null) {
            this.arrowLayout = this.manager.getArrowLayout();
        }
        return this.arrowLayout;
    }

    private boolean isViewHidden() {
        UpArrowLayout arrowLayout2 = getArrowLayout();
        if (arrowLayout2 == null || arrowLayout2.getVisibility() == 8) {
            return true;
        }
        return false;
    }

    private void hideView() {
        getArrowLayout().setVisibility(8);
    }

    private void showView(ChatHead chatHead, double d, double d2, double d3) {
        UpArrowLayout arrowLayout2 = getArrowLayout();
        arrowLayout2.setVisibility(0);
        arrowLayout2.setTranslationX((float) d);
        arrowLayout2.setTranslationY((float) d2);
        arrowLayout2.setAlpha(1.0f - (((float) d3) / ((float) this.maxDistanceFromOriginal)));
    }

    private void pointTo(ChatHead<T> chatHead) {
        UpArrowLayout arrowLayout2 = getArrowLayout();
        getArrowLayout().removeAllViews();
        this.manager.attachView(chatHead, arrowLayout2);
        sendViewToBack(this.manager.getOverlayView());
        Point point = this.positions.get(chatHead);
        if (point != null) {
            arrowLayout2.pointTo(point.x + (this.manager.getConfig().getHeadWidth() / 2), point.y + this.manager.getConfig().getHeadHeight() + this.manager.getConfig().getHeadVerticalSpacing(this.maxWidth, this.maxHeight));
        }
    }

    @Override
    public void onChatHeadAdded(ChatHead chatHead, boolean z) {
        chatHead.getHorizontalSpring().setCurrentValue((double) this.maxWidth).setAtRest();
        chatHead.getVerticalSpring().setCurrentValue((double) this.topPadding).setAtRest();
        onActivate(this.manager, getBundleWithHero(), this.maxWidth, this.maxHeight, z);
    }

    @Override
    public void onChatHeadRemoved(ChatHead chatHead) {
        this.manager.detachView(chatHead, getArrowLayout());
        this.manager.removeView(chatHead, getArrowLayout());
        this.positions.remove(chatHead);
        boolean z = false;
        if (this.currentChatHead == chatHead) {
            ChatHead<T> nextBestChatHead = getNextBestChatHead();
            if (nextBestChatHead != null) {
                selectTab(nextBestChatHead);
            } else {
                z = true;
            }
        }
        if (!z) {
            onActivate(this.manager, getBundleWithHero(), this.maxWidth, this.maxHeight, true);
        } else {
            deactivate();
        }
    }

    @Override
    public void onCapture(ChatHeadManager chatHeadManager, ChatHead chatHead) {
        if (!chatHead.isSticky()) {
            chatHeadManager.removeChatHead(chatHead.getKey(), true);
        }
    }

    @Override
    public void selectChatHead(ChatHead chatHead) {
        selectTab(chatHead);
    }

    private ChatHead getNextBestChatHead() {
        ChatHead<T> chatHead = null;
        for (ChatHead<T> chatHead2 : this.manager.getChatHeads()) {
            if (chatHead == null) {
                chatHead = chatHead2;
            }
        }
        return this.manager.getChatHeads().size() > 0 ? this.manager.getChatHeads().get(this.manager.getChatHeads().size() - 1) : chatHead;
    }

    private Bundle getBundleWithHero() {
        Bundle bundle = this.extras;
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt("hero_index", getHeroIndex().intValue());
        return bundle;
    }

    private void deactivate() {
        this.manager.setArrangement(MinimizedArrangement.class, getBundleWithHero());
        hideView();
    }

    @Override
    public Integer getHeroIndex() {
        int i = 0;
        int i2 = 0;
        for (ChatHead<T> chatHead : this.manager.getChatHeads()) {
            if (this.currentChatHead == chatHead) {
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
    public boolean canDrag(ChatHead chatHead) {
        return !chatHead.isSticky();
    }

    @Override
    public void removeOldestChatHead() {
        for (ChatHead<T> chatHead : this.manager.getChatHeads()) {
            if (!(chatHead.isSticky() || chatHead == this.currentChatHead)) {
                this.manager.removeChatHead(chatHead.getKey(), false);
                return;
            }
        }
    }

    @Override
    public void bringToFront(ChatHead chatHead) {
        selectChatHead(chatHead);
    }

    @Override
    public void onReloadFragment(ChatHead chatHead) {
        ChatHead chatHead2 = this.currentChatHead;
        if (chatHead2 != null && chatHead == chatHead2) {
            this.manager.attachView(chatHead, getArrowLayout());
        }
    }

    @Override
    public boolean shouldShowCloseButton(ChatHead chatHead) {
        return !chatHead.isSticky();
    }
}
