package com.whatscan.toolkit.forwa.User;

import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.whatscan.toolkit.forwa.ActivityHome;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.PlanCheck;
import com.whatscan.toolkit.forwa.GetSet.ResponseLogin;
import com.whatscan.toolkit.forwa.Other.CommonWebView;
import com.whatscan.toolkit.forwa.R;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    public TextInputEditText et_Pass, et_Username;
    public GoogleSignInClient googleApiClient;
    public RelativeLayout cv_login;
    public TextInputLayout mtf_UserName, mtf_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityLogin.this);
        setContentView(R.layout.activity_login);

        Utils.CheckConnection(ActivityLogin.this, findViewById(R.id.cv_login));


        FindView();
    }

    private void FindView() {
        ImageView la_back = findViewById(R.id.la_back);
        LinearLayout ll_fb = findViewById(R.id.ll_fb);
        LinearLayout ll_google = findViewById(R.id.ll_google);
        LinearLayout bt_sign_in = findViewById(R.id.bt_sign_in);
        TextView tv_send = findViewById(R.id.tv_send);
        TextView txt_term = findViewById(R.id.txt_term);
        TextView txtForgotPass = findViewById(R.id.txtForgotPass);
        TextView txtGLogIn = findViewById(R.id.txtGLogIn);
        et_Username = findViewById(R.id.et_Username);
        et_Pass = findViewById(R.id.et_Pass);
        cv_login = findViewById(R.id.cv_login);
        mtf_UserName = findViewById(R.id.mtf_UserName);
        mtf_pass = findViewById(R.id.mtf_pass);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            cv_login.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ll_google.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login_w));
            txtGLogIn.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txt_term.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_Username.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_Pass.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            setTextInputLayoutHintColor(mtf_UserName, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_pass, this, R.color.colorWhite);
            tv_send.setText(Html.fromHtml("<font color=#FFFFFF>Don\\'t have an Account?</font>  <font color=#6DA188><strong>  Sign Up</strong></font>"));
        } else {
            SetStatusBar();
            cv_login.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ll_google.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_login));
            txtGLogIn.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txt_term.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_Username.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_Pass.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            setTextInputLayoutHintColor(mtf_UserName, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_pass, this, R.color.colorBlack);
            tv_send.setText(Html.fromHtml(getString(R.string.new_to_whatstool_sign_up_here)));
        }

        txtForgotPass.setOnClickListener(v -> {
            Intent i = new Intent(ActivityLogin.this, ActivityForgotPassword.class);
            startActivity(i);
        });

        SpannableString SpanString = new SpannableString(
                "By signing in, you agree to the Terms of Use and Privacy Policy");

        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(ActivityLogin.this, CommonWebView.class);
                mIntent.putExtra("tc", true);
                startActivity(mIntent);
            }
        };

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(ActivityLogin.this, CommonWebView.class);
                mIntent.putExtra("pp", true);
                startActivity(mIntent);
            }
        };


        SpanString.setSpan(teremsAndCondition, 32, 45, 0);
        SpanString.setSpan(privacy, 49, 63, 0);
        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 32, 45, 0);
        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 49, 63, 0);

        txt_term.setMovementMethod(LinkMovementMethod.getInstance());
        txt_term.setText(SpanString, TextView.BufferType.SPANNABLE);
        txt_term.setSelected(true);

        la_back.setOnClickListener(v -> onBackPressed());

        tv_send.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
            startActivity(intent);
        });

        Constant.IntProgress(ActivityLogin.this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleApiClient = GoogleSignIn.getClient(this, gso);

        ll_google.setOnClickListener(v -> {
            Intent signInIntent = googleApiClient.getSignInIntent();
            startActivityForResult(signInIntent, 9001);
        });


        bt_sign_in.setOnClickListener(v -> {
            Preference.setUserName(Objects.requireNonNull(et_Username.getText()).toString());
            Preference.setPassword(Objects.requireNonNull(et_Pass.getText()).toString());
            if (Preference.getUserName().isEmpty()) {
                et_Username.setError("Enter username");
            } else if (Preference.getPassword().isEmpty()) {
                et_Pass.setError("Enter password");
            } else {
                ApiLogin("manually");
            }
        });
    }

    private void setTextInputLayoutHintColor(TextInputLayout text_input1, Context context, int colorBlack) {
        text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, colorBlack)));
    }

    public void ShowHidePass(View view) {
        if (view.getId() == R.id.pass_show_hide) {
            if (et_Pass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.pass_show);
                et_Pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.pass_hide);
                et_Pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
        et_Pass.setSelection(Objects.requireNonNull(et_Pass.getText()).length());
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
            ApiLogin("google");
        }
    }

    private void ApiLogin(String strType) {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getLogin(Preference.getLogin(), Preference.getUserName(), Preference.getEmail(), "+91", Preference.getPassword(), Preference.getAndroid_id(), "7433802152", Preference.getFcm_Id(), strType, Preference.getGoogle_key(), Preference.getFacebook_key()).enqueue(new Callback<ResponseLogin>() {
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
                        AppEventsLogger logger = AppEventsLogger.newLogger(ActivityLogin.this);
                        Bundle params = new Bundle();
                        params.putString(AppEventsConstants.EVENT_NAME_ACTIVATED_APP, "Login");
                        logger.logEvent("User_Login",  params);
                        Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Constant.DismissProgress();
                        assert response.body() != null;
                        Toast.makeText(ActivityLogin.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseLogin> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityLogin.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
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

    public void setStatusBarTheme() {
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityLogin.this, R.color.darkBlack));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivityLogin.this, findViewById(R.id.cv_login), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}