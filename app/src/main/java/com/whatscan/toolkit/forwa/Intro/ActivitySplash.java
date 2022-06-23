package com.whatscan.toolkit.forwa.Intro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.whatscan.toolkit.forwa.ActivityHome;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

public class ActivitySplash extends AppCompatActivity implements OnSuccessListener<AppUpdateInfo> {

    public static final int REQUEST_CODE = 1234;
    public final String TAG = ActivitySplash.class.getSimpleName();
    public final int RC_APP_UPDATE = 100;
    public AppUpdateManager appUpdateManager;
    public boolean mNeedsFlexibleUpdate;
    public boolean UpdateAvailable = false;
    RelativeLayout rl_splash;
    TextView tv_version, tv_name,tv_name_1;
    Handler handler = new Handler();

    @Override
    protected void onStart() {
        super.onStart();
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateManager.registerListener(installStateUpdatedListener);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                UpdateAvailable = true;
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, ActivitySplash.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else {
                UpdateAvailable = false;
            }
        });
    }    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if (appUpdateManager != null) {
                    appUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            }
        }
    };



    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivitySplash.this);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_splash);
        rl_splash = findViewById(R.id.rl_splash);
        tv_version = findViewById(R.id.tv_version);
        tv_name = findViewById(R.id.tv_name);
        tv_name_1 = findViewById(R.id.tv_name_1);

        appUpdateManager = AppUpdateManagerFactory.create(ActivitySplash.this);


        if (isNetworkConnected()) {
            SetPref();
        } else {
            final Runnable r = new Runnable() {
                public void run() {
                    if (isNetworkConnected()) {
                        SetPref();
                    } else {
                        handler.postDelayed(this, 100);
                    }
                }
            };
            handler.postDelayed(r, 100);
            Toast.makeText(this, "Please check your internet connection.", Toast.LENGTH_LONG).show();
        }

        tv_version.setText(Html.fromHtml("App Version" + " : " + BuildConfig.VERSION_NAME));
        appUpdateManager = AppUpdateManagerFactory.create(ActivitySplash.this);
        mNeedsFlexibleUpdate = false;


        if (Preference.getBooleanTheme(false)) {
            String text = "<font color=#6DA188>Send</font> <font color=#FFFFFF>More,</font> <font color=#6DA188>Grow</font>  <font color=#FFFFFF>More</font>";
            tv_name_1.setText(Html.fromHtml(text));

        }else{
            String text = "<font color=#6DA188>Send</font> <font color=#000000>More,</font> <font color=#6DA188>Grow</font>  <font color=#000000>More</font>";
            tv_name_1.setText(Html.fromHtml(text));
        }



        if (Preference.getBooleanTheme(false)) {
            Window window = getWindow();
            tv_name.setTextColor(ContextCompat.getColor(ActivitySplash.this, R.color.colorWhite));
            tv_version.setTextColor(ContextCompat.getColor(ActivitySplash.this, R.color.colorWhite));
            rl_splash.setBackgroundColor(ContextCompat.getColor(ActivitySplash.this, R.color.darkBlack));
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        } else {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhite));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorWhite));
            rl_splash.setBackgroundColor(ContextCompat.getColor(ActivitySplash.this, R.color.colorWhite));
            tv_name.setTextColor(ContextCompat.getColor(ActivitySplash.this, R.color.colorBlack));
            tv_version.setTextColor(ContextCompat.getColor(ActivitySplash.this, R.color.colorBlack));
        }

        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        a.reset();
        tv_name.clearAnimation();
        tv_name.startAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void SetPref() {
        Preference.setMain_Url("https://asthatechnology.net/api/Whatstool/");

        Preference.setWebUrl("https://web.whatsapp.com/");
        Preference.setAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");

        Preference.setLogin("api/user_login.php");
        Preference.setRegister("api/user_register1.php");
        Preference.setFont_url("https://asthatechnology.net/api/Whatstool/uploads/font.zip");
        Preference.setFont_Changer("api/Magic_text/font_changer.php");
        Preference.setFont_Decor("api/Magic_text/font_decorative.php");
        Preference.setCat_Sticker("api/catagery_sticker.php");
        Preference.setCat_SubSticker("api/sticker_subcatagery.php");
        Preference.setCat_Emoticon("api/catagery_emoji.php");
        Preference.setCat_SubEmoticon("api/select_emoji.php");
        Preference.setCaption_Cat("api/caption_getcatagery.php");
        Preference.setCaption_SubCat("api/caption_getdata.php");
        Preference.setSticker_path("uploads/sticker_uploads/");
        Preference.setSticker_url("https://asthatechnology.net/api/Whatstool/uploads/jsonfile/");
        Preference.setReport_Request("api/report_issued.php");
        Preference.setNotification_status("api/checknotification_status.php");
        Preference.setRegister_token("api/registertoken_check.php");
        Preference.setCaption_path("uploads/caption_image/");

        Preference.setTermCondition("https://whatscan.co/terms-conditions/");
        Preference.setPrivacyPolicy("https://whatscan.co/privacy-policy/");

        Preference.setBulkDemo("https://youtu.be/pqd6DNp2HYc");
        Preference.setReportDemo("https://youtu.be/pqd6DNp2HYc");
        Preference.setChatNo("7203927842");

        Preference.setDev_insta("https://instagram.com/whats_tool");
        Preference.setDev_fb("https://www.facebook.com/Whatstool3141");
        Preference.setDev_twitter("https://twitter.com/whatstool");
        Preference.setDev_youtube("https://www.youtube.com/watch?v=pqd6DNp2HYc");
        Preference.setDev_mail("whatscantoolkit@gmail.com");

        Preference.setGBanner("ca-app-pub-4610744403189062/8389964984");
        Preference.setGFull("ca-app-pub-4610744403189062/2491056451");
        Preference.setGNative("ca-app-pub-4610744403189062/6693739934");
        Preference.setApp_Open("ca-app-pub-4610744403189062/7815249912");
        Preference.setRewardAds("ca-app-pub-4610744403189062/2371351545");

        Preference.setBase_key("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgECFgpxRi7yQfUgK8zyiW5TJllL1TYDGjPNyJ1vMeWAdIErC5ThAKe562Gvh9jPnw7sBtuXoyTIVoYvDnXTlASriP0Em29SEh1Xnz8LYIg58QVyFsoXJHvhe9Q4AA/1AcCphSXkcofu4tH0nLUDyjx5xomdHBGDWvZYTbX8ChNyk2lM39IIfzUhfnt60ZbhXMYuDvyFOmd7wqks1yeLh4Rhvjm9MsTOWa0Cyk4tidt+tiMcalvUIS6/SRhj+7Plc5PnDsL2vzMkvX7ZnAsQfH+8NumNBfopKM5r7wYp2JwRZKNpg9fqlbzwSOnA0s7xX4pzISlYZ2B1lE/fzN82XHQIDAQAB");
        Preference.setAds_name("g");


        Preference.setClassic_monthly_key("classic_monthly_plan");
        Preference.setClassic_yearly_key("classic_yearly_plan");
        Preference.setPremium_monthly_key("premium_monthly_plan");
        Preference.setPremium_yearly_key("premium_yearly_plan");
        Preference.setMaster_monthly_key("master_monthly_plan");
        Preference.setMaster_yearly_key("master_yearly_plan");
        Preference.setOffer_time_key("offer_time");
        Preference.setVip_key("vip_key");
        Preference.setVip_close_key("vip_close_key");


        Preference.setForgotResend("api/forgotpassword_resendotp.php");
        Preference.setForgotVerify("api/otp_verifyForgot.php");
        Preference.setForgotEmail("api/forgot_password.php");
        Preference.setChangePass("api/password_match.php");
        Preference.setRegisterVerify("api/otp_verify1.php");
        Preference.setRegisterResend("api/email_resendotp.php");

        Preference.setBuy_Back("api/verify_adds.php");
        Preference.setVerify_Plan("api/verify_plan.php");
        Preference.setCheck_Plan("api/check_plan.php");

        Preference.setCheck_coin("api/check_coin.php");
        Preference.setWatch_ad("api/watch_earn.php");
        Preference.setTool_use("api/tool_use.php");
        Preference.setInvitation_code("api/refered_code.php");
        Preference.setFirst_logIn("api/firsttimelogin.php");
        Preference.setCoin_history("api/transaction_history.php");
        Preference.setAdFree_coin("api/add_free.php");
        Preference.setAdFeaturs_coin("api/ads_feature.php");
        Preference.setCheckRedeem("api/check_reedem.php");
        Preference.setCheckRedemption("api/check_redemption.php");

        Advertisement.isShow = true;

        Advertisement.preLoadAds(ActivitySplash.this);


        boolean isFirstRun = getSharedPreferences("PREFERENCE3", MODE_PRIVATE).getBoolean("isFirstRunSplash", true);
        if (isFirstRun) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(ActivitySplash.this, ActivityHome.class);
                startActivity(intent);
                finish();
                getSharedPreferences("PREFERENCE3", MODE_PRIVATE).edit().putBoolean("isFirstRunSplash", false).apply();
            }, 1200);
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(ActivitySplash.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }, 1200);
        }
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
    public void onSuccess(AppUpdateInfo appUpdateInfo) {
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
            startUpdate(appUpdateInfo);
        } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackBarForCompleteUpdate();
        } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                mNeedsFlexibleUpdate = true;
                showFlexibleUpdateNotification();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    private void startUpdate(final AppUpdateInfo appUpdateInfo) {
        final Activity activity = this;
        new Thread(() -> {
            try {
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, activity, REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar snackbar = Snackbar.make(rl_splash, "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("INSTALL", view -> {
            if (appUpdateManager != null) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    private void showFlexibleUpdateNotification() {
        Snackbar snackbar = Snackbar.make(rl_splash, "An update is available and accessible in More.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                UpdateAvailable = false;
            }
        }
    }


}