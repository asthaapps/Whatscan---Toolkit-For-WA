package com.whatscan.toolkit.forwa.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.whatscan.toolkit.forwa.Adapter.TextTools;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.AutoScheduler.ActivityAllEventScheduler;
import com.whatscan.toolkit.forwa.MsgTools.Caption.ActivityCaptionStatus;
import com.whatscan.toolkit.forwa.MsgTools.Emoticon.ActivityEmoticon;
import com.whatscan.toolkit.forwa.MsgTools.MagicText.ActivityMagicText;
import com.whatscan.toolkit.forwa.MsgTools.TextRepeater.ActivityTextRepeater;
import com.whatscan.toolkit.forwa.MsgTools.TextToEmoji.ActivityTextToEmoji;
import com.whatscan.toolkit.forwa.Other.ActivityProfile;
import com.whatscan.toolkit.forwa.Other.ActivitySearch;
import com.whatscan.toolkit.forwa.Other.CommonWebView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.WhatsappAccessibilitySchedulerService;
import com.whatscan.toolkit.forwa.User.ActivityLogin;
import com.whatscan.toolkit.forwa.Util.AccessibilityPermission;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.concurrent.atomic.AtomicInteger;

public class FragmentExplorer extends Fragment implements TextTools.ItemClickListenerText {
    public Context context;
    public ImageView imgProfile;
    public RecyclerView rv_text_tool;
    public TextView txtCaptionStatus, txtTitle, txtSearch, txtBulkHeading, txtBulkHeadingIntro, txtCaption;
    public LinearLayout llSearch, llCaption, llAutoScheduler, iv_search;
    public RelativeLayout llMain, cvProfile;
    public ChipGroup chipGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewE = inflater.inflate(R.layout.fragment_explorer, container, false);

        context = getContext();
        llMain = viewE.findViewById(R.id.llMain);
        rv_text_tool = viewE.findViewById(R.id.rv_text_tool);
        txtCaptionStatus = viewE.findViewById(R.id.txtCaptionStatus);
        txtTitle = viewE.findViewById(R.id.txtTitle);
        txtSearch = viewE.findViewById(R.id.txtSearch);
        txtBulkHeading = viewE.findViewById(R.id.txtBulkHeading);
        txtBulkHeadingIntro = viewE.findViewById(R.id.txtBulkHeadingIntro);
        txtCaption = viewE.findViewById(R.id.txtCaption);
        chipGroup = viewE.findViewById(R.id.chip_group);
        imgProfile = viewE.findViewById(R.id.imgProfile);
        llSearch = viewE.findViewById(R.id.llSearch);
        iv_search = viewE.findViewById(R.id.iv_search);
        llCaption = viewE.findViewById(R.id.llCaption);
        llAutoScheduler = viewE.findViewById(R.id.llAutoScheduler);
        cvProfile = viewE.findViewById(R.id.cvProfile);

        TextView tv_username = viewE.findViewById(R.id.tv_username);
        if (!Preference.getUserName().isEmpty()) {
            tv_username.setText(Preference.getUserName());
        } else {
            tv_username.setVisibility(View.GONE);
        }

        FrameLayout fl_native = viewE.findViewById(R.id.fl_native);

        Advertisement.showNativeAds(getActivity(), fl_native);

        if (Preference.getProfile().isEmpty()) {
            Glide.with(context).load(R.drawable.ic_profile).placeholder(R.drawable.ic_profile).into(imgProfile);
        } else {
            Glide.with(context).load(Preference.getProfile()).placeholder(R.drawable.ic_profile).into(imgProfile);
        }

