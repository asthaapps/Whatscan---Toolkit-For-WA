package com.whatscan.toolkit.forwa.Prank.FakeStory;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFakeStory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Constant.adjustFontScale(getResources().getConfiguration(), ViewFakeStory.this);
        setContentView(R.layout.activity_view_fake_story);

        FindView();
    }

    private void FindView() {
        ImageView iv_thumbnail = findViewById(R.id.iv_thumbnail);
        CircleImageView civ_dp = findViewById(R.id.civ_dp);
        TextView tv_name = findViewById(R.id.tv_name);
        TextView tv_seen = findViewById(R.id.tv_seen);
        TextView tv_caption = findViewById(R.id.tv_caption);

        tv_name.setText(getIntent().getStringExtra("strName"));
        tv_seen.setText(getIntent().getStringExtra("strSeen"));
        tv_caption.setText(getIntent().getStringExtra("strCaption"));

        Glide.with(this).load(getIntent().getStringExtra("image_uri")).into(iv_thumbnail);
        Glide.with(this).load(getIntent().getStringExtra("profile_uri")).into(civ_dp);
    }
}