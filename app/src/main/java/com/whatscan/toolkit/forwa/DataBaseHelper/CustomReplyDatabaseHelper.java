package com.whatscan.toolkit.forwa.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomReplyDatabaseHelper extends SQLiteOpenHelper {
    public static final String COL_1 = "ID";
    public static final String COL_2 = "INCOMINGMSG";
    public static final String COL_3 = "REPLYMSG";
    public static final String COL_4 = "RADIOSELECTION";
    public static final String DATABASE_NAME = "reply.db";
    public static final String TABLE_NAME = "reply_table";

    public CustomReplyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table reply_table (ID INTEGER PRIMARY KEY AUTOINCREMENT,INCOMINGMSG TEXT unique,REPLYMSG TEXT,RADIOSELECTION TEXT default 0  NOT NULL)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS reply_table");
        onCreate(sQLiteDatabase);
    }

    public boolean insertData(String str, String str2, String str3) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("INCOMINGMSG", str);
        contentValues.put(COL_3, str2);
        contentValues.put(COL_4, str3);
        return readableDatabase.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public Cursor getAllData() {
        return getReadableDatabase().rawQuery("select * from reply_table", null);
    }

    public void updateData(String str, String str2, String str3, String str4) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("INCOMINGMSG", str2);
        contentValues.put(COL_3, str3);
        contentValues.put(COL_4, str4);
        readableDatabase.update(TABLE_NAME, contentValues, "ID = ?", new String[]{str});
    }

    public void deleteData(String str) {
        getReadableDatabase().delete(TABLE_NAME, "ID = ?", new String[]{str});
    }

    public void deleteAllTxt(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        readableDatabase.execSQL("DELETE FROM reply_table WHERE INCOMINGMSG = '" + str2 + "' and " + COL_4 + " = '" + str + "'");
    }

    public void deleteAllChats() {
        getReadableDatabase().execSQL("DELETE FROM reply_table");
    }
}
