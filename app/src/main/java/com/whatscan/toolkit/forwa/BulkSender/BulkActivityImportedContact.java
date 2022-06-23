package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.ContactForGroupSelectionAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;
import com.whatscan.toolkit.forwa.BulkSender.helper.DownlodExportUtil;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDatabase;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedContact;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedContactsDao;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedFile;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedFileDao;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.ProgressDialogUtils;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.TypeCastException;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;

public class BulkActivityImportedContact extends AppCompatActivity {
    public final ArrayList<ContactModel> selectedContactModelList = new ArrayList<>();
    public final ContactForGroupSelectionAdapter selectedContactAdapter = new ContactForGroupSelectionAdapter(this);
    public boolean isEditing;
    public ProgressBar progress_bar;
    public LinearLayout llInstruction;
    public EditText etGroupName;
    public ImageView imgSample;
    public ImageView la_back;
    public RecyclerView recycleGroupList;
    public CardView card_search;
    public RelativeLayout rlGroupList, ic_include;
    public TextView tv_toolbar, txtDelete, txtSave, txtCSVFileDownlod, txtAddContact, txtCountMember;
    public ImportedFile importedFile = new ImportedFile(null, null, null, null);

    @SuppressLint({"WrongConstant", "CheckResult"})
    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityImportedContact.this);
        setContentView(R.layout.activity_group_list);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(BulkActivityImportedContact.this, ll_banner);

        tv_toolbar = findViewById(R.id.tv_toolbar);
        recycleGroupList = findViewById(R.id.recycleGroupList);
        progress_bar = findViewById(R.id.progress_bar);
        llInstruction = findViewById(R.id.llInstruction);
        etGroupName = findViewById(R.id.etGroupName);
        imgSample = findViewById(R.id.imgSample);
        txtDelete = findViewById(R.id.txtDelete);
        txtSave = findViewById(R.id.txtSave);
        txtCSVFileDownlod = findViewById(R.id.txtCSVFileDownlod);
        txtAddContact = findViewById(R.id.txtAddContact);
        txtCountMember = findViewById(R.id.txtCountMember);
        la_back = findViewById(R.id.la_back);
        rlGroupList = findViewById(R.id.rlGroupList);
        ic_include = findViewById(R.id.ic_include);
        card_search = findViewById(R.id.card_search);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.import_contact) + "<small>"));
        txtAddContact.setText(getString(R.string.select_csv_file));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlGroupList.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtDelete.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSave.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            card_search.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            etGroupName.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            etGroupName.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            etGroupName.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtCountMember.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtCSVFileDownlod.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            txtCSVFileDownlod.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rlGroupList.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtDelete.setTextColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtSave.setTextColor(ContextCompat.getColor(this, R.color.darkBlack));
            card_search.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            etGroupName.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            etGroupName.setHintTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            etGroupName.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtCountMember.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtCSVFileDownlod.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border));
            txtCSVFileDownlod.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        recycleGroupList.setAdapter(selectedContactAdapter);

        if (getIntent().getLongExtra(Event.IMPORTED_FILE_ID.name(), 0) > 0) {
            GroupDatabase groupDatabase = getGroupDatabase();
            ImportedFileAndContacts(groupDatabase);
        } else {
            NewImport();
        }

        txtAddContact.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
            intent.addCategory("android.intent.category.OPENABLE");
            intent.putExtra("android.intent.extra.LOCAL_ONLY", true);
            intent.setType("text/*");
            intent.setFlags(1);
            startActivityForResult(intent, 1278);
        });

        txtSave.setOnClickListener(v -> {
            if (isEditing()) {
                onUpdateClicked();
                return;
            }
            GroupDatabase groupDatabase = getGroupDatabase();
            if (!TextUtils.isEmpty(etGroupName.getText().toString())) {
                String obj = etGroupName.getText().toString();
                if (obj == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                } else if (StringsKt.trim(obj).toString().length() >= 2) {
                    if (selectedContactModelList.size() >= 1) {
                        try {
                            importedFile.setName(etGroupName.getText().toString());
                            importedFile.setCount((long) selectedContactModelList.size());
                            importedFile.setTimestamp(System.currentTimeMillis());
                            ProgressDialogUtils.displayProgress(BulkActivityImportedContact.this, "Saving...Please wait");
                            Completable.fromRunnable(() -> {
                                groupDatabase.importedFileDao().insertImportedFile(getImportedFile());
                                saveImportedContacts(groupDatabase);
                            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                                ProgressDialogUtils.stopProgressDisplay();
                                Intent intent = new Intent();
                                intent.putExtra(Event.IMPORTED_FILE_ID.name(), getImportedFile().getImportedFileId());
                                setResult(-1, intent);
                                finish();
                            }, throwable -> {
                                Utils.showToast(BulkActivityImportedContact.this, "Unable to save");
                                ProgressDialogUtils.stopProgressDisplay();
                            });
                        } catch (Exception unused) {
                            unused.printStackTrace();
                        }
                        return;
                    } else {
                        Utils.showToast(this, "Minimum one contact required");
                        return;
                    }
                }
            }
            Utils.showToast(this, "Enter proper name ");
        });

        la_back.setOnClickListener(v -> onBackPressed());

        selectedContactAdapter.setOnGroupContactSelection(contactModel -> {
            int size = selectedContactModelList.size();
            txtCountMember.setText(size + " contacts imported");
            DeleteImportedContacts(contactModel);
        });
    }

    public final GroupDatabase getGroupDatabase() {
        return GroupDatabase.getInstance(BulkActivityImportedContact.this);
    }

    @SuppressLint("CheckResult")
    private void ImportedFileAndContacts(GroupDatabase groupDatabase) {
        isEditing = true;
        progress_bar.setVisibility(View.VISIBLE);
        groupDatabase.importedFileDao().getImportedFile(getIntent().getLongExtra(Event.IMPORTED_FILE_ID.name(), 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(importedFile -> {
            setImportedFile(importedFile);
            if (getImportedFile() != null) {
                ActionBar supportActionBar = getSupportActionBar();
                if (supportActionBar != null) {
                    supportActionBar.setTitle(Html.fromHtml("<small>" + getImportedFile().getName() + "<small>"));
                }
                ActionBar supportActionBar2 = getSupportActionBar();
                if (supportActionBar2 != null) {
                    supportActionBar2.setSubtitle(Html.fromHtml("<small>manage contacts<small>"));
                }

                llInstruction.setVisibility(View.GONE);
                etGroupName.setText(getImportedFile().getName());
                txtDelete.setVisibility(View.VISIBLE);
                txtSave.setText(getString(R.string.updates));

                getGroupDatabase().importedContactsDao().getContactsFromImportId(getIntent().getLongExtra(Event.IMPORTED_FILE_ID.name(), 0)).subscribeOn(Schedulers.io()).doAfterSuccess((Consumer<List<? extends ContactModel>>) list -> runOnUiThread(() -> {
                    progress_bar.setVisibility(View.GONE);
                    MemberUpdate(list);
                })).doOnError(th -> runOnUiThread(() -> {
                    progress_bar.setVisibility(View.GONE);
                    Utils.showToast(BulkActivityImportedContact.this, "Unable to get the imported contacts! Try Again!");
                })).subscribe();
                return;
            }
            NewImport();
        }, throwable -> Utils.showToast(BulkActivityImportedContact.this, "Failed to load imported file"));

        txtDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BulkActivityImportedContact.this);
            builder.setMessage(getString(R.string.delete_import) + importedFile.getName() + " ?").setPositiveButton(R.string.delete, (dialog, which) -> Completable.fromCallable(() -> {
                if ((groupDatabase != null ? groupDatabase.importedFileDao() : null) == null || getImportedFile() == null) {
                    Utils.showToast(BulkActivityImportedContact.this, getString(R.string.failed_please_try_again));
                    return null;
                }
                groupDatabase.importedFileDao().delete(getImportedFile());
                groupDatabase.getContactDao().deleteAll(selectedContactModelList);
                return null;
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                setResult(-1);
                finish();
                Utils.showToast(BulkActivityImportedContact.this, "Deleted Successfully");
            }, th -> Utils.showToast(BulkActivityImportedContact.this, "Failed to delete"))).setNegativeButton(R.string.cancel, null).show();
        });
    }

    @SuppressLint("CheckResult")
    public final void NewImport() {
        etGroupName.setHint(Html.fromHtml("Enter import name <small>( ex 'Promotional Contacts')</small>"));
        llInstruction.setVisibility(View.VISIBLE);
        imgSample.setVisibility(View.VISIBLE);
        txtCSVFileDownlod.setVisibility(View.VISIBLE);
        txtCSVFileDownlod.setOnClickListener(v -> {
            Ref.ObjectRef<File> objectRef = new Ref.ObjectRef<>();
            objectRef.element = null;
            ProgressDialogUtils.displayProgress(BulkActivityImportedContact.this, "Downloading..");
            Completable.fromRunnable(() -> objectRef.element = new DownlodExportUtil().downloadSampleCsv()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                File t = (File) objectRef.element;
                String sb = "File Downloaded at " + (t != null ? t.getAbsolutePath() : null);
                Utils.showToast(BulkActivityImportedContact.this, sb);
                ProgressDialogUtils.stopProgressDisplay();
            }, throwable -> Utils.showToast(BulkActivityImportedContact.this, "Something went wrong Please try again"));
        });
    }

    @SuppressLint("CheckResult")
    public final void DeleteImportedContacts(ContactModel contactModel) {
        Completable.fromCallable(() -> {
            ImportedContactsDao importedContactsDao = getGroupDatabase().importedContactsDao();
            Long importedFileId = getImportedFile().getImportedFileId();
            long j = 0;
            long longValue = importedFileId != null ? importedFileId : 0;
            if (contactModel != null) {
                j = contactModel.getId();
            }
            importedContactsDao.deleteImportedContact(longValue, j);
            if (contactModel != null) {
                GroupDatabase groupDatabase = getGroupDatabase();
                groupDatabase.getContactDao().delete(contactModel);
            }
            getImportedFile().setCount((long) selectedContactModelList.size());
            getGroupDatabase().importedFileDao().updateImportedFile(getImportedFile());
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> Utils.showToast(BulkActivityImportedContact.this, "Deleted"), throwable -> Utils.showToast(BulkActivityImportedContact.this, "Failed to delete"));
    }

    @SuppressLint("CheckResult")
    public final void onUpdateClicked() {
        Editable text = etGroupName.getText();
        if ((text == null || (StringsKt.isBlank(text))) || selectedContactModelList.isEmpty()) {
            Utils.showToast(this, "Please write name or import minimum one contact");
        } else {
            Completable.fromCallable(() -> {
                getImportedFile().setCount((long) selectedContactModelList.size());
                ImportedFile importedFile = getImportedFile();
                importedFile.setName(etGroupName.getText().toString());
                getGroupDatabase().importedFileDao().updateImportedFile(getImportedFile());
                return null;
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> Utils.showToast(BulkActivityImportedContact.this, "Successfully Saved!"), throwable -> Utils.showToast(BulkActivityImportedContact.this, "Failed to Save! Try Again"));
        }
    }

    public final void saveImportedContacts(GroupDatabase groupDatabase) {
        try {
            ImportedFileDao importedFileDao = groupDatabase.importedFileDao();
            String name = importedFile.getName();
            if (name == null) {
                name = "";
            }
            long importedFileId = importedFileDao.getImportedFileId(name);
            ArrayList<ImportedContact> arrayList = new ArrayList<>();
            for (Long aLong : groupDatabase.getContactDao().insertAllContacts(selectedContactModelList)) {
                arrayList.add(new ImportedContact(0, importedFileId, aLong, 1));
            }
            groupDatabase.importedContactsDao().insertAll(arrayList);
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    public final void MemberUpdate(List<? extends ContactModel> list) {
        try {
            if (!list.isEmpty()) {
                txtAddContact.setVisibility(View.GONE);
                llInstruction.setVisibility(View.GONE);
            }
            txtSave.setAnimation(AnimationUtils.loadAnimation(BulkActivityImportedContact.this, R.anim.scale_up));
            txtSave.setVisibility(View.VISIBLE);
            selectedContactModelList.clear();
            selectedContactModelList.addAll(list);
            selectedContactAdapter.contactModelList = selectedContactModelList;
            selectedContactAdapter.notifyDataSetChanged();
            progress_bar.setVisibility(View.GONE);
            txtCountMember.setText(selectedContactModelList.size() + " contacts imported");
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        Uri uri;
        Uri data;
        super.onActivityResult(i, i2, intent);
        if (i == 1278 && -1 == i2 && i2 == -1) {
            Uri uri2 = null;
            if (intent != null) {
                try {
                    uri = intent.getData();
                } catch (Exception unused) {
                    return;
                }
            } else {
                uri = null;
            }
            if (uri != null) {
                try {
                    if (Build.VERSION.SDK_INT < 19) {
                        String packageName = getPackageName();
                        if (intent != null) {
                            uri2 = intent.getData();
                        }
                        grantUriPermission(packageName, uri2, 64);
                    } else {
                        ContentResolver contentResolver = getContentResolver();
                        if (intent != null) {
                            uri2 = intent.getData();
                        }
                        if (uri2 == null) {
                            Intrinsics.throwNpe();
                        }
                        contentResolver.takePersistableUriPermission(uri2, 1);
                    }
                    if (intent != null && (data = intent.getData()) != null) {
                        readContactsfromCsvFile(data);
                    }
                } catch (Exception unused2) {
                    Utils.showToast(BulkActivityImportedContact.this, "Something went wrong! Please try again");
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private void readContactsfromCsvFile(Uri uri) {
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        ProgressDialogUtils.displayProgress(BulkActivityImportedContact.this, "Importing Contacts...Please wait");
        Completable.fromRunnable(() -> {
            try {
                for (String s : TextStreamsKt.readLines(new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri))))) {
                    List list = StringsKt.split(s, new String[]{","}, false, 0);
                    list.size();
                    CharSequence charSequence = (CharSequence) list.get(0);
                    if (!(charSequence == null || charSequence.length() == 0)) {
                        try {
                            String unused = StringsKt.replace((String) list.get(0), "", "", false);
                            if (list.size() > 2) {
                                arrayList.add(new ContactModel((String) list.get(2), (String) list.get(0), (String) list.get(1)));
                            } else if (list.size() > 1) {
                                arrayList.add(new ContactModel("", (String) list.get(0), (String) list.get(1)));
                            } else if (!list.isEmpty()) {
                                arrayList.add(new ContactModel("", (String) list.get(0), ""));
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            } catch (Exception unused2) {
                unused2.printStackTrace();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            ProgressDialogUtils.stopProgressDisplay();
            MemberUpdate(arrayList);
            Editable text = etGroupName.getText();
            if (text == null || text.length() == 0) {
                Utils.showToast(BulkActivityImportedContact.this, arrayList.size() + " contacts imported Successfully. Now enter import name and Save it.");
                return;
            }
            Utils.showToast(BulkActivityImportedContact.this, arrayList.size() + " contacts imported Successfully. Now Save it.");
        });
    }

    @NotNull
    public final ImportedFile getImportedFile() {
        return importedFile;
    }

    public final void setImportedFile(@NotNull ImportedFile importedFile2) {
        importedFile = importedFile2;
    }

    public final boolean isEditing() {
        return isEditing;
    }

    public final void setEditing(boolean z) {
        isEditing = z;
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