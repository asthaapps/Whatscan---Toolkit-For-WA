package com.whatscan.toolkit.forwa.Prank.FakeChat;

import static com.whatscan.toolkit.forwa.Prank.FakeChat.FragmentCalls.selectedImageUri;
import static com.whatscan.toolkit.forwa.Prank.FakeChat.FragmentCalls.selectedImageUrinew;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCallDetails extends AppCompatActivity {
    public byte[] bmyimage;
    public Button callNow;
    public TextInputEditText txtName;
    public String nameProfile, nameProfilenew;
    public CircleImageView user_profilepic, user_profilepicnew;
    public LinearLayout llMain, ll_audio_call, ll_video_call, ll_audio_main, ll_video_main;
    public RadioButton audioCall, videoCall;
    public int callType = 0;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityCallDetails.this);
        setContentView(R.layout.activity_call_details);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityCallDetails.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        TextView txtOne = findViewById(R.id.txtOne);
        TextView txtTwo = findViewById(R.id.txtTwo);
        TextView txtThree = findViewById(R.id.txtThree);
        TextView txtFour = findViewById(R.id.txtFour);
        RelativeLayout rlCallDetails = findViewById(R.id.rlCallDetails);
        View ic_include = findViewById(R.id.ic_include);
        TextInputLayout text_input1 = findViewById(R.id.text_input1);
        txtName = findViewById(R.id.user_name);
        user_profilepic = findViewById(R.id.user_profilepic);
        user_profilepicnew = findViewById(R.id.user_profilepicnew);
        callNow = findViewById(R.id.callnow);
        llMain = findViewById(R.id.llMain);
        audioCall = findViewById(R.id.audioCall);
        videoCall = findViewById(R.id.videoCall);
        ll_audio_call = findViewById(R.id.ll_audio_call);
        ll_video_call = findViewById(R.id.ll_video_call);
        ll_audio_main = findViewById(R.id.ll_audio_main);
        ll_video_main = findViewById(R.id.ll_video_main);

        tv_toolbar.setText(Html.fromHtml("<small>" + "Call Details" + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlCallDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            txtName.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            ll_audio_call.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            ll_video_call.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtTwo.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtThree.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtFour.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        audioCall.setChecked(true);
        ll_audio_main.setVisibility(View.VISIBLE);

        user_profilepic.setOnClickListener(new btnUserProfilePicListner());
        user_profilepicnew.setOnClickListener(new btnUserProfilePicListnernew());
        callNow.setOnClickListener(new btnCallNowListner());

        ll_audio_call.setOnClickListener(v -> {
            callType = 1;
            audioCall.setChecked(true);
            videoCall.setChecked(false);
            ll_audio_main.setVisibility(View.VISIBLE);
            ll_video_main.setVisibility(View.GONE);
        });

        ll_video_call.setOnClickListener(v -> {
            callType = 2;
            audioCall.setChecked(false);
            videoCall.setChecked(true);
            ll_video_main.setVisibility(View.VISIBLE);
            ll_audio_main.setVisibility(View.VISIBLE);
        });

        la_back.setOnClickListener(v -> onBackPressed());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            onSelectFromGalleryResult(data);
        } else if (resultCode == -1 && requestCode == 201) {
            onSelectFromGalleryResultnew(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            selectedImageUri = data.getData();
            user_profilepic.setImageURI(selectedImageUri);
            bmyimage = saveImageInDB(selectedImageUri);
            getPath(selectedImageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSelectFromGalleryResultnew(Intent data) {
        try {
            selectedImageUrinew = data.getData();
            user_profilepicnew.setImageURI(selectedImageUrinew);
            bmyimage = saveImageInDB(selectedImageUrinew);
            getPath(selectedImageUrinew);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] saveImageInDB(Uri selectedImageUri) {
        try {
            return getBytes(openFileInput(String.valueOf(selectedImageUri)));
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

    private class btnUserProfilePicListner implements View.OnClickListener {
        public void onClick(View view) {
            startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 101);
        }
    }

    private class btnUserProfilePicListnernew implements View.OnClickListener {
        public void onClick(View view) {
            startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 201);
        }
    }

    private class btnCallNowListner implements View.OnClickListener {
        public void onClick(View view) {
            if (callType == 1) {
                nameProfile = Objects.requireNonNull(txtName.getText()).toString();
                if (nameProfile.isEmpty()) {
                    txtName.setError("Enter Caller Name");
                    return;
                }
                Intent intent = new Intent(ActivityCallDetails.this, ActivityFackCalls.class);
                intent.putExtra("NAME", nameProfile);
                intent.putExtra("TYPEONE", true);
                intent.putExtra("PROFILEPIC", selectedImageUri);
                startActivity(intent);
            } else if (callType == 2) {
                nameProfilenew = Objects.requireNonNull(txtName.getText()).toString();
                if (nameProfilenew.isEmpty()) {
                    txtName.setError("Enter Caller Name");
                    return;
                }
                Intent intent = new Intent(ActivityCallDetails.this, ActivityFackCalls.class);
                intent.putExtra("NAMENEW", nameProfilenew);
                intent.putExtra("TYPETWO", true);
                intent.putExtra("PROFILEPIC", selectedImageUri);
                intent.putExtra("PROFILEPICNEW", selectedImageUrinew);
                startActivity(intent);
            } else {
                nameProfile = Objects.requireNonNull(txtName.getText()).toString();
                if (nameProfile.isEmpty()) {
                    txtName.setError("Enter Caller Name");
                    return;
                }
                Intent intent = new Intent(ActivityCallDetails.this, ActivityFackCalls.class);
                intent.putExtra("NAME", nameProfile);
                intent.putExtra("TYPEONE", true);
                intent.putExtra("PROFILEPIC", selectedImageUri);
                startActivity(intent);
            }
        }
    }
}
