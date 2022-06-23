package com.whatscan.toolkit.forwa.WSticker.editimage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WSticker.editimage.fragment.AddTextFragment;
import com.whatscan.toolkit.forwa.WSticker.editimage.fragment.CropFragment;
import com.whatscan.toolkit.forwa.WSticker.editimage.fragment.MainMenuFragment;
import com.whatscan.toolkit.forwa.WSticker.editimage.fragment.PaintFragment;
import com.whatscan.toolkit.forwa.WSticker.editimage.fragment.RotateFragment;
import com.whatscan.toolkit.forwa.WSticker.editimage.fragment.StickerFragment;
import com.whatscan.toolkit.forwa.WSticker.editimage.utils.BitmapUtils;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.CropImageView;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.CustomPaintView;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.CustomViewPager;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.RotateImageView;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.StickerView;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.TextStickerView;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.imagezoom.ImageViewTouch;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.imagezoom.ImageViewTouchBase;
import com.whatscan.toolkit.forwa.WSticker.editimage.widget.RedoUndoController;

public class EditImageActivity extends BaseActivity {
    public static final String FILE_PATH = "file_path";
    public static final String EXTRA_OUTPUT = "extra_output";
    public static final String IMAGE_IS_EDIT = "image_is_edit";

    public static final int MODE_NONE = 0;
    public static final int MODE_STICKERS = 1;
    public static final int MODE_CROP = 3;
    public static final int MODE_ROTATE = 4;
    public static final int MODE_TEXT = 5;
    public static final int MODE_PAINT = 6;

    public String filePath;
    public String saveFilePath;
    public int mode = MODE_NONE;
    public ImageViewTouch mainImage;
    public ViewFlipper bannerFlipper;
    public StickerView mStickerView;
    public CropImageView mCropPanel;
    public RotateImageView mRotatePanel;
    public TextStickerView mTextStickerView;
    public CustomPaintView mPaintView;
    public CustomViewPager bottomGallery;
    public StickerFragment mStickerFragment;
    public CropFragment mCropFragment;
    public RotateFragment mRotateFragment;
    public AddTextFragment mAddTextFragment;
    public PaintFragment mPaintFragment;
    protected int mOpTimes = 0;
    protected boolean isBeenSaved = false;
    private int imageWidth, imageHeight;
    private LoadImageTask mLoadImageTask;
    private EditImageActivity mContext;
    private Bitmap mainBitmap;
    public ImageView backBtn;
    public View applyBtn;
    public View saveBtn;
    public BottomGalleryAdapter mBottomGalleryAdapter;
    private MainMenuFragment mMainMenuFragment;
    private SaveImageTask mSaveImageTask;
    private RedoUndoController mRedoUndoController;
    private RelativeLayout rlImageEdit, banner;

