package com.whatscan.toolkit.forwa.WSticker.editimage.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.whatscan.toolkit.forwa.WSticker.editimage.EditorActivity;

public class CutOut {

    public static final short CUTOUT_ACTIVITY_REQUEST_CODE = 368;
    public static final short CUTOUT_ACTIVITY_REQUEST_CODE_STICKER = 369;
    public static final short CUTOUT_ACTIVITY_RESULT_ERROR_CODE = 3680;
    public static final String CUTOUT_EXTRA_SOURCE = "CUTOUT_EXTRA_SOURCE";
    public static final String CUTOUT_EXTRA_RESULT = "CUTOUT_EXTRA_RESULT";
    public static final String CUTOUT_EXTRA_BORDER_COLOR = "CUTOUT_EXTRA_BORDER_COLOR";
    public static final String CUTOUT_EXTRA_CROP = "CUTOUT_EXTRA_CROP";
    public static final String CUTOUT_EXTRA_INTRO = "CUTOUT_EXTRA_INTRO";

    public static ActivityBuilder activity() {
        return new ActivityBuilder();
    }

    public static Uri getUri(@Nullable Intent data) {
        return data != null ? data.getParcelableExtra(CUTOUT_EXTRA_RESULT) : null;
    }

    public static Exception getError(@Nullable Intent data) {
        return data != null ? (Exception) data.getSerializableExtra(CUTOUT_EXTRA_RESULT) : null;
    }

    public static final class ActivityBuilder {

        @Nullable
        private Uri source;
        private boolean bordered;
        private boolean crop = true;
        private boolean intro;
        private int borderColor = Color.WHITE;

        private ActivityBuilder() {

        }

        private Intent getIntent(@NonNull Context context) {
            Intent intent = new Intent();
            intent.setClass(context, EditorActivity.class);

            if (source != null) {
                intent.putExtra(CUTOUT_EXTRA_SOURCE, source);
            }

            if (bordered) {
                intent.putExtra(CUTOUT_EXTRA_BORDER_COLOR, borderColor);
            }

            if (crop) {
                intent.putExtra(CUTOUT_EXTRA_CROP, true);
            }

            if (intro) {
                intent.putExtra(CUTOUT_EXTRA_INTRO, true);
            }
            return intent;
        }

        public ActivityBuilder src(Uri source) {
            this.source = source;
            return this;
        }

        public ActivityBuilder bordered() {
            this.bordered = true;
            return this;
        }

        public ActivityBuilder bordered(int borderColor) {
            this.borderColor = borderColor;
            return bordered();
        }

        public ActivityBuilder noCrop() {
            this.crop = false;
            return this;
        }

        public ActivityBuilder intro() {
            this.intro = true;
            return this;
        }

        public void start(@NonNull Activity activity, int requestId) {
            activity.startActivityForResult(getIntent(activity), requestId);
        }
    }
}
