package com.whatscan.toolkit.forwa.MsgTools.Emoticon;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;



import com.whatscan.toolkit.forwa.Adapter.EmoticonCategoryAdapter;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.EmoticonCatagery;
import com.whatscan.toolkit.forwa.GetSet.EmoticonData;
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

public class ActivityEmoticon extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    List<EmoticonCatagery> emoticonCatageries = new ArrayList<>();
    RecyclerView recyEmoticon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityEmoticon.this);
        setContentView(R.layout.activity_emoticon);

        Utils.CheckConnection(ActivityEmoticon.this, findViewById(R.id.rl_emoticon));


        FindView();
        GetEmoticonCategory();
    }

    private void FindView() {
        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        RelativeLayout rl_emoticon = findViewById(R.id.rl_emoticon);
        View ic_include = findViewById(R.id.ic_include);
        recyEmoticon = findViewById(R.id.recyEmoticon);

        la_info.setVisibility(View.VISIBLE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getResources().getString(R.string.emoji_emoticon) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_emoticon.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }


        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(v -> Constant.BottomSheetDialogView(ActivityEmoticon.this, getString(R.string.text_emoji_header), getString(R.string.text_emoji_msg)));

        Constant.IntProgress(ActivityEmoticon.this);
    }

    private void GetEmoticonCategory() {
        if (emoticonCatageries.size() == 0) {
            Constant.ShowProgress();
        }
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getEmoticonCategory(Preference.getCat_Emoticon(), "com.astha.whats.scan").enqueue(new Callback<EmoticonData>() {
            @Override
            public void onResponse(@NotNull Call<EmoticonData> call, @NotNull Response<EmoticonData> response) {
                if (response.isSuccessful()) {
                    Constant.DismissProgress();
                    assert response.body() != null;
                    emoticonCatageries = response.body().getStickerCatagery();
                    recyEmoticon.setLayoutManager(new GridLayoutManager(ActivityEmoticon.this, 2));
                    EmoticonCategoryAdapter emoticonCategoryAdapter = new EmoticonCategoryAdapter(ActivityEmoticon.this, emoticonCatageries);
                    recyEmoticon.setAdapter(emoticonCategoryAdapter);
                } else {
                    Toast.makeText(ActivityEmoticon.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<EmoticonData> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityEmoticon.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
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
        Utils.showSnack(ActivityEmoticon.this, findViewById(R.id.rl_emoticon), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}