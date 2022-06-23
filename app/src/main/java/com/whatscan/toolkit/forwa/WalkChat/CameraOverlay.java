package com.whatscan.toolkit.forwa.WalkChat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.IOException;

public class CameraOverlay extends TextureView implements TextureView.SurfaceTextureListener {
    static CameraOverlay objCamOverlay;
    static WindowManager windowManager;
    Camera camera;

    public CameraOverlay(Context context) {
        super(context);
        setSurfaceTextureListener(this);
    }

    @SuppressLint({"WrongConstant"})
    public static View methOverlayCheck(Context context) {
        int i;
        if (objCamOverlay == null) {
            objCamOverlay = new CameraOverlay(context);
        }
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            i = 2032;
        } else {
            i = 2002;
        }
        windowManager.addView(objCamOverlay, new WindowManager.LayoutParams(-1, -1, i, -2147220456, -3));
        return objCamOverlay;
    }

    public static void methWinManager() {
        if (objCamOverlay != null) {
            windowManager.removeView(objCamOverlay);
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        int i3 = 0;
        if (camera == null) {
            camera = Camera.open();
        }
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size preferredPreviewSizeForVideo = parameters.getPreferredPreviewSizeForVideo();
            parameters.setPreviewSize(preferredPreviewSizeForVideo.width, preferredPreviewSizeForVideo.height);
            int[] iArr = parameters.getSupportedPreviewFpsRange().get(0);
            parameters.setPreviewFpsRange(iArr[0], iArr[1]);
            camera.setParameters(parameters);
            Matrix matrix = new Matrix();
            int rotation = windowManager.getDefaultDisplay().getRotation();
            int i4 = preferredPreviewSizeForVideo.width;
            int i5 = preferredPreviewSizeForVideo.height;
            switch (rotation) {
                case 0:
                    i3 = 90;
                    i4 = preferredPreviewSizeForVideo.height;
                    i5 = preferredPreviewSizeForVideo.width;
                    break;
                case 1:
                    i4 = preferredPreviewSizeForVideo.width;
                    i5 = preferredPreviewSizeForVideo.height;
                    break;
                case 2:
                    i3 = 270;
                    i4 = preferredPreviewSizeForVideo.height;
                    i5 = preferredPreviewSizeForVideo.width;
                    break;
                case 3:
                    i3 = 180;
                    i4 = preferredPreviewSizeForVideo.width;
                    i5 = preferredPreviewSizeForVideo.height;
                    break;
            }
            camera.setDisplayOrientation(i3);
            setLayoutParams(new FrameLayout.LayoutParams(i4, i5, 17));
            setTransform(matrix);
            try {
                camera.setPreviewTexture(surfaceTexture);
                camera.startPreview();
            } catch (IOException e) {
                camera.release();
                camera = null;
            }
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        return true;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }
}