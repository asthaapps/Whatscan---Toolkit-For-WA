package com.whatscan.toolkit.forwa.Service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.whatscan.toolkit.forwa.BulkSender.BulkActivityAttachment;
import com.whatscan.toolkit.forwa.R;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.ktx.StorageKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public final class MyDownloadService extends MyBaseTaskService {
    public static final String ACTION_DOWNLOAD = "action_download";
    public static final Companion Companion = new Companion();
    public static final String DOWNLOAD_COMPLETED = "download_completed";
    public static final String DOWNLOAD_ERROR = "download_error";
    public static final String EXTRA_BYTES_DOWNLOADED = "extra_bytes_downloaded";
    public static final String EXTRA_DOWNLOAD_PATH = "extra_download_path";
    private static final String TAG = "Storage#DownloadService";
    private StorageReference storageRef;

    public final boolean broadcastDownloadFinished(String str, long j) {
        Intent putExtra = new Intent((j > -1 ? 1 : (j == -1 ? 0 : -1)) != 0 ? DOWNLOAD_COMPLETED : DOWNLOAD_ERROR).putExtra(EXTRA_DOWNLOAD_PATH, str).putExtra(EXTRA_BYTES_DOWNLOADED, j);
        return LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(putExtra);
    }

    private void downloadFromPath(String str) {
        Log.d(TAG, "downloadFromPath:" + str);
        taskStarted(1);
        String string = getString(R.string.progress_downloading);
        c(string, 0, 0);
        StorageReference storageReference = this.storageRef;
        if (storageReference == null) {
            Intrinsics.throwUninitializedPropertyAccessException("storageRef");
        }
        storageReference.child(str).getStream((taskSnapshot, inputStream) -> {
            long component2 = StorageKt.component2(taskSnapshot);
            byte[] bArr = new byte[1024];
            int read = inputStream.read(bArr);
            long j = 0;
            while (read != -1) {
                long j2 = j + ((long) read);
                String string1 = getString(R.string.progress_downloading);
                c(string1, j2, component2);
                read = inputStream.read(bArr);
                j = j2;
            }
            inputStream.close();
        }).addOnSuccessListener(taskSnapshot -> {
            long component2 = StorageKt.component2(taskSnapshot);
            Log.d("Storage#DownloadService", "download:SUCCESS");
            boolean unused = broadcastDownloadFinished(str, component2);
            showDownloadFinishedNotification(str, (int) component2);
            taskCompleted();
        }).addOnFailureListener(exc -> {
            Log.w("Storage#DownloadService", "download:FAILURE", exc);
            boolean unused = broadcastDownloadFinished(str, -1);
            showDownloadFinishedNotification(str, -1);
            taskCompleted();
        });
    }


    public final void showDownloadFinishedNotification(String str, int i) {
        String str2;
        a();
        @SuppressLint("WrongConstant") Intent addFlags = new Intent(this, BulkActivityAttachment.class).putExtra(EXTRA_DOWNLOAD_PATH, str).putExtra(EXTRA_BYTES_DOWNLOADED, i).addFlags(603979776);
        if (i != -1) {
            str2 = getString(R.string.download_success);
        } else {
            str2 = getString(R.string.download_failure);
        }
        b(str2, addFlags, true);
    }

    @Nullable
    public IBinder onBind(@NotNull Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        StorageReference reference = StorageKt.getStorage(Firebase.INSTANCE).getReference();
        this.storageRef = reference;
    }

    public int onStartCommand(@NotNull Intent intent, int i, int i2) {
        Log.d(TAG, "onStartCommand:" + intent + ':' + i2);
        if (!Intrinsics.areEqual(ACTION_DOWNLOAD, intent.getAction())) {
            return START_REDELIVER_INTENT;
        }
        String stringExtra = intent.getStringExtra(EXTRA_DOWNLOAD_PATH);
        if (stringExtra == null) {
            Intrinsics.throwNpe();
        }
        downloadFromPath(stringExtra);
        return START_REDELIVER_INTENT;
    }

    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final IntentFilter getIntentFilter() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MyDownloadService.DOWNLOAD_COMPLETED);
            intentFilter.addAction(MyDownloadService.DOWNLOAD_ERROR);
            return intentFilter;
        }
    }
}