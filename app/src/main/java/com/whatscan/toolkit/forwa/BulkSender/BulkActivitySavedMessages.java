package com.whatscan.toolkit.forwa.BulkSender;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.SavedMessagesAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.BulkSender.helper.BottomSheetQuickReply;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.DatabaseHandler;
import com.whatscan.toolkit.forwa.GetSet.QuickReply;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BulkActivitySavedMessages extends AppCompatActivity {
    public FloatingActionButton addQuickReplay;
    public SavedMessagesAdapter savedMessagesAdapter;
    public RecyclerView recyclerQuickReplay;
    public ImageView la_back;
    public String[] stringsList = {"Can't talk now, text you later", "I am sleeping", "I am driving", "I am in meeting", "Talk to you later", "I am busy", "I am sleeping, text you later", "At work, text you later"};
    public TextView tv_toolbar;
    public RelativeLayout rlMessage;
    public View ic_include;
    public List<QuickReply> quickReplayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivitySavedMessages.this);
        setContentView(R.layout.activity_saved_messages);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(BulkActivitySavedMessages.this, ll_banner);

        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        recyclerQuickReplay = findViewById(R.id.recyclerQuickReplay);
        addQuickReplay = findViewById(R.id.addQuickReplay);
        rlMessage = findViewById(R.id.rlMessage);
        ic_include = findViewById(R.id.ic_include);

        tv_toolbar.setText(Html.fromHtml("<small>" + getResources().getString(R.string.saved_messages) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlMessage.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        } else {
            setStatusBar();
            rlMessage.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        savedMessagesAdapter = new SavedMessagesAdapter(this);

        recyclerQuickReplay.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        for (String s : stringsList) {
            QuickReply quickReply = new QuickReply();
            quickReply.setMessage(s);
            quickReplayList.add(quickReply);
        }

        quickReplayList.addAll(new DatabaseHandler(this).getAllQuickReplies());
        savedMessagesAdapter.setQuickReplyList(quickReplayList);
        recyclerQuickReplay.setAdapter(savedMessagesAdapter);

        addQuickReplay.setOnClickListener(view -> BottomSheetQuickReply.newInstance(quickReply -> {
            quickReplayList.add(quickReply);
            savedMessagesAdapter.setQuickReplyList(quickReplayList);
        }, null).show(getSupportFragmentManager(), "Dialog Fragment"));
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

    public interface OnQuickReplyAdded {
        void onAdded(QuickReply quickReply);
    }
}