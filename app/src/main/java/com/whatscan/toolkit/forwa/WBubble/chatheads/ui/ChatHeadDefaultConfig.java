package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.content.Context;
import android.graphics.Point;

import com.whatscan.toolkit.forwa.WBubble.chatheads.ChatHeadUtils;

public class ChatHeadDefaultConfig extends ChatHeadConfig {
    public ChatHeadDefaultConfig(Context context) {
        setHeadHeight(ChatHeadUtils.dpToPx(context, 56));
        setHeadWidth(ChatHeadUtils.dpToPx(context, 56));
        setHeadHorizontalSpacing(ChatHeadUtils.dpToPx(context, 10));
        setHeadVerticalSpacing(ChatHeadUtils.dpToPx(context, 5));
        setInitialPosition(new Point(0, ChatHeadUtils.dpToPx(context, 0)));
        setCloseButtonHidden(false);
        setCloseButtonWidth(ChatHeadUtils.dpToPx(context, 62));
        setCloseButtonHeight(ChatHeadUtils.dpToPx(context, 62));
        setCloseButtonBottomMargin(ChatHeadUtils.dpToPx(context, 50));
        setCircularRingWidth(ChatHeadUtils.dpToPx(context, 61));
        setCircularRingHeight(ChatHeadUtils.dpToPx(context, 61));
        setMaxChatHeads(5);
    }

    @Override
    public int getCircularFanOutRadius(int i, int i2) {
        return (int) (((float) i) / 2.5f);
    }
}
