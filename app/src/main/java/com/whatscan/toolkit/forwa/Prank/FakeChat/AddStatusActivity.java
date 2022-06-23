package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.GetSet.StatusData;
import com.whatscan.toolkit.forwa.GetSet.otherStatusData;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddStatusActivity extends AppCompatActivity {
    TextView datetxt;
    String imagestr = "";
    CircleImageView profile_image;
    String profilestr = "";
    ImageView selectmedia;
    ImageView selectvideo;
    EditText textedt;
    TextView timetxt;
    List<StatusData> totallist = new ArrayList<>();
    List<otherStatusData> totalotherlist = new ArrayList<>();
    AutoCompleteTextView tvName;
    String videostr = "";
    EditText view;

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), AddStatusActivity.this);
        setContentView(R.layout.activity_add_status);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(AddStatusActivity.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        RelativeLayout rlStatus = findViewById(R.id.rlStatus);
        View ic_include = findViewById(R.id.ic_include);
        CoordinatorLayout layCoord = findViewById(R.id.layCoord);
        TextInputLayout text_input1 = findViewById(R.id.text_input1);
        TextInputLayout text_input2 = findViewById(R.id.text_input2);
        tvName = findViewById(R.id.tvName);
        tvName.setAdapter(new ArrayAdapter(this, 17367050, getnameData()));
        textedt = findViewById(R.id.text);
        view = findViewById(R.id.view);
        datetxt = findViewById(R.id.datetxt);
        timetxt = findViewById(R.id.timetxt);
        profile_image = findViewById(R.id.profile_image);
        selectmedia = findViewById(R.id.selectmedia);
        selectvideo = findViewById(R.id.selectvideo);

        if (Preference.getBooleanTheme(false)){
            setStatusBarTheme();
            rlStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tvName.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            datetxt.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            timetxt.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            layCoord.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            text_input2.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            tvName.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            datetxt.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            timetxt.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            tvName.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            datetxt.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            timetxt.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            textedt.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            view.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.add_status) + "</small>"));

        la_back.setOnClickListener(v -> onBackPressed());

        final DatePickerDialog.OnDateSetListener r2 = (datePicker, i, i2, i3) -> datetxt.setText(i3 + "/" + (i2 + 1) + "/" + i);

        final Calendar instance = Calendar.getInstance();
        datetxt.setOnClickListener(view -> new DatePickerDialog(AddStatusActivity.this, r2, instance.get(1), instance.get(2), instance.get(5)).show());

        findViewById(R.id.timetxt).setOnClickListener(view -> {
            Calendar instance1 = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddStatusActivity.this, (timePicker, i, i2) -> {
                String str;
                String str2;
                if (i < 10) {
                    str = "0" + i;
                } else {
                    str = "" + i;
                }
                if (i2 < 10) {
                    str2 = "0" + i2;
                } else {
                    str2 = "" + i2;
                }
                timetxt.setText(str + ":" + str2);
            }, instance1.get(11), instance1.get(12), true);
            timePickerDialog.setTitle("Select Time");
            timePickerDialog.show();
        });

        findViewById(R.id.profile_image).setOnClickListener(view -> {
            Intent intent = new Intent("android.intent.action.PICK");
            intent.setType("image/*");
            startActivityForResult(intent, 10);
        });

        findViewById(R.id.selectmedia).setOnClickListener(view -> {
            Intent intent = new Intent("android.intent.action.PICK");
            intent.setType("image/*");
            startActivityForResult(intent, 11);
        });

        findViewById(R.id.add).setOnClickListener(v -> {
            CharSequence charSequence;
            String str;
            String trim = tvName.getText().toString().trim();
            String trim2 = textedt.getText().toString().trim();
            String trim3 = timetxt.getText().toString().trim();
            String trim4 = datetxt.getText().toString().trim();
            String trim5 = view.getText().toString().trim();
            String str2 = profilestr;
            String str3 = imagestr;
            String str4 = videostr;
            if (trim.length() <= 0) {
                Toast.makeText(AddStatusActivity.this, "Please enter name...", Toast.LENGTH_SHORT).show();
                return;
            }
            String str5 = trim2.length() <= 0 ? "" : trim2;
            if (trim3.length() <= 0) {
                Toast.makeText(AddStatusActivity.this, "Please enter time...", Toast.LENGTH_SHORT).show();
            } else if (trim4.length() <= 0) {
                Toast.makeText(AddStatusActivity.this, "Please enter date...", Toast.LENGTH_SHORT).show();
            } else if (trim5.length() <= 0) {
                Toast.makeText(AddStatusActivity.this, "Please enter View detail...", Toast.LENGTH_SHORT).show();
            } else if (profilestr.length() < 0) {
                Toast.makeText(AddStatusActivity.this, "Please select profile...", Toast.LENGTH_SHORT).show();
            } else if (imagestr.length() > 0 || str4.length() > 0) {
                Toast.makeText(AddStatusActivity.this, "success", Toast.LENGTH_SHORT).show();
                if (trim.equals("My status")) {
                    List<StatusData> list = new Gson().fromJson(getApplicationContext().getSharedPreferences("data", 0).getString("mystatus", ""), new TypeToken<List<StatusData>>() {
                    }.getType());
                    if (list != null && list.size() > 0) {
                        totallist = list;
                    }

                    totallist.add(new StatusData(trim, trim3 + " " + trim4, str5, trim5, str2, str3, str4, 0));
                    SharedPreferences.Editor edit = getApplicationContext().getSharedPreferences("data", 0).edit();
                    edit.putString("mystatus", new Gson().toJson(totallist));
                    edit.apply();
                    Toast.makeText(AddStatusActivity.this, "success", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                List<otherStatusData> list3 = new Gson().fromJson(getApplicationContext().getSharedPreferences("data", 0).getString("other", ""), new TypeToken<List<otherStatusData>>() {
                }.getType());
                if (list3 != null && list3.size() > 0) {
                    totalotherlist = list3;
                }
                if (totalotherlist.size() > 0) {
                    int i = 0;
                    while (true) {
                        if (i < totalotherlist.size()) {
                            if (totalotherlist.get(i).getOthername().equals(trim)) {
                                str = "other";
                                List<StatusData> dataList = totalotherlist.get(i).getDataList();
                                dataList.add(new StatusData(trim, trim3 + " " + trim4, str5, trim5, str2, str3, str4, 0));
                                totalotherlist.get(i).setDataList(dataList);
                                break;
                            }
                            i++;
                            if (totalotherlist.size() == i) {
                                ArrayList<StatusData> arrayList = new ArrayList<>();
                                arrayList.add(new StatusData(trim, trim3 + " " + trim4, str5, trim5, str2, str3, str4, 0));
                                totalotherlist.add(new otherStatusData(trim, arrayList));
                                str = "other";
                                break;
                            }
                        } else {
                            str = "other";
                            break;
                        }
                    }
                    charSequence = "success";
                } else {
                    str = "other";
                    ArrayList<StatusData> arrayList2 = new ArrayList<>();
                    charSequence = "success";
                    arrayList2.add(new StatusData(trim, trim3 + " " + trim4, str5, trim5, str2, str3, str4, 0));
                    totalotherlist.add(new otherStatusData(trim, arrayList2));
                }
                SharedPreferences.Editor edit2 = getApplicationContext().getSharedPreferences("data", 0).edit();
                edit2.putString(str, new Gson().toJson(totalotherlist));
                edit2.apply();
                Toast.makeText(AddStatusActivity.this, charSequence, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddStatusActivity.this, "Please select image or video...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 10 && i2 == -1) {
            Uri data = intent.getData();
            String[] strArr = {"_data"};
            try {
                Cursor query = getContentResolver().query(data, strArr, null, null, null);
                query.moveToFirst();
                String string = query.getString(query.getColumnIndex(strArr[0]));
                query.close();
                profilestr = string;
                Glide.with(this).load(data).into(profile_image);
            } catch (Exception ignored) {
            }
        }
        if (i == 11 && i2 == -1) {
            Uri data2 = intent.getData();
            String[] strArr2 = {"_data"};
            try {
                Cursor query2 = getContentResolver().query(data2, strArr2, null, null, null);
                query2.moveToFirst();
                String string2 = query2.getString(query2.getColumnIndex(strArr2[0]));
                query2.close();
                imagestr = string2;
                videostr = "";
                selectmedia.setVisibility(View.VISIBLE);
                selectvideo.setVisibility(View.GONE);
                Glide.with(this).load(data2).into(selectmedia);
            } catch (Exception ignored) {
            }
        }

        if (i == 12 && i2 == -1) {
            Uri data3 = intent.getData();
            videostr = data3.getPath();
            selectmedia.setVisibility(View.GONE);
            selectvideo.setVisibility(View.VISIBLE);
            Glide.with(this).load(data3).into(selectvideo);
            String path = getPath(data3);
            imagestr = "";
            if (path != null) {
                videostr = path;
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor query = getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        if (query == null) {
            return null;
        }
        int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
        query.moveToFirst();
        return query.getString(columnIndexOrThrow);
    }

    public List<String> getnameData() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("My status");
        List<otherStatusData> list = new Gson().fromJson(getApplicationContext().getSharedPreferences("data", 0).getString("other", ""), new TypeToken<List<otherStatusData>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                arrayList.add(list.get(i).getOthername());
            }
        }
        return arrayList;
    }

    @Override
    public void onBackPressed() {
        finish();
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