package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.DataBaseHelper.DatabaseHelper;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityChatAdd extends AppCompatActivity {
    public String selectedImagePath, usename, useronline, userstatus, usertyping;
    public SwitchCompat online, typing;
    public TextInputEditText user_name, user_status;
    public byte[] bmyimage;
    public DatabaseHelper dataBaseDetails;
    public Button save_Profile;
    public Uri selectedImageUri;
    public CircleImageView user_profilepic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityChatAdd.this);
        setContentView(R.layout.activity_chat_add);

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityChatAdd.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        RelativeLayout rl_chat_add = findViewById(R.id.rl_chat_add);
        View ic_include = findViewById(R.id.ic_include);
        TextInputLayout text_input1 = findViewById(R.id.text_input1);
        TextInputLayout text_input2 = findViewById(R.id.text_input2);
        dataBaseDetails = new DatabaseHelper(this);
        user_name = findViewById(R.id.user_name);
        user_status = findViewById(R.id.user_status);
        user_profilepic = findViewById(R.id.user_profilepic);
        online = findViewById(R.id.user_onlile);
        typing = findViewById(R.id.user_typing);
        save_Profile = findViewById(R.id.saveLayout);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_chat_add.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            text_input2.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            user_name.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            user_status.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        user_profilepic.setOnClickListener(v -> startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 101));

        save_Profile.setOnClickListener(v -> SavaProfile());

        LottieAnimationView la_info = findViewById(R.id.la_info);
        la_info.setVisibility(View.GONE);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.fake_chat) + "</small>"));

        la_back.setOnClickListener(v -> onBackPressed());
    }

    private void SavaProfile() {
        usename = user_name.getText().toString();
        userstatus = user_status.getText().toString();
        if (online.isChecked()) {
            useronline = "online";
        } else {
            useronline = "offline";
        }
        if (typing.isChecked()) {
            usertyping = "typing";
        } else {
            usertyping = "nottyping";
        }
        Log.d("SELECTED DATA IS", "USER NAME" + usename + "USER Staus" + userstatus + " user onlile" + useronline + " user typing " + usertyping);
        if (bmyimage == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_photo);
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
            bmyimage = blob.toByteArray();
        }
        dataBaseDetails.InsertStudentDetails(usename, userstatus, useronline, usertyping, bmyimage);
        startActivity(new Intent(this, ActivityFakeChat.class));
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            onSelectFromGalleryResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            selectedImageUri = data.getData();
            Uri compressUri = getImageUri(MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri));
            user_profilepic.setImageURI(compressUri);
            bmyimage = saveImageInDB(compressUri);
            selectedImagePath = getPath(compressUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri getImageUri(Bitmap inImage) {
        inImage.compress(Bitmap.CompressFormat.JPEG, 10, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null));
    }

    private byte[] saveImageInDB(Uri selectedImageUri) {
        try {
            return getBytes(getContentResolver().openInputStream(selectedImageUri));
        } catch (IOException ioe) {
            Log.e("Hello1", "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            return null;
        }
    }

    private byte[] getBytes(InputStream iStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[3072];
        while (true) {
            int len = iStream.read(buffer);
            if (len == -1) {
                return byteBuffer.toByteArray();
            }
            byteBuffer.write(buffer, 0, len);
        }
    }

    private String getPath(Uri selectedImageUri) {
        Cursor cursor = managedQuery(selectedImageUri, new String[]{"_data"}, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(column_index);
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