package com.whatscan.toolkit.forwa.BulkSender.helper;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.WorkerThread;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;

import kotlin.text.StringsKt;

public final class ContentUriUtils {
    public static final ContentUriUtils INSTANCE = new ContentUriUtils();

    private ContentUriUtils() {
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @WorkerThread
    @Nullable
    public final String getFilePath(@NotNull Context context, @NotNull Uri uri) throws URISyntaxException {
        String[] strArr;
        String str;
        if (DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                String documentId = DocumentsContract.getDocumentId(uri);
                Object[] array = StringsKt
                        .split(documentId, new String[]{":"}, false, 0).toArray(new String[0]);
                return Environment.getExternalStorageDirectory().toString() + "/" + ((String[]) array)[1];
            } else if (isDownloadsDocument(uri)) {
                String documentId2 = DocumentsContract.getDocumentId(uri);
                if (StringsKt.contains(documentId2, ":", false)) {
                    documentId2 = StringsKt.split(documentId2, new String[]{":"}, false, 0).get(1);
                }
                try {
                    Uri parse = Uri.parse("content://downloads/public_downloads");
                    long valueOf = Long.parseLong(documentId2);
                    uri = ContentUris.withAppendedId(parse, valueOf);
                } catch (NumberFormatException unused) {
                    uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), 0);
                }
            } else if (isMediaDocument(uri)) {
                String documentId3 = DocumentsContract.getDocumentId(uri);
                Object[] array2 = StringsKt.split(documentId3, new String[]{":"}, false, 0).toArray(new String[0]);
                String[] strArr2 = (String[]) array2;
                String str2 = strArr2[0];
                switch (str2) {
                    case "image":
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                }

                str = "_id=?";
                strArr = new String[]{strArr2[1]};

                if ("content".equalsIgnoreCase(uri.getScheme())) {
                    if (isGooglePhotosUri(uri)) {
                        return uri.getLastPathSegment();
                    }
                    String[] projection = {MediaStore.Images.Media.DATA
                    };
                    Cursor cursor;
                    try {
                        cursor = context.getContentResolver().query(uri, projection, str, strArr, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        if (cursor.moveToFirst()) {
                            return cursor.getString(column_index);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
                return null;
            }
        }
        uri.getScheme().equals("content");
        return null;
    }
}