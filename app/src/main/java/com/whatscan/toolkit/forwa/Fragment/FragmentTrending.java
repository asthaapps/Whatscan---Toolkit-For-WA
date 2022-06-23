package com.whatscan.toolkit.forwa.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.AutoResponse.ActivityAutoResponse;
import com.whatscan.toolkit.forwa.BulkSender.BulkActivityStart;
import com.whatscan.toolkit.forwa.ContactExport.ActivityContactExport;
import com.whatscan.toolkit.forwa.Other.ActivityProfile;
import com.whatscan.toolkit.forwa.Other.ActivitySearch;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.User.ActivityLogin;
import com.whatscan.toolkit.forwa.Util.AccessibilityPermission;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WBubble.ActivityWABubble;
import com.whatscan.toolkit.forwa.WChatLocker.ActivityChatLock;

public class FragmentTrending extends Fragment {
    public Context context;
    public ImageView imgProfile;
    public LinearLayout rlBulkSender, rlChatLocker, rlBubble, ll_export, llSearch, iv_search;
    public RelativeLayout rlAutoResponse, rlAuto, llMain, cvProfile;
    public TextView txtSearch, txtTitle, txtBulkHeading, txtBulkHeadingIntro, txtSendMsg, txtBubbleHeading, txtBubbleHeadingIntro;
    public TextView tv_autoResponse, tv_text10, tv_text11, tv_text12, txtChatHeading, txtChatHeadingIntro, tv_export, tv_text51, tv_text52;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewF = inflater.inflate(R.layout.fragment_trending, container, false);

        context = getContext();
        llMain = viewF.findViewById(R.id.llMain);
        rlBulkSender = viewF.findViewById(R.id.rlBulkSender);
        rlAutoResponse = viewF.findViewById(R.id.rlAutoResponse);
        rlAuto = viewF.findViewById(R.id.rlAuto);
        ll_export = viewF.findViewById(R.id.ll_export);
        rlChatLocker = viewF.findViewById(R.id.rlChatLocker);
        rlBubble = viewF.findViewById(R.id.rlBubble);
        imgProfile = viewF.findViewById(R.id.imgProfile);
        txtSearch = viewF.findViewById(R.id.txtSearch);
        txtTitle = viewF.findViewById(R.id.txtTitle);
        txtBulkHeading = viewF.findViewById(R.id.txtBulkHeading);
        txtBulkHeadingIntro = viewF.findViewById(R.id.txtBulkHeadingIntro);
        txtSendMsg = viewF.findViewById(R.id.txtSendMsg);
        tv_autoResponse = viewF.findViewById(R.id.tv_autoResponse);
        tv_text10 = viewF.findViewById(R.id.tv_text10);
        tv_text11 = viewF.findViewById(R.id.tv_text11);
        tv_text12 = viewF.findViewById(R.id.tv_text12);
        txtChatHeading = viewF.findViewById(R.id.txtChatHeading);
        txtChatHeadingIntro = viewF.findViewById(R.id.txtChatHeadingIntro);
        tv_export = viewF.findViewById(R.id.tv_export);
        tv_text51 = viewF.findViewById(R.id.tv_text51);
        tv_text52 = viewF.findViewById(R.id.tv_text52);
        llSearch = viewF.findViewById(R.id.llSearch);
        iv_search = viewF.findViewById(R.id.iv_search);
        txtBubbleHeading = viewF.findViewById(R.id.txtBubbleHeading);
        txtBubbleHeadingIntro = viewF.findViewById(R.id.txtBubbleHeadingIntro);
        cvProfile = viewF.findViewById(R.id.cvProfile);

        TextView tv_username = viewF.findViewById(R.id.tv_username);
        if (!Preference.getUserName().isEmpty()) {
            tv_username.setText(Preference.getUserName());
        } else {
            tv_username.setVisibility(View.GONE);
        }

//        BubbleShowCaseBuilder firstShowCaseBuilder = new BubbleShowCaseBuilder((Activity) context);
//        firstShowCaseBuilder.title("Profile & Login").backgroundColor(Color.parseColor("#6DA188")).
//                textColor(Color.WHITE).showOnce("imgProfile").targetView(imgProfile);
//
//        BubbleShowCaseBuilder secondShowCaseBuilder = new BubbleShowCaseBuilder((Activity) context);
//        secondShowCaseBuilder.title("Search Tools").backgroundColor(Color.parseColor("#6DA188")).
//                textColor(Color.WHITE).showOnce("llSearch").targetView(llSearch);
//
//        BubbleShowCaseBuilder thirdShowCaseBuilder = new BubbleShowCaseBuilder((Activity) context);
//        thirdShowCaseBuilder.title(getString(R.string.bulk_automatic_sender)).description(getString(R.string.bulk_text)).backgroundColor(Color.parseColor("#6DA188")).
//                textColor(Color.WHITE).showOnce("rlBulkSender").targetView(rlBulkSender);
//
//        BubbleShowCaseSequence bubbleShowCaseSequenceOne = new BubbleShowCaseSequence();
//        bubbleShowCaseSequenceOne.addShowCase(firstShowCaseBuilder).addShowCase(secondShowCaseBuilder).addShowCase(thirdShowCaseBuilder).show();

