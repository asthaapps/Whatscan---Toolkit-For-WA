package com.whatscan.toolkit.forwa;

import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.PlanCheck;
import com.whatscan.toolkit.forwa.GetSet.ResponseLogin;
import com.whatscan.toolkit.forwa.Other.ActivityPermission;
import com.whatscan.toolkit.forwa.User.ActivityLogin;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smarteist.autoimageslider.SliderView;
import com.yariksoffice.lingver.Lingver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHome extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    ImageView ic_logo;
    TextView tv_text, tv_text1, tv_skip, tv_language, tv_started, tv_already;

    private GoogleSignInClient googleApiClient;
    private BottomSheetDialog bottomSheetDialog;
    int languageCode = 0;
    Integer[] SlideArray = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide1};


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityHome.this);
        setContentView(R.layout.activity_home);
        FirebaseApp.initializeApp(this);
        Utils.CheckConnection(ActivityHome.this, findViewById(R.id.cv_login));


        if (Preference.getAndroid_id().isEmpty()) {
            Preference.setAndroid_id(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        }
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(ActivityHome.this, Preference::setFcm_Id);

        FindView();
    }

    private void FindView() {
        ic_logo = findViewById(R.id.ic_logo);
        tv_text = findViewById(R.id.tv_text);
        tv_text1 = findViewById(R.id.tv_text1);
        tv_skip = findViewById(R.id.tv_skip);
        tv_language = findViewById(R.id.tv_language);
        tv_started = findViewById(R.id.tv_started);
        tv_already = findViewById(R.id.tv_already);
        SliderView imageSlider = findViewById(R.id.imageSlider);
        imageSlider.setSliderAdapter(new SliderAdapter(ActivityHome.this, SlideArray));

        tv_language.setOnClickListener(v -> OpenLanguage());

        SetLanguage();
        new Handler(Looper.getMainLooper()).postDelayed(() -> tv_skip.setVisibility(View.VISIBLE), 100);

        tv_skip.setOnClickListener(v -> {
            startActivity(new Intent(ActivityHome.this, ActivityMain.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            finish();
        });


        SpannableString SpanString = new SpannableString("Already have an account? Log in");

        ClickableSpan login = new ClickableSpan() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHome.this, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            }
        };


        SpanString.setSpan(new StyleSpan(Typeface.BOLD), 25, 31, 0);
        SpanString.setSpan(new UnderlineSpan(), 25, 31, 0);
        SpanString.setSpan(login, 25, 31, 0);
        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 25, 31, 0);

        tv_already.setMovementMethod(LinkMovementMethod.getInstance());
        tv_already.setText(SpanString, TextView.BufferType.SPANNABLE);
        tv_already.setSelected(true);

        Constant.IntProgress(ActivityHome.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        googleApiClient = GoogleSignIn.getClient(this, gso);

        tv_started.setOnClickListener(v -> {
            Intent signInIntent = googleApiClient.getSignInIntent();
            startActivityForResult(signInIntent, 9001);
        });
    }

    @SuppressLint("SetTextI18n")
    private void SetLanguage() {
        switch (Preference.getCheckActiveLanguage()) {
            case "hi":
                tv_language.setText("हिंदी");
                break;
            case "gu":
                tv_language.setText("ગુજરાતી");
                break;
            case "te":
                tv_language.setText("తెలుగు");
                break;
            case "es":
                tv_language.setText("Española");
                break;
            case "pt":
                tv_language.setText("português");
                break;
            case "fr":
                tv_language.setText("français");
                break;
            case "bn":
                tv_language.setText("বাংলা");
                break;
            case "mr":
                tv_language.setText("मराठी");
                break;
            case "tr":
                tv_language.setText("Türki");
                break;
            case "de":
                tv_language.setText("Deutsche");
                break;
            case "en":
            default:
                tv_language.setText("English");
                break;
        }
    }

    private void OpenLanguage() {
        bottomSheetDialog = new BottomSheetDialog(ActivityHome.this);
        View inflate = LayoutInflater.from(ActivityHome.this).inflate(R.layout.dialog_language, null);
        bottomSheetDialog.setContentView(inflate);

        LinearLayout llEnglish = inflate.findViewById(R.id.llEnglish);
        LinearLayout llHindi = inflate.findViewById(R.id.llHindi);
        LinearLayout llGujarati = inflate.findViewById(R.id.llGujarati);
        LinearLayout llTelugu = inflate.findViewById(R.id.llTelugu);
        LinearLayout llSpanish = inflate.findViewById(R.id.llSpanish);
        LinearLayout llPortuguese = inflate.findViewById(R.id.llPortuguese);
        LinearLayout llFrench = inflate.findViewById(R.id.llFrench);
        LinearLayout llBengali = inflate.findViewById(R.id.llBengali);
        LinearLayout llMarathi = inflate.findViewById(R.id.llMarathi);
        LinearLayout llTurkish = inflate.findViewById(R.id.llTurkish);
        LinearLayout llGerman = inflate.findViewById(R.id.llGerman);

        switch (Preference.getCheckActiveLanguage()) {
            case "hi":
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
            case "gu":
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
            case "te":
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
            case "es":
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
            case "pt":
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
            case "fr":
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
            case "bn":
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
            case "mr":
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
            case "tr":
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
            case "de":
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
            default:
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                break;
        }


        llEnglish.setOnClickListener(v -> {
            languageCode = 1;
            Lingver.getInstance().setLocale(ActivityHome.this, "en");
            Preference.setCheckActiveLanguage("en");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            SetLanguage();
            bottomSheetDialog.dismiss();
        });

        llHindi.setOnClickListener(v -> {
            languageCode = 2;
            Lingver.getInstance().setLocale(ActivityHome.this, "hi");
            Preference.setCheckActiveLanguage("hi");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            SetLanguage();
            bottomSheetDialog.dismiss();
        });

        llGujarati.setOnClickListener(v -> {
            languageCode = 3;
            Lingver.getInstance().setLocale(ActivityHome.this, "gu");
            Preference.setCheckActiveLanguage("gu");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            SetLanguage();
            bottomSheetDialog.dismiss();
        });

        llTelugu.setOnClickListener(v -> {
            languageCode = 4;
            Lingver.getInstance().setLocale(ActivityHome.this, "te");
            Preference.setCheckActiveLanguage("te");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            SetLanguage();
            bottomSheetDialog.dismiss();
        });

        llSpanish.setOnClickListener(v -> {
            languageCode = 5;
            Lingver.getInstance().setLocale(ActivityHome.this, "es");
            Preference.setCheckActiveLanguage("es");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            SetLanguage();
            bottomSheetDialog.dismiss();
        });

        llPortuguese.setOnClickListener(v -> {
            languageCode = 6;
            Lingver.getInstance().setLocale(ActivityHome.this, "pt");
            Preference.setCheckActiveLanguage("pt");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            SetLanguage();
            bottomSheetDialog.dismiss();
        });

        llFrench.setOnClickListener(v -> {
            languageCode = 7;
            Lingver.getInstance().setLocale(ActivityHome.this, "fr");
            Preference.setCheckActiveLanguage("fr");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            SetLanguage();
            bottomSheetDialog.dismiss();
        });

        llBengali.setOnClickListener(v -> {
            languageCode = 8;
            Lingver.getInstance().setLocale(ActivityHome.this, "bn");
            Preference.setCheckActiveLanguage("bn");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            SetLanguage();
            bottomSheetDialog.dismiss();
        });

        llMarathi.setOnClickListener(v -> {
            languageCode = 9;
            Lingver.getInstance().setLocale(ActivityHome.this, "mr");
            Preference.setCheckActiveLanguage("mr");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            SetLanguage();
            bottomSheetDialog.dismiss();
        });

        llTurkish.setOnClickListener(v -> {
            languageCode = 10;
            Lingver.getInstance().setLocale(ActivityHome.this, "tr");
            Preference.setCheckActiveLanguage("tr");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            SetLanguage();
            bottomSheetDialog.dismiss();
        });

        llGerman.setOnClickListener(v -> {
            languageCode = 11;
            Lingver.getInstance().setLocale(ActivityHome.this, "de");
            Preference.setCheckActiveLanguage("de");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language));
            }
            llGerman.setBackground(ContextCompat.getDrawable(ActivityHome.this, R.drawable.bg_language_select));
            SetLanguage();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        GoogleSignInAccount acct = result.getSignInAccount();
        updateUI(acct);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            Preference.setEmail(account.getEmail());
            Preference.setGoogle_key(account.getId());
            Preference.setUserName(account.getDisplayName());
            Preference.setProfile(String.valueOf(account.getPhotoUrl()));
            ApiLogin();
        }
    }

    private void ApiLogin() {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getLogin(Preference.getLogin(), Preference.getUserName(), Preference.getEmail(), "+91", Preference.getPassword(), Preference.getAndroid_id(), "", Preference.getFcm_Id(), "google", Preference.getGoogle_key(), Preference.getFacebook_key()).enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(@NotNull Call<ResponseLogin> call, @NotNull Response<ResponseLogin> response) {
                if (response.isSuccessful()) {
                    Constant.DismissProgress();
                    assert response.body() != null;
                    if (response.body().getFlag().equals("true")) {
                        Preference.setUserName(response.body().getUsername());
                        Preference.setU_id(response.body().getU_id());
                        Preference.setEmail(response.body().getEmail());
                        Preference.setFcm_Id(response.body().getFcm_id());
                        Preference.setAndroid_id(response.body().getAndroid_id());
                        Preference.setGoogle_key(response.body().getGoogle_key());
                        Preference.setFacebook_key(response.body().getFacebook_key());
                        Preference.setType(response.body().getType());
                        Preference.setToken(response.body().getRegister_token());
                        Preference.setTool_id(response.body().getTool_id());
                        Preference.setCheck_Notification(response.body().getNotification_status());
                        Preference.setReferedCode(response.body().getRefered_number());
                        Preference.setTotal_coin(response.body().getCoins());

                        Preference.setLogin_status("Yes");
                        setAutoLogAppEventsEnabled(true);
                        AppEventsLogger logger = AppEventsLogger.newLogger(ActivityHome.this);
                        Bundle params = new Bundle();
                        params.putString(AppEventsConstants.EVENT_NAME_ACTIVATED_APP, "Login");
                        logger.logEvent("User_Login",  params);


                        Intent intent = new Intent(ActivityHome.this, ActivityMain.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Constant.DismissProgress();
                        Toast.makeText(ActivityHome.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseLogin> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityHome.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetStatusBar() {
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
        Utils.showSnack(ActivityHome.this, findViewById(R.id.cv_login), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}