package com.whatscan.toolkit.forwa;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.whatscan.toolkit.forwa.GetSet.StickerArray;
import com.whatscan.toolkit.forwa.GetSet.StickerArrayList;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class StickerContentProvider extends ContentProvider {
    public static final String STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier";
    public static final String STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name";
    public static final String STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher";
    public static final String STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon";
    public static final String ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link";
    public static final String IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link";
    public static final String PUBLISHER_EMAIL = "sticker_pack_publisher_email";
    public static final String PUBLISHER_WEBSITE = "sticker_pack_publisher_website";
    public static final String PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website";
    public static final String LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website";
    public static final String STICKER_FILE_NAME_IN_QUERY = "sticker_file_name";
    public static final String STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji";
    public static final String CONTENT_SCHEME = "content";
    static final String METADATA = "metadata";
    static final String STICKERS = "stickers";
    static final String STICKERS_ASSET = "stickers_asset";
    private static final String TAG = StickerContentProvider.class.getSimpleName();
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int METADATA_CODE = 1;
    private static final int METADATA_CODE_FOR_SINGLE_PACK = 2;
    private static final int STICKERS_CODE = 3;
    private static final int STICKERS_ASSET_CODE = 4;
    private static final int STICKER_PACK_TRAY_ICON_CODE = 5;
    public static Uri AUTHORITY_URI = new Uri.Builder().scheme(StickerContentProvider.CONTENT_SCHEME).authority(BuildConfig.CONTENT_PROVIDER_AUTHORITY).appendPath(StickerContentProvider.METADATA).build();

    @Override
    public boolean onCreate() {
        Hawk.init(getContext()).build();
        final String authority = BuildConfig.CONTENT_PROVIDER_AUTHORITY;
        if (!authority.startsWith(Objects.requireNonNull(getContext()).getPackageName())) {
            throw new IllegalStateException("your authority (" + authority + ") for the content provider should start with your package name: " + getContext().getPackageName());
        }

        MATCHER.addURI(authority, METADATA, METADATA_CODE);
        MATCHER.addURI(authority, METADATA + "/*", METADATA_CODE_FOR_SINGLE_PACK);
        MATCHER.addURI(authority, STICKERS + "/*", STICKERS_CODE);

        for (StickerArray stickerPack : getStickerPackList()) {
            MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.id + "/" + stickerPack.catagery_image, STICKER_PACK_TRAY_ICON_CODE);
            if (stickerPack.getStickers() != null) {
                for (StickerArrayList sticker : stickerPack.getStickers()) {
                    MATCHER.addURI(authority, STICKERS_ASSET + "/" + stickerPack.id + "/" + sticker.sticker_image, STICKERS_ASSET_CODE);
                }
            }
        }

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int code = MATCHER.match(uri);
        Log.d(TAG, "query: " + code + uri);
        if (code == METADATA_CODE) {
            return getPackForAllStickerPacks(uri);
        } else if (code == METADATA_CODE_FOR_SINGLE_PACK) {
            return getCursorForSingleStickerPack(uri);
        } else if (code == STICKERS_CODE) {
            return getStickersForAStickerPack(uri);
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public AssetFileDescriptor openAssetFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        MATCHER.match(uri);
        final int matchCode = MATCHER.match(uri);
        final List<String> pathSegments = uri.getPathSegments();
        Log.d(TAG, "openFile: " + matchCode + uri + "\n" + uri.getAuthority()
                + "\n" + pathSegments.get(pathSegments.size() - 3) + "/"
                + "\n" + pathSegments.get(pathSegments.size() - 2) + "/"
                + "\n" + pathSegments.get(pathSegments.size() - 1));
        return getImageAsset(uri);
    }

    private AssetFileDescriptor getImageAsset(Uri uri) throws IllegalArgumentException {
        AssetManager am = Objects.requireNonNull(getContext()).getAssets();
        final List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() != 3) {
            throw new IllegalArgumentException("path segments should be 3, uri is: " + uri);
        }
        String fileName = pathSegments.get(pathSegments.size() - 1);
        final String identifier = pathSegments.get(pathSegments.size() - 2);
        if (TextUtils.isEmpty(identifier)) {
            throw new IllegalArgumentException("identifier is empty, uri: " + uri);
        }
        if (TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("file name is empty, uri: " + uri);
        }

        for (StickerArray stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.id)) {
                if (fileName.equals(stickerPack.catagery_image)) {
                    return fetchFile(uri, am, fileName, identifier);
                } else {
                    for (StickerArrayList sticker : stickerPack.getStickers()) {
                        if (fileName.equals(sticker.sticker_image)) {
                            return fetchFile(uri, am, fileName, identifier);
                        }
                    }
                }
            }
        }
        return null;
    }

    private AssetFileDescriptor fetchFile(@NonNull Uri uri, @NonNull AssetManager am, @NonNull String fileName, @NonNull String identifier) {
        try {
            File file;
            if (fileName.endsWith(".png")) {
                file = new File(getContext().getFilesDir() + "/" + "stickers_asset" + "/" + identifier + "/try/", fileName);
            } else {
                file = new File(getContext().getFilesDir() + "/" + "stickers_asset" + "/" + identifier + "/", fileName);
            }
            if (!file.exists()) {
                Log.d("fetFile", "StickerPack dir not found");
            }
            Log.d("fetchFile", "StickerPack " + file.getPath());
            return new AssetFileDescriptor(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY), 0L, -1L);
        } catch (IOException e) {
            Log.e(Objects.requireNonNull(getContext()).getPackageName(), "IOException when getting asset file, uri:" + uri, e);
            return null;
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int matchCode = MATCHER.match(uri);
        switch (matchCode) {
            case METADATA_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case METADATA_CODE_FOR_SINGLE_PACK:
                return "vnd.android.cursor.item/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA;
            case STICKERS_CODE:
                return "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + STICKERS;
            case STICKERS_ASSET_CODE:
                return "image/webp";
            case STICKER_PACK_TRAY_ICON_CODE:
                return "image/png";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    public List<StickerArray> getStickerPackList() {
        return (List) Hawk.get("whatsapp_sticker_packs", new ArrayList<StickerArray>());
    }

    private Cursor getPackForAllStickerPacks(@NonNull Uri uri) {
        return getStickerPackInfo(uri, getStickerPackList());
    }

    private Cursor getCursorForSingleStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        for (StickerArray stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.id)) {
                return getStickerPackInfo(uri, Collections.singletonList(stickerPack));
            }
        }

        return getStickerPackInfo(uri, new ArrayList<StickerArray>());
    }

    @NonNull
    private Cursor getStickerPackInfo(@NonNull Uri uri, @NonNull List<StickerArray> stickerPackList) {
        MatrixCursor cursor = new MatrixCursor(
                new String[]{
                        STICKER_PACK_IDENTIFIER_IN_QUERY,
                        STICKER_PACK_NAME_IN_QUERY,
                        STICKER_PACK_PUBLISHER_IN_QUERY,
                        STICKER_PACK_ICON_IN_QUERY,
                        ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                        IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                        PUBLISHER_EMAIL,
                        PUBLISHER_WEBSITE,
                        PRIVACY_POLICY_WEBSITE,
                        LICENSE_AGREENMENT_WEBSITE
                });
        for (StickerArray stickerPack : stickerPackList) {
            MatrixCursor.RowBuilder builder = cursor.newRow();
            builder.add(stickerPack.id);
            builder.add(stickerPack.catagery_title);
            builder.add(stickerPack.publisher_name);
            builder.add(stickerPack.catagery_image);
            builder.add("https://play.google.com/store/apps/details?id=" + getContext().getPackageName());
            builder.add("");
            builder.add("whatscantoolkit@gmail.com");
            builder.add("");
            builder.add("");
            builder.add("");
        }
        Log.d(TAG, "getStickerPackInfo: " + stickerPackList.size());
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @NonNull
    private Cursor getStickersForAStickerPack(@NonNull Uri uri) {
        final String identifier = uri.getLastPathSegment();
        MatrixCursor cursor = new MatrixCursor(new String[]{STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY});
        for (StickerArray stickerPack : getStickerPackList()) {
            if (identifier.equals(stickerPack.id)) {
                for (StickerArrayList sticker : stickerPack.getStickers()) {
                    cursor.addRow(new Object[]{sticker.sticker_image, ""});
                }
            }
        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported");
    }
}