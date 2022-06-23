package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.ContactViewAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ContactDao;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupModel;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDatabase;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ImportedFile;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.ProgressDialogUtils;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.jvm.internal.Intrinsics;

public class BulkActivityContactView extends AppCompatActivity {
    public int currentPosition;
    public boolean business;
    public GroupModel groupModelList;
    public ImportedFile importedFileList;
    public RecyclerView recycleContactView;
    public ContactViewAdapter contactViewAdapter;
    public ImageView la_back;
    public RelativeLayout rlContactView, ic_include;
    public TextView tv_toolbar;
    public String countryCode = "+91", strMessage, strMode;
    public ArrayList<ContactModel> contactModelList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityContactView.this);
        setContentView(R.layout.activity_contact_view);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(BulkActivityContactView.this, ll_banner);

        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        recycleContactView = findViewById(R.id.recycleContactView);
        rlContactView = findViewById(R.id.rlContactView);
        ic_include = findViewById(R.id.ic_include);

        if (Preference.getBooleanTheme(false)){
            setStatusBarTheme();
            rlContactView.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }else {
            setStatusBar();
            rlContactView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        tv_toolbar.setText(Html.fromHtml("<small>Selected Contacts</small>"));
        
        la_back.setOnClickListener(v -> onBackPressed());

        setValuesFromIntent(getIntent());
    }

    public final GroupDatabase getGroupDatabase() {
        return GroupDatabase.getInstance(BulkActivityContactView.this);
    }

    private void setValuesFromIntent(Intent intent) {
        String str;
        Serializable serializable;
        
        strMessage = intent != null ? intent.getStringExtra(Event.MESSAGE.name()) : null;
        Boolean valueOf = intent != null ? intent.getBooleanExtra(Event.IS_WHATSAPP_BUSINESS.name(), false) : null;
        if (valueOf == null) {
            Intrinsics.throwNpe();
        }
        business = valueOf;
        if ((str = intent.getStringExtra(Event.COUNTRY_CODE.name())) == null) {
            str = "";
        }
        countryCode = str;
        
        strMode = intent.getStringExtra(Event.SEND_MODE.name());
        currentPosition = intent.getIntExtra(Event.SENDING_POSITION.name(), 0);
        Serializable serializableExtra = intent.getSerializableExtra(Event.SELECTED_GROUP.name());
        if (serializableExtra != null) {
            groupModelList = (GroupModel) serializableExtra;
        }
        serializable = intent.getSerializableExtra(Event.SELECTED_IMPORT.name());
        if (serializable != null) {
            importedFileList = (ImportedFile) serializable;
        }
        
        FetchSelectedContacts();
    }

    @SuppressLint("CheckResult")
    private void FetchSelectedContacts() {
        contactModelList.clear();
        ProgressDialogUtils.displayProgress(BulkActivityContactView.this, "loading selected contacts...");
        GroupDatabase groupDatabase = getGroupDatabase();
        (Objects.requireNonNull(groupDatabase != null ? groupDatabase.selectedContactsDao() : null)).getAllContactsId().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {
            FetchGroupContactID(list);
            ProgressDialogUtils.stopProgressDisplay();
        }, throwable -> {
            UpdateRecyclerView();
            ProgressDialogUtils.stopProgressDisplay();
        });

        if (Preference.getLongString(BulkActivityContactView.this, Event.SELECTED_IMPORT.name(), 0) > 0) {
            ProgressDialogUtils.displayProgress(BulkActivityContactView.this, "loading Imported contacts...");
            getGroupDatabase().importedContactsDao().getContactsFromImportId(Preference.getLongString(BulkActivityContactView.this, Event.SELECTED_IMPORT.name(), 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(contactModels -> {
                contactModelList.addAll(contactModels);
                ProgressDialogUtils.stopProgressDisplay();
                UpdateRecyclerView();
            }, throwable -> ProgressDialogUtils.stopProgressDisplay());
        }

        if (Preference.getIntString(BulkActivityContactView.this, Event.SELECTED_GROUP.name(), 0) > 0) {
            ProgressDialogUtils.displayProgress(BulkActivityContactView.this, "loading Groups contacts...");
            getGroupDatabase().getNewContactGroupDao().getContactsFromGroupId(Preference.getIntString(BulkActivityContactView.this, Event.SELECTED_GROUP.name(), 0)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(contactModels -> {
                contactModelList.addAll(contactModels);
                ProgressDialogUtils.stopProgressDisplay();
                UpdateRecyclerView();
            }, throwable -> {
                UpdateRecyclerView();
                ProgressDialogUtils.stopProgressDisplay();
            });
        }
    }

    @SuppressLint("CheckResult")
    public final void FetchGroupContactID(List<Integer> list) {
        ProgressDialogUtils.displayProgress(BulkActivityContactView.this);
        Completable.fromCallable(() -> {
            ContactDao contactDao;
            ContactModel contactFromIdSync;
            for (Number number : list) {
                int intValue = number.intValue();
                try {
                    GroupDatabase groupDatabase = getGroupDatabase();
                    if (!(groupDatabase == null || (contactDao = groupDatabase.getContactDao()) == null || (contactFromIdSync = contactDao.getContactFromIdSync(intValue)) == null)) {
                        Log.i("GroupDetail", "Phone " + contactFromIdSync.getPhoneNumber());
                        contactModelList.add(contactFromIdSync);
                    }
                } catch (Exception unused) {
                    runOnUiThread(() -> {
                        UpdateRecyclerView();
                        ProgressDialogUtils.stopProgressDisplay();
                    });
                }
            }
            return null;
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            ProgressDialogUtils.stopProgressDisplay();
            UpdateRecyclerView();
        });
    }

    public final void UpdateRecyclerView() {
        recycleContactView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        contactViewAdapter = new ContactViewAdapter(this, contactModelList);
        recycleContactView.setAdapter(contactViewAdapter);
        contactViewAdapter.notifyDataSetChanged();
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