package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.GroupSelectionAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ContactGroup;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDatabase;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.NewContactGroup;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.ProgressDialogUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;

public class BulkActivityAddGroup extends AppCompatActivity {
    TextView tv_toolbar, txtNoGroup, txtNoGroupSub;
    FloatingActionButton btnAddGroup;
    RecyclerView recycleAddGroup;
    LinearLayout llInstruction;
    ImageView la_back;
    RelativeLayout rlGroup, ic_include;
    GroupSelectionAdapter groupSelectionAdapter;
    List<GroupModel> groupModelList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityAddGroup.this);
        setContentView(R.layout.activity_add_group);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(BulkActivityAddGroup.this, ll_banner);

        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        txtNoGroup = findViewById(R.id.txtNoGroup);
        txtNoGroupSub = findViewById(R.id.txtNoGroupSub);
        btnAddGroup = findViewById(R.id.btnAddGroup);
        recycleAddGroup = findViewById(R.id.recycleAddGroup);
        llInstruction = findViewById(R.id.llInstruction);
        rlGroup = findViewById(R.id.rlGroup);
        ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.select_group) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlGroup.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtNoGroup.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtNoGroupSub.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }else {
            setStatusBar();
            rlGroup.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtNoGroup.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        recycleAddGroup.setAdapter(groupSelectionAdapter);

        la_back.setOnClickListener(v -> onBackPressed());

        btnAddGroup.setOnClickListener(v -> CreateGroup());

        CheckGroup();
        getGroupList();
    }

    @SuppressLint("CheckResult")
    private void CheckGroup() {
        if (!Preference.getSavedBoolean(BulkActivityAddGroup.this, Event.IS_GROUP_DATA_MIGRATED.name(), false)) {
            try {
                Completable.fromCallable(() -> {
                    List<NewContactGroup> newContactGroups = getGroupDatabase().getNewContactGroupDao().getAll();
                    List<ContactGroup> contactGroups = getGroupDatabase().getContactGroupDao().getAll();
                    ArrayList<NewContactGroup> newContactGroupArrayList = new ArrayList<>();
                    HashMap hashMap = new HashMap();

                    if (!newContactGroups.isEmpty() || !contactGroups.isEmpty()) {
                        Preference.saveBooleanData(BulkActivityAddGroup.this, Event.IS_GROUP_DATA_MIGRATED.name(), true);
                        return Unit.INSTANCE;
                    }

                    runOnUiThread(() -> ProgressDialogUtils.displayProgress(BulkActivityAddGroup.this));
                    Disposable subscribe = getGroupDatabase().getContactDao().getAll().subscribeOn(Schedulers.computation()).observeOn(Schedulers.io()).subscribe((Consumer<List<? extends ContactModel>>) list -> {
                        for (ContactModel t : list) {
                            String contactId = t.getContactId();
                            hashMap.put(contactId, t.getId());
                        }
                        for (ContactGroup t2 : contactGroups) {
                            newContactGroupArrayList.add(new NewContactGroup(null, t2.getContactId(), (Integer) hashMap.get(t2.getContactId()), t2.getGroupId(), 1));
                        }
                        if (!newContactGroupArrayList.isEmpty()) {
                            getGroupDatabase().getNewContactGroupDao().insertAll(newContactGroupArrayList);
                        }
                        Preference.saveBooleanData(BulkActivityAddGroup.this, Event.IS_GROUP_DATA_MIGRATED.name(), true);
                        getGroupList();
                    }, th -> {
                        Preference.saveBooleanData(BulkActivityAddGroup.this, Event.IS_GROUP_DATA_MIGRATED.name(), true);
                        getGroupList();
                    });
                    return subscribe;
                }).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(() -> Preference.saveBooleanData(BulkActivityAddGroup.this, Event.IS_GROUP_DATA_MIGRATED.name(), true), throwable -> {
                    Preference.saveBooleanData(BulkActivityAddGroup.this, Event.IS_GROUP_DATA_MIGRATED.name(), true);
                    getGroupList();
                });
            } catch (Exception unused) {
                Preference.saveBooleanData(BulkActivityAddGroup.this, Event.IS_GROUP_DATA_MIGRATED.name(), true);
                getGroupList();
            }
        }
    }

    public final GroupDatabase getGroupDatabase() {
        return GroupDatabase.getInstance(BulkActivityAddGroup.this);
    }

    @SuppressLint("CheckResult")
    public final void getGroupList() {
        try {
            getGroupDatabase().getGroupDao().getAll().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(groups -> {
                if (groups != null) {
                    try {
                        if (!groups.isEmpty()) {
                            llInstruction.setVisibility(View.GONE);
                            recycleAddGroup.setVisibility(View.VISIBLE);
                            groupModelList = groups;
                            groupSelectionAdapter = new GroupSelectionAdapter(BulkActivityAddGroup.this, groups);
                            recycleAddGroup.setAdapter(groupSelectionAdapter);
                            groupSelectionAdapter.notifyDataSetChanged();
                            return;
                        }
                    } catch (Exception unused) {
                        setInstructionUi();
                        return;
                    }
                }
                setInstructionUi();
            }, throwable -> setInstructionUi());
        } catch (Exception unused) {
            setInstructionUi();
        }
    }

    public final void setInstructionUi() {
        llInstruction.setVisibility(View.VISIBLE);
        recycleAddGroup.setVisibility(View.GONE);
    }

    public final void CreateGroup() {
        startActivityForResult(new Intent(this, BulkActivityGroupList.class), 4321);
    }

    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        getGroupList();
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