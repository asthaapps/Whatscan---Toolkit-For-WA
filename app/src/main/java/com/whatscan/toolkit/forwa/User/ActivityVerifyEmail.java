package com.whatscan.toolkit.forwa.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.mukesh.OtpView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityVerifyEmail extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private TextView tv_resend, tv_timer, tv_error, tv_email, tv_title2, tv_dec2, tv_dec3;
    private OtpView otpView;
    private String strOtp;
    private Button btn_submit;
    private RelativeLayout rlVEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityVerifyEmail.this);
        setContentView(R.layout.activity_verify_email);

        Utils.CheckConnection(ActivityVerifyEmail.this, findViewById(R.id.rlVEmail));
        FindView();
    }

    private void FindView() {
        tv_error = findViewById(R.id.tv_error);
        tv_email = findViewById(R.id.tv_email);
        tv_title2 = findViewById(R.id.tv_title2);
        tv_dec2 = findViewById(R.id.tv_dec2);
        tv_dec3 = findViewById(R.id.tv_dec3);
        tv_resend = findViewById(R.id.tv_resend);
        tv_timer = findViewById(R.id.tv_timer);
        btn_submit = findViewById(R.id.btn_submit);
        otpView = findViewById(R.id.otpView);
        rlVEmail = findViewById(R.id.rlVEmail);

        Constant.IntProgress(ActivityVerifyEmail.this);

        if (Preference.getBooleanTheme(false)) {
            rlVEmail.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tv_title2.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_dec2.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_email.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_timer.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_dec3.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            rlVEmail.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_title2.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            tv_dec2.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            tv_email.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            tv_email.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
            tv_dec3.setTextColor(ContextCompat.getColor(this, R.color.colorGrey));
        }

        StartCountDown();
        tv_email.setText(Preference.getEmail());
        otpView.setOtpCompletionListener(s -> strOtp = s);

        btn_submit.setOnClickListener(v -> {
            if (strOtp.isEmpty()) {
                otpView.setError("Invalid otp");
            } else {
                ApiOtpVerify(strOtp);
            }
        });

        tv_resend.setOnClickListener(v -> ApiResend());

    }

    private void ApiOtpVerify(String strOtp) {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getOtpVerifyEmail(Preference.getRegisterVerify(), Preference.getU_id(), strOtp).enqueue(new Callback<ChangePassword>() {
            @Override
            public void onResponse(@NotNull Call<ChangePassword> call, @NotNull Response<ChangePassword> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Constant.DismissProgress();
                    if (response.body().getFlag().equals("true")) {
                        Toast.makeText(ActivityVerifyEmail.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(ActivityVerifyEmail.this, ActivityMain.class);
                        intent.putExtra("email", response.body().getEmail());
                        startActivity(intent);
                        finish();
                    } else {
                        otpView.setLineColor(ContextCompat.getColor(ActivityVerifyEmail.this, R.color.fingerprint_error));
                        tv_error.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(ActivityVerifyEmail.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ChangePassword> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityVerifyEmail.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ApiResend() {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getResendOtp(Preference.getRegisterResend(), Preference.getEmail()).enqueue(new Callback<ResendOtp>() {
            @Override
            public void onResponse(@NotNull Call<ResendOtp> call, @NotNull Response<ResendOtp> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Constant.DismissProgress();
                    if (response.body().getFlag().equals("true")) {
                        StartCountDown();
                        Preference.setU_id(response.body().getU_id());
                        Toast.makeText(ActivityVerifyEmail.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityVerifyEmail.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResendOtp> call, @NotNull Throwable t) {
                Constant.DismissProgress();
                Toast.makeText(ActivityVerifyEmail.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
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
        Utils.showSnack(ActivityVerifyEmail.this, findViewById(R.id.rlVEmail), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}