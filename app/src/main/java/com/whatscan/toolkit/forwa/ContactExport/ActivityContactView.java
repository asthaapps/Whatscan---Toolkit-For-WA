package com.whatscan.toolkit.forwa.ContactExport;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.ExportContactAdapter;
import com.whatscan.toolkit.forwa.ContactExport.Helper.Conversion;
import com.whatscan.toolkit.forwa.ContactExport.Helper.JSON;
import com.whatscan.toolkit.forwa.ContactExport.Helper.SharedPreferenceClass;
import com.whatscan.toolkit.forwa.GetSet.GetSetContact;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.util.ArrayList;

public class ActivityContactView extends AppCompatActivity {
    public int totalContact;
    public String Wval;
    public String allContact = "";
    public ArrayList<GetSetContact> contactArray = new ArrayList<>();
    public Cursor cursor9;
    public RecyclerView lvContact;
    public ImageView la_back;
    public TextView tv_toolbar;

    public static ArrayList<Conversion> getConvertedFiles(Context context) {
        ArrayList<Conversion> arrayList = new ArrayList<>();
        String stringPrefrence = SharedPreferenceClass.getStringPrefrence(context, Conversion.TAG_CONVERSIONS, null);
        int integerPrefrence = SharedPreferenceClass.getIntegerPrefrence(context, Conversion.TAG_CONVERSIONS_SIZE, 0);
        if (stringPrefrence != null) {
            JSON json = new JSON(stringPrefrence);
            for (int i = 0; i < integerPrefrence; i++) {
                Conversion conversion = new Conversion();
                conversion.CONVERTED_FILE_PATH = json.key(i + "_" + Conversion.TAG_CONVERTED_FILE_PATH).stringValue();
                conversion.CONVERTED_FILE_NAME = json.key(i + "_" + Conversion.TAG_CONVERTED_FILE_NAME).stringValue();
                conversion.CONVERTED_FILE_DURATION = json.key(i + "_" + Conversion.TAG_CONVERTED_FILE_DURATION).stringValue();
                conversion.CONVERTED_FILE_SIZE = json.key(i + "_" + Conversion.TAG_CONVERTED_FILE_SIZE).stringValue();
                if (new File(conversion.CONVERTED_FILE_PATH).exists()) {
                    arrayList.add(conversion);
                }
            }
        }
        return arrayList;
    }

    public static void saveConvertedFiles(Context context, ArrayList<Conversion> arrayList) {
        SharedPreferenceClass.saveIntegerPrefrence(context, Conversion.TAG_CONVERSIONS_SIZE, arrayList.size());
        ArrayList<String> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(i + "_" + Conversion.TAG_CONVERTED_FILE_PATH);
            arrayList2.add(arrayList.get(i).CONVERTED_FILE_PATH);
            arrayList2.add(i + "_" + Conversion.TAG_CONVERTED_FILE_NAME);
            arrayList2.add(arrayList.get(i).CONVERTED_FILE_NAME);
            arrayList2.add(i + "_" + Conversion.TAG_CONVERTED_FILE_DURATION);
            arrayList2.add(arrayList.get(i).CONVERTED_FILE_DURATION);
            arrayList2.add(i + "_" + Conversion.TAG_CONVERTED_FILE_SIZE);
            arrayList2.add(arrayList.get(i).CONVERTED_FILE_SIZE);
        }
        SharedPreferenceClass.saveStringPrefrence(context, Conversion.TAG_CONVERSIONS, JSON.create(JSON.dic((Object) arrayList2.toArray(new String[arrayList2.size()]))).toString());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityContactView.this);
        setContentView(R.layout.activity_export_contact_view);

        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        lvContact = findViewById(R.id.lvContact);
        RelativeLayout rlContactView = findViewById(R.id.rlContactView);
        View ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.view_all_contacts) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlContactView.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }

        new ViewContactClass().execute();
    }

    public void LoadData() {
        String string = SharedPreferenceClass.getString(this, "whatsvalue", "null");
        Wval = string;
        if (string.equals("whatsapp")) {
            cursor9 = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, "account_type='com.whatsapp' and Deleted='0'", null, "display_name ASC");
        } else if (Wval.equals("whatsappB")) {
            cursor9 = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, "account_type='com.whatsapp.w4b' and Deleted='0'", null, "display_name ASC");
        }
        Cursor cursor = cursor9;
        if (cursor != null) {
            totalContact = cursor.getCount();
            cursor9.moveToFirst();
            if (cursor9.getCount() != 0) {
                do {
                    Cursor cursor2 = cursor9;
                    String string2 = cursor2.getString(cursor2.getColumnIndex("display_name"));
                    Cursor cursor3 = cursor9;
                    String string3 = cursor3.getString(cursor3.getColumnIndex("sync1"));
                    Cursor cursor4 = cursor9;
                    String string4 = cursor4.getString(cursor4.getColumnIndex("account_type"));
                    String replace = string3.replace("@s.whatsapp.net", "");
                    allContact += IOUtils.LINE_SEPARATOR_UNIX + (string2 + "," + replace);
                    contactArray.add(new GetSetContact(string2, replace, string4));
                } while (cursor9.moveToNext());
            }
            cursor9.close();
            return;
        }
        totalContact = 0;
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

    @Override
    public void onBackPressed() {
        finish();
    }

    private class ViewContactClass extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        private ViewContactClass() {
        }


        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityContactView.this);
            pDialog.setMessage("Analyzing contacts ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        public Void doInBackground(Void... voidArr) {
            LoadData();
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            runOnUiThread(() -> {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                if (totalContact == 0) {
                    lvContact.setVisibility(View.GONE);
                    return;
                }
                lvContact.setVisibility(View.VISIBLE);
                lvContact.setLayoutManager(new LinearLayoutManager(ActivityContactView.this, RecyclerView.VERTICAL, false));
                ExportContactAdapter contactAdapter = new ExportContactAdapter(contactArray, ActivityContactView.this);
                lvContact.setAdapter(contactAdapter);
                contactAdapter.notifyDataSetChanged();
            });
        }
    }
}