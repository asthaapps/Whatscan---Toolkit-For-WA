package com.whatscan.toolkit.forwa.WBubble.chatheads.ui;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.whatscan.toolkit.forwa.WBubble.chatheads.ChatHeadUtils;

public class ChatHeadOverlayView extends View {
    private static final long ANIMATION_DURATION = 600;
    private float OVAL_RADIUS;
    private float STAMP_SPACING;
    private ObjectAnimator animator;
    private Path arrowDashedPath;
    private Paint paint = new Paint();
    private PathEffect pathDashEffect;

    public ChatHeadOverlayView(Context context) {
        super(context);
        init(context);
    }

    public ChatHeadOverlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public ChatHeadOverlayView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.STAMP_SPACING = (float) ChatHeadUtils.dpToPx(context, 20);
        this.OVAL_RADIUS = (float) ChatHeadUtils.dpToPx(context, 3);
        this.animator = ObjectAnimator.ofFloat(this, "phase", 0.0f, -this.STAMP_SPACING);
        this.animator.setInterpolator(new LinearInterpolator());
        this.animator.setRepeatMode(1);
        this.animator.setRepeatCount(-1);
        this.animator.setDuration(ANIMATION_DURATION);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.arrowDashedPath != null) {
            this.paint.setPathEffect(this.pathDashEffect);
            canvas.drawPath(this.arrowDashedPath, this.paint);
        }
    }

    private Path makeDot(float f) {
        Path path = new Path();
        path.addCircle(0.0f, 0.0f, f, Path.Direction.CCW);
        return path;
    }

    public void drawPath(float f, float f2, float f3, float f4) {
        this.arrowDashedPath = new Path();
        this.arrowDashedPath.moveTo(f, f2);
        this.arrowDashedPath.lineTo(f3, f4);
        this.paint.setColor(Color.parseColor("#77FFFFFF"));
        this.paint.setStrokeWidth(this.OVAL_RADIUS * 2.0f);
        animatePath();
        invalidate();
    }

    private void setPhase(float f) {
        this.pathDashEffect = new PathDashPathEffect(makeDot(this.OVAL_RADIUS), this.STAMP_SPACING, f, PathDashPathEffect.Style.ROTATE);
        invalidate();
    }

    @TargetApi(11)
    private void animatePath() {
        this.animator.start();
    }

    public void clearPath() {
        this.animator.cancel();
        this.arrowDashedPath = null;
        invalidate();
    }
}
