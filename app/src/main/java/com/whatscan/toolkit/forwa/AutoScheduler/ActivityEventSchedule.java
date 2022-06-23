package com.whatscan.toolkit.forwa.AutoScheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.DataBaseHelper.DBHelperScheduler;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.AlarmService;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityEventSchedule extends AppCompatActivity {
    public static int EventToken = 0;
    public static int mnth;
    public static int requestcode;
    public static ArrayList<String> contactlist;
    public static String date, hr, mDay, min, yr, msg, name, packagename;
    public static DBHelperScheduler mydbhelper;
    public int contactcount = 0;
    public int datecount = 0;
    public int timecount = 0;
    public int isdialogopen = 0;
    public int fragment_id;
    public ImageView la_back;
    public AlertDialog.Builder builder;
    public Calendar cal, calendar;
    public ChipGroup chipGroup;
    public LinearLayout contact_layout;
    public Button create, createandnew;
    public EditText et_date, et_hint, et_msg, et_time;
    public RelativeLayout mainlayout;
    public String h, m, d, mn, contacts;
    public TextView tv_toolbar;
    public View ic_include;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        setContentView(R.layout.activity_shedule_add);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityEventSchedule.this, ll_banner);

        chipGroup = findViewById(R.id.chipgroup);
        et_hint = findViewById(R.id.et_hint);
        contact_layout = findViewById(R.id.et_contact);
        et_date = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time);
        create = findViewById(R.id.btn_createevent);
        createandnew = findViewById(R.id.btn_saveandnew);
        mainlayout = findViewById(R.id.main_layout);
        et_msg = findViewById(R.id.et_Message);
        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + "Add Scheduler" + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            mainlayout.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            contact_layout.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));
            et_hint.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            et_date.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));
            et_time.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));
            et_msg.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));
            et_hint.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_date.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_time.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_msg.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_date.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_time.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            et_msg.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        et_hint.setLongClickable(false);
        et_hint.setFocusable(false);

        if (chipGroup.getChildCount() == 0) {
            et_hint.setVisibility(View.VISIBLE);
        } else {
            et_hint.setVisibility(View.GONE);
        }

        contactlist = new ArrayList<>();

        Intent intent = getIntent();
        packagename = intent.getStringExtra("PackageName");
        fragment_id = intent.getIntExtra("fragmentcode", 0);
        System.out.println("PackageName     " + packagename);

        et_msg.setCursorVisible(false);
        contact_layout.setFocusable(false);
        et_date.setFocusable(false);
        et_time.setFocusable(false);

        mydbhelper = new DBHelperScheduler(this);

        calendar = Calendar.getInstance();
        cal = Calendar.getInstance();

        yr = new SimpleDateFormat("yyyy").format(calendar.getTime());
        mnth = Integer.parseInt(new SimpleDateFormat("MM").format(calendar.getTime())) - 1;

        mn = String.valueOf(mnth);
        date = new SimpleDateFormat("dd").format(calendar.getTime());
        d = date;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        hr = simpleDateFormat.format(calendar.getTime());
        h = hr;

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
        if (Integer.parseInt(simpleDateFormat2.format(calendar.getTime())) > 57) {
            cal.add(10, 1);
            hr = simpleDateFormat.format(cal.getTime());
            h = hr;
        }

        cal.add(12, 2);
        int parseInt2 = Integer.parseInt(simpleDateFormat2.format(cal.getTime()));
        if (parseInt2 < 10) {
            min = "0" + parseInt2;
        } else {
            min = String.valueOf(parseInt2);
        }
        m = min;

        mDay = new SimpleDateFormat("EEEE").format(calendar.getTime());
        new SimpleDateFormat("hh:mm");
        et_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()));
        et_time.setText(hr + ":" + min);

        isAppInstalled(packagename);

        la_back.setOnClickListener(view -> {
            if (chipGroup.getChildCount() > 0 || !et_msg.getText().toString().isEmpty()) {
                discardDialog();
            } else {
                finish();
            }
        });

        createandnew.setOnClickListener(view -> {
            hideKeyboard(view);
            if (chipGroup.getChildCount() > 0) {
                createandaddnew();
                return;
            }
            Toast.makeText(getApplicationContext(), "Contact should not be empty.", Toast.LENGTH_SHORT).show();
        });

        this.create.setOnClickListener(view -> {
            hideKeyboard(view);
            if (chipGroup.getChildCount() > 0) {
                createEvent();
                return;
            }
            Toast.makeText(getApplicationContext(), "Contact should not be empty.", Toast.LENGTH_SHORT).show();
        });

        setOnclicklistners();
    }

    public void onRestart() {
        super.onRestart();
        if (ActivityAllEventScheduler.sendcontactname == null) {
            return;
        }
        if (chipGroup.getChildCount() > 0) {
            if (chipGroup.getChildCount() < 5) {
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i < this.chipGroup.getChildCount(); i++) {
                    String strChip = ((Chip) chipGroup.getChildAt(i)).getText().toString();
                    arrayList.add(strChip);
                }

                if (arrayList.contains(ActivityAllEventScheduler.sendcontactname)) {
                    Toast.makeText(getApplicationContext(), "These contact is already selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Chip chip = new Chip(this);
                chip.setText(ActivityAllEventScheduler.sendcontactname);
                chip.setBackgroundColor(getResources().getColor(R.color.chipback));
                chip.setCloseIcon(AppCompatResources.getDrawable(ActivityEventSchedule.this, R.drawable.ic_close2));
                chip.setCloseIconTintResource(R.color.colorAccent);
                chip.setTextColor(getResources().getColor(R.color.colorBlack));
                chip.setTextSize(2, 14.0f);
                chip.setChipCornerRadius(TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics()));
                chip.setHeight(28);
                chip.setCloseIconVisible(true);
                chip.setCheckable(false);
                chip.setOnCloseIconClickListener(v -> removeview(chip));

                chipGroup.addView(chip);
                if (et_hint.getVisibility() == View.VISIBLE) {
                    et_hint.setVisibility(View.GONE);
                }
                chipGroup.setVisibility(View.VISIBLE);
                ActivityAllEventScheduler.sendcontactname = null;
                return;
            }

            Toast.makeText(getApplicationContext(), "Whatsapp not allowing more than 5 contacts at a time.", Toast.LENGTH_SHORT).show();

        } else if (this.chipGroup.getChildCount() == 0) {
            final Chip chip2 = new Chip(this);
            chip2.setText(ActivityAllEventScheduler.sendcontactname);
            chip2.setBackgroundColor(getResources().getColor(R.color.chipback));
            chip2.setCloseIcon(AppCompatResources.getDrawable(ActivityEventSchedule.this, R.drawable.ic_close2));
            chip2.setCloseIconTintResource((int) R.color.colorAccent);
            chip2.setTextColor(getResources().getColor(R.color.colorBlack));
            chip2.setTextSize(2, 14.0f);
            chip2.setChipCornerRadius(TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics()));
            chip2.setHeight(28);
            chip2.setCloseIconVisible(true);
            chip2.setCheckable(false);
            chip2.setOnCloseIconClickListener(v -> removeview(chip2));
            chipGroup.addView(chip2);

            if (et_hint.getVisibility() == View.VISIBLE) {
                et_hint.setVisibility(View.GONE);
            }
            chipGroup.setVisibility(View.VISIBLE);
            ActivityAllEventScheduler.sendcontactname = null;
        }
    }

    private void removeview(Chip chip) {
        chipGroup.removeView(chip);
        if (chipGroup.getChildCount() == 0) {
            et_hint.setVisibility(View.VISIBLE);
        }
    }

    private void createandaddnew() {
        EventToken = 0;
        if (et_msg.getText().toString().trim() == null || et_msg.getText().toString().trim().isEmpty() || et_msg.getText().toString().trim().equals("")) {
            et_msg.setFocusableInTouchMode(true);
            et_msg.setError("Message should not be empty");
            et_msg.requestFocus();
            return;
        }
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance.set(2, mnth);
        instance.set(1, Integer.parseInt(yr));
        instance.set(5, Integer.parseInt(date));
        instance.set(11, Integer.parseInt(hr));
        instance.set(12, Integer.parseInt(min));
        instance.set(13, 0);
        instance.set(14, 0);
        if (instance.getTime().before(instance2.getTime())) {
            Toast.makeText(getApplicationContext(), "You may not select the past date &amp; time.", Toast.LENGTH_SHORT).show();
            return;
        }
        msg = et_msg.getText().toString() + "    ";
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            String strChip = ((Chip) chipGroup.getChildAt(i)).getText().toString();
            contactlist.add(strChip);
        }
        if (contactlist.size() > 0) {
            Intent intent = new Intent(this, AlarmService.class);
            intent.putStringArrayListExtra("contactname", contactlist);
            intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_MESSAGE, msg);
            intent.putExtra("PackageName", packagename);
            startService(intent);
            stopService(intent);
            int i2 = mnth + 1;
            String valueOf = i2 < 10 ? "0" + i2 : String.valueOf(i2);
            if (contactlist != null) {
                for (int i3 = 0; i3 < contactlist.size(); i3++) {
                    String str = contacts;
                    if (str == null || str.isEmpty()) {
                        contacts = contactlist.get(i3);
                    } else {
                        contacts += ", " + contactlist.get(i3);
                    }
                }
                mydbhelper.insertContact(contacts, mDay, d + "-" + valueOf + "-" + yr, h + ":" + m, msg.trim(), requestcode, packagename, "pending");
                et_msg.getText().clear();
                contacts = null;
                finish();
            }
        }
    }

    private void createEvent() {
        EventToken = 0;
        if (et_msg.getText().toString().trim() == null || et_msg.getText().toString().trim().isEmpty() || et_msg.getText().toString().trim().equals("")) {
            et_msg.setFocusableInTouchMode(true);
            et_msg.setError("Message should not be empty");
            et_msg.requestFocus();
            return;
        }

        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance.set(2, mnth);
        instance.set(1, Integer.parseInt(yr));
        instance.set(5, Integer.parseInt(date));
        instance.set(11, Integer.parseInt(hr));
        instance.set(12, Integer.parseInt(min));
        instance.set(13, 0);
        instance.set(14, 0);
        if (instance.getTime().before(instance2.getTime())) {
            Toast.makeText(getApplicationContext(), "You may not select the past date &amp; time.", Toast.LENGTH_SHORT).show();
            return;
        }
        msg = et_msg.getText().toString() + "    ";
        int childCount = chipGroup.getChildCount();

        if (contactlist != null) {
            contactlist.clear();
        }
        for (int i = 0; i < childCount; i++) {
            String strChip = ((Chip) chipGroup.getChildAt(i)).getText().toString();
            contactlist.add(strChip);
        }
        if (contactlist.size() > 0) {
            Intent intent = new Intent(this, AlarmService.class);
            intent.putStringArrayListExtra("contactname", contactlist);
            intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_MESSAGE, msg);
            intent.putExtra("PackageName", packagename);
            startService(intent);
            stopService(intent);
            int i2 = mnth + 1;
            String valueOf = i2 < 10 ? "0" + i2 : String.valueOf(i2);
            for (int i3 = 0; i3 < contactlist.size(); i3++) {
                String str = contacts;
                if (str == null || str.isEmpty()) {
                    contacts = contactlist.get(i3);
                } else {
                    contacts += ", " + contactlist.get(i3);
                }
            }

            System.out.println("Value   " + requestcode);
            requestcode++;
            mydbhelper.insertContact(contacts, mDay, d + "-" + valueOf + "-" + yr, h + ":" + m, msg.trim(), requestcode, packagename, "pending");
            contacts = null;
            finish();
        }
    }

    private void setOnclicklistners() {
        et_msg.setOnClickListener(view -> {
            et_msg.setFocusable(true);
            et_msg.setCursorVisible(true);
        });

        contact_layout.setOnClickListener(view -> {
            et_msg.clearFocus();
            hideKeyboard(view);

            Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(ActivityEventSchedule.packagename);
            if (launchIntentForPackage != null) {
                ActivityAllEventScheduler.contactname = " ";
                startActivity(launchIntentForPackage);
            } else {
                Toast.makeText(getApplicationContext(), "There is no package available in android", Toast.LENGTH_SHORT).show();
            }
            contactcount++;
        });

        et_hint.setOnClickListener(view -> {
            et_msg.clearFocus();
            hideKeyboard(view);
            Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(ActivityEventSchedule.packagename);
            if (launchIntentForPackage != null) {
                ActivityAllEventScheduler.contactname = " ";
                startActivity(launchIntentForPackage);
            } else {
                Toast.makeText(getApplicationContext(), "There is no package available in android", Toast.LENGTH_SHORT).show();
            }
            contactcount++;
        });

        et_date.setOnClickListener(view -> {
            et_msg.clearFocus();
            hideKeyboard(view);
            try {
                Calendar instance = Calendar.getInstance();
                instance.set(2, ActivityEventSchedule.mnth);
                instance.set(1, Integer.parseInt(ActivityEventSchedule.yr));
                instance.set(5, Integer.parseInt(ActivityEventSchedule.date));

                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityEventSchedule.this, R.style.DialogTheme, (datePicker, i, i2, i3) -> {
                    ActivityEventSchedule.mDay = new SimpleDateFormat("EEEE").format(new Date(i, i2, i3 - 1));
                    if (i3 < 9) {
                        d = "0" + i3;
                    } else {
                        d = String.valueOf(i3);
                    }
                    if (i2 < 9) {
                        mn = "0" + (i2 + 1);
                    } else {
                        mn = String.valueOf(i2 + 1);
                    }
                    ActivityEventSchedule.date = d;
                    ActivityEventSchedule.mnth = i2;
                    ActivityEventSchedule.yr = String.valueOf(i);
                    et_date.setText(d + "/" + mn + "/" + i);
                }, instance.get(1), instance.get(2), instance.get(5));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                if (datecount == 0) {
                    datePickerDialog.show();
                }
                datecount++;

                datePickerDialog.setOnDismissListener(dialogInterface -> datecount = 0);
                datePickerDialog.getButton(-2).setTextColor(getResources().getColor(R.color.colorAccent));
                datePickerDialog.getButton(-1).setTextColor(getResources().getColor(R.color.colorAccent));
                datePickerDialog.getButton(-3).setTextColor(getResources().getColor(R.color.colorAccent));
            } catch (Exception unused) {
                runOnUiThread(() -> {
                });
            }
        });

        et_time.setOnClickListener(view -> {
            et_msg.clearFocus();
            hideKeyboard(view);
            try {
                Calendar instance = Calendar.getInstance();
                instance.set(11, Integer.parseInt(ActivityEventSchedule.hr));
                instance.set(12, Integer.parseInt(ActivityEventSchedule.min));
                TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityEventSchedule.this, R.style.DialogTheme, (timePicker, i, i2) -> {
                    if (i < 9) {
                        h = "0" + i;
                    } else {
                        h = String.valueOf(i);
                    }
                    if (i2 < 9) {
                        m = "0" + i2;
                    } else {
                        m = String.valueOf(i2);
                    }
                    ActivityEventSchedule.hr = String.valueOf(i);
                    ActivityEventSchedule.min = String.valueOf(i2);
                    et_time.setText(h + ":" + m);
                }, instance.get(11), instance.get(12), false);
                if (timecount == 0) {
                    timePickerDialog.show();
                }
                timecount++;

                timePickerDialog.setOnDismissListener(dialogInterface -> timecount = 0);
                timePickerDialog.getButton(-2).setTextColor(getResources().getColor(R.color.colorAccent));
                timePickerDialog.getButton(-1).setTextColor(getResources().getColor(R.color.colorAccent));
                timePickerDialog.getButton(-3).setTextColor(getResources().getColor(R.color.colorAccent));
            } catch (Exception unused) {
                runOnUiThread(() -> {
                });
            }
        });
    }

    private void discardDialog() {
        if (chipGroup.getChildCount() > 0 || !et_msg.getText().toString().isEmpty()) {
            final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setCancelable(false);
            builder2.setMessage("Your changes have not been saved");
            builder2.setPositiveButton(getString(R.string.save), (dialogInterface, i) -> {
                if (chipGroup.getChildCount() > 0) {
                    createEvent();
                } else {
                    Toast.makeText(getApplicationContext(), "Contacts should not be empty.", Toast.LENGTH_SHORT).show();
                }
                isdialogopen = 0;
            }).setNegativeButton("Discard", (dialogInterface, i) -> {
                builder2.setCancelable(true);
                isdialogopen = 0;
                finish();
            });
            AlertDialog create2 = builder2.create();
            if (isdialogopen == 0) {
                create2.show();
                isdialogopen = 1;
                create2.getButton(-1).setTextColor(getResources().getColor(R.color.colorAccent));
                create2.getButton(-2).setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
    }

    private void isAppInstalled(String str) {
        try {
            getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException unused) {
            unused.printStackTrace();
        }
    }

    public void hideKeyboard(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if (this.chipGroup.getChildCount() > 0 || !this.et_msg.getText().toString().isEmpty()) {
            discardDialog();
        } else {
            finish();
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
}