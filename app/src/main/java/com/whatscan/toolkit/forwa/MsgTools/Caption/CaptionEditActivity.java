package com.whatscan.toolkit.forwa.MsgTools.Caption;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Typeface;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CaptionEditActivity extends AppCompatActivity implements View.OnClickListener {
    public boolean alreadySaved = false;
    public LinearLayout ll_background, ll_gradient, ll_tcolor, ll_font, ll_tsize, ll_tstyle, ll_save;
    public String[] fontpath;
    public OnlineFontAdapter fontAdapter;
    public LinearLayout ll_text_size, ll_text_style, ll_text_font;
    public int bgcolor;
    public String data;
    public ArrayList<Integer> gradientclrs;
    public ImageView imageView;
    public boolean is_gradient = false;
    public int style = 3;
    public int[] style_images;
    public float textSize;
    public int textcolor;
    public String[] txt_size;
    public Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), CaptionEditActivity.this);
        setContentView(R.layout.activity_caption_edit);

        FindView();
        backgroundtask();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(CaptionEditActivity.this, ll_banner);

        ll_background = findViewById(R.id.ll_background);
        ll_gradient = findViewById(R.id.ll_gradient);
        ll_tcolor = findViewById(R.id.ll_tcolor);
        ll_font = findViewById(R.id.ll_font);
        ll_tsize = findViewById(R.id.ll_tsize);
        ll_tstyle = findViewById(R.id.ll_tstyle);
        ll_save = findViewById(R.id.ll_save);
        ll_text_size = findViewById(R.id.ll_text_size);
        ll_text_style = findViewById(R.id.ll_text_style);
        ll_text_font = findViewById(R.id.ll_text_font);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        RelativeLayout rl_caption_edit = findViewById(R.id.rl_caption_edit);
        View ic_include = findViewById(R.id.ic_include);
        TextView txtOne = findViewById(R.id.txtOne);
        TextView txtTwo = findViewById(R.id.txtTwo);
        TextView txtThree = findViewById(R.id.txtThree);
        TextView txtFour = findViewById(R.id.txtFour);
        TextView txtFive = findViewById(R.id.txtFive);
        TextView txtSix = findViewById(R.id.txtSix);
        TextView txtSeven = findViewById(R.id.txtSeven);
        la_info.setVisibility(View.GONE);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_caption_edit.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFive.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSix.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSeven.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        ll_background.setOnClickListener(this);
        ll_gradient.setOnClickListener(this);
        ll_tcolor.setOnClickListener(this);
        ll_font.setOnClickListener(this);
        ll_tsize.setOnClickListener(this);
        ll_tstyle.setOnClickListener(this);
        ll_save.setOnClickListener(this);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.caption_status) + "</small>"));

        la_back.setOnClickListener(v -> onBackPressed());
    }

    private void backgroundtask() {
        new Thread(() -> {
            textcolor = ViewCompat.MEASURED_STATE_MASK;
            bgcolor = -1;
            typeface = Typeface.createFromAsset(getAssets(), "ecmedium.ttf");
            textSize = 50.0f;
            imageView = findViewById(R.id.image);
            gradientclrs = new ArrayList<>();
            data = getIntent().getStringExtra("data");
            style_images = new int[]{R.drawable.text_bold, R.drawable.text_stroke, R.drawable.text_thin};
            txt_size = new String[]{"4", "6", "8", "10", "12", "14", "16", "18", "20", "21", "22", "23", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "67", "68", "69", "70", "75", "80", "85", "90", "95", "100"};

            runOnUiThread(() -> uitask(typeface, bgcolor, textcolor, textSize, data, is_gradient, gradientclrs));
        }).start();
    }

    private void uitask(Typeface typeface2, int i, int i2, float f, String str, Boolean bool, ArrayList<Integer> arrayList) {
        imageView.setImageBitmap(new TopCreator(this).createImage(typeface2, i, i2, f, str, bool, arrayList, style));
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

    @SuppressLint({"WrongConstant", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_background:
                ll_text_size.setVisibility(View.GONE);
                ll_text_style.setVisibility(View.GONE);
                ll_text_font.setVisibility(View.GONE);
                colorpicker();
                break;
            case R.id.ll_gradient:
                ll_text_size.setVisibility(View.GONE);
                ll_text_style.setVisibility(View.GONE);
                ll_text_font.setVisibility(View.GONE);
                setGradient();
                break;
            case R.id.ll_tcolor:
                ll_text_size.setVisibility(View.GONE);
                ll_text_style.setVisibility(View.GONE);
                ll_text_font.setVisibility(View.GONE);
                ColorpickerText();
                break;
            case R.id.ll_font:
                ll_text_size.setVisibility(View.GONE);
                ll_text_style.setVisibility(View.GONE);
                if (ll_text_font.getVisibility() == View.GONE) {
                    ll_text_font.setVisibility(View.VISIBLE);
                } else {
                    ll_text_font.setVisibility(View.GONE);
                }
                RecyclerView font_recyview = findViewById(R.id.font_recyview);
                font_recyview.setLayoutManager(new LinearLayoutManager(CaptionEditActivity.this, RecyclerView.HORIZONTAL, false));
                font_recyview.setItemAnimator(new DefaultItemAnimator());
                fontAdapter = new OnlineFontAdapter(getfontpath(), CaptionEditActivity.this, CaptionEditActivity.this);
                font_recyview.setAdapter(fontAdapter);
                break;
            case R.id.ll_tsize:
                alreadySaved = false;
                ll_text_style.setVisibility(View.GONE);
                ll_text_font.setVisibility(View.GONE);
                if (ll_text_size.getVisibility() == View.GONE) {
                    ll_text_size.setVisibility(View.VISIBLE);
                } else {
                    ll_text_size.setVisibility(View.GONE);
                }
                RecyclerView recyclerView = findViewById(R.id.recycle);
                recyclerView.setLayoutManager(new LinearLayoutManager(CaptionEditActivity.this, 0, false));
                recyclerView.setAdapter(new TextSizeAdapter(CaptionEditActivity.this, txt_size, CaptionEditActivity.this));
                recyclerView.scrollToPosition(25);
                break;
            case R.id.ll_tstyle:
                this.alreadySaved = false;
                ll_text_size.setVisibility(View.GONE);
                ll_text_font.setVisibility(View.GONE);
                if (ll_text_style.getVisibility() == View.GONE) {
                    ll_text_style.setVisibility(View.VISIBLE);
                } else {
                    ll_text_style.setVisibility(View.GONE);
                }

                RecyclerView recyclerView1 = findViewById(R.id.recycle_style);
                recyclerView1.setLayoutManager(new LinearLayoutManager(CaptionEditActivity.this, 0, false));
                recyclerView1.setAdapter(new TextStyleAdapter(CaptionEditActivity.this, style_images, CaptionEditActivity.this));
                break;
            case R.id.ll_save:
                ll_text_size.setVisibility(View.GONE);
                ll_text_style.setVisibility(View.GONE);
                ll_text_font.setVisibility(View.GONE);
                if (!alreadySaved) {
                    save();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.alreadysaved), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void ColorpickerText() {
        alreadySaved = false;
        ColorPickerDialogBuilder.with(this).setTitle("Choose Color").wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE).setOnColorSelectedListener(i -> {
        }).setPositiveButton("Ok", (dialogInterface, i, numArr) -> {
            textcolor = i;
            setTextColor(i);
        }).setNegativeButton("Cancel", (dialogInterface, i) -> {
        }).build().show();
    }

    public void colorpicker() {
        alreadySaved = false;
        ColorPickerDialogBuilder.with(this).setTitle("Choose Color").wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE).setOnColorSelectedListener(i -> {
        }).setPositiveButton("Ok", (dialogInterface, i, numArr) -> {
            bgcolor = i;
            is_gradient = false;
            setBg(i);
        }).setNegativeButton("Cancel", (dialogInterface, i) -> {
        }).build().show();
    }

    public void setGradient() {
        is_gradient = true;
        alreadySaved = false;
        TopCreator topCreator = new TopCreator(this);
        TypedArray obtainTypedArray = getResources().obtainTypedArray(R.array.colors);
        Random random = new Random();
        int nextInt = random.nextInt(obtainTypedArray.length() - 1);
        int nextInt2 = random.nextInt(obtainTypedArray.length() - 1);
        int color = obtainTypedArray.getColor(nextInt, 0);
        int color2 = obtainTypedArray.getColor(nextInt2, 0);
        if (gradientclrs.size() == 0) {
            gradientclrs.add(color);
            gradientclrs.add(color2);
        } else {
            gradientclrs.set(0, color);
            gradientclrs.set(1, color2);
        }
        imageView.setImageBitmap(topCreator.createImage(typeface, bgcolor, textcolor, textSize, data, is_gradient, gradientclrs, style));
    }

    public void setBg(int i) {
        imageView.setImageBitmap(new TopCreator(this).createImage(typeface, i, textcolor, textSize, data, is_gradient, gradientclrs, style));
    }

    public void setTextColor(int i) {
        imageView.setImageBitmap(new TopCreator(this).createImage(typeface, bgcolor, i, textSize, data, is_gradient, gradientclrs, style));
    }

    public void add_textSize(float f) {
        textSize = f;
        imageView.setImageBitmap(new TopCreator(this).createImage(typeface, bgcolor, textcolor, f, data, is_gradient, gradientclrs, style));
    }

    public void addStyle(int i) {
        this.style = i;
        this.imageView.setImageBitmap(new TopCreator(this).createImage(this.typeface, this.bgcolor, this.textcolor, this.textSize, this.data, this.is_gradient, this.gradientclrs, this.style));
    }

    public void addtypeFace(Typeface typeface2) {
        this.typeface = typeface2;
        this.imageView.setImageBitmap(new TopCreator(this).createImage(typeface2, this.bgcolor, this.textcolor, this.textSize, this.data, this.is_gradient, this.gradientclrs, this.style));
    }

    public void save() {
        try {
            new SaveDialog(this, new TopCreator(this).createImage(this.typeface, this.bgcolor, this.textcolor, this.textSize, this.data, this.is_gradient, this.gradientclrs, this.style), this).dilog();
        } catch (Exception e) {
            Log.d("editorlog", e.toString());
        }
    }

    public String[] getfontpath() {
        try {
            this.fontpath = getAssets().list("fonts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.fontpath;
    }
}