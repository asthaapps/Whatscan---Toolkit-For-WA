package com.whatscan.toolkit.forwa.ReferCoin;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.TransactionData;
import com.whatscan.toolkit.forwa.GetSet.TransactionSubData;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.onLoginCoinEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCoinHistory extends AppCompatActivity implements onLoginCoinEventListener {
    public RelativeLayout rlHistory;
    public RecyclerView rvMainCategory;
    public TransactionAdapter transactionAdapter;
    public ImageView la_back, imgBanner;
    public TextView tv_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityCoinHistory.this);
        setContentView(R.layout.activity_coin_history);

        rlHistory = findViewById(R.id.rlHistory);
        rvMainCategory = findViewById(R.id.rvTransactionHistory);
        la_back = findViewById(R.id.la_back);
        imgBanner = findViewById(R.id.imgBanner);
        tv_toolbar = findViewById(R.id.tv_toolbar);

        tv_toolbar.setText(Html.fromHtml("<small>" + "Transaction History" + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlHistory.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            imgBanner.setImageResource(R.drawable.coin_banner_w);
            rvMainCategory.setBackground(ContextCompat.getDrawable(this, R.drawable.dialog_black));
        }

        Constant.IntProgress(ActivityCoinHistory.this);

        la_back.setOnClickListener(v -> onBackPressed());

        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getTransactionData(Preference.getCoin_history(), Preference.getU_id()).enqueue(new Callback<TransactionData>() {
            @Override
            public void onResponse(@NotNull Call<TransactionData> call, @NotNull Response<TransactionData> response) {
                if (response.isSuccessful()) {
                    Constant.DismissProgress();
                    assert response.body() != null;
                    Constant.DismissProgress();
                    if (response.body().getFlag()) {
                        List<TransactionSubData> transactionSubDataList = response.body().getTransactionSubList();
                        rvMainCategory.setLayoutManager(new LinearLayoutManager(ActivityCoinHistory.this, RecyclerView.VERTICAL, false));
                        transactionAdapter = new TransactionAdapter(ActivityCoinHistory.this, transactionSubDataList);
                        rvMainCategory.setAdapter(transactionAdapter);
                    } else {
                        Toast.makeText(ActivityCoinHistory.this, "flag false..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityCoinHistory.this, "response failed..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<TransactionData> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityCoinHistory.this, "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
    public void coinEvent(String s) {
        ((TextView) findViewById(R.id.tvTotalCoin)).setText(s);
    }
}