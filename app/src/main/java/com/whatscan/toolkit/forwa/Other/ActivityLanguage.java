package com.whatscan.toolkit.forwa.Other;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.yariksoffice.lingver.Lingver;

public class ActivityLanguage extends AppCompatActivity {
    public LinearLayout llEnglish, llHindi, llGujarati, llTelugu, llSpanish, llPortuguese, llFrench, llBengali, llMarathi, llTurkish, llGerman;
    public RelativeLayout rlLanguage, rlContinue;
    public ImageView la_back;
    public TextView txt_term, txtLanguage, txtLanguageD;
    public int languageCode = 0;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityLanguage.this);
        setContentView(R.layout.activity_language);

        la_back = findViewById(R.id.la_back);
        rlLanguage = findViewById(R.id.rlLanguage);
        rlContinue = findViewById(R.id.rlContinue);
        llEnglish = findViewById(R.id.llEnglish);
        llHindi = findViewById(R.id.llHindi);
        llGujarati = findViewById(R.id.llGujarati);
        llTelugu = findViewById(R.id.llTelugu);
        llSpanish = findViewById(R.id.llSpanish);
        llPortuguese = findViewById(R.id.llPortuguese);
        llFrench = findViewById(R.id.llFrench);
        llBengali = findViewById(R.id.llBengali);
        llMarathi = findViewById(R.id.llMarathi);
        llTurkish = findViewById(R.id.llTurkish);
        llGerman = findViewById(R.id.llGerman);
        txt_term = findViewById(R.id.txt_term);
        txtLanguage = findViewById(R.id.txtLanguage);
        txtLanguageD = findViewById(R.id.txtLanguageD);

        TextView txtOne = findViewById(R.id.txtOne);
        TextView txtTwo = findViewById(R.id.txtTwo);
        TextView txtThree = findViewById(R.id.txtThree);
        TextView txtFour = findViewById(R.id.txtFour);
        TextView txtFive = findViewById(R.id.txtFive);
        TextView txtSix = findViewById(R.id.txtSix);
        TextView txtSeven = findViewById(R.id.txtSeven);
        TextView txtEight = findViewById(R.id.txtEight);
        TextView txtNine = findViewById(R.id.txtNine);
        TextView txtTen = findViewById(R.id.txtTen);
        TextView txtEleven = findViewById(R.id.txtEleven);


        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlLanguage.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtLanguage.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtLanguageD.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txt_term.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            llEnglish.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            llHindi.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            llGujarati.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            llTelugu.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            llSpanish.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            llPortuguese.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            llFrench.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            llBengali.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            llMarathi.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            llTurkish.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            llGerman.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        SpannableString SpanString = new SpannableString("By Continuing you agree to the Terms of Use and Privacy Policy");

        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(ActivityLanguage.this, CommonWebView.class);
                mIntent.putExtra("tc", true);
                startActivity(mIntent);
            }
        };

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(ActivityLanguage.this, CommonWebView.class);
                mIntent.putExtra("pp", true);
                startActivity(mIntent);
            }
        };

        SpanString.setSpan(teremsAndCondition, 31, 43, 0);
        SpanString.setSpan(privacy, 48, 62, 0);
        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 31, 43, 0);
        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 48, 62, 0);
        SpanString.setSpan(new UnderlineSpan(), 31, 43, 0);
        SpanString.setSpan(new UnderlineSpan(), 48, 62, 0);

        txt_term.setMovementMethod(LinkMovementMethod.getInstance());
        txt_term.setText(SpanString, TextView.BufferType.SPANNABLE);
        txt_term.setSelected(true);

        switch (Preference.getCheckActiveLanguage()) {
            case "hi":
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtTwo.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
            case "gu":
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtThree.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
            case "te":
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtFour.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
            case "es":
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtFive.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
            case "pt":
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtSix.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
            case "fr":
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtSeven.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
            case "bn":
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtEight.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
            case "mr":
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtNine.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
            case "tr":
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtTen.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
            case "de":
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtEleven.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
            default:
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                if (Preference.getBooleanTheme(false)){
                    txtOne.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                }
                break;
        }

        la_back.setOnClickListener(v -> onBackPressed());

        llEnglish.setOnClickListener(v -> {
            languageCode = 1;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
            }
        });

        llHindi.setOnClickListener(v -> {
            languageCode = 2;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
            }
        });

        llGujarati.setOnClickListener(v -> {
            languageCode = 3;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                txtThree.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
            }
        });

        llTelugu.setOnClickListener(v -> {
            languageCode = 4;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                txtFour.setTextColor(ContextCompat.getColor(ActivityLanguage.this, R.color.colorBlack));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
            }
        });

        llSpanish.setOnClickListener(v -> {
            languageCode = 5;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
            }
        });

        llPortuguese.setOnClickListener(v -> {
            languageCode = 6;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
            }
        });

        llFrench.setOnClickListener(v -> {
            languageCode = 7;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
            }
        });

        llBengali.setOnClickListener(v -> {
            languageCode = 8;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
            }
        });

        llMarathi.setOnClickListener(v -> {
            languageCode = 9;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
            }
        });

        llTurkish.setOnClickListener(v -> {
            languageCode = 10;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
            }
        });

        llGerman.setOnClickListener(v -> {
            languageCode = 11;
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(ActivityLanguage.this, R.drawable.bg_language_select));
            }
        });

        rlContinue.setOnClickListener(v -> {
            switch (languageCode) {
                case 1:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "en");
                    Preference.setCheckActiveLanguage("en");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                case 2:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "hi");
                    Preference.setCheckActiveLanguage("hi");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                case 3:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "gu");
                    Preference.setCheckActiveLanguage("gu");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                case 4:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "te");
                    Preference.setCheckActiveLanguage("te");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                case 5:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "es");
                    Preference.setCheckActiveLanguage("es");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                case 6:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "pt");
                    Preference.setCheckActiveLanguage("pt");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                case 7:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "fr");
                    Preference.setCheckActiveLanguage("fr");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                case 8:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "bn");
                    Preference.setCheckActiveLanguage("bn");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                case 9:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "mr");
                    Preference.setCheckActiveLanguage("mr");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                case 10:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "tr");
                    Preference.setCheckActiveLanguage("tr");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                case 11:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "de");
                    Preference.setCheckActiveLanguage("de");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class).addFlags(268468224));
                    finish();
                    break;
                default:
                    Lingver.getInstance().setLocale(ActivityLanguage.this, "en");
                    Preference.setCheckActiveLanguage("en");
                    startActivity(new Intent(ActivityLanguage.this, ActivityMain.class));
                    finish();
                    break;
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
}