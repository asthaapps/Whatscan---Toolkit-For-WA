package com.whatscan.toolkit.forwa.ContactExport;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.ExportFileAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.ContactExport.Helper.SharedPreferenceClass;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityContactExport extends AppCompatActivity {
    public int totalLocalContact, totalWhatsAppContact, totalWhatsAppContactB, totalContact, totalWhatsAppC;
    public TextView tv_toolbar, txtWhatsAppContact, txtTotalContact, mSeekArcProgress, txtExportContact;
    public Cursor cursorLocal, cursorWhatsApp, cursorWhatsAppB;
    public LinearLayout llExport, llView;
    public RelativeLayout rlContactExp;
    public View ic_include;
    public CardView cardOne;
    public ProgressBar mSeekArc;
    public LottieAnimationView  la_empty;
    public ImageView la_back;
    public ExportFileAdapter adapter;
    public GridLayoutManager gridLayoutManager;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityContactExport.this);
        setContentView(R.layout.activity_contact_export);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityContactExport.this, ll_banner);

        la_back = findViewById(R.id.la_back);
        la_empty = findViewById(R.id.la_empty);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        llExport = findViewById(R.id.llExport);
        llView = findViewById(R.id.llView);
        mSeekArc = findViewById(R.id.seekArc);
        recyclerView = findViewById(R.id.recyclerView);
        txtWhatsAppContact = findViewById(R.id.txtWhatsAppContact);
        txtTotalContact = findViewById(R.id.txtTotalContact);
        mSeekArcProgress = findViewById(R.id.seekArcProgress);
        txtExportContact = findViewById(R.id.txtExportContact);
        rlContactExp = findViewById(R.id.rlContactExp);
        ic_include = findViewById(R.id.ic_include);
        cardOne = findViewById(R.id.cardOne);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.export_contact) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlContactExp.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            cardOne.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            txtExportContact.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtWhatsAppContact.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTotalContact.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        new LoadContact().execute();

        gridLayoutManager = new GridLayoutManager(this, 1);
        if (getFiles("") == null || getFiles("").size() <= 0) {
            la_empty.setVisibility(View.VISIBLE);
            ll_banner.setVisibility(View.GONE);
        } else {
            la_empty.setVisibility(View.GONE);
            ll_banner.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(gridLayoutManager);
            adapter = new ExportFileAdapter(this, getFiles(""), la_empty);
            recyclerView.setAdapter(adapter);
            adapter.updateItems((ArrayList<File>) getFiles(""));
            adapter.notifyDataSetChanged();
        }

        la_back.setOnClickListener(v -> onBackPressed());

        llExport.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityExport.class));
        });

        llView.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityContactView.class));
        });
    }

    public List<File> getFiles(String str) {
        File[] listFiles = new File(Environment.getExternalStorageDirectory().toString() + "/" + getString(R.string.app_directory) + "/" + "WhatsApp Contact Export").listFiles();
        ArrayList<File> arrayList = new ArrayList<>();
        if (listFiles != null) {
            for (File listFile : listFiles) {
                if (listFile.getName().contains(str)) {
                    arrayList.add(listFile);
                }
            }
        }
        return arrayList;
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
    protected void onResume() {
        super.onResume();
        adapter = new ExportFileAdapter(this, getFiles(""), la_empty);
        recyclerView.setAdapter(adapter);
        adapter.updateItems((ArrayList<File>) getFiles(""));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadContact extends AsyncTask<Void, Integer, Void> {
        ProgressDialog pDialog;

        private LoadContact() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            mSeekArc.setProgress(0);
            pDialog = new ProgressDialog(ActivityContactExport.this);
            pDialog.setMessage("Analyzing contacts ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        public Void doInBackground(Void... voidArr) {
            try {
                Cursor query = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "display_name ASC");
                String str = "";
                int i = 0;
                while (query.moveToNext()) {
                    String string = query.getString(query.getColumnIndex("data1"));
                    if (!string.equals(str)) {
                        i++;
                        str = string;
                    }
                }
                query.close();
                cursorLocal = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                totalLocalContact = cursorLocal.getCount() + -1;
                cursorWhatsApp = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, "account_type='com.whatsapp'and Deleted='0'", null, "display_name ASC");
                cursorWhatsAppB = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, "account_type='com.whatsapp.w4b'and Deleted='0'", null, "display_name ASC");
                totalWhatsAppContact = cursorWhatsApp.getCount();
                totalWhatsAppContactB = cursorWhatsAppB.getCount();
                totalContact = cursorLocal.getCount();
                return null;
            } catch (Exception e) {
                return null;
            }
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            SharedPreferenceClass.setBoolean(ActivityContactExport.this, "firstLoad", false);
            if (cursorWhatsApp == null && cursorWhatsAppB == null) {
                totalWhatsAppC = 0;
            } else if (cursorWhatsApp == null) {
                totalWhatsAppContact = 0;
            } else if (cursorWhatsAppB == null) {
                totalWhatsAppContactB = 0;
            }

            if (totalWhatsAppContact > totalWhatsAppContactB) {
                totalWhatsAppC = totalWhatsAppContact;
                SharedPreferenceClass.setString(ActivityContactExport.this, "whatsvalue", "whatsapp");
            } else if (totalWhatsAppContact < totalWhatsAppContactB) {
                totalWhatsAppC = totalWhatsAppContactB;
                SharedPreferenceClass.setString(ActivityContactExport.this, "whatsvalue", "whatsappB");
            } else {
                if (totalWhatsAppContact > 0) {
                    totalWhatsAppC = totalWhatsAppContact;
                    SharedPreferenceClass.setString(ActivityContactExport.this, "whatsvalue", "whatsapp");
                } else if (totalWhatsAppContactB > 0) {
                    totalWhatsAppC = totalWhatsAppContactB;
                    SharedPreferenceClass.setString(ActivityContactExport.this, "whatsvalue", "whatsappB");
                }
            }
            txtWhatsAppContact.setVisibility(View.VISIBLE);
            txtWhatsAppContact.setText(Html.fromHtml(getString(R.string.whatsapp_contact) + " : " + "<strong>" + totalWhatsAppC + "</strong>"));
            float f = 0.0f;
            if (totalWhatsAppC == 0) {
                mSeekArc.setProgress((int) 0.0f);
                txtTotalContact.setText("0");
            } else if (totalContact != 0) {
                txtTotalContact.setText(Html.fromHtml(getString(R.string.total_contact) + " : " + "<strong>" + totalContact + "</strong>"));
                cursorLocal.close();
                f = (float) ((totalWhatsAppC * 100) / totalContact);
                mSeekArc.setProgress((int) f);
            } else {
                txtTotalContact.setText("0");
                cursorLocal.close();
                mSeekArc.setProgress((int) 0.0f);
            }

            mSeekArcProgress.setText(Html.fromHtml((int) f + "%"));
        }
    }
}
