package com.whatscan.toolkit.forwa.BulkSender.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.whatscan.toolkit.forwa.BulkSender.BulkActivityCountrySelection;
import com.whatscan.toolkit.forwa.BulkSender.BulkActivitySavedMessages;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.DatabaseHandler;
import com.whatscan.toolkit.forwa.GetSet.QuickReply;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BottomSheetQuickReply extends BottomSheetDialogFragment {
    public static BulkActivitySavedMessages.OnQuickReplyAdded onQuickReplyAdded;
    public boolean e = false;
    public QuickReply quickReplyList;
    public Activity activity;
    public RelativeLayout rlQuickReply;
    public TextView title, txtCountryCode, txtSave, txtCancel;
    public TextInputLayout text_input1, text_input2, text_input3;
    public TextInputEditText hashTagEditText, contactnameEditText, messageEditText;

    public static BottomSheetQuickReply newInstance(BulkActivitySavedMessages.OnQuickReplyAdded onQuickReply, QuickReply quickReply) {
        Bundle bundle = new Bundle();
        onQuickReplyAdded = onQuickReply;
        bundle.putParcelable(Event.QUICK_REPLY.name(), quickReply);
        BottomSheetQuickReply bottomSheetQuickReply = new BottomSheetQuickReply();
        bottomSheetQuickReply.setArguments(bundle);
        return bottomSheetQuickReply;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog dialog = super.onCreateDialog(bundle);
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return dialog;
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dailog_save, viewGroup, false);

        rlQuickReply = inflate.findViewById(R.id.rlQuickReply);
        text_input1 = inflate.findViewById(R.id.text_input1);
        text_input2 = inflate.findViewById(R.id.text_input2);
        text_input3 = inflate.findViewById(R.id.text_input3);
        hashTagEditText = inflate.findViewById(R.id.hashTagEditText);
        contactnameEditText = inflate.findViewById(R.id.contactnameEditText);
        messageEditText = inflate.findViewById(R.id.messageEditText);
        title = inflate.findViewById(R.id.title);
        txtCountryCode = inflate.findViewById(R.id.country_code_text_view);
        txtSave = inflate.findViewById(R.id.save_button);
        txtCancel = inflate.findViewById(R.id.cancel_button);

        if (Preference.getBooleanTheme(false)) {
            rlQuickReply.setBackgroundColor(ContextCompat.getColor(activity, R.color.darkBlack));
            title.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            txtCountryCode.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            hashTagEditText.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            contactnameEditText.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            messageEditText.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
            setTextInputLayoutHintColor(text_input1, activity, R.color.colorWhite);
            setTextInputLayoutHintColor(text_input2, activity, R.color.colorWhite);
            setTextInputLayoutHintColor(text_input3, activity, R.color.colorWhite);
        } else {
            rlQuickReply.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorWhite));
            title.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack));
            txtCountryCode.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack));
            hashTagEditText.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack));
            contactnameEditText.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack));
            messageEditText.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack));
            setTextInputLayoutHintColor(text_input1, activity, R.color.colorBlack);
            setTextInputLayoutHintColor(text_input2, activity, R.color.colorBlack);
            setTextInputLayoutHintColor(text_input3, activity, R.color.colorBlack);
        }

        messageEditText.requestFocus();

        txtCountryCode.setText(Preference.getSavedString(getContext(), Event.COUNTRY_CODE.toString(), "+91"));

        quickReplyList = requireArguments().getParcelable(Event.QUICK_REPLY.name());
        if (quickReplyList == null || quickReplyList.getPhoneNumber() == null || quickReplyList.getMessage() == null) {
            quickReplyList = new QuickReply();
        } else {
            e = true;
            hashTagEditText.setText(quickReplyList.getPhoneNumber().replace(txtCountryCode.getText().toString(), ""));
            messageEditText.setText(quickReplyList.getMessage());
            contactnameEditText.setText(quickReplyList.getContactName());
        }

        txtCountryCode.setOnClickListener(view -> startActivityForResult(new Intent(activity, BulkActivityCountrySelection.class), 1));

        txtSave.setOnClickListener(view -> {
            DatabaseHandler databaseHandler = new DatabaseHandler(activity);
            if (TextUtils.isEmpty(Objects.requireNonNull(messageEditText.getText()).toString())) {
                Toast.makeText(activity, "Message is Empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(activity, "Saved!", Toast.LENGTH_SHORT).show();
            quickReplyList.setPhoneNumber(txtCountryCode.getText().toString() + Objects.requireNonNull(hashTagEditText.getText()).toString());
            quickReplyList.setMessage(messageEditText.getText().toString());
            quickReplyList.setContactName(Objects.requireNonNull(contactnameEditText.getText()).toString());
            if (!e) {
                databaseHandler.addQuickReply(quickReplyList);
            } else {
                databaseHandler.updateHashtag(quickReplyList);
            }
            BottomSheetQuickReply.onQuickReplyAdded.onAdded(quickReplyList);
            dismiss();
        });

        txtCancel.setOnClickListener(view -> dismiss());
        return inflate;
    }

    private void setTextInputLayoutHintColor(TextInputLayout text_input1, Context context, int colorBlack) {
        text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, colorBlack)));
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            if (intent != null && i2 == 100) {
                Preference.saveStringData(activity, Event.COUNTRY_CODE.toString(), intent.getStringExtra("code"));
                txtCountryCode.setText(intent.getStringExtra("code"));
            }
        } else if (i != 12 && i == 1345 && i2 == -1) {
            Cursor managedQuery = getActivity().managedQuery(intent.getData(), null, null, null, null);
            if (managedQuery.moveToFirst()) {
                String string = managedQuery.getString(managedQuery.getColumnIndexOrThrow("_id"));
                if (managedQuery.getString(managedQuery.getColumnIndex("has_phone_number")).equalsIgnoreCase("1")) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Cursor query = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = " + string, null, null);
                    query.moveToFirst();
                    String string2 = query.getString(query.getColumnIndex("data1"));
                    String string3 = query.getString(query.getColumnIndex("display_name"));
                    if (!TextUtils.isEmpty(string2)) {
                        hashTagEditText.setText(string2.replace(txtCountryCode.getText().toString(), ""));
                    }
                    if (!TextUtils.isEmpty(string3)) {
                        contactnameEditText.setText(string3);
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
}