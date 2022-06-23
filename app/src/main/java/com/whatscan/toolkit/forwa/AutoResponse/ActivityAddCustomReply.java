package com.whatscan.toolkit.forwa.AutoResponse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.DataBaseHelper.CustomReplyDatabaseHelper;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.material.textfield.TextInputLayout;

public class ActivityAddCustomReply extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public CustomReplyDatabaseHelper customReplyDatabaseHelper;
    public RelativeLayout rlAddCutomReply;
    public AppCompatButton donebtn;
    public TextView firstname_tag;
    public String getId;
    public EditText incomingmsg_text;
    public RadioGroup match_case_group;
    public TextView name_tag;
    public EditText replymsg_text;
    public RadioButton exact_radio, contains_radio;
    public String selectedgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityAddCustomReply.this);
        setContentView(R.layout.activity_add_custom_reply);

        Utils.CheckConnection(ActivityAddCustomReply.this, findViewById(R.id.rlAddCutomReply));
        FindView();
    }

    @SuppressLint("WrongConstant")
    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityAddCustomReply.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        View ic_include = findViewById(R.id.ic_include);
        TextInputLayout text_input1 = findViewById(R.id.text_input1);
        TextInputLayout text_input2 = findViewById(R.id.text_input2);
        LinearLayout llOne = findViewById(R.id.llOne);
        LinearLayout llTwo = findViewById(R.id.llTwo);
        TextView txtOne = findViewById(R.id.txtOne);
        TextView txtTwo = findViewById(R.id.txtTwo);
        incomingmsg_text = findViewById(R.id.incomingmsg_text);
        replymsg_text = findViewById(R.id.replymsg_text);
        match_case_group = findViewById(R.id.match_case_group);
        name_tag = findViewById(R.id.name_tag);
        firstname_tag = findViewById(R.id.firstname_tag);
        donebtn = findViewById(R.id.donebtn);
        rlAddCutomReply = findViewById(R.id.rlAddCutomReply);
        exact_radio = findViewById(R.id.exact_radio);
        contains_radio = findViewById(R.id.contains_radio);

        la_info.setVisibility(View.GONE);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.add_custom_replay) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlAddCutomReply.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            incomingmsg_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            replymsg_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            exact_radio.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            contains_radio.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            setTextInputLayoutHintColor(text_input1, this, R.color.colorWhite);
            setTextInputLayoutHintColor(text_input2, this, R.color.colorWhite);
            llOne.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            llTwo.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
        }

        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("getId");
        getId = stringExtra;
        if (stringExtra != null) {
            String stringExtra2 = intent.getStringExtra("getIncomingmsg");
            String stringExtra3 = intent.getStringExtra("getReplymsg");
            incomingmsg_text.setText(stringExtra2);
            incomingmsg_text.setSelection(incomingmsg_text.getText().length());
            replymsg_text.setText(stringExtra3);
            replymsg_text.setSelection(replymsg_text.getText().length());
            String stringExtra4 = intent.getStringExtra("getSelectedoption");
            if (stringExtra4.equals("exact")) {
                match_case_group.check(R.id.exact_radio);
            } else {
                match_case_group.check(R.id.contains_radio);
            }
            selectedgroup = stringExtra4;
        } else {
            match_case_group.check(R.id.exact_radio);
            selectedgroup = "exact";
        }

        match_case_group.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.contains_radio) {
                selectedgroup = "contains";
            } else if (i == R.id.exact_radio) {
                selectedgroup = "exact";
            }
        });

        customReplyDatabaseHelper = new CustomReplyDatabaseHelper(this);

        name_tag.setOnClickListener(v -> replymsg_text.append(" [name]"));

        firstname_tag.setOnClickListener(v -> replymsg_text.append(" [first name]"));

        donebtn.setOnClickListener(view -> {
            ((InputMethodManager) getSystemService("input_method")).toggleSoftInput(1, 0);
            if (incomingmsg_text.getText().length() <= 0 || replymsg_text.getText().length() <= 0) {
                Toast.makeText(ActivityAddCustomReply.this, "Insert message data", Toast.LENGTH_SHORT).show();
            } else if (getId != null) {
                customReplyDatabaseHelper.updateData(getId, incomingmsg_text.getText().toString(), replymsg_text.getText().toString(), selectedgroup);
                customReplyDatabaseHelper.close();
                Toast.makeText(ActivityAddCustomReply.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ActivityAddCustomReply.this, ActivityCustomReply.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
                finish();
            } else {
                boolean insertData = customReplyDatabaseHelper.insertData(incomingmsg_text.getText().toString(), replymsg_text.getText().toString(), selectedgroup);
                customReplyDatabaseHelper.close();
                if (insertData) {
                    Toast.makeText(ActivityAddCustomReply.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ActivityAddCustomReply.this, ActivityCustomReply.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
                    finish();
                    return;
                }
                Toast.makeText(ActivityAddCustomReply.this, "Custom reply already available", Toast.LENGTH_SHORT).show();
            }
        });

        la_back.setOnClickListener(v -> onBackPressed());
    }

    private void setTextInputLayoutHintColor(TextInputLayout text_input1, Context context, int colorBlack) {
        text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, colorBlack)));
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
        Utils.showSnack(ActivityAddCustomReply.this, findViewById(R.id.rlAddCutomReply), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }
}