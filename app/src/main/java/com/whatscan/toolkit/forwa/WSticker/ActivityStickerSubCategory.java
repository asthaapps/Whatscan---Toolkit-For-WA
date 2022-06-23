package com.whatscan.toolkit.forwa.WSticker;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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



import com.whatscan.toolkit.forwa.Adapter.MainStickerSubCategeryAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.StickerArray;
import com.whatscan.toolkit.forwa.GetSet.StickerData;
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

public class ActivityStickerSubCategory extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public List<StickerArray> stickerArrayLists;
    public RelativeLayout rlStickerSub;
    public TextView tv_toolbar;
    public String cat_name, sticker_id;
    public RecyclerView recy_sticker_sub;
    public LottieAnimationView  la_info;
    public ImageView la_back;
    public View ic_include;
    public MainStickerSubCategeryAdapter mainStickerSubCategeryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityStickerSubCategory.this);
        setContentView(R.layout.activity_sticker_sub_category);

        Utils.CheckConnection(ActivityStickerSubCategory.this, findViewById(R.id.rlStickerSub));


        FindView();
        GetMainStickerSubCategory();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityStickerSubCategory.this, ll_banner);

        rlStickerSub = findViewById(R.id.rlStickerSub);
        recy_sticker_sub = findViewById(R.id.recy_sticker_sub);
        la_back = findViewById(R.id.la_back);
        la_info = findViewById(R.id.la_info);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        ic_include = findViewById(R.id.ic_include);

        la_info.setVisibility(View.GONE);
        cat_name = getIntent().getStringExtra("Cat_Name");
        sticker_id = getIntent().getStringExtra("Sticker_Id");
        tv_toolbar.setText(Html.fromHtml("<small>" + cat_name + "</small>"));
        stickerArrayLists = new ArrayList<>();

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlStickerSub.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        } else {
            setStatusBar();
            rlStickerSub.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        la_back.setOnClickListener(v -> onBackPressed());

        Constant.IntProgress(ActivityStickerSubCategory.this);
    }

    private void GetMainStickerSubCategory() {
        if (stickerArrayLists.size() == 0) {
            Constant.ShowProgress();
        }
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getMainStickerSubCategory(Preference.getCat_SubSticker(), "com.astha.whats.scan", sticker_id).enqueue(new Callback<StickerData>() {
            @Override
            public void onResponse(@NotNull Call<StickerData> call, @NotNull Response<StickerData> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Constant.DismissProgress();
                    stickerArrayLists.addAll(response.body().getGetStickerArrays());
                    recy_sticker_sub.setLayoutManager(new LinearLayoutManager(ActivityStickerSubCategory.this, RecyclerView.VERTICAL, false));
                    mainStickerSubCategeryAdapter = new MainStickerSubCategeryAdapter(ActivityStickerSubCategory.this, stickerArrayLists);
                    recy_sticker_sub.setAdapter(mainStickerSubCategeryAdapter);
                } else {
                    Toast.makeText(ActivityStickerSubCategory.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<StickerData> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityStickerSubCategory.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
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
        super.onBackPressed();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivityStickerSubCategory.this, findViewById(R.id.rlStickerSub), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}