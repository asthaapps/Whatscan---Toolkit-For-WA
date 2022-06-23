package com.whatscan.toolkit.forwa.BulkSender.helper;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.whatscan.toolkit.forwa.R;

import org.jetbrains.annotations.NotNull;

public final class SingleEditTextDialog {
    public static final SingleEditTextDialog INSTANCE = new SingleEditTextDialog();

    private SingleEditTextDialog() {
    }

    public final void show(@NotNull Context context, @NotNull String str, @NotNull String str2, @NotNull String str3, @NotNull SingleEditTextDialogInterface singleEditTextDialogInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        EditText editText = new EditText(context);
        editText.setHint(str2);
        editText.setInputType(16384);
        int dimension = (int) context.getResources().getDimension(R.dimen.margin_16);
        editText.setPadding(dimension, dimension, dimension, dimension);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(dimension, dimension, dimension, dimension);
        editText.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(str3)) {
            editText.setText(str3);
        }
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        builder.setTitle(str).setPositiveButton(context.getString(R.string.save), (dialog, which) -> {
            if (editText.getText() != null) {
                if (editText.getText().toString().length() > 0) {
                    singleEditTextDialogInterface.onSaveClicked(editText.getText().toString());
                    return;
                }
            }
            singleEditTextDialogInterface.onSaveClicked(editText.getText().toString());
        }).setNegativeButton(R.string.cancel, (dialog, which) -> singleEditTextDialogInterface.onCancelClicked()).show();
    }

    public interface SingleEditTextDialogInterface {
        void onCancelClicked();

        void onSaveClicked(@NotNull String str);
    }
}