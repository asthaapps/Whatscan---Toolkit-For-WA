package com.whatscan.toolkit.forwa.BulkSender;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.whatscan.toolkit.forwa.Adapter.ImportedFileAdapter;
import com.whatscan.toolkit.forwa.Ads.Advertisement;
import com.whatscan.toolkit.forwa.BulkSender.helper.DownlodExportUtil;
import com.whatscan.toolkit.forwa.DataBaseHelper.db.GroupDatabase;
import com.whatscan.toolkit.forwa.R;
import com.whatscan.toolkit.forwa.Util.Constant;
import com.whatscan.toolkit.forwa.Util.Preference;
import com.whatscan.toolkit.forwa.Util.ProgressDialogUtils;
import com.whatscan.toolkit.forwa.Util.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Nullable;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.jvm.internal.Ref;

public class BulkActivityImportedFiles extends AppCompatActivity {
    public RelativeLayout rlGroup, ic_include;
    public LinearLayout llInstruction;
    public RecyclerView recycleAddGroup;
    public ImageView la_back;
    public ImageView imgSample;
    public TextView tv_toolbar, txtCSVFileDownlod, txtNoGroup, txtNoGroupSub;
    public ImportedFileAdapter importedFileAdapter;
    public FloatingActionButton btnAddGroup;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Constant.adjustFontScale(getResources().getConfiguration(), BulkActivityImportedFiles.this);
        setContentView(R.layout.activity_add_group);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(BulkActivityImportedFiles.this, ll_banner);

        rlGroup = findViewById(R.id.rlGroup);
        ic_include = findViewById(R.id.ic_include);
        la_back = findViewById(R.id.la_back);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        btnAddGroup = findViewById(R.id.btnAddGroup);
        recycleAddGroup = findViewById(R.id.recycleAddGroup);
        txtCSVFileDownlod = findViewById(R.id.txtCSVFileDownlod);
        imgSample = findViewById(R.id.imgSample);
        llInstruction = findViewById(R.id.llInstruction);
        txtNoGroup = findViewById(R.id.txtNoGroup);
        txtNoGroupSub = findViewById(R.id.txtNoGroupSub);

        tv_toolbar.setText(Html.fromHtml("<small>" + getString(R.string.import_contact) + "</small>"));
        txtNoGroup.setText(getString(R.string.no_import_yet_created));
        txtNoGroupSub.setText(getString(R.string.tap_add_button_create_group));

        if (Preference.getBooleanTheme(false)) {
            setStatusBarTheme();
            rlGroup.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBlack));
            txtNoGroup.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtNoGroupSub.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtCSVFileDownlod.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_w));
            txtCSVFileDownlod.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            setStatusBar();
            rlGroup.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            ic_include.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            txtNoGroup.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtNoGroupSub.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            txtCSVFileDownlod.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border));
            txtCSVFileDownlod.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        imgSample.setVisibility(View.GONE);
        txtCSVFileDownlod.setVisibility(View.VISIBLE);

        importedFileAdapter = new ImportedFileAdapter(this);
        recycleAddGroup.setAdapter(importedFileAdapter);

        la_back.setOnClickListener(v -> onBackPressed());

        btnAddGroup.setOnClickListener(v -> startCreatingNewGroup());

        txtCSVFileDownlod.setOnClickListener(v -> {
            Ref.ObjectRef<File> objectRef = new Ref.ObjectRef<>();
            objectRef.element = null;
            ProgressDialogUtils.displayProgress(BulkActivityImportedFiles.this, "Downloading..");
            Completable.fromRunnable(() -> objectRef.element = new DownlodExportUtil().downloadSampleCsv()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                File t = (File) objectRef.element;
                String sb = "File Downloaded at " + (t != null ? t.getAbsolutePath() : null);
                Utils.showToast(BulkActivityImportedFiles.this, sb);
                ProgressDialogUtils.stopProgressDisplay();
            }, throwable -> Utils.showToast(BulkActivityImportedFiles.this, "Something went wrong Please try again"));
        });

        ImportedFileList();
    }

    private GroupDatabase getGroupDatabase() {
        return GroupDatabase.getInstance(BulkActivityImportedFiles.this);
    }

    public final ImportedFileAdapter getGroupSelectionAdapter() {
        return new ImportedFileAdapter(BulkActivityImportedFiles.this);
    }

    @SuppressLint("CheckResult")
    private void ImportedFileList() {
        ProgressDialogUtils.displayProgress(this);
        getGroupDatabase().importedFileDao().getAll().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {
            ProgressDialogUtils.stopProgressDisplay();

            if (!list.isEmpty()) {
                llInstruction.setVisibility(View.GONE);
                recycleAddGroup.setVisibility(View.VISIBLE);
                importedFileAdapter.a = list;
                importedFileAdapter.notifyDataSetChanged();
                return;
            }

            llInstruction.setVisibility(View.VISIBLE);
            recycleAddGroup.setVisibility(View.GONE);
        });
    }

    public final void startCreatingNewGroup() {
        startActivityForResult(new Intent(this, BulkActivityImportedContact.class), 4321);
    }

    @Override
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 4321 && i2 == RESULT_OK) {
            ImportedFileList();
            importedFileAdapter.notifyDataSetChanged();
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