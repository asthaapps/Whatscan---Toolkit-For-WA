package com.whatscan.toolkit.forwa.Web;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import java.util.Locale;

public class ActivityWeb extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityWeb.this);
        setContentView(R.layout.activity_web);

        Utils.CheckConnection(ActivityWeb.this, findViewById(R.id.rl_web));
        FindView();
    }

    private void FindView() {
        web_view = findViewById(R.id.web_view);
        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        ImageView la_refresh = findViewById(R.id.la_refresh);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        RelativeLayout rl_web = findViewById(R.id.rl_web);
        View ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.whatsWeb) + "</small>"));

        if (Preference.getBooleanTheme(false)){
            setStatusBarTheme();
            rl_web.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }else {
            rl_web.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        SetWebView();

        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(v -> Constant.BottomSheetDialogView(ActivityWeb.this, getString(R.string.web_title), getString(R.string.web_text)));
        la_refresh.setOnClickListener(v -> SetWebView());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void SetWebView() {
        web_view.getSettings().setUseWideViewPort(true);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setSupportZoom(true);
        web_view.getSettings().setAllowFileAccess(true);
        web_view.getSettings().setAllowFileAccessFromFileURLs(true);
        web_view.getSettings().setAllowUniversalAccessFromFileURLs(true);
        web_view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_view.getSettings().setDomStorageEnabled(true);
        web_view.setWebChromeClient(new WebChromeClient());
        web_view.getSettings().setUserAgentString(Preference.getAgentString());
        web_view.loadUrl(Preference.getWebUrl() + Locale.getDefault().getLanguage());
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.wpText));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else if (Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.wpText));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        } else {
            window.clearFlags(0);
        }
    }

    public void setStatusBarTheme() {
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivityWeb.this, findViewById(R.id.rl_web), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}