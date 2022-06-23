package com.whatscan.toolkit.forwa.WSticker.editimage.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.WSticker.editimage.BaseActivity;
import com.whatscan.toolkit.forwa.WSticker.editimage.EditImageActivity;
import com.whatscan.toolkit.forwa.WSticker.editimage.ModuleConfig;
import com.whatscan.toolkit.forwa.WSticker.editimage.model.RatioItem;
import com.whatscan.toolkit.forwa.WSticker.editimage.utils.Matrix3;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.CropImageView;
import com.whatscan.toolkit.forwa.WSticker.editimage.view.imagezoom.ImageViewTouchBase;

import java.util.ArrayList;
import java.util.List;

public class CropFragment extends BaseEditFragment {
    public static final int INDEX = ModuleConfig.INDEX_CROP;
    public static final String TAG = CropFragment.class.getName();
    public static int SELECTED_COLOR = Color.YELLOW;
    public static int UNSELECTED_COLOR = Color.BLACK;
    private static List<RatioItem> dataList = new ArrayList<RatioItem>();

    static {
        dataList.add(new RatioItem("none", -1f));
        dataList.add(new RatioItem("1:1", 1f));
        dataList.add(new RatioItem("1:2", 1 / 2f));
        dataList.add(new RatioItem("1:3", 1 / 3f));
        dataList.add(new RatioItem("2:3", 2 / 3f));
        dataList.add(new RatioItem("3:4", 3 / 4f));
        dataList.add(new RatioItem("2:1", 2f));
        dataList.add(new RatioItem("3:1", 3f));
        dataList.add(new RatioItem("3:2", 3 / 2f));
        dataList.add(new RatioItem("4:3", 4 / 3f));
    }

    public CropImageView mCropPanel;
    public TextView selctedTextView;
    private View mainView;
    private View backToMenu;
    private LinearLayout ratioList;
    private List<TextView> textViewList = new ArrayList<TextView>();
    private CropRationClick mCropRationClick = new CropRationClick();

    public static CropFragment newInstance() {
        CropFragment fragment = new CropFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_crop, null);
        return mainView;
    }

    private void setUpRatioList() {
        ratioList.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.leftMargin = 20;
        params.rightMargin = 20;
        for (int i = 0, len = dataList.size(); i < len; i++) {
            TextView text = new TextView(activity);
            text.setTextColor(UNSELECTED_COLOR);
            text.setTextSize(20);
            text.setText(dataList.get(i).getText());
            textViewList.add(text);
            ratioList.addView(text, params);
            text.setTag(i);
            if (i == 0) {
                selctedTextView = text;
            }
            dataList.get(i).setIndex(i);
            text.setTag(dataList.get(i));
            text.setOnClickListener(mCropRationClick);
        }
        selctedTextView.setTextColor(SELECTED_COLOR);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backToMenu = mainView.findViewById(R.id.back_to_main);
        ratioList = (LinearLayout) mainView.findViewById(R.id.ratio_list_group);
        setUpRatioList();
        this.mCropPanel = ensureEditActivity().mCropPanel;
        backToMenu.setOnClickListener(new BackToMenuClick());
    }

    @Override
    public void onShow() {
        activity.mode = EditImageActivity.MODE_CROP;
        activity.mCropPanel.setVisibility(View.VISIBLE);
        activity.mainImage.setImageBitmap(activity.getMainBit());
        activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        activity.mainImage.setScaleEnabled(false);
        activity.bannerFlipper.showNext();
        activity.mainImage.post(new Runnable() {
            @Override
            public void run() {
                final RectF r = activity.mainImage.getBitmapRect();
                activity.mCropPanel.setCropRect(r);
            }
        });
    }

    @Override
    public void backToMain() {
        activity.mode = EditImageActivity.MODE_NONE;
        mCropPanel.setVisibility(View.GONE);
        activity.mainImage.setScaleEnabled(true);
        activity.bottomGallery.setCurrentItem(0);
        if (selctedTextView != null) {
            selctedTextView.setTextColor(UNSELECTED_COLOR);
        }
        mCropPanel.setRatioCropRect(activity.mainImage.getBitmapRect(), -1);
        activity.bannerFlipper.showPrevious();
    }

    public void applyCropImage() {
        CropImageTask task = new CropImageTask();
        task.execute(activity.getMainBit());
    }

    private final class CropRationClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            TextView curTextView = (TextView) v;
            selctedTextView.setTextColor(UNSELECTED_COLOR);
            RatioItem dataItem = (RatioItem) v.getTag();
            selctedTextView = curTextView;
            selctedTextView.setTextColor(SELECTED_COLOR);
            mCropPanel.setRatioCropRect(activity.mainImage.getBitmapRect(), dataItem.getRatio());
        }
    }

    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }

    private final class CropImageTask extends AsyncTask<Bitmap, Void, Bitmap> {
        private Dialog dialog;

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = BaseActivity.getLoadingDialog(getActivity(), R.string.saving_image, false);
            dialog.show();
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            RectF cropRect = mCropPanel.getCropRect();
            Matrix touchMatrix = activity.mainImage.getImageViewMatrix();
            float[] data = new float[9];
            touchMatrix.getValues(data);
            Matrix3 cal = new Matrix3(data);
            Matrix3 inverseMatrix = cal.inverseMatrix();
            Matrix m = new Matrix();
            m.setValues(inverseMatrix.getValues());
            m.mapRect(cropRect);

            Bitmap resultBit = Bitmap.createBitmap(params[0],
                    (int) cropRect.left, (int) cropRect.top,
                    (int) cropRect.width(), (int) cropRect.height());
            return resultBit;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result == null)
                return;

            activity.changeMainBitmap(result, true);
            activity.mCropPanel.setCropRect(activity.mainImage.getBitmapRect());
            backToMain();
        }
    }
}