package com.whatscan.toolkit.forwa.DataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.whatscan.toolkit.forwa.GetSet.ChatModel;
import com.whatscan.toolkit.forwa.GetSet.NameModel;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_NAME = "CREATE TABLE chats(id INTEGER PRIMARY KEY, NAME TEXT unique, DATE TEXT,  LAST TEXT, CTIME DATETIME DEFAULT CURRENT_TIMESTAMP);";
    private static final String CREATE_TABLE_SMS = "CREATE TABLE msgs(id INTEGER PRIMARY KEY, NAME TEXT, MESSAGE TEXT, DATE TEXT);";
    private static final String DATABASE_NAME = "deleteMsg";
    private Context context;

    public DBHelper(Context context2) {
        super(context2, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE_NAME);
        sQLiteDatabase.execSQL(CREATE_TABLE_SMS);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS msgs");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS chats");
        onCreate(sQLiteDatabase);
    }

    public void saveUsers(String str, String str2, String str3, String str4) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor query = writableDatabase.query("chats", null, "NAME = ?", new String[]{str}, null, null, null);
        if (query.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("NAME", str);
            contentValues.put("DATE", str3);
            contentValues.put("LAST", str2);
            contentValues.put("CTIME", str4);
            writableDatabase.insert("chats", null, contentValues);
            addMessagedata(str2, str, str3);
        } else {
            String str5 = "";
            while (query.moveToNext()) {
                str5 = query.getString(query.getColumnIndex("LAST"));
            }
            if (!str5.equals(str2)) {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("DATE", str3);
                contentValues2.put("LAST", str2);
                contentValues2.put("CTIME", str4);
                writableDatabase.update("chats", contentValues2, "NAME=?", new String[]{str});
                addMessagedata(str2, str, str3);
            }
        }
        query.close();
        writableDatabase.close();
    }

    private void addMessagedata(String str, String str2, String str3) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MESSAGE", str);
        contentValues.put("NAME", str2);
        contentValues.put("DATE", str3);
        writableDatabase.insert("msgs", null, contentValues);
        writableDatabase.close();
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("updaterecycle"));
    }


    public List<NameModel> getNames() {
        ArrayList<NameModel> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor query = readableDatabase.query("chats", null, null, null, null, null, "CTIME DESC");
        while (query.moveToNext()) {
            arrayList.add(new NameModel(query.getString(1), query.getString(2), query.getString(3)));
        }
        query.close();
        readableDatabase.close();
        return arrayList;
    }

    public List<ChatModel> getChat(String str) {
        ArrayList<ChatModel> arrayList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor query = readableDatabase.query("msgs", null, "NAME = ?", new String[]{str}, null, null, null);
        while (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex("MESSAGE"));
            String string2 = query.getString(query.getColumnIndex("DATE"));
            arrayList.add(new ChatModel(string, string2));
        }
        query.close();
        readableDatabase.close();
        return arrayList;
    }
}