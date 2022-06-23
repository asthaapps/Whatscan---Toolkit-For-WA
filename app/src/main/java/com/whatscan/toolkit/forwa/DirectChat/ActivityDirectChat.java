package com.whatscan.toolkit.forwa.DirectChat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.BulkSender.BulkActivityAttachment;
import com.whatscan.toolkit.forwa.BulkSender.BulkActivitySavedMessages;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ActivityDirectChat extends AppCompatActivity implements OnCountryPickerListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public String strCountryName, strCountryCode = "+91";
    public int strFlag;
    public ImageView tv_country_name;
    public TextView tv_toolbar, tv_country_code, savedMessageButton, addImageView, filesSelectedCount;
    public RelativeLayout rl_country, rl_direct;
    public LottieAnimationView  la_info;
    public ImageView la_back;
    public AppCompatButton bt_chat, btnCopyLink;
    public EditText et_mobile, et_text;
    public CountryPicker countryPicker;
    public View ic_include;
    public ArrayList<Uri> selectedFilesPathUri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityDirectChat.this);
        setContentView(R.layout.activity_direct_chat);

        Utils.CheckConnection(ActivityDirectChat.this, findViewById(R.id.rl_direct));

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityDirectChat.this, ll_banner);

        la_back = findViewById(R.id.la_back);
        la_info = findViewById(R.id.la_info);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        rl_country = findViewById(R.id.rl_country);
        bt_chat = findViewById(R.id.bt_chat);
        btnCopyLink = findViewById(R.id.btnCopyLink);
        et_mobile = findViewById(R.id.et_mobile);
        et_text = findViewById(R.id.et_text);
        tv_country_name = findViewById(R.id.tv_country_name);
        tv_country_code = findViewById(R.id.tv_country_code);
        savedMessageButton = findViewById(R.id.savedMessageButton);
        addImageView = findViewById(R.id.addImageView);
        filesSelectedCount = findViewById(R.id.filesSelectedCount);
        rl_direct = findViewById(R.id.rl_direct);
        ic_include = findViewById(R.id.ic_include);
        RadioGroup sendByRadioGroup = findViewById(R.id.sendByRadioGroup);
        int i3 = R.id.whatsappRadioButton;
        RadioButton radioButton = (RadioButton) findViewById(i3);
        int i4 = R.id.whatsappBusinessRadioButton;
        RadioButton j = (RadioButton) findViewById(i4);

        la_info.setVisibility(View.VISIBLE);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.direct_chat) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_direct.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            rl_country.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));
            sendByRadioGroup.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));
            tv_country_code.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_mobile.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            addImageView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            savedMessageButton.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_mobile.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_mobile.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_mobile.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));
            et_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_text.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_text.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));

        } else {
            setStatusBar();
            rl_direct.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            rl_country.setBackground(ContextCompat.getDrawable(this, R.drawable.boder));
            sendByRadioGroup.setBackground(ContextCompat.getDrawable(this, R.drawable.boder));
            tv_country_code.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_mobile.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_text.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            addImageView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            savedMessageButton.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_mobile.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_text.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            et_text.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_card));
            et_mobile.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_card));
        }

        la_back.setOnClickListener(v -> onBackPressed());

        la_info.setOnClickListener(v -> com.whatscan.toolkit.forwa.Util.Constant.BottomSheetDialogView(ActivityDirectChat.this, getString(R.string.direct_chat), getString(R.string.fake_profile_info_text)));

        rl_country.setOnClickListener(v -> {
            CountryPicker.Builder builder = new CountryPicker.Builder().with(ActivityDirectChat.this).listener(ActivityDirectChat.this);
            builder.style(R.style.DialogStyle);
            builder.theme(CountryPicker.THEME_NEW);
            builder.canSearch(true);
            builder.sortBy(CountryPicker.SORT_BY_DIAL_CODE);
            countryPicker = builder.build();
            countryPicker.showBottomSheet(ActivityDirectChat.this);
        });

        sendByRadioGroup.setOnCheckedChangeListener((group, checkedId) -> Preference.saveBooleanData(ActivityDirectChat.this, Event.IS_WA_BUSINESS_SELECTED_FOR_MESSAGE.toString(), checkedId == R.id.whatsappBusinessRadioButton));

        if (Preference.getSavedBoolean(ActivityDirectChat.this, Event.IS_WA_BUSINESS_SELECTED_FOR_MESSAGE.toString(), false)) {
            i3 = i4;
        }
        sendByRadioGroup.check(i3);

        savedMessageButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityDirectChat.this, BulkActivitySavedMessages.class);
            intent.putExtra(Event.SELECT_QUICK_REPLY.name(), true);
            startActivityForResult(intent, 10098);
        });

        addImageView.setOnClickListener(v -> {
            startActivityForResult(new Intent(ActivityDirectChat.this, BulkActivityAttachment.class).putParcelableArrayListExtra(Event.BULK_SENDING_FILE_ATTACHMENT.name(), selectedFilesPathUri), 786);
        });

        bt_chat.setOnClickListener(v -> {
            Boolean bool = null;
            String strMobile = et_mobile.getText().toString();
            String strText = et_text.getText().toString();

            if (strCountryCode == null) {
                Toast.makeText(this, "Select Country", Toast.LENGTH_SHORT).show();
            } else if (strMobile.isEmpty()) {
                Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            } else if (strText.isEmpty()) {
                Toast.makeText(this, "Enter Message", Toast.LENGTH_SHORT).show();
            }

            if (selectedFilesPathUri != null) {
                bool = selectedFilesPathUri.isEmpty();
            }

            if (Boolean.TRUE.equals(bool)) {
                SendWp(strMobile, strText, j.isChecked());
            } else {
                Utils.sendPhotosInWhatsApp(ActivityDirectChat.this, selectedFilesPathUri, strMobile, strText, j.isChecked());
            }
        });

        btnCopyLink.setOnClickListener(v -> {
            String strMobile = et_mobile.getText().toString();
            String strText = et_text.getText().toString();

            if (strMobile.isEmpty()) {
                Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            } else if (strText.isEmpty()) {
                Utils.setClipboard(ActivityDirectChat.this, "https://wa.me/" + strMobile);
            } else {
                Utils.setClipboard(ActivityDirectChat.this, "https://wa.me/" + strMobile + "?text=" + strText);
            }
        });
    }

    private void setTextInputLayoutHintColor(TextInputLayout text_input1, Context context, int colorBlack) {
        text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, colorBlack)));
    }

    private void SendWp(String strMobile, String strText, boolean z) {
        PackageManager packageManager = getPackageManager();
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
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int i, int i2, @Nullable @org.jetbrains.annotations.Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 10098 && -1 == i2) {
            if (intent != null) {
                if (intent.getStringExtra(Event.MESSAGE.name()) != null) {
                    et_text.setText(intent.getStringExtra(Event.MESSAGE.name()));
                }
            }
        } else if (i != 786 || -1 != i2) {
            return;
        }
        if (intent == null || !intent.getBooleanExtra(Event.IS_SENDING_FILE_DIRECTLY.name(), false)) {
            return;
        }
        selectedFilesPathUri.clear();
        ArrayList<Uri> parcelableArrayListExtra2 = intent.getParcelableArrayListExtra(Event.BULK_SENDING_FILE_ATTACHMENT.name());
        if (parcelableArrayListExtra2 != null) {
            selectedFilesPathUri.addAll(parcelableArrayListExtra2);
            setFileSelectionCount();
        }
    }

    private void setFileSelectionCount() {
        filesSelectedCount.setVisibility(this.selectedFilesPathUri.size() > 0 ? View.VISIBLE : View.GONE);
        filesSelectedCount.setText(String.valueOf(this.selectedFilesPathUri.size()));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setStatusBar() {
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
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.darkBlack));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivityDirectChat.this, findViewById(R.id.rl_direct), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}