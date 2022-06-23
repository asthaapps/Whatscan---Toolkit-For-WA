package com.whatscan.toolkit.forwa.WBubble;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
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
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WBubble.util.Constant;

public class ChatWindowActivity extends AppCompatActivity {
    public TextView applytheme;
    public RelativeLayout rlChatWindow;
    public View ic_include;
    public ImageView chat_window_img, next_img, previous_img;
    public int[] chatthemes = {R.drawable.chat_layout_1, R.drawable.chat_layout_2, R.drawable.chat_layout_3, R.drawable.chat_layout_4, R.drawable.chat_layout_5};
    public int countnumber = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        com.whatscan.toolkit.forwa.Util.Constant.adjustFontScale(getResources().getConfiguration(), ChatWindowActivity.this);
        setContentView(R.layout.activity_chat_window);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ChatWindowActivity.this, ll_banner);

        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        ImageView la_back = findViewById(R.id.la_back);

        countnumber = Constant.getInt(this, "savetheme", "selectedtheme", 0);
        chat_window_img = findViewById(R.id.chat_window_img);
        applytheme = findViewById(R.id.applytheme);
        previous_img = findViewById(R.id.previous_img);
        rlChatWindow = findViewById(R.id.rlChatWindow);
        ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + "Chat Window Layout (" + (this.countnumber + 1) + "/5)" + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlChatWindow.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }

        la_back.setOnClickListener(v -> onBackPressed());

        applytheme.setOnClickListener(view -> {
            applytheme.setText("Applied");
            Constant.saveInt(ChatWindowActivity.this, "savetheme", "selectedtheme", countnumber);
            Toast.makeText(ChatWindowActivity.this, "Layout " + (countnumber + 1) + " is Selected", Toast.LENGTH_SHORT).show();
        });

        applytheme.setText("Applied");

        chat_window_img.setImageResource(chatthemes[countnumber]);

        if (countnumber == 0) {
            previous_img.setVisibility(View.INVISIBLE);
        }

        previous_img.setOnClickListener(view -> {
            if (countnumber > 0) {
                next_img.setVisibility(View.VISIBLE);
                countnumber--;
            }
            if (Constant.getInt(ChatWindowActivity.this, "savetheme", "selectedtheme", 0) == countnumber) {
                applytheme.setText("Applied");
            } else {
                applytheme.setText("Apply");
            }
            if (countnumber == 0) {
                previous_img.setVisibility(View.INVISIBLE);
            }
            tv_toolbar.setText(Html.fromHtml("<small>" + "Chat Window Layout (" + (this.countnumber + 1) + "/5)" + "</small>"));
            chat_window_img.setImageResource(chatthemes[countnumber]);
        });

        next_img = findViewById(R.id.next_img);
        if (countnumber == 4) {
            next_img.setVisibility(View.INVISIBLE);
        }

        next_img.setOnClickListener(view -> {
            if (countnumber < 4) {
                previous_img.setVisibility(View.VISIBLE);
                countnumber++;
            }
            if (Constant.getInt(ChatWindowActivity.this, "savetheme", "selectedtheme", 0) == countnumber) {
                applytheme.setText("Applied");
            } else {
                applytheme.setText("Apply");
            }
            if (countnumber == 4) {
                next_img.setVisibility(View.INVISIBLE);
            }
            tv_toolbar.setText(Html.fromHtml("<small>" + "Chat Window Layout (" + (this.countnumber + 1) + "/5)" + "</small>"));
            chat_window_img.setImageResource(chatthemes[countnumber]);
        });
    }

    @Override
    public void onBackPressed() {
        finish();
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
}