    public static void start(Activity context, final String editImagePath, final String outputPath, final int requestCode) {
        if (TextUtils.isEmpty(editImagePath)) {
            Toast.makeText(context, R.string.no_choose, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent it = new Intent(context, EditImageActivity.class);
        it.putExtra(EditImageActivity.FILE_PATH, editImagePath);
        it.putExtra(EditImageActivity.EXTRA_OUTPUT, outputPath);
        context.startActivityForResult(it, requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), EditImageActivity.this);
        setContentView(R.layout.activity_image_edit);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(EditImageActivity.this, ll_banner);

        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText(Html.fromHtml("<small>" + getResources().getString(R.string.edit_image) + "</small>"));

        checkInitImageLoader();
        initView();
        getData();
    }

    private void getData() {
        filePath = getIntent().getStringExtra(FILE_PATH);
        saveFilePath = getIntent().getStringExtra(EXTRA_OUTPUT);// 保存图片路径
        loadImage(filePath);
    }

    private void initView() {
        mContext = this;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = metrics.widthPixels / 2;
        imageHeight = metrics.heightPixels / 2;

        mainImage = findViewById(R.id.main_image);
        backBtn = findViewById(R.id.la_back);
        bannerFlipper = findViewById(R.id.banner_flipper);
        applyBtn = findViewById(R.id.apply);
        saveBtn = findViewById(R.id.save_btn);
        mStickerView = findViewById(R.id.sticker_panel);
        mCropPanel = findViewById(R.id.crop_panel);
        mRotatePanel = findViewById(R.id.rotate_panel);
        mTextStickerView = findViewById(R.id.text_sticker_panel);
        mPaintView = findViewById(R.id.custom_paint_view);
        bottomGallery = findViewById(R.id.bottom_gallery);
        rlImageEdit = findViewById(R.id.rlImageEdit);
        banner = findViewById(R.id.banner);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlImageEdit.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            banner.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            bottomGallery.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
        } else {
            setStatusBar();
            rlImageEdit.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            banner.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            bottomGallery.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        bannerFlipper.setInAnimation(this, R.anim.fade_in);
        bannerFlipper.setOutAnimation(this, R.anim.fade_out);

        applyBtn.setOnClickListener(new ApplyBtnClick());
        saveBtn.setOnClickListener(new SaveBtnClick());
        backBtn.setOnClickListener(v -> onBackPressed());

        mMainMenuFragment = MainMenuFragment.newInstance();
        mBottomGalleryAdapter = new BottomGalleryAdapter(getSupportFragmentManager());
        mStickerFragment = StickerFragment.newInstance();
        mCropFragment = CropFragment.newInstance();
        mRotateFragment = RotateFragment.newInstance();
        mAddTextFragment = AddTextFragment.newInstance();
        mPaintFragment = PaintFragment.newInstance();

        bottomGallery.setAdapter(mBottomGalleryAdapter);

        mainImage.setFlingListener((e1, e2, velocityX, velocityY) -> {
            if (velocityY > 1) {
                closeInputMethod();
            }
        });

        mRedoUndoController = new RedoUndoController(this, findViewById(R.id.redo_uodo_panel));
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

    private void closeInputMethod() {
        if (mAddTextFragment.isAdded()) {
            mAddTextFragment.hideInput();
        }
    }

    public void loadImage(String filepath) {
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }
        mLoadImageTask = new LoadImageTask();
        mLoadImageTask.execute(filepath);
    }

    @Override
    public void onBackPressed() {
        switch (mode) {
            case MODE_STICKERS:
                mStickerFragment.backToMain();
                return;
            case MODE_CROP:
                mCropFragment.backToMain();
                return;
            case MODE_ROTATE:
                mRotateFragment.backToMain();
                return;
            case MODE_TEXT:
                mAddTextFragment.backToMain();
                return;
            case MODE_PAINT:
                mPaintFragment.backToMain();
                return;
        }

        if (canAutoExit()) {
            onSaveTaskDone();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.exit_without_save)
                    .setCancelable(false).setPositiveButton(R.string.confirm, (dialog, id) -> mContext.finish()).setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    protected void doSaveImage() {
        if (mOpTimes <= 0)
            return;

        if (mSaveImageTask != null) {
            mSaveImageTask.cancel(true);
        }

        mSaveImageTask = new SaveImageTask();
        mSaveImageTask.execute(mainBitmap);
    }

    public void changeMainBitmap(Bitmap newBit, boolean needPushUndoStack) {
        if (newBit == null)
            return;

        if (mainBitmap == null || mainBitmap != newBit) {
            if (needPushUndoStack) {
                mRedoUndoController.switchMainBit(mainBitmap, newBit);
                increaseOpTimes();
            }
            mainBitmap = newBit;
            mainImage.setImageBitmap(mainBitmap);
            mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }

        if (mSaveImageTask != null) {
            mSaveImageTask.cancel(true);
        }

        if (mRedoUndoController != null) {
            mRedoUndoController.onDestroy();
        }
    }

    public void increaseOpTimes() {
        mOpTimes++;
        isBeenSaved = false;
    }

    public void resetOpTimes() {
        isBeenSaved = true;
    }

    public boolean canAutoExit() {
        return isBeenSaved || mOpTimes == 0;
    }

    protected void onSaveTaskDone() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(FILE_PATH, filePath);
        returnIntent.putExtra(EXTRA_OUTPUT, saveFilePath);
        returnIntent.putExtra(IMAGE_IS_EDIT, mOpTimes > 0);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public Bitmap getMainBit() {
        return mainBitmap;
    }

    private final class BottomGalleryAdapter extends FragmentPagerAdapter {
        public BottomGalleryAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int index) {
            switch (index) {
                case MainMenuFragment.INDEX:
                    return mMainMenuFragment;
                case StickerFragment.INDEX:
                    return mStickerFragment;
                case CropFragment.INDEX:
                    return mCropFragment;
                case RotateFragment.INDEX:
                    return mRotateFragment;
                case AddTextFragment.INDEX:
                    return mAddTextFragment;
                case PaintFragment.INDEX:
                    return mPaintFragment;
            }
            return MainMenuFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 8;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return BitmapUtils.getSampledBitmap(params[0], imageWidth,
                    imageHeight);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            changeMainBitmap(result, false);
        }
    }

    private final class ApplyBtnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (mode) {
                case MODE_STICKERS:
                    mStickerFragment.applyStickers();
                    break;

                case MODE_CROP:
                    mCropFragment.applyCropImage();
                    break;
                case MODE_ROTATE:
                    mRotateFragment.applyRotateImage();
                    break;
                case MODE_TEXT:
                    mAddTextFragment.applyTextImage();
                    break;
                case MODE_PAINT:
                    mPaintFragment.savePaintImage();
                    break;

                default:
                    break;
            }
        }
    }

    private final class SaveBtnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (mOpTimes == 0) {
                onSaveTaskDone();
            } else {
                doSaveImage();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private final class SaveImageTask extends AsyncTask<Bitmap, Void, Boolean> {
        private Dialog dialog;

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            if (TextUtils.isEmpty(saveFilePath))
                return false;

            return BitmapUtils.saveBitmap(params[0], saveFilePath);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected void onCancelled(Boolean result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = EditImageActivity.getLoadingDialog(mContext, R.string.saving_image, false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result) {
                resetOpTimes();
                onSaveTaskDone();
            } else {
                Toast.makeText(mContext, R.string.save_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}