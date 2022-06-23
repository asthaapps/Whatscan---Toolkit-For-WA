package com.whatscan.toolkit.forwa.WChatLocker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.beautycoder.pflockscreen.viewmodels.PFPinCodeViewModel;

public class ActivityChatLock extends AppCompatActivity {

    boolean closeChat = false;
    String fromService = "";

    private final PFLockScreenFragment.OnPFLockScreenCodeCreateListener mCodeCreateListener = new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {
        @Override
        public void onCodeCreated(String str) {
            Toast.makeText(ActivityChatLock.this, "Code created", Toast.LENGTH_SHORT).show();
            Preference.saveData(ActivityChatLock.this, Preference.keylockPasscode, str);
            showMainFragment();
        }

        @Override
        public void onNewCodeValidationFailed() {
            Toast.makeText(ActivityChatLock.this, "Code validation error", Toast.LENGTH_SHORT).show();
        }
    };

    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener = new PFLockScreenFragment.OnPFLockScreenLoginListener() {
        @Override
        public void onCodeInputSuccessful() {
            closeChat = true;
            Toast.makeText(ActivityChatLock.this, "Unlock successful", Toast.LENGTH_SHORT).show();
            showMainFragment();
        }

        @Override
        public void onFingerprintSuccessful() {
            closeChat = true;
            Toast.makeText(ActivityChatLock.this, "Fingerprint Login successful", Toast.LENGTH_SHORT).show();
            showMainFragment();
        }

        @Override
        public void onPinLoginFailed() {
            closeChat = false;
            Toast.makeText(ActivityChatLock.this, "Pin failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFingerprintLoginFailed() {
            closeChat = false;
            Toast.makeText(ActivityChatLock.this, "Fingerprint failed", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityChatLock.this);
        setContentView(R.layout.activity_chat_lock);

        FindView();
    }

    private void FindView() {
        RelativeLayout rlChatLock = findViewById(R.id.rlChatLock);

        if (getIntent() != null) {
            fromService = getIntent().getStringExtra("fromService");
        }

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlChatLock.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            findViewById(R.id.container_view).setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
        } else {
            findViewById(R.id.container_view).setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        showLockScreenFragment();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onStop() {
        try {
            if (fromService != null && fromService.equalsIgnoreCase("Chat") && !closeChat) {
                Intent intent = new Intent();
                intent.setClassName("com.whatsapp", "com.whatsapp.HomeActivity");
                intent.addFlags(1342177280);
                intent.addFlags(67108864);
                startActivityForResult(intent, 9);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    private void showLockScreenFragment() {
        new PFPinCodeViewModel().isPinCodeEncryptionKeyExist().observe(this, pFResult -> {
            if (pFResult != null) {
                if (pFResult.getError() != null) {
                    Toast.makeText(ActivityChatLock.this, "Can not get pin code info", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityChatLock.this.showLockScreenFragment(pFResult.getResult());
                }
            }
        });
    }

    private void showLockScreenFragment(boolean z) {
        PFFLockScreenConfiguration.Builder useFingerprint = new PFFLockScreenConfiguration.Builder(this).setTitle(z ? "Unlock with Pin or Fingerprint" : "Create Passcode").setCodeLength(4).setUseFingerprint(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("fingure_auth", true));
        PFLockScreenFragment pFLockScreenFragment = new PFLockScreenFragment();
        pFLockScreenFragment.setOnLeftButtonClickListener(view -> Toast.makeText(ActivityChatLock.this, "Left button pressed", Toast.LENGTH_SHORT).show());
        useFingerprint.setMode(z ? PFFLockScreenConfiguration.MODE_AUTH : PFFLockScreenConfiguration.MODE_CREATE);
        if (z) {
            pFLockScreenFragment.setEncodedPinCode(Preference.getStringData(this, Preference.keylockPasscode));
            pFLockScreenFragment.setLoginListener(this.mLoginListener);
        }
        pFLockScreenFragment.setConfiguration(useFingerprint.build());
        pFLockScreenFragment.setCodeCreateListener(this.mCodeCreateListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_view, pFLockScreenFragment).commit();
    }

    @SuppressLint("WrongConstant")
    private void showMainFragment() {
        String str = fromService;
        if (str == null) {
            startActivity(new Intent(this, ActivityChatLockMain.class));
            finish();
        } else if (str.equalsIgnoreCase("Whatsapp")) {
            Intent intent = new Intent();
            intent.setClassName("com.whatsapp", "com.whatsapp.HomeActivity");
            intent.addFlags(1342177280);
            intent.addFlags(67108864);
            startActivityForResult(intent, 9);
            finish();
        } else {
            moveTaskToBack(true);
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
}