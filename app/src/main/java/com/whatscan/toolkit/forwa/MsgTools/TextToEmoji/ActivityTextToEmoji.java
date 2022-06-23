package com.whatscan.toolkit.forwa.MsgTools.TextToEmoji;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
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

import java.io.IOException;
import java.io.InputStream;

public class ActivityTextToEmoji extends AppCompatActivity {
    public RelativeLayout rl_dialog, rl_edit_view, rl_text_emoji;
    public LinearLayout ll_bottom;
    public TextInputEditText inputText;
    public TextInputEditText emojeeTxt;
    public EditText convertedText;
    public View ic_include;
    public Button bt_clear, bt_copy, bt_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityTextToEmoji.this);
        setContentView(R.layout.activity_text_emoji);

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityTextToEmoji.this, ll_banner);

        rl_dialog = findViewById(R.id.rl_dialog);
        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        inputText = findViewById(R.id.inputText);
        emojeeTxt = findViewById(R.id.emojeeTxt);
        convertedText = findViewById(R.id.convertedEmojeeTxt);
        bt_clear = findViewById(R.id.bt_clear);
        bt_copy = findViewById(R.id.bt_copy);
        bt_share = findViewById(R.id.bt_share);
        rl_edit_view = findViewById(R.id.rl_edit_view);
        rl_text_emoji = findViewById(R.id.rl_text_emoji);
        ll_bottom = findViewById(R.id.ll_bottom);
        ic_include = findViewById(R.id.ic_include);
        Button convertEmojeeBtn = findViewById(R.id.convertEmojeeBtn);
        TextInputLayout text_input1 = findViewById(R.id.text_input1);
        TextInputLayout text_input2 = findViewById(R.id.text_input2);

        la_info.setVisibility(View.VISIBLE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.text_emoji) + "</small>"));

        if (Preference.getBooleanTheme(false)){
            setStatusBarTheme();
            rl_text_emoji.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            text_input2.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            inputText.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            emojeeTxt.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(v -> Constant.BottomSheetDialogView(ActivityTextToEmoji.this, getString(R.string.text_emoji_header), getString(R.string.text_emoji_msg)));
        bt_clear.setOnClickListener(new btnClearTextListner());
        bt_copy.setOnClickListener(new btnCopyButtonListner());
        bt_share.setOnClickListener(new btnShareListner());
        convertEmojeeBtn.setOnClickListener(new btnConvertListner());
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

    private class btnConvertListner implements View.OnClickListener {
        public void onClick(View view) {

            rl_edit_view.setVisibility(View.VISIBLE);
            ll_bottom.setVisibility(View.VISIBLE);

            if (inputText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter text", Toast.LENGTH_SHORT).show();
                return;
            }
            char[] charArray = inputText.getText().toString().toCharArray();
            convertedText.setText(".\n");
            for (char character : charArray) {
                byte[] localObject3;
                InputStream localObject2;
                if (character == '?') {
                    try {
                        InputStream localObject1 = getBaseContext().getAssets().open("ques.txt");
                        localObject3 = new byte[localObject1.available()];
                        localObject1.read(localObject3);
                        localObject1.close();
                        convertedText.append(new String(localObject3).replaceAll("[*]", emojeeTxt.getText().toString()) + "\n\n");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } else if (character == ((char) (character & 95)) || Character.isDigit(character)) {
                    try {
                        localObject2 = getBaseContext().getAssets().open(character + ".txt");
                        localObject3 = new byte[localObject2.available()];
                        localObject2.read(localObject3);
                        localObject2.close();
                        convertedText.append(new String(localObject3).replaceAll("[*]", emojeeTxt.getText().toString()) + "\n\n");
                    } catch (IOException ioe2) {
                        ioe2.printStackTrace();
                    }
                } else {
                    try {
                        localObject2 = getBaseContext().getAssets().open("low" + character + ".txt");
                        localObject3 = new byte[localObject2.available()];
                        localObject2.read(localObject3);
                        localObject2.close();
                        convertedText.append(new String(localObject3).replaceAll("[*]", emojeeTxt.getText().toString()) + "\n\n");
                    } catch (IOException ioe22) {
                        ioe22.printStackTrace();
                    }
                }
            }
        }
    }

    private class btnClearTextListner implements View.OnClickListener {
        public void onClick(View view) {
            convertedText.setText("");
            ll_bottom.setVisibility(View.GONE);
            rl_edit_view.setVisibility(View.GONE);
        }
    }

    private class btnCopyButtonListner implements View.OnClickListener {
        @SuppressLint({"WrongConstant"})
        public void onClick(View view) {
            if (convertedText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Convert text before copy", Toast.LENGTH_SHORT).show();
                return;
            }
            ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(inputText.getText().toString(), convertedText.getText().toString()));
            Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private class btnShareListner implements View.OnClickListener {
        public void onClick(View view) {
            if (convertedText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Convert text to share", Toast.LENGTH_LONG).show();
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
}
