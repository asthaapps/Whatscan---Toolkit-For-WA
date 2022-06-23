package com.whatscan.toolkit.forwa.MsgTools.TextRepeater;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;



import com.whatscan.toolkit.forwa.Ads.Advertisement;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ActivityTextRepeater extends AppCompatActivity {
    RelativeLayout rl_edit_view;
    LinearLayout ll_bottom;
    String Maintext;
    int NoofRepeat;
    String RepeatText;
    Button clearTxtBtn;
    Button convertButton;
    EditText convertedText;
    Button btnCopy;
    TextInputEditText emojeeText;
    ImageView imNewLine;
    TextInputEditText txtInput;
    boolean isNewLine = false;
    String no;
    ProgressDialog pDialog;
    Button btnShare;
    TextView txtNewLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityTextRepeater.this);
        setContentView(R.layout.activity_text_repeater);

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityTextRepeater.this, ll_banner);

        txtNewLine = findViewById(R.id.txtNewLine);
        imNewLine = findViewById(R.id.btnNewLine);
        txtInput = findViewById(R.id.inputText);
        emojeeText = findViewById(R.id.emojeeTxt);
        convertedText = findViewById(R.id.convertedEmojeeTxt);
        convertButton = findViewById(R.id.convertEmojeeBtn);
        btnCopy = findViewById(R.id.bt_copy);
        btnShare = findViewById(R.id.bt_share);
        clearTxtBtn = findViewById(R.id.bt_clear);
        rl_edit_view = findViewById(R.id.rl_edit_view);
        ll_bottom = findViewById(R.id.ll_bottom);
        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        RelativeLayout rl_text_repeater = findViewById(R.id.rl_text_repeater);
        TextInputLayout text_input1 = findViewById(R.id.text_input1);
        TextInputLayout text_input2 = findViewById(R.id.text_input2);
        View ic_include = findViewById(R.id.ic_include);

        la_info.setVisibility(View.VISIBLE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.text_repeat) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_text_repeater.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            text_input2.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            txtInput.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            emojeeText.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            convertedText.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            convertedText.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        pDialog = new ProgressDialog(this);

        if (isNewLine) {
            txtNewLine.setText("New Line On");
            imNewLine.setImageResource(R.drawable.text_on);
        } else {
            txtNewLine.setText("New Line Off");
            imNewLine.setImageResource(R.drawable.text_off);
        }
        imNewLine.setOnClickListener(new newLineClick());


        convertButton.setOnClickListener(new btnConverListner());
        clearTxtBtn.setOnClickListener(new btnClearTextListner());
        convertedText.setOnClickListener(new btnConvertedTexListner());
        btnCopy.setOnClickListener(new btnCopyListner());
        btnShare.setOnClickListener(new btnShareListner());

        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(v -> com.whatscan.toolkit.forwa.Util.Constant.BottomSheetDialogView(ActivityTextRepeater.this, getString(R.string.caption_status), getString(R.string.text_emoji_msg)));
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
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

    private class btnConverListner implements View.OnClickListener {
        public void onClick(View view) {

            rl_edit_view.setVisibility(View.VISIBLE);
            ll_bottom.setVisibility(View.VISIBLE);

            convertedText.setText("");
            RepeatText = txtInput.getText().toString();
            no = emojeeText.getText().toString();
            try {
                NoofRepeat = Integer.parseInt(no);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            if (txtInput.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter Repeat Text", Toast.LENGTH_SHORT).show();
            } else if (emojeeText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter Number of Repeat Text", Toast.LENGTH_SHORT).show();
            } else if (NoofRepeat <= 10000) {
                new CreateRepeateText().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Number of Repeter Text Limited Please Enter Limited Number", Toast.LENGTH_SHORT).show();
            }

            hideSoftKeyboard(ActivityTextRepeater.this);
        }
    }

    private class btnClearTextListner implements View.OnClickListener {
        public void onClick(View view) {
            convertedText.setText("");
            ll_bottom.setVisibility(View.GONE);
            rl_edit_view.setVisibility(View.GONE);
        }
    }

    private class btnConvertedTexListner implements View.OnClickListener {
        @SuppressLint({"WrongConstant"})
        public void onClick(View view) {
            if (!convertedText.getText().toString().isEmpty()) {
                ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(txtInput.getText().toString(), convertedText.getText().toString()));
                Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class btnCopyListner implements View.OnClickListener {
        @SuppressLint({"WrongConstant"})
        public void onClick(View view) {
            if (convertedText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Convert text before copy", Toast.LENGTH_SHORT).show();
                return;
            }
            ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(txtInput.getText().toString(), convertedText.getText().toString()));
            Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private class btnShareListner implements View.OnClickListener {
        public void onClick(View view) {
            if (convertedText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Convert text to share", Toast.LENGTH_LONG).show();
                return;
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction("android.intent.action.SEND");
            shareIntent.setPackage("com.whatsapp");
            shareIntent.putExtra("android.intent.extra.TEXT", convertedText.getText().toString());
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "Select an app to share"));
        }
    }

    private class CreateRepeateText extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Please Wait...");
            pDialog.setProgressStyle(0);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public String doInBackground(String... strings) {
            int i;
            if (isNewLine) {
                for (i = 1; i <= NoofRepeat; i++) {
                    if (i == 1) {
                        Maintext = RepeatText;
                    } else {
                        Maintext += "\n" + RepeatText;
                    }
                }
            } else {
                for (i = 1; i <= NoofRepeat; i++) {
                    if (i == 1) {
                        Maintext = RepeatText;
                    } else {
                        Maintext += "\t" + RepeatText;
                    }
                }
            }
            return null;
        }

        @SuppressLint({"LongLogTag"})
        public void onPostExecute(String result) {
            pDialog.dismiss();
            convertedText.setText(Maintext);
        }
    }

    private class newLineClick implements View.OnClickListener {

        public void onClick(View v) {
            if (isNewLine) {
                isNewLine = false;
                txtNewLine.setText("New Line Off");
                imNewLine.setImageResource(R.drawable.text_off);
                return;
            }
            isNewLine = true;
            txtNewLine.setText("New Line On");
            imNewLine.setImageResource(R.drawable.text_on);
        }
    }
}