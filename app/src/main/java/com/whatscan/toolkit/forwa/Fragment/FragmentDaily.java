package com.whatscan.toolkit.forwa.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;
import com.whatscan.toolkit.forwa.Adapter.StoryImageHome;
import com.whatscan.toolkit.forwa.Ads.ActivityPremium;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.DeletedMedia.ActivityDeleteMedia;
import com.whatscan.toolkit.forwa.DirectChat.ActivityDirectChat;
import com.whatscan.toolkit.forwa.GetSet.StatusModel;
import com.whatscan.toolkit.forwa.Other.ActivityProfile;
import com.whatscan.toolkit.forwa.Other.ActivitySearch;
import com.whatscan.toolkit.forwa.Other.CommonWebView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.BasicAccessibilityService;
import com.whatscan.toolkit.forwa.Service.WhatsappAccessibility;
import com.whatscan.toolkit.forwa.Shake.ActivityShake;
import com.whatscan.toolkit.forwa.Story.ActivityStorySaver;
import com.whatscan.toolkit.forwa.User.ActivityLogin;
import com.whatscan.toolkit.forwa.Util.AccessibilityPermission;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WalkChat.ActivityWalkChat;
import com.whatscan.toolkit.forwa.Web.ActivityPatternLock;
import com.whatscan.toolkit.forwa.Web.ActivityWeb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class FragmentDaily extends Fragment implements OnCountryPickerListener {

    private final ArrayList<StatusModel> arrayListStatus;
    public Context context;
    public TextView tv_seeAll_status, tv_detail, tv_country_code, txtTitle, txtSearch, txtStory, txtTool, direct_chat, tv_recover_title, tv_text10, tv_text11;
    public ImageView tv_country_name, imgProfile;
    public RecyclerView rv_status;
    public LottieAnimationView la_loading;
    public StoryImageHome adapterStoryImage;
    public RelativeLayout card_web, card_shake, card_walkChat, cvProfile;
    public EditText et_text, et_mobile;
    public TextInputLayout text_input1, text_input2;
    public RelativeLayout rl_country, rlStory, llMain;
    public LinearLayout la_send_chat, rlDeleteMessage, llSearch, llWSC, llDirect, iv_search;
    public SharedPreferences sharedPreferences;
    public RadioGroup sendByRadioGroup;
    public String strCountryName, strCountryCode = "+91";
    public int strFlag;
    public ActivityResultLauncher<Intent> resultLauncher;

    public FragmentDaily(ArrayList<StatusModel> arrayListStatus) {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Preference.setPermission_accessbility("true");
            }
        });
        this.arrayListStatus = arrayListStatus;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewD = inflater.inflate(R.layout.fragment_daily, container, false);

        context = getContext();
        llMain = viewD.findViewById(R.id.llMain);
        tv_seeAll_status = viewD.findViewById(R.id.tv_seeAll_status);
        rv_status = viewD.findViewById(R.id.rv_status);
        TextView tv_username = viewD.findViewById(R.id.tv_username);
        if (!Preference.getUserName().isEmpty()) {
            tv_username.setText(Preference.getUserName());
        } else {
            tv_username.setVisibility(View.GONE);
        }
        card_web = viewD.findViewById(R.id.card_web);
        card_shake = viewD.findViewById(R.id.card_shake);
        card_walkChat = viewD.findViewById(R.id.card_walkChat);
        tv_detail = viewD.findViewById(R.id.tv_detail);
        rl_country = viewD.findViewById(R.id.rl_country);
        rlStory = viewD.findViewById(R.id.rlStory);
        tv_country_name = viewD.findViewById(R.id.tv_country_name);
        tv_country_code = viewD.findViewById(R.id.tv_country_code);
        la_send_chat = viewD.findViewById(R.id.ll_send_chat);
        iv_search = viewD.findViewById(R.id.iv_search);
        rlDeleteMessage = viewD.findViewById(R.id.rlDeleteMessage);
        et_text = viewD.findViewById(R.id.et_text);
        et_mobile = viewD.findViewById(R.id.et_mobile);
        text_input2 = viewD.findViewById(R.id.text_input2);
        sendByRadioGroup = viewD.findViewById(R.id.sendByRadioGroup);
        imgProfile = viewD.findViewById(R.id.imgProfile);
        llSearch = viewD.findViewById(R.id.llSearch);
        llWSC = viewD.findViewById(R.id.llWSC);
        llDirect = viewD.findViewById(R.id.llDirect);
        txtTitle = viewD.findViewById(R.id.txtTitle);
        txtSearch = viewD.findViewById(R.id.txtSearch);
        txtStory = viewD.findViewById(R.id.txtStory);
        txtTool = viewD.findViewById(R.id.txtTool);
        direct_chat = viewD.findViewById(R.id.direct_chat);
        tv_recover_title = viewD.findViewById(R.id.tv_recover_title);
        tv_text10 = viewD.findViewById(R.id.tv_text10);
        tv_text11 = viewD.findViewById(R.id.tv_text11);
        la_loading = viewD.findViewById(R.id.la_loading);
        cvProfile = viewD.findViewById(R.id.cvProfile);

        FrameLayout fl_native = viewD.findViewById(R.id.fl_native);
        Advertisement.showNativeAds(getActivity(), fl_native);

        sharedPreferences = context.getSharedPreferences("MyPreferences", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();

        MainStorySaver();
        MainScan();
        MainDirectChat(viewD);

        if (Preference.getProfile().isEmpty()) {
            Glide.with(context).load(R.drawable.ic_profile).placeholder(R.drawable.ic_profile).into(imgProfile);
        } else {
            Glide.with(context).load(Preference.getProfile()).placeholder(R.drawable.ic_profile).into(imgProfile);
        }

        if (Preference.getBooleanTheme(false)) {
            sendByRadioGroup.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_border_login_w));
            llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.darkBlack));
            llSearch.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            iv_search.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            rlStory.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            cvProfile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            llDirect.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            rlDeleteMessage.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black));
            txtTitle.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_username.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtSearch.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtStory.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            txtTool.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            direct_chat.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            et_mobile.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            et_mobile.setHintTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            et_mobile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black_dark));
            et_text.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            et_text.setHintTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            et_text.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_black_dark));
            tv_country_code.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_text10.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_text11.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_recover_title.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            tv_username.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.card_color));
            llSearch.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            iv_search.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            cvProfile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            rlStory.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            rlDeleteMessage.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            llDirect.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            llDirect.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
            txtTitle.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtSearch.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtStory.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            txtTool.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            direct_chat.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            et_mobile.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            et_text.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            et_text.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card));
            et_mobile.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card));
            tv_country_code.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            tv_recover_title.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_text10.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            tv_text11.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            sendByRadioGroup.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_white));
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

        tv_detail.setOnClickListener(v -> startActivity(new Intent(context, ActivityDirectChat.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        rlDeleteMessage.setOnClickListener(v -> {
            if (!AccessibilityPermission.CheckNoti((Activity) context)) {
                AccessibilityPermission.NotiPermission((Activity) context);
                return;
            }

            Advertisement.getInstance((Activity) context).showFullAds(() -> DeleteMediaIntent(v));
        });
        return viewD;
    }


    private void MainScan() {
        card_web.setOnClickListener(view -> {
            if (sharedPreferences.getString("Pattern", null) != null) {
                if (sharedPreferences.getBoolean("IsPassword", false)) {
                    Intent intent1 = new Intent(context, ActivityPatternLock.class);
                    intent1.putExtra("Type", "Open");
                    startActivity(intent1, ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
                    return;
                }
                startActivity(new Intent(context, ActivityWeb.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
            } else if (sharedPreferences.getBoolean("IsSkip", false)) {
                startActivity(new Intent(context, ActivityWeb.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
            } else {
                Intent intent = new Intent(context, ActivityPatternLock.class);
                intent.putExtra("Type", "Set");
                startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
            }
        });

        card_shake.setOnClickListener(view -> {
            if (AccessibilityPermission.isAccessibilityOn((Activity) context, WhatsappAccessibility.class)) {
                Preference.setPermission_accessbility("true");
                startActivity(new Intent(context, ActivityShake.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
            } else if (!AccessibilityPermission.isAccessibilityOn((Activity) context, WhatsappAccessibility.class)) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View inflate = LayoutInflater.from(context).inflate(R.layout.permission_discloser1, null);
                bottomSheetDialog.setContentView(inflate);
                AtomicInteger check = new AtomicInteger();
                Button bt_agree = inflate.findViewById(R.id.bt_agree);
                TextView bt_cancel = inflate.findViewById(R.id.bt_cancel);
                bt_cancel.setOnClickListener(view13 -> bottomSheetDialog.dismiss());
                CheckBox checkBox_agree = inflate.findViewById(R.id.checkBox_agree);
                SpannableString SpanString = new SpannableString(
                        "By signing in, you agree to the Terms of Use and Privacy Policy");

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
                        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                        resultLauncher.launch(intent);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setCancelable(false);
                bottomSheetDialog.show();
            }
        });

        card_walkChat.setOnClickListener(view -> {
            if (AccessibilityPermission.isAccessibilityOn((Activity) context, BasicAccessibilityService.class)) {
                Preference.setPermission_accessbility("true");
                startActivity(new Intent(context, ActivityWalkChat.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
            } else if (!AccessibilityPermission.isAccessibilityOn((Activity) context, BasicAccessibilityService.class)) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                View inflate = LayoutInflater.from(context).inflate(R.layout.permission_discloser3, null);
                bottomSheetDialog.setContentView(inflate);
                AtomicInteger check = new AtomicInteger();
                Button bt_agree = inflate.findViewById(R.id.bt_agree);
                TextView bt_cancel = inflate.findViewById(R.id.bt_cancel);
                bt_cancel.setOnClickListener(view13 -> bottomSheetDialog.dismiss());
                CheckBox checkBox_agree = inflate.findViewById(R.id.checkBox_agree);


                SpannableString SpanString = new SpannableString(
                        "By signing in, you agree to the Terms of Use and Privacy Policy");

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
                        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                        resultLauncher.launch(intent);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setCancelable(false);
                bottomSheetDialog.show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void MainStorySaver() {
        if (arrayListStatus.isEmpty()) {
            la_loading.setVisibility(View.VISIBLE);
            rv_status.setVisibility(View.GONE);
        } else {
            rv_status.setVisibility(View.VISIBLE);
            la_loading.setVisibility(View.GONE);
            adapterStoryImage = new StoryImageHome(arrayListStatus, context);
            GridLayoutManager manager = new GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false);
            rv_status.setLayoutManager(manager);
            rv_status.setItemAnimator(new DefaultItemAnimator());
            rv_status.setAdapter(adapterStoryImage);
            adapterStoryImage.notifyDataSetChanged();
        }
        tv_seeAll_status.setOnClickListener(view -> {
            Intent intent = new Intent(context, ActivityStorySaver.class);
            intent.putParcelableArrayListExtra("arrayListStatus", arrayListStatus);
            context.startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
        });
    }

    private void MainDirectChat(View viewD) {
        int i3 = R.id.whatsappRadioButton;
        RadioButton radioButton = viewD.findViewById(i3);
        int i4 = R.id.whatsappBusinessRadioButton;
        RadioButton j = viewD.findViewById(i4);

        rl_country.setOnClickListener(v -> {
            CountryPicker.Builder builder = new CountryPicker.Builder().with(context).listener(FragmentDaily.this);
            builder.style(R.style.DialogStyle);
            builder.theme(CountryPicker.THEME_NEW);
            builder.canSearch(true);
            builder.sortBy(CountryPicker.SORT_BY_NONE);
            CountryPicker countryPicker = builder.build();
            countryPicker.showBottomSheet((AppCompatActivity) context);
        });

        sendByRadioGroup.setOnCheckedChangeListener((group, checkedId) -> Preference.saveBooleanData(context, Event.IS_WA_BUSINESS_SELECTED_FOR_MESSAGE.toString(), checkedId == R.id.whatsappBusinessRadioButton));

        if (Preference.getSavedBoolean(context, Event.IS_WA_BUSINESS_SELECTED_FOR_MESSAGE.toString(), false)) {
            i3 = i4;
        }
        sendByRadioGroup.check(i3);

        la_send_chat.setOnClickListener(v -> {
            String strMobile = et_mobile.getText().toString();
            String strText = et_text.getText().toString();

            if (strCountryCode == null) {
                Toast.makeText(context, "Select Country", Toast.LENGTH_SHORT).show();
            } else if (strMobile.isEmpty()) {
                Toast.makeText(context, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            } else if (strText.isEmpty()) {
                Toast.makeText(context, "Enter Message", Toast.LENGTH_SHORT).show();
            } else {
                SendWp(strMobile, strText, j.isChecked());
            }
        });
    }

    private void DeleteMediaIntent(View v) {
        startActivity(new Intent(context, ActivityDeleteMedia.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
    }


    private void SendWp(String strMobile, String strText, boolean z) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.VIEW");
        try {
            URLEncoder.encode(strText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        intent.setPackage(z ? "com.whatsapp.w4b" : "com.whatsapp");
        intent.setData(Uri.parse(String.format("https://api.whatsapp.com/send?phone=%s", strCountryCode + strMobile + "&text=" + strText)));
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSelectCountry(Country country) {
        strCountryName = country.getName();
        strCountryCode = country.getDialCode();
        strFlag = country.getFlag();
        tv_country_name.setImageResource(country.getFlag());
        tv_country_code.setVisibility(View.VISIBLE);
        tv_country_code.setText(country.getDialCode());
    }

}