package com.whatscan.toolkit.forwa.Prank.FakeProfile;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.bumptech.glide.Glide;

public class ViewFakeProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ViewFakeProfile.this);
        setContentView(R.layout.activity_view_fake_profile);

        FindView();
    }

    @SuppressLint("SetTextI18n")
    private void FindView() {
        ImageView iv_profile = findViewById(R.id.iv_profile);
        TextView tv_name = findViewById(R.id.tv_name);
        TextView tv_last_seen = findViewById(R.id.tv_last_seen);
        TextView tv_status = findViewById(R.id.tv_status);
        TextView tv_status_dat = findViewById(R.id.tv_status_dat);
        TextView tv_mobile = findViewById(R.id.tv_mobile);

        tv_name.setText(getIntent().getStringExtra("strName"));
        tv_last_seen.setText(getIntent().getStringExtra("strSeen"));
        tv_status.setText(getIntent().getStringExtra("strStatus"));
        tv_status_dat.setText(getString(R.string.nineone) + getIntent().getStringExtra("strStatusDate"));
        tv_mobile.setText(getIntent().getStringExtra("strMobile"));

        Glide.with(this).load(getIntent().getStringExtra("profile_uri")).into(iv_profile);
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