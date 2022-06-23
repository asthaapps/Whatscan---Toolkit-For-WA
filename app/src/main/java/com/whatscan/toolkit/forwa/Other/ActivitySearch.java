package com.whatscan.toolkit.forwa.Other;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.Adapter.SearchAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.BulkSender.BulkActivityStart;
import com.whatscan.toolkit.forwa.Cleaner.ActivityCleaner;
import com.whatscan.toolkit.forwa.ContactExport.ActivityContactExport;
import com.whatscan.toolkit.forwa.DeletedMedia.ActivityDeleteMedia;
import com.whatscan.toolkit.forwa.DirectChat.ActivityDirectChat;
import com.whatscan.toolkit.forwa.GetSet.SearchModel;
import com.whatscan.toolkit.forwa.MsgTools.Caption.ActivityCaptionStatus;
import com.whatscan.toolkit.forwa.MsgTools.Emoticon.ActivityEmoticon;
import com.whatscan.toolkit.forwa.MsgTools.MagicText.ActivityMagicText;
import com.whatscan.toolkit.forwa.MsgTools.TextRepeater.ActivityTextRepeater;
import com.whatscan.toolkit.forwa.MsgTools.TextToEmoji.ActivityTextToEmoji;
import com.whatscan.toolkit.forwa.Prank.FakeChat.ActivityFakeChat;
import com.whatscan.toolkit.forwa.Prank.FakeProfile.ActivityFakeProfile;
import com.whatscan.toolkit.forwa.Prank.FakeStory.ActivityFakeStory;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Story.ActivityStorySaver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.DefaultTextWatcher;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WSticker.ActivitySticker;
import com.whatscan.toolkit.forwa.WSticker.ActivityStickerMakerList;

import java.util.ArrayList;

public class ActivitySearch extends AppCompatActivity {
    public EditText edSearch;
    public RecyclerView recycleSearch;
    public SearchAdapter searchAdapter;
    public ArrayList<SearchModel> searchModels;
    public RelativeLayout rlSearch,llSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivitySearch.this);
        setContentView(R.layout.activity_search);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivitySearch.this, ll_banner);

        edSearch = findViewById(R.id.edSearch);
        recycleSearch = findViewById(R.id.recycleSearch);
        rlSearch = findViewById(R.id.rlSearch);
        llSearch = findViewById(R.id.llSearch);
        ImageView iv_sea = findViewById(R.id.iv_sea);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlSearch.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            llSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white_search_d));
            edSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white_search_d));
            edSearch.setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            edSearch.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rlSearch.setBackgroundColor(ContextCompat.getColor(this, R.color.card_color));
            llSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white));
            edSearch.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_white));
            edSearch.setHintTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            edSearch.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        searchModels = new ArrayList<>();
        searchModels.add(new SearchModel(getString(R.string.whatsWeb), R.drawable.se_web, ActivityMain.class));
        searchModels.add(new SearchModel(getString(R.string.whats_shake), R.drawable.se_shake, ActivityMain.class));
        searchModels.add(new SearchModel(getString(R.string.walk_chat), R.drawable.se_walk, ActivityMain.class));
        searchModels.add(new SearchModel(getString(R.string.fake_chat), R.drawable.w_fake_chat, ActivityFakeChat.class));
        searchModels.add(new SearchModel(getString(R.string.fake_profile), R.drawable.w_fake_profile, ActivityFakeProfile.class));
        searchModels.add(new SearchModel(getString(R.string.fake_story), R.drawable.w_fake_status, ActivityFakeStory.class));
        searchModels.add(new SearchModel(getString(R.string.magic_text), R.drawable.w_magic, ActivityMagicText.class));
        searchModels.add(new SearchModel(getString(R.string.text_emoji), R.drawable.w_text_to_emoji, ActivityTextToEmoji.class));
        searchModels.add(new SearchModel(getString(R.string.text_repeat), R.drawable.w_repeater, ActivityTextRepeater.class));
        searchModels.add(new SearchModel(getString(R.string.emoji_emoticon), R.drawable.w_emoticon, ActivityEmoticon.class));
        searchModels.add(new SearchModel(getString(R.string.stickers), R.drawable.w_sticker, ActivitySticker.class));
        searchModels.add(new SearchModel(getString(R.string.sticker_maker), R.drawable.w_maker, ActivityStickerMakerList.class));
        searchModels.add(new SearchModel(getString(R.string.story_saver), R.drawable.w_saver, ActivityStorySaver.class));
        searchModels.add(new SearchModel(getString(R.string.direct_chat), R.drawable.w_direct, ActivityDirectChat.class));
        searchModels.add(new SearchModel(getString(R.string.deleted_message), R.drawable.w_recover, ActivityDeleteMedia.class));
        searchModels.add(new SearchModel(getString(R.string.bulk_manual_sender), R.drawable.w_bulk, BulkActivityStart.class));
        searchModels.add(new SearchModel(getString(R.string.auto_response), R.drawable.w_auto, ActivityMain.class));
        searchModels.add(new SearchModel(getString(R.string.chat_locker), R.drawable.w_chat_lock, ActivityMain.class));
        searchModels.add(new SearchModel(getString(R.string.whats_bubble), R.drawable.w_chat_bubble, ActivityMain.class));
        searchModels.add(new SearchModel(getString(R.string.export_contact), R.drawable.w_export, ActivityContactExport.class));
        searchModels.add(new SearchModel(getString(R.string.whatsapp_cleaner), R.drawable.w_cleaner, ActivityCleaner.class));
        searchModels.add(new SearchModel(getString(R.string.caption_status), R.drawable.w_caption, ActivityCaptionStatus.class));
        searchModels.add(new SearchModel("Auto Scheduler Messages", R.drawable.w_schedul, ActivityMain.class));

        recycleSearch.setLayoutManager(new GridLayoutManager(ActivitySearch.this, 3));
        searchAdapter = new SearchAdapter(ActivitySearch.this, searchModels);
        recycleSearch.setAdapter(searchAdapter);

        edSearch.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SearchModel obj, int position) {
                if (obj.Act != null) {
                    startActivity(new Intent(ActivitySearch.this, obj.Act));
                }
            }
        });
    }

    private void filter(String text) {
        ArrayList<SearchModel> filterdNames = new ArrayList<>();
        for (SearchModel s : searchModels) {
            if (s.getTxtName().toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(s);
            }
        }
        if (filterdNames.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            searchAdapter.filterList(filterdNames);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void setStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.card_color));
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.card_color));
    }

    public void setStatusBarTheme() {
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivitySearch.this, R.color.darkBlack));
    }
}