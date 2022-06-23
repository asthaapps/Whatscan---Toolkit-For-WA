package com.whatscan.toolkit.forwa.WSticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.StickerPackListAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.GetSet.StickerArray;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ActivityStickerMakerList extends AppCompatActivity {
    ArrayList<StickerArray> stickerPackList = new ArrayList<>();
    RelativeLayout rlStickerMaker;
    RecyclerView sticker_pack_list;
    LinearLayout emptyLayout;
    StickerPackListAdapter allStickerPacksListAdapter;
    LinearLayoutManager packLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityStickerMakerList.this);
        setContentView(R.layout.activity_sticker_maker_list);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityStickerMakerList.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        RelativeLayout rl_create_new_pack = findViewById(R.id.rl_create_new_pack);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        View ic_include = findViewById(R.id.ic_include);
        sticker_pack_list = findViewById(R.id.sticker_pack_list);
        emptyLayout = findViewById(R.id.emptyLayout);
        rlStickerMaker = findViewById(R.id.rlStickerMaker);

        la_info.setVisibility(View.INVISIBLE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.sticker_maker) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlStickerMaker.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        } else {
            setStatusBar();
            rlStickerMaker.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        rl_create_new_pack.setOnClickListener(v -> addNewStickerPack());
        la_back.setOnClickListener(v -> onBackPressed());

        showStickerPackList();
    }

    private void addNewStickerPack() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityStickerMakerList.this);
        View inflate = LayoutInflater.from(ActivityStickerMakerList.this).inflate(R.layout.dialog_sticker_pack, null);
        bottomSheetDialog.setContentView(inflate);

        RelativeLayout rlCreateSticker = inflate.findViewById(R.id.rlCreateSticker);
        TextInputLayout text_input1 = inflate.findViewById(R.id.text_input1);
        TextInputLayout text_input2 = inflate.findViewById(R.id.text_input2);
        EditText pack_name = inflate.findViewById(R.id.pack_name);
        EditText creator_name = inflate.findViewById(R.id.creator_name);
        TextView txt_header = inflate.findViewById(R.id.txt_header);
        TextView txt_msg = inflate.findViewById(R.id.txt_msg);
        TextView txt_ok = inflate.findViewById(R.id.txt_ok);
        TextView txt_cancel = inflate.findViewById(R.id.txt_cancel);

        if (Preference.getBooleanTheme(false)) {
            rlCreateSticker.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txt_header.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txt_msg.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            pack_name.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            creator_name.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            setTextInputLayoutHintColor(text_input1, this, R.color.colorWhite);
            setTextInputLayoutHintColor(text_input2, this, R.color.colorWhite);
        }

        txt_cancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        txt_ok.setOnClickListener(v -> {
            if (TextUtils.isEmpty(pack_name.getText())) {
                pack_name.setError("Pack name is required!");
            }
            if (TextUtils.isEmpty(creator_name.getText())) {
                creator_name.setError("Creator is required!");
            }
            if (!TextUtils.isEmpty(pack_name.getText()) && !TextUtils.isEmpty(creator_name.getText())) {
                String newName = pack_name.getText().toString();
                String newCreator = creator_name.getText().toString();
                Intent intent = new Intent(ActivityStickerMakerList.this, ActivityCustomStickerMaker.class);
                intent.putExtra("PackName", newName);
                intent.putExtra("CreatorName", newCreator);
                startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void setTextInputLayoutHintColor(TextInputLayout text_input1, Context context, int colorBlack) {
        text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, colorBlack)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        showStickerPackList();
    }

    @SuppressLint("WrongConstant")
    public void showStickerPackList() {
        if (AppController.tinyDB != null) {
            stickerPackList = AppController.tinyDB.getListAdVideo("arrayAdVideo", StickerArray.class);
        }

        if (stickerPackList.size() == 0) {
            sticker_pack_list.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            sticker_pack_list.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }

        packLayoutManager = new LinearLayoutManager(ActivityStickerMakerList.this, 1, false);
        allStickerPacksListAdapter = new StickerPackListAdapter(ActivityStickerMakerList.this, stickerPackList);
        sticker_pack_list.setLayoutManager(packLayoutManager);
        sticker_pack_list.setAdapter(allStickerPacksListAdapter);
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
        finish();
    }
}