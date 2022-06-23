package com.whatscan.toolkit.forwa.Fragment;



import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smarteist.autoimageslider.SliderView;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.Adapter.ReviewSliderAdapter;
import com.whatscan.toolkit.forwa.Adapter.SettingSliderAdapter;
import com.whatscan.toolkit.forwa.Ads.ActivityPremium;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.GetSet.NotificationModel;
import com.whatscan.toolkit.forwa.GetSet.ReportRequest;
import com.whatscan.toolkit.forwa.Other.ActivityPermission;
import com.whatscan.toolkit.forwa.Other.ActivityProfile;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.ReferCoin.ActivityCoin;
import com.whatscan.toolkit.forwa.User.ActivityLogin;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.whatscan.toolkit.forwa.WChatLocker.ActivityChatLock;
import com.whatscan.toolkit.forwa.Web.ActivityPatternLock;
import com.yariksoffice.lingver.Lingver;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSetting extends Fragment {
    public Context context;
    public SwitchCompat globalDark;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public String switchON;
    public boolean switchOnOff;
    public View viewS;
    public RelativeLayout rlSetting, rl_details, rl_login,rl_chat_us;
    public TextView tv_general, tv_security, tv_about, tv_legal, tv_follow, txtAppVersn, txtWebsite;
    public TextView tv_profile, tv_notification, tv_premium, tv_darkMode, tv_figure;
    public TextView tv_pattern, tv_Changepattern, tv_change_chat,chat_us_title;
    public TextView tv_language, tv_features, tv_developer, tv_rate, tv_contact, tv_bug, tv_share, tv_download, tv_user, tv_rating, tv_f1, tv_f2, tv_f3;
    public TextView tv_privacy, tv_term, tv_username, tv_email, tv_toolId, tv_sign, tv_permission, tv_Reward, tv_Faq, tv_Blog, tv_Update;
    public ImageView iv_profile, iv_premium, iv_pattern, iv_ChangePattern, iv_ChangeChatLocker, iv_language;
    public ImageView iv_developer, iv_rate, iv_contact, iv_bug, iv_share, iv_privacy, iv_term, toolIdCopy;
    public ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12, img13, img14, img15, img17, img18, img19,icon_chat_us_next;
    public LinearLayout llGeneral, llSecurity, llAboutUs, llLegal, llFollow;
    public RoundedImageView imgProfile;
    int languageCode = 0;

    Integer[] ImageArray = {R.drawable.review1, R.drawable.review2, R.drawable.review3, R.drawable.review4, R.drawable.review5, R.drawable.review6};
    String[] NameArray = {"Mark Evans", "Emeka Augustine", "Meet Ramani", "Martha Linus", "Jones Aggrey", "Aditi Chauhan"};
    String[] ReviewArray = {"Whatscan is the best experience you need in your online outreaches. The best thing that can happen to you and your business. They help you bridge the communication gap between you, your team, and prospects for maximum customer satisfaction and a positive experience. I've been a fan of the app for more than a year already; every update is done on the app always translates to a better experience for the user. Great work",
            "This is the perfect all-in-one WhatsApp marketing tool you can get anywhere. I gave it 5stars because it is precisely what I have been looking for. When I am using the bulk message feature, it's an excellent application feature.",
            "This app is perfect for all uses, being the best app for WhatsApp. It virtually has all features anybody would want. This could include sending bulk, receiving status, downloading contacts, and speedily sending videos. It even has a fantastic chat analysis feature! A must-buy.",
            "The best application for WhatsApp Tool for sending bulk messages, sending messages without saving numbers, status downloader, and much more exciting tool which this one application is carrying. Love this application, and this application is going to stay forever in my app drawer.",
            "I've been using this application since last year. This app helped me a lot and saved my time using special tools and an excellent application with advantageous features. Many valuable tools are available for business uses, and the best part of the app is its simple and clean UI makes it more convenient to use this app. Anyone new person can use this app easily. I recommend this app to everyone using WhatsApp for personal or business use.",
            "Do not struggle with the crowd. Stand apart with this app and get going on the go. Easy to set up and easy to use. Can't ask for better. The UI is sleek and functional. Significant relief for the novices to set up bulk sending on WhatsApp. Thanks to the developers for this app."};
    private BottomSheetDialog bottomSheetDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewS = inflater.inflate(R.layout.fragment_setting, container, false);

        context = getContext();

        sharedPreferences = context.getSharedPreferences("MyPreferences", 0);
        editor = sharedPreferences.edit();
        editor.apply();

        FindView();

        return viewS;
    }

    private void FindView() {
        LottieAnimationView la_twitter = viewS.findViewById(R.id.la_twitter);
        LottieAnimationView la_ins = viewS.findViewById(R.id.la_ins);
        LottieAnimationView la_fb = viewS.findViewById(R.id.la_fb);
        LottieAnimationView la_youtube = viewS.findViewById(R.id.la_youtube);
        SwitchCompat notification_switch = viewS.findViewById(R.id.notification_switch);
        SwitchCompat pattern_switch = viewS.findViewById(R.id.pattern_switch);


        RelativeLayout rlChangePattern = viewS.findViewById(R.id.rlChangePattern);
        RelativeLayout rlReqFeature = viewS.findViewById(R.id.rlReqFeature);
        RelativeLayout rlDeveloper = viewS.findViewById(R.id.rlDeveloper);
        RelativeLayout rlRateUs = viewS.findViewById(R.id.rlRateUs);
        RelativeLayout rlContact = viewS.findViewById(R.id.rlContact);
        RelativeLayout rlBugReport = viewS.findViewById(R.id.rlBugReport);
        RelativeLayout rlShare = viewS.findViewById(R.id.rlShare);
        RelativeLayout rlPrivacy = viewS.findViewById(R.id.rlPrivacy);
        RelativeLayout rlTerm = viewS.findViewById(R.id.rlTerm);
        RelativeLayout rlPrePlan = viewS.findViewById(R.id.rlPrePlan);
        RelativeLayout rl_notification = viewS.findViewById(R.id.rl_notification);
        RelativeLayout rlChangeChatLocker = viewS.findViewById(R.id.rlChangeChatLocker);
        RelativeLayout rlLanguage = viewS.findViewById(R.id.rlLanguage);
        RelativeLayout rlProfile = viewS.findViewById(R.id.rlProfile);
        RelativeLayout rlSignOut = viewS.findViewById(R.id.rlSignOut);
        RelativeLayout rlPermission = viewS.findViewById(R.id.rlPermission);
        RelativeLayout rlReward = viewS.findViewById(R.id.rlReward);
        RelativeLayout rlFaq = viewS.findViewById(R.id.rlFaq);
        RelativeLayout rlBlog = viewS.findViewById(R.id.rlBlog);
        RelativeLayout rlUpdate = viewS.findViewById(R.id.rlUpdate);
        RelativeLayout rl_1 = viewS.findViewById(R.id.rl_1);
        RelativeLayout rl_2 = viewS.findViewById(R.id.rl_2);
        RelativeLayout rl_3 = viewS.findViewById(R.id.rl_3);
        ImageView iv_dark = viewS.findViewById(R.id.iv_dark);
        rl_details = viewS.findViewById(R.id.rl_details);
        rl_login = viewS.findViewById(R.id.rl_login);
        globalDark = viewS.findViewById(R.id.globalDark);
        rlSetting = viewS.findViewById(R.id.rlSetting);
        llGeneral = viewS.findViewById(R.id.llGeneral);
        llSecurity = viewS.findViewById(R.id.llSecurity);
        llAboutUs = viewS.findViewById(R.id.llAboutUs);
        llLegal = viewS.findViewById(R.id.llLegal);
        llFollow = viewS.findViewById(R.id.llFollow);
        txtAppVersn = viewS.findViewById(R.id.txtAppVersn);
        txtWebsite = viewS.findViewById(R.id.txtWebsite);
        tv_general = viewS.findViewById(R.id.tv_general);
        tv_security = viewS.findViewById(R.id.tv_security);
        tv_about = viewS.findViewById(R.id.tv_about);
        tv_legal = viewS.findViewById(R.id.tv_legal);
        tv_follow = viewS.findViewById(R.id.tv_follow);
        tv_profile = viewS.findViewById(R.id.tv_profile);
        tv_notification = viewS.findViewById(R.id.tv_notification);
        tv_premium = viewS.findViewById(R.id.tv_premium);
        tv_darkMode = viewS.findViewById(R.id.tv_darkMode);
        tv_figure = viewS.findViewById(R.id.tv_figure);
        tv_pattern = viewS.findViewById(R.id.tv_pattern);
        tv_Changepattern = viewS.findViewById(R.id.tv_Changepattern);
        tv_change_chat = viewS.findViewById(R.id.tv_change_chat);
        chat_us_title = viewS.findViewById(R.id.chat_us_title);
        tv_language = viewS.findViewById(R.id.tv_language);
        tv_features = viewS.findViewById(R.id.tv_features);
        tv_developer = viewS.findViewById(R.id.tv_developer);
        tv_rate = viewS.findViewById(R.id.tv_rate);
        tv_contact = viewS.findViewById(R.id.tv_contact);
        tv_bug = viewS.findViewById(R.id.tv_bug);
        tv_share = viewS.findViewById(R.id.tv_share);
        tv_privacy = viewS.findViewById(R.id.tv_privacy);
        tv_term = viewS.findViewById(R.id.tv_term);
        tv_download = viewS.findViewById(R.id.tv_download);
        tv_user = viewS.findViewById(R.id.tv_user);
        tv_rating = viewS.findViewById(R.id.tv_rating);
        tv_f1 = viewS.findViewById(R.id.tv_f1);
        tv_f2 = viewS.findViewById(R.id.tv_f2);
        tv_f3 = viewS.findViewById(R.id.tv_f3);
        iv_profile = viewS.findViewById(R.id.iv_profile);
        iv_premium = viewS.findViewById(R.id.iv_premium);
        iv_pattern = viewS.findViewById(R.id.iv_pattern);
        iv_ChangePattern = viewS.findViewById(R.id.iv_ChangePattern);
        iv_ChangeChatLocker = viewS.findViewById(R.id.iv_ChangeChatLocker);
        iv_language = viewS.findViewById(R.id.iv_language);
        iv_developer = viewS.findViewById(R.id.iv_developer);
        iv_rate = viewS.findViewById(R.id.iv_rate);
        iv_contact = viewS.findViewById(R.id.iv_contact);
        iv_bug = viewS.findViewById(R.id.iv_bug);
        iv_share = viewS.findViewById(R.id.iv_share);
        iv_privacy = viewS.findViewById(R.id.iv_privacy);
        iv_term = viewS.findViewById(R.id.iv_term);
        imgProfile = viewS.findViewById(R.id.imgProfile);
        tv_username = viewS.findViewById(R.id.tv_username);
        tv_email = viewS.findViewById(R.id.tv_email);
        tv_toolId = viewS.findViewById(R.id.tv_toolId);
        tv_sign = viewS.findViewById(R.id.tv_sign);
        tv_permission = viewS.findViewById(R.id.tv_permission);
        tv_Reward = viewS.findViewById(R.id.tv_Reward);
        tv_Faq = viewS.findViewById(R.id.tv_Faq);
        tv_Blog = viewS.findViewById(R.id.tv_Blog);
        tv_Update = viewS.findViewById(R.id.tv_Update);
        toolIdCopy = viewS.findViewById(R.id.toolIdCopy);
        rl_chat_us = viewS.findViewById(R.id.rl_chat_us);
        SliderView review_slider = viewS.findViewById(R.id.review_slider);
        review_slider.setSliderAdapter(new ReviewSliderAdapter(context, ImageArray, NameArray, ReviewArray));

        img1 = viewS.findViewById(R.id.img1);
        img2 = viewS.findViewById(R.id.img2);
        img3 = viewS.findViewById(R.id.img3);
        img4 = viewS.findViewById(R.id.img4);
        img5 = viewS.findViewById(R.id.img5);
        img6 = viewS.findViewById(R.id.img6);
        img7 = viewS.findViewById(R.id.img7);
        img8 = viewS.findViewById(R.id.img8);
        img9 = viewS.findViewById(R.id.img9);
        img10 = viewS.findViewById(R.id.img10);
        img11 = viewS.findViewById(R.id.img11);
        img12 = viewS.findViewById(R.id.img12);
        img13 = viewS.findViewById(R.id.img13);
        img14 = viewS.findViewById(R.id.img14);
        img15 = viewS.findViewById(R.id.img15);
        img17 = viewS.findViewById(R.id.img17);
        img18 = viewS.findViewById(R.id.img18);
        img19 = viewS.findViewById(R.id.img19);
        icon_chat_us_next = viewS.findViewById(R.id.icon_chat_us_next);

        if (Preference.getLogin_status().equals("Yes")) {
            rl_details.setVisibility(View.VISIBLE);
            rl_login.setVisibility(View.GONE);
        } else {
            rl_login.setVisibility(View.VISIBLE);
            rl_details.setVisibility(View.GONE);
        }


        rlReward.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                startActivity(new Intent(context, ActivityCoin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            } else {
                Constant.BottomSheetDialogLogIn((Activity) context);
            }
        });

        rlUpdate.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()))));

        rl_login.setOnClickListener(v -> startActivity(new Intent(context, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        if (Preference.getProfile().isEmpty()) {
            Glide.with(context).load(R.drawable.ic_profile).placeholder(R.drawable.ic_profile).into(imgProfile);
        } else {
            Glide.with(context).load(Preference.getProfile()).placeholder(R.drawable.ic_profile).into(imgProfile);
        }

        if (!Preference.getUserName().isEmpty()) {
            tv_username.setText(Preference.getUserName());
        }

        imgProfile.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                startActivity(new Intent(context, ActivityProfile.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            } else {
                startActivity(new Intent(context, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            }
        });
        if (!Preference.getEmail().isEmpty()) {
            tv_email.setText(Preference.getEmail());
        }
        tv_toolId.setText(Preference.getTool_id());
        toolIdCopy.setOnClickListener(v -> Utils.setClipboard(context, Preference.getTool_id()));

        txtAppVersn.setText(Html.fromHtml("App Version" + " : " + BuildConfig.VERSION_NAME));

        globalDark.setChecked(Preference.getBooleanTheme(false));
        if (globalDark.isChecked()) {
            switchOnOff = Preference.getBooleanTheme(true);
        } else {
            switchOnOff = Preference.getBooleanTheme(false);
        }
        globalDark.setChecked(switchOnOff);

        if (Preference.getLogin_status().equals("Yes")) {
            rl_notification.setVisibility(View.VISIBLE);
            rlReqFeature.setVisibility(View.VISIBLE);
        } else {
            rl_notification.setVisibility(View.GONE);
            rlReqFeature.setVisibility(View.GONE);
        }

        if (Preference.getLogin_status().equals("Yes")) {
            rlSignOut.setVisibility(View.VISIBLE);
        } else {
            rlSignOut.setVisibility(View.GONE);
        }

        rlSignOut.setOnClickListener(v -> {
            Preference.setLogin_status("");
            Preference.setProfile("");
            Preference.setEmail("");
            Preference.setUserName("");
            Preference.setGoogle_key("");
            Preference.setFacebook_key("");
            Preference.setActive_CkeyM("");
            Preference.setActive_CkeyY("");
            Preference.setActive_PkeyM("");
            Preference.setActive_PkeyY("");
            Preference.setActive_MkeyM("");
            Preference.setActive_MkeyY("");
            Preference.setActive_offer("");
            Preference.setActive_vip("");
            Preference.setTotal_coin("");
            Preference.setReferedCode("");
            Preference.setAds_one_day("");
            Preference.setAds_three_day("");
            Preference.setAds_seven_day("");
            Preference.setAds_thirty_day("");
            Preference.setBulk_one_day("");
            Preference.setBulk_seven_day("");
            Preference.setAuto_one_day("");
            Preference.setAuto_seven_day("");
            Preference.setImport_one_day("");
            Preference.setImport_seven_day("");
            Intent intent = new Intent(context, ActivityMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
        });

        globalDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Preference.setBooleanTheme(globalDark.isChecked());
                Preference.setBooleanTheme(true);

                switchON = "yes";

                startActivity(new Intent(context, ActivityMain.class), ActivityOptionsCompat.makeScaleUpAnimation(buttonView, (int) buttonView.getX(), (int) buttonView.getY(), buttonView.getWidth(), buttonView.getHeight()).toBundle());
                return;
            }
            Preference.setBooleanTheme(globalDark.isChecked());
            Preference.setBooleanTheme(false);
            switchON = "no";

            startActivity(new Intent(context, ActivityMain.class), ActivityOptionsCompat.makeScaleUpAnimation(buttonView, (int) buttonView.getX(), (int) buttonView.getY(), buttonView.getWidth(), buttonView.getHeight()).toBundle());
        });

        if (Preference.getBooleanTheme(false)) {
            rlSetting.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
            llGeneral.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llSecurity.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llAboutUs.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llLegal.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llFollow.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            rl_chat_us.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            rl_1.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            rl_2.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            rl_3.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            tv_general.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_security.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_about.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_legal.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_follow.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtAppVersn.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_profile.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_notification.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_premium.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_darkMode.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_figure.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_pattern.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_Changepattern.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_change_chat.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_language.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_features.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_developer.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_rate.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_contact.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_bug.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_share.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_privacy.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_term.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_username.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_email.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_toolId.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_sign.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_permission.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_Reward.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_Faq.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_Blog.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_Update.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_download.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_user.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_rating.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_f1.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_f2.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_f3.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            chat_us_title.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));

            img1.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img2.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img3.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img4.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img5.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img6.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img7.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img8.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img9.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img10.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img11.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img12.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img13.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img14.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img15.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img17.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img18.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img19.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            icon_chat_us_next.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg_black));
            img1.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img2.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img3.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img4.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img5.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img6.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img7.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img8.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img9.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img10.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img11.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img12.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img13.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img14.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img15.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img17.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img18.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            img19.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            icon_chat_us_next.setImageResource(R.drawable.ic_baseline_navigate_next_24_white);
            iv_dark.setImageResource(R.drawable.them1);
        } else {
            rl_1.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            rl_2.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            rl_3.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            rlSetting.setBackgroundColor(ContextCompat.getColor(context, R.color.card_color));
            llGeneral.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            llSecurity.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            llAboutUs.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            llLegal.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            rl_chat_us.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            llFollow.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            tv_general.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_security.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_about.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_legal.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_follow.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            txtAppVersn.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_profile.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_notification.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_premium.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_darkMode.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_figure.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_pattern.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_Changepattern.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_change_chat.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_language.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_features.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_developer.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_rate.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_contact.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_bug.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_share.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_privacy.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_term.setTextColor(ContextCompat.getColor(context, R.color.darkBlack));
            tv_username.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_email.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_toolId.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_sign.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_permission.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_Reward.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_Faq.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_Blog.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_Update.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_download.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_user.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_rating.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_f1.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_f2.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_f3.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            chat_us_title.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));

            img1.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img2.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img3.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img4.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img5.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img6.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img7.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img8.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img9.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img10.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img11.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img12.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img13.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img14.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img15.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img17.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img18.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img19.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            icon_chat_us_next.setBackground(ContextCompat.getDrawable(context, R.drawable.setting_next_bg));
            img1.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img2.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img3.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img4.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img5.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img6.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img7.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img8.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img9.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img10.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img11.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img12.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img13.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img14.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img15.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img17.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img18.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            img19.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            icon_chat_us_next.setImageResource(R.drawable.ic_baseline_navigate_next_24_black);
            iv_dark.setImageResource(R.drawable.them2);
        }

        rl_chat_us.setOnClickListener(v -> {
            try {
                if (Constant.whatsappInstalledOrNot((Activity) context)) {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("91" + Preference.getChatNo()) + "@s.whatsapp.net");
                    startActivity(sendIntent);
                } else {
                    Uri uri = Uri.parse("market://details?id=com.whatsapp");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                    startActivity(goToMarket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        rlFaq.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorTools));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(context, Uri.parse("https://whatscan.co/faqs/"));
        });
        txtWebsite.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorTools));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(context, Uri.parse("https://whatscan.co/"));
        });

        rlBlog.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorTools));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(context, Uri.parse("https://whatscan.co/blog/"));
        });

        rlProfile.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                startActivity(new Intent(context, ActivityProfile.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            } else {
                startActivity(new Intent(context, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            }
        });

        rlLanguage.setOnClickListener(v -> OpenLanguage());

        rlPermission.setOnClickListener(v -> startActivity(new Intent(context, ActivityPermission.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        rlRateUs.setOnClickListener(v -> showRateApp());

        rlContact.setOnClickListener(v -> {
            try {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("mailto:whatscantoolkit@gmail.com"));
                intent.putExtra("android.intent.extra.SUBJECT", "");
                intent.putExtra("android.intent.extra.TEXT", "");
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        });

        rlBugReport.setOnClickListener(v -> {
            try {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("mailto:whatscantoolkit@gmail.com"));
                intent.putExtra("android.intent.extra.SUBJECT", "");
                intent.putExtra("android.intent.extra.TEXT", "");
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        });

        rlShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Enjoy all the tools & tricks of Whatscan :- Toolkit For WA.\n\n" + "Download Link\n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\nWebsite :- https://whatscan.co/");
            sendIntent.setType("text/*");
         //   sendIntent.putExtra(Intent.EXTRA_STREAM, R.drawable.banner_share);

          //  Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(sendIntent);
        });

        rlPrivacy.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorTools));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(context, Uri.parse(Preference.getPrivacyPolicy()));
        });

        rlTerm.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorTools));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(context, Uri.parse(Preference.getTermCondition()));
        });

        rlPrePlan.setOnClickListener(v -> startActivity(new Intent(context, ActivityPremium.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        pattern_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                editor.putBoolean("IsPassword", false).apply();
            } else if (sharedPreferences.getString("Pattern", null) == null) {
                Intent intent = new Intent(context, ActivityPatternLock.class);
                intent.putExtra("Type", "Set");
                startActivity(intent);
            } else {
                editor.putBoolean("IsPassword", true).apply();
            }
        });

        tv_pattern.setOnClickListener(v -> pattern_switch.setChecked(!pattern_switch.isChecked()));

        rlChangePattern.setOnClickListener(v -> {
            if (sharedPreferences.getString("Pattern", null) == null) {
                Toast.makeText(context, "Please set Password First", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(context, ActivityPatternLock.class);
            intent.putExtra("Type", "Set");
            startActivity(intent);
        });

        rlReqFeature.setOnClickListener(v -> {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_req_features, null);
            bottomSheetDialog.setContentView(inflate);

            RelativeLayout rlReport = inflate.findViewById(R.id.rlReport);
            EditText et_report = inflate.findViewById(R.id.et_report);
            Button btnSubmit = inflate.findViewById(R.id.btnSubmit);

            if (Preference.getBooleanTheme(false)) {
                rlReport.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
                et_report.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white_search_d));
                et_report.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                et_report.setHintTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            } else {
                et_report.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card));
                rlReport.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
                et_report.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
                et_report.setHintTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            }

            btnSubmit.setOnClickListener(v12 -> {
                Constant.ShowProgress();
                ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
                apiInterface.getReportRequest(Preference.getReport_Request(), Preference.getU_id(), Build.MANUFACTURER, Build.VERSION.RELEASE + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName(), BuildConfig.VERSION_CODE, et_report.getText().toString(), "request").enqueue(new Callback<ReportRequest>() {
                    @Override
                    public void onResponse(@NotNull Call<ReportRequest> call, @NotNull Response<ReportRequest> response) {
                        if (response.isSuccessful()) {
                            Constant.DismissProgress();
                            assert response.body() != null;
                            if (response.body().getFlag().equals("true")) {
                                Toast.makeText(context, "Thanks for submit request", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ReportRequest> call, @NotNull Throwable t) {
                        Toast.makeText(context, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.show();
        });


        notification_switch.setChecked(Preference.getNotification_status().equals("true"));
        notification_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean flag;
            flag = isChecked;
            ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
            apiInterface.getNotification(Preference.getNotification_status(), Preference.getU_id(), String.valueOf(flag)).enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(@NotNull Call<NotificationModel> call, @NotNull Response<NotificationModel> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getFlag().equals("true")) {
                            if (flag) {
                                Preference.setCheck_Notification("true");
                                Toast.makeText(context, "We will notify now.", Toast.LENGTH_LONG).show();
                            } else {
                                Preference.setCheck_Notification("false");
                                Toast.makeText(context, "Thank you", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<NotificationModel> call, @NotNull Throwable t) {
                    Toast.makeText(context, "Something wrong! try again", Toast.LENGTH_SHORT).show();
                }
            });
        });

        rlChangeChatLocker.setOnClickListener(v -> {
            new PFPinCodeViewModel().delete();
            if (Preference.getVip_key().equals("true") || Preference.getActive_offer().equals("true")||Preference.getActive_PkeyM().equals("true") ||Preference.getActive_PkeyY().equals("true") ||Preference.getActive_MkeyM().equals("true") ||Preference.getActive_MkeyY().equals("true")) {
                startActivity(new Intent(context, ActivityChatLock.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            } else {
                Constant.BottomSheetDialogPremium((Activity) context, "Upgrade to Essential, Premium, Master Plan", "WhatsApp Chat Locker is available under Essential, Premium and Master plan.");
            }
        });

        la_fb.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(Preference.getDev_fb()));
            startActivity(i);
        });
        la_youtube.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(Preference.getDev_youtube()));
            startActivity(i);
        });

        la_ins.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(Preference.getDev_insta()));
            startActivity(i);
        });

        la_twitter.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(Preference.getDev_twitter()));
            startActivity(i);
        });

        rlDeveloper.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context, R.style.ios_sheet_style);
            dialog.setContentView(R.layout.dialog_developer);

            CardView cardDevloper = dialog.findViewById(R.id.cardDevloper);
            ImageView bt_close = dialog.findViewById(R.id.bt_close);
            TextView txtOne = dialog.findViewById(R.id.txtOne);
            TextView txtTwo = dialog.findViewById(R.id.txtTwo);
            TextView txtThree = dialog.findViewById(R.id.txtThree);
            TextView txtversion = dialog.findViewById(R.id.txtversion);
            TextView txtWebsite = dialog.findViewById(R.id.txtWebsite);

            if (Preference.getBooleanTheme(false)) {
                cardDevloper.setCardBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
                bt_close.setImageResource(R.drawable.ic_close2);
                txtOne.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                txtTwo.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                txtThree.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                txtversion.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

            txtWebsite.setOnClickListener(view -> {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorTools));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setPackage("com.android.chrome");
                customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                customTabsIntent.launchUrl(context, Uri.parse("https://whatscan.co/"));
            });

            txtversion.setText(Html.fromHtml("Version: " + BuildConfig.VERSION_NAME));

            bt_close.setOnClickListener(v1 -> dialog.dismiss());

            dialog.findViewById(R.id.imgWapp).setOnClickListener(v1 -> {
                try {
                    if (Constant.whatsappInstalledOrNot((Activity) context)) {
                        Intent sendIntent = new Intent("android.intent.action.MAIN");
                        sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                        sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("91" + Preference.getChatNo()) + "@s.whatsapp.net");
                        startActivity(sendIntent);
                    } else {
                        Uri uri = Uri.parse("market://details?id=com.whatsapp");
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                        startActivity(goToMarket);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            });

            dialog.findViewById(R.id.imgTwitter).setOnClickListener(v1 -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Preference.getDev_twitter()));
                startActivity(i);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.imgFb).setOnClickListener(v1 -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Preference.getDev_fb()));
                startActivity(i);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.imgInsta).setOnClickListener(v1 -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Preference.getDev_insta()));
                startActivity(i);
                dialog.dismiss();
            });
            dialog.findViewById(R.id.imgLinkdin).setOnClickListener(v1 -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Preference.getDev_insta()));
                startActivity(i);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.imgYoutube).setOnClickListener(v1 -> {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Preference.getDev_youtube())));
                } catch (ActivityNotFoundException unused) {
                    unused.printStackTrace();
                }
                dialog.dismiss();
            });

            dialog.show();
        });
    }

    private void RefreshFragment() {
        startActivity(new Intent(context, ActivityMain.class));
        getActivity().finish();
    }

    private void OpenLanguage() {
        bottomSheetDialog = new BottomSheetDialog(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_language, null);
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
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
            case "gu":
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
            case "te":
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
            case "es":
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
            case "pt":
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
            case "fr":
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
            case "bn":
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
            case "mr":
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
            case "tr":
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
            case "de":
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
            default:
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                break;
        }


        llEnglish.setOnClickListener(v -> {
            languageCode = 1;
            Lingver.getInstance().setLocale(context, "en");
            Preference.setCheckActiveLanguage("en");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            bottomSheetDialog.dismiss();
        });

        llHindi.setOnClickListener(v -> {
            languageCode = 2;
            Lingver.getInstance().setLocale(context, "hi");
            Preference.setCheckActiveLanguage("hi");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            bottomSheetDialog.dismiss();
        });

        llGujarati.setOnClickListener(v -> {
            languageCode = 3;
            Lingver.getInstance().setLocale(context, "gu");
            Preference.setCheckActiveLanguage("gu");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            bottomSheetDialog.dismiss();
        });

        llTelugu.setOnClickListener(v -> {
            languageCode = 4;
            Lingver.getInstance().setLocale(context, "te");
            Preference.setCheckActiveLanguage("te");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            bottomSheetDialog.dismiss();
        });

        llSpanish.setOnClickListener(v -> {
            languageCode = 5;
            Lingver.getInstance().setLocale(context, "es");
            Preference.setCheckActiveLanguage("es");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            bottomSheetDialog.dismiss();
        });

        llPortuguese.setOnClickListener(v -> {
            languageCode = 6;
            Lingver.getInstance().setLocale(context, "pt");
            Preference.setCheckActiveLanguage("pt");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            bottomSheetDialog.dismiss();
        });

        llFrench.setOnClickListener(v -> {
            languageCode = 7;
            Lingver.getInstance().setLocale(context, "fr");
            Preference.setCheckActiveLanguage("fr");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            bottomSheetDialog.dismiss();
        });

        llBengali.setOnClickListener(v -> {
            languageCode = 8;
            Lingver.getInstance().setLocale(context, "bn");
            Preference.setCheckActiveLanguage("bn");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            bottomSheetDialog.dismiss();
        });

        llMarathi.setOnClickListener(v -> {
            languageCode = 9;
            Lingver.getInstance().setLocale(context, "mr");
            Preference.setCheckActiveLanguage("mr");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            bottomSheetDialog.dismiss();
        });

        llTurkish.setOnClickListener(v -> {
            languageCode = 10;
            Lingver.getInstance().setLocale(context, "tr");
            Preference.setCheckActiveLanguage("tr");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
                llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            bottomSheetDialog.dismiss();
        });

        llGerman.setOnClickListener(v -> {
            languageCode = 11;
            Lingver.getInstance().setLocale(context, "de");
            Preference.setCheckActiveLanguage("de");
            if (Preference.getBooleanTheme(false)) {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            } else {
                llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llHindi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llGujarati.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTelugu.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llSpanish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llPortuguese.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llFrench.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llBengali.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llMarathi.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
                llTurkish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language));
            }
            RefreshFragment();
            llGerman.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_language_select));
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }

    public void showRateApp() {
        Constant.BottomSheetDialogRateApp((Activity) context);
    }
}