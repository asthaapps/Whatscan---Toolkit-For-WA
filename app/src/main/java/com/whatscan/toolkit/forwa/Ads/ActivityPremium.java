package com.whatscan.toolkit.forwa.Ads;

import static com.whatscan.toolkit.forwa.Util.Constant.getDate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.whatscan.toolkit.forwa.Adapter.ReviewPlanAdapter;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.GetSet.VeryfyData;
import com.whatscan.toolkit.forwa.GetSet.VeryfyPlan;
import com.whatscan.toolkit.forwa.Other.CommonWebView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPremium extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    public List<VeryfyData> veryfyDataList = new ArrayList<>();
    public RelativeLayout rl_classic, rl_premium, rl_master, bt_subscribe, rl_offer_click, rl_master_offer;
    public TextView duration_classic, duration_premium, duration_master, price_classic, price_premium, price_master,
            no_price_offer, price_offer, no_price_vip, price_vip;
    public int tab = 0, select_plan = 2;
    RelativeLayout rl_premium_plan, rl_premium_main, rl_1, rl_2, rl_3, rl_bottom, rv_limited_offer, rv_master_required, rl_master_design,
            rl_premium_design, rl_classic_design;
    TextView text_title, text_sub_title, text_rev, tv_figure, bulk_text, bulk_text_1, faq_title, tv_download, tv_f1, tv_user, tv_f2, tv_rating, tv_f3, chat_us_title;
    LinearLayout ll_limited_offer, ll_required_plan, ll_classic_required, ll_master_required;
    ImageView icon_faq_next, icon_chat_us_next;
    TextView tv_classic, tv_premium, tv_master;
    RecyclerView rv_review;
    Integer[] ImageArray = {R.drawable.review1, R.drawable.review2, R.drawable.review3, R.drawable.review4, R.drawable.review5, R.drawable.review6};
    String[] NameArray = {"Mark Evans", "Emeka Augustine", "Harsh Desai", "Martha Linus", "Jones Aggrey", "Aditi Chauhan"};
    String[] ReviewArray = {"Whatscan is the best experience you need in your online outreaches. The best thing that can happen to you and your business. They help you bridge the communication gap between you, your team, and prospects for maximum customer satisfaction and a positive experience. I've been a fan of the app for more than a year already; every update is done on the app always translates to a better experience for the user. Great work",
            "This is the perfect all-in-one WhatsApp marketing tool you can get anywhere. I gave it 5stars because it is precisely what I have been looking for. When I am using the bulk message feature, it's an excellent application feature.",
            "This app is perfect for all uses, being the best app for WhatsApp. It virtually has all features anybody would want. This could include sending bulk, receiving status, downloading contacts, and speedily sending videos. It even has a fantastic chat analysis feature! A must-buy.",
            "The best application for WhatsApp Tool for sending bulk messages, sending messages without saving numbers, status downloader, and much more exciting tool which this one application is carrying. Love this application, and this application is going to stay forever in my app drawer.",
            "I've been using this application since last year. This app helped me a lot and saved my time using special tools and an excellent application with advantageous features. Many valuable tools are available for business uses, and the best part of the app is its simple and clean UI makes it more convenient to use this app. Anyone new person can use this app easily. I recommend this app to everyone using WhatsApp for personal or business use.",
            "Do not struggle with the crowd. Stand apart with this app and get going on the go. Easy to set up and easy to use. Can't ask for better. The UI is sleek and functional. Significant relief for the novices to set up bulk sending on WhatsApp. Thanks to the developers for this app."};
    private BillingClient billingClient;
    PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        }
    };
    private RadioButton radio_btn_monthly, radio_btn_yearly;
    private RadioGroup radio_group;

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            Constant.ShowProgress();
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        PlanApiCall(purchase);
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityPremium.this);
        setContentView(R.layout.activity_premium_plan);

        setStatusBar();

        Utils.CheckConnection(ActivityPremium.this, findViewById(R.id.rl_premium));


        TextView txt_term = findViewById(R.id.txt_term);
        ImageView la_back = findViewById(R.id.la_back);
        rl_classic = findViewById(R.id.rl_classic);
        rl_premium = findViewById(R.id.rl_premium);
        rl_master = findViewById(R.id.rl_master);
        bt_subscribe = findViewById(R.id.bt_subscribe);
        radio_group = findViewById(R.id.radio_group);
        radio_btn_monthly = findViewById(R.id.radio_btn_monthly);
        radio_btn_yearly = findViewById(R.id.radio_btn_yearly);
        rl_offer_click = findViewById(R.id.rl_offer_click);
        rl_master_offer = findViewById(R.id.rl_master_offer);
        rv_review = findViewById(R.id.rv_review);
        duration_classic = findViewById(R.id.duration_classic);
        duration_premium = findViewById(R.id.duration_premium);
        duration_master = findViewById(R.id.duration_master);
        price_classic = findViewById(R.id.price_classic);
        price_premium = findViewById(R.id.price_premium);
        price_master = findViewById(R.id.price_master);
        no_price_offer = findViewById(R.id.no_price_offer);
        price_offer = findViewById(R.id.price_offer);
        no_price_vip = findViewById(R.id.no_price_vip);
        price_vip = findViewById(R.id.price_vip);
        rl_premium_plan = findViewById(R.id.rl_premium_plan);
        rl_premium_main = findViewById(R.id.rl_premium_main);
        rv_limited_offer = findViewById(R.id.rv_limited_offer);
        rv_master_required = findViewById(R.id.rv_master_required);
        ll_limited_offer = findViewById(R.id.ll_limited_offer);
        ll_required_plan = findViewById(R.id.ll_required_plan);
        ll_classic_required = findViewById(R.id.ll_classic_required);
        ll_master_required = findViewById(R.id.ll_master_required);
        rl_bottom = findViewById(R.id.rl_bottom);
        icon_faq_next = findViewById(R.id.icon_faq_next);
        icon_chat_us_next = findViewById(R.id.icon_chat_us_next);
        tv_premium = findViewById(R.id.tv_premium);
        tv_classic = findViewById(R.id.tv_classic);
        tv_master = findViewById(R.id.tv_master);


        text_title = findViewById(R.id.text_title);
        text_sub_title = findViewById(R.id.text_sub_title);
        text_rev = findViewById(R.id.text_rev);
        tv_figure = findViewById(R.id.tv_figure);
        bulk_text = findViewById(R.id.bulk_text);
        bulk_text_1 = findViewById(R.id.bulk_text_1);
        chat_us_title = findViewById(R.id.chat_us_title);

        tv_download = findViewById(R.id.tv_download);
        tv_f1 = findViewById(R.id.tv_f1);
        tv_user = findViewById(R.id.tv_user);
        tv_f2 = findViewById(R.id.tv_f2);
        tv_rating = findViewById(R.id.tv_rating);
        tv_f3 = findViewById(R.id.tv_f3);

        faq_title = findViewById(R.id.faq_title);
        rl_1 = findViewById(R.id.rl_1);
        rl_2 = findViewById(R.id.rl_2);
        rl_3 = findViewById(R.id.rl_3);


        rl_classic_design = findViewById(R.id.rl_classic_design);
        rl_premium_design = findViewById(R.id.rl_premium_design);
        rl_master_design = findViewById(R.id.rl_master_design);
        RelativeLayout rl_faq = findViewById(R.id.rl_faq);
        RelativeLayout rl_chat_us = findViewById(R.id.rl_chat_us);
        LottieAnimationView la_info_plan = findViewById(R.id.la_info_plan);

        la_info_plan.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityPremium.this);
            View inflate = LayoutInflater.from(ActivityPremium.this).inflate(R.layout.dialog_plan_details, null);
            bottomSheetDialog.setContentView(inflate);
            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.show();
        });

        // FilterChangeGroup();
        setPrice();
        intProcess();
        radio_group.setOnCheckedChangeListener(this);

        Constant.IntProgress(ActivityPremium.this);


        if (Preference.getBooleanTheme(false)) { // Dark
            // rl_premium_plan.setBackgroundColor(ContextCompat.getColor(ActivityPremium.this, R.color.darkBlack));
            setStatusBarTheme();
            FilterChangeGroup_1();
            rl_premium_main.setBackgroundColor(ContextCompat.getColor(ActivityPremium.this, R.color.darkBlack));
            rl_bottom.setBackgroundColor(ContextCompat.getColor(ActivityPremium.this, R.color.darkBlack_1));
            text_title.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            text_sub_title.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            text_rev.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            tv_figure.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            price_offer.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            faq_title.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            bulk_text.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            bulk_text_1.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            price_vip.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            txt_term.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            tv_download.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            tv_f1.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            tv_user.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            tv_f2.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            tv_rating.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            tv_f3.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            chat_us_title.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            rl_offer_click.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.plan_offer_border_1));
            rl_chat_us.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_black));
            rl_faq.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_black));
            rl_1.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_black));
            rl_2.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_black));
            rl_3.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_black));
            rl_master_offer.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.plan_master_border_1));
            rv_limited_offer.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_black));
            rv_master_required.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_black));
            ll_limited_offer.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.offer_top_1));
            ll_required_plan.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.offer_top_1));
            ll_classic_required.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.offer_top_1));
            ll_master_required.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.offer_top_1));
            icon_faq_next.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.setting_next_bg_black));
            icon_chat_us_next.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.setting_next_bg_black));
            icon_faq_next.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            icon_chat_us_next.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            //  icon_faq_next.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.offer_top_1));

        } else { // Light
            setStatusBar();
            FilterChangeGroup();
            // rl_premium_plan.setBackgroundColor(ContextCompat.getColor(ActivityPremium.this, R.color.card_color));
            rl_premium_main.setBackgroundColor(ContextCompat.getColor(ActivityPremium.this, R.color.card_color));
            rl_bottom.setBackgroundColor(ContextCompat.getColor(ActivityPremium.this, R.color.card_color));
            text_title.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            text_sub_title.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            text_rev.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            tv_figure.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            price_offer.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            bulk_text.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            bulk_text_1.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            price_vip.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            faq_title.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            txt_term.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            tv_download.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            tv_f1.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            tv_user.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            tv_f2.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            tv_rating.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            tv_f3.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            chat_us_title.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            rl_offer_click.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.plan_offer_border));
            rl_faq.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_white));
            rl_chat_us.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_white));
            rl_1.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_white));
            rl_2.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_white));
            rl_3.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_white));
            rl_master_offer.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.plan_master_border));
            rv_limited_offer.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_white));
            rv_master_required.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.shape_white));
            ll_limited_offer.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.offer_top));
            ll_required_plan.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.offer_top));
            ll_classic_required.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.offer_top));
            ll_master_required.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.offer_top));
            icon_faq_next.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.setting_next_bg));
            icon_chat_us_next.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.setting_next_bg));
            //    icon_faq_next.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.setting_next_bg));

            icon_faq_next.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            icon_chat_us_next.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityPremium.this, RecyclerView.HORIZONTAL, false);
        rv_review.setLayoutManager(linearLayoutManager);
        ReviewPlanAdapter reviewPlanAdapter = new ReviewPlanAdapter(ActivityPremium.this, ImageArray, NameArray, ReviewArray);
        rv_review.setAdapter(reviewPlanAdapter);
        rv_review.setHasFixedSize(true);

        la_back.setOnClickListener(v -> onBackPressed());
        rl_faq.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorTools));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(ActivityPremium.this, Uri.parse("https://whatscan.co/faqs/"));
        });

        rl_chat_us.setOnClickListener(v -> {
            try {
                if (Constant.whatsappInstalledOrNot(ActivityPremium.this)) {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("91" + Preference.getChatNo()) + "@s.whatsapp.net");
                    startActivity(sendIntent);
                } else {
                    Uri uri = Uri.parse("market://details?id=com.whatsapp");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    Toast.makeText(ActivityPremium.this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                    startActivity(goToMarket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        if (Preference.getBooleanTheme(false)) {
            rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
            rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
            tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));

            price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
        } else {
            rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
            rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
            tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));

            price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
        }

        rl_classic.setOnClickListener(v -> {
            if (tab == 0) {
                select_plan = 1;
            } else if (tab == 1) {
                select_plan = 2;
            }
            rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_select));

            if (Preference.getBooleanTheme(false)) {
                rl_premium.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                tv_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                price_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));

            } else {
                rl_premium.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                tv_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                price_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            }


            rl_classic_design.setVisibility(View.VISIBLE);
            rl_premium_design.setVisibility(View.GONE);
            rl_master_design.setVisibility(View.GONE);
        });


        rl_premium.setOnClickListener(v -> {
            if (tab == 0) {
                select_plan = 3;
            } else if (tab == 1) {
                select_plan = 4;
            }
            rl_premium.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_select1));
            if (Preference.getBooleanTheme(false)) {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));

                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            } else {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));

                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            }

            rl_classic_design.setVisibility(View.GONE);
            rl_premium_design.setVisibility(View.VISIBLE);
            rl_master_design.setVisibility(View.GONE);
        });


        rl_master.setOnClickListener(v -> {
            if (tab == 0) {
                select_plan = 5;
            } else if (tab == 1) {
                select_plan = 6;
            }
            rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_select2));
            if (Preference.getBooleanTheme(false)) {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                rl_premium.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                tv_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                price_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            } else {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                rl_premium.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                tv_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_premium.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));

            }

            rl_classic_design.setVisibility(View.GONE);
            rl_premium_design.setVisibility(View.GONE);
            rl_master_design.setVisibility(View.VISIBLE);
        });

        rl_offer_click.setOnClickListener(v -> {
            purchase_plan(Preference.getOffer_time_key());   // Limited Time Offer
        });

        rl_master_offer.setOnClickListener(v -> {
            purchase_plan(Preference.getVip_key());   // Master Offer
        });

        bt_subscribe.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                if (select_plan == 1) { // Classic Monthly
                    purchase_plan(Preference.getClassic_monthly_key());
                } else if (select_plan == 2) { // Classic Yearly
                    purchase_plan(Preference.getClassic_yearly_key());
                } else if (select_plan == 3) {  // Premium Monthly
                    purchase_plan(Preference.getPremium_monthly_key());
                } else if (select_plan == 4) {  // Premium Yearly
                    purchase_plan(Preference.getPremium_yearly_key());
                } else if (select_plan == 5) {  // Master Monthly
                    purchase_plan(Preference.getMaster_monthly_key());
                } else if (select_plan == 6) {  // Master Yearly
                    purchase_plan(Preference.getMaster_yearly_key());
                }
            } else {
                Constant.BottomSheetDialogLogIn(ActivityPremium.this);
            }
        });
        SpannableString SpanString = new SpannableString(
                "By signing in, you agree to the Terms of Use and Privacy Policy");

        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(ActivityPremium.this, CommonWebView.class);
                mIntent.putExtra("tc", true);
                startActivity(mIntent);
            }
        };

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(ActivityPremium.this, CommonWebView.class);
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


        //  FindView();
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.card_color));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.card_color));
    }

    public void setStatusBarTheme() {
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityPremium.this, R.color.darkBlack));
    }


    private void FilterChangeGroup() {
        if (radio_group.getCheckedRadioButtonId() == R.id.radio_btn_monthly) {
            duration_classic.setText("@Monthly");
            duration_premium.setText("@Monthly");
            duration_master.setText("@Monthly");
            radio_btn_monthly.setBackgroundResource(R.drawable.shape_app_color_m);
            radio_btn_monthly.setTextColor(getResources().getColor(R.color.colorWhite));
            radio_btn_yearly.setTextColor(getResources().getColor(R.color.colorBlack));
            radio_btn_yearly.setBackgroundResource(R.drawable.shape_non_select_y);
            tab = 0;
            setPrice();
            select_plan = 3;
            rl_premium.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_select1));
            if (Preference.getBooleanTheme(false)) {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));

                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            } else {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));

                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            }

            rl_classic_design.setVisibility(View.GONE);
            rl_premium_design.setVisibility(View.VISIBLE);
            rl_master_design.setVisibility(View.GONE);
        } else if (radio_group.getCheckedRadioButtonId() == R.id.radio_btn_yearly) {
            tab = 1;
            setPrice();
            select_plan = 4;
            duration_classic.setText("@Yearly");
            duration_premium.setText("@Yearly");
            duration_master.setText("@Yearly");
            radio_btn_monthly.setBackgroundResource(R.drawable.shape_non_select_m);
            radio_btn_monthly.setTextColor(getResources().getColor(R.color.colorBlack));
            radio_btn_yearly.setTextColor(getResources().getColor(R.color.colorWhite));
            radio_btn_yearly.setBackgroundResource(R.drawable.shape_app_color_y);
            rl_premium.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_select1));
            if (Preference.getBooleanTheme(false)) {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));

                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            } else {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));

                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            }

            rl_classic_design.setVisibility(View.GONE);
            rl_premium_design.setVisibility(View.VISIBLE);
            rl_master_design.setVisibility(View.GONE);
        }
    }

    private void FilterChangeGroup_1() {
        if (radio_group.getCheckedRadioButtonId() == R.id.radio_btn_monthly) {
            duration_classic.setText("@Monthly");
            duration_premium.setText("@Monthly");
            duration_master.setText("@Monthly");
            radio_btn_monthly.setBackgroundResource(R.drawable.shape_app_color_m);
            radio_btn_monthly.setTextColor(getResources().getColor(R.color.colorWhite));
            radio_btn_yearly.setTextColor(getResources().getColor(R.color.colorWhite));
            radio_btn_yearly.setBackgroundResource(R.drawable.shape_non_select_y_1);
            tab = 0;
            setPrice();
            select_plan = 3;
            rl_premium.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_select1));
            if (Preference.getBooleanTheme(false)) {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));

                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            } else {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));

                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            }

            rl_classic_design.setVisibility(View.GONE);
            rl_premium_design.setVisibility(View.VISIBLE);
            rl_master_design.setVisibility(View.GONE);
        } else if (radio_group.getCheckedRadioButtonId() == R.id.radio_btn_yearly) {
            tab = 1;
            select_plan = 4;
            setPrice();
            duration_classic.setText("@Yearly");
            duration_premium.setText("@Yearly");
            duration_master.setText("@Yearly");
            radio_btn_monthly.setBackgroundResource(R.drawable.shape_non_select_m_1);
            radio_btn_monthly.setTextColor(getResources().getColor(R.color.colorWhite));
            radio_btn_yearly.setTextColor(getResources().getColor(R.color.colorWhite));
            radio_btn_yearly.setBackgroundResource(R.drawable.shape_app_color_y);
            rl_premium.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_select1));
            if (Preference.getBooleanTheme(false)) {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom_1));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));

                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
                duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorWhite));
            } else {
                rl_classic.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                rl_master.setBackground(ContextCompat.getDrawable(ActivityPremium.this, R.drawable.border_bottom));
                tv_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                tv_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));

                price_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_classic.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                price_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
                duration_master.setTextColor(ContextCompat.getColor(ActivityPremium.this, R.color.colorBlack));
            }

            rl_classic_design.setVisibility(View.GONE);
            rl_premium_design.setVisibility(View.VISIBLE);
            rl_master_design.setVisibility(View.GONE);
        }
    }

    private void setPrice() {
        if (tab == 0) {
            price_classic.setText(Preference.getClassic_monthly_price());
            price_premium.setText(Preference.getPremium_monthly_price());
            price_master.setText(Preference.getMaster_monthly_price());
            price_offer.setText(Preference.getOffer_time_price());
            no_price_offer.setText(Preference.getMaster_yearly_price());
            price_vip.setText(Preference.getVip_price());
            no_price_vip.setText(Preference.getVip_close_price());
        } else if (tab == 1) {
            price_classic.setText(Preference.getClassic_yearly_price());
            price_premium.setText(Preference.getPremium_yearly_price());
            price_master.setText(Preference.getMaster_yearly_price());
            price_offer.setText(Preference.getOffer_time_price());
            no_price_offer.setText(Preference.getMaster_yearly_price());
            price_vip.setText(Preference.getVip_price());
            no_price_vip.setText(Preference.getVip_close_price());
        }
    }

    private void purchase_plan(String plan_name) {
        List<String> skuList = new ArrayList<>();
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        if (billingClient != null) {
            skuList.add(plan_name);
            params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        } else {
            Toast.makeText(ActivityPremium.this, "Billing not initialized", Toast.LENGTH_SHORT).show();
        }

        billingClient.querySkuDetailsAsync(params.build(), (billingResult, list) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(list.get(0)).build();
                int responseCode = billingClient.launchBillingFlow(ActivityPremium.this, billingFlowParams).getResponseCode();
            }
        });
    }


    private void intProcess() {
        billingClient = BillingClient.newBuilder(ActivityPremium.this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NotNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    getSkuDetails();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void getSkuDetails() {
        List<String> skuList_sub = new ArrayList<>();
        skuList_sub.add(Preference.getClassic_monthly_key());
        skuList_sub.add(Preference.getPremium_monthly_key());
        skuList_sub.add(Preference.getMaster_monthly_key());
        skuList_sub.add(Preference.getOffer_time_key());
        skuList_sub.add(Preference.getVip_key());
        skuList_sub.add(Preference.getVip_close_key());
        skuList_sub.add(Preference.getClassic_yearly_key());
        skuList_sub.add(Preference.getPremium_yearly_key());
        skuList_sub.add(Preference.getMaster_yearly_key());
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList_sub).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(), (billingResult, list) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                Toast.makeText(this, list.toString(), Toast.LENGTH_SHORT).show();
                if (Preference.getClassic_monthly_price().isEmpty()) {
                    Preference.setClassic_monthly_price(list.get(0).getPrice());
                } else if (Preference.getClassic_yearly_price().isEmpty()) {
                    Preference.setClassic_yearly_price(list.get(1).getPrice());
                } else if (Preference.getMaster_monthly_price().isEmpty()) {
                    Preference.setMaster_monthly_price(list.get(2).getPrice());
                } else if (Preference.getMaster_yearly_price().isEmpty()) {
                    Preference.setMaster_yearly_price(list.get(3).getPrice());
                } else if (Preference.getOffer_time_price().isEmpty()) {
                    Preference.setOffer_time_price(list.get(4).getPrice());
                } else if (Preference.getPremium_monthly_price().isEmpty()) {
                    Preference.setPremium_monthly_price(list.get(5).getPrice());
                } else if (Preference.getPremium_yearly_price().isEmpty()) {
                    Preference.setPremium_yearly_price(list.get(6).getPrice());
                } else if (Preference.getVip_close_price().isEmpty()) {
                    Preference.setVip_close_price(list.get(7).getPrice());
                } else if (Preference.getVip_price().isEmpty()) {
                    Preference.setVip_price(list.get(8).getPrice());
                }
                setPrice();
            }
        });
    }


    private void PlanApiCall(Purchase purchase) {
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);

        apiInterface.getVerifyPaymentPlanBilling(Preference.getVerify_Plan(), Preference.getU_id(), purchase.getSkus().get(0), purchase.getPurchaseToken(), getDate()).enqueue(new Callback<VeryfyPlan>() {
            @Override
            public void onResponse(@NotNull Call<VeryfyPlan> call, @NotNull Response<VeryfyPlan> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getFlag().equals("true")) {
                        Constant.DismissProgress();
                        veryfyDataList = response.body().getData();
                        for (int i = 0; i < veryfyDataList.size(); i++) {
                            if (veryfyDataList.get(i).getPlanActive().equals(Preference.getClassic_monthly_key())) {
                                Preference.setActive_CkeyM("true");
                            } else if (veryfyDataList.get(i).getPlanActive().equals(Preference.getClassic_yearly_key())) {
                                Preference.setActive_CkeyY("true");
                            } else if (veryfyDataList.get(i).getPlanActive().equals(Preference.getPremium_monthly_key())) {
                                Preference.setActive_PkeyM("true");
                            } else if (veryfyDataList.get(i).getPlanActive().equals(Preference.getPremium_yearly_key())) {
                                Preference.setActive_PkeyY("true");
                            } else if (veryfyDataList.get(i).getPlanActive().equals(Preference.getMaster_monthly_key())) {
                                Preference.setActive_MkeyM("true");
                            } else if (veryfyDataList.get(i).getPlanActive().equals(Preference.getMaster_yearly_key())) {
                                Preference.setActive_MkeyY("true");
                            }
                        }
                        showDialogPaymentSuccess(purchase);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<VeryfyPlan> call, @NotNull Throwable t) {

            }
        });

    }

    @SuppressLint("WrongConstant")
    private void showDialogPaymentSuccess(Purchase purchase) {
        Dialog dialog = new Dialog(ActivityPremium.this, R.style.ios_sheet_style);
        dialog.setContentView(R.layout.dialog_payment_success);

        CardView cardOne = dialog.findViewById(R.id.cardOne);
        CardView cardTwo = dialog.findViewById(R.id.cardTwo);
        TextView txtDate = dialog.findViewById(R.id.txtDate);
        TextView txtTime = dialog.findViewById(R.id.txtTime);
        TextView txtUName = dialog.findViewById(R.id.txtUName);
        TextView txtUMail = dialog.findViewById(R.id.txtUMail);
        TextView txtOrderId = dialog.findViewById(R.id.txtOrderId);
        TextView txtOne = dialog.findViewById(R.id.txtOne);
        TextView heading = dialog.findViewById(R.id.heading);
        ImageView imgCopyId = dialog.findViewById(R.id.imgCopyId);

        if (Preference.getBooleanTheme(false)) {
            cardOne.setCardBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            cardTwo.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtDate.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTime.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtUName.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            heading.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtOrderId.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        Date date = new Date(purchase.getPurchaseTime());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String purchaseTime = sdf.format(date);

        String OrderID = purchase.getOrderId();
        txtDate.setText(getDate());
        txtTime.setText(purchaseTime);
        txtOrderId.setText(OrderID);
        txtUName.setText(Preference.getUserName());
        txtUMail.setText(Preference.getEmail());

        imgCopyId.setOnClickListener(v -> Utils.setClipboard(ActivityPremium.this, OrderID));

        dialog.findViewById(R.id.fab).setOnClickListener(v -> {
            dialog.dismiss();
        });

        try {
            dialog.show();
        } catch (WindowManager.BadTokenException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (Preference.getBooleanTheme(false)) {
            FilterChangeGroup_1();
        } else {
            FilterChangeGroup();
        }
    }
}