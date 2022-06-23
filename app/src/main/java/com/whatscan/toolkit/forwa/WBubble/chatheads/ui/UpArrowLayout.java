package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.whatscan.toolkit.forwa.R;

import java.util.ArrayList;

public class UpArrowLayout extends ViewGroup {
    private final ArrayList<View> mMatchParentChildren = new ArrayList<>(1);
    private final Point pointTo = new Point(0, 0);
    private int arrowDrawable = R.drawable.chat_top_arrow;
    private ImageView arrowView;

    public UpArrowLayout(Context context) {
        super(context);
        init();
    }

    public UpArrowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public UpArrowLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public int getArrowDrawable() {
        return this.arrowDrawable;
    }

    public void setArrowDrawable(int i) {
        this.arrowDrawable = i;
        init();
    }

    private void init() {
        ImageView imageView = this.arrowView;
        if (imageView != null) {
            removeView(imageView);
        }
        this.arrowView = createArrowView();
        addView(this.arrowView);
    }

    public ImageView createArrowView() {
        Drawable drawable = getResources().getDrawable(this.arrowDrawable);
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawable);
        return imageView;
    }

    @SuppressLint("WrongConstant")
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        super.onMeasure(i, i2);
        int childCount = getChildCount();
        @SuppressLint("WrongConstant") int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        this.arrowView.measure(makeMeasureSpec, makeMeasureSpec);
        int measuredHeight = this.arrowView.getMeasuredHeight();
        int size = MeasureSpec.getSize(i2);
        int makeMeasureSpec2 = size > measuredHeight ? MeasureSpec.makeMeasureSpec(size - (this.pointTo.y + measuredHeight), MeasureSpec.getMode(i2)) : i2;
        @SuppressLint("WrongConstant") boolean z = (MeasureSpec.getMode(i) == 1073741824 && MeasureSpec.getMode(makeMeasureSpec2) == 1073741824) ? false : true;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = getChildAt(i11);
            if (childAt == this.arrowView) {
                i5 = i8;
                i6 = i9;
                i7 = i10;
            } else if (childAt.getVisibility() != 8) {
                measureChildWithMargins(childAt, i, 0, makeMeasureSpec2, 0);
                LayoutParams layoutParams = childAt.getLayoutParams();
                int max = Math.max(i9, childAt.getMeasuredWidth());
                int max2 = Math.max(i10, childAt.getMeasuredHeight());
                i8 = combineMeasuredStates(i8, childAt.getMeasuredState());
                if (z && (layoutParams.width == -1 || layoutParams.height == -1)) {
                    this.mMatchParentChildren.add(childAt);
                }
                i9 = max;
                i10 = max2;
            } else {
                i5 = i8;
                i6 = i9;
                i7 = i10;
            }
            i10 = i7;
            i9 = i6;
            i8 = i5;
        }
        setMeasuredDimension(resolveSizeAndState(Math.max(i9, getSuggestedMinimumWidth()), i, i8), resolveSizeAndState(Math.max(i10, getSuggestedMinimumHeight()), makeMeasureSpec2, i8 << 16));
        int size2 = this.mMatchParentChildren.size();
        if (size2 > 1) {
            for (int i12 = 0; i12 < size2; i12++) {
                View view = this.mMatchParentChildren.get(i12);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
                if (marginLayoutParams.width == -1) {
                    i3 = MeasureSpec.makeMeasureSpec((getMeasuredWidth() - marginLayoutParams.leftMargin) - marginLayoutParams.rightMargin, 1073741824);
                } else {
                    i3 = getChildMeasureSpec(i, marginLayoutParams.leftMargin + marginLayoutParams.rightMargin, marginLayoutParams.width);
                }
                if (marginLayoutParams.height == -1) {
                    i4 = MeasureSpec.makeMeasureSpec((getMeasuredHeight() - marginLayoutParams.topMargin) - marginLayoutParams.bottomMargin, 1073741824);
                } else {
                    i4 = getChildMeasureSpec(makeMeasureSpec2, marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height);
                }
                view.measure(i3, i4);
            }
        }
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + measuredHeight + this.pointTo.y);
        updatePointer();
    }

    public void measureChildWithMargins(View view, int i, int i2, int i3, int i4) {
        LayoutParams layoutParams = view.getLayoutParams();
        view.measure(getChildMeasureSpec(i, i2, layoutParams.width), getChildMeasureSpec(i3, i4, layoutParams.height));
    }

    public void pointTo(int i, int i2) {
        Point point = this.pointTo;
        point.x = i;
        point.y = i2;
        if (!(getMeasuredHeight() == 0 || getMeasuredWidth() == 0)) {
            updatePointer();
        }
        invalidate();
    }

    private void updatePointer() {
        int measuredWidth = this.pointTo.x - (this.arrowView.getMeasuredWidth() / 2);
        int i = this.pointTo.y;
        float f = (float) measuredWidth;
        if (f != this.arrowView.getTranslationX()) {
            this.arrowView.setTranslationX(f);
        }
        float f2 = (float) i;
        if (f2 != this.arrowView.getTranslationY()) {
            this.arrowView.setTranslationY(f2);
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        ImageView imageView = this.arrowView;
        imageView.layout(i, i2, imageView.getMeasuredWidth() + i, this.arrowView.getMeasuredHeight() + i2);
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            ImageView imageView2 = this.arrowView;
            if (childAt != imageView2) {
                childAt.layout(i, imageView2.getMeasuredHeight() + i2 + this.pointTo.y, i3, i4);
            }
        }
    }

    public LayoutParams generateDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(-1, -1);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new FrameLayout.LayoutParams(getContext(), attributeSet);
    }

    public void removeAllViews() {
        super.removeAllViews();
        addView(this.arrowView);
    }
}