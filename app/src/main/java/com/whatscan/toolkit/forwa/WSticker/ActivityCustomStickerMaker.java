package com.whatscan.toolkit.forwa.WSticker;

import static com.whatscan.toolkit.forwa.WSticker.ActivityStickerDetails.EXTRA_STICKER_PACK_AUTHORITY;
import static com.whatscan.toolkit.forwa.WSticker.ActivityStickerDetails.EXTRA_STICKER_PACK_ID;
import static com.whatscan.toolkit.forwa.WSticker.ActivityStickerDetails.EXTRA_STICKER_PACK_NAME;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.GetSet.StickerArray;
import com.whatscan.toolkit.forwa.GetSet.StickerArrayList;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WSticker.editimage.EditImageActivity;
import com.whatscan.toolkit.forwa.WSticker.editimage.EditorActivity;
import com.whatscan.toolkit.forwa.WSticker.editimage.utils.CutOut;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.orhanobut.hawk.Hawk;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActivityCustomStickerMaker extends AppCompatActivity {
    public static final int SELECT_GALLERY_IMAGE_CODE_TO_STICKER = 1578;
    public static final int SELECT_GALLERY_IMAGE_CODE_TO_TRY_IMAGE = 1572;
    public static final int EDITOR_IMAGE_CODE_TO_STICKER = 1598;
    public static final int EDITOR_IMAGE_CODE_TO_TRY_IMAGE = 1592;
    public static final int REMOVE_BG_IMAGE_CODE_TO_STICKER = 1568;
    public static final int REMOVE_BG__IMAGE_CODE_TO_TRY_IMAGE = 1562;
    private static final int ADD_PACK = 22200;
    public static String mainpath;
    public List<Bitmap> stickersList = new ArrayList<>();
    public List<StickerArrayList> mStickers = new ArrayList<>();
    public List<String> mEmojis, mDownloadFiles;
    public String imageurl;
    public Integer counter = 0;
    private Bitmap TrayImage;
    private String packId, etPackName, etCreatorName;
    public int PICK_IMAGE_TRAY_CIRCLE = 300;
    public int PICK_IMAGE_TRAY_RECTANGLE = 301;
    public int PICK_IMAGE_TRAY_NO_CROP = 302;
    public int PICK_IMAGE_STICKER_ADD_CIRCLE = 200;
    public int PICK_IMAGE_STICKER_ADD_RECTANGLE = 201;
    public int PICK_IMAGE_STICKER_ADD_NO_CROP = 202;
    public RelativeLayout relative_layout_select;
    public RelativeLayout relative_layout_add_sticker;
    public RelativeLayout rlCustomeSticker;
    public RelativeLayout rl_toolbar;
    private ImageView image_view_tray_image;
    private EditText edit_text_input_name;
    private EditText edit_text_input_publisher;
    public RecyclerView recyclerView;
    private BitmapListAdapter adapter;
    public GridLayoutManager gridLayoutManager;
    public ImageView la_back;
    private RelativeLayout relative_layout_add_to_whatsapp;
    private StickerArray stickerPack;

    public static void SaveTryImage(Bitmap finalBitmap, String name, String identifier) {
        String root = mainpath + "/" + identifier;
        File myDir = new File(root + "/" + "try");
        myDir.mkdirs();
        String fname = name.replace(".png", "").replace(" ", "_") + ".png";
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

    public static void SaveImage(Bitmap finalBitmap, String name, String identifier) {
        String root = mainpath + "/" + identifier;
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
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityCustomStickerMaker.this);
        setContentView(R.layout.activity_custom_sticker_maker);

        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        TextView txtAddSticker = findViewById(R.id.txtAddSticker);
        TextInputLayout text_input1 = findViewById(R.id.text_input1);
        TextInputLayout text_input2 = findViewById(R.id.text_input2);
        rlCustomeSticker = findViewById(R.id.rlCustomeSticker);
        rl_toolbar = findViewById(R.id.rl_toolbar);

        etPackName = getIntent().getExtras().getString("PackName");
        etCreatorName = getIntent().getExtras().getString("CreatorName");

        mainpath = getFilesDir() + "/" + "stickers_asset";

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.pack_details) + "</small>"));

        initView();
        initAction();

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlCustomeSticker.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            rl_toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            relative_layout_select.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));
            relative_layout_add_sticker.setBackground(ContextCompat.getDrawable(this, R.drawable.boder_w));
            edit_text_input_name.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            edit_text_input_publisher.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtAddSticker.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            setTextInputLayoutHintColor(text_input1, this, R.color.colorWhite);
            setTextInputLayoutHintColor(text_input2, this, R.color.colorWhite);
        } else {
            setStatusBar();
            rlCustomeSticker.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            rl_toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            relative_layout_select.setBackground(ContextCompat.getDrawable(this, R.drawable.boder));
            relative_layout_add_sticker.setBackground(ContextCompat.getDrawable(this, R.drawable.boder));
            edit_text_input_name.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            edit_text_input_publisher.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtAddSticker.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            setTextInputLayoutHintColor(text_input1, this, R.color.colorBlack);
            setTextInputLayoutHintColor(text_input2, this, R.color.colorBlack);
        }

        int id = (new Random().nextInt(99999) + 10000);
        packId = String.valueOf(id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == SELECT_GALLERY_IMAGE_CODE_TO_STICKER) {
                addSticker();
            }
            if (requestCode == SELECT_GALLERY_IMAGE_CODE_TO_TRY_IMAGE) {
                SelectTrayImage();
            }
        }
    }

    private void setTextInputLayoutHintColor(TextInputLayout text_input1, Context context, int colorBlack) {
        text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, colorBlack)));
    }

    public void DialogTrayImage() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityCustomStickerMaker.this);
        View inflate = LayoutInflater.from(ActivityCustomStickerMaker.this).inflate(R.layout.crop_mode_dialog, null);
        bottomSheetDialog.setContentView(inflate);

        RelativeLayout rlCrop = inflate.findViewById(R.id.rlCrop);
        TextView txtFirst = inflate.findViewById(R.id.txtFirst);
        TextView txtSecond = inflate.findViewById(R.id.txtSecond);
        TextView txtThird = inflate.findViewById(R.id.txtThird);
        RelativeLayout relative_layout_crop_as_circle = inflate.findViewById(R.id.relative_layout_crop_as_circle);
        RelativeLayout relative_layout_crop_as_rect = inflate.findViewById(R.id.relative_layout_crop_as_rect);
        RelativeLayout relative_layout_no_crop = inflate.findViewById(R.id.relative_layout_no_crop);

        if (Preference.getBooleanTheme(false)) {
            rlCrop.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtFirst.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSecond.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtThird.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        relative_layout_no_crop.setOnClickListener(v -> {
            SelectTrayImage();
            bottomSheetDialog.dismiss();
        });

        relative_layout_crop_as_circle.setOnClickListener(v -> {
            openAblumWithPermissionsCheck(1003);
            bottomSheetDialog.dismiss();
        });

        relative_layout_crop_as_rect.setOnClickListener(v -> {
            openAblumWithPermissionsCheck(1004);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setOnKeyListener((arg0, keyCode, event) -> {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                bottomSheetDialog.dismiss();
            }
            return true;
        });

        bottomSheetDialog.show();
    }

    public void DialogaddSticker() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityCustomStickerMaker.this);
        View inflate = LayoutInflater.from(ActivityCustomStickerMaker.this).inflate(R.layout.crop_mode_dialog, null);
        bottomSheetDialog.setContentView(inflate);

        RelativeLayout rlCrop = inflate.findViewById(R.id.rlCrop);
        TextView txtFirst = inflate.findViewById(R.id.txtFirst);
        TextView txtSecond = inflate.findViewById(R.id.txtSecond);
        TextView txtThird = inflate.findViewById(R.id.txtThird);
        RelativeLayout relative_layout_crop_as_circle = inflate.findViewById(R.id.relative_layout_crop_as_circle);
        RelativeLayout relative_layout_crop_as_rect = inflate.findViewById(R.id.relative_layout_crop_as_rect);
        RelativeLayout relative_layout_no_crop = inflate.findViewById(R.id.relative_layout_no_crop);

        if (Preference.getBooleanTheme(false)) {
            rlCrop.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtFirst.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtSecond.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtThird.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        relative_layout_no_crop.setOnClickListener(v -> {
            addSticker();
            bottomSheetDialog.dismiss();
        });

        relative_layout_crop_as_circle.setOnClickListener(v -> {
            openAblumWithPermissionsCheck(1005);
            bottomSheetDialog.dismiss();
        });

        relative_layout_crop_as_rect.setOnClickListener(v -> {
            openAblumWithPermissionsCheck(1006);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setOnKeyListener((arg0, keyCode, event) -> {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                bottomSheetDialog.dismiss();
            }
            return true;
        });

        bottomSheetDialog.show();
    }

    private void initAction() {
        this.relative_layout_add_to_whatsapp.setOnClickListener(view -> createPack());

        this.relative_layout_add_sticker.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(ActivityCustomStickerMaker.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityCustomStickerMaker.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
            } else {
                DialogaddSticker();
            }
        });

        this.relative_layout_select.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(ActivityCustomStickerMaker.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityCustomStickerMaker.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 600);
            } else {
                DialogTrayImage();
            }
        });
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private void initView() {
        relative_layout_add_to_whatsapp = findViewById(R.id.relative_layout_add_to_whatsapp);
        edit_text_input_name = findViewById(R.id.edit_text_input_name);
        edit_text_input_publisher = findViewById(R.id.edit_text_input_publisher);
        relative_layout_select = findViewById(R.id.relative_layout_select);
        relative_layout_add_sticker = findViewById(R.id.relative_layout_add_sticker);
        image_view_tray_image = findViewById(R.id.image_view_tray_image);
        recyclerView = findViewById(R.id.recyclerView);
        la_back = findViewById(R.id.la_back);

        edit_text_input_name.setText(etPackName);
        edit_text_input_publisher.setText(etCreatorName);

        gridLayoutManager = new GridLayoutManager(ActivityCustomStickerMaker.this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new BitmapListAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        la_back.setOnClickListener(v -> onBackPressed());
    }

    private void openAblum(int code) {
        if (Preference.getBooleanTheme(false)) {
            FishBun.with(ActivityCustomStickerMaker.this)
                    .setImageAdapter(new GlideAdapter())
                    .setMaxCount(1)
                    .setMinCount(1)
                    .setActionBarColor(ContextCompat.getColor(this, R.color.darkBlack), ContextCompat.getColor(this, R.color.darkBlack), true)
                    .setActionBarTitleColor(Color.parseColor("#6DA188"))
                    .setAlbumSpanCount(1, 2)
                    .setButtonInAlbumActivity(true)
                    .setCamera(false)
                    .exceptGif(true)
                    .setReachLimitAutomaticClose(false)
                    .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_back_white))
                    .setDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done_white))
                    .setIsShowCount(false)
                    .setRequestCode(code)
                    .startAlbum();
        } else {
            FishBun.with(ActivityCustomStickerMaker.this)
                    .setImageAdapter(new GlideAdapter())
                    .setMaxCount(1)
                    .setMinCount(1)
                    .setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), true)
                    .setActionBarTitleColor(Color.parseColor("#6DA188"))
                    .setAlbumSpanCount(1, 2)
                    .setButtonInAlbumActivity(true)
                    .setCamera(false)
                    .exceptGif(true)
                    .setReachLimitAutomaticClose(false)
                    .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_back_black))
                    .setDoneButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done_black))
                    .setIsShowCount(false)
                    .setRequestCode(code)
                    .startAlbum();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_GALLERY_IMAGE_CODE_TO_STICKER: {
                    ArrayList<Uri> dk = data.getParcelableArrayListExtra("intent_path");
                    Uri filepath = dk.get(0);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
                    } catch (IOException ignored) {
                    }
                    File file = getFileFromBitmap(bitmap);
                    EditImageActivity.start(this, file.getAbsolutePath(), file.getAbsolutePath(), EDITOR_IMAGE_CODE_TO_STICKER);
                    break;
                }

                case SELECT_GALLERY_IMAGE_CODE_TO_TRY_IMAGE: {
                    ArrayList<Uri> dk = data.getParcelableArrayListExtra("intent_path");
                    Uri filepath = dk.get(0);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
                    } catch (IOException ignored) {
                    }
                    File file = getFileFromBitmap(bitmap);
                    EditImageActivity.start(this, file.getAbsolutePath(), file.getAbsolutePath(), EDITOR_IMAGE_CODE_TO_TRY_IMAGE);
                }

                case REMOVE_BG_IMAGE_CODE_TO_STICKER: {
                    String newFilePath = data.getStringExtra(CutOut.CUTOUT_EXTRA_RESULT);
                    File file = new File(newFilePath);
                    String filePath = file.getPath();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    if (bitmap == null) {
                        Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                    }
                    stickersList.add(bitmap);
                    adapter.notifyDataSetChanged();
                    break;
                }

                case REMOVE_BG__IMAGE_CODE_TO_TRY_IMAGE: {
                    String newFilePath = data.getStringExtra(CutOut.CUTOUT_EXTRA_RESULT);
                    File file = new File(newFilePath);
                    String filePath = file.getPath();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    if (bitmap == null) {
                        Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                    }
                    image_view_tray_image.setImageBitmap(bitmap);
                    TrayImage = bitmap;
                    imageurl = filePath;
                    break;
                }

                case EDITOR_IMAGE_CODE_TO_STICKER: {
                    String newFilePath = data.getStringExtra(EditImageActivity.EXTRA_OUTPUT);
                    boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IMAGE_IS_EDIT, false);
                    if (isImageEdit) {
                    } else {
                        newFilePath = data.getStringExtra(EditImageActivity.FILE_PATH);
                    }
                    Intent intent = new Intent(ActivityCustomStickerMaker.this, EditorActivity.class);
                    intent.putExtra("uri", newFilePath);
                    startActivityForResult(intent, REMOVE_BG_IMAGE_CODE_TO_STICKER);
                    break;
                }

                case EDITOR_IMAGE_CODE_TO_TRY_IMAGE: {
                    String newFilePath = data.getStringExtra(EditImageActivity.EXTRA_OUTPUT);
                    boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IMAGE_IS_EDIT, false);
                    if (isImageEdit) {
                    } else {
                        newFilePath = data.getStringExtra(EditImageActivity.FILE_PATH);
                    }
                    Intent intent = new Intent(ActivityCustomStickerMaker.this, EditorActivity.class);
                    intent.putExtra("uri", newFilePath);
                    startActivityForResult(intent, REMOVE_BG__IMAGE_CODE_TO_TRY_IMAGE);
                    break;
                }
            }
        }

        if (requestCode == 1003) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Uri> uries;
                uries = data.getParcelableArrayListExtra("intent_path");

                CropImage.ActivityBuilder cropImge = CropImage.activity(uries.get(0))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("Crop Your TryImage")
                        .setAllowFlipping(true)
                        .setFixAspectRatio(true)
                        .setActivityMenuIconColor(R.color.colorBlack)
                        .setCropMenuCropButtonIcon(R.drawable.ic_done_white)
                        .setBackgroundColor(R.color.colorWhite);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cropImge.setCropShape(CropImageView.CropShape.RECTANGLE);
                } else {
                    cropImge.setCropShape(CropImageView.CropShape.OVAL);
                }
                Intent intent = cropImge.getIntent(ActivityCustomStickerMaker.this);
                startActivityForResult(intent, PICK_IMAGE_TRAY_CIRCLE);
            }
        }

        if (requestCode == 1004) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Uri> uries;
                uries = data.getParcelableArrayListExtra("intent_path");

                CropImage.ActivityBuilder cropImge = CropImage.activity(uries.get(0))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("Crop Your TryImage")
                        .setAllowFlipping(true)
                        .setFixAspectRatio(true)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setActivityMenuIconColor(R.color.colorBlack)
                        .setCropMenuCropButtonIcon(R.drawable.ic_done_white)
                        .setBackgroundColor(R.color.colorWhite);
                Intent intent = cropImge.getIntent(ActivityCustomStickerMaker.this);
                startActivityForResult(intent, PICK_IMAGE_TRAY_RECTANGLE);
            }
        }

        if (requestCode == 1005) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Uri> uries;
                uries = data.getParcelableArrayListExtra("intent_path");

                CropImage.ActivityBuilder cropImge = CropImage.activity(uries.get(0))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("Crop Your TryImage")
                        .setAllowFlipping(true)
                        .setFixAspectRatio(true)
                        .setActivityMenuIconColor(R.color.colorBlack)
                        .setCropMenuCropButtonIcon(R.drawable.ic_done_white)
                        .setBackgroundColor(R.color.colorWhite);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cropImge.setCropShape(CropImageView.CropShape.RECTANGLE);
                } else {
                    cropImge.setCropShape(CropImageView.CropShape.OVAL);
                }
                Intent intent = cropImge.getIntent(ActivityCustomStickerMaker.this);
                startActivityForResult(intent, PICK_IMAGE_STICKER_ADD_CIRCLE);
            }
        }

        if (requestCode == 1006) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<Uri> uries;
                uries = data.getParcelableArrayListExtra("intent_path");

                CropImage.ActivityBuilder cropImge = CropImage.activity(uries.get(0))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("Crop Your TryImage")
                        .setAllowFlipping(true)
                        .setFixAspectRatio(true)
                        .setActivityMenuIconColor(R.color.colorBlack)
                        .setCropMenuCropButtonIcon(R.drawable.ic_done_white)
                        .setBackgroundColor(R.color.colorWhite);
                cropImge.setCropShape(CropImageView.CropShape.RECTANGLE);

                Intent intent = cropImge.getIntent(ActivityCustomStickerMaker.this);
                startActivityForResult(intent, PICK_IMAGE_STICKER_ADD_RECTANGLE);
            }
        }

        if (requestCode == CutOut.CUTOUT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = CutOut.getUri(data);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image_view_tray_image.setImageBitmap(bitmap);
                TrayImage = bitmap;
                imageurl = selectedImage.getPath();
            }
        }

        if (requestCode == CutOut.CUTOUT_ACTIVITY_REQUEST_CODE_STICKER) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = CutOut.getUri(data);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    stickersList.add(bitmap);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == PICK_IMAGE_TRAY_RECTANGLE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                image_view_tray_image.setImageBitmap(bitmap);
                TrayImage = bitmap;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == PICK_IMAGE_TRAY_CIRCLE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image_view_tray_image.setImageBitmap(getCroppedBitmap(bitmap));
                TrayImage = getCroppedBitmap(bitmap);
                imageurl = resultUri.getPath();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == PICK_IMAGE_STICKER_ADD_CIRCLE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stickersList.add(getCroppedBitmap(bitmap));
                adapter.notifyDataSetChanged();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == PICK_IMAGE_STICKER_ADD_RECTANGLE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stickersList.add(bitmap);
                adapter.notifyDataSetChanged();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == PICK_IMAGE_STICKER_ADD_NO_CROP) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                if (bitmap != null) {
                    stickersList.add(bitmap);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == PICK_IMAGE_TRAY_NO_CROP) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                if (bitmap != null) {
                    image_view_tray_image.setImageBitmap(bitmap);
                    TrayImage = bitmap;
                    imageurl = picturePath;

                } else {
                    Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void openAblumWithPermissionsCheck(int code) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, code);
            return;
        }
        openAblum(code);
    }

    public void SelectTrayImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            openAblumWithPermissionsCheck(SELECT_GALLERY_IMAGE_CODE_TO_TRY_IMAGE);
        } else {
            openAblum(SELECT_GALLERY_IMAGE_CODE_TO_TRY_IMAGE);
        }
    }

    public void addSticker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            openAblumWithPermissionsCheck(SELECT_GALLERY_IMAGE_CODE_TO_STICKER);
        } else {
            openAblum(SELECT_GALLERY_IMAGE_CODE_TO_STICKER);
        }
    }

    public Bitmap getResizedBitmap(Bitmap srcBmp) {
        Bitmap dstBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()) {

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        } else {
            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }
        return dstBmp;
    }

    public File getFileFromBitmap(Bitmap resource) {
        int width = 512; // - Dimension in pixels
        int height = 512;  // - Dimension in pixels
        if (resource.getWidth() != resource.getHeight()) {
            resource = getResizedBitmap(resource);
        }
        Bitmap bitmap = Bitmap.createScaledBitmap(
                resource, width, height, false);
        counter++;
        OutputStream fOut = null;
        File file = new File(getApplicationContext().getCacheDir(), "FitnessGirl" + counter + ".png");
        try {
            fOut = new FileOutputStream(file);
            Bitmap pictureBitmap = bitmap; // obtaining the Bitmap
            pictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            //MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (IOException e) {

        }
        return file;
    }

    public void createPack() {
        if (edit_text_input_name.getText().toString().trim().length() < 4) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.name_short), Toast.LENGTH_LONG).show();
            return;
        }
        if (edit_text_input_publisher.getText().toString().trim().length() < 3) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.publisher_short), Toast.LENGTH_LONG).show();
            return;
        }
        if (TrayImage == null) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.tray_image_required), Toast.LENGTH_LONG).show();
            return;
        }
        if (stickersList.size() < 3) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.sticker_number_required), Toast.LENGTH_LONG).show();
            return;
        }
        if (stickersList.size() > 30) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.sticker_number_required_min), Toast.LENGTH_LONG).show();
            return;
        }
        mStickers = new ArrayList<>();
        mEmojis = new ArrayList<>();
        mDownloadFiles = new ArrayList<>();
        mEmojis.add("");

        stickerPack = new StickerArray(
                packId,
                etPackName,
                etCreatorName,
                "tray_image.png"
        );

        for (int j = 0; j < stickersList.size(); j++) {
            mStickers.add(new StickerArrayList(
                    "sticker_" + j + ".webp",
                    "sticker_" + j + ".webp"
            ));
            mDownloadFiles.add("sticker_" + j + ".webp");
        }
        Hawk.put(stickerPack.id, mStickers);
        stickerPack.setStickers(Hawk.get(stickerPack.id, new ArrayList<>()));

        new DownloadFileFromURL().execute();

        if (AppController.tinyDB != null) {
            AppController.tinyDB.putListSticker("arraySticker", mStickers);
        }
    }

    public void Addtowhatsapp() {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra(EXTRA_STICKER_PACK_ID, packId);
        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra(EXTRA_STICKER_PACK_NAME, edit_text_input_name.getText().toString().trim());
        try {
            startActivityForResult(intent, ADD_PACK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(ActivityCustomStickerMaker.this, "WhatsApp Application not installed on this device", Toast.LENGTH_LONG).show();
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

    public class BitmapListAdapter extends RecyclerView.Adapter<BitmapListAdapter.Holder> {
        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bitmap, null);
            Holder mh = new Holder(v);
            return mh;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {
            holder.bitmap_image.setImageBitmap(stickersList.get(position));
            holder.button_bitmap_item.setOnClickListener(view -> {
                stickersList.remove(holder.getAbsoluteAdapterPosition());
                adapter.notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return stickersList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class Holder extends RecyclerView.ViewHolder {

            private final ImageView bitmap_image;
            private final Button button_bitmap_item;

            public Holder(@NonNull View itemView) {
                super(itemView);
                this.bitmap_image = (ImageView) itemView.findViewById(R.id.bitmap_image);
                this.button_bitmap_item = (Button) itemView.findViewById(R.id.button_bitmap_item);
            }
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {

            try {

                int width_try = 96; // - Dimension in pixels
                int height_try = 96;  // - Dimension in pixels
                Bitmap bitmap_try = Bitmap.createScaledBitmap(TrayImage, width_try, height_try, false);

                SaveTryImage(bitmap_try, "tray_image.png", stickerPack.id);

                ArrayList<StickerArray> stickerPacks = Hawk.get("whatsapp_sticker_packs", new ArrayList<StickerArray>());
                if (stickerPacks == null) {
                    stickerPacks = new ArrayList<>();
                }

                for (int i = 0; i < stickerPacks.size(); i++) {
                    if (stickerPacks.get(i).id == packId) {
                        stickerPacks.remove(i);
                        i--;
                    }
                }
                stickerPacks.add(stickerPack);
                Hawk.put("whatsapp_sticker_packs", stickerPacks);

                if (AppController.tinyDB != null) {
                    AppController.tinyDB.putListAdVideo("arrayAdVideo", stickerPacks);
                }

                int progress = 0;
                for (final StickerArrayList s : stickerPack.getStickers()) {

                    Bitmap resource = stickersList.get(progress);

                    int width = 512; // - Dimension in pixels
                    int height = 512;  // - Dimension in pixels
                    if (resource.getWidth() != resource.getHeight()) {
                        resource = getResizedBitmap(resource);
                    }
                    Bitmap bitmap1 = Bitmap.createScaledBitmap(resource, width, height, false);

                    SaveImage(bitmap1, s.sticker_image, stickerPack.id);
                    progress++;
                    publishProgress("" + (int) ((progress * 100) / stickerPack.getStickers().size()));
                }
            } catch (Exception ignored) {
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String file_url) {
            Addtowhatsapp();
        }
    }
}