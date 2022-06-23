package com.whatscan.toolkit.forwa.Service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.whatscan.toolkit.forwa.BulkSender.BulkActivityAttachment;
import com.whatscan.toolkit.forwa.BulkSender.helper.FileSharingUtils;
import com.whatscan.toolkit.forwa.R;
import com.google.android.gms.tasks.Continuation;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.ktx.StorageKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import kotlin.TypeCastException;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

public final class MyUploadService extends MyBaseTaskService {
    public static final String ACTION_UPLOAD = "action_upload";
    public static final Companion Companion = new Companion();
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";
    public static final String EXTRA_FILES = "extra_files";
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";
    private static final String TAG = "MyUploadService";
    MyUploadService myUploadService = this;
    private StorageReference storageRef;

    public final boolean broadcastUploadFinished(ArrayList<Uri> arrayList, ArrayList<File> arrayList2) {
        Intent putExtra = new Intent(arrayList != null ? UPLOAD_COMPLETED : UPLOAD_ERROR).putParcelableArrayListExtra(EXTRA_DOWNLOAD_URL, arrayList).putExtra(EXTRA_FILE_URI, arrayList2);
        return LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(putExtra);
    }

    public final void showUploadFinishedNotification(ArrayList<Uri> arrayList, ArrayList<File> arrayList2) {
        a();
        @SuppressLint("WrongConstant") Intent addFlags = new Intent(this, BulkActivityAttachment.class).putParcelableArrayListExtra(EXTRA_DOWNLOAD_URL, arrayList).putExtra(EXTRA_FILE_URI, arrayList2).addFlags(603979776);
        boolean z = arrayList != null;
        String string = getString(z ? R.string.preparation_success : R.string.preparation_failed);
        b(string, addFlags, z);
    }

    private void uploadFromUri(ArrayList<Uri> arrayList, ArrayList<File> arrayList2) {

        myUploadService.taskStarted(arrayList.size());
        String string = myUploadService.getString(R.string.preparing_file_sharing);
        c(string, 0, 0);
        Ref.IntRef intRef = new Ref.IntRef();
        intRef.element = 0;
        for (Uri t : arrayList) {
            File file = arrayList2.get(intRef.element);
            String name = file.getName();
            if (name != null) {
                FileSharingUtils fileSharingUtils = FileSharingUtils.INSTANCE;
                File file2 = arrayList2.get(intRef.element);
                String fileType = fileSharingUtils.getFileType(FilesKt.getExtension(file2));
                StorageReference storageReference = myUploadService.storageRef;
                if (storageReference == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("storageRef");
                }
                StorageReference child = storageReference.child(fileType).child(name);
                StorageMetadata storageMetadata = StorageKt.storageMetadata(builder -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(fileType);
                    sb.append('/');
                    Object obj = arrayList2.get(intRef.element);
                    sb.append(FilesKt.getExtension((File) obj));
                    builder.setContentType(sb.toString());
                    builder.setCustomMetadata("size", FileSharingUtils.INSTANCE.getFileSizeInString(arrayList2.get(intRef.element).length()));
                    Object obj2 = arrayList2.get(intRef.element);
                    builder.setCustomMetadata("extension", FilesKt.getExtension((File) obj2));
                    return null;
                });
                intRef.element++;
                Log.d(TAG, "uploadFromUri:dst:" + child.getPath());
                child.putFile(t, storageMetadata).addOnProgressListener(taskSnapshot -> {
                    long component1 = StorageKt.component1(taskSnapshot);
                    long component2 = StorageKt.component2(taskSnapshot);
                    String string1 = myUploadService.getString(R.string.preparing_file_sharing);
                    myUploadService.c(string1, component1, component2);
                }).continueWithTask((Continuation) task -> {
                    if (!task.isSuccessful()) {
                        Exception exception = task.getException();
                        if (exception == null) {
                            Intrinsics.throwNpe();
                        }
                        throw exception;
                    }
                    Log.d("MyUploadService", "uploadFromUri: upload success");
                    child.getDownloadUrl();
                    return null;
                }).addOnSuccessListener(uri -> {
                    Log.d("MyUploadService", "getDownloadUri " + uri.toString());
                    arrayList.add((Uri) uri);
                    myUploadService.taskCompleted();
                    if (arrayList.size() == arrayList2.size()) {
                        boolean unused = myUploadService.broadcastUploadFinished(arrayList, arrayList2);
                        myUploadService.showUploadFinishedNotification(arrayList, arrayList2);
                    }
                }).addOnFailureListener(exc -> {
                    Log.w("MyUploadService", "uploadFromUri:onFailure", exc);
                    boolean unused = myUploadService.broadcastUploadFinished(arrayList, arrayList2);
                    myUploadService.showUploadFinishedNotification(arrayList, arrayList2);
                    myUploadService.taskCompleted();
                });
            }
        }
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
        if (!Intrinsics.areEqual(ACTION_UPLOAD, intent.getAction())) {
            return START_NOT_STICKY;
        }
        ArrayList<Uri> parcelableArrayListExtra = intent.getParcelableArrayListExtra(EXTRA_FILE_URI);
        if (parcelableArrayListExtra != null) {
            Serializable serializableExtra = intent.getSerializableExtra(EXTRA_FILES);
            if (serializableExtra != null) {
                uploadFromUri(parcelableArrayListExtra, (ArrayList) serializableExtra);
                return START_REDELIVER_INTENT;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.ArrayList<java.io.File> /* = java.util.ArrayList<java.io.File> */");
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.ArrayList<android.net.Uri> /* = java.util.ArrayList<android.net.Uri> */");
    }

    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final IntentFilter getIntentFilter() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MyUploadService.UPLOAD_COMPLETED);
            intentFilter.addAction(MyUploadService.UPLOAD_ERROR);
            return intentFilter;
        }
    }
}