        FrameLayout fl_native = viewF.findViewById(R.id.fl_native);
        Advertisement.showNativeAds(getActivity(), fl_native);

        if (Preference.getProfile().isEmpty()) {
            Glide.with(context).load(R.drawable.ic_profile).placeholder(R.drawable.ic_profile).into(imgProfile);
        } else {
            Glide.with(context).load(Preference.getProfile()).placeholder(R.drawable.ic_profile).into(imgProfile);
        }

        if (Preference.getBooleanTheme(false)) {
            llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
            iv_search.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            cvProfile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llSearch.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            rlBulkSender.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            rlChatLocker.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            rlAuto.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            ll_export.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            rlBubble.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            txtSearch.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTitle.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtBulkHeading.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtBulkHeadingIntro.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_autoResponse.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_text10.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_text11.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_text12.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtChatHeading.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtChatHeadingIntro.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_export.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_text51.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_text52.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtBubbleHeading.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtBubbleHeadingIntro.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_username.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.card_color));
            cvProfile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            iv_search.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            llSearch.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            rlBulkSender.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            rlChatLocker.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            rlBubble.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            rlAuto.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            ll_export.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            txtSearch.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtTitle.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtBulkHeading.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtBulkHeadingIntro.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_autoResponse.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_text10.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_text11.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_text12.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtChatHeading.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtChatHeadingIntro.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_export.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_text51.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_text52.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtBubbleHeading.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtBubbleHeadingIntro.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_username.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }

        llSearch.setOnClickListener(v -> startActivity(new Intent(context, ActivitySearch.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));
        iv_search.setOnClickListener(v -> startActivity(new Intent(context, ActivitySearch.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        imgProfile.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                startActivity(new Intent(context, ActivityProfile.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            } else {
                startActivity(new Intent(context, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            }
        });

        rlBulkSender.setOnClickListener(view -> startActivity(new Intent(context, BulkActivityStart.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle()));

        rlAutoResponse.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                if (Preference.getVip_key().equals("true") || Preference.getActive_offer().equals("true") || Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") || Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true") || Preference.getAuto_one_day().equals("true") || Preference.getAuto_seven_day().equals("true")) {
                    startActivity(new Intent(context, ActivityAutoResponse.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
                } else {
                    Constant.BottomSheetDialogPremium((Activity) context, "Upgrade to Classic, Premium, Master Plan", "WhatsApp Auto response is available under Classic, Premium and Master plan, upgrade to enable it.");
                }
            } else {
                startActivity(new Intent(context, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            }
        });

        rlChatLocker.setOnClickListener(v -> {
            if (Preference.getVip_key().equals("true") || Preference.getActive_offer().equals("true") || Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") || Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true")) {
                startActivity(new Intent(context, ActivityChatLock.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            } else {
                Constant.BottomSheetDialogPremium((Activity) context, "Upgrade to Classic, Premium, Master Plan", "WhatsApp Auto response is available under Essential, Premium and Master plan, upgrade to enable it.");
            }
        });

        rlBubble.setOnClickListener(view -> {
            if (Preference.getPermission_overlay().equals("true") && Preference.getPermission_notification().equals("true")) {
                startActivity(new Intent(context, ActivityWABubble.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
            } else if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) {
                Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + context.getPackageName()));
                startActivity(intent);
            } else if (!AccessibilityPermission.CheckNoti((Activity) context)) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            } else {
                startActivity(new Intent(context, ActivityWABubble.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
            }
        });

        ll_export.setOnClickListener(v -> {
            Advertisement.getInstance((Activity) context).showFullAds(() -> ExportIntent(v));
        });

        return viewF;
    }

    private void ExportIntent(View v) {
        startActivity(new Intent(context, ActivityContactExport.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
    }
}
