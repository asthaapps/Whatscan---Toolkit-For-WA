package com.whatscan.toolkit.forwa.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.ChangePassword;
import com.whatscan.toolkit.forwa.GetSet.PlanCheck;
import com.whatscan.toolkit.forwa.GetSet.ResendOtp;
import com.whatscan.toolkit.forwa.GetSet.verifyEmail;
import com.whatscan.toolkit.forwa.GetSet.verifyOtp;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mukesh.OtpView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityForgotPassword extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private RelativeLayout rlForPass, rl_reset, rl_otp, rl_pass;
    private TextInputEditText et_Pass, et_CPass, et_email;
    private TextInputLayout mtf_Email, mtf_password, mtf_C_password;
    private TextView tv_resend, tv_timer, tv_email, tv_error, tv_title, tv_dec, tv_title2, tv_dec2, tv_dec3, tv_title3, tv_dec4;
    private OtpView otpView;
    private String strOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityForgotPassword.this);
        setContentView(R.layout.activity_forgot_password);

        Utils.CheckConnection(ActivityForgotPassword.this, findViewById(R.id.rlForPass));
        FindView();
    }

    private void FindView() {
        rlForPass = findViewById(R.id.rlForPass);
        rl_reset = findViewById(R.id.rl_reset);
        rl_otp = findViewById(R.id.rl_otp);
        rl_pass = findViewById(R.id.rl_pass);
        et_email = findViewById(R.id.et_email);
        otpView = findViewById(R.id.otpView);
        tv_resend = findViewById(R.id.tv_resend);
        tv_timer = findViewById(R.id.tv_timer);
        tv_email = findViewById(R.id.tv_email);
        tv_error = findViewById(R.id.tv_error);
        tv_title = findViewById(R.id.tv_title);
        tv_dec = findViewById(R.id.tv_dec);
        tv_title2 = findViewById(R.id.tv_title2);
        tv_dec2 = findViewById(R.id.tv_dec2);
        tv_dec3 = findViewById(R.id.tv_dec3);
        tv_title3 = findViewById(R.id.tv_title3);
        tv_dec4 = findViewById(R.id.tv_dec4);
        et_Pass = findViewById(R.id.et_Pass);
        et_CPass = findViewById(R.id.et_CPass);
        mtf_Email = findViewById(R.id.mtf_Email);
        mtf_password = findViewById(R.id.mtf_password);
        mtf_C_password = findViewById(R.id.mtf_C_password);
        Button btn_cPass = findViewById(R.id.btn_cPass);
        Button btn_submit = findViewById(R.id.btn_submit);
        Button btn_resetPass = findViewById(R.id.btn_resetPass);
        TextView tv_change = findViewById(R.id.tv_change);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlForPass.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tv_title.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_dec.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_email.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_Pass.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_CPass.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_title2.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_dec2.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_dec3.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_email.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_timer.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_title3.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_dec4.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            setTextInputLayoutHintColor(mtf_Email, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_password, this, R.color.colorWhite);
            setTextInputLayoutHintColor(mtf_C_password, this, R.color.colorWhite);
        } else {
            SetStatusBar();
            rlForPass.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_title.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            tv_dec.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            et_email.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_Pass.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_CPass.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            tv_title2.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            tv_dec2.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            tv_email.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            tv_timer.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            tv_dec3.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            tv_title3.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            tv_dec4.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            setTextInputLayoutHintColor(mtf_Email, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_password, this, R.color.colorBlack);
            setTextInputLayoutHintColor(mtf_C_password, this, R.color.colorBlack);
        }

        Constant.IntProgress(ActivityForgotPassword.this);

        btn_resetPass.setOnClickListener(v -> {
            Preference.setEmail(Objects.requireNonNull(et_email.getText()).toString());
            if (Preference.getEmail().isEmpty()) {
                et_email.setError("Enter email");
            } else if (isValidEmail(Preference.getEmail())) {
                et_email.setError("Invalid email");
            } else {
                ApiSendOtp(Preference.getEmail());
            }
        });
        otpView.setOtpCompletionListener(s -> strOtp = s);
        btn_submit.setOnClickListener(v -> {
            if (strOtp.isEmpty()) {
                otpView.setError("Invalid otp");
            } else {
                ApiOtpVerify(strOtp);
            }
        });

        btn_cPass.setOnClickListener(v -> {
            String strPass = Objects.requireNonNull(et_Pass.getText()).toString();
            String strCPass = Objects.requireNonNull(et_CPass.getText()).toString();

            if (strPass.isEmpty()) {
                et_Pass.setError("Enter password");
            } else if (isValidPassword(strPass)) {
                et_Pass.setError("Invalid password");
            } else if (strCPass.isEmpty()) {
                et_CPass.setError("Enter confirm password");
            } else if (!strPass.equals(strCPass)) {
                et_Pass.setError("Password Not Match");
                et_CPass.setError("Password Not Match");
            } else {
                ApiChangePass(strPass, strCPass);
            }
        });

        tv_resend.setOnClickListener(v -> ApiResend());
        tv_change.setOnClickListener(v -> {
            if (rl_otp.getVisibility() == View.VISIBLE) {
                rl_reset.setVisibility(View.VISIBLE);
                rl_otp.setVisibility(View.GONE);
            }
            Preference.setEmail("");
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
        if (view.getId() == R.id.pass_show_hide1) {
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

    private void ApiResend() {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getResendOtp(Preference.getForgotResend(), Preference.getEmail()).enqueue(new Callback<ResendOtp>() {
            @Override
            public void onResponse(@NotNull Call<ResendOtp> call, @NotNull Response<ResendOtp> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Constant.DismissProgress();
                    if (response.body().getFlag().equals("true")) {
                        StartCountDown();
                        Preference.setU_id(response.body().getU_id());
                        Toast.makeText(ActivityForgotPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityForgotPassword.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResendOtp> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityForgotPassword.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ApiChangePass(String strPass, String strCPass) {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getChangePass(Preference.getChangePass(), Preference.getU_id(), strPass, strCPass).enqueue(new Callback<ChangePassword>() {
            @Override
            public void onResponse(@NotNull Call<ChangePassword> call, @NotNull Response<ChangePassword> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Constant.DismissProgress();
                    if (response.body().getFlag().equals("true")) {
                        Toast.makeText(ActivityForgotPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Preference.setU_id(response.body().getuId());
                        Preference.setTool_id(response.body().getToolId());
                        Preference.setUserName(response.body().getUsername());
                        Preference.setEmail(response.body().getEmail());
                        Preference.setMobile(response.body().getMobile());
                        Preference.setFcm_Id(response.body().getFcmId());
                        Preference.setAndroid_id(response.body().getAndroidId());
                        Preference.setCheck_Notification(response.body().getNotificationStatus());
                        Preference.setToken(response.body().getRegisterToken());
                        Preference.setReferedCode(response.body().getReferedNumber());
                        Preference.setTotal_coin(response.body().getCoins());

                        Preference.setLogin_status("Yes");
                        Intent intent = new Intent(ActivityForgotPassword.this, ActivityMain.class);
                        intent.putExtra("email", response.body().getEmail());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ActivityForgotPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityForgotPassword.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ChangePassword> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityForgotPassword.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private void ApiOtpVerify(String strOtp) {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getOtpVerifyForgot(Preference.getForgotVerify(), Preference.getU_id(), strOtp).enqueue(new Callback<verifyOtp>() {
            @Override
            public void onResponse(@NotNull Call<verifyOtp> call, @NotNull Response<verifyOtp> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Constant.DismissProgress();
                    if (response.body().getFlag().equals("true")) {
                        rl_otp.setVisibility(View.GONE);
                        rl_pass.setVisibility(View.VISIBLE);
                    } else {
                        otpView.setLineColor(ContextCompat.getColor(ActivityForgotPassword.this, R.color.fingerprint_error));
                        tv_error.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(ActivityForgotPassword.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<verifyOtp> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityForgotPassword.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ApiSendOtp(String strEmail) {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getForgotEmail(Preference.getForgotEmail(), strEmail).enqueue(new Callback<verifyEmail>() {
            @Override
            public void onResponse(@NotNull Call<verifyEmail> call, @NotNull Response<verifyEmail> response) {
                if (response.isSuccessful()) {
                    Constant.DismissProgress();
                    assert response.body() != null;
                    if (response.body().getFlag().equals("true")) {
                        StartCountDown();
                        Preference.setU_id(response.body().getU_id());
                        tv_email.setText(response.body().getEmail());
                        Toast.makeText(ActivityForgotPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        rl_reset.setVisibility(View.GONE);
                        rl_otp.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ActivityForgotPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityForgotPassword.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<verifyEmail> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityForgotPassword.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void StartCountDown() {
        new CountDownTimer(45000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                tv_resend.setEnabled(false);
                tv_resend.setTextColor(getResources().getColor(R.color.colorGrey));
                tv_timer.setText(hms);
            }

            @Override
            public void onFinish() {
                tv_timer.setText("");
                tv_resend.setEnabled(true);
                tv_resend.setTextColor(getResources().getColor(R.color.colorTools));
            }
        }.start();
    }

    private boolean isValidEmail(CharSequence email) {
        return (!Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    @Override
    public void onBackPressed() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityForgotPassword.this);
        View inflate = LayoutInflater.from(ActivityForgotPassword.this).inflate(R.layout.dialog_hint, (ViewGroup) null);
        bottomSheetDialog.setContentView(inflate);

        LinearLayout llHint = inflate.findViewById(R.id.llHint);
        TextView txtTitle = inflate.findViewById(R.id.txtTitle);
        TextView txtInfo = inflate.findViewById(R.id.txtInfo);
        View findViewById = inflate.findViewById(R.id.btnHintContinue);

        if (Preference.getBooleanTheme(false)){
            llHint.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtTitle.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtInfo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        ((Button) findViewById).setText("Exit");
        txtInfo.setText("Didn't try to forgot password?");

        ((Button) inflate.findViewById(R.id.btnHintContinue)).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            ActivityForgotPassword.super.onBackPressed();
        });
        bottomSheetDialog.show();
    }

    public void setStatusBarTheme() {
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityForgotPassword.this, R.color.darkBlack));
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
        Utils.showSnack(ActivityForgotPassword.this, findViewById(R.id.rlForPass), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}