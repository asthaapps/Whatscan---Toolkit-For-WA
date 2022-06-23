package com.whatscan.toolkit.forwa.WSticker.editimage.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static boolean checkFileExist(final String path) {
        if (TextUtils.isEmpty(path))
            return false;

        File file = new File(path);
        return file.exists();
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    public static void ablumUpdate(final Context context, final String dstPath) {
        if (TextUtils.isEmpty(dstPath) || context == null)
            return;

        File file = new File(dstPath);

        if (!file.exists() || file.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues(2);
        String extensionName = getExtensionName(dstPath);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + (TextUtils.isEmpty(extensionName) ? "jpeg" : extensionName));
        values.put(MediaStore.Images.Media.DATA, dstPath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static File from(Context context, Uri uri) throws IOException {
        FileOutputStream fileOutputStream;
        InputStream openInputStream = context.getContentResolver().openInputStream(uri);
        String fileName = getFileName(context, uri);
        String[] splitFileName = splitFileName(fileName);
        File rename = rename(File.createTempFile(splitFileName[0], splitFileName[1]), fileName);
        rename.deleteOnExit();
        try {
            fileOutputStream = new FileOutputStream(rename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fileOutputStream = null;
        }
        if (openInputStream != null) {
            copy(openInputStream, fileOutputStream);
            openInputStream.close();
        }
        if (fileOutputStream != null) {
            fileOutputStream.close();
        }
        return rename;
    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private static File rename(File file, String str) {
        File file2 = new File(file.getParent(), str);
        if (!file2.equals(file)) {
            if (file2.exists() && file2.delete()) {
                Log.d(TAG, "Delete old " + str + " file");
            }
            if (file.renameTo(file2)) {
                Log.d(TAG, "Rename file to " + str);
            }
        }
        return file2;
    }

    private static String[] splitFileName(String str) {
        String str2;
        int lastIndexOf = str.lastIndexOf(".");
        if (lastIndexOf != -1) {
            String substring = str.substring(0, lastIndexOf);
            str2 = str.substring(lastIndexOf);
            str = substring;
        } else {
            str2 = "";
        }
        return new String[]{str, str2};
    }

    private static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 == read) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }
}