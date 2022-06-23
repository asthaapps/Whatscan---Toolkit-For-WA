package com.whatscan.toolkit.forwa.Util;

import static android.content.Context.WINDOW_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.whatscan.toolkit.forwa.Ads.ActivityPremium;
import com.whatscan.toolkit.forwa.GetSet.StatusModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.User.ActivityLogin;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Constant {
    public static final String FIRST_NAME_CODE = "{FirstName}";
    public static final String FULL_NAME_CODE = "{FullName}";
    public static final String LAST_NAME_CODE = "{lastName}";
    public static final String IMAGE = "image";
    public static final String DOCUMENT = "document";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    public static final String GIF = "gif";
    public static final String WALLPAPER = "wallpaper";
    public static final String VOICE = "status";
    public static final String STATUS = "Status";
    public static final String NONDEFAULT = "nondefault";
    public static final String imagesReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images";
    public static final String imagesSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images/Sent";
    public static final String imagesReceivedPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images";
    public static final String imagesSentPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/Sent";
    public static final String imagesReceivedPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Images";
    public static final String imagesSentPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Images/Sent";
    public static final String imagesReceivedPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Images";
    public static final String imagesSentPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Images/Sent";
    public static final String documentsReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Documents";
    public static final String documentsSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Documents/Sent";
    public static final String documentsReceivedPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents";
    public static final String documentsSentPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents/Sent";
    public static final String documentsReceivedPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Documents";
    public static final String documentsSentPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Documents/Sent";
    public static final String documentsReceivedPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Documents";
    public static final String documentsSentPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Documents/Sent";
    public static final String videosReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Video";
    public static final String videosSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Video/Sent";
    public static final String videosReceivedPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video";
    public static final String videosSentPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video/Sent";
    public static final String videosReceivedPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Video";
    public static final String videosSentPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Video/Sent";
    public static final String videosReceivedPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Video";
    public static final String videosSentPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Video/Sent";
    public static final String audiosReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Audio";
    public static final String audiosSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Audio/Sent";
    public static final String audiosReceivedPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Audio";
    public static final String audiosSentPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Audio/Sent";
    public static final String audiosReceivedPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Audio";
    public static final String audiosSentPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Audio/Sent";
    public static final String audiosReceivedPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Audio";
    public static final String audiosSentPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Audio/Sent";
    public static final String gifReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Animated Gifs";
    public static final String gifSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Animated Gifs/Sent";
    public static final String gifReceivedPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Animated Gifs";
    public static final String gifSentPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Animated Gifs/Sent";
    public static final String gifReceivedPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Animated Gifs";
    public static final String gifSentPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Animated Gifs/Sent";
    public static final String gifReceivedPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Animated Gifs";
    public static final String gifSentPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Animated Gifs/Sent";
    public static final String wallReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WallPaper";
    public static final String wallgifSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WallPaper/Sent";
    public static final String wallReceivedPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WallPaper";
    public static final String wallgifSentPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WallPaper/Sent";
    public static final String wallReceivedPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/Wallpaper";
    public static final String wallSentPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/Wallpapers/Sent";
    public static final String wallReceivedPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/Wallpaper";
    public static final String wallSentPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/Wallpaper/Sent";
    public static final String voiceReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Voice Notes";
    public static final String voicegifSentPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Voice Notes/Sent";
    public static final String voiceReceivedPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Voice Notes";
    public static final String voicegifSentPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Voice Notes/Sent";
    public static final String voiceReceivedPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Voice Notes";
    public static final String voiceSentPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Voice Notes/Sent";
    public static final String voiceReceivedPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Voice Notes";
    public static final String voiceSentPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Voice Notes/Sent";
    public static final String stickerReceivedPath = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Stickers";
    public static final String stickerReceivedPath11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Stickers";
    public static final String stickerReceivedPathNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/WhatsApp Business Stickers";
    public static final String stickerReceivedPathB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Stickers";
    public static final String statuscache = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses";
    public static final String statuscache11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
    public static final String statuscacheNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/.Statuses";
    public static final String statuscacheB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses";
    public static final String statusdownload = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Status Download";
    public static final String statusdownload11 = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/WhatsApp/Media/.Status Download";
    public static final String statusdownloadNB = Environment.getExternalStorageDirectory().toString() + "/WhatsApp Business/Media/.Status Download";
    public static final String statusdownloadB = Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Status Download";
    public static boolean WhatsAppChatLock = false;
    public static String seperator = "__/__";
    private static Dialog processDialog;



    public static void IntProgress(Context context) {
        processDialog = new Dialog(context);
        processDialog.requestWindowFeature(1);
        processDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        processDialog.setContentView(R.layout.progress_loading);
        processDialog.setCancelable(false);

        RelativeLayout rlDialog = processDialog.findViewById(R.id.rlDialog);

        if (Preference.getBooleanTheme(false)){
            rlDialog.setBackground(ContextCompat.getDrawable(context, R.drawable.round_progress_w));
        }
    }

    public static void ShowProgress() {
        try {
            processDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DismissProgress() {
        try {
            if (processDialog != null && processDialog.isShowing()) {
                processDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles())) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    public static String convertArrayToString(ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            sb.append(arrayList.get(i).replace("-", ""));
            if (i < arrayList.size() - 1) {
                sb.append(seperator);
            }
        }
        return sb.toString();
    }

    public static String encode(String str) {
        return URLEncoder.encode(str);
    }

    public static String decode(String str) {
        return str != null ? URLDecoder.decode(str) : "";
    }

    public static String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        date.getTime();
        return simpleDateFormat.format(date);
    }

    public static void addEvent(Context context, String str, Bundle bundle) {
        FirebaseAnalytics.getInstance(context).logEvent(str, bundle);
    }

    public static void adjustFontScale(Configuration configuration, Activity activity) {
        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) activity.getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        activity.getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

    public static boolean whatsappInstalledOrNot(Activity activity) {
        PackageManager pm = activity.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return app_installed;
    }

    public static boolean whatsappBusinessInstalledOrNot(Activity activity) {
        PackageManager pm = activity.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return app_installed;
    }

    public static void BottomSheetDialogView(Activity activity, String title, String msg) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.info_dialog, null);
        bottomSheetDialog.setContentView(inflate);

        RelativeLayout rl_dialog = inflate.findViewById(R.id.rl_dialog);
        TextView tv_dialog_title = inflate.findViewById(R.id.tv_dialog_title);
        TextView tv_dialog_tip = inflate.findViewById(R.id.tv_dialog_tip);
        AppCompatButton bt_start = inflate.findViewById(R.id.bt_start);

        if (Preference.getBooleanTheme(false)){
            rl_dialog.setBackground(ContextCompat.getDrawable(activity, R.drawable.dialog_black));
            tv_dialog_title.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            tv_dialog_tip.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
        }

        bt_start.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tv_dialog_title.setText(title);
        tv_dialog_tip.setText(msg);

        bottomSheetDialog.show();
    }

    public static void BottomSheetDialogPremium(Activity activity, String header, String description) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.dailog_upgrade_premium, null);
        bottomSheetDialog.setContentView(inflate);

        LinearLayout llUpgradeP = inflate.findViewById(R.id.llUpgradeP);
        TextView txtHeader = inflate.findViewById(R.id.txtHeader);
        TextView freeTrialTextView = inflate.findViewById(R.id.freeTrialTextView);

        if (Preference.getBooleanTheme(false)) {
            llUpgradeP.setBackgroundColor(ContextCompat.getColor(activity, R.color.darkBlack));
            txtHeader.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            freeTrialTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
        }

        txtHeader.setText(header);
        freeTrialTextView.setText(description);

        inflate.findViewById(R.id.startUpgradeButton).setOnClickListener(v -> {
            activity.startActivity(new Intent(activity, ActivityPremium.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.imgClose).setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    public static void BottomSheetDialogLogIn(Activity activity) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.dailog_login, null);
        bottomSheetDialog.setContentView(inflate);

        LinearLayout rlLogin = inflate.findViewById(R.id.rlLogin);
        TextView txtOne = inflate.findViewById(R.id.txtOne);
        TextView tv_text1 = inflate.findViewById(R.id.tv_text1);

        if (Preference.getBooleanTheme(false)) {
            rlLogin.setBackgroundColor(ContextCompat.getColor(activity, R.color.darkBlack));
            txtOne.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            tv_text1.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
        }

        inflate.findViewById(R.id.bt_sign_in).setOnClickListener(v -> {
            activity.startActivity(new Intent(activity, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    public static void BottomSheetDialogRateApp(Activity activity) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.dialog_add_review, null);
        bottomSheetDialog.setContentView(inflate);

        LinearLayout rlReview = inflate.findViewById(R.id.rlReview);
        TextView txtOne = inflate.findViewById(R.id.txtOne);
        TextView txtTwo = inflate.findViewById(R.id.txtTwo);
        TextInputLayout mtf_name = inflate.findViewById(R.id.mtf_name);
        EditText et_post = inflate.findViewById(R.id.et_post);
        AppCompatRatingBar rating_bar = inflate.findViewById(R.id.rating_bar);

        if (Preference.getBooleanTheme(false)) {
            rlReview.setBackgroundColor(ContextCompat.getColor(activity, R.color.darkBlack));
            txtOne.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            et_post.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            mtf_name.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorWhite)));
        }

        inflate.findViewById(R.id.bt_cancel).setOnClickListener(v -> bottomSheetDialog.dismiss());

        inflate.findViewById(R.id.bt_submit).setOnClickListener(v -> {
            String review = et_post.getText().toString().trim();
            try {
                Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(goMarket);
            } catch (ActivityNotFoundException e) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName());
                Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(goMarket);
            }
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}