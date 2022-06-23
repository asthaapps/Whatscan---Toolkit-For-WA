package com.whatscan.toolkit.forwa.WSticker;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;



import com.whatscan.toolkit.forwa.Adapter.MainStickerCategeryAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.GetSet.MainStickerCategery;
import com.whatscan.toolkit.forwa.GetSet.StickerCatagery;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySticker extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private List<StickerCatagery> getMainSticker = new ArrayList<>();
    private RecyclerView recy_sticker_main;
    private MainStickerCategeryAdapter mainStickerCategeryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivitySticker.this);
        setContentView(R.layout.activity_sticker);

        Utils.CheckConnection(ActivitySticker.this, findViewById(R.id.rlSticker));


        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivitySticker.this, ll_banner);

        RelativeLayout rlSticker = findViewById(R.id.rlSticker);
        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        recy_sticker_main = findViewById(R.id.recy_sticker_main);
        View ic_include = findViewById(R.id.ic_include);

        la_info.setVisibility(View.VISIBLE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getResources().getString(R.string.stickers) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlSticker.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        } else {
            setStatusBar();
            rlSticker.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(v -> Constant.BottomSheetDialogView(ActivitySticker.this, getString(R.string.caption_status), getString(R.string.text_emoji_msg)));

        Constant.IntProgress(ActivitySticker.this);

        GetMainStickerCategory();
    }

    private void GetMainStickerCategory() {
        if (getMainSticker.size() == 0) {
            Constant.ShowProgress();
        }
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getMainStickerCategory(Preference.getCat_Sticker(), "com.astha.whats.scan", BuildConfig.VERSION_CODE).enqueue(new Callback<MainStickerCategery>() {
            @Override
            public void onResponse(@NotNull Call<MainStickerCategery> call, @NotNull Response<MainStickerCategery> response) {
                if (response.isSuccessful()) {
                    Constant.DismissProgress();
                    assert response.body() != null;
                    getMainSticker = response.body().getStickerCatagery();
                    recy_sticker_main.setLayoutManager(new LinearLayoutManager(ActivitySticker.this, RecyclerView.VERTICAL, false));
                    mainStickerCategeryAdapter = new MainStickerCategeryAdapter(ActivitySticker.this, getMainSticker);
                    recy_sticker_main.setAdapter(mainStickerCategeryAdapter);
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainStickerCategery> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivitySticker.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivitySticker.this, findViewById(R.id.rlSticker), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}