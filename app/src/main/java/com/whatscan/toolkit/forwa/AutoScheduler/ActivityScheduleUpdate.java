package com.whatscan.toolkit.forwa.AutoScheduler;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.DataBaseHelper.DBHelperScheduler;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.AlarmReceiver;
import com.whatscan.toolkit.forwa.Service.AlarmService;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ActivityScheduleUpdate extends AppCompatActivity {
    public String id, contacts, date, message, d, h, m, mn, msg_, name, requestcode, time;
    public int childcount;
    public int contactcount = 0;
    public int datecount = 0;
    public int fragment_id;
    public int isdialogopen = 0;
    public int timecount = 0;
    public ImageView delete;
    public TextView tv_toolbar;
    public ImageView la_back;
    public LinearLayout contact_layout;
    public AlertDialog.Builder builder;
    public ChipGroup chipGroup;
    public EditText et_date;
    public EditText et_hint;
    public EditText et_msg;
    public EditText et_time;
    public DBHelperScheduler helper;
    public RelativeLayout mainlayout;
    public AppCompatButton update;
    public ArrayList<String> update_contact_list;
    public View ic_include;
    public boolean installed;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        setContentView(R.layout.activity_schedule_update);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityScheduleUpdate.this, ll_banner);

        tv_toolbar = findViewById(R.id.tv_toolbar);
        update = findViewById(R.id.btn_updateevent);
        contact_layout = findViewById(R.id.et_updatecontact);
        chipGroup = findViewById(R.id.chipgroup);
        mainlayout = findViewById(R.id.main_layout);
        la_back = findViewById(R.id.la_back);
        delete = findViewById(R.id.la_info);
        et_date = findViewById(R.id.et_updatedate);
        et_hint = findViewById(R.id.et_hint);
        et_time = findViewById(R.id.et_updatetime);
        et_msg = findViewById(R.id.et_updateMessage);
        ic_include = findViewById(R.id.ic_include);

        delete.setVisibility(View.VISIBLE);
        delete.setImageResource(R.drawable.ic_w_delete);

        tv_toolbar.setText(Html.fromHtml("<small>" + "Update Scheduler" + "</small>"));

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

        if (chipGroup.getChildCount() == 0) {
            et_hint.setVisibility(View.VISIBLE);
        }

        et_date.setFocusable(false);
        et_hint.setFocusable(false);
        et_hint.setLongClickable(false);
        et_time.setFocusable(false);
        et_msg.setCursorVisible(false);
        helper = new DBHelperScheduler(this);

        Intent intent = getIntent();
        ActivityEventSchedule.packagename = intent.getStringExtra("PackageName");
        fragment_id = intent.getIntExtra("fragmentcode", 0);
        id = intent.getStringExtra(DBHelperScheduler.EVENTS_COLUMN_ID);
        name = intent.getStringExtra("name");

        if (name.contains(",")) {
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(name.split(",")));
            update_contact_list = arrayList;
            childcount = arrayList.size();
        } else {
            update_contact_list = new ArrayList<>();
            update_contact_list.add(name);
            childcount = update_contact_list.size();
        }

        if (ActivityEventSchedule.contactlist != null) {
            ActivityEventSchedule.contactlist.clear();
        }

        if (update_contact_list.size() > 1) {
            for (int i = 0; i < update_contact_list.size(); i++) {
                final Chip chip = new Chip(this);
                chip.setText(update_contact_list.get(i));
                chip.setBackgroundColor(getResources().getColor(R.color.chipback));
                chip.setTextColor(getResources().getColor(R.color.colorBlack));
                chip.setCloseIconTintResource(R.color.colorAccent);
                chip.setTextSize(2, 14.0f);
                chip.setChipCornerRadius(TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics()));
                chip.setHeight(28);
                chip.setCloseIcon(AppCompatResources.getDrawable(ActivityScheduleUpdate.this, R.drawable.ic_close2));
                chip.setCloseIconVisible(true);
                chip.setCheckable(false);
                chip.setOnCloseIconClickListener(v -> removeview(chip));
                chipGroup.addView(chip);

                if (et_hint.getVisibility() == View.VISIBLE) {
                    et_hint.setVisibility(View.GONE);
                }
                chipGroup.setVisibility(View.VISIBLE);
            }
        } else if (update_contact_list.size() == 1) {
            for (int i2 = 0; i2 < update_contact_list.size(); i2++) {
                final Chip chip2 = new Chip(this);
                chip2.setText(update_contact_list.get(i2));
                chip2.setBackgroundColor(getResources().getColor(R.color.chipback));
                chip2.setTextColor(getResources().getColor(R.color.colorBlack));
                chip2.setCloseIconTintResource(R.color.colorAccent);
                chip2.setTextSize(2, 14.0f);
                chip2.setChipCornerRadius(TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics()));
                chip2.setHeight(28);
                chip2.setCloseIcon(AppCompatResources.getDrawable(ActivityScheduleUpdate.this, R.drawable.ic_close2));
                chip2.setCloseIconVisible(true);
                chip2.setCheckable(false);
                chip2.setOnCloseIconClickListener(v -> removeview(chip2));
                chipGroup.addView(chip2);

                if (et_hint.getVisibility() == View.VISIBLE) {
                    et_hint.setVisibility(View.GONE);
                }
                chipGroup.setVisibility(View.VISIBLE);
            }
        }

        update_contact_list.clear();
        String stringExtra2 = intent.getStringExtra(DBHelperScheduler.EVENTS_COLUMN_MESSAGE);
        message = stringExtra2;
        msg_ = stringExtra2;
        date = intent.getStringExtra(DBHelperScheduler.EVENTS_COLUMN_DATE);
        time = intent.getStringExtra(DBHelperScheduler.EVENTS_COLUMN_TIME);
        requestcode = intent.getStringExtra(DBHelperScheduler.EVENTS_COLUMN_REQUESTCODE);
        ActivityEventSchedule.requestcode = Integer.parseInt(requestcode);
        ActivityEventSchedule.mDay = intent.getStringExtra("dayofweek");
        installed = isAppInstalled(ActivityEventSchedule.packagename);
        et_date.setText(date);
        et_time.setText(time);
        et_msg.setText(message);
        String[] split = date.split("-");
        d = split[0];
        ActivityEventSchedule.date = d;
        mn = split[1];
        ActivityEventSchedule.mnth = Integer.parseInt(split[1]) - 1;
        ActivityEventSchedule.yr = split[2];
        String[] split2 = time.split(":");
        h = split2[0];
        ActivityEventSchedule.hr = h;
        m = split2[1];
        ActivityEventSchedule.min = m;
        onclickListners();
    }

    public void onRestart() {
        super.onRestart();
        if (ActivityAllEventScheduler.sendcontactname == null) {
            return;
        }
        if (chipGroup.getChildCount() >= 1) {
            if (chipGroup.getChildCount() < 5) {
                ArrayList<String> arrayList = new ArrayList<>();
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    Chip childAt = (Chip) chipGroup.getChildAt(i);
                    childAt.setBackgroundColor(getResources().getColor(R.color.chipback));
                    arrayList.add(childAt.getText().toString());
                }
                if (arrayList.contains(ActivityAllEventScheduler.sendcontactname)) {
                    Toast.makeText(getApplicationContext(), "These contact is already selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Chip chip = new Chip(this);
                chip.setText(ActivityAllEventScheduler.sendcontactname);
                chip.setBackgroundColor(getResources().getColor(R.color.chipback));
                chip.setTextColor(getResources().getColor(R.color.colorBlack));
                chip.setCloseIconTintResource(R.color.colorAccent);
                chip.setTextSize(2, 14.0f);
                chip.setChipCornerRadius(TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics()));
                chip.setHeight(28);
                chip.setCloseIcon(AppCompatResources.getDrawable(ActivityScheduleUpdate.this, R.drawable.ic_close2));
                chip.setCloseIconVisible(true);
                chip.setCheckable(false);
                chip.setOnCloseIconClickListener(v -> removeview(chip));

                chipGroup.addView(chip);
                if (et_hint.getVisibility() == View.VISIBLE) {
                    et_hint.setVisibility(View.GONE);
                }
                chipGroup.setVisibility(View.VISIBLE);
                ActivityAllEventScheduler.sendcontactname = null;
            } else {
                Toast.makeText(getApplicationContext(), "Whatsapp not allowing more than 5 contacts at a time.", Toast.LENGTH_SHORT).show();
            }
        } else if (chipGroup.getChildCount() == 0) {
            final Chip chip2 = new Chip(this);
            chip2.setText(ActivityAllEventScheduler.sendcontactname);
            chip2.setBackgroundColor(getResources().getColor(R.color.chipback));
            chip2.setTextColor(getResources().getColor(R.color.colorBlack));
            chip2.setCloseIconTintResource(R.color.colorAccent);
            chip2.setTextSize(2, 14.0f);
            chip2.setChipCornerRadius(TypedValue.applyDimension(1, 5.0f, getResources().getDisplayMetrics()));
            chip2.setHeight(28);
            chip2.setCloseIcon(AppCompatResources.getDrawable(ActivityScheduleUpdate.this, R.drawable.ic_close2));
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
            et_hint.setVisibility(View.GONE);
        }
    }

    public void hideKeyboard(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void onclickListners() {
        et_msg.setOnClickListener(view -> et_msg.setCursorVisible(true));

        la_back.setOnClickListener(view -> {
            if (chipGroup.getChildCount() != childcount || !et_msg.getText().toString().equals(msg_)) {
                discardDialog();
            } else {
                finish();
            }
        });

        delete.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityScheduleUpdate.this);
            builder.setCancelable(false);
            builder.setTitle("Delete Scheduler");
            builder.setMessage("Are you sure want to delete this Scheduler?");

            builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), ActivityEventSchedule.requestcode, new Intent(ActivityScheduleUpdate.this, AlarmReceiver.class), 0);
                getApplicationContext();
                ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).cancel(broadcast);
                helper = new DBHelperScheduler(ActivityScheduleUpdate.this);
                helper.deleteContact(Integer.parseInt(id));
                finish();
            }).setNegativeButton("No", (dialogInterface, i) -> builder.setCancelable(false));
            AlertDialog create = builder.create();
            create.show();
            create.getButton(-1).setTextColor(getResources().getColor(R.color.colorAccent));
            create.getButton(-2).setTextColor(getResources().getColor(R.color.colorAccent));
        });

        contact_layout.setOnClickListener(view -> {
            et_msg.clearFocus();
            hideKeyboard(view);
            if (chipGroup.getChildCount() <= 0) {
                Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(ActivityEventSchedule.packagename);
                if (launchIntentForPackage != null) {
                    ActivityAllEventScheduler.contactname = " ";
                    startActivity(launchIntentForPackage);
                } else {
                    Toast.makeText(getApplicationContext(), "There is no package available in android", Toast.LENGTH_SHORT).show();
                }
                contactcount++;
            }
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
            try {
                et_msg.clearFocus();
                hideKeyboard(view);
                Calendar.getInstance();

                @SuppressLint("SimpleDateFormat") DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityScheduleUpdate.this, R.style.DialogTheme, (datePicker, i, i2, i3) -> {
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
                }, Integer.parseInt(ActivityEventSchedule.yr), ActivityEventSchedule.mnth, Integer.parseInt(d));
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
            try {
                et_msg.clearFocus();
                hideKeyboard(view);
                Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityScheduleUpdate.this, R.style.DialogTheme, (timePicker, i, i2) -> {
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
                }, Integer.parseInt(h), Integer.parseInt(m), false);
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

        update.setOnClickListener(view -> {
            hideKeyboard(view);
            if (chipGroup.getChildCount() > 0) {
                updateEvent();
                return;
            }
            Toast.makeText(getApplicationContext(), "Contacts should not be empty.", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateEvent() {
        if (et_msg.getText().toString().trim() == null || et_msg.getText().toString().trim().isEmpty() || et_msg.getText().toString().trim().equals("")) {
            et_msg.setFocusableInTouchMode(true);
            et_msg.setError("Message should not be Empty");
            et_msg.requestFocus();
            return;
        }
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance.set(2, ActivityEventSchedule.mnth);
        instance.set(1, Integer.parseInt(ActivityEventSchedule.yr));
        instance.set(5, Integer.parseInt(ActivityEventSchedule.date));
        instance.set(11, Integer.parseInt(ActivityEventSchedule.hr));
        instance.set(12, Integer.parseInt(ActivityEventSchedule.min));
        instance.set(13, 0);
        instance.set(14, 0);
        if (instance.getTime().before(instance2.getTime())) {
            Toast.makeText(getApplicationContext(), "You may not select the past date &amp; time.", Toast.LENGTH_SHORT).show();
            return;
        }
        ActivityEventSchedule.contactlist = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip childAt = (Chip) chipGroup.getChildAt(i);
            childAt.setBackgroundColor(Color.parseColor("#ebebeb"));
            ActivityEventSchedule.contactlist.add(childAt.getText().toString());
        }
        ActivityEventSchedule.msg = et_msg.getText().toString() + "    ";
        ActivityEventSchedule.EventToken = 1;
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra("contactname", ActivityEventSchedule.contactlist);
        intent.putExtra(DBHelperScheduler.EVENTS_COLUMN_MESSAGE, ActivityEventSchedule.msg);
        startService(intent);
        stopService(intent);
        for (int i2 = 0; i2 < ActivityEventSchedule.contactlist.size(); i2++) {
            String str = contacts;
            if (str == null || str.isEmpty()) {
                contacts = ActivityEventSchedule.contactlist.get(i2);
            } else {
                contacts += ", " + ActivityEventSchedule.contactlist.get(i2);
            }
        }
        helper.updateContact(Integer.parseInt(id), contacts, ActivityEventSchedule.mDay, d + "-" + mn + "-" + ActivityEventSchedule.yr, h + ":" + m, ActivityEventSchedule.msg.trim(), Integer.parseInt(requestcode), ActivityEventSchedule.packagename, "pending");
        contacts = null;
        finish();
    }

    private boolean isAppInstalled(String str) {
        try {
            getPackageManager().getPackageInfo(str, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (chipGroup.getChildCount() != childcount || !et_msg.getText().toString().equals(msg_)) {
            discardDialog();
        } else {
            finish();
        }
    }

    private void discardDialog() {
        if (chipGroup.getChildCount() > 0 || !et_msg.getText().toString().isEmpty()) {
            final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setCancelable(false);
            builder2.setMessage("Your changes have not been saved");

            builder2.setPositiveButton(getString(R.string.save), (dialogInterface, i) -> {
                if (chipGroup.getChildCount() > 0) {
                    updateEvent();
                } else {
                    Toast.makeText(getApplicationContext(), "Contacts should not be empty.", Toast.LENGTH_SHORT).show();
                }
                isdialogopen = 0;
            }).setNegativeButton("Discard", (dialogInterface, i) -> {
                builder2.setCancelable(true);
                isdialogopen = 0;
                finish();
            });
            AlertDialog create = builder2.create();
            if (isdialogopen == 0) {
                create.show();
                isdialogopen = 1;
                create.getButton(-1).setTextColor(getResources().getColor(R.color.colorAccent));
                create.getButton(-2).setTextColor(getResources().getColor(R.color.colorAccent));
            }
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