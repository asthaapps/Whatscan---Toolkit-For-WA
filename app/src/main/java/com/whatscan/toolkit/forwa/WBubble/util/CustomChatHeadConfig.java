package com.whatscan.toolkit.forwa.WBubble.util;

import android.content.Context;
import android.graphics.Point;

import com.whatscan.toolkit.forwa.WBubble.chatheads.ChatHeadUtils;
import com.whatscan.toolkit.forwa.WBubble.chatheads.ui.ChatHeadDefaultConfig;


public class CustomChatHeadConfig extends ChatHeadDefaultConfig {
    public static int diameter = 62;

    public CustomChatHeadConfig(Context context, int i, int i2) {
        super(context);
        setHeadHorizontalSpacing(ChatHeadUtils.dpToPx(context, -2));
        setHeadVerticalSpacing(ChatHeadUtils.dpToPx(context, 2));
        setHeadWidth(ChatHeadUtils.dpToPx(context, diameter));
        setHeadHeight(ChatHeadUtils.dpToPx(context, diameter));
        setInitialPosition(new Point(i, i2));
        setCloseButtonHeight(ChatHeadUtils.dpToPx(context, diameter));
        setCloseButtonWidth(ChatHeadUtils.dpToPx(context, diameter));
        setCloseButtonBottomMargin(ChatHeadUtils.dpToPx(context, 140));
        setCircularRingWidth(ChatHeadUtils.dpToPx(context, 60));
        setCircularRingHeight(ChatHeadUtils.dpToPx(context, 60));
    }

    @Override
    public int getMaxChatHeads(int i, int i2) {
        return (int) Math.floor((double) (i / (getHeadWidth() + getHeadHorizontalSpacing(i, i2))));
    }
}
