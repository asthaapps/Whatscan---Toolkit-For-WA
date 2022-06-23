package com.whatscan.toolkit.forwa.WBubble.util;

import android.view.animation.Interpolator;

public class MyBounceInterpolator implements Interpolator {
    double a = 1.0d;
    double b = 10.0d;

    public MyBounceInterpolator(double d, double d2) {
        this.a = d;
        this.b = d2;
    }

    public float getInterpolation(float f) {
        return (float) ((Math.pow(2.718281828459045d, ((double) (-f)) / this.a) * -1.0d * Math.cos(this.b * ((double) f))) + 1.0d);
    }
}
