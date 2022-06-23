package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.ContactSelectionAdapter;
import com.whatscan.toolkit.forwa.BulkSender.helper.BulkBaseTools;
import com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel;
import com.whatscan.toolkit.forwa.BulkSender.helper.WhatsAppContactRetreiver;
import com.whatscan.toolkit.forwa.DataBaseHelper.ContactRepository;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.ContactDao;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDatabase;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.SelectedContact;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.ProgressDialogUtils;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;

public class BulkActivityContactShowList extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public static int READ_LOGS;
    public final String[] Permissions = {"android.permission.READ_CONTACTS"};
    public boolean isFetchingFromDbDone, isSelected, isSelectedSearch;
    public ArrayList<ContactModel> contactModelList = new ArrayList<>();
    public ContactRepository contactRepository;
    public ContactSelectionAdapter contactSelectionAdapter;
    public MaterialButton doneContactShow;
    public EditText txtFilter;
    public RecyclerView fastScrollRecyclerView;
    public ImageView imgSelectAll, imgRefresh;
    public LottieAnimationView la_loading;
    public ImageView la_back;
    public LinearLayout progressLayout;
    public RelativeLayout rlContactList;
  //  public ImageView searchbtn;
    public View ic_include;
    public TextView tv_toolbar, searchText, progressUpdateTV, txtSelectContact;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityContactShowList.this);
        setContentView(R.layout.activity_contact_show_list);

        Utils.CheckConnection(BulkActivityContactShowList.this, findViewById(R.id.rlContactList));

        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        progressUpdateTV = findViewById(R.id.progressUpdateTV);
        doneContactShow = findViewById(R.id.doneContactShow);
        la_loading = findViewById(R.id.la_loading);
        txtSelectContact = findViewById(R.id.txtSelectContact);
        fastScrollRecyclerView = findViewById(R.id.recycler_view);
        searchText = findViewById(R.id.tv_result);
        imgSelectAll = findViewById(R.id.imgSelectAll);
        imgRefresh = findViewById(R.id.imgRefresh);
        txtFilter = findViewById(R.id.txt_filter);
        progressLayout = findViewById(R.id.progressLayout);
        rlContactList = findViewById(R.id.rlContactList);
        ic_include = findViewById(R.id.ic_include);

    //    searchbtn = findViewById(R.id.searchbtn);
        TextView txtOne= findViewById(R.id.txtOne);

        tv_toolbar.setText(Html.fromHtml("<small>" + getResources().getString(R.string.contacts) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlContactList.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
          //  card_search.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            txtFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            txtFilter.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFilter.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSelectContact.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
       //     searchbtn.setImageResource(R.drawable.ic_baseline_search_24);
            progressLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.round_progress_w));
            progressUpdateTV.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        imgSelectAll.setOnClickListener(v -> {
            if (isSelected) {
                isSelected = false;
                imgSelectAll.setImageResource(R.drawable.ic_done_all);
                if (contactSelectionAdapter != null) {
                    contactSelectionAdapter.unSetectAll();
                }
                WhatsAppContactRetreiver.unsellectMyWhatsappContacts();
            } else {
                imgSelectAll.setImageResource(R.drawable.ic_done_outline);
                if (contactSelectionAdapter != null) {
                    contactSelectionAdapter.setectAll();
                }
                isSelected = true;
            }
        });

        GroupDatabase instance = GroupDatabase.getInstance(this);
        ContactDao contactDao = instance.getContactDao();
        contactRepository = new ContactRepository(contactDao);

        if (fastScrollRecyclerView != null) {
            fastScrollRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        contactSelectionAdapter = new ContactSelectionAdapter(this);
        if (fastScrollRecyclerView != null) {
            fastScrollRecyclerView.setAdapter(contactSelectionAdapter);
        }

        loadContacts();

        imgRefresh.setOnClickListener(v -> RefreshContact());

        la_back.setOnClickListener(v -> onBackPressed());

        if (contactSelectionAdapter != null) {
            contactSelectionAdapter.setOnContactSelection(this::setSelectCountText);
        }

        if (contactSelectionAdapter != null) {
            contactSelectionAdapter.showAll();
        }

        if (doneContactShow != null) {
            doneContactShow.setOnClickListener(v -> {
                ArrayList<Integer> selectedContactsIds = contactSelectionAdapter != null ? contactSelectionAdapter.selectedContactsIds() : null;
                if (selectedContactsIds == null) {
                    Utils.showToast(BulkActivityContactShowList.this, "No contact selected!");
                } else if (selectedContactsIds.size() <= 0) {
                    Utils.showToast(BulkActivityContactShowList.this, "No contact selected! Select minimum 1 contact");
                } else if (selectedContactsIds.size() < 100000) {
                    Utils.showToast(BulkActivityContactShowList.this, selectedContactsIds.size() + " Selected");
                    if (!getIntent().getBooleanExtra(Event.REQUEST_NEW_SELECTION.name(), false)) {
                        Intent intent = new Intent();
                        intent.putIntegerArrayListExtra(Event.SELECTED_CONTACTS.name(), selectedContactsIds);
                        setResult(-1, intent);
                        finish();
                        return;
                    }
                    saveSelectedContactInDb(selectedContactsIds);
                } else {
                    Utils.showToast(BulkActivityContactShowList.this, "Max limit of 1,00,000 reached. Please select less than 1,00,000 contacts");
                }
            });
        }

        txtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        BulkBaseTools.getWtBus().toObserve().subscribe(obj -> {
            if ((obj instanceof String) && StringsKt.contains((CharSequence) obj, "Saving", false)) {
                runOnUiThread(() -> {
                    if (progressUpdateTV != null) {
                        progressUpdateTV.setText((String) obj);
                    }
                });
            }
        });
    }

    private void loadContacts() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissionToExecute(Permissions, READ_LOGS);
        } else {
            FetchAllContacts();
        }
    }

    @TargetApi(23)
    public final void checkPermissionToExecute(String[] strArr, int i) {
        boolean z = false;
        if (ContextCompat.checkSelfPermission(this, strArr[0]) != 0) {
            z = true;
        }
        if (z) {
            requestPermissions(strArr, i);
        } else {
            FetchAllContacts();
        }
    }

    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult(int i, @NotNull String[] strArr, @NotNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == READ_LOGS && Intrinsics.areEqual(strArr[0], "android.permission.READ_CONTACTS")) {
            if (iArr[0] == 0) {
                FetchAllContacts();
            } else {
                new AlertDialog.Builder(this).setMessage("The app needs these permissions to work, Exit?").setTitle("Permission Denied").setCancelable(false).setPositiveButton("Retry", (dialog, which) -> {
                    dialog.dismiss();
                    checkPermissionToExecute(Permissions, BulkActivityContactShowList.READ_LOGS);
                }).setNegativeButton("Exit App", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).show();
            }
        }
    }

    public final GroupDatabase getGroupDatabase() {
        return GroupDatabase.getInstance(BulkActivityContactShowList.this);
    }

    @SuppressLint("CheckResult")
    public final void FetchAllContacts() {
        Single<List<ContactModel>> allContacts;
        Single<List<ContactModel>> subscribeOn;
        Single<List<ContactModel>> observeOn;
        Ref.IntRef intRef = new Ref.IntRef();
        intRef.element = 0;

        if (doneContactShow != null) {
            doneContactShow.setEnabled(false);
        }

        if (progressLayout != null) {
            progressLayout.setVisibility(View.VISIBLE);
        }

        if (progressUpdateTV != null) {
            progressUpdateTV.setVisibility(View.VISIBLE);
        }

        if (fastScrollRecyclerView != null) {
            fastScrollRecyclerView.setVisibility(View.GONE);
        }

        if (contactRepository != null && (allContacts = contactRepository.getAllContacts()) != null && (subscribeOn = allContacts.subscribeOn(Schedulers.io())) != null && (observeOn = subscribeOn.observeOn(Schedulers.io())) != null) {
            observeOn.subscribe(contactModels -> {
                if (contactModels != null) {
                    contactModelList = (ArrayList<ContactModel>) contactModels;
                    {
                        ArrayList<ContactModel> arrayList = contactModelList;
                        if (arrayList.size() == 0) {
                            ArrayList<ContactModel> arrayList2 = contactModelList;
                            List<ContactModel> myWhatsappContacts = WhatsAppContactRetreiver.getMyWhatsappContacts(BulkActivityContactShowList.this);
                            if (myWhatsappContacts != null) {
                                arrayList2.addAll((ArrayList<ContactModel>) myWhatsappContacts);
                            } else {
                                throw new TypeCastException("null cannot be cast to non-null type java.util.ArrayList<com.whatscan.toolkit.forwa.model.ContactModel>");
                            }

                            if (contactRepository != null) {
                                ArrayList<ContactModel> arrayList3 = contactModelList;
                                if (arrayList3 != null) {
                                    contactRepository.saveAllContacts(arrayList3);
                                } else {
                                    throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.List<com.whatscan.toolkit.forwa.BulkSender.helper>");
                                }
                            }
                            if (!isFetchingFromDbDone) {
                                FetchAllContacts();
                                isFetchingFromDbDone = true;
                            }
                        }
                    }

                    getGroupDatabase().selectedContactsDao().getAllContactsId().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe((Consumer<List<? extends Integer>>) list -> {
                        intRef.element = list.size();
                        HashMap<Integer, String> hashMap = new HashMap<>();
                        int size = list.size();
                        for (int i = 0; i < size; i++) {
                            hashMap.put(list.get(i), "present");
                        }
                        ArrayList<ContactModel> arrayList = contactModelList;
                        if (arrayList != null) {
                            for (ContactModel t : arrayList) {
                                if (hashMap.containsKey(t.getId())) {
                                    t.setSelected(true);
                                }
                            }
                        }

                        runOnUiThread(() -> setContactsUiAfterLoadingData(intRef.element));
                    }, th -> runOnUiThread(() -> setContactsUiAfterLoadingData(intRef.element)));
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type java.util.ArrayList<com.whatscan.toolkit.forwa.BulkSender.helper>");
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Utils.showToast(BulkActivityContactShowList.this, "Something went wrong, Refresh Contacts");
                }
            });
        }
    }

    private void RefreshContact() {
        Utils.showToast(BulkActivityContactShowList.this, "Syncing contacts, Please wait");
        if (progressLayout != null) {
            progressLayout.setVisibility(View.VISIBLE);
        }

        if (progressUpdateTV != null) {
            progressUpdateTV.setVisibility(View.VISIBLE);
        }

        if (fastScrollRecyclerView != null) {
            fastScrollRecyclerView.setVisibility(View.GONE);
        }
        syncContacts();
    }

    private void syncContacts() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean z = false;
            if (ContextCompat.checkSelfPermission(this, Permissions[0]) != 0) {
                z = true;
            }
            if (z) {
                requestPermissions(Permissions, READ_LOGS);
            } else {
                syncContactsFromDbOrPhone();
            }
        } else {
            syncContactsFromDbOrPhone();
        }
    }

    @SuppressLint("CheckResult")
    private void syncContactsFromDbOrPhone() {
        if (doneContactShow != null) {
            doneContactShow.setEnabled(false);
        }

        if (progressLayout != null) {
            progressLayout.setVisibility(View.VISIBLE);
        }

        if (progressUpdateTV != null) {
            progressUpdateTV.setVisibility(View.VISIBLE);
        }

        if (fastScrollRecyclerView != null) {
            fastScrollRecyclerView.setVisibility(View.GONE);
        }

        Completable.fromCallable(() -> {
            ArrayList<ContactModel> arrayList = new ArrayList<>();
            for (ContactModel contactModel : contactModelList) {
                String contactId = contactModel.getContactId();
                if (!(contactId == null || contactId.length() == 0)) {
                    arrayList.add(contactModel);
                }
            }

            if (contactRepository != null) {
                contactRepository.deleteAll(arrayList);
            }

            if (contactRepository == null) {
                return null;
            }
            List<ContactModel> myWhatsappContacts = WhatsAppContactRetreiver.getMyWhatsappContacts(BulkActivityContactShowList.this);
            if (myWhatsappContacts != null) {
                contactRepository.saveAllContacts(myWhatsappContacts);
                return Unit.INSTANCE;
            }
            throw new TypeCastException("null cannot be cast to non-null type java.util.ArrayList<com.whatscan.toolkit.forwa.BulkSender.helper.ContactModel>");
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::FetchAllContacts, throwable -> Utils.showToast(BulkActivityContactShowList.this, "Something went wrong in selection. Try again"));
    }

    private void filter(String text) {
        ArrayList<ContactModel> filteredlist = new ArrayList<>();
        for (ContactModel item : contactModelList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            contactSelectionAdapter.filterList(filteredlist);
        }
    }

    @SuppressLint("CheckResult")
    public final void saveSelectedContactInDb(ArrayList<Integer> arrayList) {
        ProgressDialogUtils.displayProgress(this, "Selecting Contacts...Please wait");
        ArrayList<SelectedContact> arrayList2 = new ArrayList<>();
        for (int intValue : arrayList) {
            arrayList2.add(new SelectedContact(0, intValue));
        }
        Completable.fromCallable(() -> {
            getGroupDatabase().selectedContactsDao().deleteAll();
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> Completable.fromCallable(() -> {
            getGroupDatabase().selectedContactsDao().insertAll(arrayList2);
            return null;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            ProgressDialogUtils.stopProgressDisplay();
            Intent intent = new Intent();
            intent.putExtra(Event.SELECTED_CONTACTS_COUNT.name(), arrayList.size());
            setResult(-1, intent);
            finish();
        }, th -> Utils.showToast(BulkActivityContactShowList.this, "Something went wrong in selection. Try again")), throwable -> Utils.showToast(BulkActivityContactShowList.this, "Something went wrong in selection. Try again"));
    }

    public final void setContactsUiAfterLoadingData(int i) {
        if (progressLayout != null) {
            progressLayout.setVisibility(View.GONE);
        }

        if (progressUpdateTV != null) {
            progressUpdateTV.setVisibility(View.GONE);
        }

        if (fastScrollRecyclerView != null) {
            fastScrollRecyclerView.setVisibility(View.VISIBLE);
        }

        if (doneContactShow != null) {
            doneContactShow.setEnabled(true);
        }

        if (contactSelectionAdapter != null) {
            contactSelectionAdapter.setContactModelList(contactModelList);
        }

        if (contactSelectionAdapter != null) {
            contactSelectionAdapter.setSelectedCount(i);
        }
        setSelectCountText(i);
    }

    public final void setSelectCountText(int i) {
        if (txtSelectContact != null) {
            txtSelectContact.setText(String.valueOf(i));
        }
    }

    public final boolean isSelected() {
        return isSelected;
    }

    public final void setSelected(boolean z) {
        isSelected = z;
    }

    public final boolean isSelectedSearch() {
        return isSelectedSearch;
    }

    public final void setSelectedSearch(boolean z) {
        isSelectedSearch = z;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Thread(WhatsAppContactRetreiver::unsellectMyWhatsappContacts).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(BulkActivityContactShowList.this, findViewById(R.id.rlContactList), isConnected);
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