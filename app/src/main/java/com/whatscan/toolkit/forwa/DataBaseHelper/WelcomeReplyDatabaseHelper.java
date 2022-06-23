package com.whatscan.toolkit.forwa.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WelcomeReplyDatabaseHelper extends SQLiteOpenHelper {
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "INCOMINGMSG";
    public static final String COL_4 = "DATETIME";
    public static final String DATABASE_NAME = "welcome.db";
    public static final String TABLE_NAME = "welcome_table";

    public WelcomeReplyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table welcome_table (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT unique,INCOMINGMSG TEXT,DATETIME TEXT default 0  NOT NULL)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS welcome_table");
        onCreate(sQLiteDatabase);
    }

    public boolean insertData(String str, String str2, String str3) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, str);
        contentValues.put("INCOMINGMSG", str2);
        contentValues.put(COL_4, str3);
        return readableDatabase.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public Cursor getAllData() {
        return getReadableDatabase().rawQuery("select * from welcome_table", null);
    }

    public void updateData(String str, String str2, String str3, String str4) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, str2);
        contentValues.put("INCOMINGMSG", str3);
        contentValues.put(COL_4, str4);
        readableDatabase.update(TABLE_NAME, contentValues, "ID = ?", new String[]{str});
    }

    public Integer deleteData(String str) {
        return getReadableDatabase().delete(TABLE_NAME, "ID = ?", new String[]{str});
    }

    public void deleteAllTxt(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        readableDatabase.execSQL("DELETE FROM welcome_table WHERE NAME = '" + str2 + "' and " + COL_4 + " = '" + str + "'");
    }

    public void deleteAllChats() {
        getReadableDatabase().execSQL("DELETE FROM welcome_table");
    }
}
