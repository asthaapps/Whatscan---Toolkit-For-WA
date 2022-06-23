package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.graphics.Point;

public class ChatHeadConfig {
    private int circularFanOutRadius;
    private int circularRingHeight;
    private int circularRingWidth;
    private int closeButtonBottomMargin;
    private int closeButtonHeight;
    private boolean closeButtonHidden;
    private int closeButtonWidth;
    private int headHeight;
    private int headHorizontalSpacing;
    private int headVerticalSpacing;
    private int headWidth;
    private Point initialPosition;
    private int maxChatHeads;

    public boolean isCloseButtonHidden() {
        return this.closeButtonHidden;
    }

    public void setCloseButtonHidden(boolean z) {
        this.closeButtonHidden = z;
    }

    public void setCircularFanOutRadius(int i) {
        this.circularFanOutRadius = i;
    }

    public int getMaxChatHeads() {
        return this.maxChatHeads;
    }

    public void setMaxChatHeads(int i) {
        this.maxChatHeads = i;
    }

    public int getHeadHeight() {
        return this.headHeight;
    }

    public void setHeadHeight(int i) {
        this.headHeight = i;
    }

    public int getHeadWidth() {
        return this.headWidth;
    }

    public void setHeadWidth(int i) {
        this.headWidth = i;
    }

    public int getHeadHorizontalSpacing(int i, int i2) {
        return this.headHorizontalSpacing;
    }

    public void setHeadHorizontalSpacing(int i) {
        this.headHorizontalSpacing = i;
    }

    public int getHeadVerticalSpacing(int i, int i2) {
        return this.headVerticalSpacing;
    }

    public void setHeadVerticalSpacing(int i) {
        this.headVerticalSpacing = i;
    }

    public Point getInitialPosition() {
        return this.initialPosition;
    }

    public void setInitialPosition(Point point) {
        this.initialPosition = point;
    }

    public int getMaxChatHeads(int i, int i2) {
        return this.maxChatHeads;
    }

    public int getCloseButtonWidth() {
        return this.closeButtonWidth;
    }

    public void setCloseButtonWidth(int i) {
        this.closeButtonWidth = i;
    }

    public int getCloseButtonHeight() {
        return this.closeButtonHeight;
    }

    public void setCloseButtonHeight(int i) {
        this.closeButtonHeight = i;
    }

    public int getCloseButtonBottomMargin() {
        return this.closeButtonBottomMargin;
    }

    public void setCloseButtonBottomMargin(int i) {
        this.closeButtonBottomMargin = i;
    }

    public int getCircularRingWidth() {
        return this.circularRingWidth;
    }

    public void setCircularRingWidth(int i) {
        this.circularRingWidth = i;
    }

    public int getCircularRingHeight() {
        return this.circularRingHeight;
    }

    public void setCircularRingHeight(int i) {
        this.circularRingHeight = i;
    }

    public int getCircularFanOutRadius(int i, int i2) {
        return this.circularFanOutRadius;
    }
}
