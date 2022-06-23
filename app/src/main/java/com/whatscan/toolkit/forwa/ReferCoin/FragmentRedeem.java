package com.whatscan.toolkit.forwa.ReferCoin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.AdFreeData;
import com.whatscan.toolkit.forwa.GetSet.FeatursData;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.onLoginCoinEventListener;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRedeem extends Fragment {
    public Context context;
    public RelativeLayout rlAdOneDay, rlAdThreeDay, rlAdSevenDay, rlAdOneMonth;
    public RelativeLayout rlBulkOneDay, rlBulkSevenDay, rlAutoOneDay, rlAutoSevenDay, rlBulkExcelOneDay, rlBulkExcelSevenDay;
    public RelativeLayout rlRedeem;
    public TextView txtOne, txtTwo, txtThree, txtFour, txtFive, txtSix, txtAdFreeOne, txtAdFreeTwo, txtAdFreeThree, txtAdFreeFour;
    public TextView txtSeven, txtEight, txtNine, txtTen, txtEleven, txtTweleve;
    public TextView txtBulkOne, txtBulkSeven, txtAutoResOne, txtAutoResSeven, txtImportOne, txtImportSeven;
    public ImageView imgOne, imgTwo, imgThree, imgFour, imgFive, imgSix, imgSeven, imgEight, imgNine, imgTen;
    public onLoginCoinEventListener onLoginCoinEvent;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onLoginCoinEvent = (onLoginCoinEventListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_redeem, container, false);

        context = getContext();
        rlAdOneDay = view.findViewById(R.id.rlAdOneDay);
        rlAdThreeDay = view.findViewById(R.id.rlAdThreeDay);
        rlAdSevenDay = view.findViewById(R.id.rlAdSevenDay);
        rlAdOneMonth = view.findViewById(R.id.rlAdOneMonth);
        rlBulkOneDay = view.findViewById(R.id.rlBulkOneDay);
        rlBulkSevenDay = view.findViewById(R.id.rlBulkSevenDay);
        rlAutoOneDay = view.findViewById(R.id.rlAutoOneDay);
        rlAutoSevenDay = view.findViewById(R.id.rlAutoSevenDay);
        rlBulkExcelOneDay = view.findViewById(R.id.rlBulkExcelOneDay);
        rlBulkExcelSevenDay = view.findViewById(R.id.rlBulkExcelSevenDay);
        rlRedeem = view.findViewById(R.id.rlRedeem);
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
        txtAdFreeOne = view.findViewById(R.id.txtAdFreeOne);
        txtAdFreeTwo = view.findViewById(R.id.txtAdFreeTwo);
        txtAdFreeThree = view.findViewById(R.id.txtAdFreeThree);
        txtAdFreeFour = view.findViewById(R.id.txtAdFreeFour);
        txtBulkOne = view.findViewById(R.id.txtBulkOne);
        txtBulkSeven = view.findViewById(R.id.txtBulkSeven);
        txtAutoResOne = view.findViewById(R.id.txtAutoResOne);
        txtAutoResSeven = view.findViewById(R.id.txtAutoResSeven);
        txtImportOne = view.findViewById(R.id.txtImportOne);
        txtImportSeven = view.findViewById(R.id.txtImportSeven);
        imgOne = view.findViewById(R.id.imgOne);
        imgTwo = view.findViewById(R.id.imgTwo);
        imgThree = view.findViewById(R.id.imgThree);
        imgFour = view.findViewById(R.id.imgFour);
        imgFive = view.findViewById(R.id.imgFive);
        imgSix = view.findViewById(R.id.imgSix);
        imgSeven = view.findViewById(R.id.imgSeven);
        imgEight = view.findViewById(R.id.imgEight);
        imgNine = view.findViewById(R.id.imgNine);
        imgTen = view.findViewById(R.id.imgTen);

        if (Preference.getBooleanTheme(false)) {
            rlRedeem.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
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

            imgOne.setImageResource(R.drawable.ic_ticket_new_w);
            imgTwo.setImageResource(R.drawable.ic_ticket_new_w);
            imgThree.setImageResource(R.drawable.ic_ticket_new_w);
            imgFour.setImageResource(R.drawable.ic_ticket_new_w);
            imgFive.setImageResource(R.drawable.ic_ticket_new_w);
            imgSix.setImageResource(R.drawable.ic_ticket_new_w);
            imgSeven.setImageResource(R.drawable.ic_ticket_new_w);
            imgEight.setImageResource(R.drawable.ic_ticket_new_w);
            imgNine.setImageResource(R.drawable.ic_ticket_new_w);
            imgTen.setImageResource(R.drawable.ic_ticket_new_w);

            txtAdFreeOne.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtAdFreeTwo.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtAdFreeThree.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtAdFreeFour.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtBulkOne.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtBulkSeven.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtAutoResOne.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtAutoResSeven.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtImportOne.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtImportSeven.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        Constant.IntProgress(context);

        PushDownAnim.setPushDownAnimTo(rlAdOneDay).setOnClickListener(v -> ShowBottomDialogAds(1));

        PushDownAnim.setPushDownAnimTo(rlAdThreeDay).setOnClickListener(v -> ShowBottomDialogAds(2));

        PushDownAnim.setPushDownAnimTo(rlAdSevenDay).setOnClickListener(v -> ShowBottomDialogAds(3));

        PushDownAnim.setPushDownAnimTo(rlAdOneMonth).setOnClickListener(v -> ShowBottomDialogAds(4));

        PushDownAnim.setPushDownAnimTo(rlBulkOneDay).setOnClickListener(v -> ShowBottomDialogFeaturs(5));

        PushDownAnim.setPushDownAnimTo(rlBulkSevenDay).setOnClickListener(v -> ShowBottomDialogFeaturs(6));

        PushDownAnim.setPushDownAnimTo(rlAutoOneDay).setOnClickListener(v -> ShowBottomDialogFeaturs(7));

        PushDownAnim.setPushDownAnimTo(rlAutoSevenDay).setOnClickListener(v -> ShowBottomDialogFeaturs(8));

        PushDownAnim.setPushDownAnimTo(rlBulkExcelOneDay).setOnClickListener(v -> ShowBottomDialogFeaturs(9));

        PushDownAnim.setPushDownAnimTo(rlBulkExcelSevenDay).setOnClickListener(v -> ShowBottomDialogFeaturs(10));

        return view;
    }

    private void ShowBottomDialogFeaturs(int featursType) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_redeem_featurs_bottom, null);
        bottomSheetDialog.setContentView(inflate);

        RelativeLayout rlTop = inflate.findViewById(R.id.rlTop);
        LinearLayout rlBottom = inflate.findViewById(R.id.rlBottom);
        ImageView imgFeaturs = inflate.findViewById(R.id.imgFeaturs);
        ImageView imgCoinFeatursError = inflate.findViewById(R.id.imgCoinFeatursError);
        TextView txtFeaturs = inflate.findViewById(R.id.txtFeaturs);
        TextView txtFeatursViewDay = inflate.findViewById(R.id.txtFeatursViewDay);
        TextView txtOne = inflate.findViewById(R.id.txtOne);
        TextView txtTwo = inflate.findViewById(R.id.txtTwo);
        TextView txtThree = inflate.findViewById(R.id.txtThree);
        TextView txtFour = inflate.findViewById(R.id.txtFour);
        TextView txtFive = inflate.findViewById(R.id.txtFive);
        TextView txtSix = inflate.findViewById(R.id.txtSix);
        TextView txtRedeemFeatursCoin = inflate.findViewById(R.id.txtRedeemFeatursCoin);
        TextView txtTerms = inflate.findViewById(R.id.txtTerms);
        TextView txtUse = inflate.findViewById(R.id.txtUse);
        TextView txtRedeem = inflate.findViewById(R.id.txtRedeem);
        AppCompatButton btnFeatursMoreCoins = inflate.findViewById(R.id.btnFeatursMoreCoins);
        LinearLayout btnRedeemFeatursNow = inflate.findViewById(R.id.btnRedeemFeatursNow);

        if (Preference.getBooleanTheme(false)) {
            rlTop.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            rlBottom.setBackground(ContextCompat.getDrawable(context, R.drawable.dialog_black));
            txtFeaturs.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtFeatursViewDay.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTerms.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtUse.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtRedeem.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        txtOne.setText(Html.fromHtml("• Valid on all section for whole application"));
        txtTwo.setText(Html.fromHtml("• Will expire after the indicated date"));
        txtThree.setText(Html.fromHtml("• After the coupon is redeemed successfully. you will automatically get the free use of above tools When using tools, you can use tool without any limitation"));
        txtFour.setText(Html.fromHtml("• add plan or membership on your login account to use free of cost using tools"));
        txtFive.setText(Html.fromHtml("• While redeeming only that tools free and no other tools"));
        txtSix.setText(Html.fromHtml("• All benefits like pro and paid user"));

        if (featursType == 5) {
            Glide.with(context).load(R.drawable.w_bulk).into(imgFeaturs);
            txtFeaturs.setText(Html.fromHtml("Bulk Sender"));
            txtFeatursViewDay.setText(Html.fromHtml("Use Tools for 1 day"));
            txtRedeemFeatursCoin.setText(Html.fromHtml("800"));
        } else if (featursType == 6) {
            Glide.with(context).load(R.drawable.w_bulk).into(imgFeaturs);
            txtFeaturs.setText(Html.fromHtml("Bulk Sender"));
            txtFeatursViewDay.setText(Html.fromHtml("Use Tools for 7 day"));
            txtRedeemFeatursCoin.setText(Html.fromHtml("4200"));
        } else if (featursType == 7) {
            Glide.with(context).load(R.drawable.w_auto).into(imgFeaturs);
            txtFeaturs.setText(Html.fromHtml("Auto Response"));
            txtFeatursViewDay.setText(Html.fromHtml("Use Tools for 1 day"));
            txtRedeemFeatursCoin.setText(Html.fromHtml("700"));
        } else if (featursType == 8) {
            Glide.with(context).load(R.drawable.w_auto).into(imgFeaturs);
            txtFeaturs.setText(Html.fromHtml("Auto Response"));
            txtFeatursViewDay.setText(Html.fromHtml("Use Tools for 7 day"));
            txtRedeemFeatursCoin.setText(Html.fromHtml("3800"));
        } else if (featursType == 9) {
            Glide.with(context).load(R.drawable.w_export).into(imgFeaturs);
            txtFeaturs.setText(Html.fromHtml("Bulk Sender Import From Excel"));
            txtFeatursViewDay.setText(Html.fromHtml("Use Tools for 1 day"));
            txtRedeemFeatursCoin.setText(Html.fromHtml("1200"));
        } else if (featursType == 10) {
            Glide.with(context).load(R.drawable.w_export).into(imgFeaturs);
            txtFeaturs.setText(Html.fromHtml("Bulk Sender Import From Excel"));
            txtFeatursViewDay.setText(Html.fromHtml("Use Tools for 7 day"));
            txtRedeemFeatursCoin.setText(Html.fromHtml("5600"));
        }

        btnRedeemFeatursNow.setOnClickListener(v -> {
            int coin = Integer.parseInt(Preference.getTotal_coin());
            if (featursType == 5) {
                if (coin <= 800) {
                    btnFeatursMoreCoins.setVisibility(View.VISIBLE);
                    btnRedeemFeatursNow.setVisibility(View.GONE);
                    imgCoinFeatursError.setVisibility(View.VISIBLE);
                    btnFeatursMoreCoins.setOnClickListener(v1 -> startActivity(new Intent(context, ActivityMain.class)));
                } else {
                    FeatursCoinApiCall(1, 800, "Bulk sender", "Bulk One", bottomSheetDialog);
                }
            } else if (featursType == 6) {
                if (coin <= 4200) {
                    btnFeatursMoreCoins.setVisibility(View.VISIBLE);
                    btnRedeemFeatursNow.setVisibility(View.GONE);
                    imgCoinFeatursError.setVisibility(View.VISIBLE);
                    btnFeatursMoreCoins.setOnClickListener(v1 -> startActivity(new Intent(context, ActivityMain.class)));
                } else {
                    FeatursCoinApiCall(7, 4200, "Bulk sender", "Bulk Seven", bottomSheetDialog);
                }
            } else if (featursType == 7) {
                if (coin <= 700) {
                    btnFeatursMoreCoins.setVisibility(View.VISIBLE);
                    btnRedeemFeatursNow.setVisibility(View.GONE);
                    imgCoinFeatursError.setVisibility(View.VISIBLE);
                    btnFeatursMoreCoins.setOnClickListener(v1 -> startActivity(new Intent(context, ActivityMain.class)));
                } else {
                    FeatursCoinApiCall(1, 700, "Auto responce", "Auto One", bottomSheetDialog);
                }
            } else if (featursType == 8) {
                if (coin <= 3800) {
                    btnFeatursMoreCoins.setVisibility(View.VISIBLE);
                    btnRedeemFeatursNow.setVisibility(View.GONE);
                    imgCoinFeatursError.setVisibility(View.VISIBLE);
                    btnFeatursMoreCoins.setOnClickListener(v1 -> startActivity(new Intent(context, ActivityMain.class)));
                } else {
                    FeatursCoinApiCall(7, 3800, "Auto responce", "Auto Seven", bottomSheetDialog);
                }
            } else if (featursType == 9) {
                if (coin <= 1200) {
                    btnFeatursMoreCoins.setVisibility(View.VISIBLE);
                    btnRedeemFeatursNow.setVisibility(View.GONE);
                    imgCoinFeatursError.setVisibility(View.VISIBLE);
                    btnFeatursMoreCoins.setOnClickListener(v1 -> startActivity(new Intent(context, ActivityMain.class)));
                } else {
                    FeatursCoinApiCall(1, 1200, "Import excel", "Import One", bottomSheetDialog);
                }
            } else if (featursType == 10) {
                if (coin <= 5600) {
                    btnFeatursMoreCoins.setVisibility(View.VISIBLE);
                    btnRedeemFeatursNow.setVisibility(View.GONE);
                    imgCoinFeatursError.setVisibility(View.VISIBLE);
                    btnFeatursMoreCoins.setOnClickListener(v1 -> startActivity(new Intent(context, ActivityMain.class)));
                } else {
                    FeatursCoinApiCall(7, 5600, "Import excel", "Import Seven", bottomSheetDialog);
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void ShowBottomDialogAds(int adType) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_redeem_ads_bottom, null);
        bottomSheetDialog.setContentView(inflate);

        LinearLayout rlBottom = inflate.findViewById(R.id.rlBottom);
        RelativeLayout rlTop = inflate.findViewById(R.id.rlTop);
        TextView txtAdViewDay = inflate.findViewById(R.id.txtAdViewDay);
        TextView txtRedeemCoin = inflate.findViewById(R.id.txtRedeemCoin);
        TextView txtOne = inflate.findViewById(R.id.txtOne);
        TextView txtTwo = inflate.findViewById(R.id.txtTwo);
        TextView txtThree = inflate.findViewById(R.id.txtThree);
        TextView txtAdFree = inflate.findViewById(R.id.txtAdFree);
        LinearLayout btnRedeemNow = inflate.findViewById(R.id.btnRedeemNow);
        AppCompatButton btnMoreCoins = inflate.findViewById(R.id.btnMoreCoins);
        ImageView imgCoinError = inflate.findViewById(R.id.imgCoinError);

        if (Preference.getBooleanTheme(false)) {
            rlBottom.setBackground(ContextCompat.getDrawable(context, R.drawable.dialog_black));
            rlTop.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            txtOne.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtAdFree.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtAdViewDay.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }

        if (adType == 1) {
            txtAdViewDay.setText(Html.fromHtml("Ad-Free Viewing for 1 day"));
            txtRedeemCoin.setText(Html.fromHtml("600"));
        } else if (adType == 2) {
            txtAdViewDay.setText(Html.fromHtml("Ad-Free Viewing for 3 day"));
            txtRedeemCoin.setText(Html.fromHtml("1600"));
        } else if (adType == 3) {
            txtAdViewDay.setText(Html.fromHtml("Ad-Free Viewing for 7 day"));
            txtRedeemCoin.setText(Html.fromHtml("3200"));
        } else if (adType == 4) {
            txtAdViewDay.setText(Html.fromHtml("Ad-Free Viewing for 30 day"));
            txtRedeemCoin.setText(Html.fromHtml("8800"));
        }

        btnRedeemNow.setOnClickListener(v -> {
            int coin = Integer.parseInt(Preference.getTotal_coin());
            if (adType == 1) {
                if (coin <= 600) {
                    btnMoreCoins.setVisibility(View.VISIBLE);
                    btnRedeemNow.setVisibility(View.GONE);
                    imgCoinError.setVisibility(View.VISIBLE);
                    btnMoreCoins.setOnClickListener(v1 -> startActivity(new Intent(context, ActivityMain.class)));
                } else {
                    AdFreeCoinApiCall(1, 600, "One Day", bottomSheetDialog);
                }
            } else if (adType == 2) {
                if (coin <= 1600) {
                    btnMoreCoins.setVisibility(View.VISIBLE);
                    btnRedeemNow.setVisibility(View.GONE);
                    imgCoinError.setVisibility(View.VISIBLE);
                    btnMoreCoins.setOnClickListener(v1 -> startActivity(new Intent(context, ActivityMain.class)));
                } else {
                    AdFreeCoinApiCall(3, 1600, "Three Day", bottomSheetDialog);
                }
            } else if (adType == 3) {
                if (coin <= 3200) {
                    btnMoreCoins.setVisibility(View.VISIBLE);
                    btnRedeemNow.setVisibility(View.GONE);
                    imgCoinError.setVisibility(View.VISIBLE);
                    btnMoreCoins.setOnClickListener(v1 -> startActivity(new Intent(context, ActivityMain.class)));
                } else {
                    AdFreeCoinApiCall(7, 3200, "Seven Day", bottomSheetDialog);
                }
            } else if (adType == 4) {
                if (coin <= 8800) {
                    btnMoreCoins.setVisibility(View.VISIBLE);
                    btnRedeemNow.setVisibility(View.GONE);
                    imgCoinError.setVisibility(View.VISIBLE);
                    btnMoreCoins.setOnClickListener(v1 -> startActivity(new Intent(context, ActivityMain.class)));
                } else {
                    AdFreeCoinApiCall(30, 8800, "Thirty Day", bottomSheetDialog);
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void AdFreeCoinApiCall(int day, int coin, String dMatch, BottomSheetDialog bottomSheetDialog) {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getAdFree(Preference.getAdFree_coin(), Preference.getU_id(), String.valueOf(day), String.valueOf(coin)).enqueue(new Callback<AdFreeData>() {
            @Override
            public void onResponse(@NotNull Call<AdFreeData> call, @NotNull Response<AdFreeData> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getFlag().equals("true")) {
                        Constant.DismissProgress();
                        if (response.body().getReedem()) {
                            switch (dMatch) {
                                case "One Day":
                                    Preference.setAds_one_day("true");
                                    Preference.setTotal_coin(response.body().getCoin());
                                    onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                                    CongratView(bottomSheetDialog);
                                    break;
                                case "Three Day":
                                    Preference.setAds_three_day("true");
                                    Preference.setTotal_coin(response.body().getCoin());
                                    onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                                    CongratView(bottomSheetDialog);
                                    break;
                                case "Seven Day":
                                    Preference.setAds_seven_day("true");
                                    Preference.setTotal_coin(response.body().getCoin());
                                    onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                                    CongratView(bottomSheetDialog);
                                    break;
                                case "Thirty Day":
                                    Preference.setAds_thirty_day("true");
                                    Preference.setTotal_coin(response.body().getCoin());
                                    onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                                    CongratView(bottomSheetDialog);
                                    break;
                            }
                        }
                    } else if (response.body().getFlag().equals("false")) {
                        Constant.DismissProgress();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<AdFreeData> call, @NotNull Throwable t) {
                Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FeatursCoinApiCall(int day, int coin, String f_name, String sMatch, BottomSheetDialog bottomSheetDialog) {
        Constant.ShowProgress();
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getFeaturs(Preference.getAdFeaturs_coin(), Preference.getU_id(), String.valueOf(day), String.valueOf(coin), f_name).enqueue(new Callback<FeatursData>() {
            @Override
            public void onResponse(@NotNull Call<FeatursData> call, @NotNull Response<FeatursData> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Constant.DismissProgress();
                    if (response.body().getFlag().equals("true")) {
                        if (response.body().getReedem()) {
                            switch (sMatch) {
                                case "Bulk One":
                                    Preference.setBulk_one_day("true");
                                    Preference.setTotal_coin(response.body().getCoin());
                                    onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                                    CongratView(bottomSheetDialog);
                                    break;
                                case "Bulk Seven":
                                    Preference.setBulk_seven_day("true");
                                    Preference.setTotal_coin(response.body().getCoin());
                                    onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                                    CongratView(bottomSheetDialog);
                                    break;
                                case "Auto One":
                                    Preference.setAuto_one_day("true");
                                    Preference.setTotal_coin(response.body().getCoin());
                                    onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                                    CongratView(bottomSheetDialog);
                                    break;
                                case "Auto Seven":
                                    Preference.setAuto_seven_day("true");
                                    Preference.setTotal_coin(response.body().getCoin());
                                    onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                                    CongratView(bottomSheetDialog);
                                    break;
                                case "Import One":
                                    Preference.setImport_one_day("true");
                                    Preference.setTotal_coin(response.body().getCoin());
                                    onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                                    CongratView(bottomSheetDialog);
                                    break;
                                case "Import Seven":
                                    Preference.setImport_seven_day("true");
                                    Preference.setTotal_coin(response.body().getCoin());
                                    onLoginCoinEvent.coinEvent(Preference.getTotal_coin());
                                    CongratView(bottomSheetDialog);
                                    break;
                            }
                        }
                    } else if (response.body().getFlag().equals("false")) {
                        Constant.DismissProgress();
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<FeatursData> call, @NotNull Throwable t) {
                Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void CongratView(BottomSheetDialog bottomSheetDialog) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_congratulation);
        bottomSheetDialog.dismiss();
        dialog.show();

        new Handler().postDelayed(() -> {
            dialog.dismiss();
            startActivity(new Intent(context, ActivityMain.class).addFlags(268468224));
        }, 4000);
    }
}