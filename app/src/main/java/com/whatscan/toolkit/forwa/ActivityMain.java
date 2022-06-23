package com.whatscan.toolkit.forwa;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.whatscan.toolkit.forwa.Ads.ActivityPremium;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.Api.ApiInterface;
import com.whatscan.toolkit.forwa.Api.RetroClient;
import com.whatscan.toolkit.forwa.Fragment.FragmentDaily;
import com.whatscan.toolkit.forwa.Fragment.FragmentExplorer;
import com.whatscan.toolkit.forwa.Fragment.FragmentPopular;
import com.whatscan.toolkit.forwa.Fragment.FragmentSetting;
import com.whatscan.toolkit.forwa.Fragment.FragmentTrending;
import com.whatscan.toolkit.forwa.GetSet.CheckRedeemData;
import com.whatscan.toolkit.forwa.GetSet.RegisterToken;
import com.whatscan.toolkit.forwa.GetSet.StatusModel;
import com.whatscan.toolkit.forwa.ReferCoin.ActivityCoin;
import com.whatscan.toolkit.forwa.Util.AppController;
import com.whatscan.toolkit.forwa.Util.ConnectivityReceiver;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMain extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, PurchasesUpdatedListener {

    public BottomNavigationView bottomNavigation;
    public RelativeLayout rlMain, rl_coin;
    public ImageView imgHeader, imgPopular, imgTrending, imgDaily, imgExplorer, imgSetting, laRefer;
    public TextView tv_popular, tv_trending, tv_daily, tv_explore, tv_setting, tvTotalCoin, tvHey;
    public loadDataAsync async;
    public ActivityResultLauncher<Intent> resultWhatsapp, resultWhatsappB;
    public ArrayList<StatusModel> arrayListStatus = new ArrayList<>();
    PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                //  handlePurchase(purchase);
            }
        }
    };
    private BottomSheetDialog bottomSheetDialog;
    private BillingClient billingClient;


    public ActivityMain() {
        resultWhatsapp = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                if (intent != null) {
                    Uri data = intent.getData();
                    try {
                        getContentResolver().takePersistableUriPermission(data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Preference.setWATree(ActivityMain.this, data.toString());
                    populateGrid();
                }
            }
        });

        resultWhatsappB = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                if (intent != null) {
                    Uri data = intent.getData();
                    try {
                        getContentResolver().takePersistableUriPermission(data, 3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Preference.setWATree(ActivityMain.this, data.toString());
                    populateGrid();
                }
            }
        });
    }


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.adjustFontScale(getResources().getConfiguration(), ActivityMain.this);
        setStatusBar();
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(ActivityMain.this, Preference::setFcm_Id);


        FindView();
        CheckPermission();
        openFragment(new FragmentTrending());
        if (Preference.getLogin_status().equals("Yes")) {
            CheckRegisterToken();
            CheckRedeem();
        }

        if (Preference.getAndroid_id().isEmpty()) {
            Preference.setAndroid_id(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        }

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlMain.setBackgroundColor(ContextCompat.getColor(ActivityMain.this, R.color.darkBlack));
            rl_coin.setBackground(ContextCompat.getDrawable(ActivityMain.this, R.drawable.shape_white_coin_w));
            bottomNavigation.setBackground(ContextCompat.getDrawable(ActivityMain.this, R.drawable.shape_black));
            imgHeader.setImageResource(R.drawable.header_logo_w);
            tvHey.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorWhite));
            tvTotalCoin.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorWhite));
            tv_daily.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorWhite));
            tv_popular.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorWhite));
            tv_trending.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorWhite));
            tv_explore.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorWhite));
            tv_setting.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorWhite));
        } else {
            setStatusBar();
            rlMain.setBackgroundColor(ContextCompat.getColor(ActivityMain.this, R.color.card_color));
            rl_coin.setBackground(ContextCompat.getDrawable(ActivityMain.this, R.drawable.shape_white_coin));
            bottomNavigation.setBackground(ContextCompat.getDrawable(ActivityMain.this, R.drawable.shape_white_tab));
            imgHeader.setImageResource(R.drawable.header_logo);
            tvHey.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorBlack));
            tvTotalCoin.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorBlack));
            tv_daily.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorBlack));
            tv_popular.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorBlack));
            tv_trending.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorBlack));
            tv_explore.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorBlack));
            tv_setting.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorBlack));
        }
    }

    private void CheckRegisterToken() {
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getRegisterToken(Preference.getRegister_token(), Preference.getU_id(), Preference.getToken()).enqueue(new Callback<RegisterToken>() {
            @Override
            public void onResponse(@NotNull Call<RegisterToken> call, @NotNull Response<RegisterToken> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getFlag().equals("true")) {
                        boolean isFirstRun = getSharedPreferences("PREFERENCE2", MODE_PRIVATE).getBoolean("isFirstRun", true);
                        if (isFirstRun) {
                            Toast.makeText(ActivityMain.this, "Your are logout by old device.\n please wait while login here...", Toast.LENGTH_SHORT).show();
                            getSharedPreferences("PREFERENCE2", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
                        }
                    } else {
                        switch (Preference.getType()) {
                            case "manually":
                                ClearPref();
                                break;
                            case "google":
                                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestEmail()
                                        .build();
                                GoogleSignIn.getClient(ActivityMain.this, googleSignInOptions).signOut().addOnCompleteListener(ActivityMain.this, task -> ClearPref());
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<RegisterToken> call, @NotNull Throwable t) {
                Toast.makeText(ActivityMain.this, "Something wrong! try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ClearPref() {
        Preference.setU_id("");
        Preference.setLogin_status("");
        Preference.setProfile("");
        Preference.setEmail("");
        Preference.setUserName("");
        Preference.setGoogle_key("");
        Preference.setFacebook_key("");
        Preference.setTotal_coin("");
        Preference.setReferedCode("");
        Preference.setAds_one_day("");
        Preference.setAds_three_day("");
        Preference.setAds_seven_day("");
        Preference.setAds_thirty_day("");
        Preference.setBulk_one_day("");
        Preference.setBulk_seven_day("");
        Preference.setAuto_one_day("");
        Preference.setAuto_seven_day("");
        Preference.setImport_one_day("");
        Preference.setImport_seven_day("");
        getSharedPreferences("PREFERENCE2", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).apply();
    }

    private void CheckPermission() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Preference.setPermission_media("true");
                            Utils.CheckConnection(ActivityMain.this, rlMain);
                            if (Preference.getWATree(ActivityMain.this).equals("")) {
                                OpenBottomSheet();
                            } else {
                                populateGrid();
                            }
                        } else {
                            CheckPermission();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void OpenBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(ActivityMain.this);
        View inflate = LayoutInflater.from(ActivityMain.this).inflate(R.layout.dialog_status_folder, null);
        bottomSheetDialog.setContentView(inflate);

        LinearLayout llFolder = inflate.findViewById(R.id.llFolder);
        TextView txtOne = inflate.findViewById(R.id.txtOne);
        TextView txt2 = inflate.findViewById(R.id.txt2);
        TextView txtGet = inflate.findViewById(R.id.txtGet);

        if (Preference.getBooleanTheme(false)) {
            llFolder.setBackgroundColor(ContextCompat.getColor(ActivityMain.this, R.color.darkBlack));
            txtOne.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorWhite));
            txt2.setTextColor(ContextCompat.getColor(ActivityMain.this, R.color.colorWhite));
        }

        txtGet.setOnClickListener(v -> FolderPermision());
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    public void populateGrid() {
        async = new loadDataAsync();
        async.execute();
    }

    @SuppressLint("WrongConstant")
    private void FolderPermision() {
        bottomSheetDialog.dismiss();
        Intent intent;
        if (Constant.whatsappInstalledOrNot(ActivityMain.this)) {
            @SuppressLint("WrongConstant") StorageManager storageManager = (StorageManager) ActivityMain.this.getSystemService("storage");
            String whatsupFolder = getWhatsupFolder();
            if (Build.VERSION.SDK_INT >= 29) {
                intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                String replace = ((Uri) intent.getParcelableExtra("android.provider.extra.INITIAL_URI")).toString().replace("/root/", "/document/");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(replace + "%3A" + whatsupFolder));
            } else {
                intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse("content://com.android.externalstorage.documents/document/primary%3A" + whatsupFolder));
            }
            intent.addFlags(2);
            intent.addFlags(1);
            intent.addFlags(128);
            intent.addFlags(64);
            resultWhatsapp.launch(intent);
        } else if (Constant.whatsappBusinessInstalledOrNot(ActivityMain.this)) {
            StorageManager storageManager = (StorageManager) ActivityMain.this.getSystemService("storage");
            String whatsupFolder = getWhatsupFolderB();
            if (Build.VERSION.SDK_INT >= 29) {
                intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                String replace = intent.getParcelableExtra("android.provider.extra.INITIAL_URI").toString().replace("/root/", "/document/");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(replace + "%3A" + whatsupFolder));
            } else {
                intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse("content://com.android.externalstorage.documents/document/primary%3A" + whatsupFolder));
            }
            intent.addFlags(2);
            intent.addFlags(1);
            intent.addFlags(128);
            intent.addFlags(64);
            resultWhatsappB.launch(intent);
        } else if (!Constant.whatsappInstalledOrNot(ActivityMain.this)) {
            Toast.makeText(ActivityMain.this, "Please Install WhatsApp For Download Status!!!!!", Toast.LENGTH_SHORT).show();
        } else if (!Constant.whatsappBusinessInstalledOrNot(ActivityMain.this)) {
            Toast.makeText(ActivityMain.this, "Please Install WhatsApp Business For Download Status!!!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getWhatsupFolder() {
        String sb = Environment.getExternalStorageDirectory() +
                File.separator +
                "Android/media/com.whatsapp/WhatsApp" +
                File.separator +
                "Media" +
                File.separator +
                ".Statuses";
        return new File(sb).isDirectory() ? "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses" : "WhatsApp%2FMedia%2F.Statuses";
    }

    public String getWhatsupFolderB() {
        String sb = Environment.getExternalStorageDirectory() +
                File.separator +
                "Android/media/com.whatsapp.w4b/WhatsApp Business" +
                File.separator +
                "Media" +
                File.separator +
                ".Statuses";
        return new File(sb).isDirectory() ? "Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp Business%2FMedia%2F.Statuses" : "WhatsApp Business%2FMedia%2F.Statuses";
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (async != null) {
            async.cancel(true);
        }
    }

    private DocumentFile[] getFromSdcard() {
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(ActivityMain.this, Uri.parse(Preference.getWATree(ActivityMain.this)));
        if (fromTreeUri == null || !fromTreeUri.exists() || !fromTreeUri.isDirectory() || !fromTreeUri.canRead() || !fromTreeUri.canWrite()) {
            return null;
        }
        return fromTreeUri.listFiles();
    }

    @SuppressLint({"WrongConstant", "SetTextI18n", "NonConstantResourceId"})
    private void FindView() {
        rlMain = findViewById(R.id.rlMain);
        laRefer = findViewById(R.id.laRefer);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        imgPopular = findViewById(R.id.imgPopular);
        imgHeader = findViewById(R.id.imgHeader);
        tv_popular = findViewById(R.id.tv_popular);
        imgTrending = findViewById(R.id.imgTrending);
        tv_trending = findViewById(R.id.tv_trending);
        imgDaily = findViewById(R.id.imgDaily);
        tv_daily = findViewById(R.id.tv_daily);
        imgExplorer = findViewById(R.id.imgExplorer);
        tv_explore = findViewById(R.id.tv_explore);
        imgSetting = findViewById(R.id.imgSetting);
        tv_setting = findViewById(R.id.tv_setting);
        tvTotalCoin = findViewById(R.id.tvTotalCoin);
        rl_coin = findViewById(R.id.rl_coin);
        ImageView imgPremium = findViewById(R.id.imgPremium);
        tvHey = findViewById(R.id.tvHey);
        RelativeLayout rl_coin = findViewById(R.id.rl_coin);


        billingClient = BillingClient.newBuilder(ActivityMain.this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NotNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    getSkuDetails();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });

        if (Preference.getLogin_status().equals("Yes")) {
            Payment.checkActiveSubs(ActivityMain.this);
        } else {
            Preference.setActive_CkeyM("");
            Preference.setActive_CkeyY("");
            Preference.setActive_PkeyM("");
            Preference.setActive_PkeyY("");
            Preference.setActive_MkeyM("");
            Preference.setActive_MkeyY("");
        }


        imgPremium.setOnClickListener(v -> startActivity(new Intent(ActivityMain.this, ActivityPremium.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle()));

        if (!Preference.getLogin_status().isEmpty()) {
            if (!Preference.getTotal_coin().isEmpty() || !Preference.getTotal_coin().equals("0")) {
                tvTotalCoin.setText(Preference.getTotal_coin());
            } else {
                tvTotalCoin.setText("Coin");
            }
            tvHey.setVisibility(View.VISIBLE);
            tvHey.setText("Hey, " + Preference.getUserName());
            imgHeader.setVisibility(View.GONE);
        } else {
            tvTotalCoin.setText("Coin");
            tvHey.setVisibility(View.GONE);
            imgHeader.setVisibility(View.VISIBLE);
        }
        bottomNavigation.setItemIconTintList(null);

        rl_coin.setOnClickListener(v -> {
            if (Preference.getLogin_status().equals("Yes")) {
                startActivity(new Intent(ActivityMain.this, ActivityCoin.class));
            } else {
                Constant.BottomSheetDialogLogIn(ActivityMain.this);
            }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_daily:
                    tv_daily.setVisibility(View.VISIBLE);
                    tv_popular.setVisibility(View.GONE);
                    tv_trending.setVisibility(View.GONE);
                    tv_explore.setVisibility(View.GONE);
                    tv_setting.setVisibility(View.GONE);
                    imgDaily.setBackground(ContextCompat.getDrawable(ActivityMain.this, R.drawable.round_menu));
                    imgDaily.setImageResource(R.drawable.s_daily);
                    imgPopular.setImageResource(R.drawable.n_popular);
                    imgTrending.setImageResource(R.drawable.n_trending);
                    imgExplorer.setImageResource(R.drawable.n_explore);
                    imgSetting.setImageResource(R.drawable.n_setting);
                    imgPopular.setBackground(null);
                    imgTrending.setBackground(null);
                    imgExplorer.setBackground(null);
                    imgSetting.setBackground(null);
                    openFragment(new FragmentDaily(arrayListStatus));
                    return true;
                case R.id.nav_tranding:
                    tv_trending.setVisibility(View.VISIBLE);
                    tv_popular.setVisibility(View.GONE);
                    tv_explore.setVisibility(View.GONE);
                    tv_daily.setVisibility(View.GONE);
                    tv_setting.setVisibility(View.GONE);
                    imgTrending.setBackground(ContextCompat.getDrawable(ActivityMain.this, R.drawable.round_menu));
                    imgPopular.setImageResource(R.drawable.n_popular);
                    imgTrending.setImageResource(R.drawable.s_trending);
                    imgDaily.setImageResource(R.drawable.n_daily);
                    imgExplorer.setImageResource(R.drawable.n_explore);
                    imgSetting.setImageResource(R.drawable.n_setting);
                    imgDaily.setBackground(null);
                    imgPopular.setBackground(null);
                    imgExplorer.setBackground(null);
                    imgSetting.setBackground(null);
                    openFragment(new FragmentTrending());
                    return true;
                case R.id.nav_pupular:
                    tv_popular.setVisibility(View.VISIBLE);
                    tv_trending.setVisibility(View.GONE);
                    tv_explore.setVisibility(View.GONE);
                    tv_daily.setVisibility(View.GONE);
                    tv_setting.setVisibility(View.GONE);
                    imgPopular.setBackground(ContextCompat.getDrawable(ActivityMain.this, R.drawable.round_menu));
                    imgDaily.setImageResource(R.drawable.n_daily);
                    imgPopular.setImageResource(R.drawable.s_popular);
                    imgTrending.setImageResource(R.drawable.n_trending);
                    imgExplorer.setImageResource(R.drawable.n_explore);
                    imgSetting.setImageResource(R.drawable.n_setting);
                    imgDaily.setBackground(null);
                    imgTrending.setBackground(null);
                    imgExplorer.setBackground(null);
                    imgSetting.setBackground(null);
                    openFragment(new FragmentPopular());
                    return true;
                case R.id.nav_explorer:
                    tv_popular.setVisibility(View.GONE);
                    tv_trending.setVisibility(View.GONE);
                    tv_explore.setVisibility(View.VISIBLE);
                    tv_daily.setVisibility(View.GONE);
                    tv_setting.setVisibility(View.GONE);
                    imgPopular.setImageResource(R.drawable.n_popular);
                    imgTrending.setImageResource(R.drawable.n_trending);
                    imgDaily.setImageResource(R.drawable.n_daily);
                    imgSetting.setImageResource(R.drawable.n_setting);
                    imgPopular.setBackground(null);
                    imgDaily.setBackground(null);
                    imgTrending.setBackground(null);
                    imgExplorer.setImageResource(R.drawable.s_explore);
                    imgExplorer.setBackground(ContextCompat.getDrawable(ActivityMain.this, R.drawable.round_menu));
                    imgSetting.setBackground(null);
                    openFragment(new FragmentExplorer());
                    return true;
                case R.id.nav_setting:
                    tv_popular.setVisibility(View.GONE);
                    tv_trending.setVisibility(View.GONE);
                    tv_explore.setVisibility(View.GONE);
                    tv_daily.setVisibility(View.GONE);
                    tv_setting.setVisibility(View.VISIBLE);
                    imgPopular.setBackground(null);
                    imgDaily.setBackground(null);
                    imgTrending.setBackground(null);
                    imgExplorer.setBackground(null);
                    imgPopular.setImageResource(R.drawable.n_popular);
                    imgTrending.setImageResource(R.drawable.n_trending);
                    imgExplorer.setImageResource(R.drawable.n_explore);
                    imgDaily.setImageResource(R.drawable.n_daily);
                    imgSetting.setImageResource(R.drawable.s_setting);
                    imgSetting.setBackground(ContextCompat.getDrawable(ActivityMain.this, R.drawable.round_menu));
                    openFragment(new FragmentSetting());
                    return true;
            }
            return false;
        });
    }


    @SuppressLint("SetTextI18n")
    private void getSkuDetails() {
        List<String> skuList_sub = new ArrayList<>();
        skuList_sub.add(Preference.getClassic_monthly_key());
        skuList_sub.add(Preference.getPremium_monthly_key());
        skuList_sub.add(Preference.getMaster_monthly_key());
        skuList_sub.add(Preference.getOffer_time_key());
        skuList_sub.add(Preference.getVip_key());
        skuList_sub.add(Preference.getVip_close_key());
        skuList_sub.add(Preference.getClassic_yearly_key());
        skuList_sub.add(Preference.getPremium_yearly_key());
        skuList_sub.add(Preference.getMaster_yearly_key());
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList_sub).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(), (billingResult, list) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                Preference.setClassic_monthly_price(list.get(0).getPrice());
                Preference.setClassic_yearly_price(list.get(1).getPrice());
                Preference.setMaster_monthly_price(list.get(2).getPrice());
                Preference.setMaster_yearly_price(list.get(3).getPrice());
                Preference.setOffer_time_price(list.get(4).getPrice());
                Preference.setPremium_monthly_price(list.get(5).getPrice());
                Preference.setPremium_yearly_price(list.get(6).getPrice());
                Preference.setVip_close_price(list.get(7).getPrice());
                Preference.setVip_price(list.get(8).getPrice());
            }
        });
    }


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityMain.this, R.color.darkBlack));
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onBackPressed() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE2", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            Constant.BottomSheetDialogRateApp(ActivityMain.this);
            getSharedPreferences("PREFERENCE2", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
        } else {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ActivityMain.this);
            View inflate = LayoutInflater.from(ActivityMain.this).inflate(R.layout.dialog_exit, null);
            bottomSheetDialog.setContentView(inflate);

            LinearLayout llExit = inflate.findViewById(R.id.llExit);
            TextView txtExit = inflate.findViewById(R.id.txtExit);
            FrameLayout fl_native = inflate.findViewById(R.id.fl_native);

            if (Preference.getBooleanTheme(false)) {
                llExit.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
                txtExit.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            }

            Advertisement.showNativeAds(ActivityMain.this, fl_native);

            txtExit.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                finishAffinity();
            });
            bottomSheetDialog.show();
        }
    }

    private void CheckRedeem() {
        ApiInterface apiInterface = RetroClient.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.getCheckRedeem(Preference.getCheckRedeem(), Preference.getU_id()).enqueue(new Callback<CheckRedeemData>() {
            @Override
            public void onResponse(@NonNull Call<CheckRedeemData> call, @NonNull Response<CheckRedeemData> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getFlag().equals("true")) {
                        if (response.body().getAds1day().equals("true")) {
                            Preference.setAds_one_day("true");
                        } else {
                            Preference.setAds_one_day("");
                        }

                        if (response.body().getAds3days().equals("true")) {
                            Preference.setAds_three_day("true");
                        } else {
                            Preference.setAds_three_day("");
                        }

                        if (response.body().getAds7day().equals("true")) {
                            Preference.setAds_seven_day("true");
                        } else {
                            Preference.setAds_seven_day("");
                        }

                        if (response.body().getAds30days().equals("true")) {
                            Preference.setAds_thirty_day("true");
                        } else {
                            Preference.setAds_thirty_day("");
                        }

                        if (response.body().getBulksender1day().equals("true")) {
                            Preference.setBulk_one_day("true");
                        } else {
                            Preference.setBulk_one_day("");
                        }

                        if (response.body().getBulksender7day().equals("true")) {
                            Preference.setBulk_seven_day("true");
                        } else {
                            Preference.setBulk_seven_day("");
                        }

                        if (response.body().getAutoresponce1day().equals("true")) {
                            Preference.setAuto_one_day("true");
                        } else {
                            Preference.setAuto_one_day("");
                        }

                        if (response.body().getAutoresponce7day().equals("true")) {
                            Preference.setAuto_seven_day("true");
                        } else {
                            Preference.setAuto_seven_day("");
                        }

                        if (response.body().getImportexcel1day().equals("true")) {
                            Preference.setImport_one_day("true");
                        } else {
                            Preference.setImport_one_day("");
                        }

                        if (response.body().getImportexcel7day().equals("true")) {
                            Preference.setImport_seven_day("true");
                        } else {
                            Preference.setImport_seven_day("");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckRedeemData> call, @NonNull Throwable t) {

            }
        });
    }


    @Override
    public void onPurchasesUpdated(@NonNull @NotNull BillingResult billingResult, @Nullable @org.jetbrains.annotations.Nullable List<Purchase> list) {

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Utils.showSnack(ActivityMain.this, rlMain, isConnected);
    }

    public class loadDataAsync extends AsyncTask<Void, Void, Void> {
        DocumentFile[] allFiles;

        loadDataAsync() {
        }

        public void onPreExecute() {
            super.onPreExecute();
        }


        public Void doInBackground(Void... voidArr) {
            allFiles = null;
            DocumentFile[] fromSdcard = getFromSdcard();
            allFiles = fromSdcard;
            assert fromSdcard != null;
            Arrays.sort(fromSdcard, new Comparator<DocumentFile>() {
                @Override
                public int compare(DocumentFile o1, DocumentFile o2) {
                    return Long.compare(((DocumentFile) o1).lastModified(), ((DocumentFile) o2).lastModified());
                }
            });
            int i = 0;
            while (true) {
                DocumentFile[] documentFileArr = allFiles;
                if (i >= documentFileArr.length - 1) {
                    return null;
                }
                if (!documentFileArr[i].getUri().toString().contains(".nomedia")) {
                    arrayListStatus.add(new StatusModel(allFiles[i].getUri().toString()));
                }
                i++;
            }
        }

        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);

        }
    }
}