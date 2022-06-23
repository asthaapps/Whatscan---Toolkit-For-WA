package com.whatscan.toolkit.forwa.WBubble;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.AllNotificationService;
import com.whatscan.toolkit.forwa.Service.NotificationWear;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WBubble.util.Constant;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class ActivityBubbleSetting extends AppCompatActivity {
    public Button bubbles_border_color, popup_background_color, popup_text_color;
    public SeekBar bubbles_border_seek, bubbles_size_seek, bubbles_trans_seek, chat_font_seek, preview_popup_seek;
    public TextView tv_toolbar, bubbles_border_text, bubbles_size_text, bubbles_trans_text, chat_font_text, preview_popup_text, resetall;
    public ImageView la_preview, clear_history_on_bubble_close, close_bubble_open, display_badge_icon, message_preview_popup, remember_last_position, vibrate_on_bubble_close;
    public RelativeLayout chat_window_layout, rlBubbleSetting, ic_include;
    public ImageView la_back;

    @SuppressLint("WrongConstant")
    public static boolean needPermissionForBlocking(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            return ((AppOpsManager) context.getSystemService("appops")).checkOpNoThrow("android:get_usage_stats", applicationInfo.uid, applicationInfo.packageName) != 0;
        } catch (PackageManager.NameNotFoundException unused) {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        com.whatscan.toolkit.forwa.Util.Constant.adjustFontScale(getResources().getConfiguration(), ActivityBubbleSetting.this);
        setContentView(R.layout.activity_bubble_setting);

        FindView();
    }

    @SuppressLint("SimpleDateFormat")
    private void FindView() {
        tv_toolbar = findViewById(R.id.tv_toolbar);
        la_back = findViewById(R.id.la_back);
        la_preview = findViewById(R.id.la_preview);
        bubbles_size_seek = findViewById(R.id.bubbles_size_seek);
        bubbles_size_text = findViewById(R.id.bubbles_size_text);
        bubbles_trans_seek = findViewById(R.id.bubbles_trans_seek);
        bubbles_trans_text = findViewById(R.id.bubbles_trans_text);
        bubbles_border_seek = findViewById(R.id.bubbles_border_seek);
        bubbles_border_text = findViewById(R.id.bubbles_border_text);
        bubbles_border_color = findViewById(R.id.bubbles_border_color);
        display_badge_icon = findViewById(R.id.display_badge_icon);
        message_preview_popup = findViewById(R.id.message_preview_popup);
        preview_popup_seek = findViewById(R.id.preview_popup_seek);
        preview_popup_text = findViewById(R.id.preview_popup_text);
        popup_background_color = findViewById(R.id.popup_background_color);
        popup_text_color = findViewById(R.id.popup_text_color);
        chat_window_layout = findViewById(R.id.chat_window_layout);
        rlBubbleSetting = findViewById(R.id.rlBubbleSetting);
        ic_include = findViewById(R.id.ic_include);
        chat_font_seek = findViewById(R.id.chat_font_seek);
        chat_font_text = findViewById(R.id.chat_font_text);
        remember_last_position = findViewById(R.id.remember_last_position);
        vibrate_on_bubble_close = findViewById(R.id.vibrate_on_bubble_close);
        clear_history_on_bubble_close = findViewById(R.id.clear_history_on_bubble_close);
        close_bubble_open = findViewById(R.id.close_bubble_open);
        resetall = findViewById(R.id.resetall);

        TextView txtOne = findViewById(R.id.txtOne);
        TextView txtTwo = findViewById(R.id.txtTwo);
        TextView txtThree = findViewById(R.id.txtThree);
        TextView txtFour = findViewById(R.id.txtFour);
        TextView txtFive = findViewById(R.id.txtFive);
        TextView txtSix = findViewById(R.id.txtSix);
        TextView txtSeven = findViewById(R.id.txtSeven);
        TextView txtEight = findViewById(R.id.txtEight);
        TextView txtNine = findViewById(R.id.txtNine);
        TextView txtTen = findViewById(R.id.txtTen);
        TextView txtEleven = findViewById(R.id.txtEleven);
        TextView txtTweleve = findViewById(R.id.txtTweleve);
        TextView txtThirteen = findViewById(R.id.txtThirteen);
        TextView txtFourteen = findViewById(R.id.txtFourteen);
        TextView txtFifteen = findViewById(R.id.txtFifteen);
        TextView txtSixteen = findViewById(R.id.txtSixteen);
        TextView txtSeventeen = findViewById(R.id.txtSeventeen);
        TextView txtEighteen = findViewById(R.id.txtEighteen);
        TextView txtNineteen = findViewById(R.id.txtNineteen);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.setting) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlBubbleSetting.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            bubbles_size_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            bubbles_trans_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            bubbles_border_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            preview_popup_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            chat_font_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            txtEight.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtNine.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtEleven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTweleve.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            txtThirteen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFourteen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFifteen.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            txtSixteen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSeventeen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtEighteen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtNineteen.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        resetall.setOnClickListener(view -> new AlertDialog.Builder(ActivityBubbleSetting.this).setMessage("Do you want to reset all settings?").setPositiveButton("Yes", (dialogInterface, i) -> {
            Constant.saveInt(ActivityBubbleSetting.this, "settings", "bubblesize", 50);
            Constant.saveInt(ActivityBubbleSetting.this, "settings", "bubblealpha", 0);
            Constant.saveInt(ActivityBubbleSetting.this, "settings", "bubbleborder", 0);
            Constant.saveInt(ActivityBubbleSetting.this, "settings", "bubblebordercolor", -1);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "displaybadge", true);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "messagepreviewpopup", true);
            Constant.saveInt(ActivityBubbleSetting.this, "settings", "popuppreviewtime", 2);
            Constant.saveInt(ActivityBubbleSetting.this, "settings", "popupbackgroundcolor", -1);
            Constant.saveInt(ActivityBubbleSetting.this, "settings", "popuptextcolor", -16777216);
            Constant.saveInt(ActivityBubbleSetting.this, "settings", "chatfonttextsize", 3);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "rememberlastposition", false);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "vibrateonbubbleclose", false);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "clearhistoryonbubbleclose", false);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "closebubbleopen", false);

            Toast.makeText(ActivityBubbleSetting.this, "Default settings applied", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }).setNegativeButton("No", (dialogInterface, i) -> {
        }).show());

        la_preview.setOnClickListener(view -> {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ActivityBubbleSetting.this);
            new SimpleDateFormat("hh:mm aaa");
            if (defaultSharedPreferences.getBoolean("timeFormat", false)) {
                new SimpleDateFormat("HH:mm");
            }
            new Date();
            String str = "dc#" + getResources().getString(R.string.app_name);
            Map<String, PendingIntent> map = NotificationWear.openConv;

            map.put(str, PendingIntent.getActivity(ActivityBubbleSetting.this, 0, new Intent(ActivityBubbleSetting.this, ActivityBubbleSetting.class), 0));
            if (AllNotificationService.chatHeadService != null) {
                String[] strArr = {"Hello! What's up?", "Have a good day!", "Hope you're having fun today!", "Hey there!", "Heya!", "Bubble preview window is here.."};
                AllNotificationService.chatHeadService.addChatHead(str, new SpannableString(strArr[new Random().nextInt(strArr.length)]), System.currentTimeMillis(), null, false);
            }
        });

        bubbles_size_seek.setMax(100);
        int intValue = Constant.getInt(this, "settings", "bubblesize", 50);
        bubbles_size_text.setText(intValue + "%");
        bubbles_size_seek.setProgress(intValue);
        bubbles_size_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Constant.saveInt(ActivityBubbleSetting.this, "settings", "bubblesize", i);
                bubbles_size_text.setText(i + "%");
            }
        });

        bubbles_trans_seek.setMax(100);
        int intValue2 = Constant.getInt(this, "settings", "bubblealpha", 0);
        bubbles_trans_text.setText(intValue2 + "%");
        bubbles_trans_seek.setProgress(intValue2);
        bubbles_trans_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Constant.saveInt(ActivityBubbleSetting.this, "settings", "bubblealpha", i);
                TextView textView = bubbles_trans_text;
                textView.setText(i + "%");
            }
        });

        bubbles_border_seek.setMax(4);
        int intValue3 = Constant.getInt(this, "settings", "bubbleborder", 0);
        bubbles_border_text.setText(intValue3 + "");
        bubbles_border_seek.setProgress(intValue3);
        bubbles_border_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Constant.saveInt(ActivityBubbleSetting.this, "settings", "bubbleborder", i);
                TextView textView = bubbles_border_text;
                textView.setText(i + "");
            }
        });


        bubbles_border_color.setBackgroundColor(Constant.getInt(this, "settings", "bubblebordercolor", -1));
        bubbles_border_color.setOnClickListener(v -> ColorPickerDialogBuilder.with(this).setTitle(getString(R.string.choose_color)).wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE).setOnColorSelectedListener(i -> {
        }).setPositiveButton(getString(R.string.txt_ok), (dialogInterface, i, numArr) -> {
            Constant.saveInt(ActivityBubbleSetting.this, "settings", "bubblebordercolor", i);
            bubbles_border_color.setBackgroundColor(i);
        }).setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
        }).build().show());

        if (Constant.getBoolean(this, "settings", "displaybadge", true)) {
            display_badge_icon.setImageResource(R.drawable.text_on);
        }

        display_badge_icon.setOnClickListener(view -> {
            if (Constant.getBoolean(ActivityBubbleSetting.this, "settings", "displaybadge", true)) {
                display_badge_icon.setImageResource(R.drawable.text_off);
                Constant.setBoolean(ActivityBubbleSetting.this, "settings", "displaybadge", false);
                return;
            }
            display_badge_icon.setImageResource(R.drawable.text_on);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "displaybadge", true);
        });

        if (Constant.getBoolean(this, "settings", "messagepreviewpopup", true)) {
            message_preview_popup.setImageResource(R.drawable.text_on);
        }

        message_preview_popup.setOnClickListener(view -> {
            if (Constant.getBoolean(ActivityBubbleSetting.this, "settings", "messagepreviewpopup", true)) {
                message_preview_popup.setImageResource(R.drawable.text_off);
                Constant.setBoolean(ActivityBubbleSetting.this, "settings", "messagepreviewpopup", false);
                return;
            }
            message_preview_popup.setImageResource(R.drawable.text_on);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "messagepreviewpopup", true);
        });

        preview_popup_seek.setMax(4);
        int intValue4 = Constant.getInt(this, "settings", "popuppreviewtime", 2);
        preview_popup_text.setText((intValue4 + 1) + " sec");
        preview_popup_seek.setProgress(intValue4);
        preview_popup_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Constant.saveInt(ActivityBubbleSetting.this, "settings", "popuppreviewtime", i);
                TextView textView = preview_popup_text;
                textView.setText((i + 1) + " sec");
            }
        });

        popup_background_color.setBackgroundColor(Constant.getInt(this, "settings", "popupbackgroundcolor", -1));
        popup_background_color.setOnClickListener(v -> ColorPickerDialogBuilder.with(this).setTitle(getString(R.string.choose_color)).wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE).setOnColorSelectedListener(i -> {
        }).setPositiveButton(getString(R.string.txt_ok), (dialogInterface, i, numArr) -> {
            Constant.saveInt(ActivityBubbleSetting.this, "settings", "popupbackgroundcolor", i);
            popup_background_color.setBackgroundColor(i);
        }).setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
        }).build().show());

        popup_text_color.setBackgroundColor(Constant.getInt(this, "settings", "popuptextcolor", ViewCompat.MEASURED_STATE_MASK));
        popup_text_color.setOnClickListener(v -> {
            ColorPickerDialogBuilder.with(this).setTitle(getString(R.string.choose_color)).wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE).setOnColorSelectedListener(i -> {
            }).setPositiveButton(getString(R.string.txt_ok), (dialogInterface, i, numArr) -> {
                Constant.saveInt(ActivityBubbleSetting.this, "settings", "popuptextcolor", i);
                popup_text_color.setBackgroundColor(i);
            }).setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
            }).build().show();
        });

        chat_window_layout.setOnClickListener(view -> startActivity(new Intent(ActivityBubbleSetting.this, ChatWindowActivity.class)));

        chat_font_seek.setMax(12);
        int intValue5 = Constant.getInt(this, "settings", "chatfonttextsize", 3);
        chat_font_text.setText((intValue5 + 12) + "");
        chat_font_seek.setProgress(intValue5);
        chat_font_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Constant.saveInt(ActivityBubbleSetting.this, "settings", "chatfonttextsize", i);
                TextView textView = chat_font_text;
                textView.setText((i + 12) + "");
            }
        });

        if (Constant.getBoolean(this, "settings", "rememberlastposition", false)) {
            remember_last_position.setImageResource(R.drawable.text_on);
        }
        remember_last_position.setOnClickListener(view -> {
            if (Constant.getBoolean(ActivityBubbleSetting.this, "settings", "rememberlastposition", false)) {
                remember_last_position.setImageResource(R.drawable.text_off);
                Constant.setBoolean(ActivityBubbleSetting.this, "settings", "rememberlastposition", false);
                return;
            }
            remember_last_position.setImageResource(R.drawable.text_on);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "rememberlastposition", true);
        });


        if (Constant.getBoolean(this, "settings", "vibrateonbubbleclose", false)) {
            vibrate_on_bubble_close.setImageResource(R.drawable.text_on);
        }
        vibrate_on_bubble_close.setOnClickListener(view -> {
            if (Constant.getBoolean(ActivityBubbleSetting.this, "settings", "vibrateonbubbleclose", false)) {
                vibrate_on_bubble_close.setImageResource(R.drawable.text_off);
                Constant.setBoolean(ActivityBubbleSetting.this, "settings", "vibrateonbubbleclose", false);
                return;
            }
            vibrate_on_bubble_close.setImageResource(R.drawable.text_on);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "vibrateonbubbleclose", true);
        });

        if (Constant.getBoolean(this, "settings", "clearhistoryonbubbleclose", false)) {
            clear_history_on_bubble_close.setImageResource(R.drawable.text_on);
        }
        clear_history_on_bubble_close.setOnClickListener(view -> {
            if (Constant.getBoolean(ActivityBubbleSetting.this, "settings", "clearhistoryonbubbleclose", false)) {
                clear_history_on_bubble_close.setImageResource(R.drawable.text_off);
                Constant.setBoolean(ActivityBubbleSetting.this, "settings", "clearhistoryonbubbleclose", false);
                return;
            }
            clear_history_on_bubble_close.setImageResource(R.drawable.text_on);
            Constant.setBoolean(ActivityBubbleSetting.this, "settings", "clearhistoryonbubbleclose", true);
        });

        if (Constant.getBoolean(this, "settings", "closebubbleopen", false)) {
            close_bubble_open.setImageResource(R.drawable.text_on);
        }
        close_bubble_open.setOnClickListener(view -> {
            if (needPermissionForBlocking(ActivityBubbleSetting.this)) {
                AlertDialog.Builder title = new AlertDialog.Builder(ActivityBubbleSetting.this).setTitle("Permission for Usage Access");
                title.setMessage(getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.usage_permission)).setPositiveButton("Got it!", (dialogInterface, i) -> {
                    try {
                        startActivity(new Intent("android.settings.USAGE_ACCESS_SETTINGS"));
                    } catch (ActivityNotFoundException unused) {
                        Toast.makeText(ActivityBubbleSetting.this, "It seems your device doesn't allow Usage Access Settings!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).show();
            } else if (Constant.getBoolean(ActivityBubbleSetting.this, "settings", "closebubbleopen", false)) {
                close_bubble_open.setImageResource(R.drawable.text_off);
                Constant.setBoolean(ActivityBubbleSetting.this, "settings", "closebubbleopen", false);
            } else {
                close_bubble_open.setImageResource(R.drawable.text_on);
                Constant.setBoolean(ActivityBubbleSetting.this, "settings", "closebubbleopen", true);
            }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}