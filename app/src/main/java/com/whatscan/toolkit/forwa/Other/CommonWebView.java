package com.whatscan.toolkit.forwa.Other;

import static androidx.browser.customtabs.CustomTabsIntent.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

public class CommonWebView extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public RelativeLayout rlWebView, rlToolbar;
    public ProgressBar progressBar;
    public WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), CommonWebView.this);
        setContentView(R.layout.activity_common_web_view);

        Utils.CheckConnection(CommonWebView.this, findViewById(R.id.rlWebView));

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        View ic_include = findViewById(R.id.ic_include);
        rlWebView = findViewById(R.id.rlWebView);
        rlToolbar = findViewById(R.id.rlToolbar);
        browser = findViewById(R.id.browser);
        progressBar = findViewById(R.id.progressBar);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlWebView.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tv_toolbar.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rlWebView.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_color));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_color));
            tv_toolbar.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        boolean termCondition = getIntent().getBooleanExtra("tc", false);
        boolean pPolicy = getIntent().getBooleanExtra("pp", false);



        browser.setSoundEffectsEnabled(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        browser.getSettings().setGeolocationEnabled(true);
        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setAllowContentAccess(true);
        browser.getSettings().setDatabaseEnabled(true);
        browser.getSettings().setLoadsImagesAutomatically(true);
        CookieManager.getInstance().setAcceptCookie(true);
        browser.setBackgroundColor(Color.argb(1,0,0,0));
        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(browser, true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            browser.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            browser.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            browser.getSettings().setAllowFileAccessFromFileURLs(true);
            browser.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(false);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            browser.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            browser.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        browser.getSettings().setAllowFileAccess(true);
        browser.setHapticFeedbackEnabled(false);
        if (termCondition) {
            tv_toolbar.setText(Html.fromHtml("<small>" + "Term & Condition" + "</small>"));
            browser.setWebViewClient(new WebViewClient());
            browser.loadUrl(Preference.getTermCondition());
        } else if (pPolicy) {
            tv_toolbar.setText(Html.fromHtml("<small>" + "Privacy Policy" + "</small>"));
            browser.setWebViewClient(new WebViewClient());
            browser.loadUrl(Preference.getPrivacyPolicy());
        }

        la_back.setOnClickListener(v -> onBackPressed());
    }

    public void setStatusBarTheme() {
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darkBlack));
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(CommonWebView.this, findViewById(R.id.rlWebView), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            browser.setVisibility(View.VISIBLE);
        }
    }
}