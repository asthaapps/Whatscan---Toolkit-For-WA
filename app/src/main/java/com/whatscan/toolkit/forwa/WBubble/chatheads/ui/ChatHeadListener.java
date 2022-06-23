package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import java.io.Serializable;

public interface ChatHeadListener<T> {
    void onChatHeadAdded(T t);

    <T extends Serializable> void onChatHeadAnimateEnd(ChatHead<T> chatHead);

    <T extends Serializable> void onChatHeadAnimateStart(ChatHead chatHead);

    void onChatHeadArrangementChanged(ChatHeadArrangement chatHeadArrangement, ChatHeadArrangement chatHeadArrangement2);

    void onChatHeadRemoved(T t, boolean z);
}
