package com.whatscan.toolkit.forwa.Web;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.takwolf.android.lock9.Lock9View;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;


public class ActivityPatternLock extends AppCompatActivity {
    private static final String KEY_NAME = "SomeKey";
    private boolean isEnteringFirstTime = true;
    private int[] enteredPassword;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String Type;
    private TextView tv_text;
    private KeyStore keyStore;
    private Cipher cipher;
    private RelativeLayout rl_pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityPatternLock.this);
        setContentView(R.layout.activity_pattern_lock);

        FindView();
    }

    @SuppressLint("MissingPermission")
    private void FindView() {
        rl_pattern = findViewById(R.id.rl_pattern);
        tv_text = findViewById(R.id.tv_text);
        LottieAnimationView la_skip = findViewById(R.id.la_skip);
        Lock9View lock_9_view = findViewById(R.id.lock_9_view);

        sharedPreferences = getSharedPreferences("MyPreferences", 0);
        editor = sharedPreferences.edit();

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rl_pattern.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            tv_text.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rl_pattern.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            tv_text.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        if (getIntent().hasExtra("Type")) {
            Type = getIntent().getStringExtra("Type");
        }

        la_skip.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= 23) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                tv_text.setText(getString(R.string.fingure_not_avalible));
            } else if (ContextCompat.checkSelfPermission(this, "android.permission.USE_FINGERPRINT") != 0) {
                tv_text.setText(getString(R.string.fingure_per));
            } else if (!keyguardManager.isKeyguardSecure()) {
                tv_text.setText(getString(R.string.fingure_lock));
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                tv_text.setText(getString(R.string.fingure_add));
            } else {
                tv_text.setText(getString(R.string.fingure_place));
                generateKey();
                if (cipherInit()) {
                    new FingerprintHandler(ActivityPatternLock.this).startAuth(fingerprintManager, new FingerprintManager.CryptoObject(cipher));
                }
            }
        }

        if (sharedPreferences.getBoolean("IsSkip", false) || sharedPreferences.getString("Pattern", null) != null || !Preference.getCheckFingure().isEmpty()) {
            la_skip.setVisibility(View.GONE);
        }

        la_skip.setOnClickListener(v -> {
            editor.putBoolean("IsSkip", true).apply();
            startActivity(new Intent(ActivityPatternLock.this, ActivityWeb.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            finish();
        });

        lock_9_view.setGestureCallback(new Lock9View.GestureCallback() {
            @Override
            public void onNodeConnected(@NonNull int[] numbers) {

            }

            @Override
            public void onGestureFinished(@NonNull int[] numbers) {
                if (Type.equalsIgnoreCase("Open")) {
                    if (sharedPreferences.getString("Pattern", null).equalsIgnoreCase(Arrays.toString(numbers))) {
                        Intent intent = new Intent(ActivityPatternLock.this, ActivityWeb.class);
                        intent.putExtra("From", "password");
                        startActivity(intent);
                        finish();
                        return;
                    }
                    Toast.makeText(ActivityPatternLock.this, "Pattern did not match - Try again", Toast.LENGTH_SHORT).show();
                } else if (isEnteringFirstTime) {
                    enteredPassword = numbers;
                    isEnteringFirstTime = false;
                    tv_text.setText(getString(R.string.re_draw));
                } else if (!Arrays.equals(enteredPassword, numbers)) {
                    Toast.makeText(getApplicationContext(), "Both Pattern did not match - Try again", Toast.LENGTH_SHORT).show();
                    isEnteringFirstTime = true;
                    tv_text.setText(getString(R.string.draw_pattern));
                } else if (Type.equalsIgnoreCase("Set")) {
                    editor.putString("Pattern", Arrays.toString(enteredPassword));
                    editor.apply();
                    editor.putBoolean("IsPassword", true).commit();
                    Intent intent2 = new Intent(ActivityPatternLock.this, ActivityWeb.class);
                    intent2.putExtra("From", "password");
                    startActivity(intent2);
                    finish();
                }
            }
        });
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
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityPatternLock.this, R.color.darkBlack));
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator instance = KeyGenerator.getInstance("AES", "AndroidKeyStore");
            keyStore.load(null);
            instance.init(new KeyGenParameterSpec.Builder(KEY_NAME, 3).setBlockModes("CBC").setUserAuthenticationRequired(true).setEncryptionPaddings(new String[]{"PKCS7Padding"}).build());
            instance.generateKey();
        } catch (IOException | InvalidAlgorithmParameterException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            try {
                keyStore.load(null);
                cipher.init(1, keyStore.getKey(KEY_NAME, null));
                return true;
            } catch (KeyPermanentlyInvalidatedException unused) {
                return false;
            } catch (IOException | InvalidKeyException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException e) {
                throw new RuntimeException("failed to init cipher", e);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e2) {
            throw new RuntimeException("Failed to get cipher", e2);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
        private final Context context;

        @SuppressWarnings("deprecation")
        public FingerprintHandler(Context context) {
            this.context = context;
        }

        public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
            fingerprintManager.authenticate(cryptoObject, new CancellationSignal(), 0, this, null);
        }

        @Override
        public void onAuthenticationError(int i, CharSequence charSequence) {

        }

        @Override
        public void onAuthenticationFailed() {

        }

        @Override
        public void onAuthenticationHelp(int i, CharSequence charSequence) {

        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult) {
            Preference.setCheckFingure("check");
            Intent intent2 = new Intent(context, ActivityWeb.class);
            intent2.putExtra("From", "password");
            startActivity(intent2);
            finish();
        }
    }
}