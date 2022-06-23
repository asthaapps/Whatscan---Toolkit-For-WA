package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
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
import com.whatscan.toolkit.forwa.DataBaseHelper.db.DatabaseHandler;
import com.whatscan.toolkit.forwa.GetSet.QuickReply;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

public class BulkActivityNewMessage extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public Boolean msgSaved = false;
    public Button doneComMsg;
    public EditText edComMsg;
    public ImageView la_back;
    public RelativeLayout rlNewMsg, ic_include;
    public LinearLayout relativeLayout;
    public ImageView imgBold, imgItalic, imgStrike, imgSave, imgCopy, imgPaste;
    public TextView txtFirstName, txtFullName, txtLastName, tv_toolbar, txtDelete;
    public String bold = "*<strong></strong>*", strike = "~<strike></strike>~", italic = "_<i></i>_", firstName = "<strong> {FirstName} </strong>", lastName = "<strong> {lastName} </strong>", fullName = "<strong> {FullName} </strong>";

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityNewMessage.this);
        setContentView(R.layout.activity_new_message);

        Utils.CheckConnection(BulkActivityNewMessage.this, findViewById(R.id.rlNewMsg));

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(BulkActivityNewMessage.this, ll_banner);

        edComMsg = findViewById(R.id.edComMsg);
        txtFullName = findViewById(R.id.txtFullName);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtDelete = findViewById(R.id.txtDelete);
        imgBold = findViewById(R.id.imgBold);
        imgItalic = findViewById(R.id.imgItalic);
        imgStrike = findViewById(R.id.imgStrike);
        doneComMsg = findViewById(R.id.doneComMsg);
        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        imgSave = findViewById(R.id.imgSave);
        imgCopy = findViewById(R.id.imgCopy);
        imgPaste = findViewById(R.id.imgPaste);
        rlNewMsg = findViewById(R.id.rlNewMsg);
        ic_include = findViewById(R.id.ic_include);
        relativeLayout = findViewById(R.id.relativeLayout);
        View view1 = findViewById(R.id.view1);
        TextView txtOne = findViewById(R.id.txtOne);
        TextView txtTwo = findViewById(R.id.txtTwo);
        TextView txtThree = findViewById(R.id.txtThree);
        TextView txtFour = findViewById(R.id.txtFour);
        TextView txtFive = findViewById(R.id.txtFive);
        TextView txtSix = findViewById(R.id.txtSix);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.add_msg) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlNewMsg.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            edComMsg.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            edComMsg.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFullName.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            txtFirstName.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            txtLastName.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            txtFullName.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFirstName.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtLastName.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            view1.setVisibility(View.INVISIBLE);
        } else {
            setStatusBar();
            rlNewMsg.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            relativeLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFullName.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_color));
            txtFirstName.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_color));
            txtLastName.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_color));
            txtFullName.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtFirstName.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtLastName.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        String stringExtra = getIntent().getStringExtra(Event.MESSAGE.name());
        if (stringExtra != null && stringExtra.length() > 0) {
            edComMsg.setText(stringExtra);
            edComMsg.setSelection(stringExtra.length());
        }
        edComMsg.requestFocus();

        imgSave.setOnClickListener(v -> {
            if (!msgSaved) {
                SaveReplyMessage();
            } else {
                Utils.showToast(BulkActivityNewMessage.this, "Already Saved!");
            }
        });

        imgCopy.setOnClickListener(v -> Utils.setClipboard(BulkActivityNewMessage.this, edComMsg.getText().toString()));

        imgPaste.setOnClickListener(v -> {
            @SuppressLint("WrongConstant") ClipboardManager clipboardManager = (ClipboardManager) getSystemService("clipboard");
            if (!(clipboardManager == null || clipboardManager.getPrimaryClip() == null || clipboardManager.getPrimaryClip().getItemCount() <= 0)) {
                if (TextUtils.isEmpty(edComMsg.getText())) {
                    edComMsg.setText(clipboardManager.getPrimaryClip().getItemAt(0).getText().toString());
                } else {
                    edComMsg.setText(edComMsg.getText() + clipboardManager.getPrimaryClip().getItemAt(0).getText().toString());
                }
                edComMsg.setSelection(edComMsg.length());
            }
        });

        txtDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.delete_msg);
            builder.setPositiveButton(R.string.delete, (dialogInterface, i) -> edComMsg.setText(""));
            builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        });

        txtFullName.setOnClickListener(view -> setNamesText(fullName));

        txtFirstName.setOnClickListener(view -> setNamesText(firstName));

        txtLastName.setOnClickListener(view -> setNamesText(lastName));

        imgBold.setOnClickListener(view -> setFormatText(bold));

        imgItalic.setOnClickListener(view -> setFormatText(italic));

        imgStrike.setOnClickListener(view -> setFormatText(strike));

        doneComMsg.setOnClickListener(view -> sentMessage());

        la_back.setOnClickListener(v -> onBackPressed());
    }

    private void SaveReplyMessage() {
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        if (TextUtils.isEmpty(edComMsg.getText().toString())) {
            Toast.makeText(BulkActivityNewMessage.this, "Message is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        msgSaved = Boolean.TRUE;
        QuickReply quickReply = new QuickReply();
        quickReply.setMessage(edComMsg.getText().toString());
        databaseHandler.addQuickReply(quickReply);
        invalidateOptionsMenu();
        showSavedDialog();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.imgSave).setIcon(msgSaved ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_empty);
        return super.onPrepareOptionsMenu(menu);
    }

    public void showSavedDialog() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.save_msg_sucess)).setMessage(getString(R.string.save_msg_sucess_desc)).setPositiveButton(getString(R.string.open_save_msg), (dialogInterface, i) -> startActivity(new Intent(BulkActivityNewMessage.this, BulkActivitySavedMessages.class))).setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
        }).show();
    }

    private void sentMessage() {
        Intent intent = new Intent();
        intent.putExtra(Event.MESSAGE.name(), edComMsg.getText().toString());
        Preference.saveStringData(BulkActivityNewMessage.this, Event.LAST_ENTER_MESSAGE.name(), edComMsg.getText().toString());
        setResult(-1, intent);
        finish();
    }

    private void setFormatText(String str) {
        EditText editText = edComMsg;
        editText.setText(Html.fromHtml(edComMsg.getText() + str));
        EditText editText2 = edComMsg;
        editText2.setSelection(editText2.length() + -1);
    }

    private void setNamesText(String str) {
        EditText editText = edComMsg;
        editText.setText(Html.fromHtml(edComMsg.getText() + str));
        EditText editText2 = edComMsg;
        editText2.setSelection(editText2.length());
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(BulkActivityNewMessage.this, findViewById(R.id.rlNewMsg), isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onBackPressed() {
        sentMessage();
        super.onBackPressed();
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