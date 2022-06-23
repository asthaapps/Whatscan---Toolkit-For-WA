package com.whatscan.toolkit.forwa.WBubble;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.BuildConfig;
import com.whatscan.toolkit.forwa.DataBaseHelper.ChatHistoryDatabaseHelper;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.WBubble.Adapter.ChatHistoryAdapter;
import com.whatscan.toolkit.forwa.WBubble.model.ChatHistoryModel;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ChatHistoryPreviewActivity extends AppCompatActivity {
    public String PackageName;
    public ChatHistoryAdapter chatHistoryAdapter;
    public ChatHistoryDatabaseHelper chatHistoryDatabaseHelper = new ChatHistoryDatabaseHelper(this);
    public ChatHistoryModel chatHistoryModel;
    public ArrayList<ChatHistoryModel> chatHistoryModels;
    public ImageView circularImageViewDP;
    public String filename;
    public LinearLayoutManager linearLayoutManager;
    public ChatHistoryDatabaseHelper myDb = new ChatHistoryDatabaseHelper(this);
    public ProgressBar progressBar;
    public RecyclerView recyclerView;
    public File requestFile;
    public Intent resultIntent;
    public String ss;

    public BroadcastReceiver onNotice = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                chatHistoryModels = new ArrayList<>();
                chatHistoryModels = viewAllTxt();
                recyclerView = findViewById(R.id.chat_list);
                recyclerView.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getApplicationContext()));
                linearLayoutManager.setStackFromEnd(true);
                chatHistoryAdapter = new ChatHistoryAdapter(getApplicationContext(), chatHistoryModels);
                recyclerView.setAdapter(chatHistoryAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ChatHistoryPreviewActivity.this);
        resultIntent = new Intent(getApplicationContext().getPackageName() + ".Provider.ACTION_RETURN_FILE");
        setContentView(R.layout.activity_chat_history_preview);

        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        RelativeLayout rlChatPreview = findViewById(R.id.rlChatPreview);
        RelativeLayout ic_include = findViewById(R.id.ic_include);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlChatPreview.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        }

        ss = getIntent().getStringExtra("PersonName");
        PackageName = getIntent().getStringExtra("PackageName");
        tv_toolbar.setText(Html.fromHtml("<small>" + ss + "</small>"));

        circularImageViewDP = findViewById(R.id.chatDP);
        circularImageViewDP.setImageBitmap(getIntent().getParcelableExtra("PersonDP"));
        progressBar = findViewById(R.id.progressbar_chatscreen);
        recyclerView = findViewById(R.id.chat_list);

        chatHistoryModels = new ArrayList<>();
        chatHistoryModels = viewAllTxt();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        new LoadChatData().execute();
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg1"));

        la_back.setOnClickListener(v -> onBackPressed());
        la_info.setOnClickListener(view -> showCustomDL());
    }

    @Override
    public void onResume() {
        super.onResume();
        @SuppressLint("WrongConstant") NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isActiveChat", false).apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isActiveChat", true).apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isActiveChat", true).apply();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public ArrayList<ChatHistoryModel> viewAllTxt() {
        chatHistoryModels = new ArrayList<>();
        Cursor allTxtMsg = myDb.getAllTxtMsg(ss, PackageName);
        while (allTxtMsg.moveToNext()) {
            chatHistoryModel = new ChatHistoryModel();
            chatHistoryModel.setTxtmsg(allTxtMsg.getString(0));
            chatHistoryModel.setDtTm(allTxtMsg.getString(1));
            if (chatHistoryModels.size() <= 0) {
                chatHistoryModels.add(chatHistoryModel);
            } else {
                boolean z = false;
                for (int i = 0; i < chatHistoryModels.size(); i++) {
                    if (!chatHistoryModels.get(i).getTxtmsg().equals(allTxtMsg.getString(0))) {
                        if (!z) {
                            chatHistoryModels.add(chatHistoryModel);
                        }
                        z = true;
                    }
                }
            }
        }
        return chatHistoryModels;
    }

    public ChatHistoryModel getLasttxt() {
        Cursor lastPosition = myDb.getLastPosition(PackageName);
        chatHistoryModel = new ChatHistoryModel();
        boolean z = false;
        while (lastPosition.moveToNext()) {
            if (lastPosition.getString(3).trim().equals(ss)) {
                chatHistoryModel.setTxtmsg(lastPosition.getString(4));
                chatHistoryModel.setDtTm(lastPosition.getString(6));
                z = true;
            }
        }
        if (z) {
            return chatHistoryModel;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ChatHistoryActivity.class));
        finish();
    }

    private void showCustomDL() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this chat data?").setCancelable(false).setPositiveButton("Yes", (dialogInterface, i) -> {
            chatHistoryDatabaseHelper.deleteAllTxt(ss, PackageName);
            Toast.makeText(ChatHistoryPreviewActivity.this, "Chat deleted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), ChatHistoryActivity.class));
            finish();
            dialogInterface.cancel();
        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
        builder.create().show();
    }

    @SuppressLint("WrongConstant")
    public void SendConversation() {
        filename = "secrets/" + ss + "'s " + PackageName + " Conversation.txt";
        requestFile = new File(getFilesDir(), filename);
        Cursor ticker = myDb.getTicker(ss.trim(), PackageName);
        if (!requestFile.exists()) {
            requestFile.getParentFile().mkdirs();
            try {
                PrintWriter printWriter = new PrintWriter(requestFile);
                while (ticker.moveToNext()) {
                    printWriter.print(ticker.getString(0));
                    printWriter.print("\t");
                    printWriter.print(ticker.getString(1));
                    printWriter.println();
                }
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestFile.exists()) {
            requestFile.delete();
            requestFile = new File(getFilesDir(), filename);
            requestFile.getParentFile().mkdirs();
            try {
                PrintWriter printWriter2 = new PrintWriter(requestFile);
                while (ticker.moveToNext()) {
                    printWriter2.print(ticker.getString(0));
                    printWriter2.print("\t");
                    printWriter2.print(ticker.getString(1));
                    printWriter2.println();
                }
                printWriter2.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        requestFile = new File(getFilesDir(), filename);
        Uri uriForFile = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", requestFile);
        resultIntent.addFlags(1);
        resultIntent.setDataAndType(uriForFile, getContentResolver().getType(uriForFile));
        resultIntent.setData(uriForFile);
        Uri data = resultIntent.getData();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addFlags(1);
        intent.putExtra("android.intent.extra.EMAIL", new String[0]);
        intent.putExtra("android.intent.extra.SUBJECT", PackageName + " Chat Conversation with " + ss);
        intent.putExtra("android.intent.extra.TEXT", "Please Find Attached Text File");
        intent.setType("application/octet-stream");
        intent.putExtra("android.intent.extra.STREAM", data);
        intent.addFlags(536870912);
        startActivity(Intent.createChooser(intent, "Send WhatsApp Conversation"));
        return;
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

    @SuppressLint("WrongConstant")
    private class LoadChatData extends AsyncTask {
        private LoadChatData() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            recyclerView.setVisibility(8);
            progressBar.setVisibility(0);
        }

        @Override
        public Object doInBackground(Object[] objArr) {
            ChatHistoryPreviewActivity chatHistoryPreviewActivity = ChatHistoryPreviewActivity.this;
            chatHistoryPreviewActivity.chatHistoryAdapter = new ChatHistoryAdapter(chatHistoryPreviewActivity.getApplicationContext(), chatHistoryModels);
            recyclerView.setAdapter(chatHistoryAdapter);
            return null;
        }

        @Override
        public void onPostExecute(Object obj) {
            super.onPostExecute(obj);
            recyclerView.setVisibility(0);
            progressBar.setVisibility(8);
        }
    }
}