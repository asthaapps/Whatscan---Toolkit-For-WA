package com.whatscan.toolkit.forwa.MsgTools.Caption;

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
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.CaptionSubCategory;
import com.whatscan.toolkit.forwa.GetSet.CaptionSubData;
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

public class ActivitySubCaption extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public String name, captionID;
    public RecyclerView simplerecyView;
    public LottieAnimationView  la_up, la_info;
    public ImageView la_back;
    public CaptionSubStatusAdapter captionSubStatusAdapter;
    public List<CaptionSubCategory> getCaptionsubData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivitySubCaption.this);
        setContentView(R.layout.activity_sub_caption);

        Utils.CheckConnection(ActivitySubCaption.this, findViewById(R.id.rl_sub_caption));


        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivitySubCaption.this, ll_banner);


        la_back = findViewById(R.id.la_back);
        la_up = findViewById(R.id.la_up);
        la_info = findViewById(R.id.la_info);
        la_info.setVisibility(View.GONE);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        RelativeLayout rl_sub_caption = findViewById(R.id.rl_sub_caption);
        View ic_include = findViewById(R.id.ic_include);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_sub_caption.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }

        name = getIntent().getStringExtra("image");
        captionID = getIntent().getStringExtra("caption_id");

        Constant.IntProgress(ActivitySubCaption.this);
        getCaptionsubData = new ArrayList<>();

        tv_toolbar.setText(Html.fromHtml("<small>" + name + "</small>"));

        la_up.setOnClickListener(view -> {
            simplerecyView.smoothScrollToPosition(0);
            la_up.setVisibility(View.GONE);
        });

        la_back.setOnClickListener(v -> onBackPressed());


        simplerecyView = findViewById(R.id.simplerecyView);
        simplerecyView.setLayoutManager(new LinearLayoutManager(ActivitySubCaption.this, RecyclerView.VERTICAL, false));

        GetSubCateData();
    }

    private void GetSubCateData() {
        if (getCaptionsubData.size() == 0) {
            Constant.ShowProgress();
        }
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getCaptionSubCategory(Preference.getCaption_SubCat(), captionID).enqueue(new Callback<CaptionSubData>() {
            @Override
            public void onResponse(@NotNull Call<CaptionSubData> call, @NotNull Response<CaptionSubData> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Constant.DismissProgress();
                    getCaptionsubData = response.body().getCaptionData();
                    captionSubStatusAdapter = new CaptionSubStatusAdapter(getApplicationContext(), getCaptionsubData, la_up);
                    simplerecyView.setAdapter(captionSubStatusAdapter);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CaptionSubData> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivitySubCaption.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
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
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivitySubCaption.this, findViewById(R.id.rl_sub_caption), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}