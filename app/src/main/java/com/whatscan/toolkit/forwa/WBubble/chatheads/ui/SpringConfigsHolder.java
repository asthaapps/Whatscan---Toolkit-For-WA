package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import com.facebook.rebound.SpringConfig;

public class SpringConfigsHolder {
    public static SpringConfig CAPTURING = SpringConfig.fromOrigamiTensionAndFriction(100.0d, 10.0d);
    public static SpringConfig DRAGGING = SpringConfig.fromOrigamiTensionAndFriction(0.0d, 1.5d);
    public static SpringConfig NOT_DRAGGING = SpringConfig.fromOrigamiTensionAndFriction(190.0d, 20.0d);
}
