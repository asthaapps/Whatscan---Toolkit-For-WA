package com.whatscan.toolkit.forwa.ReferCoin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.CheckCoinData;
import com.whatscan.toolkit.forwa.GetSet.LoginCoin;
import com.whatscan.toolkit.forwa.GetSet.ToolUseCoin;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.whatscan.toolkit.forwa.Util.onLoginCoinEventListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mukesh.OtpView;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCoin extends Fragment {
    public static int noviceCount = 2, basicCount = 8;
    public Context context;
    public boolean isLoading;
    public RelativeLayout rlCoin;
    public Button btnLoginCoin, btnInvitation, btnDaily, btn1min, btn5min, btn10min, btn20min, btn30min, btnWatchAd, btnInviteFrd;
    public TextView txtNoviceCount, txtBasicCount, txtOne, txtTwo, txtThree, txtFour, txtFive, txtSix, txtSeven, txtEight;
    public TextView txtNine, txtTen, txtEleven, txtTweleve, txtThirteen, txtFourteen, txtFifteen, txtSixteen, txtSeventeen, txtEighteen, txtNineteen, txtTwenty, txtTwentyOne, txtTwentyTwo;
    public LinearLayout llOne, llTwo, llThree, llFour, llFive, llSix, llSeven, llEight, llNine, llTen;
    public onLoginCoinEventListener onLoginCoinEvent;
    public RewardedAd rewardedAd;
    private RewardedInterstitialAd rewardedInterstitialAd;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onLoginCoinEvent = (onLoginCoinEventListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coin, container, false);

        context = getContext();
        btnLoginCoin = view.findViewById(R.id.btnLoginCoin);
        btnInvitation = view.findViewById(R.id.btnInvitation);
        btnDaily = view.findViewById(R.id.btnDaily);
        txtNoviceCount = view.findViewById(R.id.tvNoviceCount);
        txtBasicCount = view.findViewById(R.id.tvBasicCount);
        txtOne = view.findViewById(R.id.txtOne);
        txtTwo = view.findViewById(R.id.txtTwo);
        txtThree = view.findViewById(R.id.txtThree);
        txtFour = view.findViewById(R.id.txtFour);
        txtFive = view.findViewById(R.id.txtFive);
        txtSix = view.findViewById(R.id.txtSix);
        txtSeven = view.findViewById(R.id.txtSeven);
        txtEight = view.findViewById(R.id.txtEight);
        txtNine = view.findViewById(R.id.txtNine);
        txtTen = view.findViewById(R.id.txtTen);
        txtEleven = view.findViewById(R.id.txtEleven);
        txtTweleve = view.findViewById(R.id.txtTweleve);
        txtThirteen = view.findViewById(R.id.txtThirteen);
        txtFourteen = view.findViewById(R.id.txtFourteen);
        txtFifteen = view.findViewById(R.id.txtFifteen);
        txtSixteen = view.findViewById(R.id.txtSixteen);
        txtSeventeen = view.findViewById(R.id.txtSeventeen);
        txtEighteen = view.findViewById(R.id.txtEighteen);
        txtNineteen = view.findViewById(R.id.txtNineteen);
        txtNineteen = view.findViewById(R.id.txtNineteen);
        txtTwenty = view.findViewById(R.id.txtTwenty);
        txtTwentyOne = view.findViewById(R.id.txtTwentyOne);
        txtTwentyTwo = view.findViewById(R.id.txtTwentyTwo);
        btn1min = view.findViewById(R.id.btnOneMin);
        btn5min = view.findViewById(R.id.btnFiveMin);
        btn10min = view.findViewById(R.id.btnTenMin);
        btn20min = view.findViewById(R.id.btn20Min);
        btn30min = view.findViewById(R.id.btn30Min);
        btnWatchAd = view.findViewById(R.id.btnWatch);
        btnInviteFrd = view.findViewById(R.id.btnInviteFrd);
        rlCoin = view.findViewById(R.id.rlCoin);
        llOne = view.findViewById(R.id.llOne);
        llTwo = view.findViewById(R.id.llTwo);
        llThree = view.findViewById(R.id.llThree);
        llFour = view.findViewById(R.id.llFour);
        llFive = view.findViewById(R.id.llFive);
        llSix = view.findViewById(R.id.llSix);
        llSeven = view.findViewById(R.id.llSeven);
        llEight = view.findViewById(R.id.llEight);
        llNine = view.findViewById(R.id.llNine);
        llTen = view.findViewById(R.id.llTen);

        if (Preference.getBooleanTheme(false)) {
            rlCoin.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
            txtOne.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtFour.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtFive.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtSix.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtSeven.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtEight.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtNine.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTen.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtEleven.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTweleve.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtThirteen.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtFourteen.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtFifteen.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtSixteen.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtSeventeen.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtEighteen.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtNineteen.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTwenty.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTwentyOne.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTwentyTwo.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));

            llOne.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llTwo.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llThree.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llFour.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llFive.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llSix.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llSeven.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llEight.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llNine.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llTen.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));

            btnLoginCoin.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_w));
            btnInvitation.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_w));
            btnDaily.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_w));
            btnInviteFrd.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_w));
            btnWatchAd.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_w));
            btn1min.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_w));
            btn5min.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_w));
            btn10min.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_w));
            btn20min.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_w));
            btn30min.setBackground(ContextCompat.getDrawable(context, R.drawable.boder_w));

            btnLoginCoin.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            btnInvitation.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            btnDaily.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            btnInviteFrd.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            btnWatchAd.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            btn1min.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            btn5min.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            btn10min.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            btn20min.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            btn30min.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        CheckCoinApi();
        CheckEnability();
        loadFullReward(context);

        btnWatchAd.setOnClickListener(v -> showRewardedVideo());

        btnLoginCoin.setOnClickListener(this::callApi);

        btnDaily.setOnClickListener(v -> Toast.makeText(context, "Comming Soon", Toast.LENGTH_SHORT).show());

        btnInvitation.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.invitation_dialog, null);
            bottomSheetDialog.setContentView(inflate);
            OtpView otpView = inflate.findViewById(R.id.otpView);
            TextView tv_error = inflate.findViewById(R.id.tv_error);
            TextView txtOne = inflate.findViewById(R.id.txtOne);
            RelativeLayout rlInvitation = inflate.findViewById(R.id.rlInvitation);

            if (Preference.getBooleanTheme(false)) {
                rlInvitation.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
                txtOne.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

            otpView.setOtpCompletionListener(otp -> callInvitationApi(otp, bottomSheetDialog, tv_error));

            bottomSheetDialog.show();
        });

        btnInviteFrd.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.invite_frd_dialog, null);
            bottomSheetDialog.setContentView(inflate);

            TextView txtCode = inflate.findViewById(R.id.txtCode);
            TextView txtCopy = inflate.findViewById(R.id.txtCopy);
            TextView tvInfo = inflate.findViewById(R.id.tvInfo);
            TextView tvInviteInfo = inflate.findViewById(R.id.tvInviteInfo);
            AppCompatButton btnInviteNow = inflate.findViewById(R.id.btnInviteNow);
            RelativeLayout rlInviteFrd = inflate.findViewById(R.id.rlInviteFrd);

            if (Preference.getBooleanTheme(false)) {
                rlInviteFrd.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
                txtCopy.setTextColor(ContextCompat.getColor(context, R.color.Red));
                tvInfo.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                tvInviteInfo.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                btnInviteNow.setBackground(ContextCompat.getDrawable(context, R.drawable.button_light));
                btnInviteNow.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

            txtCode.setText(Preference.getReferedCode());

            btnInviteNow.setOnClickListener(v1 -> {
                bottomSheetDialog.dismiss();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here are 50 WhatsTool Coins which you can use to use premium Features and redeem exclusive coupons on WhatsTool. Tap the link  : https://play.google.com/store/apps/details?id=" + context.getPackageName()
                        + "to download the app and use my invitation code " + Preference.getReferedCode() + " to register.";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "App Link :");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share App Link Via"));
            });

            txtCode.setOnClickListener(v12 -> Utils.setClipboard(context, txtCode.getText().toString()));
            bottomSheetDialog.show();
        });

        btn1min.setOnClickListener(v -> startTimer(60000, 1000, 1));

        btn5min.setOnClickListener(v -> startTimer(300000, 1000, 5));

        btn10min.setOnClickListener(v -> startTimer(600000, 1000, 10));

        btn20min.setOnClickListener(v -> startTimer(1200000, 1000, 20));

        btn30min.setOnClickListener(v -> startTimer(1800000, 1000, 30));

        return view;
    }

    private void CheckCoinApi() {
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.geCheckCoinData(Preference.getCheck_coin(), Preference.getU_id()).enqueue(new Callback<CheckCoinData>() {
            @Override
            public void onResponse(@NotNull Call<CheckCoinData> call, @NotNull Response<CheckCoinData> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getFlag().equals("true")) {
                        Preference.setTotal_coin(response.body().getCoins());
                        Preference.setCoin1Min(response.body().getTooluse_1min());
                        Preference.setCoin5Min(response.body().getTooluse_5min());
                        Preference.setCoin10Min(response.body().getTooluse_10min());
                        Preference.setCoin20Min(response.body().getTooluse_20min());
                        Preference.setCoin30Min(response.body().getTooluse_30min());
                        Preference.setLogin_coin(response.body().getFirstlogin_status());
                        Preference.setInvitation_coin(response.body().getInvitation_status());
                    }
                } else {
                    Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<CheckCoinData> call, @NotNull Throwable t) {
                Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRewardedVideo() {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdShowedFullScreenContent() {

                        }

                        /** Called when the ad failed to show full screen content. */
                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {


                            // Don't forget to set the ad reference to null so you
                            // don't show the ad a second time.
                            rewardedInterstitialAd = null;
                            loadFullReward(context);
                        }

                        /** Called when full screen content is dismissed. */
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Don't forget to set the ad reference to null so you
                            // don't show the ad a second time.
                            rewardedInterstitialAd = null;

                            // Preload the next rewarded interstitial ad.
                            loadFullReward(context);
                        }
                    });


            rewardedInterstitialAd.show((Activity) context, rewardItem -> {
                // Handle the reward.
                callWatchAdApi();
            });
        }

    }

    private void callWatchAdApi() {
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getWatchAdCoin(Preference.getWatch_ad(), Preference.getU_id(), "true").enqueue(new Callback<LoginCoin>() {
            @Override
            public void onResponse(@NotNull Call<LoginCoin> call, @NotNull Response<LoginCoin> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getFlag()) {
                        Preference.setTotal_coin(response.body().getCoins());
                        onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                        Toast.makeText(getActivity(), "Coins added..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginCoin> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void loadFullReward(Context context) {
        isLoading = true;
        RewardedInterstitialAd.load(context, Preference.getRewardAds(),
                new AdRequest.Builder().build(), new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        isLoading = false;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        isLoading = false;
                    }
                });

    }

    @SuppressLint("SetTextI18n")
    private void CheckEnability() {
        if (!Preference.getLogin_coin().equals("true")) {
            btnLoginCoin.setEnabled(false);
            btnLoginCoin.setText("Completed");
            btnLoginCoin.setBackground(ContextCompat.getDrawable(context, R.drawable.button_light));
            btnLoginCoin.setTextColor(getResources().getColor(R.color.white));
        } else if (Preference.getInvitation_coin().equals("false")) {
            btnInvitation.setEnabled(false);
            btnInvitation.setText("Completed");
            btnInvitation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_light));
            btnInvitation.setTextColor(getResources().getColor(R.color.white));
        } else if (Preference.getCoin1Min().equals("false")) {
            btn1min.setEnabled(false);
            btn1min.setText("Completed");
            btn1min.setBackground(ContextCompat.getDrawable(context, R.drawable.button_light));
            btn1min.setTextColor(getResources().getColor(R.color.white));
        } else if (Preference.getCoin5Min().equals("false")) {
            btn5min.setEnabled(false);
            btn5min.setText("Completed");
            btn5min.setBackground(ContextCompat.getDrawable(context, R.drawable.button_light));
            btn5min.setTextColor(getResources().getColor(R.color.white));
        } else if (Preference.getCoin10Min().equals("false")) {
            btn10min.setEnabled(false);
            btn10min.setText("Completed");
            btn10min.setBackground(ContextCompat.getDrawable(context, R.drawable.button_light));
            btn10min.setTextColor(getResources().getColor(R.color.white));
        } else if (Preference.getCoin20Min().equals("false")) {
            btn20min.setEnabled(false);
            btn20min.setText("Completed");
            btn20min.setBackground(ContextCompat.getDrawable(context, R.drawable.button_light));
            btn20min.setTextColor(getResources().getColor(R.color.white));
        } else if (Preference.getCoin30Min().equals("false")) {
            btn30min.setEnabled(false);
            btn30min.setText("Completed");
            btn30min.setBackground(ContextCompat.getDrawable(context, R.drawable.button_light));
            btn30min.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void startTimer(long millisecond, long interval, int time) {
        new CountDownTimer(millisecond, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                callToolUseApi(String.valueOf(time));
            }
        }.start();
        startActivity(new Intent(getActivity(), ActivityMain.class));
    }

    private void callToolUseApi(String time) {
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getToolsUseCoin(Preference.getTool_use(), Preference.getU_id(), time).enqueue(new Callback<ToolUseCoin>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<ToolUseCoin> call, @NotNull Response<ToolUseCoin> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getFlag().equals("true")) {
                        basicCount--;
                        txtBasicCount.setText(basicCount + "/8 left");
                        Preference.setTotal_coin(response.body().getCoins());
                        onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                        Toast.makeText(getActivity(), "Coins Added...", Toast.LENGTH_LONG).show();
                        switch (time) {
                            case "1":
                                Preference.setCoin1Min(response.body().getStatus());
                                break;
                            case "5":
                                Preference.setCoin5Min(response.body().getStatus());
                                break;
                            case "10":
                                Preference.setCoin10Min(response.body().getStatus());
                                break;
                            case "20":
                                Preference.setCoin20Min(response.body().getStatus());
                                break;
                            case "30":
                                Preference.setCoin30Min(response.body().getStatus());
                                break;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "response unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ToolUseCoin> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callInvitationApi(String otp, BottomSheetDialog bottomSheetDialog, TextView tv_error) {
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getInvitationCoin(Preference.getInvitation_code(), Preference.getU_id(), otp).enqueue(new Callback<LoginCoin>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<LoginCoin> call, @NotNull Response<LoginCoin> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getFlag()) {
                        Preference.setInvitation_coin("true");
                        Preference.setTotal_coin(response.body().getCoins());
                        onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                        btnInvitation.setEnabled(false);
                        btnInvitation.setText("Completed");
                        btnInvitation.setBackground(ContextCompat.getDrawable(context, R.drawable.button_light));
                        btnInvitation.setTextColor(getResources().getColor(R.color.white));
                        bottomSheetDialog.dismiss();
                        noviceCount--;
                        txtNoviceCount.setText(noviceCount + "/2 left");
                    } else {
                        tv_error.setVisibility(View.VISIBLE);
                        tv_error.postDelayed(() -> tv_error.setVisibility(View.INVISIBLE), 8000);
                    }

                } else {
                    Toast.makeText(getContext(), "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginCoin> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callApi(View v) {
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getLoginCoin(Preference.getFirst_logIn(), Preference.getU_id(), "login").enqueue(new Callback<LoginCoin>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<LoginCoin> call, @NotNull Response<LoginCoin> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getFlag()) {
                        Preference.setTotal_coin(response.body().getCoins());
                        onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                        btnLoginCoin.setEnabled(false);
                        btnLoginCoin.setText("Completed");
                        btnLoginCoin.setBackground(ContextCompat.getDrawable(context, R.drawable.button_light));
                        btnLoginCoin.setTextColor(getResources().getColor(R.color.white));
                        noviceCount--;
                        txtNoviceCount.setText(noviceCount + "/2 left");
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginCoin> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}