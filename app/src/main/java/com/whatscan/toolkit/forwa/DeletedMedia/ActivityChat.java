package com.whatscan.toolkit.forwa.DeletedMedia;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.DataBaseHelper.DBHelper;
import com.whatscan.toolkit.forwa.GetSet.ChatModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;

import java.util.List;

public class ActivityChat extends AppCompatActivity {
    public DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityChat.this);
        setContentView(R.layout.activity_chat);

        FindView();
    }

    private void FindView() {
        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        RecyclerView rv_chat = findViewById(R.id.rv_chat);
        RelativeLayout rl_chat = findViewById(R.id.rl_chat);
        View ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + getIntent().getStringExtra("strName") + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_chat.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        } else {
            setStatusBar();
            rl_chat.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        la_back.setOnClickListener(v -> onBackPressed());

        dbHelper = new DBHelper(ActivityChat.this);
        List<ChatModel> chatModels = dbHelper.getChat(getIntent().getStringExtra("strName"));
        rv_chat.setLayoutManager(new LinearLayoutManager(this));
        AdapterChatList adapterChatList = new AdapterChatList(this, chatModels);
        rv_chat.setAdapter(adapterChatList);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}