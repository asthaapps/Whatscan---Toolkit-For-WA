package com.whatscan.toolkit.forwa.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import com.whatscan.toolkit.forwa.Adapter.StoryImage;
import com.whatscan.toolkit.forwa.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    public static void CheckConnection(Activity activity, View view) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(activity, view, isConnected);
    }

    public static void showSnack(Activity activity, View view, boolean isConnected) {
        final Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        if (isConnected) {
            snackbar.dismiss();
            return;
        }

        View custom_view = activity.getLayoutInflater().inflate(R.layout.snackbar_light_button, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();

        snackBarView.setPadding(0, 0, 0, 0);
        TextView tv_msg = custom_view.findViewById(R.id.tv_msg);
        TextView tv_retry = custom_view.findViewById(R.id.tv_retry);
        tv_msg.setText(activity.getString(R.string.no_internet));
        tv_retry.setOnClickListener(v -> {
            CheckConnection(activity, view);
            snackbar.dismiss();
        });

        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

    @SuppressLint("WrongConstant")
    public static void setClipboard(Context context, String str) {
        ((android.content.ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copied Text", str));
        Toast.makeText(context, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidPhoneNumber(String str) {
        if (str.trim().equals("") || str.length() <= 10) {
            return false;
        }
        return Patterns.PHONE.matcher(str).matches();
    }

    @SuppressLint("WrongConstant")
    public static void sendPhotosInWhatsApp(Activity activity, ArrayList<Uri> arrayList, String str, String str2, Boolean bool) {
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        intent.putExtra("jid", str + "@s.whatsapp.net");
        intent.setType("image/gif");
        intent.setPackage(bool ? "com.whatsapp.w4b" : "com.whatsapp");
        if (str2 != null) {
            intent.putExtra("android.intent.extra.TEXT", str2);
        }
        intent.setFlags(335544320);
        if (intent.resolveActivityInfo(activity.getPackageManager(), 0) != null) {
            activity.startActivity(intent);
        } else {
            showToast(activity, "WhatsApp or WA Business is not installed");
        }
    }

    public static void openWhatsAppConversation(Context context, String str, String str2) {
        String replace = str.replace(" ", "").replace("+", "");
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", str2);
            intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            intent.putExtra("jid", PhoneNumberUtils.stripSeparators(replace) + "@s.whatsapp.net");
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public static void shareDirecctToSingleWhatsAppUser(Activity activity, String str, String str2, boolean z) {
        String replace = str.replace("+", "").replace(" ", "");
        PackageManager packageManager = activity.getPackageManager();
        Intent intent = new Intent("android.intent.action.VIEW");
        try {
            String str3 = "https://api.whatsapp.com/send?phone=" + replace + "&text=" + URLEncoder.encode(str2, "UTF-8");
            intent.setPackage(z ? "com.whatsapp.w4b" : "com.whatsapp");
            intent.setData(Uri.parse(str3));
            if (intent.resolveActivity(packageManager) != null) {
                activity.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareToMultipleWhatsAppUser(Activity activity, String str, boolean z) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.setPackage(z ? "com.whatsapp.w4b" : "com.whatsapp");
        intent.putExtra("android.intent.extra.TEXT", str);
        if (intent.resolveActivity(activity.getPackageManager()) == null) {
            Toast.makeText(activity, "Whatsapp not installed.", Toast.LENGTH_SHORT).show();
        } else {
            activity.startActivity(intent);
        }
    }

    public static void showToast(Context context, String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            if (Build.VERSION.SDK_INT == 25) {
                Toast.makeText(context, (CharSequence) str, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ignored) {
        }
    }

    public static String getBack(String str, String str2) {
        Matcher matcher = Pattern.compile(str2).matcher(str);
        return matcher.find() ? matcher.group(1) : "";
    }

    public static boolean download(Context context, String str) {
        return copyFileInSavedDir(context, str);
    }

    public static boolean copyFileInSavedDir(Context context, String str) {
        String str2;
        if (isVideoFile(context, str)) {
            str2 = getDir(context, "Videos").getAbsolutePath();
        } else {
            str2 = getDir(context, "Images").getAbsolutePath();
        }
        Uri fromFile = Uri.fromFile(new File(str2 + File.separator + new File(str).getName()));
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
            OutputStream openOutputStream = context.getContentResolver().openOutputStream(fromFile, "w");
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read > 0) {
                    openOutputStream.write(bArr, 0, read);
                } else {
                    openInputStream.close();
                    openOutputStream.flush();
                    openOutputStream.close();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(fromFile);
                    context.sendBroadcast(intent);
                    Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    static File getDir(Context context, String str) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + context.getResources().getString(R.string.app_directory) + File.separator + str);
        file.mkdirs();
        return file;
    }

    public static boolean isVideoFile(Context context, String str) {
        if (str.startsWith("content")) {
            String type = DocumentFile.fromSingleUri(context, Uri.parse(str)).getType();
            if (type == null || !type.startsWith("video")) {
                return false;
            }
            return true;
        }
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(str);
        if (guessContentTypeFromName == null || !guessContentTypeFromName.startsWith("video")) {
            return false;
        }
        return true;
    }
}