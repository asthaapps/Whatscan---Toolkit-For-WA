package com.whatscan.toolkit.forwa.Prank.FakeChat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.ActivityMain;
import com.whatscan.toolkit.forwa.DataBaseHelper.DatabaseHelper;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ActivityEditMessage extends AppCompatActivity implements OnClickListener {
    String chatid;
    DatabaseHelper databaseHelper;
    Button delete_usermessage;
    Button edit_usermessage;
    String message;
    RadioGroup messagestatus;
    TextInputEditText msgedit;
    String online;
    int position;
    byte[] profile;
    String sender;
    RadioGroup senduser;
    String status;
    String typing;
    String username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityEditMessage.this);
        setContentView(R.layout.activity_fack_edit_message);

        ImageView la_back = findViewById(R.id.la_back);
        LottieAnimationView la_info = findViewById(R.id.la_info);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        RelativeLayout rl_edit_msg = findViewById(R.id.rl_edit_msg);
        View ic_include = findViewById(R.id.ic_include);
        TextInputLayout text_input1 = findViewById(R.id.text_input1);
        msgedit = findViewById(R.id.msgedit);
        edit_usermessage = findViewById(R.id.saveLayout);
        delete_usermessage = findViewById(R.id.deleteLayout);
        senduser = findViewById(R.id.senduser);
        messagestatus = findViewById(R.id.messagestatus);

        if (Preference.getBooleanTheme(false)){
            setStatusBarTheme();
            rl_edit_msg.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            text_input1.setDefaultHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorWhite)));
            msgedit.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            ((RadioButton) findViewById(R.id.me)).setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            ((RadioButton) findViewById(R.id.myfriend)).setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            ((RadioButton) findViewById(R.id.send)).setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            ((RadioButton) findViewById(R.id.receive)).setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            ((RadioButton) findViewById(R.id.read)).setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        la_info.setVisibility(View.GONE);
        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.fake_chat_edit) + "</small>"));

        la_back.setOnClickListener(v -> onBackPressed());

        Bundle bundle = getIntent().getExtras();
        databaseHelper = new DatabaseHelper(this);
        if (bundle != null) {
            position = bundle.getInt("POSITION");
            message = bundle.getString("MESSAGE");
            sender = bundle.getString("SENDER");
            status = bundle.getString("STATUS");
            chatid = bundle.getString("CHATID");
            username = getIntent().getExtras().getString("USER_NAME");
            profile = getIntent().getExtras().getByteArray("USER_PROFILE");
            online = getIntent().getExtras().getString("USER_ONLINE");
            typing = getIntent().getExtras().getString("USER_TYPING");
        }

        edit_usermessage.setOnClickListener(this);
        delete_usermessage.setOnClickListener(this);
        msgedit.setText(this.message + "");

        if (sender.equals("yes")) {
            senduser.check(R.id.me);
        } else {
            senduser.check(R.id.myfriend);
        }

        if (status.equals("send")) {
            messagestatus.check(R.id.send);
        } else if (status.equals("receive")) {
            messagestatus.check(R.id.receive);
        } else {
            messagestatus.check(R.id.read);
        }

        senduser.setOnCheckedChangeListener(new btnSendUserListner());
        messagestatus.setOnCheckedChangeListener(new btnMessageStatusListner());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteLayout:
                databaseHelper.DeleteMessage(chatid);
                Intent editmessageintent = new Intent(this, ActivityFackUserChat.class);
                editmessageintent.putExtra("USER_ID", position);
                editmessageintent.putExtra("USER_NAME", username);
                editmessageintent.putExtra("USER_ONLINE", online);
                editmessageintent.putExtra("USER_TYPING", typing);
                editmessageintent.putExtra("USER_PROFILE", profile);
                startActivity(editmessageintent);
                finish();
                return;
            case R.id.saveLayout:
                message = msgedit.getText().toString();
                databaseHelper.UpdateMessageDetails(chatid, sender, message, status);
                Intent intent = new Intent(this, ActivityFackUserChat.class);
                intent.putExtra("USER_ID", position);
                intent.putExtra("USER_NAME", username);
                intent.putExtra("USER_ONLINE", online);
                intent.putExtra("USER_TYPING", typing);
                intent.putExtra("USER_PROFILE", profile);
                startActivity(intent);
                finish();
                return;
            default:
                return;
        }
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
        Intent intentback = new Intent(this, ActivityFackUserChat.class);
        intentback.putExtra("USER_ID", this.position);
        intentback.putExtra("USER_NAME", this.username);
        intentback.putExtra("USER_ONLINE", this.online);
        intentback.putExtra("USER_TYPING", this.typing);
        intentback.putExtra("USER_PROFILE", this.profile);
        startActivity(intentback);
        finish();
    }

    private class btnSendUserListner implements OnCheckedChangeListener {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.me) {
                ActivityEditMessage.this.sender = "yes";
                return;
            }
            ActivityEditMessage.this.sender = "no";
        }
    }

    private class btnMessageStatusListner implements OnCheckedChangeListener {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.send) {
                ActivityEditMessage.this.status = "send";
            } else if (checkedId == R.id.receive) {
                ActivityEditMessage.this.status = "receive";
            } else {
                ActivityEditMessage.this.status = "read";
            }
        }
    }
}
