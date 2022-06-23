package com.whatscan.toolkit.forwa.ContactExport;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.ContactExport.Helper.HtmlUtility;
import com.whatscan.toolkit.forwa.ContactExport.Helper.JsonUtility;
import com.whatscan.toolkit.forwa.ContactExport.Helper.PdfUtility;
import com.whatscan.toolkit.forwa.ContactExport.Helper.VcfUtility;
import com.whatscan.toolkit.forwa.ContactExport.Helper.XmlUtility;
import com.whatscan.toolkit.forwa.GetSet.ContactModelAuto;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityExport extends AppCompatActivity {
    public boolean backround = false;
    public ImageView la_back;
    public TextView tv_toolbar, mTextViewStoragepath, textview_contact_support_title;
    public TextInputLayout mtf_UserName;
    public EditText mEditTextExportFileName;
    public Button button_save_file;
    public File file;
    public ProgressBar progress_bar;
    public String fileName, allContact = "";
    public Cursor cursorWhatsApp, cursorWhatsAppB;
    public int totalWhatsAppContactB, whatsappContactCount = 0;
    public List<ContactModelAuto> contacts = new ArrayList<>();
    public AlertDialog alertExitDialog;
    public RelativeLayout rlContactExp;
    public View ic_include;
    public CardView cardOne;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityExport.this);
        setContentView(R.layout.activity_export);

        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        textview_contact_support_title = findViewById(R.id.textview_contact_support_title);
        mEditTextExportFileName = findViewById(R.id.edittext_export_contact);
        button_save_file = findViewById(R.id.button_save_file);
        rlContactExp = findViewById(R.id.rlContactExp);
        ic_include = findViewById(R.id.ic_include);
        cardOne = findViewById(R.id.cardOne);
        mtf_UserName = findViewById(R.id.mtf_UserName);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.view_all_contacts) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlContactExp.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            cardOne.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            textview_contact_support_title.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            mEditTextExportFileName.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            mtf_UserName.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
        }

        button_save_file.setOnClickListener(v -> {
            if (!validateString(mEditTextExportFileName.getText().toString())) {
                Toast.makeText(ActivityExport.this, "Please enter valid file name", Toast.LENGTH_SHORT).show();
            } else {
                hideKeybord();
                startprocess();
            }
        });
    }

    public boolean validateString(String str) {
        return str != null && !str.isEmpty() && str.length() > 0 && !str.equals("null");
    }

    @SuppressLint("WrongConstant")
    private void hideKeybord() {
        ((InputMethodManager) getApplicationContext().getSystemService("input_method")).hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 2);
    }

    private void startprocess() {
        try {
            fileName = mEditTextExportFileName.getText().toString().trim();

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityExport.this);
            View inflate = LayoutInflater.from(ActivityExport.this).inflate(R.layout.dialog_export_choose_type, null);
            bottomSheetDialog.setContentView(inflate);

            LinearLayout llExport = inflate.findViewById(R.id.llExport);
            TextView txtCSV = inflate.findViewById(R.id.txtCSV);
            TextView txtHTML = inflate.findViewById(R.id.txtHTML);
            TextView txtPDF = inflate.findViewById(R.id.txtPDF);
            TextView txtVCF = inflate.findViewById(R.id.txtVCF);
            TextView txtJSON = inflate.findViewById(R.id.txtJSON);
            TextView txtXML = inflate.findViewById(R.id.txtXML);
            TextView txtTXT = inflate.findViewById(R.id.txtTXT);
            TextView txtCancel = inflate.findViewById(R.id.txtCancel);
            TextView txtOne = inflate.findViewById(R.id.txtOne);

            if (Preference.getBooleanTheme(false)) {
                llExport.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
                txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtCSV.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtHTML.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtPDF.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtVCF.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtJSON.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtXML.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                txtTXT.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            }

            txtCSV.setOnClickListener(v -> {
                new LoadContactClass(".csv").execute();
                bottomSheetDialog.dismiss();
            });

            txtHTML.setOnClickListener(v -> {
                new LoadContactTypes(1).execute();
                bottomSheetDialog.dismiss();
            });

            txtPDF.setOnClickListener(v -> {
                new LoadContactTypes(2).execute();
                bottomSheetDialog.dismiss();
            });

            txtVCF.setOnClickListener(v -> {
                new LoadContactTypes(3).execute();
                bottomSheetDialog.dismiss();
            });

            txtJSON.setOnClickListener(v -> {
                new LoadContactTypes(4).execute();
                bottomSheetDialog.dismiss();
            });

            txtXML.setOnClickListener(v -> {
                new LoadContactTypes(5).execute();
                bottomSheetDialog.dismiss();
            });

            txtTXT.setOnClickListener(v -> {
                new LoadContactClass(".txt").execute();
                bottomSheetDialog.dismiss();
            });

            txtCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

            bottomSheetDialog.show();
        } catch (Exception ignored) {
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

    @Override
    public void onBackPressed() {
        finish();
    }

    public void createFile(String str, String str2) {
        File file2 = new File(Environment.getExternalStorageDirectory().toString() + "/" + getString(R.string.app_directory) + "/WhatsApp Contact Export");
        if (!file2.exists()) {
            try {
                file2.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File file3 = new File(Environment.getExternalStorageDirectory().toString() + "/" + getString(R.string.app_directory) + "/WhatsApp Contact Export/" + str + str2);
        file = file3;
        if (!file3.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (file.exists()) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write(allContact);
                bufferedWriter.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        new Handler().postDelayed(() -> {
            if (backround) {
                progress_bar.setVisibility(View.GONE);
                mTextViewStoragepath.setText(Html.fromHtml("<strong>" + getString(R.string.export_file_location) + " :\n" + "</strong>" + file.getAbsolutePath()));
                return;
            }
            backround = false;
        }, 2000);
    }

    public void showProgressDialog() {
        try {
            View inflate = View.inflate(this, R.layout.dialog_contact_export, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(inflate);

            LinearLayout llContactE = inflate.findViewById(R.id.llContactE);
            mTextViewStoragepath = inflate.findViewById(R.id.path_textview);
            progress_bar = inflate.findViewById(R.id.progress_bar);
            TextView buttonExit = inflate.findViewById(R.id.button_yes);

            if (Preference.getBooleanTheme(false)){
                llContactE.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
                mTextViewStoragepath.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            }

            buttonExit.setOnClickListener(view -> {
                alertExitDialog.dismiss();
                finish();
            });
            AlertDialog create = builder.create();
            alertExitDialog = create;
            create.setCancelable(false);
            alertExitDialog.show();
            backround = true;
        } catch (Exception ignored) {

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadContactClass extends AsyncTask<Void, Integer, Void> {
        ProgressDialog pDialog;
        int f462i = 0, percentage = 0;
        String fileExt;

        public LoadContactClass(String str) {
            fileExt = str;
        }

        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityExport.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            cursorWhatsApp = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, "account_type='com.whatsapp'and Deleted='0'", null, "display_name ASC");
            cursorWhatsAppB = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, "account_type='com.whatsapp.w4b' and Deleted='0'", null, "display_name ASC");

            if (cursorWhatsApp == null) {
                whatsappContactCount = 0;
            } else {
                whatsappContactCount = cursorWhatsApp.getCount();
            }

            if (cursorWhatsAppB == null) {
                totalWhatsAppContactB = 0;
            } else {
                totalWhatsAppContactB = cursorWhatsAppB.getCount();
            }

            if (cursorWhatsApp != null && whatsappContactCount > totalWhatsAppContactB) {
                whatsappContactCount = cursorWhatsApp.getCount();
            } else if (cursorWhatsAppB != null && whatsappContactCount < totalWhatsAppContactB) {
                whatsappContactCount = cursorWhatsAppB.getCount();
                cursorWhatsApp = cursorWhatsAppB;
            } else if (whatsappContactCount > 0) {
                whatsappContactCount = cursorWhatsApp.getCount();
            } else if (totalWhatsAppContactB > 0) {
                whatsappContactCount = cursorWhatsAppB.getCount();
            } else {
                whatsappContactCount = 0;
            }
        }

        public Void doInBackground(Void... voidArr) {
            try {
                f462i = 0;
                allContact = "";
                if (cursorWhatsApp != null) {
                    cursorWhatsApp.moveToFirst();
                    if (whatsappContactCount != 0) {
                        do {
                            String str = cursorWhatsApp.getString(cursorWhatsApp.getColumnIndex("display_name")) + "," + cursorWhatsApp.getString(cursorWhatsApp.getColumnIndex("sync1")).replace("@s.whatsapp.net", "");
                            allContact = allContact + IOUtils.LINE_SEPARATOR_UNIX + str;
                            f462i = f462i + 1;
                            if (whatsappContactCount > 0) {
                                percentage = (f462i * 100) / whatsappContactCount;

                            }
                        } while (cursorWhatsApp.moveToNext());
                    }
                }
            } catch (Exception ignored) {
            }
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (whatsappContactCount == 0) {
                Toast.makeText(ActivityExport.this, "You have no whatsapp contact to export", Toast.LENGTH_SHORT).show();
                return;
            }

            showProgressDialog();

            createFile(fileName, fileExt);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadContactTypes extends AsyncTask<Void, Integer, Void> {
        int type, f463i = 0, percentage = 0;
        ProgressDialog pDialog;

        public LoadContactTypes(int i) {
            type = i;
        }

        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ActivityExport.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            cursorWhatsApp = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, "account_type='com.whatsapp'and Deleted='0'", null, "display_name ASC");
            cursorWhatsAppB = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, null, "account_type='com.whatsapp.w4b' and Deleted='0'", null, "display_name ASC");

            if (cursorWhatsApp == null) {
                whatsappContactCount = 0;
            } else {
                whatsappContactCount = cursorWhatsApp.getCount();
            }

            if (cursorWhatsAppB == null) {
                totalWhatsAppContactB = 0;
            } else {
                totalWhatsAppContactB = cursorWhatsAppB.getCount();
            }

            if (cursorWhatsApp != null && whatsappContactCount > totalWhatsAppContactB) {
                whatsappContactCount = cursorWhatsApp.getCount();
            } else if (cursorWhatsAppB != null && whatsappContactCount < totalWhatsAppContactB) {
                whatsappContactCount = cursorWhatsAppB.getCount();
                cursorWhatsApp = cursorWhatsAppB;
            } else if (whatsappContactCount > 0) {
                whatsappContactCount = cursorWhatsApp.getCount();
            } else if (totalWhatsAppContactB > 0) {
                whatsappContactCount = cursorWhatsAppB.getCount();
            } else {
                whatsappContactCount = 0;
            }
        }

        public Void doInBackground(Void... voidArr) {
            try {
                f463i = 0;
                allContact = "";
                if (cursorWhatsApp != null) {
                    cursorWhatsApp.moveToFirst();
                    if (whatsappContactCount != 0) {
                        do {
                            String string = cursorWhatsApp.getString(cursorWhatsApp.getColumnIndex("display_name"));
                            String replace = cursorWhatsApp.getString(cursorWhatsApp.getColumnIndex("sync1")).replace("@s.whatsapp.net", "");

                            allContact = allContact + IOUtils.LINE_SEPARATOR_UNIX + (string + "," + replace);
                            contacts.add(new ContactModelAuto(string, replace));
                            f463i = f463i + 1;
                            if (whatsappContactCount > 0) {
                                percentage = (f463i * 100) / whatsappContactCount;
                            }
                        } while (cursorWhatsApp.moveToNext());
                    }
                }
            } catch (Exception ignored) {
            }
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            int i = type;
            if (i == 1) {
                HtmlUtility.exportContacts(ActivityExport.this, contacts, fileName, false, null);
            } else if (i == 2) {
                PdfUtility.exportContacts(ActivityExport.this, contacts, fileName, false, null);
            } else if (i == 3) {
                VcfUtility.exportContacts(ActivityExport.this, contacts, fileName, false, null);
            } else if (i == 4) {
                JsonUtility.exportContacts(ActivityExport.this, contacts, fileName, false, null);
            } else if (i == 5) {
                XmlUtility.exportContacts(ActivityExport.this, contacts, fileName, false, null);
            }

            if (whatsappContactCount == 0) {
                Toast.makeText(ActivityExport.this, "You have no whatsapp contact to export", Toast.LENGTH_SHORT).show();
                return;
            }

            showProgressDialog();

            new Handler().postDelayed(() -> {
                int i1 = type;
                String str = Environment.getExternalStorageDirectory().toString() + "/" + getString(R.string.app_directory) + "WhatsApp Contact Export/" + fileName + (i1 != 1 ? i1 != 2 ? i1 != 3 ? i1 != 4 ? i1 != 5 ? "" : ".xml" : ".json" : ".vcf" : ".pdf" : ".html");
                if (backround) {
                    progress_bar.setVisibility(View.GONE);
                    mTextViewStoragepath.setText(Html.fromHtml("<strong>" + getString(R.string.export_file_location) + " :\n" + "</strong>" + str));
                    return;
                }
                backround = false;
            }, 3000);
        }
    }
}