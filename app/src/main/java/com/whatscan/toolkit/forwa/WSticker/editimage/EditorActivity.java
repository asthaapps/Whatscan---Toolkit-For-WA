package com.whatscan.toolkit.forwa.WSticker.editimage;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.alexvasilkov.gestures.views.interfaces.GestureView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WSticker.editimage.task.SaveDrawingTask;
import com.whatscan.toolkit.forwa.WSticker.editimage.utils.BitmapUtility;
import com.whatscan.toolkit.forwa.WSticker.editimage.utils.CutOut;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.DrawView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;

public class EditorActivity extends AppCompatActivity {
    private static final short MAX_ERASER_SIZE = 150;
    private static final short BORDER_SIZE = 45;
    private static final float MAX_ZOOM = 4F;
    private static final int INTRO_REQUEST_CODE = 4;
    private static final String INTRO_SHOWN = "INTRO_SHOWN";
    public FrameLayout loadingModal;
    private GestureView gestureView;
    private DrawView drawView;
    private LinearLayout manualClearSettingsLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), EditorActivity.this);
        setContentView(R.layout.activity_editor);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(EditorActivity.this, ll_banner);

        TextView doneButton = findViewById(R.id.done);
        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        SeekBar strokeBar = findViewById(R.id.strokeBar);
        RelativeLayout rlEraseImage = findViewById(R.id.rlEraseImage);
        RelativeLayout rl_toolbar = findViewById(R.id.rl_toolbar);
        LinearLayout controllers = findViewById(R.id.controllers);
        manualClearSettingsLayout = findViewById(R.id.manual_clear_settings_layout);

        tv_toolbar.setText(Html.fromHtml("<small>" + getResources().getString(R.string.erase_image) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlEraseImage.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            rl_toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            controllers.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            manualClearSettingsLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
        } else {
            setStatusBar();
            rlEraseImage.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            rl_toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            controllers.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            manualClearSettingsLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        strokeBar.setMax(MAX_ERASER_SIZE);
        strokeBar.setProgress(50);

        gestureView = findViewById(R.id.gestureView);
        drawView = findViewById(R.id.drawView);
        drawView.setDrawingCacheEnabled(true);
        drawView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        drawView.setDrawingCacheEnabled(true);
        drawView.setStrokeWidth(strokeBar.getProgress());

        strokeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                drawView.setStrokeWidth(seekBar.getProgress());
            }
        });

        loadingModal = findViewById(R.id.loadingModal);
        loadingModal.setVisibility(INVISIBLE);

        drawView.setLoadingModal(loadingModal);


        setUndoRedo();
        initializeActionButtons();

        la_back.setOnClickListener(v -> onBackPressed());
        doneButton.setOnClickListener(v -> startSaveDrawingTask());

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("uri");
        File file = new File(url);
        String filePath = file.getPath();
        setDrawViewBitmap(Uri.fromFile(file));
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhite));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        } else {
            window.clearFlags(0);
        }
    }

    public void setStatusBarTheme() {
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darkBlack));
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void startSaveDrawingTask() {
        SaveDrawingTask task = new SaveDrawingTask(this);
        int borderColor;
        if ((borderColor = getIntent().getIntExtra(CutOut.CUTOUT_EXTRA_BORDER_COLOR, -1)) != -1) {
            Bitmap image = BitmapUtility.getBorderedBitmap(this.drawView.getDrawingCache(), borderColor, BORDER_SIZE);
            task.execute(image);
        } else {
            task.execute(this.drawView.getDrawingCache());
        }
    }

    private void activateGestureView() {
        gestureView.getController().getSettings()
                .setMaxZoom(MAX_ZOOM)
                .setDoubleTapZoom(-1f)
                .setPanEnabled(true)
                .setZoomEnabled(true)
                .setDoubleTapEnabled(true)
                .setOverscrollDistance(0f, 0f)
                .setOverzoomFactor(2f);
    }

    private void deactivateGestureView() {
        gestureView.getController().getSettings()
                .setPanEnabled(false)
                .setZoomEnabled(false)
                .setDoubleTapEnabled(false);
    }

    private void initializeActionButtons() {
        LinearLayout autoClearButton = findViewById(R.id.auto_clear_button);
        LinearLayout manualClearButton = findViewById(R.id.manual_clear_button);
        LinearLayout zoomButton = findViewById(R.id.zoom_button);

        autoClearButton.setActivated(false);
        autoClearButton.setOnClickListener((buttonView) -> {
            if (!autoClearButton.isActivated()) {
                drawView.setAction(DrawView.DrawViewAction.AUTO_CLEAR);
                manualClearSettingsLayout.setVisibility(INVISIBLE);
                autoClearButton.setActivated(true);
                manualClearButton.setActivated(false);
                zoomButton.setActivated(false);
                deactivateGestureView();
            }
        });

        manualClearButton.setActivated(true);
        drawView.setAction(DrawView.DrawViewAction.MANUAL_CLEAR);
        manualClearButton.setOnClickListener((buttonView) -> {
            if (!manualClearButton.isActivated()) {
                drawView.setAction(DrawView.DrawViewAction.MANUAL_CLEAR);
                manualClearSettingsLayout.setVisibility(VISIBLE);
                manualClearButton.setActivated(true);
                autoClearButton.setActivated(false);
                zoomButton.setActivated(false);
                deactivateGestureView();
            }
        });

        zoomButton.setActivated(false);
        deactivateGestureView();
        zoomButton.setOnClickListener((buttonView) -> {
            if (!zoomButton.isActivated()) {
                drawView.setAction(DrawView.DrawViewAction.ZOOM);
                manualClearSettingsLayout.setVisibility(INVISIBLE);
                zoomButton.setActivated(true);
                manualClearButton.setActivated(false);
                autoClearButton.setActivated(false);
                activateGestureView();
            }
        });
    }

    private void setUndoRedo() {
        ImageView undoButton = findViewById(R.id.undo);
        undoButton.setEnabled(false);
        undoButton.setOnClickListener(v -> undo());
        ImageView redoButton = findViewById(R.id.redo);
        redoButton.setEnabled(false);
        redoButton.setOnClickListener(v -> redo());
        drawView.setButtons(undoButton, redoButton);
    }

    public void exitWithError(Exception e) {
        Intent intent = new Intent();
        intent.putExtra(CutOut.CUTOUT_EXTRA_RESULT, e);
        setResult(CutOut.CUTOUT_ACTIVITY_RESULT_ERROR_CODE, intent);
        finish();
    }

    private void setDrawViewBitmap(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            drawView.setBitmap(bitmap);
        } catch (IOException e) {
            exitWithError(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                setDrawViewBitmap(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                exitWithError(result.getError());
            } else {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        } else if (requestCode == INTRO_REQUEST_CODE) {
            SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
            editor.putBoolean(INTRO_SHOWN, true);
            editor.apply();
        }
    }

    private void undo() {
        drawView.undo();
    }

    private void redo() {
        drawView.redo();
    }
}