package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDatabase;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedFile;
import com.whatscan.toolkit.forwa.Other.CommonWebView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.AutoSendingService;
import com.whatscan.toolkit.forwa.Service.WhatsappAccessibility;
import com.whatscan.toolkit.forwa.User.ActivityLogin;
import com.whatscan.toolkit.forwa.Util.AccessibilityPermission;
import com.whatscan.toolkit.forwa.Util.AnalyticsReport;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.ProgressDialogUtils;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.TypeCastException;

public class BulkActivityStart extends AppCompatActivity {

    public int selectedContactsCount;
    public long totalCount;
    public GroupModel groupModelList;
    public ImportedFile importedList;
    public RadioButton rbWpBusines;
    public RadioGroup radioGroupSend;
    public MaterialButton btnNext, btnReset;
    public ImageView la_back;
    public String strSendMode;
    public RelativeLayout rlBulkMain, rlMsgEdt;
    public View ic_include;
    public ImageView imgDropDown;
    public CardView cardOne, cardTwo, cardThree, card_contact;
    public LinearLayout llCountryCode, llContactSelect, llGroupSelect, llSavedMsg, llContact, llGroups, llImport, llImportContactSelect, llFiles;
    public TextView tv_toolbar, txtCountryCode, txtSelectedContact, txtViewContact, txtEnterMsg, txtFilesCount, txtSelectContact, txtImportGroup, txtImportContact;
    public ArrayList<Uri> FilePathUri = new ArrayList<>();
    ImageView llAttachFile;
    TextView txtGroup, txtImpCsv, txtContact;

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityStart.this);
        setContentView(R.layout.activity_bulk_start);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(BulkActivityStart.this, ll_banner);

        strSendMode = Event.MANUAL.name();

        llContactSelect = findViewById(R.id.llContactSelect);
        txtContact = findViewById(R.id.txtContact);
        llGroupSelect = findViewById(R.id.llGroupSelect);
        txtGroup = findViewById(R.id.txtGroup);
        txtImpCsv = findViewById(R.id.txtImpCsv);
        llCountryCode = findViewById(R.id.llCountryCode);
        llAttachFile = findViewById(R.id.llAttachFile);
        llSavedMsg = findViewById(R.id.llSavedMsg);
        llContact = findViewById(R.id.llContact);
        llGroups = findViewById(R.id.llGroups);
        rlMsgEdt = findViewById(R.id.rlMsgEdt);
        llImport = findViewById(R.id.llImport);
        llImportContactSelect = findViewById(R.id.llImportContactSelect);
        llFiles = findViewById(R.id.llFiles);
        txtCountryCode = findViewById(R.id.txtCountryCode);
        txtEnterMsg = findViewById(R.id.txtEnterMsg);
        txtFilesCount = findViewById(R.id.txtFilesCount);
        txtSelectedContact = findViewById(R.id.txtSelectedContact);
        txtViewContact = findViewById(R.id.txtViewContact);
        txtSelectContact = findViewById(R.id.txtSelectContact);
        txtImportGroup = findViewById(R.id.txtImportGroup);
        txtImportContact = findViewById(R.id.txtImportContact);
        rbWpBusines = findViewById(R.id.rbWpBusines);
        radioGroupSend = findViewById(R.id.radioGroupSend);
        btnNext = findViewById(R.id.btnNext);
        btnReset = findViewById(R.id.btnReset);
        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        rlBulkMain = findViewById(R.id.rlBulkMain);
        ic_include = findViewById(R.id.ic_include);
        imgDropDown = findViewById(R.id.imgDropDown);
        cardOne = findViewById(R.id.cardOne);
        card_contact = findViewById(R.id.card_contact);
        cardTwo = findViewById(R.id.cardTwo);
        cardThree = findViewById(R.id.cardThree);
        TextView txtOne = findViewById(R.id.txtOne);
        TextView txtTwo = findViewById(R.id.txtTwo);
        TextView txtThree = findViewById(R.id.txtThree);
        TextView txtFour = findViewById(R.id.txtFour);

        tv_toolbar.setText(Html.fromHtml("<small>" + getResources().getString(R.string.bulk_automatic_sender) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlBulkMain.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            llContactSelect.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_black_dark));
            rlMsgEdt.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_black_dark));
            cardOne.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            card_contact.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            cardTwo.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            cardThree.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            llGroupSelect.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_black_dark));
            llImportContactSelect.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_black_dark));
            llCountryCode.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));
            imgDropDown.setImageResource(R.drawable.ic_drop_down_white);
            txtCountryCode.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSelectedContact.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSelectContact.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtImportGroup.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtImportContact.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtGroup.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtContact.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFilesCount.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtImpCsv.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtEnterMsg.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtEnterMsg.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            ((RadioButton) findViewById(R.id.rbWp)).setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            ((RadioButton) findViewById(R.id.rbWpBusines)).setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rlBulkMain.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_color_1));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            llContactSelect.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_light_bulk));
            cardOne.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            card_contact.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            llGroupSelect.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_light_bulk));
            rlMsgEdt.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_light_bulk));
            llImportContactSelect.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_light_bulk));
            cardTwo.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            cardThree.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            llCountryCode.setBackground(ContextCompat.getDrawable(this, R.drawable.boder));
            imgDropDown.setImageResource(R.drawable.ic_drop_down_black);
            txtCountryCode.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtImpCsv.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtSelectedContact.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtSelectContact.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtContact.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtImportGroup.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtImportContact.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtFilesCount.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtGroup.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtEnterMsg.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtEnterMsg.setHintTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            ((RadioButton) findViewById(R.id.rbWp)).setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            ((RadioButton) findViewById(R.id.rbWpBusines)).setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        boolean z = true;

        llCountryCode.setVisibility(View.VISIBLE);
        llAttachFile.setVisibility(View.VISIBLE);
        txtCountryCode.setText(Preference.getSavedString(this, Event.COUNTRY_CODE.toString(), "+91"));

        String string = Preference.getSavedString(BulkActivityStart.this, Event.LAST_ENTER_MESSAGE.name(), "");
        if (string.length() <= 0) {
            z = false;
        }
        if (z) {
            txtEnterMsg.setText(string);
        }

        la_back.setOnClickListener(v -> onBackPressed());
        llCountryCode.setOnClickListener(v -> startActivityForResult(new Intent(BulkActivityStart.this, BulkActivityCountrySelection.class), 1));
        llSavedMsg.setOnClickListener(v -> startActivityForResult(new Intent(BulkActivityStart.this, BulkActivitySavedMessages.class).putExtra(Event.SELECT_QUICK_REPLY.name(), true), 10098));
        llAttachFile.setOnClickListener(v -> startActivityForResult(new Intent(BulkActivityStart.this, BulkActivityAttachment.class).putParcelableArrayListExtra(Event.BULK_SENDING_FILE_ATTACHMENT.name(), FilePathUri), 786));

        llContactSelect.setOnClickListener(v -> {
            Intent intent = new Intent(BulkActivityStart.this, BulkActivityContactShowList.class);
            intent.putExtra(Event.REQUEST_NEW_SELECTION.name(), true);
            startActivityForResult(intent, 123);
        });

        llGroupSelect.setOnClickListener(v -> {
            if (Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") ||
                    Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true") || Preference.getActive_offer().equals("true") || Preference.getActive_vip().equals("true")) {
                startActivityForResult(new Intent(BulkActivityStart.this, BulkActivityAddGroup.class), 131);
            } else {
                Constant.BottomSheetDialogPremium(BulkActivityStart.this, "Upgrade to Classic, Essential, Premium, Master Plan", "WhatsApp Create New GroupModel is available under Classic, Essential, Premium and Master plan, upgrade to enable it.");
            }
        });

        llImportContactSelect.setOnClickListener(v -> {
            if (Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true") || Preference.getImport_one_day().equals("true") || Preference.getImport_seven_day().equals("true")) {
                startActivityForResult(new Intent(BulkActivityStart.this, BulkActivityImportedFiles.class), 1316);
            } else {
                Constant.BottomSheetDialogPremium(BulkActivityStart.this, "Upgrade to Master Plan", "Imported Contact is available under Master plan, upgrade to enable it.");
            }
        });

        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                if (TextUtils.isEmpty(txtEnterMsg.getText().toString()) && FilePathUri.size() <= 0) {
                    Utils.showToast(BulkActivityStart.this, "Message is empty!");
                } else if (totalCount > 0) {
                    showForwardingWayDialog();
                } else {
                    Utils.showToast(BulkActivityStart.this, "Please select contacts to sent your message!");
                }
            });
        }

        btnReset.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BulkActivityStart.this);
            builder.setTitle("Reset Contacts?");
            builder.setMessage("Do you want to reset selected contacts?");
            builder.setPositiveButton("Reset", (dialog, which) -> resetContacts());
            builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

        txtViewContact.setOnClickListener(v -> {
            if (totalCount > 0) {
                Intent intent = new Intent(BulkActivityStart.this, BulkActivityContactView.class);
                intent.putExtra(Event.COUNTRY_CODE.name(), txtCountryCode.getText().toString());
                intent.putExtra(Event.MESSAGE.name(), txtEnterMsg.getText().toString());
                intent.putExtra(Event.SEND_MODE.name(), strSendMode);
                intent.getIntExtra(Event.SENDING_POSITION.name(), 0);
                intent.putExtra(Event.IS_WHATSAPP_BUSINESS.name(), rbWpBusines.isChecked());
                startActivity(intent);
                return;
            }
            Utils.showToast(BulkActivityStart.this, "No Contact Selected");
        });

        txtEnterMsg.setOnClickListener(v -> {
            Intent intent = new Intent(BulkActivityStart.this, BulkActivityNewMessage.class);
            intent.putExtra(Event.MESSAGE.name(), txtEnterMsg.getText().toString());
            startActivityForResult(intent, 14682);
        });

        radioGroupSend.setOnCheckedChangeListener((group, checkedId) -> Preference.saveBooleanData(BulkActivityStart.this, Event.IS_WA_BUSINESS_SELECTED_FOR_MESSAGE.toString(), checkedId == R.id.rbWpBusines));
        radioGroupSend.check(Preference.getSavedBoolean(BulkActivityStart.this, Event.IS_WA_BUSINESS_SELECTED_FOR_MESSAGE.toString(), false) ? R.id.rbWpBusines : R.id.rbWp);

        FetchSelectedContacts();
    }

    @SuppressLint("CheckResult")
    public final void resetContacts() {
        ProgressDialogUtils.displayProgress(BulkActivityStart.this, "Resetting selection...");
        Completable.fromCallable(() -> {
            getGroupDatabase().selectedContactsDao().deleteAll();
            Preference.saveIntData(BulkActivityStart.this, Event.SELECTED_GROUP.name(), -1);
            Preference.saveLongData(BulkActivityStart.this, Event.SELECTED_IMPORT.name(), -1);
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            importedList = null;
            groupModelList = null;
            selectedContactsCount = 0;
            totalCount = 0;

            updateSelectedTextContact(txtSelectContact, 0);
            updateSelectedTextGroup(txtImportGroup, 0);
            updateSelectedTextImport(txtImportContact, 0);
            ProgressDialogUtils.stopProgressDisplay();
        });
    }

    private GroupDatabase getGroupDatabase() {
        return GroupDatabase.getInstance(BulkActivityStart.this);
    }

    @SuppressLint("CheckResult")
    private void FetchSelectedContacts() {
        totalCount = 0;
        selectedContactsCount = 0;
        ProgressDialogUtils.displayProgress(BulkActivityStart.this, "loading selected contacts...");
        GroupDatabase groupDatabase = getGroupDatabase();
        (Objects.requireNonNull(groupDatabase != null ? groupDatabase.selectedContactsDao() : null)).getAllContactsId().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(integers -> {
            totalCount = totalCount + ((long) integers.size());

            selectedContactsCount = integers.size();
            ContactListFetched();
        }, throwable -> ContactListFetched());

        if (Preference.getLongString(BulkActivityStart.this, Event.SELECTED_IMPORT.name(), 0) > 0) {
            ProgressDialogUtils.displayProgress(BulkActivityStart.this, "Loading imported contacts...");
            getGroupDatabase().importedFileDao().getImportedFile(Preference.getLongString(BulkActivityStart.this, Event.SELECTED_IMPORT.name(), 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(importedFile -> {
                importedList = importedFile;
                long j = totalCount;
                Long count = importedFile.getCount();
                totalCount = j + (count != null ? count : 0);
                ContactListFetched();
            }, throwable -> ContactListFetched());
        }

        if (Preference.getIntString(BulkActivityStart.this, Event.SELECTED_GROUP.name(), 0) > 0) {
            ProgressDialogUtils.displayProgress(BulkActivityStart.this, "Loading groupModel contacts...");
            getGroupDatabase().getGroupDao().getGroup(Preference.getIntString(BulkActivityStart.this, Event.SELECTED_GROUP.name(), 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(group -> {
                groupModelList = group;
                long j = totalCount;
                Integer count = group.getCount();
                totalCount = j + ((long) (count != null ? count : 0));
                ContactListFetched();
            }, throwable -> ContactListFetched());
        }
    }

    public final void ContactListFetched() {
        ProgressDialogUtils.stopProgressDisplay();
        txtSelectedContact.setText(Html.fromHtml("<strong>" + getString(R.string.total_selected) + " : " + totalCount + "</strong>"));
    }

    private void forwardMessageToWhatsApp() {
        Advertisement.getInstance(BulkActivityStart.this).showFullAds(this::SendMsgIntent);
    }

    private void SendMsgIntent() {
        Long count;
        Integer count2;
        Intent intent = new Intent(BulkActivityStart.this, BulkActivitySendingMessage.class);
        intent.putExtra(Event.COUNTRY_CODE.name(), txtCountryCode.getText().toString());
        intent.putExtra(Event.MESSAGE.name(), txtEnterMsg.getText().toString());
        intent.putExtra(Event.SEND_MODE.name(), strSendMode);
        String name = Event.IS_WHATSAPP_BUSINESS.name();
        intent.putExtra(name, rbWpBusines.isChecked());

        if (groupModelList != null) {
            if (((count2 = groupModelList.getCount()) == null ? 0 : count2) > 0) {
                intent.putExtra(Event.SELECTED_GROUP.name(), groupModelList);
            }
        }

        if (importedList != null) {
            if (((count = importedList.getCount()) == null ? 0 : count) > 0) {
                intent.putExtra(Event.SELECTED_IMPORT.name(), importedList);
            }
        }
        intent.putParcelableArrayListExtra(Event.BULK_SENDING_FILE_ATTACHMENT.name(), FilePathUri);
        intent.putExtra(Event.TOTAL_SELECTED_CONTACT.name(), totalCount);
        startActivity(intent);
    }

    private void setAutoService(boolean z) {
        WhatsappAccessibility.setStartTheService(z);
        AutoSendingService.startTheService(z);
    }

    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        Integer id;
        Integer count2;
        Integer num = null;
        int i3 = 0;
        Serializable serializable2 = null;
        Serializable serializable = null;
        ArrayList<Uri> parcelableArrayListExtra;
        Long importedFileId;
        Long count;

        if (i == 1) {
            if (intent != null && i2 == 100) {
                Preference.saveStringData(this, Event.COUNTRY_CODE.toString(), intent.getStringExtra("code"));
                txtCountryCode.setText(intent.getStringExtra("code"));
            }
        } else if (i == 123 && -1 == i2) {
            if (intent != null) {
                num = intent.getIntExtra(Event.SELECTED_CONTACTS_COUNT.name(), 0);
            }
            if (num != null) {
                i3 = num;
            }
            selectedContactsCount = i3;
            updateSelectedTextContact(txtSelectContact, selectedContactsCount);

            btnNext.setAnimation(AnimationUtils.loadAnimation(BulkActivityStart.this, R.anim.scale_up));
            btnNext.setVisibility(View.VISIBLE);
            FetchSelectedContacts();
        } else if (i == 131 && -1 == i2) {
            btnNext.setAnimation(AnimationUtils.loadAnimation(BulkActivityStart.this, R.anim.scale_up));
            btnNext.setVisibility(View.VISIBLE);
            if (intent != null) {
                serializable2 = intent.getSerializableExtra(Event.SELECTED_GROUP.name());
            }
            if (serializable2 != null) {
                groupModelList = (GroupModel) serializable2;
                if (groupModelList != null) {
                    String name = Event.SELECTED_GROUP.name();
                    Preference.saveIntData(BulkActivityStart.this, name, (groupModelList == null || (id = groupModelList.getId()) == null) ? 0 : id);
                    if (!(groupModelList == null || (count2 = groupModelList.getCount()) == null)) {
                        i3 = count2;
                    }
                    updateSelectedTextGroup(txtImportGroup, i3);
                    return;
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupModel");
        } else if (i == 9876 && -1 == i2) {
            FilePathUri.clear();
            if (!(intent == null || (parcelableArrayListExtra = intent.getParcelableArrayListExtra(Event.BULK_SENDING_FILE_ATTACHMENT.name())) == null)) {
                FilePathUri.addAll(parcelableArrayListExtra);
                SelectedFileCount();
            }
        } else if (i == 7867) {
            forwardMessageToWhatsApp();
        } else if (i == 10098 && -1 == i2) {
            if (intent != null) {
                if (intent.getStringExtra(Event.MESSAGE.name()) != null) {
                    txtEnterMsg.setText(intent.getStringExtra(Event.MESSAGE.name()));
                }
            }
        } else if (i == 14682 && intent != null) {
            if (intent.getStringExtra(Event.MESSAGE.name()) != null) {
                txtEnterMsg.setText(intent.getStringExtra(Event.MESSAGE.name()));
            }
        } else if (i == 1316 && -1 == i2) {
            if (intent != null) {
                serializable = intent.getSerializableExtra(Event.SELECTED_IMPORT.name());
            }
            if (serializable != null) {
                importedList = (ImportedFile) serializable;
                if (importedList != null) {
                    String name2 = Event.SELECTED_IMPORT.name();
                    Preference.saveLongData(BulkActivityStart.this, name2, (importedList == null || (importedFileId = importedList.getImportedFileId()) == null) ? 0 : importedFileId);
                    if (!(importedList == null || (count = importedList.getCount()) == null)) {
                        i3 = (int) count.longValue();
                    }
                    updateSelectedTextImport(txtImportContact, i3);
                    return;
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedFile");
        }
        if (i != 786 || -1 != i2) {
            return;
        }

        FilePathUri.clear();
        if (!(intent == null)) {
            ArrayList<Uri> parcelableArrayListExtra2 = intent.getParcelableArrayListExtra(Event.BULK_SENDING_FILE_ATTACHMENT.name());
            if (parcelableArrayListExtra2 != null) {
                FilePathUri.addAll(parcelableArrayListExtra2);
                SelectedFileCount();
            }
        }
    }

    public final void updateSelectedTextContact(TextView textView, int i) {
        Long count;
        Integer count2;
        textView.setText(String.valueOf(i));
        int intValue = (groupModelList == null || (count2 = groupModelList.getCount()) == null) ? 0 : count2;
        totalCount = ((long) (selectedContactsCount + intValue)) + ((importedList == null || (count = importedList.getCount()) == null) ? 0 : count);
        llContact.setVisibility(View.VISIBLE);
        txtSelectedContact.setText(Html.fromHtml("<strong>" + getString(R.string.total_selected) + " : " + totalCount + "</strong>"));
    }

    public final void updateSelectedTextGroup(TextView textView, int i) {
        Long count;
        Integer count2;
        textView.setText(String.valueOf(i));
        int intValue = (groupModelList == null || (count2 = groupModelList.getCount()) == null) ? 0 : count2;
        totalCount = ((long) (selectedContactsCount + intValue)) + ((importedList == null || (count = importedList.getCount()) == null) ? 0 : count);
        llGroups.setVisibility(View.VISIBLE);
        txtSelectedContact.setText(Html.fromHtml("<strong>" + getString(R.string.total_selected) + " : " + totalCount + "</strong>"));
    }

    public final void updateSelectedTextImport(TextView textView, int i) {
        Long count;
        Integer count2;
        textView.setText(String.valueOf(i));
        int intValue = (groupModelList == null || (count2 = groupModelList.getCount()) == null) ? 0 : count2;
        totalCount = ((long) (selectedContactsCount + intValue)) + ((importedList == null || (count = importedList.getCount()) == null) ? 0 : count);
        llImport.setVisibility(View.VISIBLE);
        txtSelectedContact.setText(Html.fromHtml("<strong>" + getString(R.string.total_selected) + " : " + totalCount + "</strong>"));
    }

    private void SelectedFileCount() {
        llFiles.setVisibility(View.VISIBLE);
        txtFilesCount.setVisibility(FilePathUri.size() > 0 ? View.VISIBLE : View.GONE);
        txtFilesCount.setText(Html.fromHtml("<strong>" + FilePathUri.size() + "</strong>"));
    }

    private void showAccesibilityDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BulkActivityStart.this);
        View inflate = LayoutInflater.from(BulkActivityStart.this).inflate(R.layout.permission_discloser1, null);
        bottomSheetDialog.setContentView(inflate);
        AtomicInteger check = new AtomicInteger();
        Button bt_agree = inflate.findViewById(R.id.bt_agree);
        TextView bt_cancel = inflate.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(view13 -> bottomSheetDialog.dismiss());
        CheckBox checkBox_agree = inflate.findViewById(R.id.checkBox_agree);
        SpannableString SpanString = new SpannableString(
                "By signing in, you agree to the Terms of Use and Privacy Policy");

        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(BulkActivityStart.this, CommonWebView.class);
                mIntent.putExtra("tc", true);
                startActivity(mIntent);
            }
        };

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(BulkActivityStart.this, CommonWebView.class);
                mIntent.putExtra("pp", true);
                startActivity(mIntent);
            }
        };


        SpanString.setSpan(teremsAndCondition, 32, 45, 0);
        SpanString.setSpan(privacy, 49, 63, 0);
        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 32, 45, 0);
        SpanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorTools)), 49, 63, 0);

        checkBox_agree.setMovementMethod(LinkMovementMethod.getInstance());
        checkBox_agree.setText(SpanString, TextView.BufferType.SPANNABLE);
        checkBox_agree.setSelected(true);

        checkBox_agree.setOnClickListener(view12 -> {
            boolean checked = ((CheckBox) view12).isChecked();
            if (checked) {
                check.set(1);
            } else {
                check.set(0);
            }

        });

        bt_agree.setOnClickListener(view1 -> {
            if (check.get() == 0) {
                Toast.makeText(BulkActivityStart.this, "please agree conditions.", Toast.LENGTH_SHORT).show();
            } else if (check.get() == 1) {
                AnalyticsReport.addEvent(BulkActivityStart.this, Event.AccessibilityServiceActivityOpen.name(), null);
                startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    public final void showForwardingWayDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BulkActivityStart.this);
        View inflate = LayoutInflater.from(BulkActivityStart.this).inflate(R.layout.dailog_manual_automatic_sending, null);
        bottomSheetDialog.setContentView(inflate);

        LinearLayout rlSendMode = inflate.findViewById(R.id.rlSendMode);
        TextView txtOne = inflate.findViewById(R.id.txtOne);
        MaterialRadioButton btnManualForwarding = inflate.findViewById(R.id.btnManualForwarding);
        MaterialRadioButton btnAutomaticForwarding = inflate.findViewById(R.id.btnAutomaticForwarding);
        MaterialButton btnEnableAutomaticForwarding = inflate.findViewById(R.id.btnEnableAutomaticForwarding);

        if (Preference.getBooleanTheme(false)) {
            rlSendMode.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            btnManualForwarding.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            btnAutomaticForwarding.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            rlSendMode.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            btnManualForwarding.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            btnAutomaticForwarding.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        btnManualForwarding.setText(Html.fromHtml("<strong>" + getString(R.string.manual) + "</strong><br>" + "<small>" + getString(R.string.send_mode_one) + "<br>" + getString(R.string.send_mode_two) + "<br>" + getString(R.string.send_mode_three) + "</small"));
        btnAutomaticForwarding.setText(Html.fromHtml("<strong>" + getString(R.string.automatic) + "</strong><br>" + "<small>" + getString(R.string.auto_mode_one) + "</small>"));

        if (Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") || Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true")) {
            btnAutomaticForwarding.setEnabled(true);
            btnEnableAutomaticForwarding.setVisibility(View.GONE);
        } else {
            Constant.BottomSheetDialogPremium(BulkActivityStart.this, "Upgrade to Premium & Master Plan", "Automatic Sending is available under Premium and Master plan, upgrade to enable it.");
            btnEnableAutomaticForwarding.setVisibility(View.GONE);
        }


        inflate.findViewById(R.id.forward_material_button).setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                if (Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") || Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true")) {
                    if (btnManualForwarding.isChecked()) {
                        strSendMode = Event.MANUAL.name();
                        ManualForward(bottomSheetDialog);
                        bottomSheetDialog.dismiss();
                    } else if (btnAutomaticForwarding.isChecked()) {
                        strSendMode = Event.AUTO.name();
                        AutoForward(bottomSheetDialog);
                    } else {
                        Utils.showToast(BulkActivityStart.this, "Please choose one way to start");
                    }
                } else {
                    Constant.BottomSheetDialogPremium(BulkActivityStart.this, "Upgrade to Premium & Master Plan", "Automatic Sending is available under Premium and Master plan, upgrade to enable it.");
                }
                try {
                    AnalyticsReport.addEvent(BulkActivityStart.this, (btnManualForwarding.isChecked() ? Event.ManualForwarding : Event.AutomaticForwarding).name(), new Bundle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                startActivity(new Intent(BulkActivityStart.this, ActivityLogin.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            }
        });
        bottomSheetDialog.show();
    }

    public final void AutoForward(BottomSheetDialog bottomSheetDialog) {
        setAutoService(true);
        if (Preference.getPermission_accessbility().equals("true")) {
            bottomSheetDialog.dismiss();
            forwardMessageToWhatsApp();
        } else if (!AccessibilityPermission.isAccessibilityOn(BulkActivityStart.this, WhatsappAccessibility.class)) {
            showAccesibilityDialog();
        }
    }

    public final void ManualForward(BottomSheetDialog bottomSheetDialog) {
        setAutoService(false);
        if (totalCount > ((long) 50)) {
            bottomSheetDialog.dismiss();
            forwardMessageToWhatsApp();
            return;
        }
        forwardMessageToWhatsApp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setAutoService(false);
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