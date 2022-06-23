package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.DataBaseHelper.DatabaseHelper;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityEditChatUserProfile extends AppCompatActivity implements OnClickListener {
    byte[] blob;
    byte[] bmyimage;
    DatabaseHelper databaseHelper;
    Button delete_Profile;
    String isonline;
    String istyping;
    Switch online;
    Uri selectedImageUri;
    String status;
    Switch typing;
    Button update_Profile;
    String user;
    int user_id;
    TextInputEditText user_name;
    CircleImageView user_profilepic;
    TextInputEditText user_status;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityEditChatUserProfile.this);
        setContentView(R.layout.activity_fack_edit_profile);

        databaseHelper = new DatabaseHelper(this);
        user_id = getIntent().getExtras().getInt("USER_ID");


        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);

        LottieAnimationView la_info = findViewById(R.id.la_info);
        la_info.setVisibility(View.GONE);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.fake_chat_edit) + "</small>"));

        user_name = findViewById(R.id.user_name);
        user_status = findViewById(R.id.user_status);
        user_profilepic = findViewById(R.id.user_profilepic);
        online = findViewById(R.id.user_onlile);
        typing = findViewById(R.id.user_typing);
        update_Profile = findViewById(R.id.saveLayout);
        delete_Profile = findViewById(R.id.deleteLayout);
        GetCurrentUserDetails();
        update_Profile.setOnClickListener(this);
        delete_Profile.setOnClickListener(this);
        user_profilepic.setOnClickListener(this);
        la_back.setOnClickListener(v -> onBackPressed());

    }

    private void GetCurrentUserDetails() {
        Cursor c = this.databaseHelper.getUserHistory(this.user_id + "");
        Log.d("Total Colounmn", c.getCount() + "");
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            this.user = c.getString(c.getColumnIndex("uname"));
            this.status = c.getString(c.getColumnIndex("ustatus"));
            this.isonline = c.getString(c.getColumnIndex("uonline"));
            this.istyping = c.getString(c.getColumnIndex("utyping"));
            this.blob = c.getBlob(c.getColumnIndex("uprofile"));
        }
        this.user_name.setText(this.user + "");
        this.user_status.setText(this.status + "");
        if (this.isonline.equals("online")) {
            this.online.setChecked(true);
        } else {
            this.online.setChecked(false);
        }
        if (this.istyping.equals("typing")) {
            this.typing.setChecked(true);
        } else {
            this.typing.setChecked(false);
        }
        this.user_profilepic.setImageBitmap(getImagefromdatabase(this.blob));
    }

    private Bitmap getImagefromdatabase(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteLayout:
                this.databaseHelper.DeleteUserProfile(this.user_id);
                startActivity(new Intent(this, ActivityFakeChat.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
                finish();
                return;
            case R.id.saveLayout:
                String isonline;
                String istyping;
                String uname = this.user_name.getText().toString();
                String status = this.user_status.getText().toString();
                if (this.online.isChecked()) {
                    isonline = "online";
                } else {
                    isonline = "offline";
                }
                if (this.typing.isChecked()) {
                    istyping = "typing";
                } else {
                    istyping = "nottyping";
                }
                if (this.bmyimage == null) {
                    Bitmap bitmap = ((BitmapDrawable) this.user_profilepic.getDrawable()).getBitmap();
                    ByteArrayOutputStream blob = new ByteArrayOutputStream();
                    bitmap.compress(CompressFormat.PNG, 0, blob);
                    this.bmyimage = blob.toByteArray();
                }
                this.databaseHelper.getUserDetailsUpdate(this.user_id, uname, status, isonline, istyping, this.bmyimage);
                Toast.makeText(this, "Profile Updated...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ActivityFakeChat.class), ActivityOptionsCompat.makeScaleUpAnimation(view, (int) view.getX(), (int) view.getY(), view.getWidth(), view.getHeight()).toBundle());
                return;
            case R.id.user_profilepic:
                startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), 101);
                return;
            default:
                return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            onSelectFromGalleryResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            this.selectedImageUri = data.getData();
            this.user_profilepic.setImageURI(this.selectedImageUri);
            this.bmyimage = saveImageInDB(this.selectedImageUri);
            getPath(this.selectedImageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        byte[] buffer = new byte[1024];
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
}