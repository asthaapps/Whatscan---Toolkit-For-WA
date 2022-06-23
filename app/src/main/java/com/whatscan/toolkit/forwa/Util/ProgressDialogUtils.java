package com.whatscan.toolkit.forwa.Util;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressDialogUtils {
    static ProgressDialog progressDialog;

    public static void displayProgress(Activity activity) {
        displayProgress(activity, "Please Wait...");
    }

    public static void stopProgressDisplay() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            progressDialog = null;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void displayProgress(Activity activity, String str) {
        if (activity != null) {
            try {
                if (!activity.isFinishing()) {
                    if (progressDialog != null) {
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage(str);
                        progressDialog.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}