        if (Preference.getBooleanTheme(false)) {
            llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
            llSearch.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            iv_search.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            cvProfile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llAutoScheduler.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            txtTitle.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_username.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtSearch.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtBulkHeading.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtBulkHeadingIntro.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtCaption.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.card_color));
            cvProfile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            llSearch.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            iv_search.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            llAutoScheduler.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            txtTitle.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtSearch.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_username.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtBulkHeading.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtBulkHeadingIntro.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtCaption.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
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

        llAutoScheduler.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                if (Preference.getVip_key().equals("true") || Preference.getActive_offer().equals("true") || Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") || Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true") || Preference.getAuto_one_day().equals("true") || Preference.getAuto_seven_day().equals("true")) {
                    if (!AccessibilityPermission.isAccessibilityOn((Activity) context, WhatsappAccessibilitySchedulerService.class)) {
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                        View inflate = LayoutInflater.from(context).inflate(R.layout.permission_discloser2, null);
                        bottomSheetDialog.setContentView(inflate);
                        AtomicInteger check = new AtomicInteger();
                        Button bt_agree = inflate.findViewById(R.id.bt_agree);
                        TextView bt_cancel = inflate.findViewById(R.id.bt_cancel);
                        bt_cancel.setOnClickListener(view13 -> bottomSheetDialog.dismiss());
                        CheckBox checkBox_agree = inflate.findViewById(R.id.checkBox_agree);
                        SpannableString SpanString = new SpannableString("By signing in, you agree to the Terms of Use and Privacy Policy");

                        ClickableSpan teremsAndCondition = new ClickableSpan() {
                            @Override
                            public void onClick(View textView) {
                                Intent mIntent = new Intent(context, CommonWebView.class);
                                mIntent.putExtra("tc", true);
                                startActivity(mIntent);
                            }
                        };

                        ClickableSpan privacy = new ClickableSpan() {
                            @Override
                            public void onClick(View textView) {
                                Intent mIntent = new Intent(context, CommonWebView.class);
                                mIntent.putExtra("pp", true);
                                startActivity(mIntent);
                            }
                        };


                        SpanString.setSpan(teremsAndCondition, 32, 45, 0);
                        SpanString.setSpan(privacy, 49, 63, 0);
                        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 32, 45, 0);
                        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 49, 63, 0);

                        checkBox_agree.setMovementMethod(LinkMovementMethod.getInstance());
                        checkBox_agree.setText(SpanString, TextView.BufferType.SPANNABLE);
                        checkBox_agree.setSelected(true);
                        checkBox_agree.setOnClickListener(view12 -> {
                            boolean checked = ((CheckBox) view12).isChecked();
                            if (checked) {
                                check.set(1);
                            } else {
                                check.set(0);
                            }
                        });

                        bt_agree.setOnClickListener(view1 -> {
                            if (check.get() == 0) {
                                Toast.makeText(context, "please agree conditions.", Toast.LENGTH_SHORT).show();
                            } else if (check.get() == 1) {
                                Intent intent1 = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                                startActivity(intent1);
                                bottomSheetDialog.dismiss();
                            }
                        });
                        bottomSheetDialog.setCancelable(false);
                        bottomSheetDialog.show();
                    } else {
                        startActivity(new Intent(context, ActivityAllEventScheduler.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
                    }

                } else {
                    Constant.BottomSheetDialogPremium((Activity) context, "Upgrade to Essential, Premium, Master Plan", "WhatsApp Auto response is available under Essential, Premium and Master plan, upgrade to enable it.");
                }
            } else {
                startActivity(new Intent(context, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            }
        });

        //  int[] animation_gif_text = {R.drawable.ic_magic_text, R.drawable.ic_text_to_emoji, R.drawable.ic_text_repeter, R.drawable.ic_emoticon};
        String[] tools_name_text = getResources().getStringArray(R.array.tools_name_text);

        TextTools textTools = new TextTools(context, tools_name_text);
        textTools.setClickListener(FragmentExplorer.this);
        rv_text_tool.setLayoutManager(new GridLayoutManager(context, 2));
        rv_text_tool.setAdapter(textTools);

        Chip chip1 = new Chip(context);
        chip1.setText("Best");
        chip1.setTextColor(getResources().getColor(R.color.colorBlack));

        Chip chip2 = new Chip(context);
        chip2.setText("Clever");
        chip2.setTextColor(getResources().getColor(R.color.colorBlack));

        Chip chip3 = new Chip(context);
        chip3.setText("Cool");
        chip3.setTextColor(getResources().getColor(R.color.colorBlack));

        Chip chip4 = new Chip(context);
        chip4.setText("Cute");
        chip4.setTextColor(getResources().getColor(R.color.colorBlack));

        Chip chip5 = new Chip(context);
        chip5.setText("Fitness");
        chip5.setTextColor(getResources().getColor(R.color.colorBlack));

        Chip chip6 = new Chip(context);
        chip6.setText("Funny");
        chip6.setTextColor(getResources().getColor(R.color.colorBlack));

        Chip chip7 = new Chip(context);
        chip7.setText("Life");
        chip7.setTextColor(getResources().getColor(R.color.colorBlack));

        Chip chip8 = new Chip(context);
        chip8.setText("Love");
        chip8.setTextColor(getResources().getColor(R.color.colorBlack));

        Chip chip9 = new Chip(context);
        chip9.setText("Motivation");
        chip9.setTextColor(getResources().getColor(R.color.colorBlack));

        Chip chip10 = new Chip(context);
        chip10.setChipCornerRadius(13);
        chip10.setText("Sad");
        chip10.setTextColor(getResources().getColor(R.color.colorBlack));

        chip1.setChipCornerRadius(13);
        chip2.setChipCornerRadius(13);
        chip3.setChipCornerRadius(13);
        chip4.setChipCornerRadius(13);
        chip5.setChipCornerRadius(13);
        chip6.setChipCornerRadius(13);
        chip7.setChipCornerRadius(13);
        chip8.setChipCornerRadius(13);
        chip9.setChipCornerRadius(13);
        chip10.setChipCornerRadius(13);

        chip1.setOnClickListener(this::CaptionIntent);

        chip2.setOnClickListener(this::CaptionIntent);

        chip3.setOnClickListener(this::CaptionIntent);

        chip4.setOnClickListener(this::CaptionIntent);

        chip5.setOnClickListener(this::CaptionIntent);

        chip6.setOnClickListener(this::CaptionIntent);

        chip7.setOnClickListener(this::CaptionIntent);

        chip8.setOnClickListener(this::CaptionIntent);

        chip9.setOnClickListener(this::CaptionIntent);

        chip10.setOnClickListener(this::CaptionIntent);

        chipGroup.addView(chip1);
        chipGroup.addView(chip2);
        chipGroup.addView(chip3);
        chipGroup.addView(chip4);
        chipGroup.addView(chip5);
        chipGroup.addView(chip6);
        chipGroup.addView(chip7);
        chipGroup.addView(chip8);
        chipGroup.addView(chip9);
        chipGroup.addView(chip10);


        txtCaptionStatus.setOnClickListener(this::CaptionIntent);

        return viewE;
    }

    @Override
    public void onItemClickText(View view, int position) {
        if (position == 0) {
            Advertisement.getInstance((Activity) context).showFullAds(() -> MagicTextIntent(view));
        } else if (position == 1) {
            startActivity(new Intent(context, ActivityTextToEmoji.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
        } else if (position == 2) {
            startActivity(new Intent(context, ActivityTextRepeater.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
        } else if (position == 3) {
            Advertisement.getInstance((Activity) context).showFullAds(() -> EmoticonIntent(view));
        }
    }

    private void EmoticonIntent(View v) {
        startActivity(new Intent(context, ActivityEmoticon.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
    }

    private void MagicTextIntent(View v) {
        startActivity(new Intent(context, ActivityMagicText.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
    }

    private void CaptionIntent(View view) {
        startActivity(new Intent(context, ActivityCaptionStatus.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
    }
}
