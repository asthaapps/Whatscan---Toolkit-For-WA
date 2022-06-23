package com.whatscan.toolkit.forwa.AutoResponse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.DataBaseHelper.CustomReplyDatabaseHelper;
import com.whatscan.toolkit.forwa.GetSet.ReplyModel;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WSticker.ActivityStickerSubCategory;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;

public class ActivityCustomReply extends AppCompatActivity {
    public AppCompatButton addmsg;
    public CustomReplyDatabaseHelper customReplyDatabaseHelper;
    public LottieAnimationView delete_all;
    public LottieAnimationView infotext;
    public RelativeLayout rlCustomReply;
    public View ic_include;
    public RecyclerView recyclerView;
    public ReplyModel replyModel;
    public ArrayList<ReplyModel> replyModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityCustomReply.this);
        setContentView(R.layout.activity_custom_reply);

        FindView();
    }

    private void FindView() {
        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(ActivityCustomReply.this, ll_banner);

        ImageView la_back = findViewById(R.id.la_back);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        rlCustomReply = findViewById(R.id.rlCustomReply);
        ic_include = findViewById(R.id.ic_include);
        infotext = findViewById(R.id.infotext);
        addmsg = findViewById(R.id.addmsg);
        recyclerView = findViewById(R.id.reply_list);
        delete_all = findViewById(R.id.la_info);
        delete_all.setVisibility(View.GONE);
        delete_all.setAnimation(R.raw.delete_pack);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.custom_replay_list) + "</small>"));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlCustomReply.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        } else {
            setStatusBar();
            rlCustomReply.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        customReplyDatabaseHelper = new CustomReplyDatabaseHelper(this);

        addmsg.setOnClickListener(v -> {
            Advertisement.getInstance(ActivityCustomReply.this).showFullAds(new Advertisement.MyCallback() {
                @Override
                public void callbackCall() {
                    AddMsgIntent(v);
                }
            });

        });

        delete_all.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityCustomReply.this);
            View inflate = LayoutInflater.from(ActivityCustomReply.this).inflate(R.layout.delete_dialog, null);
            bottomSheetDialog.setContentView(inflate);

            RelativeLayout rl_dialog = inflate.findViewById(R.id.rl_dialog);
            TextView tv_dialog_title = inflate.findViewById(R.id.tv_dialog_title);
            TextView tv_dialog_tip = inflate.findViewById(R.id.tv_dialog_tip);
            AppCompatButton bt_yes = inflate.findViewById(R.id.bt_yes);
            AppCompatButton bt_no = inflate.findViewById(R.id.bt_no);

            if (Preference.getBooleanTheme(false)) {
                rl_dialog.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
                tv_dialog_title.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                tv_dialog_tip.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            }

            tv_dialog_title.setText(getString(R.string.custom_auto_reply));
            tv_dialog_tip.setText(getString(R.string.delete_replay));

            bt_no.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

            bt_yes.setOnClickListener(v1 -> {
                customReplyDatabaseHelper.deleteAllChats();
                Toast.makeText(ActivityCustomReply.this, "Custom replies deleted", Toast.LENGTH_SHORT).show();
                startActivity(getIntent());
                finish();
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });

        replyModels = new ArrayList<>();
        replyModels = viewAll();
        Collections.reverse(replyModels);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (replyModels.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            infotext.setVisibility(View.GONE);
            delete_all.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new ReplyListAdapter(replyModels));
            return;
        }
        delete_all.setVisibility(View.GONE);
        infotext.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        la_back.setOnClickListener(v -> onBackPressed());
    }

    private void AddMsgIntent(View v) {
        startActivity(new Intent(ActivityCustomReply.this, ActivityAddCustomReply.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
        finish();
    }

    public ArrayList<ReplyModel> viewAll() {
        Cursor allData = customReplyDatabaseHelper.getAllData();
        replyModels = new ArrayList<>();
        while (allData.moveToNext()) {
            replyModel = new ReplyModel();
            replyModel.setId(allData.getString(0));
            replyModel.setIncomingmsg(allData.getString(1));
            replyModel.setReplymsg(allData.getString(2));
            replyModel.setSelectedoption(allData.getString(3));
            replyModels.add(replyModel);
        }
        return replyModels;
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

    public class ReplyListAdapter extends RecyclerView.Adapter<ReplyListAdapter.ChatViewHolder> {
        ArrayList<ReplyModel> arrayList;
        CustomReplyDatabaseHelper customReplyDatabaseHelper;

        public ReplyListAdapter(ArrayList<ReplyModel> arrayList2) {
            this.arrayList = arrayList2;
            this.customReplyDatabaseHelper = new CustomReplyDatabaseHelper(ActivityCustomReply.this);
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ChatViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reply_item_layout, viewGroup, false));
        }

        @SuppressLint({"WrongConstant"})
        public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
            if (Preference.getBooleanTheme(false)) {
                chatViewHolder.mainlayout.setBackgroundColor(ContextCompat.getColor(ActivityCustomReply.this, R.color.colorShape));
                chatViewHolder.incominmsg.setTextColor(ContextCompat.getColor(ActivityCustomReply.this, R.color.colorWhite));
                chatViewHolder.replymsg.setTextColor(ContextCompat.getColor(ActivityCustomReply.this, R.color.colorWhite));
            }

            final ReplyModel replyModel = arrayList.get(i);
            chatViewHolder.incominmsg.setText(replyModel.getIncomingmsg());
            chatViewHolder.replymsg.setText(replyModel.getReplymsg());

            chatViewHolder.mainlayout.setOnClickListener(view -> {
                Intent intent = new Intent(ActivityCustomReply.this, ActivityAddCustomReply.class);
                intent.putExtra("getId", replyModel.getId());
                intent.putExtra("getIncomingmsg", replyModel.getIncomingmsg());
                intent.putExtra("getReplymsg", replyModel.getReplymsg());
                intent.putExtra("getSelectedoption", replyModel.getSelectedoption());
                startActivity(intent);
                finish();
            });

            chatViewHolder.mainlayout.setOnLongClickListener(v -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityCustomReply.this);
                View inflate = LayoutInflater.from(ActivityCustomReply.this).inflate(R.layout.delete_dialog, null);
                bottomSheetDialog.setContentView(inflate);

                RelativeLayout rl_dialog = inflate.findViewById(R.id.rl_dialog);
                TextView tv_dialog_title = inflate.findViewById(R.id.tv_dialog_title);
                TextView tv_dialog_tip = inflate.findViewById(R.id.tv_dialog_tip);
                AppCompatButton bt_yes = inflate.findViewById(R.id.bt_yes);
                AppCompatButton bt_no = inflate.findViewById(R.id.bt_no);

                if (Preference.getBooleanTheme(false)) {
                    rl_dialog.setBackgroundColor(ContextCompat.getColor(ActivityCustomReply.this, R.color.darkBlack));
                    tv_dialog_title.setTextColor(ContextCompat.getColor(ActivityCustomReply.this, R.color.colorWhite));
                    tv_dialog_tip.setTextColor(ContextCompat.getColor(ActivityCustomReply.this, R.color.colorWhite));
                }

                tv_dialog_title.setText(getString(R.string.custom_auto_reply));
                tv_dialog_tip.setText(getString(R.string.delete_replay));

                bt_no.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

                bt_yes.setOnClickListener(v1 -> {
                    customReplyDatabaseHelper.deleteData(String.valueOf(replyModel.getId()));
                    Toast.makeText(ActivityCustomReply.this, "Custom reply deleted", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                    finish();
                    bottomSheetDialog.dismiss();
                });

                bottomSheetDialog.show();
                return false;
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ChatViewHolder extends RecyclerView.ViewHolder {
            public TextView incominmsg, replymsg;
            public LinearLayout mainlayout;

            public ChatViewHolder(View view) {
                super(view);
                incominmsg = view.findViewById(R.id.incominmsg);
                replymsg = view.findViewById(R.id.replymsg);
                mainlayout = view.findViewById(R.id.mainlayout);
            }
        }
    }
}