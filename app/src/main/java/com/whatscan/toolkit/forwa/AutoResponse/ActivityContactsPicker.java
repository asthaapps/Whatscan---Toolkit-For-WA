package com.whatscan.toolkit.forwa.AutoResponse;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.DataBaseHelper.ContactDatabaseHelper;
import com.whatscan.toolkit.forwa.GetSet.Contact;
import com.whatscan.toolkit.forwa.GetSet.ContactModelAuto;
import com.whatscan.toolkit.forwa.GetSet.ContactsList;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.MainService;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.ArrayList;

public class ActivityContactsPicker extends AppCompatActivity {
    public ArrayList<ContactModelAuto> TablecontactModels;
    public ImageView btnDone;
    public ListView contactsChooser;
    public ContactDatabaseHelper contactDatabaseHelper;
    public ContactsListAdapter contactsListAdapter;
    public ContactsLoader contactsLoader;
    public ContactsList selectedContactsList;
    public ContactsList tempcontactsList;
    public EditText txtFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityContactsPicker.this);
        setContentView(R.layout.activity_contacts_picker);

        FindView();
    }

    private void FindView() {
        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        ImageView la_info = findViewById(R.id.la_info);
        RelativeLayout rlContact = findViewById(R.id.rlContact);
        RelativeLayout rlSearch = findViewById(R.id.rlSearch);
        View ic_include = findViewById(R.id.ic_include);
   //     ImageView searchbtn = findViewById(R.id.searchbtn);
        contactsChooser = findViewById(R.id.lst_contacts_chooser);
        btnDone = findViewById(R.id.btn_done);
        txtFilter = findViewById(R.id.txt_filter);

        la_info.setVisibility(View.GONE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.contact_list) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlContact.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            rlSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_black));
            txtFilter.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            txtFilter.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFilter.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
      //      searchbtn.setImageResource(R.drawable.ic_baseline_search_24);
        }

        TablecontactModels = new ArrayList<>();
        tempcontactsList = new ContactsList();
        selectedContactsList = new ContactsList();
        contactDatabaseHelper = new ContactDatabaseHelper(this);

        if (MainService.contactsList == null) {
            MainService.contactsList = new ContactsList();
            MainService.contactsList.contactArrayList.clear();
        }


        TablecontactModels = viewAll();
        contactsListAdapter = new ContactsListAdapter(this, MainService.contactsList);
        contactsChooser.setAdapter(contactsListAdapter);
        tempcontactsList = MainService.contactsList;
        if (MainService.contactsList.getCount() == 0) {
            loadContacts("");
        } else {
            contactsListAdapter.refreshaddContacts();
            MainService.contactsList = new ContactsList();
            MainService.contactsList = tempcontactsList;
        }

        txtFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                contactsListAdapter.filter(charSequence.toString());
            }
        });

        btnDone.setOnClickListener(view -> {
            contactDatabaseHelper.deleteAllChats();
            if (selectedContactsList.contactArrayList.size() > 0) {
                for (int i = 0; i < selectedContactsList.contactArrayList.size(); i++) {
                    contactDatabaseHelper.insertData(selectedContactsList.contactArrayList.get(i).name);
                }
            } else {
                contactDatabaseHelper.deleteAllChats();
            }
            contactDatabaseHelper.close();
            finish();
        });

        la_back.setOnClickListener(v -> onBackPressed());
    }

    public ArrayList<ContactModelAuto> viewAll() {
        Cursor allData = contactDatabaseHelper.getAllData();
        TablecontactModels = new ArrayList<>();
        while (allData.moveToNext()) {
            ContactModelAuto contactModel = new ContactModelAuto();
            contactModel.setId(allData.getString(0));
            contactModel.setName(allData.getString(1));
            TablecontactModels.add(contactModel);
        }
        contactDatabaseHelper.close();
        return TablecontactModels;
    }

    private void loadContacts(String str) {
        if (!(contactsLoader == null || contactsLoader.getStatus() == AsyncTask.Status.FINISHED)) {
            try {
                contactsLoader.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (str == null) {
            str = "";
        }
        try {
            contactsLoader = new ContactsLoader(this, contactsListAdapter);
            contactsLoader.execute(str);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
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

    public class ContactsListAdapter extends BaseAdapter {
        ContactsList contactsList;
        Context context;
        String filterContactName = "";
        ContactsList filteredContactsList = new ContactsList();

        public ContactsListAdapter(Context context2, ContactsList contactsList2) {
            context = context2;
            contactsList = contactsList2;
        }

        public void filter(String str) {
            filteredContactsList.contactArrayList.clear();
            if (str.isEmpty() || str.length() < 1) {
                filteredContactsList.contactArrayList.addAll(contactsList.contactArrayList);
            } else {
                String trim = str.toLowerCase().trim();
                for (int i = 0; i < contactsList.contactArrayList.size(); i++) {
                    if (contactsList.contactArrayList.get(i).name.toLowerCase().contains(trim)) {
                        filteredContactsList.addContact(contactsList.contactArrayList.get(i));
                    }
                }
            }
            notifyDataSetChanged();
        }

        public void addContacts(ArrayList<Contact> arrayList) {
            contactsList.contactArrayList.addAll(arrayList);
            filter(filterContactName);
        }

        public void refreshaddContacts() {
            filter(filterContactName);
            for (int i = 0; i < filteredContactsList.contactArrayList.size(); i++) {
                for (int i2 = 0; i2 < TablecontactModels.size(); i2++) {
                    if (filteredContactsList.contactArrayList.get(i).toString().split(" : ")[0].equals(TablecontactModels.get(i2).getName())) {
                        notifyDataSetChanged();
                    }
                }
            }
        }

        public int getCount() {
            return filteredContactsList.getCount();
        }

        public Contact getItem(int i) {
            return filteredContactsList.contactArrayList.get(i);
        }

        public long getItemId(int i) {
            return Long.parseLong(getItem(i).id);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = ((Activity) context).getLayoutInflater().inflate(R.layout.contact_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.chkContact = view.findViewById(R.id.chk_contact);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            if (Preference.getBooleanTheme(false)){
                viewHolder.chkContact.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

            viewHolder.chkContact.setText(filteredContactsList.contactArrayList.get(i).name);
            viewHolder.chkContact.setId(Integer.parseInt(filteredContactsList.contactArrayList.get(i).id));
            viewHolder.chkContact.setChecked(alreadySelected(filteredContactsList.contactArrayList.get(i)));
            for (int i2 = 0; i2 < TablecontactModels.size(); i2++) {
                if (filteredContactsList.contactArrayList.get(i).toString().split(" : ")[0].equals(TablecontactModels.get(i2).getName())) {
                    viewHolder.chkContact.setChecked(true);
                    if (btnDone != null) {
                        btnDone.setVisibility(View.VISIBLE);
                    }
                }
            }

            viewHolder.chkContact.setOnCheckedChangeListener((compoundButton, z) -> {
                contactDatabaseHelper = new ContactDatabaseHelper(context);
                Contact contact = filteredContactsList.getContact(compoundButton.getId());
                if (contact != null && z && !alreadySelected(contact)) {
                    selectedContactsList.addContact(contact);
                } else if (contact != null && !z) {
                    selectedContactsList.removeContact(contact);
                }
            });
            return view;
        }

        public boolean alreadySelected(Contact contact) {
            return selectedContactsList.getContact(Integer.parseInt(contact.id)) != null;
        }

        public class ViewHolder {
            CheckBox chkContact;

            public ViewHolder() {
            }
        }
    }

    public class ContactsLoader extends AsyncTask<String, Void, Void> {
        private final ArrayList<Contact> tempContactHolder = new ArrayList<>();
        ContactsListAdapter contactsListAdapter;
        Context context;
        int loadedContactsCount = 0;
        int totalContactsCount = 0;

        public ContactsLoader(Context context2, ContactsListAdapter contactsListAdapter2) {
            context = context2;
            contactsListAdapter = contactsListAdapter2;
        }

        public Void doInBackground(String[] strArr) {
            Cursor cursor;
            String str = strArr[0];
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            String[] strArr2 = {"_id", "display_name", "has_phone_number"};
            if (str.length() > 0) {
                cursor = contentResolver.query(uri, strArr2, "display_name LIKE ?", new String[]{str}, "display_name ASC");
            } else {
                cursor = contentResolver.query(uri, strArr2, null, null, "display_name ASC");
            }
            totalContactsCount = cursor.getCount();
            if (cursor == null || cursor.getCount() <= 0) {
                return null;
            }
            while (cursor.moveToNext()) {
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex("has_phone_number"))) > 0) {
                    String string = cursor.getString(cursor.getColumnIndex("_id"));
                    String string2 = cursor.getString(cursor.getColumnIndex("display_name"));
                    Cursor query = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id=?", new String[]{string}, null);
                    if (query != null && query.getCount() > 0) {
                        while (query.moveToNext()) {
                            String string3 = query.getString(query.getColumnIndex("_id"));
                            String str2 = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), query.getInt(query.getColumnIndex("data2")), query.getString(query.getColumnIndex("data3")));
                            String string4 = query.getString(query.getColumnIndex("data1"));
                            boolean z = false;
                            for (int i = 0; i < tempContactHolder.size(); i++) {
                                if (tempContactHolder.get(i).name.equals(string2)) {
                                    z = true;
                                }
                            }
                            if (!z) {
                                tempContactHolder.add(new Contact(string3, string2, string4, str2));
                            }
                        }
                        query.close();
                    }
                }
                loadedContactsCount++;
                publishProgress();
            }
            cursor.close();
            return null;
        }

        public void onProgressUpdate(Void[] voidArr) {
            if (tempContactHolder.size() >= 100) {
                contactsListAdapter.addContacts(tempContactHolder);
                tempContactHolder.clear();
            }
        }

        public void onPostExecute(Void r2) {
            contactsListAdapter.addContacts(tempContactHolder);
            tempContactHolder.clear();
        }
    }
}