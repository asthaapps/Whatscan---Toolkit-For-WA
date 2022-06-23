package com.whatscan.toolkit.forwa.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupDatabaseHelper extends SQLiteOpenHelper {
    public static final String COL_1 = "ID";
    public static final String COL_2 = "GROUPNAME";
    public static final String DATABASE_NAME = "group.db";
    public static final String TABLE_NAME = "group_table";

    public GroupDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table group_table (ID INTEGER PRIMARY KEY AUTOINCREMENT,GROUPNAME TEXT unique default 0  NOT NULL)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS group_table");
        onCreate(sQLiteDatabase);
    }

    public boolean insertData(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, str);
        return readableDatabase.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public Cursor getAllData() {
        return getReadableDatabase().rawQuery("select * from group_table", null);
    }

    public void updateData(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, str2);
        readableDatabase.update(TABLE_NAME, contentValues, "ID = ?", new String[]{str});
    }

    public void deleteData(String str) {
        getReadableDatabase().delete(TABLE_NAME, "ID = ?", new String[]{str});
    }

    public void deleteAllChats() {
        getReadableDatabase().execSQL("DELETE FROM group_table");
    }
}
