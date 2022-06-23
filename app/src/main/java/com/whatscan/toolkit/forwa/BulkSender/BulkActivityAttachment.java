package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.whatscan.toolkit.forwa.Adapter.FileSelectionAdapter;
import com.whatscan.toolkit.forwa.Adapter.FileSelectionMethodsAdapter;
import com.whatscan.toolkit.forwa.Ads.ActivityPremium;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.BulkSender.helper.FileSharingUtils;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Service.MyDownloadService;
import com.whatscan.toolkit.forwa.Service.MyUploadService;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Event;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.yalantis.ucrop.util.FileUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public class BulkActivityAttachment extends AppCompatActivity {
    public static final String KEY_FILE_URI = "key_file_uri";
    public int max_file_size_allowed = 20, AdsType = 0;
    public double totalFileSize;
    public boolean isLoading;
    public BroadcastReceiver broadcastReceiver;
    public Uri fileUri;
    public FileSelectionAdapter fileSelectionAdapter;
    public CardView cardPreview;
    public RecyclerView filesRecyclerView, rvfileSelectionMethods;
    public RelativeLayout rlAttachment, ic_include;
    public TextView caption;
    public ProgressBar progressBar;
    public MaterialButton doneAttachment;
    public ImageView backButton;
    public RewardedAd rewardedAd;
    public ArrayList<File> filePaths = new ArrayList<>();
    public ArrayList<Uri> filePathUri = new ArrayList<>();
    private RewardedInterstitialAd rewardedInterstitialAd;

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityAttachment.this);
        setContentView(R.layout.activity_attachment);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(BulkActivityAttachment.this, ll_banner);

        loadFullReward(BulkActivityAttachment.this);
        cardPreview = findViewById(R.id.cardPreview);
        filesRecyclerView = findViewById(R.id.filesRecyclerView);
        rvfileSelectionMethods = findViewById(R.id.rvfileSelectionMethods);
        caption = findViewById(R.id.caption);
        progressBar = findViewById(R.id.progressBar);
        doneAttachment = findViewById(R.id.doneAttachment);
        backButton = findViewById(R.id.backButton);
        rlAttachment = findViewById(R.id.rlAttachment);
        ic_include = findViewById(R.id.ic_include);
        TextView txtOne = findViewById(R.id.txtOne);

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlAttachment.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            cardPreview.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorShape));
            txtOne.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rlAttachment.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            cardPreview.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        fileSelectionAdapter = new FileSelectionAdapter(BulkActivityAttachment.this, filePaths);
        filesRecyclerView.setAdapter(fileSelectionAdapter);

        Intent intent = getIntent();
        ArrayList<Uri> parcelableArrayListExtra = intent != null ? intent.getParcelableArrayListExtra(Event.BULK_SENDING_FILE_ATTACHMENT.name()) : null;
        if (parcelableArrayListExtra != null) {
            for (Uri uri : parcelableArrayListExtra) {
                updateList(uri);
            }
        }

        if (fileSelectionAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fileSelectionAdapter");
        }

        fileSelectionAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int i, int i2) {
                super.onItemRangeRemoved(i, i2);
                filePathUri.remove(i);
                totalFileSize = 0.0d;
                cardPreview.setVisibility(filePaths.size() > 0 ? View.GONE : View.VISIBLE);
            }
        });

        setFileSelectionMethodsUi();

        doneAttachment.setOnClickListener(v -> {
            ArrayList<String> arrayList = new ArrayList<>();
            for (Uri uri : filePathUri) {
                arrayList.add(uri.toString());
            }
            Intent intent1 = new Intent();
            intent1.putParcelableArrayListExtra(Event.BULK_SENDING_FILE_ATTACHMENT.name(), filePathUri);
            intent1.putExtra(Event.IS_SENDING_FILE_DIRECTLY.name(), true);
            setResult(-1, intent1);
            finish();
        });

        backButton.setOnClickListener(v -> onBackPressed());

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                hideProgressBar();
                String action = intent.getAction();
                if (action != null) {
                    switch (action.hashCode()) {
                        case -2091830899:
                            if (!action.equals(MyUploadService.UPLOAD_COMPLETED)) {
                                return;
                            }
                            break;
                        case -1358365110:
                            if (!action.equals(MyUploadService.UPLOAD_ERROR)) {
                                return;
                            }
                            break;
                        case 974485393:
                            if (action.equals(MyDownloadService.DOWNLOAD_ERROR)) {
                                Locale locale = Locale.getDefault();
                                String format = String.format(locale, "Failed to download from %s", Arrays.copyOf(new Object[]{intent.getStringExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH)}, 1));
                                showMessageDialog("Error", format);
                                return;
                            }
                            return;
                        case 1432465236:
                            if (action.equals(MyDownloadService.DOWNLOAD_COMPLETED)) {
                                long longExtra = intent.getLongExtra(MyDownloadService.EXTRA_BYTES_DOWNLOADED, 0);
                                String string = getString(R.string.success);
                                Locale locale2 = Locale.getDefault();
                                String format2 = String.format(locale2, "%d bytes downloaded from %s", Arrays.copyOf(new Object[]{longExtra, intent.getStringExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH)}, 2));
                                showMessageDialog(string, format2);
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                    ResultIntent(intent);
                }
            }
        };

        if (bundle != null) {
            fileUri = bundle.getParcelable(KEY_FILE_URI);
        }
        Intent intent2 = getIntent();
        onNewIntent(intent2);
    }

    private void setFileSelectionMethodsUi() {
        rvfileSelectionMethods.setAdapter(new FileSelectionMethodsAdapter(BulkActivityAttachment.this, fileType -> {
            if (fileType != null) {
                int i = WhenMappings.SwitchMapping[fileType.ordinal()];
                if (i == 1) {
                    AdsType = 1;
                    if (Preference.getActive_CkeyM().equals("true") || Preference.getActive_CkeyY().equals("true") || Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") ||
                            Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true") || Preference.getActive_offer().equals("true") || Preference.getActive_vip().equals("true")) {
                        openFileSelectionDocument("image/*");
                    } else {
                        ShowDialog();
                    }
                } else if (i == 2) {
                    AdsType = 2;
                    if (Preference.getActive_CkeyM().equals("true") || Preference.getActive_CkeyY().equals("true") || Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") ||
                            Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true") || Preference.getActive_offer().equals("true") || Preference.getActive_vip().equals("true")) {
                        openFileSelectionDocument("video/*");
                    } else {
                        ShowDialog();
                    }
                } else if (i == 3) {
                    AdsType = 3;
                    if (Preference.getActive_CkeyM().equals("true") || Preference.getActive_CkeyY().equals("true") || Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") ||
                            Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true") || Preference.getActive_offer().equals("true") || Preference.getActive_vip().equals("true")) {
                        openFileSelectionDocument("application/pdf");
                    } else {
                        ShowDialog();
                    }
                } else if (i == 4) {
                    AdsType = 4;
                    if (Preference.getActive_CkeyM().equals("true") || Preference.getActive_CkeyY().equals("true") || Preference.getActive_PkeyM().equals("true") || Preference.getActive_PkeyY().equals("true") ||
                            Preference.getActive_MkeyM().equals("true") || Preference.getActive_MkeyY().equals("true") || Preference.getActive_offer().equals("true") || Preference.getActive_vip().equals("true")) {
                        openFileSelectionDocument("audio/*");
                    } else {
                        ShowDialog();
                    }
                }
            }
        }));
    }

    private double calulateTotalFileSize(double d) {
        for (File filePath : filePaths) {
            d += FileSharingUtils.INSTANCE.getFileSizeInMB(filePath.length());
        }
        return d;
    }

    public final void hideProgressBar() {
        caption.setText("");
        progressBar.setVisibility(View.INVISIBLE);
    }

    public final void ResultIntent(Intent intent) {
        ArrayList<Uri> parcelableArrayListExtra = intent.getParcelableArrayListExtra(MyUploadService.EXTRA_DOWNLOAD_URL);
        if (parcelableArrayListExtra != null) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<String> arrayList2 = new ArrayList<>();
            ArrayList<String> arrayList3 = new ArrayList<>();
            ArrayList<String> arrayList4 = new ArrayList<>();
            for (Uri uri : parcelableArrayListExtra) {
                String uri2 = uri.toString();
                String str;
                if (uri2.contains("/image")) {
                    str = uri.toString();
                    arrayList.add(str);
                } else {
                    String uri3 = uri.toString();
                    if (uri3.contains("/video")) {
                        str = uri.toString();
                        arrayList2.add(str);
                    } else {
                        String uri4 = uri.toString();
                        if (uri4.contains("/pdf")) {
                            str = uri.toString();
                            arrayList3.add(str);
                        } else {
                            String uri5 = uri.toString();
                            if (uri5.contains("/audio")) {
                                str = uri.toString();
                                arrayList4.add(str);
                            }
                        }
                    }
                }
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.util.ArrayList<android.net.Uri>");
    }

    @SuppressLint("WrongConstant")
    public final void openFileSelectionDocument(String str) {
        if (filePathUri.size() >= 4 || totalFileSize >= ((double) max_file_size_allowed)) {
            showFileSizeLimitMessageDialog();
            return;
        }
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.putExtra("android.intent.extra.LOCAL_ONLY", true);
        intent.setType(str);
        intent.setFlags(1);
        startActivityForResult(intent, 101);
    }

    private void showFileSizeLimitMessageDialog() {
        AlertDialog create = new AlertDialog.Builder(this).setTitle("File sharing rule").setMessage("You are allowed to share maximum 4 files . \nMAX total size limit: 20 MB ").setPositiveButton("Ok", (dialog, which) -> {

        }).setNegativeButton("Learn more", (dialog, which) -> {
            Utils.showToast(BulkActivityAttachment.this, "Please chat with us!");
        }).create();
        create.show();
    }

    public final void showMessageDialog(String str, String str2) {
        AlertDialog create = new AlertDialog.Builder(this).setTitle(str).setMessage(str2).create();
        create.show();
    }

    private void updateList(Uri uri) {
        String filePath = FileUtils.getPath(BulkActivityAttachment.this, uri);
        if (filePath != null) {
            addFiles(filePath, uri);
        } else {
            showDownloadFilePickError();
        }
    }

    private void addFiles(String str, Uri uri) {
        File file = new File(str);
        double calulateTotalFileSize = calulateTotalFileSize(FileSharingUtils.INSTANCE.getFileSizeInMB(file.length()));
        if (calulateTotalFileSize < ((double) max_file_size_allowed)) {
            totalFileSize = calulateTotalFileSize;
            filePathUri.add(uri);
            filePaths.add(file);
            cardPreview.setVisibility(filePaths.size() > 0 ? View.GONE : View.VISIBLE);
            if (fileSelectionAdapter == null) {
                Intrinsics.throwUninitializedPropertyAccessException("fileSelectionAdapter");
            }
            fileSelectionAdapter.notifyDataSetChanged();
            filesRecyclerView.scrollToPosition(filePathUri.size() - 1);
            return;
        }
        showFileSizeLimitMessageDialog();
    }

    private void showDownloadFilePickError() {
        new AlertDialog.Builder(this).setTitle("Selection Failed").setMessage(Html.fromHtml("<strong>Problem: </strong>We faced issue in picking file from <strong>Download Folder</strong><br><br><strong>Solution: </strong><font color='#4CAF50'> Please copy your file to other folder from Download and then pick from there.</font></i> ")).setNegativeButton("Ok", (dialog, which) -> {
        }).show();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        Uri data;
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            Uri uri;
            if ((intent != null ? intent.getData() : null) != null) {
                try {
                    if (filePathUri.size() >= 4 || totalFileSize >= ((double) max_file_size_allowed)) {
                        showFileSizeLimitMessageDialog();
                        return;
                    }
                    ContentResolver contentResolver = getContentResolver();
                    uri = intent.getData();
                    if (uri == null) {
                        Intrinsics.throwNpe();
                    }
                    contentResolver.takePersistableUriPermission(uri, 1);
                    if ((data = intent.getData()) != null) {
                        updateList(data);
                    }
                } catch (Exception unused) {
                    unused.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onNewIntent(@NotNull Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(MyUploadService.EXTRA_DOWNLOAD_URL)) {
            ResultIntent(intent);
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(KEY_FILE_URI, fileUri);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this);
        if (broadcastReceiver == null) {
            Intrinsics.throwUninitializedPropertyAccessException("broadcastReceiver");
        }
        instance.registerReceiver(broadcastReceiver, MyDownloadService.Companion.getIntentFilter());
        if (broadcastReceiver == null) {
            Intrinsics.throwUninitializedPropertyAccessException("broadcastReceiver");
        }
        instance.registerReceiver(broadcastReceiver, MyUploadService.Companion.getIntentFilter());
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this);
        if (broadcastReceiver == null) {
            Intrinsics.throwUninitializedPropertyAccessException("broadcastReceiver");
        }
        instance.unregisterReceiver(broadcastReceiver);
    }

    private void ShowDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BulkActivityAttachment.this);
        View inflate = LayoutInflater.from(BulkActivityAttachment.this).inflate(R.layout.dailog_upgrade_ads, null);
        bottomSheetDialog.setContentView(inflate);

        LinearLayout rlUpgradeP = inflate.findViewById(R.id.rlUpgradeP);
        TextView txtHeader = inflate.findViewById(R.id.txtHeader);
        TextView freeTrialTextView = inflate.findViewById(R.id.freeTrialTextView);

        if (Preference.getBooleanTheme(false)) {
            rlUpgradeP.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtHeader.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            freeTrialTextView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        }

        inflate.findViewById(R.id.startUpgradeButton).setOnClickListener(v -> {
            startActivity(new Intent(BulkActivityAttachment.this, ActivityPremium.class), ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.startWatchAds).setOnClickListener(v -> {
            showRewardedVideo();
            bottomSheetDialog.dismiss();
        });

        inflate.findViewById(R.id.imgClose).setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
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

    public void loadFullReward(Context context) {
        isLoading = true;
        RewardedInterstitialAd.load(context, Preference.getRewardAds(),
                new AdRequest.Builder().build(), new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                        isLoading = false;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        isLoading = false;
                    }
                });

    }

    private void showRewardedVideo() {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdShowedFullScreenContent() {

                        }

                        /** Called when the ad failed to show full screen content. */
                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            // Don't forget to set the ad reference to null so you
                            // don't show the ad a second time.
                            rewardedInterstitialAd = null;
                            loadFullReward(BulkActivityAttachment.this);
                        }

                        /** Called when full screen content is dismissed. */
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Don't forget to set the ad reference to null so you
                            // don't show the ad a second time.
                            rewardedInterstitialAd = null;

                            // Preload the next rewarded interstitial ad.
                            loadFullReward(BulkActivityAttachment.this);
                        }
                    });


            rewardedInterstitialAd.show(BulkActivityAttachment.this, rewardItem -> {
                // Handle the reward.
                if (AdsType == 1) {
                    openFileSelectionDocument("image/*");
                } else if (AdsType == 2) {
                    openFileSelectionDocument("video/*");
                } else if (AdsType == 3) {
                    openFileSelectionDocument("application/pdf");
                } else if (AdsType == 4) {
                    openFileSelectionDocument("audio/*");
                }
            });
        }
    }

    public static final class WhenMappings {
        public static final int[] SwitchMapping;

        static {
            int[] iArr = new int[Event.values().length];
            SwitchMapping = iArr;
            iArr[Event.IMAGE.ordinal()] = 1;
            iArr[Event.VIDEO.ordinal()] = 2;
            iArr[Event.PDF.ordinal()] = 3;
            iArr[Event.AUDIO.ordinal()] = 4;
        }
    }
}