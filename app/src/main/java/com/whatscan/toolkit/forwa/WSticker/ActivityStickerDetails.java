package com.whatscan.toolkit.forwa.WSticker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;
import com.whatscan.toolkit.forwa.Adapter.MainStickerSubListCategeryAdapter;
import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.GetSet.StickerArray;
import com.whatscan.toolkit.forwa.GetSet.StickerArrayList;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ActivityStickerDetails extends AppCompatActivity {
    public static final int ADD_PACK = 200;
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_DATA = "sticker_pack";
    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";
    public static final String EXTRA_STICKER_PACK_EMAIL = "sticker_pack_email";
    public static final String EXTRA_STICKER_PACK_PRIVACY_POLICY = "sticker_pack_privacy_policy";
    public static final String EXTRA_STICKER_PACK_TRAY_ICON = "sticker_pack_tray_icon";
    public static final String EXTRA_STICKER_PACK_WEBSITE = "sticker_pack_website";
    public static String mainpath;
    RelativeLayout rlStickerDetail;
    View ic_include;
    StickerArray arrayList;
    List<StickerArrayList> newstklist;
    ArrayList<String> stringslist;
    String sticker_id;
    TextView item_pack_name, txt_progress;
    ImageView pack_try_image;
    RecyclerView recyclerView;
    LinearLayout ll_add_whatsapp, linear_layout_progress;
    ProgressBar progress_bar_pack;
    int progress = 0;
    MainStickerSubListCategeryAdapter mainStickerSubListCategeryAdapter;

    public static void SaveTryImage(Bitmap finalBitmap, String name, String identifier) {
        String root = mainpath + "/" + identifier;
        File myDir = new File(root + "/" + "try");
        myDir.mkdirs();
        String fname = name.replace(".webp", "").replace(" ", "_") + ".png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SaveImage(Bitmap finalBitmap, String name, String catageryId) {
        String root = mainpath + "/" + catageryId;
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = name;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.WEBP, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityStickerDetails.this);
        setContentView(R.layout.activity_sticker_details);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        la_info.setVisibility(View.GONE);

        tv_toolbar.setText(Html.fromHtml("<small>" + "Sticker Detail" + "</small>"));

        rlStickerDetail = findViewById(R.id.rlStickerDetail);
        ic_include = findViewById(R.id.ic_include);
        item_pack_name = findViewById(R.id.item_pack_name);
        txt_progress = findViewById(R.id.txt_progress);
        pack_try_image = findViewById(R.id.pack_try_image);
        recyclerView = findViewById(R.id.recyclerView);
        ll_add_whatsapp = findViewById(R.id.ll_add_whatsapp);
        linear_layout_progress = findViewById(R.id.linear_layout_progress);
        progress_bar_pack = findViewById(R.id.progress_bar_pack);

        arrayList = (StickerArray) getIntent().getSerializableExtra("sticker_pack");


        Glide.with(ActivityStickerDetails.this).asBitmap().load(Preference.getMain_Url() + Preference.getSticker_path() + arrayList.catagery_image).placeholder(R.drawable.sticker_error).error(R.drawable.sticker_error).into(pack_try_image);
        item_pack_name.setText(arrayList.catagery_title);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlStickerDetail.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            linear_layout_progress.setBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            item_pack_name.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txt_progress.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rlStickerDetail.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            linear_layout_progress.setBackgroundColor(Color.parseColor("#97F6F6F6"));
            item_pack_name.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txt_progress.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        newstklist = arrayList.getStickers();
        stringslist = new ArrayList<>();
        for (StickerArrayList s : newstklist) {
            stringslist.add(s.sticker_image);
        }

        sticker_id = getIntent().getStringExtra("sticker_id");
        mainpath = getFilesDir() + "/" + "stickers_asset";

        recyclerView.setLayoutManager(new GridLayoutManager(ActivityStickerDetails.this, 4));
        mainStickerSubListCategeryAdapter = new MainStickerSubListCategeryAdapter(ActivityStickerDetails.this, stringslist);
        recyclerView.setAdapter(mainStickerSubListCategeryAdapter);

        la_back.setOnClickListener(v -> onBackPressed());

        ll_add_whatsapp.setOnClickListener(v -> {
            ll_add_whatsapp.setVisibility(View.GONE);
            linear_layout_progress.setVisibility(View.VISIBLE);
            progress = 0;
            new DownloadTryImageFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        });
    }

    public Bitmap scaleBitmap(Bitmap bitmapGived, int wantedWidth, int wantedHeight) {
        if (bitmapGived.getHeight() > bitmapGived.getWidth()) {
            return scaleBitmapH(bitmapGived, wantedWidth, wantedHeight);
        } else {
            return scaleBitmapW(bitmapGived, wantedWidth, wantedHeight);
        }
    }

    public Bitmap scaleBitmapH(Bitmap bitmapGived, int wantedWidth, int wantedHeight) {
        Bitmap resultBitmap = Bitmap.createBitmap(bitmapGived.getHeight(), bitmapGived.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(resultBitmap);
        Rect sourceRect = new Rect(0, 0, bitmapGived.getWidth(), bitmapGived.getHeight());
        Rect destinationRect = new Rect((resultBitmap.getWidth() - bitmapGived.getWidth()) / 2, (resultBitmap.getHeight() - bitmapGived.getHeight()) / 2, (resultBitmap.getWidth() + bitmapGived.getWidth()) / 2, bitmapGived.getHeight());
        c.drawBitmap(bitmapGived, sourceRect, destinationRect, null);
        return getFinalBitmap(resultBitmap, 512, 512);
    }

    public Bitmap getFinalBitmap(Bitmap bitmapGived, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmapGived.getWidth(), (float) wantedHeight / bitmapGived.getHeight());
        canvas.drawBitmap(bitmapGived, m, new Paint());
        return output;
    }

    public Bitmap scaleBitmapW(Bitmap originalImage, int width, int height) {
        Bitmap background = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);

        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();

        Canvas canvas = new Canvas(background);

        float scale = width / originalWidth;

        float xTranslation = 0.0f;
        float yTranslation = (height - originalHeight * scale) / 2.0f;

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(originalImage, transformation, paint);

        return background;
    }

    public void Addtowhatsapp() {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra(EXTRA_STICKER_PACK_ID, arrayList.id);
        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra(EXTRA_STICKER_PACK_NAME, arrayList.catagery_title);
        try {
            startActivityForResult(intent, ADD_PACK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "WhatsApp Application not installed on this device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PACK) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Download Success", Toast.LENGTH_SHORT).show();
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

    class DownloadTryImageFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            try {
                System.out.println("Downloading Try Image");
                URL urltry = new URL(Preference.getMain_Url() + Preference.getSticker_path() + arrayList.catagery_image);

                URLConnection conectiontry = urltry.openConnection();
                conectiontry.connect();

                InputStream input_try = new BufferedInputStream(urltry.openStream(), 8192);

                Bitmap resource_try = BitmapFactory.decodeStream(input_try);
                int width_try = 96;
                int height_try = 96;
                Bitmap bitmap_try = Bitmap.createScaledBitmap(resource_try, width_try, height_try, false);
                SaveTryImage(bitmap_try, arrayList.catagery_image, arrayList.id);
                progress++;
                publishProgress("" + ((progress * 100) / arrayList.getStickers().size()));
            } catch (Exception ignored) {
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            progress_bar_pack.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            ArrayList<StickerArray> stickerPacks = Hawk.get("whatsapp_sticker_packs", new ArrayList<>());
            if (stickerPacks == null) {
                stickerPacks = new ArrayList<>();
            }
            for (int i = 0; i < stickerPacks.size(); i++) {
                if (stickerPacks.get(i).id.equals(arrayList.id)) {
                    stickerPacks.remove(i);
                    Log.e("PACKSTICKER", "DELETED");
                    i--;
                }
            }
            stickerPacks.add(arrayList);
            Hawk.put("whatsapp_sticker_packs", stickerPacks);
            for (int i = 0; i < stickerPacks.size(); i++) {
                Log.e("PACKSTICKER", stickerPacks.get(i).id + " / " + stickerPacks.get(i).catagery_title);
            }
            new DownloadOneStickerFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    class DownloadOneStickerFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            for (int i = 0; i < arrayList.getStickers().size(); i++) {
                String strurl = Preference.getMain_Url() + Preference.getSticker_path() + arrayList.getStickers().get(i).getStickerImage();

                try {
                    URL url = new URL(strurl);
                    URLConnection conection = url.openConnection();
                    conection.connect();
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    Bitmap resource = BitmapFactory.decodeStream(input);
                    Bitmap bitmap1 = scaleBitmap(resource, 512, 512);
                    SaveImage(bitmap1, arrayList.getStickers().get(i).getStickerImage(), arrayList.id);

                    progress++;
                    publishProgress("" + ((progress * 100) / arrayList.getStickers().size()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            progress_bar_pack.setProgress(Integer.parseInt(progress[0]));
            TextView textView = findViewById(R.id.txt_progress);
            textView.setText(Integer.parseInt(progress[0]) + "%");
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (progress == arrayList.getStickers().size() + 1) {
                ll_add_whatsapp.setVisibility(View.VISIBLE);
                linear_layout_progress.setVisibility(View.GONE);
                Addtowhatsapp();
            }
        }
    }
}