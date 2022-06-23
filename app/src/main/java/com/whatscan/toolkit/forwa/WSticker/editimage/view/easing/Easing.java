package com.whatscan.toolkit.forwa.WSticker.editimage.view.easing;

public interface Easing {

    double easeOut(double time, double start, double end, double duration);

    double easeIn(double time, double start, double end, double duration);

    double easeInOut(double time, double start, double end, double duration);
}
