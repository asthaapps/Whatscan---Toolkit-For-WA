package com.whatscan.toolkit.forwa.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.whatscan.toolkit.forwa.GetSet.ChatLockModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chatlockinfo.db";
    private static final String KEY_APP_NAME = "appname";
    private static final String KEY_ID = "id";
    private static final String KEY_ISCHECKTOLOCK = "isToCheckLock";
    private static final String KEY_ISLOCK = "isLock";
    private static final String KEY_USER_NAME = "username";
    private static final String TABLE_CHATLOCK = "chatlock";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE chatlock(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,isLock INTEGER,isToCheckLock INTEGER,appname TEXT)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS chatlock");
        onCreate(sQLiteDatabase);
    }

    public void addChatLockInfo(ChatLockModel homeModel) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_NAME, homeModel.getUsername());
        contentValues.put(KEY_APP_NAME, homeModel.getAppName());
        contentValues.put(KEY_ISLOCK, homeModel.getIsLock());
        contentValues.put(KEY_ISCHECKTOLOCK, homeModel.getIsToCheckLock());
        long insert = readableDatabase.insert(TABLE_CHATLOCK, null, contentValues);
        readableDatabase.close();
    }

    public List<ChatLockModel> getAllChatLock() {
        ArrayList<ChatLockModel> arrayList = new ArrayList<>();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("SELECT  * FROM chatlock", null);
        if (rawQuery.moveToFirst()) {
            do {
                ChatLockModel homeModel = new ChatLockModel();
                homeModel.setId(rawQuery.getInt(rawQuery.getColumnIndex(KEY_ID)));
                homeModel.setAppName(rawQuery.getString(rawQuery.getColumnIndex(KEY_APP_NAME)));
                homeModel.setUsername(rawQuery.getString(rawQuery.getColumnIndex(KEY_USER_NAME)));
                homeModel.setIsLock(rawQuery.getInt(rawQuery.getColumnIndex(KEY_ISLOCK)));
                homeModel.setIsToCheckLock(rawQuery.getInt(rawQuery.getColumnIndex(KEY_ISCHECKTOLOCK)));
                arrayList.add(homeModel);
            } while (rawQuery.moveToNext());
        }
        writableDatabase.close();
        return arrayList;
    }

    public void updateChatLock(ChatLockModel homeModel) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, homeModel.getId());
        contentValues.put(KEY_USER_NAME, homeModel.getUsername());
        contentValues.put(KEY_APP_NAME, homeModel.getAppName());
        contentValues.put(KEY_ISLOCK, homeModel.getIsLock());
        contentValues.put(KEY_ISCHECKTOLOCK, homeModel.getIsToCheckLock());
        writableDatabase.update(TABLE_CHATLOCK, contentValues, "id = ?", new String[]{String.valueOf(homeModel.getId())});
    }

    public void deleteChatInfo(ChatLockModel homeModel) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(TABLE_CHATLOCK, "id = ?", new String[]{String.valueOf(homeModel.getId())});
        writableDatabase.close();
    }
}
