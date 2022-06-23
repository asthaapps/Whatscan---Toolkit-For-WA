package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.BulkSender.helper.BulkBaseTools;
import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.AutomaticSendRecord;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.AutomaticSenderDao;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ContactDao;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDatabase;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedContactsDao;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedFile;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.NewContactGroupDao;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.AutoSendingService;
import com.whatscan.toolkit.forwa.Service.WhatsappAccessibility;
import com.whatscan.toolkit.forwa.Util.AnalyticsReport;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.ProgressDialogUtils;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

public class BulkActivitySendingMessage extends AppCompatActivity {
    public final HashMap<String, Boolean> ContactMap = new HashMap<>();
    public Intent notificationIntent;
    public long totalCount;
    public SwitchCompat swAntiBan;
    public LinearLayout llMainSnack;
    public RelativeLayout ic_include, rlFirst;
    public GroupModel groupModelList;
    public ImportedFile importedFileList;
    public ArrayList<Integer> selectContactList = new ArrayList<>();
    public ArrayList<Uri> filesPathUri = new ArrayList<>();
    public ImageView la_back;
    public Button btnPlusDelay, btnMinusDelay;
    public int DelaySec = 4, MinDelaySec, delayAdded, currentPosition;
    public String countryCode = "+91", strMessage, strMode;
    public boolean wpBusiness, isAntiBan, isResume, isStartSending;
    public MaterialButton btnStartSend, btnCancelSend;
    public TextView tv_toolbar, txtProgressView, txtDelay, txtMsg, txtContactCount, txtMode, txtAttachment, fileInfo, txtSecondCount, txtViewContact, progressInfo, messageInfo, txtModeInfo, addDelayInSending;

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivitySendingMessage.this);
        setContentView(R.layout.activity_sending_status);

        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        llMainSnack = findViewById(R.id.llMainSnack);
        ic_include = findViewById(R.id.ic_include);
        rlFirst = findViewById(R.id.rlFirst);
        btnStartSend = findViewById(R.id.btnStartSend);
        btnCancelSend = findViewById(R.id.btnCancelSend);
        txtViewContact = findViewById(R.id.txtViewContact);
        txtProgressView = findViewById(R.id.txtProgressView);
        txtDelay = findViewById(R.id.txtDelay);
        swAntiBan = findViewById(R.id.swAntiBan);
        btnPlusDelay = findViewById(R.id.btnPlus);
        btnMinusDelay = findViewById(R.id.btnMinus);
        txtMsg = findViewById(R.id.txtMsg);
        txtContactCount = findViewById(R.id.txtContactCount);
        txtMode = findViewById(R.id.txtMode);
        txtAttachment = findViewById(R.id.txtAttachment);
        fileInfo = findViewById(R.id.fileInfo);
        txtSecondCount = findViewById(R.id.txtSecondCount);
        progressInfo = findViewById(R.id.progressInfo);
        messageInfo = findViewById(R.id.messageInfo);
        txtModeInfo = findViewById(R.id.txtModeInfo);
        addDelayInSending = findViewById(R.id.addDelayInSending);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            llMainSnack.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            rlFirst.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            swAntiBan.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            messageInfo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            fileInfo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtContactCount.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtModeInfo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            progressInfo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            addDelayInSending.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSecondCount.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            swAntiBan.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtProgressView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtMsg.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtAttachment.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtViewContact.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtMode.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        } else {
            setStatusBar();
            llMainSnack.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_color));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_color));
            rlFirst.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            swAntiBan.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            messageInfo.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            fileInfo.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtContactCount.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtModeInfo.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            progressInfo.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            addDelayInSending.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            swAntiBan.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        setIntentValues(getIntent());

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.bulk_sending_status) + "</small>"));
        txtContactCount.setText(Html.fromHtml(getString(R.string.total_selected_contact) + " " + totalCount));
        txtMode.setText(strMode);
        txtMsg.setText(strMessage);

        boolean z = true;
        swAntiBan.setChecked(isAntiBan);

        la_back.setOnClickListener(v -> onBackPressed());

        btnCancelSend.setOnClickListener(v -> showCancelDialog(BulkActivitySendingMessage.this, SpannedString.valueOf(getString(R.string.cancel_send_msg))));

        swAntiBan.setOnClickListener(v -> {
            if (Preference.getActive_PkeyM().equals("true")) {
                AntiBanDialog();
                swAntiBan.setChecked(true);
            } else {
                Constant.BottomSheetDialogPremium(BulkActivitySendingMessage.this, "Upgrade to Premium, Master Plan", "Enable Anti Ban  is available under Premium, Master Plan, upgrade to enable it.");
                swAntiBan.setChecked(false);
            }
        });

        btnStartSend.setOnClickListener(v -> {
            isResume = false;
            if (isStartSending) {
                isStartSending = false;
                WhatsappAccessibility.setStartTheService(false);
                AutoSendingService.startTheService(false);
            } else {
                isStartSending = true;
                String str = strMode;
                Event strMode = Event.MANUAL;
                WhatsappAccessibility.setStartTheService(!StringsKt.equals(str, strMode.name(), false));
                AutoSendingService.startTheService(!StringsKt.equals(str, strMode.name(), false));
                MessageSendWhatsApp();
            }
            ShowNotification();
            SendingButtonControl();
        });

        txtViewContact.setOnClickListener(v -> {
            Intent intent = new Intent(BulkActivitySendingMessage.this, BulkActivityContactView.class);
            intent.putExtra(Event.COUNTRY_CODE.name(), getCountryCode());
            intent.putExtra(Event.MESSAGE.name(), getMessage());
            intent.putExtra(Event.SEND_MODE.name(), strMode);
            intent.getIntExtra(Event.SENDING_POSITION.name(), currentPosition);
            intent.putExtra(Event.IS_WHATSAPP_BUSINESS.name(), wpBusiness());
            startActivity(intent);
        });

        int i4 = 0;
        if (filesPathUri != null && !filesPathUri.isEmpty()) {
            z = false;
        }
        if (z) {
            i4 = 8;
        }
        txtAttachment.setVisibility(i4);
        fileInfo.setVisibility(i4);
        txtAttachment.setOnClickListener(v -> startActivity(new Intent(BulkActivitySendingMessage.this, BulkActivityAttachment.class).putParcelableArrayListExtra(Event.BULK_SENDING_FILE_ATTACHMENT.name(), filesPathUri), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        AnalyticsReport.setScreen(this, "BulkActivitySendingMessage", null);

        if (isStartSending) {
            isResume = false;
            MessageSendWhatsApp();
        }

        ProgressUpdate();
        SendingButtonControl();
        setAdvanceControl();
        DelayText();
    }

    private void setIntentValues(Intent intent) {
        if (intent == null || intent.getBooleanExtra(Event.IS_CANCEL_SENDING.name(), false)) {
            btnCancelSend();
            return;
        }
        strMessage = intent.getStringExtra(Event.MESSAGE.name());
        wpBusiness = intent.getBooleanExtra(Event.IS_WHATSAPP_BUSINESS.name(), false);
        countryCode = intent.getStringExtra(Event.COUNTRY_CODE.name());
        if (countryCode == null) {
            countryCode = "";
        }

        strMode = intent.getStringExtra(Event.SEND_MODE.name());
        currentPosition = intent.getIntExtra(Event.SENDING_POSITION.name(), 0);
        isStartSending = intent.getBooleanExtra(Event.SENDING_STATUS.name(), false);
        isAntiBan = intent.getBooleanExtra(Event.ENABLE_ANTI_BAN.name(), false);

        Serializable serializableExtra = intent.getSerializableExtra(Event.SELECTED_GROUP.name());
        if (serializableExtra != null) {
            groupModelList = (GroupModel) serializableExtra;
        }

        Serializable serializableExtra2 = intent.getSerializableExtra(Event.SELECTED_IMPORT.name());
        if (serializableExtra2 != null) {
            importedFileList = (ImportedFile) serializableExtra2;
        }

        ArrayList<? extends Uri> parcelableArrayListExtra = intent.getParcelableArrayListExtra(Event.BULK_SENDING_FILE_ATTACHMENT.name());
        if (parcelableArrayListExtra != null) {
            if (filesPathUri != null) {
                Boolean.valueOf(filesPathUri.addAll(parcelableArrayListExtra));
            }
        }

        totalCount = intent.getLongExtra(Event.TOTAL_SELECTED_CONTACT.name(), 0);

        FetchSelectedContacts();
        notificationIntent = NotificationIntent();
        ShowNotification();
        SendingButtonControl();
    }

    public final void btnCancelSend() {
        isStartSending = false;
        dismissProgressNotification();
        Utils.showToast(this, "Sending Canceled");
        finish();
    }

    public final boolean checkEndOfSendingMessage() {
        if (((long) currentPosition) < totalCount) {
            return false;
        }
        isStartSending = false;

        btnStartSend.setVisibility(View.GONE);
        btnCancelSend.setVisibility(View.GONE);

        txtProgressView.setText(getString(R.string.send_msg_complete));
        txtProgressView.setTextColor(getResources().getColor(R.color.colorTools));
        dismissProgressNotification();
        return true;
    }

    private void createDefaultChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            getManager().createNotificationChannel(new NotificationChannel("sendingControl", "Bulk Sending Control", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    private void dismissProgressNotification() {
        getManager().cancel(10);
    }

    @SuppressLint("CheckResult")
    private void FetchSelectedContacts() {
        Integer id;
        Long importedFileId;
        selectContactList.clear();
        ProgressDialogUtils.displayProgress(this, "loading selected contacts...");
        GroupDatabase groupDatabase = getGroupDatabase();
        Integer num;

        (Objects.requireNonNull(groupDatabase != null ? groupDatabase.selectedContactsDao() : null)).getAllContactsId().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::onContactFetched, throwable -> ProgressDialogUtils.stopProgressDisplay());

        if (importedFileList != null) {
            if (importedFileList.getImportedFileId() != null) {
                ProgressDialogUtils.displayProgress(this, "loading Imported contacts...");
                ImportedContactsDao importedContactsDao = getGroupDatabase().importedContactsDao();
                importedContactsDao.getContactsIdsFromImportId((importedFileList == null || (importedFileId = importedFileList.getImportedFileId()) == null) ? 0 : importedFileId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::onContactFetched, throwable -> ProgressDialogUtils.stopProgressDisplay());
            }
        }

        if (groupModelList != null) {
            num = groupModelList.getId();
            if (num != null) {
                ProgressDialogUtils.displayProgress(this, "loading Groups contacts...");
                NewContactGroupDao newContactGroupDao = getGroupDatabase().getNewContactGroupDao();
                newContactGroupDao.getContactsIdsFromGroupId((groupModelList == null || (id = groupModelList.getId()) == null) ? 0 : id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::onContactFetched, throwable -> ProgressDialogUtils.stopProgressDisplay());
            }
        }
    }

    @SuppressLint({"NewApi", "CheckResult"})
    public final void MessageSendWhatsApp() {
        int i;
        try {
            if (!checkEndOfSendingMessage()) {
                int i2 = currentPosition;
                boolean isChecked = swAntiBan.isChecked();
                long j = 30;
                if (isResume) {
                    if (isChecked) {
                        final long random1 = new java.util.Random().nextInt(147);
                        final long random2 = new java.util.Random().nextInt(37);
                        final long random3 = new java.util.Random().nextInt(29);
                        final long random4 = new java.util.Random().nextInt(23);
                        j = ((delayAdded + random1 * random2)) + random3 + random4;
                    } else {
                        final long random1 = new java.util.Random().nextInt(147);
                        final long random2 = new java.util.Random().nextInt(37);
                        final long random3 = new java.util.Random().nextInt(29);
                        final long random4 = new java.util.Random().nextInt(23);
                        j = ((delayAdded + random1 * random2)) + random3 + random4;
                    }
                }
                if (j > ((long) 30)) {
                    try {
                        i = Math.toIntExact(j / ((long) 1000));
                    } catch (Exception unused) {
                        i = 0;
                    }
                    txtDelay.setText(Html.fromHtml(i + " sec delay added in sending"));
                    txtDelay.setVisibility(View.VISIBLE);
                } else {
                    txtDelay.setText("");
                    txtDelay.setVisibility(View.INVISIBLE);
                }

                if (i2 < selectContactList.size()) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        GroupDatabase groupDatabase;
                        ContactDao contactDao;
                        Single<List<ContactModel>> subscribeOn;
                        Single<List<ContactModel>> observeOn;
                        if (!(checkEndOfSendingMessage()) && (groupDatabase = getGroupDatabase()) != null && (contactDao = groupDatabase.getContactDao()) != null) {
                            Integer num = getSelectedContactModelList().get(i2);
                            Single<List<ContactModel>> contactFromIds = contactDao.getContactFromIds(num);
                            if ((subscribeOn = contactFromIds.subscribeOn(Schedulers.io())) != null && (observeOn = subscribeOn.observeOn(AndroidSchedulers.mainThread())) != null) {
                                observeOn.subscribe(new Consumer<List<? extends ContactModel>>() {
                                    public final void accept(List<? extends ContactModel> list) {
                                        String str;
                                        String str2;
                                        String str3;
                                        String str4;
                                        String str5;
                                        String str6;
                                        String str7;
                                        String str8;
                                        Boolean bool = null;
                                        if (!list.isEmpty()) {
                                            ContactModel contactModel = list.get(0);
                                            String strMessage = getMessage();
                                            String name = contactModel.getName();
                                            if ((name.length() > 0) || Utils.isValidPhoneNumber(contactModel.getName())) {
                                                String replace$default = strMessage != null ? StringsKt.replace(strMessage, Constant.FULL_NAME_CODE, getDefaultName(), false) : null;
                                                String replace$default2 = replace$default != null ? StringsKt.replace(replace$default, Constant.FIRST_NAME_CODE, getDefaultName(), false) : null;
                                                if (replace$default2 != null) {
                                                    str = StringsKt.replace(replace$default2, Constant.LAST_NAME_CODE, getDefaultName(), false);
                                                    String phoneNumber = contactModel.getPhoneNumber();
                                                    str2 = getPhoneNumber(phoneNumber, getCountryCode());
                                                    currentPosition = currentPosition + 1;
                                                    if (!(ContactMap != null ? ContactMap.containsKey(str2) : null)) {
                                                        try {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("number", str2);
                                                            bundle.putString("strMessage", str);
                                                            AnalyticsReport.addEvent(BulkActivitySendingMessage.this, Event.BulkMessageSent.name(), bundle);
                                                        } catch (Exception unused) {
                                                            unused.printStackTrace();
                                                        }

                                                        if (filesPathUri != null) {
                                                            bool = filesPathUri.isEmpty();
                                                        }
                                                        if (bool) {
                                                            Utils.shareDirecctToSingleWhatsAppUser(BulkActivitySendingMessage.this, str2, str, wpBusiness());
                                                        } else {
                                                            Utils.sendPhotosInWhatsApp(BulkActivitySendingMessage.this, filesPathUri, str2, str, wpBusiness());
                                                        }

                                                        ContactMap.put(str2, Boolean.TRUE);
                                                        AutomaticSendRecord automaticSendRecord = new AutomaticSendRecord(null, null, null, null, null, null, 0);
                                                        automaticSendRecord.setMessage(str);
                                                        automaticSendRecord.setPhoneNumber(str2);
                                                        automaticSendRecord.setSendThrough(wpBusiness() ? "WA Business" : "WhatsApp");
                                                        automaticSendRecord.setTimestamp(System.currentTimeMillis());
                                                        automaticSendRecord.setUserPlan(BulkBaseTools.isAutoForwardingEnabled() ? "paid" : "free");
                                                        automaticSendRecord.setSendMode(strMode);
                                                        recordAutomaticSendData(automaticSendRecord);
                                                        return;
                                                    }
                                                    currentPosition = currentPosition + 1;
                                                    MessageSendWhatsApp();
                                                    return;
                                                }
                                            } else {
                                                String name2 = contactModel.getName();
                                                List split = name2 != null ? new Regex("\\s+").split(name2, 0) : null;
                                                if (strMessage != null) {
                                                    String name3 = contactModel.getName();
                                                    str3 = StringsKt.replace(strMessage, Constant.FULL_NAME_CODE, name3, false);
                                                } else {
                                                    str3 = null;
                                                }
                                                if (str3 != null) {
                                                    if (split == null || (str8 = (String) CollectionsKt.firstOrNull(split)) == null) {
                                                        str7 = "";
                                                    } else {
                                                        str7 = str8;
                                                    }
                                                    str4 = StringsKt.replace(str3, Constant.FIRST_NAME_CODE, str7, false);
                                                } else {
                                                    str4 = null;
                                                }
                                                if (str4 != null) {
                                                    if (split == null || (str6 = (String) CollectionsKt.lastOrNull(split)) == null) {
                                                        str5 = "";
                                                    } else {
                                                        str5 = str6;
                                                    }
                                                    str = StringsKt.replace(str4, Constant.LAST_NAME_CODE, str5, false);
                                                    String phoneNumber2 = contactModel.getPhoneNumber();
                                                    str2 = getPhoneNumber(phoneNumber2, getCountryCode());
                                                    currentPosition = currentPosition + 1;
                                                    if (!(ContactMap != null ? ContactMap.containsKey(str2) : null)) {
                                                    }
                                                }
                                            }
                                            str = null;
                                            String phoneNumber22 = contactModel.getPhoneNumber();
                                            str2 = getPhoneNumber(phoneNumber22, getCountryCode());
                                            currentPosition = currentPosition + 1;
                                            if (!(ContactMap != null ? ContactMap.containsKey(str2) : null)) {
                                            }
                                        } else {
                                            currentPosition = currentPosition + 1;
                                            MessageSendWhatsApp();
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    public final void accept(Throwable th) {
                                        Utils.showToast(BulkActivitySendingMessage.this, "Something went wrong, Please try again");
                                    }
                                });
                            }
                        }
                    }, j);
                }
                ShowNotification();
            }
        } catch (Exception e) {
            currentPosition++;
            Utils.showToast(this, "Failed in sending! Trying to send to next Contact");
            MessageSendWhatsApp();
        }
    }

    public final String getDefaultName() {
        return Preference.getSavedString(BulkActivitySendingMessage.this, Event.DEFAULT_NAME.name(), "Dear");
    }

    public final GroupDatabase getGroupDatabase() {
        return GroupDatabase.getInstance(BulkActivitySendingMessage.this);
    }

    private NotificationManager getManager() {
        @SuppressLint("WrongConstant") Object systemService = getSystemService("notification");
        if (systemService != null) {
            return (NotificationManager) systemService;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.app.NotificationManager");
    }

    public final String getPhoneNumber(String str, String str2) {
        String str3;
        String str4;
        int length = str.length() - 1;
        int i = 0;
        boolean z = false;
        while (i <= length) {
            boolean z2 = str.charAt(!z ? i : length) <= ' ';
            if (!z) {
                if (!z2) {
                    z = true;
                } else {
                    i++;
                }
            } else if (!z2) {
                break;
            } else {
                length--;
            }
        }
        String replace = new Regex("-").replace(new Regex("  ").replace(new Regex(" ").replace(str.subSequence(i, length + 1).toString(), ""), ""), "");
        if (!(StringsKt.startsWith(replace, str2, false))) {
            if (Intrinsics.areEqual("+91", str2)) {
                String str5 = StringsKt.replace(replace, "+", "", false);
                if (str5.length() <= 10 || !(StringsKt.startsWith(str5, "0", false))) {
                    str4 = str5;
                } else {
                    str4 = str5.substring(1);
                }
                if (str4.length() == 10) {
                    replace = "+91" + str4;
                } else {
                    str3 = str4;
                    return StringsKt.replace(str3, "+", "", false);
                }
            } else {
                replace = str2 + replace;
            }
        }
        str3 = replace;
        return StringsKt.replace(str3, "+", "", false);
    }

    public final void onContactFetched(List<Integer> list) {
        if (!(list == null || list.isEmpty())) {
            if (selectContactList != null) {
                selectContactList.addAll(list);
            }
            totalCount = selectContactList.size();
        }
        ProgressDialogUtils.stopProgressDisplay();
    }

    private Intent NotificationIntent() {
        Intent intent = new Intent(this, BulkActivitySendingMessage.class);
        intent.putExtra(Event.COUNTRY_CODE.name(), countryCode);
        intent.putExtra(Event.MESSAGE.name(), strMessage);
        intent.putExtra(Event.SEND_MODE.name(), strMode);
        intent.putExtra(Event.IS_WHATSAPP_BUSINESS.name(), wpBusiness);
        return intent;
    }

    public final void recordAutomaticSendData(AutomaticSendRecord automaticSendRecord) {
        Completable.fromRunnable(() -> {
            AutomaticSenderDao automaticSenderDao;
            GroupDatabase groupDatabase = getGroupDatabase();
            if (groupDatabase != null && (automaticSenderDao = groupDatabase.getAutomaticSenderDao()) != null) {
                automaticSenderDao.insert(automaticSendRecord);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private void setAdvanceControl() {
        btnPlusDelay.setOnClickListener(v -> {
            if (getDelayAdded() < DelaySec) {
                setDelayAdded(getDelayAdded() + 1);
                DelayText();
                return;
            }
            Utils.showToast(BulkActivitySendingMessage.this, "Max delay reached");
        });

        btnMinusDelay.setOnClickListener(v -> {
            if (getDelayAdded() > MinDelaySec) {
                setDelayAdded(getDelayAdded() - 1);
                DelayText();
                return;
            }
            Utils.showToast(BulkActivitySendingMessage.this, "Min delay reached");
        });
    }

    public final void showCancelDialog(Activity activity, Spanned spanned) {
        if (!activity.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(spanned);
            builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> btnCancelSend());
            builder.setNeutralButton(getString(R.string.cancel), (dialog, which) -> {

            });
            builder.show();
        }
    }

    public final void AntiBanDialog() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.enti_ban)).setMessage(Html.fromHtml("We have added some technique to stop the ban from WhatsApp for bulk messaging. <br>Enable it to avoid the ban from WhatsApp.<br><br><small><font color='#666'>We have added all the technique but WhatsApp can add/implement any new methods to ban user so we don't promise 100% ban-free Gurantee.</font></small>")).setPositiveButton("Enable", (dialog, which) -> swAntiBan.setChecked(true)).setNegativeButton("Close", (dialog, which) -> swAntiBan.setChecked(false)).setCancelable(false).show();
    }

    public final void ShowNotification() {
        int i;
        boolean z;
        String str;
        if (!this.selectContactList.isEmpty()) {
            long j = this.totalCount;
            if (j > 0) {
                i = (int) (((long) (this.currentPosition * 100)) / j);
                if (this.notificationIntent == null) {
                    this.notificationIntent = NotificationIntent();
                }

                notificationIntent.putExtra(Event.SENDING_POSITION.name(), this.currentPosition);

                if (notificationIntent != null) {
                    notificationIntent.putExtra(Event.SENDING_STATUS.name(), !this.isStartSending);
                }

                if (notificationIntent != null) {
                    String name = Event.ENABLE_ANTI_BAN.name();
                    notificationIntent.putExtra(name, swAntiBan.isChecked());
                }
                createDefaultChannel();
                @SuppressLint("WrongConstant") PendingIntent activity = PendingIntent.getActivity(BulkActivitySendingMessage.this, 6321, this.notificationIntent, 134217728);
                Intent intent4 = new Intent(BulkActivitySendingMessage.this, BulkActivitySendingMessage.class);
                intent4.putExtra(Event.IS_CANCEL_SENDING.name(), true);
                @SuppressLint("WrongConstant") PendingIntent activity2 = PendingIntent.getActivity(BulkActivitySendingMessage.this, 345, intent4, 268435456);
                NotificationCompat.Builder smallIcon = new NotificationCompat.Builder(BulkActivitySendingMessage.this, "sendingControl").setSmallIcon(R.drawable.logo);
                NotificationCompat.Builder contentTitle = smallIcon.setContentTitle(progressInfo.getText());
                NotificationCompat.Builder autoCancel = contentTitle.setContentText(this.currentPosition + " / " + this.totalCount).setProgress(100, i, false).setOngoing(true).setPriority(1).setCategory(NotificationCompat.CATEGORY_CALL).setAutoCancel(true);
                long[] jArr = new long[1];
                for (int i2 = 0; i2 < 1; i2++) {
                    jArr[i2] = 0;
                }
                NotificationCompat.Builder onlyAlertOnce = autoCancel.setVibrate(jArr).setOnlyAlertOnce(true);
                z = this.isStartSending;
                int i3 = !z ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_forward_white_24dp;
                if (!z) {
                    str = "Pause";
                } else {
                    str = this.currentPosition == 0 ? "Start" : "Resume";
                }
                getManager().notify(10, onlyAlertOnce.addAction(i3, str, activity).addAction(R.drawable.ic_close, "Cancel", activity2).setColorized(true).setColor(getResources().getColor(R.color.colorTools)).build());
            }
        }
        i = 0;
        createDefaultChannel();
        @SuppressLint("WrongConstant") PendingIntent activity3 = PendingIntent.getActivity(BulkActivitySendingMessage.this, 6321, this.notificationIntent, 134217728);
        Intent intent42 = new Intent(BulkActivitySendingMessage.this, BulkActivitySendingMessage.class);
        intent42.putExtra(Event.IS_CANCEL_SENDING.name(), true);
        @SuppressLint("WrongConstant") PendingIntent activity22 = PendingIntent.getActivity(BulkActivitySendingMessage.this, 345, intent42, 268435456);
        NotificationCompat.Builder smallIcon2 = new NotificationCompat.Builder(BulkActivitySendingMessage.this, "sendingControl").setSmallIcon(R.drawable.logo);
        NotificationCompat.Builder contentTitle2 = smallIcon2.setContentTitle(progressInfo.getText());
        NotificationCompat.Builder autoCancel2 = contentTitle2.setContentText(this.currentPosition + " / " + this.totalCount).setProgress(100, i, false).setOngoing(true).setPriority(1).setCategory(NotificationCompat.CATEGORY_CALL).setAutoCancel(true);
        long[] jArr = new long[1];
        for (int i2 = 0; i2 < 1; i2++) {
            jArr[i2] = 0;
        }
        NotificationCompat.Builder onlyAlertOnce2 = autoCancel2.setVibrate(jArr).setOnlyAlertOnce(true);
        z = this.isStartSending;
        int i3 = !z ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_forward_white_24dp;
        if (!z) {
            str = "Pause";
        } else {
            str = this.currentPosition == 0 ? "Start" : "Resume";
        }
        getManager().notify(10, onlyAlertOnce2.addAction(i3, str, activity3).addAction(R.drawable.ic_close, "Cancel", activity22).setColorized(true).setColor(getResources().getColor(R.color.colorTools)).build());
    }

    public final void DelayText() {
        txtSecondCount.setText(Html.fromHtml(delayAdded + " <small>sec</small>"));
    }

    private void ProgressUpdate() {
        if (((long) currentPosition) < totalCount) {
            txtProgressView.setText(Html.fromHtml(currentPosition + "<small><small> / " + totalCount + " sent</small></small>"));
            return;
        }
        txtProgressView.setText(getString(R.string.send_msg_complete));
        txtProgressView.setTextColor(getResources().getColor(R.color.colorTools));
    }

    public final void SendingButtonControl() {
        btnStartSend.setText(isStartSending ? "Pause sending" : currentPosition == 0 ? getString(R.string.start_forward_to_all) : "Resume sending");
        btnStartSend.setIcon(ContextCompat.getDrawable(getApplication(), isStartSending ? R.drawable.ic_baseline_pause_24 : R.drawable.ic_forward_all));

        StringBuilder sb = new StringBuilder();
        String obj = btnStartSend.getText().toString();
        if (obj != null) {
            sb.append(StringsKt.trim(obj).toString());
            sb.append("Clicked");
            AnalyticsReport.addEvent(BulkActivitySendingMessage.this, sb.toString(), null);
            txtDelay.setVisibility(View.VISIBLE);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public final String getCountryCode() {
        return countryCode;
    }

    public final void setCountryCode(@NotNull String str) {
        countryCode = str;
    }

    public final int getDelayAdded() {
        return delayAdded;
    }

    public final void setDelayAdded(int i) {
        delayAdded = i;
    }

    public final String getMessage() {
        return strMessage;
    }

    public final void setMessage(@Nullable String str) {
        strMessage = str;
    }

    public final ArrayList<Integer> getSelectedContactModelList() {
        return selectContactList;
    }

    public final void setSelectedContactModelList(@NotNull ArrayList<Integer> arrayList) {
        selectContactList = arrayList;
    }

    public final boolean wpBusiness() {
        return wpBusiness;
    }

    public final void setBusiness(boolean z) {
        wpBusiness = z;
    }

    @Override
    public void onBackPressed() {
        if (((long) currentPosition) < totalCount - 1) {
            Snackbar.make(llMainSnack, "Sending not completed! Are you sure to go back?", BaseTransientBottomBar.LENGTH_LONG).setAction("Go Back", v -> finish()).show();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isStartSending = false;
        if (((long) currentPosition) < totalCount - 1) {
            ShowNotification();
        }
    }

    @Override
    public void onNewIntent(@Nullable Intent intent) {
        super.onNewIntent(intent);
        setIntentValues(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isStartSending) {
            isResume = true;
            MessageSendWhatsApp();
            ProgressUpdate();
        }
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 23) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.bg_color));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.bg_color));
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