package com.whatscan.toolkit.forwa.User;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.ResponseRegister;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityRegister extends AppCompatActivity implements OnCountryPickerListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public TextInputEditText et_name, et_mobile, et_mail, et_Pass, et_CPass;
    public TextInputLayout mtf_name, mtf_mobile, mtf_email, mtf_pass, mtf_CPass;
    private TextView tv_country_name, tv_country_code;
    public String strCountryName, strCountryCode;
    public int strFlag;
    private LottieAnimationView la_earth;
    private ImageView iv_flag;
    private CountryPicker countryPicker;
    private RelativeLayout rl_country;
    private NestedScrollView cv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityRegister.this);
        setContentView(R.layout.activity_register);

        Utils.CheckConnection(ActivityRegister.this, findViewById(R.id.cv_register));



        FindView();
    }

    private void FindView() {
        ImageView la_back = findViewById(R.id.la_back);
        LinearLayout bt_sign_up = findViewById(R.id.bt_sign_up);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        et_mail = findViewById(R.id.et_mail);
        et_Pass = findViewById(R.id.et_Pass);
        et_CPass = findViewById(R.id.et_CPass);
        mtf_name = findViewById(R.id.mtf_name);
        mtf_mobile = findViewById(R.id.mtf_mobile);
        mtf_email = findViewById(R.id.mtf_email);
        mtf_pass = findViewById(R.id.mtf_pass);
        mtf_CPass = findViewById(R.id.mtf_CPass);
        rl_country = findViewById(R.id.rl_country);
        la_earth = findViewById(R.id.la_earth);
        iv_flag = findViewById(R.id.iv_flag);
        tv_country_name = findViewById(R.id.tv_country_name);
        tv_country_code = findViewById(R.id.tv_country_code);
        cv_register = findViewById(R.id.cv_register);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            cv_register.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            et_name.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_mobile.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_mail.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_Pass.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_CPass.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            setTextInputLayoutHintColor(mtf_name, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_mobile, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_email, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_pass, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_CPass, this, R.color.colorWhite);
        } else {
            SetStatusBar();
            cv_register.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_name.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_mobile.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_mail.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_Pass.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_CPass.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            setTextInputLayoutHintColor(mtf_name, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_mobile, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_email, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_pass, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_CPass, this, R.color.colorBlack);
        }

        Constant.IntProgress(ActivityRegister.this);

        la_back.setOnClickListener(v -> onBackPressed());
        bt_sign_up.setOnClickListener(v -> {
            Preference.setUserName(Objects.requireNonNull(et_name.getText()).toString());
            Preference.setEmail(Objects.requireNonNull(et_mail.getText()).toString());
            Preference.setPassword(Objects.requireNonNull(et_Pass.getText()).toString());
            Preference.setCPassword(Objects.requireNonNull(et_CPass.getText()).toString());
            Preference.setMobile(Objects.requireNonNull(et_mobile.getText()).toString());
            if (Preference.getUserName().isEmpty()) {
                et_name.setError("Enter name");
            } else if (Preference.getMobile().isEmpty()) {
                et_mobile.setError("Enter mobile no");
            } else if (Preference.getEmail().isEmpty()) {
                et_mail.setError("Enter email");
            } else if (isValidEmail(Preference.getEmail())) {
                et_mail.setError("Invalid email");
            } else if (Preference.getPassword().isEmpty()) {
                et_Pass.setError("Enter password");
            } else if (isValidPassword(Preference.getPassword())) {
                et_Pass.setError("Invalid password");
            } else if (Preference.getCPassword().isEmpty()) {
                et_CPass.setError("Enter confirm password");
            } else if (!Preference.getPassword().equals(Preference.getCPassword())) {
                et_Pass.setError("Password Not Match");
                et_CPass.setError("Password Not Match");
            } else {
                ApiRegister(et_name.getText().toString(), et_mail.getText().toString(), Objects.requireNonNull(et_mobile.getText()).toString(), et_Pass.getText().toString(), et_CPass.getText().toString());
            }
        });

        rl_country.setOnClickListener(v -> {
            CountryPicker.Builder builder = new CountryPicker.Builder().with(ActivityRegister.this).listener(ActivityRegister.this);
            builder.style(R.style.DialogStyle);
            builder.theme(CountryPicker.THEME_NEW);
            builder.canSearch(true);
            builder.sortBy(CountryPicker.SORT_BY_NONE);
            countryPicker = builder.build();
            countryPicker.showBottomSheet(ActivityRegister.this);
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

    public void ShowHidePassC(View view) {
        if (view.getId() == R.id.pass_show_hideC) {
            if (et_CPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.pass_show);
                et_CPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.pass_hide);
                et_CPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
        et_CPass.setSelection(Objects.requireNonNull(et_CPass.getText()).length());
    }

    private void ApiRegister(String strName, String strMail, String strMobile, String strPass, String strCPass) {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getRegister(Preference.getRegister(), strName, strMail, strCountryCode, strMobile, strPass, strCPass, Preference.getAndroid_id(), Preference.getFcm_Id(), "manually", Preference.getGoogle_key(), Preference.getFacebook_key(), "").enqueue(new Callback<ResponseRegister>() {
            @Override
            public void onResponse(@NotNull Call<ResponseRegister> call, @NotNull Response<ResponseRegister> response) {
                if (response.isSuccessful()) {
                    Constant.DismissProgress();
                    assert response.body() != null;
                    if (response.body().getFlag().equals("true")) {
                        Preference.setU_id(response.body().getU_id());
                        Preference.setEmail(response.body().getEmail());
                        Toast.makeText(ActivityRegister.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ActivityRegister.this, ActivityVerifyEmail.class);
                        intent.putExtra("email", response.body().getEmail());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ActivityRegister.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Constant.DismissProgress();
                    Toast.makeText(ActivityRegister.this, "You are already registered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseRegister> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityRegister.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
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
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityRegister.this, R.color.darkBlack));
    }

    private boolean isValidEmail(CharSequence email) {
        return (!Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    @Override
    public void onSelectCountry(Country country) {
        strCountryName = country.getName();
        strCountryCode = country.getDialCode();
        strFlag = country.getFlag();
        tv_country_name.setText(country.getName());
        tv_country_code.setVisibility(View.VISIBLE);
        iv_flag.setVisibility(View.VISIBLE);
        la_earth.setVisibility(View.GONE);
        tv_country_code.setText(country.getDialCode());
        iv_flag.setImageResource(country.getFlag());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_country.getLayoutParams();
        params.setMargins(30, 30, 0, 30);
        rl_country.setLayoutParams(params);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivityRegister.this, findViewById(R.id.cv_register), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}
