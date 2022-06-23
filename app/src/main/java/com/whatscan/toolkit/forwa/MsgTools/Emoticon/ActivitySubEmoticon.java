package com.whatscan.toolkit.forwa.MsgTools.Emoticon;

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

import com.whatscan.toolkit.forwa.Adapter.EmoticonSubCategoryAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.EmoticonSubCatagery;
import com.whatscan.toolkit.forwa.GetSet.EmoticonSubData;
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

public class ActivitySubEmoticon extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    List<EmoticonSubCatagery> emoticonSubCatageries = new ArrayList<>();
    RecyclerView recySubEmoticon;
    String cat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivitySubEmoticon.this);
        setContentView(R.layout.activity_sub_emoticon);

        Utils.CheckConnection(ActivitySubEmoticon.this, findViewById(R.id.rl_sub_emoticon));

        FindView();
        GetEmoticonSubCategory();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivitySubEmoticon.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        RelativeLayout rl_sub_emoticon = findViewById(R.id.rl_sub_emoticon);
        View ic_include = findViewById(R.id.ic_include);
        recySubEmoticon = findViewById(R.id.recySubEmoticon);

        la_info.setVisibility(View.GONE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getIntent().getStringExtra("EmoticonName") + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_sub_emoticon.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }

        cat_id = getIntent().getStringExtra("EmoticonId");

        la_back.setOnClickListener(v -> onBackPressed());

        Constant.IntProgress(ActivitySubEmoticon.this);
    }

    private void GetEmoticonSubCategory() {
        if (emoticonSubCatageries.size() == 0) {
            Constant.ShowProgress();
        }
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getEmoticonSubCategory(Preference.getCat_SubEmoticon(), "com.astha.whats.scan", cat_id).enqueue(new Callback<EmoticonSubData>() {
            @Override
            public void onResponse(@NotNull Call<EmoticonSubData> call, @NotNull Response<EmoticonSubData> response) {
                if (response.isSuccessful()) {
                    Constant.DismissProgress();
                    assert response.body() != null;
                    emoticonSubCatageries = response.body().getCatageryArray();
                    recySubEmoticon.setLayoutManager(new LinearLayoutManager(ActivitySubEmoticon.this, RecyclerView.VERTICAL, false));
                    EmoticonSubCategoryAdapter emoticonSubCategoryAdapter = new EmoticonSubCategoryAdapter(ActivitySubEmoticon.this, emoticonSubCatageries);
                    recySubEmoticon.setAdapter(emoticonSubCategoryAdapter);
                } else {
                    Toast.makeText(ActivitySubEmoticon.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<EmoticonSubData> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivitySubEmoticon.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
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
        finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivitySubEmoticon.this, findViewById(R.id.rl_sub_emoticon), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}