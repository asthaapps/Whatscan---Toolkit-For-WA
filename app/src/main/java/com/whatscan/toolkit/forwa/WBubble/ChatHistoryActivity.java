package com.whatscan.toolkit.forwa.WBubble;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.DataBaseHelper.ChatHistoryDatabaseHelper;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WBubble.Adapter.ChatHistorySubAdapter;
import com.whatscan.toolkit.forwa.WBubble.model.ChatHistoryNoResultfound;
import com.whatscan.toolkit.forwa.WBubble.model.ChatHistorySubModel;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.ListIterator;

public class ChatHistoryActivity extends AppCompatActivity implements ChatHistoryNoResultfound {
    public ChatHistorySubAdapter chatHistorySubAdapter;
    public String Name;
    public ChatHistorySubModel chatHistorySubModel;
    public ArrayList<ChatHistorySubModel> chatHistorySubModels;
    public LinearLayoutManager linearLayoutManager;
    public ChatHistoryDatabaseHelper myDb;
    public RecyclerView programminglist;
    public ProgressBar progressBar;
    public LottieAnimationView tab_bg_txt;
    public LinearLayout ll_banner;
    private final BroadcastReceiver onNotice = new BroadcastReceiver() {
        @SuppressLint("WrongConstant")
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("package");
            boolean booleanExtra = intent.getBooleanExtra("DeleteIntent", false);
            if (stringExtra.equals("WhatsApp")) {
                try {
                    if (chatHistorySubModels != null) {
                        programminglist.setVisibility(View.VISIBLE);
                        tab_bg_txt.setVisibility(View.GONE);
                        ll_banner.setVisibility(View.VISIBLE);
                        chatHistorySubModels.add(getLastElement());
                        ListIterator listIterator = chatHistorySubModels.listIterator();
                        while (listIterator.hasNext()) {
                            if (((ChatHistorySubModel) listIterator.next()).getName().equals(Name)) {
                                listIterator.remove();
                            }
                        }
                        chatHistorySubModels.add(chatHistorySubModel);
                        chatHistorySubAdapter.notifyDataSetChanged();
                    } else {
                        programminglist.setVisibility(View.VISIBLE);
                        tab_bg_txt.setVisibility(View.GONE);
                        ll_banner.setVisibility(View.VISIBLE);
                        chatHistorySubModels = new ArrayList<>();
                        chatHistorySubModels = viewAll();
                        programminglist = findViewById(R.id.fragment_tab2);
                        programminglist.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getApplicationContext()));
                        linearLayoutManager.setStackFromEnd(true);
                        linearLayoutManager.setReverseLayout(true);
                        chatHistorySubAdapter = new ChatHistorySubAdapter(getApplicationContext(), chatHistorySubModels, "WhatsApp", ChatHistoryActivity.this);
                        programminglist.setAdapter(chatHistorySubAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (booleanExtra) {
                    chatHistorySubModels.clear();
                    programminglist.setVisibility(View.GONE);
                    tab_bg_txt.setVisibility(View.VISIBLE);
                    ll_banner.setVisibility(View.GONE);
                }
            }
        }
    };
    public TextView title;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ChatHistoryActivity.this);
        setContentView(R.layout.activity_chat_history);

        ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ChatHistoryActivity.this, ll_banner);

        title = findViewById(R.id.tv_toolbar);
        progressBar = findViewById(R.id.progressbar_whatsapp);
        tab_bg_txt = findViewById(R.id.tab_bg_txt_whatsapp);
        programminglist = findViewById(R.id.fragment_tab2);
        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        RelativeLayout rlChatHistory = findViewById(R.id.rlChatHistory);
        View ic_include = findViewById(R.id.ic_include);

        la_info.setVisibility(View.GONE);
        title.setText(Html.fromHtml("<small>" + getString(R.string.chat_list) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlChatHistory.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }

        myDb = new ChatHistoryDatabaseHelper(this);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        programminglist.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        chatHistorySubModels = new ArrayList<>();
        chatHistorySubModels = viewAll();
        if (chatHistorySubModels.isEmpty()) {
            programminglist.setVisibility(View.GONE);
            tab_bg_txt.setVisibility(View.VISIBLE);
            ll_banner.setVisibility(View.GONE);
        } else {
            programminglist.setVisibility(View.VISIBLE);
            tab_bg_txt.setVisibility(View.GONE);
            ll_banner.setVisibility(View.VISIBLE);
        }

        new LoadChatHistoryData().execute();

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onNotice, new IntentFilter("Msg1"));

        la_back.setOnClickListener(v -> onBackPressed());
    }

    public ChatHistorySubModel getLastElement() {
        Cursor lastPosition = myDb.getLastPosition("WhatsApp");
        chatHistorySubModel = new ChatHistorySubModel();
        while (lastPosition.moveToNext()) {
            Name = lastPosition.getString(3);
            chatHistorySubModel.setName(lastPosition.getString(3));
            chatHistorySubModel.setTxtmsg(lastPosition.getString(4));
            chatHistorySubModel.setDtTm(lastPosition.getString(6));
            chatHistorySubModel.setImage(BitmapFactory.decodeStream(new ByteArrayInputStream(lastPosition.getBlob(2))));
        }
        return chatHistorySubModel;
    }

    public ArrayList<ChatHistorySubModel> viewAll() {
        Cursor allData = myDb.getAllData("WhatsApp");
        chatHistorySubModels = new ArrayList<>();
        while (allData.moveToNext()) {
            chatHistorySubModel = new ChatHistorySubModel();
            Name = allData.getString(3);
            chatHistorySubModel.setName(allData.getString(3));
            chatHistorySubModel.setTxtmsg(allData.getString(4));
            chatHistorySubModel.setDtTm(allData.getString(6));
            chatHistorySubModel.setImage(BitmapFactory.decodeStream(new ByteArrayInputStream(allData.getBlob(2))));
            ListIterator<ChatHistorySubModel> listIterator = chatHistorySubModels.listIterator();
            while (listIterator.hasNext()) {
                if (listIterator.next().getName().equals(Name)) {
                    listIterator.remove();
                }
            }
            chatHistorySubModels.add(chatHistorySubModel);
        }
        return chatHistorySubModels;
    }

    public void onTextChange(String str) {
        chatHistorySubAdapter.getFilter().filter(str);
    }

    public void unselectall() {
        chatHistorySubAdapter.unselectall();
    }

    @Override
    public void onnoresult(boolean z) {
        if (z) {
            programminglist.setVisibility(View.GONE);
            tab_bg_txt.setVisibility(View.VISIBLE);
            ll_banner.setVisibility(View.GONE);
            return;
        }
        programminglist.setVisibility(View.VISIBLE);
        tab_bg_txt.setVisibility(View.GONE);
        ll_banner.setVisibility(View.VISIBLE);
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

    public class LoadChatHistoryData extends AsyncTask {
        public LoadChatHistoryData() {
        }

        @Override
        public Object doInBackground(Object[] objArr) {
            return null;
        }

        public void onPreExecute() {
            super.onPreExecute();
            programminglist.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPostExecute(Object obj) {
            super.onPostExecute(obj);
            chatHistorySubAdapter = new ChatHistorySubAdapter(ChatHistoryActivity.this, chatHistorySubModels, "WhatsApp", ChatHistoryActivity.this);
            programminglist.setAdapter(chatHistorySubAdapter);
            programminglist.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}