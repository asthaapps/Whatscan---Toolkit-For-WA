package com.whatscan.toolkit.forwa.AutoResponse;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

public class ActivityAutoSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityAutoSetting.this);
        setContentView(R.layout.activity_auto_settings);

        FindView();
    }

    @SuppressLint("ResourceType")
    private void FindView() {
        RadioGroup reply_options_group = findViewById(R.id.reply_options_group);
        RadioButton time_delay_radio = findViewById(R.id.time_delay_radio);
        RadioButton welcome_radio_button = findViewById(R.id.welcome_radio_button);
        RadioButton ifphonelock_radio = findViewById(R.id.ifphonelock_radio);
        RadioButton reply_continuously_radio = findViewById(R.id.reply_continuously_radio);
        RadioButton reply_once_radio = findViewById(R.id.reply_once_radio);
        ImageView reply_options_info = findViewById(R.id.reply_options_info);
        ImageView smart_options_info = findViewById(R.id.smart_options_info);
        ImageView edit_welcome_text = findViewById(R.id.edit_welcome_text);
        TextView welcome_text = findViewById(R.id.welcome_text);
        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        TextView txtOne = findViewById(R.id.txtOne);
        TextView txtTwo = findViewById(R.id.txtTwo);
        TextView txtThree = findViewById(R.id.txtThree);
        RelativeLayout rlAutoSetting = findViewById(R.id.rlAutoSetting);
        View ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.action_settings) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlAutoSetting.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            ifphonelock_radio.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            reply_continuously_radio.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            reply_once_radio.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            welcome_radio_button.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            time_delay_radio.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            welcome_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        FragmentHome.isSpinnerInitial = true;

        if (Preference.getBoolean(this, "settings", "ifphonelock_radio", false)) {
            ifphonelock_radio.setChecked(true);
            ifphonelock_radio.setSelected(true);
        }

        ifphonelock_radio.setOnClickListener(view -> {
            if (!ifphonelock_radio.isSelected()) {
                Preference.setBoolean(ActivityAutoSetting.this, "settings", "ifphonelock_radio", true);
                ifphonelock_radio.setChecked(true);
                ifphonelock_radio.setSelected(true);
                return;
            }
            Preference.setBoolean(ActivityAutoSetting.this, "settings", "ifphonelock_radio", false);
            ifphonelock_radio.setChecked(false);
            ifphonelock_radio.setSelected(false);
        });

        smart_options_info.setOnClickListener(view -> showInfoDialog("Reply time options", "1) Reply continuously is use to continue reply everytime without delay to the receiver. \\n\\n2) Time delay is use to replay after selected time(sec/min) interval to the receiver. \\n\\n3) Reply once is use to reply only one time to the receiver until you restart service or choose another option in reply options."));

        reply_options_info.setOnClickListener(view -> showInfoDialog("Smart options", "Turn on if phone is locked smart option will start app service automatically if your device is locked and incoming message arrive, So if you are busy or unavailable to answer the reply our app will answer the reply if you enable this option."));

        if (Preference.getBoolean(this, "settings", "reply_once_radio", false)) {
            reply_options_group.check(R.id.reply_once_radio);
        } else if (Preference.getBoolean(this, "settings", "time_delay_radio", false)) {
            reply_options_group.check(R.id.time_delay_radio);
            time_delay_radio.setText(Html.fromHtml("Time delay (" + Preference.getstring(this, "timedelaydialog", "timedelay", "3 Seconds") + ")"));
        } else {
            reply_options_group.check(R.id.reply_continuously_radio);
        }

        reply_options_group.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.reply_continuously_radio) {
                Preference.setBoolean(ActivityAutoSetting.this, "servicecheck", "reply_once_radio", false);
                Preference.setBoolean(ActivityAutoSetting.this, "settings", "reply_continuously_radio", true);
                Preference.setBoolean(ActivityAutoSetting.this, "settings", "time_delay_radio", false);
                Preference.setBoolean(ActivityAutoSetting.this, "settings", "reply_once_radio", false);
            } else if (i == R.id.reply_once_radio) {
                Preference.setBoolean(ActivityAutoSetting.this, "servicecheck", "reply_once_radio", false);
                Preference.setBoolean(ActivityAutoSetting.this, "settings", "reply_once_radio", true);
                Preference.setBoolean(ActivityAutoSetting.this, "settings", "time_delay_radio", false);
                Preference.setBoolean(ActivityAutoSetting.this, "settings", "reply_continuously_radio", false);
            }
        });

        time_delay_radio.setOnClickListener(view -> {
            Preference.setBoolean(ActivityAutoSetting.this, "servicecheck", "reply_once_radio", false);
            Preference.setBoolean(ActivityAutoSetting.this, "settings", "time_delay_radio", true);
            Preference.setBoolean(ActivityAutoSetting.this, "settings", "reply_continuously_radio", false);
            Preference.setBoolean(ActivityAutoSetting.this, "settings", "reply_once_radio", false);

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityAutoSetting.this);
            View inflate = LayoutInflater.from(ActivityAutoSetting.this).inflate(R.layout.time_delay_dialog, null);
            bottomSheetDialog.setContentView(inflate);

            RelativeLayout rlDelay = inflate.findViewById(R.id.rlDelay);
            TextView txtOneT = inflate.findViewById(R.id.txtOne);
            TextView txtTwoT = inflate.findViewById(R.id.txtTwo);
            TextInputLayout mtf_UserName = inflate.findViewById(R.id.mtf_UserName);
            final EditText editText = inflate.findViewById(R.id.editreply_text);
            final Spinner spinner = inflate.findViewById(R.id.second_minute_spinner);

            if (Preference.getBooleanTheme(false)){
                rlDelay.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
                txtOneT.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTwoT.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                editText.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mtf_UserName.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            }

            editText.setText(Preference.getstring(ActivityAutoSetting.this, "timedelaydialog", "timedelay", "3 Seconds").split(" ")[0]);
            editText.setSelection(editText.getText().length());

            ArrayAdapter arrayAdapter = new ArrayAdapter(ActivityAutoSetting.this, (int) R.layout.delayspinnertext, new String[]{"Seconds", "Minutes"});
            arrayAdapter.setDropDownViewResource(17367049);
            spinner.setAdapter(arrayAdapter);
            if (Preference.getstring(ActivityAutoSetting.this, "timedelaydialog", "timedelay", "3 Seconds").split(" ")[1].equalsIgnoreCase("Seconds")) {
                spinner.setSelection(0);
            } else {
                spinner.setSelection(1);
            }

            inflate.findViewById(R.id.updatebtn).setOnClickListener(view13 -> {
                if (editText.getText().toString().length() > 0) {
                    if (spinner.getSelectedItem().toString().equals("Seconds")) {
                        if (Integer.parseInt(editText.getText().toString()) > 600) {
                            Toast.makeText(ActivityAutoSetting.this, "Set seconds below or equal to 600", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else if (Integer.parseInt(editText.getText().toString()) > 10) {
                        Toast.makeText(ActivityAutoSetting.this, "Set minutes below or equal to 10", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(ActivityAutoSetting.this, editText.getText().toString() + " " + spinner.getSelectedItem().toString() + " set", Toast.LENGTH_SHORT).show();
                    Preference.setstring(ActivityAutoSetting.this, "timedelaydialog", "timedelay", editText.getText().toString() + " " + spinner.getSelectedItem().toString());
                    time_delay_radio.setText(Html.fromHtml("Time delay (" + editText.getText().toString() + " " + spinner.getSelectedItem().toString() + ")"));
                    bottomSheetDialog.dismiss();
                    return;
                }
                Toast.makeText(ActivityAutoSetting.this, "Enter time", Toast.LENGTH_SHORT).show();
            });

            bottomSheetDialog.show();
        });


        if (Preference.getBoolean(ActivityAutoSetting.this, "settings", "welcome_radio_button", false)) {
            welcome_radio_button.setChecked(true);
            welcome_radio_button.setSelected(true);
        }

        welcome_text.setText(Preference.getstring(ActivityAutoSetting.this, "settings", "welcome_text", "Hi welcome, thank you for messaging me, i will reply you soon"));

        edit_welcome_text.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityAutoSetting.this);
            View inflate1 = LayoutInflater.from(ActivityAutoSetting.this).inflate(R.layout.text_update_dialog, null);
            bottomSheetDialog.setContentView(inflate1);

            RelativeLayout rlAutoReplay = inflate1.findViewById(R.id.rlAutoReplay);
            TextView txt_header = inflate1.findViewById(R.id.txt_header);
            EditText editText = inflate1.findViewById(R.id.editreply_text);
            TextInputLayout text_input1 = inflate1.findViewById(R.id.text_input1);

            if (Preference.getBooleanTheme(false)) {
                rlAutoReplay.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
                txt_header.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                editText.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            }

            (inflate1.findViewById(R.id.close_dialog)).setOnClickListener(view1 -> bottomSheetDialog.dismiss());

            editText.setText(welcome_text.getText().toString());
            editText.setSelection(editText.getText().length());
            (inflate1.findViewById(R.id.updatebtn)).setOnClickListener(view12 -> {
                Toast.makeText(ActivityAutoSetting.this, "Text Updated", Toast.LENGTH_SHORT).show();
                Preference.setstring(ActivityAutoSetting.this, "settings", "welcome_text", editText.getText().toString());
                welcome_text.setText(editText.getText().toString());
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });

        welcome_radio_button.setOnClickListener(view -> {
            if (!welcome_radio_button.isSelected()) {
                Preference.setBoolean(ActivityAutoSetting.this, "settings", "welcome_radio_button", true);
                welcome_radio_button.setChecked(true);
                welcome_radio_button.setSelected(true);
                return;
            }
            Preference.setBoolean(ActivityAutoSetting.this, "settings", "welcome_radio_button", false);
            welcome_radio_button.setChecked(false);
            welcome_radio_button.setSelected(false);
        });

        la_back.setOnClickListener(v -> onBackPressed());
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

    public final void showInfoDialog(String str, String str2) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityAutoSetting.this);
        View inflate = LayoutInflater.from(ActivityAutoSetting.this).inflate(R.layout.dialog_hint, null);
        bottomSheetDialog.setContentView(inflate);

        LinearLayout llHint = inflate.findViewById(R.id.llHint);
        Button button = inflate.findViewById(R.id.btnHintContinue);
        TextView txtTitle = inflate.findViewById(R.id.txtTitle);
        TextView txtInfo = inflate.findViewById(R.id.txtInfo);

        if (Preference.getBooleanTheme(false)){
            llHint.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtTitle.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtInfo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        button.setText(getString(R.string.start));
        txtTitle.setText(str);
        txtInfo.setText(Html.fromHtml(str2));

        button.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }
}