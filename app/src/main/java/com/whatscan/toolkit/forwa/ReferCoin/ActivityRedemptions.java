package com.whatscan.toolkit.forwa.ReferCoin;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.CheckRedemption;
import com.whatscan.toolkit.forwa.GetSet.CheckRedemptionList;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityRedemptions extends AppCompatActivity {
    public RelativeLayout rlRedemption, ic_include, rlView;
    public ImageView la_back;
    public TextView tv_toolbar, txtNoData;
    public AppCompatButton btnRedeemNow;
    public RecyclerView rvRedemption;
    public RedemptionAdapter redemptionAdapter;
    public LottieAnimationView llEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityRedemptions.this);
        setContentView(R.layout.activity_redemption);

        rlRedemption = findViewById(R.id.rlRedemption);
        ic_include = findViewById(R.id.ic_include);
        rlView = findViewById(R.id.rlView);
        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        txtNoData = findViewById(R.id.txtNoData);
        rvRedemption = findViewById(R.id.rvRedemption);
        btnRedeemNow = findViewById(R.id.btnRedeemNow);
        llEmpty = findViewById(R.id.llEmpty);

        tv_toolbar.setText(Html.fromHtml("<small>" + "My Redemptions" + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlRedemption.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            rlView.setBackground(ContextCompat.getDrawable(this, R.drawable.dialog_black));
        }

        Constant.IntProgress(ActivityRedemptions.this);
        ChechRedemption();

        la_back.setOnClickListener(v -> onBackPressed());

        btnRedeemNow.setOnClickListener(v -> onBackPressed());
    }

    private void ChechRedemption() {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getCheckRedemption(Preference.getCheckRedemption(), Preference.getU_id()).enqueue(new Callback<CheckRedemption>() {
            @Override
            public void onResponse(@NonNull Call<CheckRedemption> call, @NonNull Response<CheckRedemption> response) {
                Constant.DismissProgress();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getFlag()) {
                        List<CheckRedemptionList> checkRedemptionLists = response.body().getData();
                        if (checkRedemptionLists.isEmpty()) {
                            llEmpty.setVisibility(View.VISIBLE);
                            rvRedemption.setVisibility(View.GONE);
                            btnRedeemNow.setVisibility(View.VISIBLE);
                        } else {
                            llEmpty.setVisibility(View.GONE);
                            rvRedemption.setVisibility(View.VISIBLE);
                            btnRedeemNow.setVisibility(View.GONE);
                            txtNoData.setVisibility(View.GONE);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityRedemptions.this, RecyclerView.VERTICAL, false);
                            rvRedemption.setLayoutManager(linearLayoutManager);
                            redemptionAdapter = new RedemptionAdapter(ActivityRedemptions.this, checkRedemptionLists);
                            rvRedemption.setAdapter(redemptionAdapter);
                            rvRedemption.setHasFixedSize(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckRedemption> call, @NonNull Throwable t) {
                Constant.DismissProgress();
                Log.i("CheckError", t.getMessage());
                Toast.makeText(ActivityRedemptions.this, "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setStatusBar() {
        Window window = getWindow();
        
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent));
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
        finish();
    }
}