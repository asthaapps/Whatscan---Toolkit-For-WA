package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDatabase;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedFile;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.ProgressDialogUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public class BulkActivityContactGroupImportContact extends AppCompatActivity {
    public final String[] Permissions = {"android.permission.READ_CALL_LOG", "android.permission.READ_CONTACTS"};
    public int contactsCount;
    public long totalCount;
    public GroupModel groupModelList;
    public ImportedFile importedFileList;
    public ImageView la_back;
    public LinearLayout llContactSelect, llGroupSelect, llImportContactSelect, llContactSelection;
    public TextView tv_toolbar, txtSelectContact, txtImportContact, txtImportGroup, txtTotalSelect;

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityContactGroupImportContact.this);
        setContentView(R.layout.activity_contact_group_import);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(BulkActivityContactGroupImportContact.this, ll_banner);

        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.contact_group_imports) + "<s/mall>"));

        txtSelectContact = findViewById(R.id.txtSelectContact);
        txtImportContact = findViewById(R.id.txtImportContact);
        txtImportGroup = findViewById(R.id.txtImportGroup);
        txtTotalSelect = findViewById(R.id.txtTotalSelect);
        llContactSelection = findViewById(R.id.llContactSelection);
        llContactSelect = findViewById(R.id.llContactSelect);
        llGroupSelect = findViewById(R.id.llGroupSelect);
        llImportContactSelect = findViewById(R.id.llImportContactSelect);

        la_back.setOnClickListener(v -> onBackPressed());

        llContactSelect.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermission(Permissions, 725);
            } else {
                IntentContactSelection();
            }
        });

        llGroupSelect.setOnClickListener(v -> {
            if (Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") ||
                    Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true") || Preference.getActive_offer().equals("true")|| Preference.getActive_vip().equals("true")) {
                startActivityForResult(new Intent(BulkActivityContactGroupImportContact.this, BulkActivityAddGroup.class), 131);
            } else {
                Constant.BottomSheetDialogPremium(BulkActivityContactGroupImportContact.this, "Upgrade to Classic, Essential, Premium, Master Plan", "WhatsApp Create New GroupModel is available under Classic, Essential, Premium and Master plan, upgrade to enable it.");
            }
        });

        llImportContactSelect.setOnClickListener(v -> {
            if (Preference.getActive_MkeyM().equals("true") ||Preference.getActive_MkeyY().equals("true") || Preference.getImport_one_day().equals("true") || Preference.getImport_seven_day().equals("true")) {
                startActivityForResult(new Intent(BulkActivityContactGroupImportContact.this, BulkActivityImportedFiles.class), 1316);
            } else {
                Constant.BottomSheetDialogPremium(BulkActivityContactGroupImportContact.this, "Upgrade to Master Plan", "Imported Contact is available under Master plan, upgrade to enable it.");
            }
        });

        llContactSelection.setOnClickListener(v -> SelectContacts());

        findViewById(R.id.llReset).setOnClickListener(v -> showResetDialog());

        FetchSelectedContacts();
    }

    @TargetApi(23)
    public final void checkPermission(String[] strArr, int i) {
        boolean z = true;
        if (ContextCompat.checkSelfPermission(this, strArr[1]) == 0) {
            z = false;
        }
        if (z) {
            requestPermissions(strArr, i);
        } else {
            IntentContactSelection();
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NotNull String[] strArr, @NotNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (strArr.length > 1 && Intrinsics.areEqual(strArr[1], "android.permission.READ_CONTACTS")) {
            if (iArr.length <= 1 || iArr[1] != 0) {
                checkPermission(Permissions, 725);
            } else {
                IntentContactSelection();
            }
        }
    }

    public final GroupDatabase getGroupDatabase() {
        return GroupDatabase.getInstance(BulkActivityContactGroupImportContact.this);
    }

    private void IntentContactSelection() {
        Intent intent = new Intent(BulkActivityContactGroupImportContact.this, BulkActivityContactShowList.class);
        intent.putExtra(Event.REQUEST_NEW_SELECTION.name(), true);
        startActivityForResult(intent, 123);
    }

    public final void showResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BulkActivityContactGroupImportContact.this);
        builder.setTitle("Reset Contacts?");
        builder.setMessage("Do you want to reset selected contacts?");
        builder.setPositiveButton("Reset", (dialog, which) -> resetContacts());
        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @SuppressLint("CheckResult")
    public final void resetContacts() {
        ProgressDialogUtils.displayProgress(BulkActivityContactGroupImportContact.this, "Resetting selection...");
        Completable.fromCallable(() -> {
            getGroupDatabase().selectedContactsDao().deleteAll();
            Preference.saveIntData(BulkActivityContactGroupImportContact.this, Event.SELECTED_GROUP.name(), -1);
            Preference.saveLongData(BulkActivityContactGroupImportContact.this, Event.SELECTED_IMPORT.name(), -1);
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            importedFileList = null;
            groupModelList = null;
            contactsCount = 0;
            totalCount = 0;

            updateSelectedText(txtSelectContact, 0);
            updateSelectedText(txtImportGroup, 0);
            updateSelectedText(txtImportContact, 0);
            ProgressDialogUtils.stopProgressDisplay();
        });
    }

    @SuppressLint("CheckResult")
    private void FetchSelectedContacts() {
        ProgressDialogUtils.displayProgress(BulkActivityContactGroupImportContact.this, "loading selected contacts...");
        GroupDatabase groupDatabase = getGroupDatabase();
        (Objects.requireNonNull(groupDatabase != null ? groupDatabase.selectedContactsDao() : null)).getAllContactsId().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {
            totalCount = totalCount + ((long) list.size());
            contactsCount = list.size();
            ProgressDialogUtils.stopProgressDisplay();
            updateSelectedText(txtSelectContact, contactsCount);
        }, throwable -> ProgressDialogUtils.stopProgressDisplay());

        if (Preference.getLongString(BulkActivityContactGroupImportContact.this, Event.SELECTED_IMPORT.name(), 0) > 0) {
            ProgressDialogUtils.displayProgress(BulkActivityContactGroupImportContact.this, "Loading imported contacts...");
            getGroupDatabase().importedFileDao().getImportedFile(Preference.getLongString(BulkActivityContactGroupImportContact.this, Event.SELECTED_IMPORT.name(), 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(importedFile -> {
                importedFileList = importedFile;
                Long count = importedFile.getCount();
                totalCount = totalCount + (count != null ? count : 0);
                ProgressDialogUtils.stopProgressDisplay();
                Long count2 = importedFile.getCount();
                updateSelectedText(txtImportContact, count2 != null ? (int) count2.longValue() : 0);
            }, throwable -> ProgressDialogUtils.stopProgressDisplay());
        }

        if (Preference.getIntString(BulkActivityContactGroupImportContact.this, Event.SELECTED_GROUP.name(), 0) > 0) {
            ProgressDialogUtils.displayProgress(BulkActivityContactGroupImportContact.this, "Loading group contacts...");
            getGroupDatabase().getGroupDao().getGroup(Preference.getIntString(BulkActivityContactGroupImportContact.this, Event.SELECTED_GROUP.name(), 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(group -> {
                groupModelList = group;
                Integer count = group.getCount();
                int i = 0;
                totalCount = totalCount + ((long) (count != null ? count : 0));
                ProgressDialogUtils.stopProgressDisplay();
                Integer count2 = group.getCount();
                if (count2 != null) {
                    i = count2;
                }
                updateSelectedText(txtImportGroup, i);
            }, throwable -> ProgressDialogUtils.stopProgressDisplay());
        }
    }

    public final void SelectContacts() {
        Long count;
        Integer count2;
        Intent intent = new Intent();
        intent.putExtra(Event.SELECTED_CONTACTS_COUNT.name(), contactsCount);

        if (groupModelList != null) {
            if (((groupModelList == null || (count2 = groupModelList.getCount()) == null) ? 0 : count2) > 0) {
                intent.putExtra(Event.SELECTED_GROUP.name(), groupModelList);
            }
        }

        if (importedFileList != null) {
            if (((importedFileList == null || (count = importedFileList.getCount()) == null) ? 0 : count) > 0) {
                intent.putExtra(Event.SELECTED_IMPORT.name(), importedFileList);
            }
        }
        intent.putExtra(Event.TOTAL_SELECTED_CONTACT.name(), totalCount);
        setResult(-1, intent);
        finish();
    }

    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        Long count;
        Long importedFileId;
        Integer count2;
        Integer id;
        Integer num = null;
        Serializable serializable = null;

        Serializable serializable2 = null;
        int i3 = 0;
        if (i == 123 && -1 == i2) {
            if (intent != null) {
                num = intent.getIntExtra(Event.SELECTED_CONTACTS_COUNT.name(), 0);
            }
            if (num != null) {
                i3 = num;
            }
            contactsCount = i3;
            updateSelectedText(txtSelectContact, contactsCount);
        } else if (i == 131 && -1 == i2) {
            if (intent != null) {
                serializable2 = intent.getSerializableExtra(Event.SELECTED_GROUP.name());
            }
            if (serializable2 != null) {
                groupModelList = (GroupModel) serializable2;
                if (groupModelList != null) {
                    String name = Event.SELECTED_GROUP.name();
                    Preference.saveIntData(BulkActivityContactGroupImportContact.this, name, (groupModelList == null || (id = groupModelList.getId()) == null) ? 0 : id);
                    if (!(groupModelList == null || (count2 = groupModelList.getCount()) == null)) {
                        i3 = count2;
                    }
                    updateSelectedText(txtImportGroup, i3);
                    return;
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupModel");
        } else if (i == 1316 && -1 == i2) {
            if (intent != null) {
                serializable = intent.getSerializableExtra(Event.SELECTED_IMPORT.name());
            }
            if (serializable != null) {
                importedFileList = (ImportedFile) serializable;
                if (importedFileList != null) {
                    String name2 = Event.SELECTED_IMPORT.name();
                    Preference.saveLongData(BulkActivityContactGroupImportContact.this, name2, (importedFileList == null || (importedFileId = importedFileList.getImportedFileId()) == null) ? 0 : importedFileId);
                    if (!(importedFileList == null || (count = importedFileList.getCount()) == null)) {
                        i3 = (int) count.longValue();
                    }
                    updateSelectedText(txtImportContact, i3);
                    return;
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedFile");
        }
    }

    public final void updateSelectedText(TextView textView, int i) {
        Long count;
        Integer count2;
        textView.setText(String.valueOf(i));
        int intValue = (groupModelList == null || (count2 = groupModelList.getCount()) == null) ? 0 : count2;
        totalCount = ((long) (contactsCount + intValue)) + ((importedFileList == null || (count = importedFileList.getCount()) == null) ? 0 : count);
        txtTotalSelect.setText(Html.fromHtml(getString(R.string.total_selected) + " : " + "<strong>" + totalCount + "</strong>"));
    }

    @Override
    public void onBackPressed() {
        SelectContacts();
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
}