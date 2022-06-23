package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
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
import com.whatscan.toolkit.forwa.DataBaseHelper.ContactGroupRepository;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ContactDao;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDao;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDatabase;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.NewContactGroup;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.NewContactGroupDao;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.ProgressDialogUtils;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;

public class BulkActivityGroupList extends AppCompatActivity {

    public final ContactForGroupSelectionAdapter selectedContactAdapter = new ContactForGroupSelectionAdapter(this);
    public final ArrayList<ContactModel> contactModelList = new ArrayList<>();
    public boolean isEdit;
    public EditText etGroupName;
    public RecyclerView recycleGroupList;
    public ProgressBar progress_bar;
    public LinearLayout llInstruction;
    public ImageView imgSample;
    public ImageView la_back;
    public CardView card_search;
    public TextView tv_toolbar, txtDelete, txtSave, txtAddContact, txtCountMember;
    public GroupModel groupModel = new GroupModel();
    public ArrayList<Integer> selectedContactIDsList = new ArrayList<>();
    public RelativeLayout rlGroupList, ic_include;

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityGroupList.this);
        setContentView(R.layout.activity_group_list);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(BulkActivityGroupList.this, ll_banner);

        txtDelete = findViewById(R.id.txtDelete);
        txtSave = findViewById(R.id.txtSave);
        txtAddContact = findViewById(R.id.txtAddContact);
        txtCountMember = findViewById(R.id.txtCountMember);
        etGroupName = findViewById(R.id.etGroupName);
        recycleGroupList = findViewById(R.id.recycleGroupList);
        progress_bar = findViewById(R.id.progress_bar);
        llInstruction = findViewById(R.id.llInstruction);
        imgSample = findViewById(R.id.imgSample);
        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        rlGroupList = findViewById(R.id.rlGroupList);
        ic_include = findViewById(R.id.ic_include);
        card_search = findViewById(R.id.card_search);

        tv_toolbar.setText(Html.fromHtml("<small>" + getResources().getString(R.string.new_group) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlGroupList.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            card_search.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            etGroupName.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            txtDelete.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSave.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            etGroupName.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            etGroupName.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtCountMember.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rlGroupList.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            card_search.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            etGroupName.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtDelete.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtSave.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            etGroupName.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            etGroupName.setHintTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtCountMember.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        recycleGroupList.setAdapter(selectedContactAdapter);

        Ref.ObjectRef<ContactGroupRepository> objectRef = new Ref.ObjectRef<>();
        NewContactGroupDao newContactGroupDao = getGroupDatabase().getNewContactGroupDao();
        GroupDao groupDao = getGroupDatabase().getGroupDao();

        objectRef.element = new ContactGroupRepository(newContactGroupDao, groupDao);
        if (getIntent().getIntExtra(Event.GROUP_ID.name(), 0) > 0) {
            GroupDetailListShow();
        } else {
            GroupInstruction();
        }

        txtDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BulkActivityGroupList.this);
            builder.setMessage(getString(R.string.delete_import) + groupModel.getName() + " ?").setPositiveButton(R.string.delete, (dialog, which) -> {
                new Thread(() -> {
                    if (getGroupDatabase() == null || getGroupDatabase().getGroupDao() == null || getGroup() == null) {
                        Utils.showToast(BulkActivityGroupList.this, getString(R.string.failed_please_try_again));
                        return;
                    }
                    getGroupDatabase().getGroupDao().delete(getGroup());
                }).start();
                setResult(-1);
                finish();
            }).setNegativeButton(R.string.cancel, null).show();
        });

        txtSave.setOnClickListener(v -> {
            if (isEdit) {
                UpdateGroup();
            } else {
                SaveOfGroup((ContactGroupRepository) objectRef.element);
            }
        });

        txtAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(BulkActivityGroupList.this, BulkActivityContactShowList.class);
            intent.putIntegerArrayListExtra(Event.SELECTED_CONTACTS.name(), selectedContactIDsList);
            startActivityForResult(intent, 123);
        });

        la_back.setOnClickListener(v -> onBackPressed());

        selectedContactAdapter.setOnGroupContactSelection(contactModel -> {
            int size = contactModelList.size();
            txtCountMember.setText(Html.fromHtml(size + " members"));
            DeleteGroupContact(contactModel);
        });
    }

    public final GroupDatabase getGroupDatabase() {
        return GroupDatabase.getInstance(BulkActivityGroupList.this);
    }

    private void GroupInstruction() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(R.string.new_group));
        }

        llInstruction.setVisibility(View.VISIBLE);
        imgSample.setVisibility(View.GONE);
    }

    @SuppressLint("CheckResult")
    public final void UpdateGroup() {
        Editable text = etGroupName.getText();
        if ((text == null || (StringsKt.isBlank(text))) || !(!contactModelList.isEmpty())) {
            Utils.showToast(this, "Please write name or import minimum one contact");
        } else {
            Completable.fromCallable(() -> {
                getGroup().setCount(contactModelList.size());
                GroupModel groupModel = getGroup();
                groupModel.setName(etGroupName.getText().toString());
                getGroupDatabase().getGroupDao().updateGroup(getGroup());
                return null;
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> Utils.showToast(BulkActivityGroupList.this, "Successfully Saved!"), throwable -> Utils.showToast(BulkActivityGroupList.this, "Failed to Save! Try Again"));
        }
    }

    @SuppressLint("CheckResult")
    public final void DeleteGroupContact(ContactModel contactModel) {
        try {
            Completable.fromRunnable(() -> {
                NewContactGroupDao newContactGroupDao = getGroupDatabase().getNewContactGroupDao();
                String contactId = contactModel.getContactId();
                NewContactGroup groupModelByContactId = newContactGroupDao.getGroupModelByContactId(contactId);
                groupModelByContactId.setGroupId(getGroup().getId());
                getGroupDatabase().getNewContactGroupDao().delete(groupModelByContactId);
                getGroup().setCount(contactModelList.size());
                getGroupDatabase().getGroupDao().updateGroup(getGroup());
            }).subscribeOn(Schedulers.io()).subscribe(() -> Utils.showToast(BulkActivityGroupList.this, "Deleted"), throwable -> Utils.showToast(BulkActivityGroupList.this, "Failed to delete"));
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    private void GroupDetailListShow() {
        isEdit = true;

        GroupDao groupDao = getGroupDatabase().getGroupDao();
        Intent intent = getIntent();
        Event intentExtraKey = Event.GROUP_ID;
        groupDao.getGroup(intent.getIntExtra(intentExtraKey.name(), 0)).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(group -> {
            setGroup(group);

            etGroupName.setText(group.getName());
            txtDelete.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.VISIBLE);

            if (getGroup().getId() != null) {
                getGroupDatabase().getNewContactGroupDao().getContactsFromGroupId(intent.getIntExtra(intentExtraKey.name(), 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<ContactModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<ContactModel> contactModels) {
                        if (!contactModels.isEmpty()) {
                            contactModelList.clear();
                            contactModelList.addAll(contactModels);
                        }
                        updateMembers();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.showToast(BulkActivityGroupList.this, "Failed to load GroupModel Contacts");
                    }
                });
            }
        }, throwable -> Utils.showToast(BulkActivityGroupList.this, "Failed to load GroupModel Detail"));
    }

    public final void SaveOfGroup(ContactGroupRepository contactGroupRepository) {
        if (!TextUtils.isEmpty(etGroupName.getText().toString())) {
            String obj = etGroupName.getText().toString();
            if (obj == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            } else if (StringsKt.trim(obj).toString().length() >= 2) {
                if (contactModelList.size() >= 2) {
                    saveGroup(contactGroupRepository);
                    return;
                } else {
                    Utils.showToast(this, "Minimum two members required");
                    return;
                }
            }
        }
        Utils.showToast(this, "Give a proper name of groupModel");
    }

    @SuppressLint("CheckResult")
    private void saveGroup(ContactGroupRepository contactGroupRepository) {
        try {
            groupModel.setName(etGroupName.getText().toString());
            groupModel.setCount(contactModelList.size());
            Completable.fromRunnable(() -> {
                contactGroupRepository.saveGroup(getGroup());
                saveGroupContacts(contactGroupRepository);
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                Intent intent = new Intent();
                intent.putExtra(Event.GROUP_ID.name(), getGroup().getId());
                setResult(-1, intent);
                finish();
            }, throwable -> Utils.showToast(BulkActivityGroupList.this, "Failed to save  GroupModel"));
        } catch (Exception unused) {
            Utils.showToast(BulkActivityGroupList.this, "Failed to save  GroupModel");
        }
    }

    @SuppressLint("CheckResult")
    public final void saveGroupContacts(ContactGroupRepository contactGroupRepository) {
        try {
            String name = groupModel.getName();
            if (name == null) {
                name = "";
            }
            int groupId = contactGroupRepository.getGroupId(name);
            ArrayList<NewContactGroup> arrayList = new ArrayList<>();
            for (int intValue : selectedContactIDsList) {
                NewContactGroup newContactGroup = new NewContactGroup();
                newContactGroup.setGroupPhoneContactId(intValue);
                newContactGroup.setGroupId(groupId);
                arrayList.add(newContactGroup);
            }
            Completable.fromRunnable(() -> contactGroupRepository.getNewContactGroupDao().deleteAll(arrayList)).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(() -> contactGroupRepository.saveContactGroups(arrayList), throwable -> Utils.showToast(BulkActivityGroupList.this, "Failed to save contacts in GroupModel"));
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    public final void updateMembers() {
        try {
            if (!contactModelList.isEmpty()) {
                llInstruction.setVisibility(View.GONE);
            }

            txtSave.setAnimation(AnimationUtils.loadAnimation(BulkActivityGroupList.this, R.anim.scale_up));
            txtSave.setVisibility(View.VISIBLE);

            selectedContactAdapter.contactModelList = contactModelList;
            selectedContactAdapter.notifyDataSetChanged();

            progress_bar.setVisibility(View.GONE);
            txtCountMember.setText(contactModelList.size() + " members");
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 123 && -1 == i2 && intent != null) {
            try {
                selectedContactIDsList = intent.getIntegerArrayListExtra(Event.SELECTED_CONTACTS.name());
                if (selectedContactIDsList == null) {
                    return;
                }
                if (!isEdit) {
                    txtCountMember.setText(selectedContactIDsList.size() + " members");
                    FetchGroupContactIDs();
                    return;
                }

                updateNewContactInGroup(selectedContactIDsList);
            } catch (Exception unused) {
                unused.printStackTrace();
            }
        }
    }

    @SuppressLint("CheckResult")
    private void FetchGroupContactIDs() {
        ProgressDialogUtils.displayProgress(BulkActivityGroupList.this);
        Completable.fromCallable(() -> {
            ContactDao contactDao;
            ContactModel contactFromIdSync;
            for (Number number : selectedContactIDsList) {
                int intValue = number.intValue();
                try {
                    GroupDatabase groupDatabase = getGroupDatabase();
                    if (!(groupDatabase == null || (contactDao = groupDatabase.getContactDao()) == null || (contactFromIdSync = contactDao.getContactFromIdSync(intValue)) == null)) {
                        contactModelList.add(contactFromIdSync);
                    }
                } catch (Exception unused) {
                    runOnUiThread(ProgressDialogUtils::stopProgressDisplay);
                }
            }
            return null;
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            ProgressDialogUtils.stopProgressDisplay();
            updateMembers();
        }, throwable -> {
            Utils.showToast(BulkActivityGroupList.this, "Failed to save contacts in GroupModel");
            updateMembers();
        });
    }

    @SuppressLint("CheckResult")
    private void updateNewContactInGroup(ArrayList<Integer> arrayList1) {
        ProgressDialogUtils.displayProgress(BulkActivityGroupList.this);
        Completable.fromCallable(() -> {
            ContactDao contactDao;
            ContactModel contactFromIdSync;
            ArrayList<NewContactGroup> arrayList = new ArrayList<>();
            for (Number number : arrayList1) {
                int intValue = number.intValue();
                try {
                    GroupDatabase groupDatabase = getGroupDatabase();
                    if (!(groupDatabase == null || (contactDao = groupDatabase.getContactDao()) == null || (contactFromIdSync = contactDao.getContactFromIdSync(intValue)) == null)) {
                        contactModelList.add(contactFromIdSync);
                    }
                } catch (Exception unused) {
                    unused.printStackTrace();
                }
                NewContactGroup newContactGroup = new NewContactGroup(null, null, null, null, 15);
                newContactGroup.setGroupPhoneContactId(intValue);
                newContactGroup.setGroupId(getGroup().getId());
                arrayList.add(newContactGroup);
            }
            getGroupDatabase().getNewContactGroupDao().insertAll(arrayList);
            getGroup().setCount(contactModelList.size());
            GroupModel groupModel = getGroup();
            groupModel.setName(etGroupName.getText().toString());
            getGroupDatabase().getGroupDao().updateGroup(getGroup());
            return null;
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            ProgressDialogUtils.stopProgressDisplay();
            Utils.showToast(BulkActivityGroupList.this, arrayList1.size() + " new contacts added in the GroupModel");
            updateMembers();
        }, throwable -> {
            Utils.showToast(BulkActivityGroupList.this, "Failed to save contacts in GroupModel");
            updateMembers();
        });
    }

    @NotNull
    public final GroupModel getGroup() {
        return groupModel;
    }

    public final void setGroup(@NotNull GroupModel groupModel) {
        this.groupModel = groupModel;
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