package com.whatscan.toolkit.forwa.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayOutputStream;

public class ChatHistoryDatabaseHelper extends SQLiteOpenHelper {
    public static final String COL_1 = "ID";
    public static final String COL_2 = "PACKAGE";
    public static final String COL_3 = "DP";
    public static final String COL_4 = "NAME";
    public static final String COL_5 = "MSG";
    public static final String COL_6 = "TICKER";
    public static final String COL_7 = "DTTM";
    public static final String COL_8 = "STATE";
    public static final String DATABASE_NAME = "Student.db";
    public static final String TABLE_NAME = "student_table";

    public ChatHistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("create table student_table (ID INTEGER PRIMARY KEY AUTOINCREMENT,PACKAGE TEXT,DP BLOB,NAME TEXT,MSG TEXT,TICKER TEXT,DTTM TEXT,STATE BOOLEAN  default 0  NOT NULL)");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS student_table");
        onCreate(sQLiteDatabase);
    }

    public boolean insertData(String str, ByteArrayOutputStream byteArrayOutputStream, String str2, String str3, String str4, String str5, Boolean bool) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, str);
        contentValues.put(COL_3, byteArrayOutputStream.toByteArray());
        contentValues.put(COL_4, str2);
        contentValues.put(COL_5, str3);
        contentValues.put(COL_6, str4);
        contentValues.put(COL_7, str5);
        contentValues.put(COL_8, bool);
        return readableDatabase.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public Cursor getAllData(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        return readableDatabase.rawQuery("select * from student_table where PACKAGE = '" + str + "'", null);
    }

    public Cursor getAllTxtMsg(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        String replaceAll = str.replaceAll("'", "''");
        return readableDatabase.rawQuery(" select MSG,DTTM from student_table where PACKAGE = '" + str2 + "' and " + COL_4 + " = '" + replaceAll + "'", null);
    }

    public Cursor getTicker(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        return readableDatabase.rawQuery(" select DTTM,TICKER from student_table where PACKAGE = '" + str2 + "' and " + COL_4 + " = '" + str + "'", null);
    }

    public boolean updateData(String str, ByteArrayOutputStream byteArrayOutputStream, String str2, String str3, String str4, Boolean bool) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, str);
        contentValues.put(COL_3, byteArrayOutputStream.toByteArray());
        contentValues.put(COL_4, str2);
        contentValues.put(COL_5, str3);
        contentValues.put(COL_6, str4);
        contentValues.put(COL_8, bool);
        readableDatabase.update(TABLE_NAME, contentValues, "ID = ?", new String[0]);
        return true;
    }

    public Integer deleteData(String str) {
        return Integer.valueOf(getReadableDatabase().delete(TABLE_NAME, "ID = ?", new String[]{str}));
    }

    public void deleteAllTxt(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        readableDatabase.execSQL("DELETE FROM student_table WHERE PACKAGE = '" + str2 + "' and " + COL_4 + " = '" + str + "'");
    }

    public void deleteAllChats(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        readableDatabase.execSQL("DELETE FROM student_table WHERE PACKAGE = '" + str + "'");
    }

    public Cursor getLastPosition(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        return readableDatabase.rawQuery(" select * FROM student_table WHERE ID= (SELECT MAX(ID)  FROM student_table where PACKAGE = '" + str + "')", null);
    }

    public Cursor getLastTxt() {
        return getReadableDatabase().rawQuery(" SELECT MSG  FROM student_table WHERE ID= (SELECT MAX(ID)  FROM student_table)", null);
    }
}
